package com.fast.auth;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public final class AuthListener implements Listener {

    private final FastttAuth plugin;

    public AuthListener(FastttAuth plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getPlayerManager().setLoggedIn(player, false);
        plugin.getPlayerManager().freezePlayer(player);

        player.sendMessage(ChatColor.GOLD + "=================================");
        player.sendMessage(ChatColor.YELLOW + "  مرحباً " + player.getName());
        player.sendMessage(ChatColor.GOLD + "=================================");

        if (plugin.getDatabaseManager().isRegistered(player.getUniqueId())) {
            player.sendMessage(ChatColor.GREEN + "📝 سجل الدخول: /login <كلمة المرور>");
        } else {
            player.sendMessage(ChatColor.RED + "📝 سجل: /register <كلمة المرور> <تأكيد>");
        }
        player.sendMessage(ChatColor.GOLD + "=================================");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getPlayerManager().clearPlayer(event.getPlayer());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (plugin.getPlayerManager().isLoggedIn(player)) return;

        if (!plugin.getConfigManager().isFreezePlayer()) return;

        Location frozen = plugin.getPlayerManager().getFrozenLocation(player);
        if (frozen == null) return;

        Location to = event.getTo();
        if (to.getX() != frozen.getX() || to.getZ() != frozen.getZ()) {
            event.setTo(frozen);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (plugin.getPlayerManager().isLoggedIn(player)) return;
        if (!plugin.getConfigManager().isBlockChat()) return;

        event.setCancelled(true);
        player.sendMessage(ChatColor.RED + "❌ يجب تسجيل الدخول أولاً!");
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (plugin.getPlayerManager().isLoggedIn(player)) return;
        if (!plugin.getConfigManager().isBlockCommands()) return;

        String cmd = event.getMessage().toLowerCase();
        if (cmd.startsWith("/login") ||
            cmd.startsWith("/register") ||
            cmd.startsWith("/l ") ||
            cmd.startsWith("/reg ")) {
            return;
        }

        event.setCancelled(true);
        player.sendMessage(ChatColor.RED + "❌ يجب تسجيل الدخول أولاً!");
    }
}
