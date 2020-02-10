package com.abstractwolf.cursedeffects.database;

import com.abstractwolf.cursedeffects.CursedEffectsPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LocalFileDatabase
{

    private File file;
    private FileConfiguration fileConfiguration;

    public LocalFileDatabase()
    {

        this.file = new File(CursedEffectsPlugin.getPlugin().getDataFolder(), "data.yml");
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);

    }

    public void updateConfig()
    {

        try
        {

            fileConfiguration.save(file);

        }
        catch (IOException e)
        {

            e.printStackTrace();

        }

    }

    public File getFile()
    {

        return file;

    }

    public FileConfiguration getFileConfiguration()
    {

        return fileConfiguration;

    }

}
