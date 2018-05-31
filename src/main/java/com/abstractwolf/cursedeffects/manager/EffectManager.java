package com.abstractwolf.cursedeffects.manager;

import com.abstractwolf.cursedeffects.CursedEffectsPlugin;
import org.bukkit.Particle;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.List;

public class EffectManager {

    private List<Particle> availableParticles;
    private List<Sound> availableSounds;

    public EffectManager() {
        availableParticles = new ArrayList<>();
        availableSounds = new ArrayList<>();

        for (String particle : CursedEffectsPlugin.getPlugin().getConfig().getConfigurationSection("particles").getKeys(false)) {
            availableParticles.add(Particle.valueOf(particle));
        }

        for (String sound : CursedEffectsPlugin.getPlugin().getConfig().getConfigurationSection("sounds").getKeys(false)) {
            availableSounds.add(Sound.valueOf(sound));
        }
    }

    public List<Particle> getAvailableParticles() {
        return availableParticles;
    }

    public List<Sound> getAvailableSounds() {
        return availableSounds;
    }
}
