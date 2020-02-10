package com.abstractwolf.cursedeffects.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;

import com.abstractwolf.cursedeffects.CursedEffectsPlugin;
import com.abstractwolf.cursedeffects.EffectType;
import com.abstractwolf.cursedeffects.manager.data.UserData;
import com.abstractwolf.cursedeffects.utils.Message;
import com.abstractwolf.cursedeffects.utils.StringUtil;
import com.abstractwolf.cursedeffects.utils.itemstack.ItemFactory;
import com.abstractwolf.cursedeffects.utils.itemstack.PotionFactory;
import com.abstractwolf.cursedeffects.utils.menu.MenuFactory;
import com.abstractwolf.cursedeffects.utils.menu.MenuItem;
import com.abstractwolf.cursedeffects.utils.placeholder.Placeholder;

public class EffectSelectGui extends MenuFactory
{

    public EffectSelectGui(Player player, int page, EffectType type)
    {

        super("&5&lEffects &7: Select your " + (type == EffectType.PARTICLE ? "particle" : (type == EffectType.SOUND ? "sound" : "trail")) + ". Page " + page, 6);

        final List<Integer> darkPurpleGlass = Arrays.asList(0, 1, 7, 8, 36, 37, 43, 44, 45, 46, 52, 53);
        final List<Integer> lightPurpleGlass = Arrays.asList(2, 6, 38, 42, 47, 51);
        final List<Integer> cyanGlass = Arrays.asList(3, 4, 5, 39, 40, 41, 48, 49, 50);

        final int itemsPerPage = 27;
        final int totalPages = getPagesByPermission(player, itemsPerPage, type);
        int position = 9;

        for (int i = 0; i < getInventory().getSize(); i++)
        {

            if (darkPurpleGlass.contains(i))
            {

                addItem(new MenuItem(i, new ItemFactory(Material.PURPLE_STAINED_GLASS_PANE, 1, (byte) (10))
                        .setDisplayName(" ")
                        .build()));

            }
            else if (lightPurpleGlass.contains(i))
            {

                addItem(new MenuItem(i, new ItemFactory(Material.MAGENTA_STAINED_GLASS_PANE, 1, (byte) (2))
                        .setDisplayName(" ")
                        .build()));

            }
            else if (cyanGlass.contains(i))
            {

                addItem(new MenuItem(i, new ItemFactory(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, (byte) (3))
                        .setDisplayName(" ")
                        .build()));

            }

        }

        final UserData data = CursedEffectsPlugin.getPlugin().getUserManager().getUserData(player.getUniqueId());

        switch (type)
        {

            case PARTICLE:

                for (final Particle particle : getParticles(page, itemsPerPage))
                {

                    final Material material = Material.valueOf(CursedEffectsPlugin.getPlugin().getConfig().getString("particles." + particle.name() + ".icon.material"));
                    final byte materialData = (byte) (CursedEffectsPlugin.getPlugin().getConfig().getInt("particles." + particle.name() + ".icon.data"));

                    final String permission = CursedEffectsPlugin.getPlugin().getConfig().getString("particles." + particle.name() + ".permission");

                    final boolean hasPermission = player.hasPermission(permission)
                            || player.hasPermission("cursedeffects.particles.*");
                    final boolean isEnabled = data.getParticle() == particle;

                    if (hasPermission)
                    {

                        ItemFactory itemFactory = null;
                        PotionFactory potionFactory = null;

                        if (material == Material.POTION
                                || material == Material.SPLASH_POTION)
                        {

                            potionFactory = new PotionFactory((material == Material.SPLASH_POTION))
                                    .setDisplayName("&7&l" + StringUtil.setUppercaseEachStart(particle.name().replace("_", " ")) + " &7(" + (hasPermission ? "&aUnlocked" : "&cLocked") + "&7)")
                                    .setLore(
                                            "",
                                            "&7This particle effect is currently " + (isEnabled ? "&aenabled" : "&cnot enabled") + "&7.",
                                            "",
                                            "&7Click to enable this particle effect.");

                            try
                            {

                                final String[] rgb = CursedEffectsPlugin.getPlugin().getConfig().getString("particles." + particle.name() + ".icon.color").split(",");
                                potionFactory.setColour(Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));

                            }
                            catch (final Exception e)
                            {

                            }

                            if (isEnabled)
                            {

                                potionFactory.enchant(Enchantment.DURABILITY);

                            }

                            potionFactory.addItemFlag(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES);

                        }
                        else
                        {

                            itemFactory = new ItemFactory(material, 1, materialData)
                                    .setDisplayName("&7&l" + StringUtil.setUppercaseEachStart(particle.name().replace("_", " ")) + " &7(" + (hasPermission ? "&aUnlocked" : "&cLocked") + "&7)")
                                    .setLore(
                                            "",
                                            "&7This particle effect is currently " + (isEnabled ? "&aenabled" : "&cnot enabled") + "&7.",
                                            "",
                                            "&7Click to enable this particle effect.");

                            if (isEnabled)
                            {

                                itemFactory.enchant(Enchantment.DURABILITY);

                            }

                            itemFactory.addItemFlag(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES);

                        }

                        addItem(new MenuItem(position, ((material == Material.POTION
                                || material == Material.SPLASH_POTION) ? potionFactory.build() : itemFactory.build()))
                        {

                            @Override
                            public void click(Player player, ClickType clickType)
                            {

                                if (!hasPermission)
                                {

                                    Message.sendMessage(player, "noPermission", Collections.singletonList(new Placeholder("%permission%", "cursedeffects.particle." + particle.name())));
                                    return;

                                }

                                data.setParticle(particle);
                                Message.sendMessage(player, "setParticle", Collections.singletonList(new Placeholder("%particle%", StringUtil.setUppercaseEachStart(particle.name().replace("_", " ")))));
                                player.closeInventory();

                            }

                        });
                        position += 1;

                    }

                }

                break;

            case SOUND:

                for (final Sound sound : getSounds(page, itemsPerPage))
                {

                    final Material material = Material.valueOf(CursedEffectsPlugin.getPlugin().getConfig().getString("sounds." + sound.name() + ".icon.material"));
                    final byte materialData = (byte) (CursedEffectsPlugin.getPlugin().getConfig().getInt("sounds." + sound.name() + ".icon.data"));

                    final String permission = CursedEffectsPlugin.getPlugin().getConfig().getString("sounds." + sound.name() + ".permission");

                    final boolean hasPermission = player.hasPermission(permission)
                            || player.hasPermission("cursedeffects.sounds.*");
                    final boolean isEnabled = data.getSound() == sound;

                    if (hasPermission)
                    {

                        ItemFactory itemFactory = null;
                        PotionFactory potionFactory = null;

                        if (material == Material.POTION
                                || material == Material.SPLASH_POTION)
                        {

                            potionFactory = new PotionFactory((material == Material.SPLASH_POTION))
                                    .setDisplayName("&7&l" + StringUtil.setUppercaseEachStart(sound.name().replace("_", " ")) + " &7(" + (hasPermission ? "&aUnlocked" : "&cLocked") + "&7)")
                                    .setLore(
                                            "",
                                            "&7This sound effect is currently " + (isEnabled ? "&aenabled" : "&cnot enabled") + "&7.",
                                            "",
                                            "&7Click to enable this sound effect.");

                            try
                            {

                                final String[] rgb = CursedEffectsPlugin.getPlugin().getConfig().getString("sounds." + sound.name() + ".icon.color").split(",");
                                potionFactory.setColour(Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));

                            }
                            catch (final Exception e)
                            {

                            }

                            if (isEnabled)
                            {

                                potionFactory.enchant(Enchantment.DURABILITY);

                            }

                            potionFactory.addItemFlag(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES);

                        }
                        else
                        {

                            itemFactory = new ItemFactory(material, 1, materialData)
                                    .setDisplayName("&7&l" + StringUtil.setUppercaseEachStart(sound.name().replace("_", " ")) + " &7(" + (hasPermission ? "&aUnlocked" : "&cLocked") + "&7)")
                                    .setLore(
                                            "",
                                            "&7This sound effect is currently " + (isEnabled ? "&aenabled" : "&cnot enabled") + "&7.",
                                            "",
                                            "&7Click to enable this sound effect.");

                            if (isEnabled)
                            {

                                itemFactory.enchant(Enchantment.DURABILITY);

                            }

                            itemFactory.addItemFlag(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES);

                        }

                        addItem(new MenuItem(position, ((material == Material.POTION
                                || material == Material.SPLASH_POTION) ? potionFactory.build() : itemFactory.build()))
                        {

                            @Override
                            public void click(Player player, ClickType clickType)
                            {

                                data.setSound(sound);
                                Message.sendMessage(player, "setSound", Collections.singletonList(new Placeholder("%sound%", StringUtil.setUppercaseEachStart(sound.name().replace("_", " ")))));
                                player.closeInventory();

                            }

                        });
                        position += 1;

                    }

                }

                break;

            case TRAIL:

                for (final Particle particle : getTrails(page, itemsPerPage))
                {

                    final Material material = Material.valueOf(CursedEffectsPlugin.getPlugin().getConfig().getString("trails." + particle.name() + ".icon.material"));
                    final byte materialData = (byte) (CursedEffectsPlugin.getPlugin().getConfig().getInt("trails." + particle.name() + ".icon.data"));

                    final String permission = CursedEffectsPlugin.getPlugin().getConfig().getString("trails." + particle.name() + ".permission");

                    final boolean hasPermission = player.hasPermission(permission)
                            || player.hasPermission("cursedeffects.trails.*");
                    final boolean isEnabled = data.getParticleTrail() == particle;

                    if (hasPermission)
                    {

                        ItemFactory itemFactory = null;
                        PotionFactory potionFactory = null;

                        if (material == Material.POTION
                                || material == Material.SPLASH_POTION)
                        {

                            potionFactory = new PotionFactory((material == Material.SPLASH_POTION))
                                    .setDisplayName("&7&l" + StringUtil.setUppercaseEachStart(particle.name().replace("_", " ")) + " &7(" + (hasPermission ? "&aUnlocked" : "&cLocked") + "&7)")
                                    .setLore(
                                            "",
                                            "&7This trail effect is currently " + (isEnabled ? "&aenabled" : "&cnot enabled") + "&7.",
                                            "",
                                            "&7Click to enable this trail effect.");

                            try
                            {

                                final String[] rgb = CursedEffectsPlugin.getPlugin().getConfig().getString("trails." + particle.name() + ".icon.color").split(",");
                                potionFactory.setColour(Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));

                            }
                            catch (final Exception e)
                            {

                            }

                            if (isEnabled)
                            {

                                potionFactory.enchant(Enchantment.DURABILITY);

                            }

                            potionFactory.addItemFlag(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES);

                        }
                        else
                        {

                            itemFactory = new ItemFactory(material, 1, materialData)
                                    .setDisplayName("&7&l" + StringUtil.setUppercaseEachStart(particle.name().replace("_", " ")) + " &7(" + (hasPermission ? "&aUnlocked" : "&cLocked") + "&7)")
                                    .setLore(
                                            "",
                                            "&7This trail effect is currently " + (isEnabled ? "&aenabled" : "&cnot enabled") + "&7.",
                                            "",
                                            "&7Click to enable this trail effect.");

                            if (isEnabled)
                            {

                                itemFactory.enchant(Enchantment.DURABILITY);

                            }

                            itemFactory.addItemFlag(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES);

                        }

                        addItem(new MenuItem(position, ((material == Material.POTION
                                || material == Material.SPLASH_POTION) ? potionFactory.build() : itemFactory.build()))
                        {

                            @Override
                            public void click(Player player, ClickType clickType)
                            {

                                if (!hasPermission)
                                {

                                    Message.sendMessage(player, "noPermission", Collections.singletonList(new Placeholder("%permission%", "cursedeffects.trail." + particle.name())));
                                    return;

                                }

                                data.setParticleTrail(particle);
                                Message.sendMessage(player, "setTrail", Collections.singletonList(new Placeholder("%trail%", StringUtil.setUppercaseEachStart(particle.name().replace("_", " ")))));
                                player.closeInventory();

                            }

                        });
                        position += 1;

                    }

                }

                break;

        }

        // Information item used to check the current page the user is on.
        addItem(new MenuItem(49, new ItemFactory(Material.PAPER)
                .setDisplayName("&5&lCurrent Page:")
                .setLore("&d" + page + "&5/&d" + totalPages)
                .build()));

        // Adds an item if a next page is available for the user.
        addItem(new MenuItem(47, new ItemFactory(Material.ARROW)
                .setDisplayName("&5&lBack")
                .setLore((page - 1 == 0) ? "&dYou cannot go back anymore." : "&dGo back a page.")
                .build())
        {

            @Override
            public void click(Player player, ClickType clickType)
            {

                if (page - 1 > 0)
                {

                    new EffectSelectGui(player, page - 1, type);

                }

            }

        });

        // Adds an item if the user can go back to a previous page.
        addItem(new MenuItem(51, new ItemFactory(Material.ARROW)
                .setDisplayName("&5&lNext")
                .setLore(((page + 1) <= totalPages ? "&dGo forward a page." : "&dThere are no more pages."))
                .build())
        {

            @Override
            public void click(Player player, ClickType clickType)
            {

                if (((page + 1) <= totalPages))
                {

                    new EffectSelectGui(player, page + 1, type);

                }

            }

        });

        openInventory(player);

    }

    private int getPagesByPermission(Player player, int perPage, EffectType type)
    {

        int totalItems = 0;

        switch (type)
        {

            case PARTICLE:
                totalItems = player.hasPermission("cursedeffects.particles.*") ? CursedEffectsPlugin.getPlugin().getEffectManager().getAvailableParticles().size() : 0;
                break;

            case SOUND:
                totalItems = player.hasPermission("cursedeffects.sounds.*") ? CursedEffectsPlugin.getPlugin().getEffectManager().getAvailableSounds().size() : 0;
                break;

            case TRAIL:
                totalItems = player.hasPermission("cursedeffects.trails.*") ? CursedEffectsPlugin.getPlugin().getEffectManager().getAvailableTrails().size() : 0;
                break;

        }

        if (type == EffectType.PARTICLE
                && !player.hasPermission("cursedeffects.particles.*"))
        {

            for (final Particle particle : CursedEffectsPlugin.getPlugin().getEffectManager().getAvailableParticles())
            {

                if (player.hasPermission(CursedEffectsPlugin.getPlugin().getConfig().getString("particles." + particle.name() + ".permission")))
                {

                    totalItems += 1;

                }

            }

        }

        if (type == EffectType.SOUND
                && !player.hasPermission("cursedeffects.sounds.*"))
        {

            for (final Sound sound : CursedEffectsPlugin.getPlugin().getEffectManager().getAvailableSounds())
            {

                if (player.hasPermission(CursedEffectsPlugin.getPlugin().getConfig().getString("sounds." + sound.name() + ".permission")))
                {

                    totalItems += 1;

                }

            }

        }

        if (type == EffectType.TRAIL
                && !player.hasPermission("cursedeffects.trails.*"))
        {

            for (final Particle trail : CursedEffectsPlugin.getPlugin().getEffectManager().getAvailableTrails())
            {

                if (player.hasPermission(CursedEffectsPlugin.getPlugin().getConfig().getString("trails." + trail.name() + ".permission")))
                {

                    totalItems += 1;

                }

            }

        }

        int pages = (totalItems / perPage);

        if (pages < 1)
        {

            pages = 1;

        }

        return ((totalItems % perPage == 0
                || totalItems <= perPage) ? pages : pages + 1);

    }

    private List<Particle> getParticles(int page, int perPage)
    {

        final List<Particle> effects = new ArrayList<>();

        try
        {

            for (int i = ((page - 1) * perPage); i < (((page - 1) * perPage) + perPage); i++)
            {

                final Particle particle = CursedEffectsPlugin.getPlugin().getEffectManager().getAvailableParticles().get(i);
                effects.add(particle);

            }

        }
        catch (final IndexOutOfBoundsException e)
        {

        /* Ignored */ }

        return effects;

    }

    private List<Sound> getSounds(int page, int perPage)
    {

        final List<Sound> effects = new ArrayList<>();

        try
        {

            for (int i = ((page - 1) * perPage); i < (((page - 1) * perPage) + perPage); i++)
            {

                final Sound sound = CursedEffectsPlugin.getPlugin().getEffectManager().getAvailableSounds().get(i);
                effects.add(sound);

            }

        }
        catch (final IndexOutOfBoundsException e)
        {

        /* Ignored */ }

        return effects;

    }

    private List<Particle> getTrails(int page, int perPage)
    {

        final List<Particle> effects = new ArrayList<>();

        try
        {

            for (int i = ((page - 1) * perPage); i < (((page - 1) * perPage) + perPage); i++)
            {

                final Particle particle = CursedEffectsPlugin.getPlugin().getEffectManager().getAvailableTrails().get(i);
                effects.add(particle);

            }

        }
        catch (final IndexOutOfBoundsException e)
        {

        /* Ignored */ }

        return effects;

    }

}
