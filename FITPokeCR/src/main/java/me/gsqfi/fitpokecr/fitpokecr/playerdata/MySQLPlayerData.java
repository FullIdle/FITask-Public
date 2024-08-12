package me.gsqfi.fitpokecr.fitpokecr.playerdata;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import lombok.Getter;
import lombok.SneakyThrows;
import me.gsqfi.fitask.fitask.api.taskcomponent.conditions.ICondition;

import javax.sql.PooledConnection;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class MySQLPlayerData implements IPlayerData {
    private final MysqlConnectionPoolDataSource poolDataSource = new MysqlConnectionPoolDataSource();
    private final PooledConnection pooledConnection;
    private final Map<String, Map<UUID, Map<Class<? extends ICondition>, String>>> cache = new HashMap<>();

    @SneakyThrows
    public MySQLPlayerData(String url, String user, String password) {
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
                    "conditions VARCHAR(255) NOT NULL," +
                    "value DATETIME NOT NULL, " +
                    "UNIQUE (player_name, task_uuid, conditions))";
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
    public String getPlayerTaskCondition(String playerName, UUID taskUid, Class<? extends ICondition> condition) {
        Map<UUID, Map<Class<? extends ICondition>, String>> map = this.cache.get(playerName);
        if (map.containsKey(taskUid)) {
            return null;
        }
        return map.get(taskUid).get(condition);
    }

    @Override
    public void setPlayerTaskCondition(String playerName, UUID taskUid, Class<? extends ICondition> condition, String value) {
        this.cache.get(playerName).computeIfAbsent(taskUid, k -> new HashMap<>()).put(condition, value);
        String sql = "INSERT INTO player_data (player_name, task_uuid, conditions, value) VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE value = VALUES(value)";
        try (Connection connection = this.getConnection();) {
            connection.setAutoCommit(false);
            try (PreparedStatement prepared = connection.prepareStatement(sql)) {
                prepared.setString(1, playerName);
                prepared.setString(2, taskUid.toString());
                prepared.setString(3, condition.getName());
                prepared.setString(4, value);
                prepared.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    e.printStackTrace();
                }
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removePlayerTaskCondition(String playerName, UUID taskUid, Class<? extends ICondition> condition) {
        Map<UUID, Map<Class<? extends ICondition>, String>> map = this.cache.get(playerName);
        if (!map.containsKey(taskUid)) {
            return;
        }
        map.remove(taskUid);
        String sql = "DELETE FROM player_data WHERE player_name = ? AND task_uuid = ? AND conditions = ?";
        try (Connection connection = this.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement prepared = connection.prepareStatement(sql)) {
                prepared.setString(1, playerName);
                prepared.setString(2, taskUid.toString());
                prepared.setString(3, condition.getName());
                prepared.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
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

    @Override
    public void removePlayerTask(String playerName, UUID taskUid) {
        Map<UUID, Map<Class<? extends ICondition>, String>> map = this.cache.get(playerName);
        if (!map.containsKey(taskUid)) {
            return;
        }
        map.remove(taskUid);
        String sql = "DELETE FROM player_data WHERE player_name = ? AND task_uuid = ?";
        try (Connection connection = this.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement prepared = connection.prepareStatement(sql)) {
                prepared.setString(1, playerName);
                prepared.setString(2, taskUid.toString());
                prepared.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
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

    @SneakyThrows
    @Override
    public void load(String playerName) {
        String sql = "SELECT task_uuid, conditions, value FROM player_data WHERE player_name = ?";
        try (
                Connection conn = getConnection();
                PreparedStatement prepared = conn.prepareStatement(sql)
        ) {
            prepared.setString(1, playerName);
            try (ResultSet rs = prepared.executeQuery()) {
                while (rs.next()) {
                    String value = rs.getString("value");
                    this.cache.computeIfAbsent(playerName, k -> new HashMap<>())
                            .computeIfAbsent(
                                    UUID.fromString(rs.getString("task_uuid")),
                                    k -> new HashMap<>())
                            .put(
                                    (Class<? extends ICondition>) Class.forName(rs.getString("conditions")),
                                    value
                            );
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

    @Override
    public void close() {
        this.cache.clear();
    }
}
