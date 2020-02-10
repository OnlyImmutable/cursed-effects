package com.abstractwolf.cursedeffects.utils;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.abstractwolf.cursedeffects.CursedEffectsPlugin;
import com.abstractwolf.cursedeffects.utils.placeholder.Placeholder;
import com.abstractwolf.cursedeffects.utils.placeholder.PlaceholderManager;

public class Message
{

    public static void sendMessageToPlayer(Player player, String message)
    {

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));

    }

    public static void sendMessage(Player player, String key)
    {

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', CursedEffectsPlugin.getPlugin().getMessageManager().getMessage(key)));

    }

    public static void sendMessage(Player player, String key, List<Placeholder> placeholders)
    {

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', PlaceholderManager.replaceCustomPlaceholders(CursedEffectsPlugin.getPlugin().getMessageManager().getMessage(key), placeholders)));

    }

}
