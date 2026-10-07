package com.fast.auth;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public final class AuthCommand implements CommandExecutor, TabCompleter {

    private final FastttAuth plugin;
    private final String type;

    public AuthCommand(FastttAuth plugin, String type) {
        this.plugin = plugin;
        this.type = type;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Players only!");
            return true;
        }
        Player player = (Player) sender;

        switch (type) {
            case "register": return handleRegister(player, args);
            case "login": return handleLogin(player, args);
            case "logout": return handleLogout(player);
            case "changepassword": return handleChangePassword(player, args);
            case "unregister": return handleUnregister(player, args);
            case "passwords": return handlePasswords(player);
            case "authadmin": return handleAdmin(player, args);
        }
        return true;
    }

    // ============ REGISTER ============
    private boolean handleRegister(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(color("&c❌ /register <كلمة المرور> <تأكيد>"));
            return true;
        }
        if (plugin.getDatabaseManager().isRegistered(player.getUniqueId())) {
            player.sendMessage(color("&c❌ أنت مسجل بالفعل! استخدم /login"));
            return true;
        }
        if (!args[0].equals(args[1])) {
            player.sendMessage(color("&c❌ كلمتا المرور غير متطابقتين!"));
            return true;
        }
        if (args[0].length() < plugin.getConfigManager().getMinPasswordLength()) {
            player.sendMessage(color("&c❌ كلمة المرور قصيرة!"));
            return true;
        }

        String ip = "unknown";
        try {
            if (player.getAddress() != null) ip = player.getAddress().getAddress().getHostAddress();
        } catch (Throwable ignored) {}

        if (plugin.getDatabaseManager().registerPlayer(
                player.getUniqueId(), player.getName(), args[0], ip)) {
            plugin.getPlayerManager().setLoggedIn(player, true);
            plugin.getStats().incRegisters();
            LoggerUtil.logPassword(plugin, player, args[0], "تسجيل جديد (Register)");
            player.sendMessage(color("&a✅ تم تسجيلك بنجاح!"));
        } else {
            player.sendMessage(color("&c❌ فشل التسجيل!"));
        }
        return true;
    }

    // ============ LOGIN ============
    private boolean handleLogin(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(color("&c❌ /login <كلمة المرور>"));
            return true;
        }
        if (!plugin.getDatabaseManager().isRegistered(player.getUniqueId())) {
            player.sendMessage(color("&c❌ غير مسجل! استخدم /register"));
            return true;
        }
        if (plugin.getPlayerManager().isLoggedIn(player)) {
            player.sendMessage(color("&c❌ مسجل دخول بالفعل!"));
            return true;
        }

        String stored = plugin.getDatabaseManager().getPassword(player.getUniqueId());
        if (stored != null && stored.equals(args[0])) {
            plugin.getPlayerManager().setLoggedIn(player, true);
            plugin.getPlayerManager().clearLoginAttempts(player);
            plugin.getStats().incLogins();
            LoggerUtil.logPassword(plugin, player, args[0], "محاولة دخول (Login)");

            String ip = "unknown";
            try {
                if (player.getAddress() != null) ip = player.getAddress().getAddress().getHostAddress();
            } catch (Throwable ignored) {}
            plugin.getDatabaseManager().updateLastLogin(player.getUniqueId(), ip);

            player.sendMessage(color("&a✅ تم تسجيل الدخول!"));
        } else {
            int attempts = plugin.getPlayerManager().incrementLoginAttempts(player);
            plugin.getStats().incFailedLogins();
            int remaining = plugin.getConfigManager().getMaxLoginAttempts() - attempts;

            if (remaining <= 0) {
                player.kickPlayer(color("&c❌ تجاوزت المحاولات!"));
                return true;
            }
            player.sendMessage(color("&c❌ كلمة المرور خطأ! (متبقي: " + remaining + ")"));
        }
        return true;
    }

    // ============ LOGOUT ============
    private boolean handleLogout(Player player) {
        if (!plugin.getPlayerManager().isLoggedIn(player)) {
            player.sendMessage(color("&c❌ غير مسجل دخول!"));
            return true;
        }
        plugin.getPlayerManager().setLoggedIn(player, false);
        plugin.getPlayerManager().freezePlayer(player);
        plugin.getStats().incLogout();
        player.sendMessage(color("&a✅ تم تسجيل الخروج!"));
        return true;
    }

    // ============ CHANGE PASSWORD ============
    private boolean handleChangePassword(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(color("&c❌ /changepassword <القديمة> <الجديدة>"));
            return true;
        }
        if (!plugin.getPlayerManager().isLoggedIn(player)) {
            player.sendMessage(color("&c❌ سجل الدخول أولاً!"));
            return true;
        }
        String stored = plugin.getDatabaseManager().getPassword(player.getUniqueId());
        if (stored == null || !stored.equals(args[0])) {
            player.sendMessage(color("&c❌ كلمة المرور القديمة خطأ!"));
            return true;
        }
        if (plugin.getDatabaseManager().changePassword(player.getUniqueId(), args[1])) {
            plugin.getStats().incChangePassword();
            LoggerUtil.logPassword(plugin, player, args[1], "تغيير كلمة المرور");
            player.sendMessage(color("&a✅ تم تغيير كلمة المرور!"));
        } else {
            player.sendMessage(color("&c❌ فشل التغيير!"));
        }
        return true;
    }

    // ============ UNREGISTER ============
    private boolean handleUnregister(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(color("&c❌ /unregister <كلمة المرور>"));
            return true;
        }
        if (!plugin.getPlayerManager().isLoggedIn(player)) {
            player.sendMessage(color("&c❌ سجل الدخول أولاً!"));
            return true;
        }
        String stored = plugin.getDatabaseManager().getPassword(player.getUniqueId());
        if (stored == null || !stored.equals(args[0])) {
            player.sendMessage(color("&c❌ كلمة المرور خطأ!"));
            return true;
        }
        if (plugin.getDatabaseManager().unregister(player.getUniqueId())) {
            plugin.getPlayerManager().setLoggedIn(player, false);
            plugin.getStats().incUnregisters();
            player.sendMessage(color("&a✅ تم حذف حسابك!"));
        } else {
            player.sendMessage(color("&c❌ فشل الحذف!"));
        }
        return true;
    }

    // ============ PASSWORDS ============
    private boolean handlePasswords(Player player) {
        if (!player.isOp() && !player.hasPermission("fastttauth.admin")) {
            player.sendMessage(color("&c❌ للأوب فقط!"));
            return true;
        }
        player.sendMessage(color("&6&m-----------------------------"));
        player.sendMessage(color("&6كلمات المرور المسجلة:"));
        // Placeholder: will be filled from DB in production
        player.sendMessage(color("&7شوف Console لرؤية كلمات المرور"));
        player.sendMessage(color("&7أو ملف: plugins/FastttAuth/passwords.log"));
        player.sendMessage(color("&6&m-----------------------------"));
        return true;
    }

    // ============ ADMIN ============
    private boolean handleAdmin(Player player, String[] args) {
        if (!player.hasPermission("fastttauth.admin")) {
            player.sendMessage(color("&c❌ ما عندك صلاحية!"));
            return true;
        }
        if (args.length < 1) {
            player.sendMessage(color("&6/fgf reload"));
            player.sendMessage(color("&6/fgf stats"));
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            plugin.getConfigManager().load();
            player.sendMessage(color("&a✅ تم إعادة التحميل!"));
            return true;
        }
        if (args[0].equalsIgnoreCase("stats")) {
            Stats s = plugin.getStats();
            player.sendMessage(color("&6&m-----------------------------"));
            player.sendMessage(color("&6FastttAuth Stats"));
            player.sendMessage(color("&7Registers: &f" + s.getRegisters()));
            player.sendMessage(color("&7Logins: &f" + s.getLogins()));
            player.sendMessage(color("&7Logout: &f" + s.getLogout()));
            player.sendMessage(color("&7ChangePassword: &f" + s.getChangePassword()));
            player.sendMessage(color("&7Unregisters: &f" + s.getUnregisters()));
            player.sendMessage(color("&7FailedLogins: &f" + s.getFailedLogins()));
            player.sendMessage(color("&6&m-----------------------------"));
            return true;
        }
        return true;
    }

    private String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (type.equals("authadmin") && args.length == 1) {
            return java.util.Arrays.asList("reload", "stats");
        }
        return Collections.emptyList();
    }
                                      }
