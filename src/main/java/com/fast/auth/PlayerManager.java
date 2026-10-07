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

    public PlayerManager(FastttAuth plugin) {
        this.plugin = plugin;
        this.loggedInPlayers = new HashMap<>();
        this.frozenLocations = new HashMap<>();
    }

    /**
     * Set player logged in status
     */
    public void setLoggedIn(Player player, boolean loggedIn) {
        loggedInPlayers.put(player.getUniqueId(), loggedIn);
    }

    /**
     * Check if player is logged in
     */
    public boolean isLoggedIn(Player player) {
        return loggedInPlayers.getOrDefault(player.getUniqueId(), false);
    }

    /**
     * Freeze player at their current location
     */
    public void freezePlayer(Player player) {
        frozenLocations.put(player.getUniqueId(), player.getLocation().clone());
    }

    /**
     * Get frozen location for a player
     */
    public Location getFrozenLocation(Player player) {
        return frozenLocations.get(player.getUniqueId());
    }

    /**
     * Clear player data when they leave
     */
    public void clearPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        loggedInPlayers.remove(uuid);
        frozenLocations.remove(uuid);
    }
}
