package com.abstractwolf.cursedeffects.utils.command;

import java.lang.reflect.Field;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import com.abstractwolf.cursedeffects.utils.Message;
import com.abstractwolf.cursedeffects.utils.placeholder.Placeholder;

public abstract class AbstractCommand extends BukkitCommand
{

    private final String command;
    private final String permission;

    public AbstractCommand(String command, String permission)
    {

        super(command);
        this.command = command;
        this.permission = permission;
        this.register();

    }

    public abstract void execute(CommandSender sender, String[] args);

    public void register()
    {

        try
        {

            final Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            final CommandMap commandMap = (CommandMap) field.get(Bukkit.getServer());
            setPermission(permission);
            commandMap.register(this.command, this);

        }
        catch (final Exception var5)
        {

            var5.printStackTrace();

        }

    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args)
    {

        if (!(sender instanceof Player))
        {

            this.execute(sender, args);
            return false;

        }
        else
        {

            final Player player = (Player) sender;

            if (!player.hasPermission(permission))
            {

                Message.sendMessage(player, "noPermission", Collections.singletonList(new Placeholder("%permission%", permission)));
                return false;

            }
            else
            {

                this.execute(sender, args);
                return false;

            }

        }

    }

    public String getCommand()
    {

        return command;

    }

}
