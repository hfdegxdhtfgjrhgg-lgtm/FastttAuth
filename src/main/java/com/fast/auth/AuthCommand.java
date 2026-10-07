package com.fast.auth;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Collections;
import java.util.List;

public final class AuthCommand implements CommandExecutor, TabCompleter {

    private final FastttAuth plugin;
    private final PlayerManager playerManager;

    public AuthCommand(FastttAuth plugin, PlayerManager playerManager) {
        this.plugin = plugin;
        this.playerManager = playerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof org.bukkit.entity.Player)) {
            sender.sendMessage("§cThis command can only be used by players.");
            return true;
        }

        org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
        String cmd = command.getName().toLowerCase();

        if ("login".equals(cmd)) {
            if (playerManager.isLoggedIn(player)) {
                player.sendMessage("§aYou are already logged in.");
                return true;
            }
            player.sendMessage("§eUse /login <password>");
            return true;
        }

        if ("register".equals(cmd)) {
            if (playerManager.isLoggedIn(player)) {
                player.sendMessage("§aYou are already logged in.");
                return true;
            }
            player.sendMessage("§eUse /register <password> <confirm>");
            return true;
        }

        player.sendMessage("§cUnknown command.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
