package com.fast.auth;

import java.util.UUID;

public final class PlayerData {

    private final UUID uuid;
    private final String username;
    private final String password;
    private final String ip;
    private final long registerDate;
    private long lastLogin;

    public PlayerData(UUID uuid, String username, String password, String ip,
                      long registerDate, long lastLogin) {
        this.uuid = uuid;
        this.username = username;
        this.password = password;
        this.ip = ip;
        this.registerDate = registerDate;
        this.lastLogin = lastLogin;
    }

    public UUID getUuid() { return uuid; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getIp() { return ip; }
    public long getRegisterDate() { return registerDate; }
    public long getLastLogin() { return lastLogin; }
    public void setLastLogin(long lastLogin) { this.lastLogin = lastLogin; }
}
