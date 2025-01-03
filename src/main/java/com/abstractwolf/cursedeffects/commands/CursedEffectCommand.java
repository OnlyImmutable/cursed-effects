package com.abstractwolf.cursedeffects.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.abstractwolf.cursedeffects.CursedEffectsPlugin;
import com.abstractwolf.cursedeffects.EffectType;
import com.abstractwolf.cursedeffects.gui.EffectSelectGui;
import com.abstractwolf.cursedeffects.manager.data.UserData;
import com.abstractwolf.cursedeffects.utils.Message;
import com.abstractwolf.cursedeffects.utils.ParticleUtil;
import com.abstractwolf.cursedeffects.utils.command.AbstractCommand;

public class CursedEffectCommand extends AbstractCommand
{

    public CursedEffectCommand()
    {

        super("cursedeffects", "cursedeffects.select");

    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {

        if (!(sender instanceof Player))
        {

            return;

        }

        final Player player = (Player) sender;

        if (args.length == 1)
        {

            if (args[0].equalsIgnoreCase("particles"))
            {

                new EffectSelectGui(player, 1, EffectType.PARTICLE);

            }
            else if (args[0].equalsIgnoreCase("sounds"))
            {

                new EffectSelectGui(player, 1, EffectType.SOUND);

            }
            else if (args[0].equalsIgnoreCase("trails"))
            {

                new EffectSelectGui(player, 1, EffectType.TRAIL);

            }
            else if (args[0].equalsIgnoreCase("test")
                    && player.hasPermission("cursedeffects.test"))
            {

                final UserData data = CursedEffectsPlugin.getPlugin().getUserManager().getUserData(player.getUniqueId());

                new BukkitRunnable()
                {

                    @Override
                    public void run()
                    {

                        if (data.getParticle() != null)
                        {

                            final int amount = CursedEffectsPlugin.getPlugin().getConfig().getInt("particles." + data.getParticle().name() + ".amount");
                            final float speed = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("particles." + data.getParticle().name() + ".speed");

                            ParticleUtil.sendParticle(player.getLocation(), data.getParticle(), amount, speed, 0, 0 , 0);

                        }

                        if (data.getSound() != null)
                        {

                            final float volume = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("sounds." + data.getSound().name() + ".volume");
                            final float pitch = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("sounds." + data.getSound().name() + ".pitch");

                            player.getWorld().playSound(player.getLocation(), data.getSound(), volume, pitch);

                        }

                        if (data.getParticleTrail() != null)
                        {

                            final int amount = CursedEffectsPlugin.getPlugin().getConfig().getInt("trails." + data.getParticleTrail().name() + ".amount");
                            final float speed = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("trails." + data.getParticleTrail().name() + ".speed");

                            ParticleUtil.sendParticle(player.getLocation(), data.getParticle(), amount, speed, 0, 0 , 0);

                        }

                        Message.sendMessageToPlayer(player, "Ran test sound, trail and particle..");

                    }

                }.runTaskLater(CursedEffectsPlugin.getPlugin(), 10);

            }
            else
            {

                Message.sendMessageToPlayer(player, "/cursedeffects [particles|trails|sounds]");

            }

        }
        else
        {

            Message.sendMessageToPlayer(player, "/cursedeffects [particles|trails|sounds]");

        }

    }

}
