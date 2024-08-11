package me.gsqfi.fitask.fitask.api.playerdata;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.sql.PooledConnection;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Getter
public class MySQLData implements IPlayerData {
    private final MysqlConnectionPoolDataSource poolDataSource = new MysqlConnectionPoolDataSource();
    private final PooledConnection pooledConnection;
    private final Map<String, Map<UUID, LocalDateTime>> cache = new HashMap<>();

    @SneakyThrows
    public MySQLData(String url, String user, String password) {
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
                    "accept_time LONG NOT NULL," +
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
    public boolean accept(String playerName, UUID taskUid) {
        Map<UUID, LocalDateTime> uuids = this.cache.computeIfAbsent(playerName, k -> new HashMap<>());
        if (!uuids.containsKey(taskUid)) {
            LocalDateTime now = LocalDateTime.now();
            uuids.put(taskUid, now);
            //sql
            String sql = "INSERT INTO player_data (player_name, task_uuid, accept_time) VALUES (?,?,?)";
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
                        return false;
                    }
                    e.printStackTrace();
                    return false;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean abandon(String playerName, UUID taskUid) {
        if (this.cache.containsKey(playerName)) return false;
        if (this.cache.get(playerName).containsKey(taskUid)) {
            this.cache.get(playerName).remove(taskUid);
            //sql
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
                        return false;
                    }
                    e.printStackTrace();
                    return false;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }

    @Override
    public Map<UUID, LocalDateTime> getAllAcceptedTasks(String playerName) {
        return this.cache.getOrDefault(playerName, new HashMap<>());
    }

    @Override
    public boolean isAccept(String playerName, UUID taskUid) {
        return this.cache.getOrDefault(playerName, new HashMap<>()).containsKey(taskUid);
    }

    @Override
    public LocalDateTime getAcceptTime(String playerName, UUID taskUid) {
        return this.cache.get(playerName).get(taskUid);
    }

    @Override
    public void load(String playerName) {
        String sql = "SELECT task_uuid, accept_time FROM player_data WHERE player_name = ?";
        try (
                Connection conn = getConnection();
                PreparedStatement prepared = conn.prepareStatement(sql)
        ) {
            prepared.setString(1, playerName);
            try (ResultSet rs = prepared.executeQuery()) {
                if (rs.next()) {
                    this.cache.computeIfAbsent(playerName, k -> new HashMap<>())
                            .put(UUID.fromString(rs.getString("task_uuid")),
                                    LocalDateTime.ofInstant(Instant.ofEpochMilli(rs.getLong("accept_time")), ZoneId.systemDefault()));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void release(String playerName) {
        this.cache.clear();
    }

    @SneakyThrows
    @Override
    public void close() {
        this.cache.clear();
        this.pooledConnection.close();
    }
}
