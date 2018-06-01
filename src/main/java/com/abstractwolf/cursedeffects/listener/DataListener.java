package com.abstractwolf.cursedeffects.listener;

import com.abstractwolf.cursedeffects.CursedEffectsPlugin;
import com.abstractwolf.cursedeffects.manager.data.UserData;
import com.abstractwolf.cursedeffects.utils.ParticleUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class DataListener implements Listener {

    @EventHandler
    public void onPreConnect(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();

        if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            CursedEffectsPlugin.getPlugin().getUserManager().addToCache(uuid);
            CursedEffectsPlugin.getPlugin().getUserManager().getUserData(uuid).load();
        }
    }

    @EventHandler
    public void onConnect(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        UserData data = CursedEffectsPlugin.getPlugin().getUserManager().getUserData(player.getUniqueId());

        if (data.getParticle() != null) {
            int amount = CursedEffectsPlugin.getPlugin().getConfig().getInt("particles." + data.getParticle().name() + ".amount");
            float speed = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("particles." + data.getParticle().name() + ".speed");

//            ParticleUtil.sendParticle(player, data.getParticle(), amount, speed);

            new BukkitRunnable() {
                @Override
                public void run() {
                    ParticleUtil.sendParticle(player.getLocation(), data.getParticle(), amount, speed);
                }
            }.runTaskLater(CursedEffectsPlugin.getPlugin(), 10);
        }

        if (data.getSound() != null) {

            float volume = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("sounds." + data.getSound().name() + ".volume");
            float pitch = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("sounds." + data.getSound().name() + ".pitch");

            player.getWorld().playSound(player.getLocation(), data.getSound(), volume, pitch);
        }
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        UserData data = CursedEffectsPlugin.getPlugin().getUserManager().getUserData(player.getUniqueId());

        if (data.getParticle() != null) {
            int amount = CursedEffectsPlugin.getPlugin().getConfig().getInt("particles." + data.getParticle().name() + ".amount");
            float speed = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("particles." + data.getParticle().name() + ".speed");

            ParticleUtil.sendParticle(player.getLocation(), data.getParticle(), amount, speed);
        }

        if (data.getSound() != null) {

            float volume = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("sounds." + data.getSound().name() + ".volume");
            float pitch = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("sounds." + data.getSound().name() + ".pitch");

            player.getWorld().playSound(player.getLocation(), data.getSound(), volume, pitch);
        }

        data.save(false);
        CursedEffectsPlugin.getPlugin().getUserManager().removeFromCache(player.getUniqueId());
    }
}
