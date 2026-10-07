package com.fast.auth;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerManager {

    private final FastttAuth plugin;
    private final Map<UUID, Boolean> loggedInPlayers;
    private final Map<UUID, Location> frozenLocations;
    private final Map<UUID, Integer> loginAttempts;

    public PlayerManager(FastttAuth plugin) {
        this.plugin = plugin;
        this.loggedInPlayers = new HashMap<>();
        this.frozenLocations = new HashMap<>();
        this.loginAttempts = new HashMap<>();
    }

    public void setLoggedIn(Player player, boolean loggedIn) {
        loggedInPlayers.put(player.getUniqueId(), loggedIn);
    }

    public boolean isLoggedIn(Player player) {
        return loggedInPlayers.getOrDefault(player.getUniqueId(), false);
    }

    public void freezePlayer(Player player) {
        frozenLocations.put(player.getUniqueId(), player.getLocation().clone());
    }

    public Location getFrozenLocation(Player player) {
        return frozenLocations.get(player.getUniqueId());
    }

    public int incrementLoginAttempts(Player player) {
        UUID uuid = player.getUniqueId();
        int attempts = loginAttempts.getOrDefault(uuid, 0) + 1;
        loginAttempts.put(uuid, attempts);
        return attempts;
    }

    public void clearLoginAttempts(Player player) {
        loginAttempts.remove(player.getUniqueId());
    }

    public void clearPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        loggedInPlayers.remove(uuid);
        frozenLocations.remove(uuid);
        loginAttempts.remove(uuid);
    }
}
