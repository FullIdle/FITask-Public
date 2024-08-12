package me.gsqfi.fitaskgui.fitaskgui.api.playerdata;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.sql.PooledConnection;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class MySQLPlayerLastCompleteData implements IPlayerLastCompleteData{
    private final MysqlConnectionPoolDataSource poolDataSource = new MysqlConnectionPoolDataSource();
    private final PooledConnection pooledConnection;
    private final Map<String, Map<UUID, LocalDateTime>> cache = new HashMap<>();

    @SneakyThrows
    public MySQLPlayerLastCompleteData(String url, String user, String password) {
        poolDataSource.setURL(url);
        poolDataSource.setUser(user);
        poolDataSource.setPassword(password);
        poolDataSource.setUseUnicode(true);
        poolDataSource.setCharacterEncoding("utf-8");
        poolDataSource.setAutoReconnect(true);
        this.pooledConnection = poolDataSource.getPooledConnection();
        this.verify();
    }

    private void verify() {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            String sql = "CREATE TABLE IF NOT EXISTS player_data ( " +
                    "player_name VARCHAR(255) NOT NULL, " +
                    "task_uuid VARCHAR(255) NOT NULL, " +
                    "completion_time LONG NOT NULL," +
                    "UNIQUE (player_name, task_uuid))";
            try (
                    Statement statement = conn.createStatement()
            ) {
                statement.executeUpdate(sql);
                conn.commit();
            } catch (SQLException e) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return this.pooledConnection.getConnection();
    }

    @Override
    public void completeTask(String playerName, UUID taskUid) {
        Map<UUID, LocalDateTime> uuids = this.cache.computeIfAbsent(playerName, k -> new HashMap<>());
        if (!uuids.containsKey(taskUid)) {
            LocalDateTime now = LocalDateTime.now();
            uuids.put(taskUid, now);
            String sql = "INSERT INTO player_data (player_name, task_uuid, completion_time) VALUES (?,?,?) "+
                    "ON DUPLICATE KEY UPDATE completion_time = VALUES(completion_time)";
            try (
                    Connection conn = getConnection();
            ) {
                conn.setAutoCommit(false);
                try (PreparedStatement prepared = conn.prepareStatement(sql)) {
                    prepared.setString(1, playerName);
                    prepared.setString(2, taskUid.toString());
                    prepared.setLong(3, now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                    prepared.executeUpdate();
                    conn.commit();
                } catch (SQLException e) {
                    try {
                        conn.rollback();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        return;
                    }
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeTask(String playerName, UUID taskUid) {
        if (this.cache.containsKey(playerName)) return;
        if (this.hasCompleteTask(playerName, taskUid)) {
            this.cache.get(playerName).remove(taskUid);
            String sql = "DELETE FROM player_data WHERE player_name = ? AND task_uuid = ?";
            try (Connection conn = getConnection();) {
                conn.setAutoCommit(false);
                try (PreparedStatement prepared = conn.prepareStatement(sql)) {
                    prepared.setString(1, playerName);
                    prepared.setString(2, taskUid.toString());
                    prepared.executeUpdate();
                    conn.commit();
                } catch (SQLException e) {
                    try {

                        conn.rollback();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        return;
                    }
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public LocalDateTime getLastCompleteTaskTime(String playerName, UUID taskUid) {
        return this.cache.getOrDefault(playerName,new HashMap<>()).get(taskUid);
    }

    @Override
    public boolean hasCompleteTask(String playerName, UUID taskUid) {
        return this.cache.getOrDefault(playerName,new HashMap<>()).containsKey(taskUid);
    }

    @Override
    public void load(String playerName) {
        String sql = "SELECT task_uuid, completion_time FROM player_data WHERE player_name = ?";
        try (
                Connection conn = getConnection();
                PreparedStatement prepared = conn.prepareStatement(sql)
        ) {
            prepared.setString(1, playerName);
            try (ResultSet rs = prepared.executeQuery()) {
                if (rs.next()) {
                    this.cache.computeIfAbsent(playerName, k -> new HashMap<>())
                            .put(UUID.fromString(rs.getString("task_uuid")),
                                    LocalDateTime.ofInstant(Instant.ofEpochMilli(rs.getLong("completion_time")), ZoneId.systemDefault()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void release(String playerName) {
        this.cache.remove(playerName);
    }

    @SneakyThrows
    @Override
    public void close() {
        this.cache.clear();
        this.pooledConnection.close();
    }
}
