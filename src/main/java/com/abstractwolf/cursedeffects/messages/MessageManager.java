package com.abstractwolf.cursedeffects.messages;

import com.abstractwolf.cursedeffects.CursedEffectsPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessageManager
{

    private File messageFile;
    private FileConfiguration messageFileConfiguration;

    public MessageManager()
    {

        this.messageFile = new File(CursedEffectsPlugin.getPlugin().getDataFolder(), "messages.yml");
        this.messageFileConfiguration = YamlConfiguration.loadConfiguration(messageFile);

    }

    public File getMessageFile()
    {

        return messageFile;

    }

    public FileConfiguration getMessageFileConfiguration()
    {

        return messageFileConfiguration;

    }

    public String getMessage(String name)
    {

        return messageFileConfiguration.getString("messages." + name);

    }

}
