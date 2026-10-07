package com.fast.auth;

import java.io.File;
import java.sql.*;
import java.util.UUID;

public final class DatabaseManager {

    private final FastttAuth plugin;
    private Connection connection;

    public DatabaseManager(FastttAuth plugin) {
        this.plugin = plugin;
    }

    public boolean connect() {
        try {
            if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();

            File dbFile = new File(plugin.getDataFolder(),
                    plugin.getConfigManager().getDatabaseFile());

            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());

            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS players (" +
                    "uuid TEXT PRIMARY KEY, " +
                    "username TEXT NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "register_date INTEGER, " +
                    "last_login INTEGER, " +
                    "last_ip TEXT)");
            stmt.close();
            return true;
        } catch (SQLException e) {
            plugin.getLogger().severe("DB connection failed: " + e.getMessage());
            return false;
        }
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {
            plugin.getLogger().warning("DB disconnect failed: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isRegistered(UUID uuid) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT uuid FROM players WHERE uuid = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            boolean exists = rs.next();
            rs.close();
            ps.close();
            return exists;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean registerPlayer(UUID uuid, String username, String password, String ip) {
        try {
            long now = System.currentTimeMillis();
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO players (uuid, username, password, register_date, last_login, last_ip) " +
                            "VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, uuid.toString());
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setLong(4, now);
            ps.setLong(5, now);
            ps.setString(6, ip);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException e) {
            plugin.getLogger().severe("Register failed: " + e.getMessage());
            return false;
        }
    }

    public String getPassword(UUID uuid) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT password FROM players WHERE uuid = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            String password = rs.next() ? rs.getString("password") : null;
            rs.close();
            ps.close();
            return password;
        } catch (SQLException e) {
            return null;
        }
    }

    public void updateLastLogin(UUID uuid, String ip) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE players SET last_login = ?, last_ip = ? WHERE uuid = ?");
            ps.setLong(1, System.currentTimeMillis());
            ps.setString(2, ip);
            ps.setString(3, uuid.toString());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ignored) {}
    }

    public boolean changePassword(UUID uuid, String newPassword) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE players SET password = ? WHERE uuid = ?");
            ps.setString(1, newPassword);
            ps.setString(2, uuid.toString());
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean unregister(UUID uuid) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM players WHERE uuid = ?");
            ps.setString(1, uuid.toString());
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}
