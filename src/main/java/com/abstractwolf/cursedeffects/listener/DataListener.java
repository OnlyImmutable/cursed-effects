package com.abstractwolf.cursedeffects.listener;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.abstractwolf.cursedeffects.CursedEffectsPlugin;
import com.abstractwolf.cursedeffects.manager.data.UserData;
import com.abstractwolf.cursedeffects.utils.ParticleUtil;

import de.myzelyam.api.vanish.VanishAPI;

public class DataListener implements Listener
{

    @EventHandler
    public void onPreConnect(AsyncPlayerPreLoginEvent event)
    {

        if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED)
        {

            final UUID uuid = event.getUniqueId();

            CursedEffectsPlugin.getPlugin().getUserManager().addToCache(uuid);
            CursedEffectsPlugin.getPlugin().getUserManager().getUserData(uuid).load();

        }

    }

    @EventHandler
    public void onConnect(PlayerJoinEvent event)
    {

        final Player player = event.getPlayer();

        final UserData data = CursedEffectsPlugin.getPlugin().getUserManager().getUserData(player.getUniqueId());

        if (!VanishAPI.isInvisible(player))
        {

            if (data == null)
            {

                return;

            }

            new BukkitRunnable()
            {

                @Override
                public void run()
                {

                    if (data.getParticle() != null)
                    {

                        final int amount = CursedEffectsPlugin.getPlugin().getConfig().getInt("particles." + data.getParticle().name() + ".amount");
                        final float speed = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("particles." + data.getParticle().name() + ".speed");

                        ParticleUtil.sendParticle(player.getLocation(), data.getParticle(), amount, speed, 0.1F, 0, 0.1F);

                    }

                    if (data.getSound() != null)
                    {

                        final float volume = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("sounds." + data.getSound().name() + ".volume");
                        final float pitch = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("sounds." + data.getSound().name() + ".pitch");

                        player.getWorld().playSound(player.getLocation(), data.getSound(), volume, pitch);

                    }

                }

            }.runTaskLater(CursedEffectsPlugin.getPlugin(), 2L);

        }

    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event)
    {

        final Player player = event.getPlayer();

        final UserData data = CursedEffectsPlugin.getPlugin().getUserManager().getUserData(player.getUniqueId());

        if (!VanishAPI.isInvisible(player))
        {

            if (data == null)
            {

                return;

            }

            new BukkitRunnable()
            {

                @Override
                public void run()
                {

                    if (data.getParticle() != null)
                    {

                        final int amount = CursedEffectsPlugin.getPlugin().getConfig().getInt("particles." + data.getParticle().name() + ".amount");
                        final float speed = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("particles." + data.getParticle().name() + ".speed");

                        ParticleUtil.sendParticle(player.getLocation(), data.getParticle(), amount, speed, 0.1F, 0, 0.1F);

                    }

                    if (data.getSound() != null)
                    {

                        final float volume = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("sounds." + data.getSound().name() + ".volume");
                        final float pitch = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("sounds." + data.getSound().name() + ".pitch");

                        player.getWorld().playSound(player.getLocation(), data.getSound(), volume, pitch);

                    }

                }

            }.runTaskLater(CursedEffectsPlugin.getPlugin(), 2L);

        }

        data.save(false);
        CursedEffectsPlugin.getPlugin().getUserManager().removeFromCache(player.getUniqueId());

    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event)
    {

        final Player player = event.getEntity();
        final UserData data = CursedEffectsPlugin.getPlugin().getUserManager().getUserData(player.getUniqueId());

        if (!VanishAPI.isInvisible(player))
        {

            if (data == null)
            {

                return;

            }

            if (data.getParticle() != null)
            {

                final int amount = CursedEffectsPlugin.getPlugin().getConfig().getInt("particles." + data.getParticle().name() + ".amount");
                final float speed = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("particles." + data.getParticle().name() + ".speed");

                ParticleUtil.sendParticle(player.getLocation(), data.getParticle(), amount, speed, 0.1F, 0, 0.1F);

            }

            if (data.getSound() != null)
            {

                final float volume = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("sounds." + data.getSound().name() + ".volume");
                final float pitch = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("sounds." + data.getSound().name() + ".pitch");

                player.getWorld().playSound(player.getLocation(), data.getSound(), volume, pitch);

            }

        }

    }

    @EventHandler
    public void onMove(PlayerMoveEvent event)
    {

        final Player player = event.getPlayer();
        final UserData data = CursedEffectsPlugin.getPlugin().getUserManager().getUserData(player.getUniqueId());

        if (!VanishAPI.isInvisible(player))
        {

            if (data == null)
            {

                return;

            }

            if (data.getParticleTrail() != null)
            {

                final int amount = CursedEffectsPlugin.getPlugin().getConfig().getInt("trails." + data.getParticleTrail().name() + ".amount");
                final float speed = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("trails." + data.getParticleTrail().name() + ".speed");

                final int offsetX = CursedEffectsPlugin.getPlugin().getConfig().getInt("trails." + data.getParticleTrail().name() + ".offsetX");
                final int offsetY = CursedEffectsPlugin.getPlugin().getConfig().getInt("trails." + data.getParticleTrail().name() + ".offsetY");
                final int offsetZ = CursedEffectsPlugin.getPlugin().getConfig().getInt("trails." + data.getParticleTrail().name() + ".offsetZ");

                ParticleUtil.sendParticle(player.getLocation(), data.getParticleTrail(), amount, speed, offsetX, offsetY, offsetZ);

            }

        }

    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event)
    {

        new BukkitRunnable()
        {

            @Override
            public void run()
            {

                final Player player = event.getPlayer();
                final UserData data = CursedEffectsPlugin.getPlugin().getUserManager().getUserData(player.getUniqueId());

                final Location previous = event.getFrom();
                final Location location = event.getTo();

                if ((previous != null
                        && location != null)
                        && location.getWorld() == previous.getWorld()
                        && previous.distance(location) < 10)
                {

                    //            System.out.println("Previous location was " + (previous.distance(location)) + " blocks from new location.");
                    return;

                }

                if (!VanishAPI.isInvisible(player))
                {

                    if (data == null)
                    {

                        return;

                    }

                    if (data.getParticle() != null)
                    {

                        final int amount = CursedEffectsPlugin.getPlugin().getConfig().getInt("particles." + data.getParticle().name() + ".amount");
                        final float speed = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("particles." + data.getParticle().name() + ".speed");

                        ParticleUtil.sendParticle(location, data.getParticle(), amount, speed, 0, 0, 0);

                    }

                    if (data.getSound() != null)
                    {

                        final float volume = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("sounds." + data.getSound().name() + ".volume");
                        final float pitch = (float) CursedEffectsPlugin.getPlugin().getConfig().getDouble("sounds." + data.getSound().name() + ".pitch");

                        player.getWorld().playSound(location, data.getSound(), volume, pitch);

                    }

                }

            }

        }.runTaskLater(CursedEffectsPlugin.getPlugin(), 2);

    }

}
