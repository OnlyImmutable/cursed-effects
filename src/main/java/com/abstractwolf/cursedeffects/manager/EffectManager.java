package com.abstractwolf.cursedeffects.manager;

import com.abstractwolf.cursedeffects.CursedEffectsPlugin;
import org.bukkit.Particle;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.List;

public class EffectManager {

    private List<Particle> availableParticles;
    private List<Particle> availableTrails;
    private List<Sound> availableSounds;

    public EffectManager() {
        availableParticles = new ArrayList<>();
        availableSounds = new ArrayList<>();
        availableTrails = new ArrayList<>();

        for (String particle : CursedEffectsPlugin.getPlugin().getConfig().getConfigurationSection("particles").getKeys(false)) {
            availableParticles.add(Particle.valueOf(particle));
        }

        for (String sound : CursedEffectsPlugin.getPlugin().getConfig().getConfigurationSection("sounds").getKeys(false)) {
            availableSounds.add(Sound.valueOf(sound));
        }

        for (String trail : CursedEffectsPlugin.getPlugin().getConfig().getConfigurationSection("trails").getKeys(false)) {
            availableTrails.add(Particle.valueOf(trail));
        }
    }

    public List<Particle> getAvailableParticles() {
        return availableParticles;
    }

    public List<Sound> getAvailableSounds() {
        return availableSounds;
    }

    public List<Particle> getAvailableTrails() {
        return availableTrails;
    }
}
