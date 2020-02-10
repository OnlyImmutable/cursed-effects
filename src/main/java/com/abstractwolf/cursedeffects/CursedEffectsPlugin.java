package com.abstractwolf.cursedeffects;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.abstractwolf.cursedeffects.database.LocalFileDatabase;
import com.abstractwolf.cursedeffects.database.MySqlDatabase;
import com.abstractwolf.cursedeffects.listener.DataListener;
import com.abstractwolf.cursedeffects.manager.EffectManager;
import com.abstractwolf.cursedeffects.manager.UserManager;
import com.abstractwolf.cursedeffects.messages.MessageManager;
import com.abstractwolf.cursedeffects.utils.command.CommandManager;
import com.abstractwolf.cursedeffects.utils.menu.MenuManager;

public class CursedEffectsPlugin extends JavaPlugin
{

    private static CursedEffectsPlugin plugin;

    private UserManager userManager;
    private EffectManager effectManager;
    private MessageManager messageManager;
    private CommandManager commandManager;

    private MySqlDatabase database;
    private LocalFileDatabase flatfileDatabase;

    private boolean useFlatfile;

    @Override
    public void onEnable()
    {

        plugin = this;

        saveResource("config.yml", false);
        saveResource("messages.yml", false);

        final FileConfiguration config = getConfig();

        useFlatfile = config.getBoolean("settings.flatfile");

        if (isUseFlatfile())
        {

            flatfileDatabase = new LocalFileDatabase();

        }
        else
        {

            database = new MySqlDatabase(config.getString("database.host"), config.getInt("database.port"), config.getString("database.database"), config.getString("database.username"), config.getString("database.password"));
            database.openConnection();

        }

        userManager = new UserManager();
        messageManager = new MessageManager();
        commandManager = new CommandManager();
        commandManager.registerCommands();

        setupTables();

        Bukkit.getPluginManager().registerEvents(new DataListener(), this);
        Bukkit.getPluginManager().registerEvents(new MenuManager(), this);

        Bukkit.getOnlinePlayers().forEach(user ->
        {

            CursedEffectsPlugin.getPlugin().getUserManager().addToCache(user.getUniqueId());
            CursedEffectsPlugin.getPlugin().getUserManager().getUserData(user.getUniqueId()).load();

        });

        if (config.getConfigurationSection("particles") == null
                || config.getConfigurationSection("sounds") == null
                || config.getConfigurationSection("trails") == null)
        {

            if (config.getConfigurationSection("particles") == null)
            {

                for (final Particle particle : Particle.values())
                {

                    if (particle == Particle.BLOCK_CRACK
                            || particle == Particle.BLOCK_DUST
                            || particle == Particle.ITEM_CRACK
                            || particle == Particle.LEGACY_BLOCK_CRACK
                            || particle == Particle.LEGACY_BLOCK_DUST
                            || particle == Particle.LEGACY_FALLING_DUST
                            || particle == Particle.BARRIER
                            || particle == Particle.MOB_APPEARANCE
                            || particle == Particle.EXPLOSION_HUGE
                            || particle == Particle.EXPLOSION_LARGE
                            )
                    {

                        continue;

                    }

                    config.set("particles." + particle.name() + ".amount", 25);
                    config.set("particles." + particle.name() + ".speed", 0.10);
                    config.set("particles." + particle.name() + ".permission", "cursedeffects.particle." + particle.name());
                    config.set("particles." + particle.name() + ".icon.material", Material.GLASS.name());
                    config.set("particles." + particle.name() + ".icon.data", (byte) 0);
                    saveConfig();

                }

            }

            if (config.getConfigurationSection("sounds") == null)
            {

                for (final Sound sound : Sound.values())
                {

                    config.set("sounds." + sound.name() + ".volume", 1);
                    config.set("sounds." + sound.name() + ".pitch", 1);
                    config.set("sounds." + sound.name() + ".permission", "cursedeffects.sound." + sound.name());
                    config.set("sounds." + sound.name() + ".icon.material", Material.GLASS.name());
                    config.set("sounds." + sound.name() + ".icon.data", (byte) 0);
                    saveConfig();

                }

            }

            if (config.getConfigurationSection("trails") == null)
            {

                for (final Particle particle : Particle.values())
                {

                    if (particle == Particle.BLOCK_CRACK
                            || particle == Particle.BLOCK_DUST
                            || particle == Particle.ITEM_CRACK
                            || particle == Particle.LEGACY_BLOCK_CRACK
                            || particle == Particle.LEGACY_BLOCK_DUST
                            || particle == Particle.LEGACY_FALLING_DUST
                            || particle == Particle.BARRIER
                            || particle == Particle.MOB_APPEARANCE
                            || particle == Particle.EXPLOSION_HUGE
                            || particle == Particle.EXPLOSION_LARGE
                            )
                    {

                        continue;

                    }

                    config.set("trails." + particle.name() + ".amount", 1);
                    config.set("trails." + particle.name() + ".speed", 0.05);
                    config.set("trails." + particle.name() + ".permission", "cursedeffects.trail." + particle.name());
                    config.set("trails." + particle.name() + ".icon.material", Material.GLASS.name());
                    config.set("trails." + particle.name() + ".icon.data", (byte) 0);
                    config.set("trails." + particle.name() + ".offsetX", 0.2);
                    config.set("trails." + particle.name() + ".offsetY", 0);
                    config.set("trails." + particle.name() + ".offsetZ", 0.2);
                    saveConfig();

                }

            }

        }

        effectManager = new EffectManager();

        System.out.println("CursedEffects by ThatAbstractWolf enabled.");

    }

    @Override
    public void onDisable()
    {

        Bukkit.getOnlinePlayers().forEach(player ->
        {

            getUserManager().getUserData(player.getUniqueId()).save(true);
            getUserManager().removeFromCache(player.getUniqueId());

        });

        if (!isUseFlatfile())
        {

            database.closePool();

        }

        System.out.println("CursedEffects by ThatAbstractWolf disabled.");

    }

    public UserManager getUserManager()
    {

        return userManager;

    }

    public EffectManager getEffectManager()
    {

        return effectManager;

    }

    public MessageManager getMessageManager()
    {

        return messageManager;

    }

    public CommandManager getCommandManager()
    {

        return commandManager;

    }

    public MySqlDatabase getDatabase()
    {

        return database;

    }

    public LocalFileDatabase getFlatfileDatabase()
    {

        return flatfileDatabase;

    }

    public static CursedEffectsPlugin getPlugin()
    {

        return plugin;

    }

    private void setupTables()
    {

        if (database == null)
        {

            return;

        }

        /**
         * users: uuid: (UNIQUE) particle: (VARCHAR) sound: (VARCHAR)
         */

        // Setup the User data table.
        database.sendPreparedStatement("CREATE TABLE IF NOT EXISTS cursedeffects (\n" +
                "    uuid VARCHAR(40) NOT NULL,\n" +
                "    particle VARCHAR(45),\n" +
                "    sound VARCHAR(45),\n" +
                "    trail VARCHAR(45),\n" +
                "    PRIMARY KEY (uuid)\n" +
                ");", false, true, (statement) ->
        {

        });

    }

    public boolean isUseFlatfile()
    {

        return useFlatfile;

    }

}
