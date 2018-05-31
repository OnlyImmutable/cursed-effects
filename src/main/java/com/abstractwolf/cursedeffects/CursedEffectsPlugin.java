package com.abstractwolf.cursedeffects;

import com.abstractwolf.cursedeffects.database.MySqlDatabase;
import com.abstractwolf.cursedeffects.listener.DataListener;
import com.abstractwolf.cursedeffects.manager.EffectManager;
import com.abstractwolf.cursedeffects.manager.UserManager;
import com.abstractwolf.cursedeffects.messages.MessageManager;
import com.abstractwolf.cursedeffects.utils.command.CommandManager;
import com.abstractwolf.cursedeffects.utils.menu.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class CursedEffectsPlugin extends JavaPlugin {

    private static CursedEffectsPlugin plugin;

    private UserManager userManager;
    private EffectManager effectManager;
    private MessageManager messageManager;
    private CommandManager commandManager;

    private MySqlDatabase database;

    @Override
    public void onEnable() {

        plugin = this;

        saveResource("config.yml", false);
        saveResource("messages.yml", false);

        FileConfiguration config = getConfig();

        database = new MySqlDatabase(config.getString("database.host"), config.getInt("database.port"), config.getString("database.database"), config.getString("database.username"), config.getString("database.password"));
        database.openConnection();

        userManager = new UserManager();
        messageManager = new MessageManager();
        commandManager = new CommandManager();
        commandManager.registerCommands();

        setupTables();

        Bukkit.getPluginManager().registerEvents(new DataListener(), this);
        Bukkit.getPluginManager().registerEvents(new MenuManager(), this);

        Bukkit.getOnlinePlayers().forEach(user -> {
            CursedEffectsPlugin.getPlugin().getUserManager().addToCache(user.getUniqueId());
            CursedEffectsPlugin.getPlugin().getUserManager().getUserData(user.getUniqueId()).load();
        });

        if (config.getConfigurationSection("particles") == null && config.getConfigurationSection("sounds") == null) {

            for (Particle particle : Particle.values()) {
                config.set("particles." + particle.name() + ".amount", 5);
                config.set("particles." + particle.name() + ".speed", 0.25);
                config.set("particles." + particle.name() + ".icon.material", Material.GLASS.name());
                config.set("particles." + particle.name() + ".icon.data", (byte) 0);
                saveConfig();
            }

            for (Sound sound : Sound.values()) {
                config.set("sounds." + sound.name() + ".volume", 1.25);
                config.set("sounds." + sound.name() + ".pitch", 0.75);
                config.set("sounds." + sound.name() + ".icon.material", Material.GLASS.name());
                config.set("sounds." + sound.name() + ".icon.data", (byte) 0);
                saveConfig();
            }
        }

        effectManager = new EffectManager();

        System.out.println("Cursed Effects by ThatAbstractWolf enabled.");
    }

    @Override
    public void onDisable() {

        Bukkit.getOnlinePlayers().forEach(player -> {
            getUserManager().getUserData(player.getUniqueId()).save(true);
            getUserManager().removeFromCache(player.getUniqueId());
        });

        database.closePool();
        System.out.println("Cursed Effects by ThatAbstractWolf disabled.");
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public EffectManager getEffectManager() {
        return effectManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public MySqlDatabase getDatabase() {
        return database;
    }

    public static CursedEffectsPlugin getPlugin() {
        return plugin;
    }

    private void setupTables() {

        if (database == null) return;

        /**
         * users:
         *  uuid: (UNIQUE)
         *  particle: (VARCHAR)
         *  sound: (VARCHAR)
         */

        // Setup the User data table.
        database.sendPreparedStatement("CREATE TABLE IF NOT EXISTS cursedeffects (\n" +
                "    uuid VARCHAR(40) NOT NULL,\n" +
                "    particle VARCHAR(45),\n" +
                "    sound VARCHAR(45),\n" +
                "    PRIMARY KEY (uuid)\n" +
                ");", false, true, (statement) -> {});
    }
}
