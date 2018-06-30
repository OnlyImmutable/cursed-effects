package com.abstractwolf.cursedeffects.listener;

import com.abstractwolf.cursedeffects.CursedEffectsPlugin;
import com.abstractwolf.cursedeffects.manager.data.UserData;
import com.abstractwolf.cursedeffects.utils.ParticleUtil;
import de.myzelyam.api.vanish.VanishAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
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

        if (!VanishAPI.isInvisible(player)) {

            if (data == null) return;

            new BukkitRunnable() {

                @Override
                public void run() {

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
                }
            }.runTaskLater(CursedEffectsPlugin.getPlugin(), 10L);
        }
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        UserData data = CursedEffectsPlugin.getPlugin().getUserManager().getUserData(player.getUniqueId());

        if (!VanishAPI.isInvisible(player)) {

            if (data == null) return;

            new BukkitRunnable() {

                @Override
                public void run() {

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
                }
            }.runTaskLater(CursedEffectsPlugin.getPlugin(), 10L);
        }

        data.save(false);
        CursedEffectsPlugin.getPlugin().getUserManager().removeFromCache(player.getUniqueId());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        UserData data = CursedEffectsPlugin.getPlugin().getUserManager().getUserData(player.getUniqueId());

        if (!VanishAPI.isInvisible(player)) {

            if (data == null) return;

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
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UserData data = CursedEffectsPlugin.getPlugin().getUserManager().getUserData(player.getUniqueId());

        if (!VanishAPI.isInvisible(player)) {

            if (data == null) return;

            if (data.getParticleTrail() != null) {
                int amount = CursedEffectsPlugin.getPlugin().getConfig().getInt("trails." + data.getParticleTrail().name() + ".amount");
                float speed = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("trails." + data.getParticleTrail().name() + ".speed");

                int offsetX = CursedEffectsPlugin.getPlugin().getConfig().getInt("trails." + data.getParticleTrail().name() + ".offsetX");
                int offsetY = CursedEffectsPlugin.getPlugin().getConfig().getInt("trails." + data.getParticleTrail().name() + ".offsetY");
                int offsetZ = CursedEffectsPlugin.getPlugin().getConfig().getInt("trails." + data.getParticleTrail().name() + ".offsetZ");

                ParticleUtil.sendParticle(player.getLocation(), data.getParticleTrail(), amount, speed, offsetX, offsetY, offsetZ);
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {

        new BukkitRunnable() {

            @Override
            public void run() {
                Player player = event.getPlayer();
                UserData data = CursedEffectsPlugin.getPlugin().getUserManager().getUserData(player.getUniqueId());

                Location previous = event.getFrom();
                Location location = event.getTo();

                if ((previous != null && location != null) && location.getWorld() == previous.getWorld() && previous.distance(location) < 10) {
//            System.out.println("Previous location was " + (previous.distance(location)) + " blocks from new location.");
                    return;
                }

                if (!VanishAPI.isInvisible(player)) {

                    if (data == null) return;

                    if (data.getParticle() != null) {
                        int amount = CursedEffectsPlugin.getPlugin().getConfig().getInt("particles." + data.getParticle().name() + ".amount");
                        float speed = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("particles." + data.getParticle().name() + ".speed");

                        ParticleUtil.sendParticle(location, data.getParticle(), amount, speed);
                    }

                    if (data.getSound() != null) {

                        float volume = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("sounds." + data.getSound().name() + ".volume");
                        float pitch = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("sounds." + data.getSound().name() + ".pitch");

                        player.getWorld().playSound(location, data.getSound(), volume, pitch);
                    }
                }
            }
        }.runTaskLater(CursedEffectsPlugin.getPlugin(), CursedEffectsPlugin.getPlugin().getConfig().getInt("settings.teleportDelay"));
    }
}
