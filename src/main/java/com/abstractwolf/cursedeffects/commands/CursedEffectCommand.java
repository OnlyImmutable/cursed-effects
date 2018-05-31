package com.abstractwolf.cursedeffects.commands;

import com.abstractwolf.cursedeffects.CursedEffectsPlugin;
import com.abstractwolf.cursedeffects.gui.EffectSelectGui;
import com.abstractwolf.cursedeffects.manager.data.UserData;
import com.abstractwolf.cursedeffects.utils.Message;
import com.abstractwolf.cursedeffects.utils.ParticleUtil;
import com.abstractwolf.cursedeffects.utils.command.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CursedEffectCommand extends AbstractCommand {

    public CursedEffectCommand() {
        super("cursedeffects", "cursedeffects.select");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) {
            return;
        }

        Player player = (Player) sender;

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("particles")) {
                new EffectSelectGui(player, 1, true);
            } else if (args[0].equalsIgnoreCase("sounds")) {
                new EffectSelectGui(player, 1, false);
            } else if (args[0].equalsIgnoreCase("test") && player.isOp()) {
                UserData data = CursedEffectsPlugin.getPlugin().getUserManager().getUserData(player.getUniqueId());

                if (data.getParticle() != null) {
                    int amount = CursedEffectsPlugin.getPlugin().getConfig().getInt("particles." + data.getParticle().name() + ".amount");
                    float speed = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("particles." + data.getParticle().name() + ".speed");

//            ParticleUtil.sendParticle(player, data.getParticle(), amount, speed);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            ParticleUtil.sendParticle(player, data.getParticle(), amount, speed);
                        }
                    }.runTaskLater(CursedEffectsPlugin.getPlugin(), 10);
                }

                if (data.getSound() != null) {

                    float volume = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("sounds." + data.getSound().name() + ".volume");
                    float pitch = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("sounds." + data.getSound().name() + ".pitch");

                    player.getWorld().playSound(player.getLocation(), data.getSound(), volume, pitch);
                }

                Message.sendMessageToPlayer(player, "Ran test sound and particle..");
            } else {
                Message.sendMessageToPlayer(player, "/cursedeffects [particles|sounds]");
            }
        } else {
            Message.sendMessageToPlayer(player, "/cursedeffects [particles|sounds]");
        }
    }
}
