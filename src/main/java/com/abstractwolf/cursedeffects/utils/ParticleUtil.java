package com.abstractwolf.cursedeffects.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ParticleUtil {

    public static void sendParticle(Location location, Particle particle, int amount, float speed) {

        try {

            Class<?> enumParticle = ReflectionUtil.PackageType.MINECRAFT_SERVER.getClass("EnumParticle");
            Method enumValueOf = enumParticle.getMethod("valueOf", String.class);
            Object enumValue = enumValueOf.invoke(null, particle.name());

            Object packet = ReflectionUtil.PackageType.MINECRAFT_SERVER.getClass("PacketPlayOutWorldParticles")
                    .getConstructor(enumParticle, boolean.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class, int[].class)
                    .newInstance(
                            enumValue,
                            false,
                            (float) location.getX(),
                            (float) location.getY(),
                            (float) location.getZ(),
                            1,
                            1,
                            1,
                            speed,
                            amount,
                            null);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                sendPacket(onlinePlayer, packet);
            }
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void sendParticle(Location location, Particle particle, int amount, float speed, int x, int y, int z) {

        try {

            Class<?> enumParticle = ReflectionUtil.PackageType.MINECRAFT_SERVER.getClass("EnumParticle");
            Method enumValueOf = enumParticle.getMethod("valueOf", String.class);
            Object enumValue = enumValueOf.invoke(null, particle.name());

            Object packet = ReflectionUtil.PackageType.MINECRAFT_SERVER.getClass("PacketPlayOutWorldParticles")
                    .getConstructor(enumParticle, boolean.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class, int[].class)
                    .newInstance(
                            enumValue,
                            false,
                            (float) location.getX(),
                            (float) location.getY(),
                            (float) location.getZ(),
                            x,
                            y,
                            z,
                            speed,
                            amount,
                            null);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                sendPacket(onlinePlayer, packet);
            }
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void sendPacket(Player player, Object packet) {

        try {
            Object nmsPlayer = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
            playerConnection.getClass().getMethod("sendPacket", ReflectionUtil.PackageType.MINECRAFT_SERVER.getClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
