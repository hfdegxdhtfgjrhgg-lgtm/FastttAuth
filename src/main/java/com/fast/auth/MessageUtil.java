package com.fast.auth;

import org.bukkit.ChatColor;

public final class MessageUtil {

    private MessageUtil() {}

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text == null ? "" : text);
    }
}
