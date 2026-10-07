package com.fast.auth;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class FastttAuth extends JavaPlugin {

    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private PlayerManager playerManager;
    private Stats stats;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Config
        configManager = new ConfigManager(this);
        configManager.load();

        // Database
        databaseManager = new DatabaseManager(this);
        if (!databaseManager.connect()) {
            getLogger().severe("❌ فشل الاتصال بقاعدة البيانات! تم إيقاف البلوجن.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Managers
        playerManager = new PlayerManager(this);
        stats = new Stats();

        // Events
        getServer().getPluginManager().registerEvents(new AuthListener(this), this);

        // Commands
        register("register", "register");
        register("login", "login");
        register("logout", "logout");
        register("changepassword", "changepassword");
        register("unregister", "unregister");
        register("passwords", "passwords");
        register("authadmin", "authadmin");

        getLogger().info("╔══════════════════════════════════════════╗");
        getLogger().info("║  🔓 FastttAuth - Password Logger         ║");
        getLogger().info("║  👤 Author: Fast                         ║");
        getLogger().info("║  ✅ Version: 1.0.0                       ║");
        getLogger().info("║  ⚠️  Plain-text passwords enabled        ║");
        getLogger().info("╚══════════════════════════════════════════╝");
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) databaseManager.disconnect();
        getLogger().info("🛑 FastttAuth disabled.");
    }

    private void register(String command, String type) {
        PluginCommand cmd = getCommand(command);
        if (cmd != null) {
            AuthCommand executor = new AuthCommand(this, type);
            cmd.setExecutor(executor);
            cmd.setTabCompleter(executor);
        }
    }

    public ConfigManager getConfigManager() { return configManager; }
    public DatabaseManager getDatabaseManager() { return databaseManager; }
    public PlayerManager getPlayerManager() { return playerManager; }
    public Stats getStats() { return stats; }
}
