package com.abstractwolf.cursedeffects.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_15_R1.CraftParticle;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_15_R1.PacketPlayOutWorldParticles;

public class ParticleUtil
{

    public static void sendParticle(Location location, Particle particle, int amount, float speed, float x, float y, float z)
    {

        final PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(CraftParticle.toNMS(particle, null),
                false,
                location.getX(),
                location.getY(),
                location.getZ(),
                x,
                y,
                z,
                speed,
                amount);

        for (final Player onlinePlayer : Bukkit.getOnlinePlayers())
        {

            ((CraftPlayer) onlinePlayer).getHandle().playerConnection.sendPacket(packet);

        }

    }

}
