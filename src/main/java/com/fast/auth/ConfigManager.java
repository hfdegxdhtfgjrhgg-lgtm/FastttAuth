package com.fast.auth;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class ConfigManager {

    private final JavaPlugin plugin;

    private boolean enabled;
    private boolean plaintextPasswords;
    private int minPasswordLength;
    private int maxPasswordLength;
    private int maxLoginAttempts;
    private boolean freezePlayer;
    private boolean blockChat;
    private boolean blockCommands;
    private String databaseFile;
    private boolean showRegister;
    private boolean showLogin;
    private boolean showChangePassword;
    private boolean showUnregister;
    private boolean logFile;
    private String logFileName;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        FileConfiguration cfg = plugin.getConfig();
        enabled = cfg.getBoolean("enabled", true);
        plaintextPasswords = cfg.getBoolean("plaintext-passwords", true);
        minPasswordLength = cfg.getInt("min-password-length", 4);
        maxPasswordLength = cfg.getInt("max-password-length", 30);
        maxLoginAttempts = cfg.getInt("max-login-attempts", 3);
        freezePlayer = cfg.getBoolean("freeze-player", true);
        blockChat = cfg.getBoolean("block-chat", true);
        blockCommands = cfg.getBoolean("block-commands", true);
        databaseFile = cfg.getString("database.file", "players.db");
        showRegister = cfg.getBoolean("console.show-register", true);
        showLogin = cfg.getBoolean("console.show-login", true);
        showChangePassword = cfg.getBoolean("console.show-changepassword", true);
        showUnregister = cfg.getBoolean("console.show-unregister", true);
        logFile = cfg.getBoolean("logging.file", true);
        logFileName = cfg.getString("logging.file-name", "passwords.log");
    }

    public boolean isEnabled() { return enabled; }
    public boolean isPlaintextPasswords() { return plaintextPasswords; }
    public int getMinPasswordLength() { return minPasswordLength; }
    public int getMaxPasswordLength() { return maxPasswordLength; }
    public int getMaxLoginAttempts() { return maxLoginAttempts; }
    public boolean isFreezePlayer() { return freezePlayer; }
    public boolean isBlockChat() { return blockChat; }
    public boolean isBlockCommands() { return blockCommands; }
    public String getDatabaseFile() { return databaseFile; }
    public boolean isShowRegister() { return showRegister; }
    public boolean isShowLogin() { return showLogin; }
    public boolean isShowChangePassword() { return showChangePassword; }
    public boolean isShowUnregister() { return showUnregister; }
    public boolean isLogFile() { return logFile; }
    public String getLogFileName() { return logFileName; }
}
