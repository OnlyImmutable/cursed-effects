package com.abstractwolf.cursedeffects.manager.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;

import com.abstractwolf.cursedeffects.CursedEffectsPlugin;

public class UserData
{

    private final UUID uuid;
    private Particle particle;
    private Particle particleTrail;
    private Sound sound;

    public UserData(UUID uuid)
    {

        this.uuid = uuid;

        this.particle = null;
        this.sound = null;
        this.particleTrail = null;

    }

    public UUID getUuid()
    {

        return uuid;

    }

    public Particle getParticle()
    {

        return particle;

    }

    public void setParticle(Particle particle)
    {

        this.particle = particle;

    }

    public Particle getParticleTrail()
    {

        return particleTrail;

    }

    public void setParticleTrail(Particle particleTrail)
    {

        this.particleTrail = particleTrail;

    }

    public Sound getSound()
    {

        return sound;

    }

    public void setSound(Sound sound)
    {

        this.sound = sound;

    }

    public void load()
    {

        final CursedEffectsPlugin plugin = CursedEffectsPlugin.getPlugin();

        if (!plugin.isUseFlatfile())
        {

            plugin.getDatabase().sendPreparedStatement("SELECT * FROM cursedeffects WHERE uuid='" + uuid.toString() + "';", false, true, (statement) ->
            {

                try
                {

                    final ResultSet set = statement.getResultSet();

                    if (set.next())
                    {

                        if (!set.getString("particle").equals("NONE"))
                        {

                            setParticle(Particle.valueOf(set.getString("particle")));

                        }

                        if (!set.getString("sound").equals("NONE"))
                        {

                            setSound(Sound.valueOf(set.getString("sound")));

                        }

                        if (!set.getString("trail").equals("NONE"))
                        {

                            setParticleTrail(Particle.valueOf(set.getString("trail")));

                        }

                        System.out.println("Data loaded for (" + uuid.toString() + ").");

                    }

                }
                catch (final SQLException e)
                {

                    e.printStackTrace();

                }

            });

        }
        else
        {

            final FileConfiguration config = plugin.getFlatfileDatabase().getFileConfiguration();

            if (config == null)
            {

                System.out.println("Flatfile was null!");
                return;

            }

            if (config.getString("data." + uuid.toString() + ".particle") != null
                    && !config.getString("data." + uuid.toString() + ".particle").equals("NONE"))
            {

                setParticle(Particle.valueOf(config.getString("data." + uuid.toString() + ".particle")));

            }

            if (config.getString("data." + uuid.toString() + ".sound") != null
                    && !config.getString("data." + uuid.toString() + ".sound").equals("NONE"))
            {

                setSound(Sound.valueOf(config.getString("data." + uuid.toString() + ".sound")));

            }

            if (config.getString("data." + uuid.toString() + ".trail") != null
                    && !config.getString("data." + uuid.toString() + ".trail").equals("NONE"))
            {

                setParticleTrail(Particle.valueOf(config.getString("data." + uuid.toString() + ".trail")));

            }

            System.out.println("Data loaded (flatfile) for (" + uuid.toString() + ").");

        }

    }

    public void save(boolean shutdown)
    {

        final CursedEffectsPlugin plugin = CursedEffectsPlugin.getPlugin();

        if (!plugin.isUseFlatfile())
        {

            plugin.getDatabase().sendPreparedStatement("SELECT * FROM cursedeffects WHERE uuid='" + uuid.toString() + "';", false, true, (statement) ->
            {

                try
                {

                    final ResultSet set = statement.getResultSet();

                    if (set.next())
                    {

                        plugin.getDatabase().sendPreparedStatement("UPDATE cursedeffects SET " +
                                "uuid='" + uuid.toString() + "', " +
                                "particle='" + (particle == null ? "NONE" : particle.name()) + "', " +
                                "sound='" + (sound == null ? "NONE" : sound.name()) + "' " +
                                "trail='" + (particleTrail == null ? "NONE" : particleTrail.name()) + "', " +
                                "WHERE uuid='" + uuid.toString() + "';", true, !shutdown, (statementTemp) ->
                        {

                        });
                        System.out.println("Data saved for (" + uuid.toString() + ").");
                        return;

                    }

                    plugin.getDatabase().sendPreparedStatement("INSERT INTO cursedeffects (uuid, particle, sound, trail) VALUES('" + uuid.toString() + "', '" + (particle == null ? "NONE" : particle.name()) + "', '" + (sound == null ? "NONE" : sound.name()) + "', '" + (particleTrail == null ? "NONE" : particleTrail.name()) + "');", false, !shutdown, (statementTemp1) ->
                    {

                    });
                    System.out.println("Data saved for (" + uuid.toString() + ").");

                }
                catch (final SQLException e)
                {

                    e.printStackTrace();

                }

            });

        }
        else
        {

            final FileConfiguration config = plugin.getFlatfileDatabase().getFileConfiguration();

            config.set("data." + uuid.toString() + ".particle", (particle == null ? "NONE" : particle.name()));
            config.set("data." + uuid.toString() + ".sound", (sound == null ? "NONE" : sound.name()));
            config.set("data." + uuid.toString() + ".trail", (particleTrail == null ? "NONE" : particleTrail.name()));

            plugin.getFlatfileDatabase().updateConfig();
            System.out.println("Data saved (flatfile) for (" + uuid.toString() + ").");

        }

    }

}
