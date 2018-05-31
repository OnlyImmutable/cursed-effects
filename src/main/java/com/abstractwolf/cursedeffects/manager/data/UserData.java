package com.abstractwolf.cursedeffects.manager.data;

import com.abstractwolf.cursedeffects.CursedEffectsPlugin;
import org.bukkit.Particle;
import org.bukkit.Sound;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserData {

    private boolean existed;

    private final UUID uuid;
    private Particle particle;
    private Sound sound;

    public UserData(UUID uuid) {
        this.uuid = uuid;
        this.existed = false;

        this.particle = null;
        this.sound = null;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Particle getParticle() {
        return particle;
    }

    public void setParticle(Particle particle) {
        this.particle = particle;
    }

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public void load() {
        CursedEffectsPlugin plugin = CursedEffectsPlugin.getPlugin();

        plugin.getDatabase().sendPreparedStatement("SELECT * FROM cursedeffects WHERE uuid='" + uuid.toString() + "';", false, true, (statement) -> {

            try {

                ResultSet set = statement.getResultSet();

                if (set.next()) {

                    if (!set.getString("particle").equals("NONE")) {
                        setParticle(Particle.valueOf(set.getString("particle")));
                    }

                    if (!set.getString("sound").equals("NONE")) {
                        setSound(Sound.valueOf(set.getString("sound")));
                    }

                    existed = true;

                    System.out.println("Data loaded for (" + uuid.toString() + ").");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void save(boolean shutdown) {

        CursedEffectsPlugin plugin = CursedEffectsPlugin.getPlugin();

        if (existed) {
            // save data (UPDATE)

            plugin.getDatabase().sendPreparedStatement("UPDATE cursedeffects SET " +
                            "uuid='" + uuid.toString() + "', " +
                            "particle='" + (particle == null ? "NONE" : particle.name()) + "', " +
                            "sound='" + (sound == null ? "NONE" : sound.name()) + "' " +
                            "WHERE uuid='" + uuid.toString() + "';"
                    , true, !shutdown, (statement) -> {});
        } else {
            // save data (INSERT)
            plugin.getDatabase().sendPreparedStatement("INSERT INTO cursedeffects (uuid, particle, sound) VALUES('" + uuid.toString() + "', '" + (particle == null ? "NONE" :  particle.name()) + "', '" + (sound == null ? "NONE" : sound.name()) + "');"
                    , false, !shutdown, (statement) -> {});
        }
    }
}
