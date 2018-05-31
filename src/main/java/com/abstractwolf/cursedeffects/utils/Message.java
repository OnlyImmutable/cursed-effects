package com.abstractwolf.cursedeffects.utils;

import com.abstractwolf.cursedeffects.CursedEffectsPlugin;
import com.abstractwolf.cursedeffects.utils.placeholder.Placeholder;
import com.abstractwolf.cursedeffects.utils.placeholder.PlaceholderManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class Message {

    public static void sendMessageToPlayer(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', CursedEffectsPlugin.getPlugin().getMessageManager().getMessage("prefix") + " &7" + message));
    }

    public static void sendMessage(Player player, String key) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', CursedEffectsPlugin.getPlugin().getMessageManager().getMessage("prefix") + " " + CursedEffectsPlugin.getPlugin().getMessageManager().getMessage(key)));
    }

    public static void sendMessage(Player player, String key, List<Placeholder> placeholders) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', CursedEffectsPlugin.getPlugin().getMessageManager().getMessage("prefix") + " " + PlaceholderManager.replaceCustomPlaceholders(CursedEffectsPlugin.getPlugin().getMessageManager().getMessage(key), placeholders)));
    }
}
