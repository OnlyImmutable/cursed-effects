package com.abstractwolf.cursedeffects.gui;

import com.abstractwolf.cursedeffects.CursedEffectsPlugin;
import com.abstractwolf.cursedeffects.manager.data.UserData;
import com.abstractwolf.cursedeffects.utils.Message;
import com.abstractwolf.cursedeffects.utils.StringUtil;
import com.abstractwolf.cursedeffects.utils.itemstack.ItemFactory;
import com.abstractwolf.cursedeffects.utils.itemstack.PotionFactory;
import com.abstractwolf.cursedeffects.utils.menu.MenuFactory;
import com.abstractwolf.cursedeffects.utils.menu.MenuItem;
import com.abstractwolf.cursedeffects.utils.placeholder.Placeholder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EffectSelectGui extends MenuFactory {

    public EffectSelectGui(Player player, int page, boolean isParticle) {

        super("&5Select your &d" + (isParticle ? "particle" : "sound") + "&5. Page &d" + page, 6);

        int itemsPerPage = 27;
        int totalPages = getPagesByPermission(player, itemsPerPage, isParticle);
        int position = 9;

        for (int i = 0; i < getInventory().getSize(); i++) {
            addItem(new MenuItem(i, new ItemFactory(Material.STAINED_GLASS_PANE, 1, (byte) (i % 2 == 0 ? 10 : 2))
                    .setDisplayName(" ")
                    .build()));
        }

        UserData data = CursedEffectsPlugin.getPlugin().getUserManager().getUserData(player.getUniqueId());

        if (isParticle) {
            // TODO particle display

            for (Particle particle : getParticles(page, itemsPerPage)) {

                Material material = Material.valueOf(CursedEffectsPlugin.getPlugin().getConfig().getString("particles." + particle.name() + ".icon.material"));
                byte materialData = (byte) (CursedEffectsPlugin.getPlugin().getConfig().getInt("particles." + particle.name() + ".icon.data"));

                String permission = CursedEffectsPlugin.getPlugin().getConfig().getString("particles." + particle.name() + ".permission");

                boolean hasPermission = player.hasPermission(permission) || player.hasPermission("cursedeffects.particles.*");
                boolean isEnabled = data.getParticle() == particle;

                if (hasPermission) {
                    ItemFactory itemFactory = null;
                    PotionFactory potionFactory = null;

                    if (material == Material.POTION || material == Material.SPLASH_POTION) {

                        potionFactory =  new PotionFactory((material == Material.SPLASH_POTION))
                                .setDisplayName("&7&l" + StringUtil.setUppercaseEachStart(particle.name().replace("_", " ")) + " &7(" + (hasPermission ? "&aUnlocked" : "&cLocked") + "&7)")
                                .setLore(
                                        "",
                                        "&7This particle effect is currently " + (isEnabled ? "&aenabled" : "&cnot enabled") + "&7.",
                                        "",
                                        "&7Click to enable this particle effect."
                                );

                        try {
                            String[] rgb = CursedEffectsPlugin.getPlugin().getConfig().getString("particles." + particle.name() + ".icon.color").split(",");
                            potionFactory.setColour(Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));
                        } catch (Exception e) {}

                        if (isEnabled) {
                            potionFactory.enchant(Enchantment.DURABILITY);
                        }

                        potionFactory.addItemFlag(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES);
                    } else {
                        itemFactory = new ItemFactory(material, 1, materialData)
                                .setDisplayName("&7&l" + StringUtil.setUppercaseEachStart(particle.name().replace("_", " ")) + " &7(" + (hasPermission ? "&aUnlocked" : "&cLocked") + "&7)")
                                .setLore(
                                        "",
                                        "&7This particle effect is currently " + (isEnabled ? "&aenabled" : "&cnot enabled") + "&7.",
                                        "",
                                        "&7Click to enable this particle effect."
                                );
                        if (isEnabled) {
                            itemFactory.enchant(Enchantment.DURABILITY);
                        }

                        itemFactory.addItemFlag(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES);
                    }


                    addItem(new MenuItem(position, ((material == Material.POTION  || material == Material.SPLASH_POTION) ? potionFactory.build() : itemFactory.build())) {

                        @Override
                        public void click(Player player, ClickType clickType) {

                            if (!hasPermission) {
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
        } else {
            // TODO sound display

            for (Sound sound : getSounds(page, itemsPerPage)) {

                Material material = Material.valueOf(CursedEffectsPlugin.getPlugin().getConfig().getString("sounds." + sound.name() + ".icon.material"));
                byte materialData = (byte) (CursedEffectsPlugin.getPlugin().getConfig().getInt("sounds." + sound.name() + ".icon.data"));

                String permission = CursedEffectsPlugin.getPlugin().getConfig().getString("sounds." + sound.name() + ".permission");

                boolean hasPermission = player.hasPermission(permission) || player.hasPermission("cursedeffects.sounds.*");
                boolean isEnabled = data.getSound() == sound;

                if (hasPermission) {

                    ItemFactory itemFactory = null;
                    PotionFactory potionFactory = null;

                    if (material == Material.POTION || material == Material.SPLASH_POTION) {

                        potionFactory =  new PotionFactory((material == Material.SPLASH_POTION))
                                .setDisplayName("&7&l" + StringUtil.setUppercaseEachStart(sound.name().replace("_", " ")) + " &7(" + (hasPermission ? "&aUnlocked" : "&cLocked") + "&7)")
                                .setLore(
                                        "",
                                        "&7This sound effect is currently " + (isEnabled ? "&aenabled" : "&cnot enabled") + "&7.",
                                        "",
                                        "&7Click to enable this sound effect."
                                );

                        try {
                            String[] rgb = CursedEffectsPlugin.getPlugin().getConfig().getString("sounds." + sound.name() + ".icon.color").split(",");
                            potionFactory.setColour(Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));
                        } catch (Exception e) {}

                        if (isEnabled) {
                            potionFactory.enchant(Enchantment.DURABILITY);
                        }

                        potionFactory.addItemFlag(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES);
                    } else {
                        itemFactory = new ItemFactory(material, 1, materialData)
                                .setDisplayName("&7&l" + StringUtil.setUppercaseEachStart(sound.name().replace("_", " ")) + " &7(" + (hasPermission ? "&aUnlocked" : "&cLocked") + "&7)")
                                .setLore(
                                        "",
                                        "&7This sound effect is currently " + (isEnabled ? "&aenabled" : "&cnot enabled") + "&7.",
                                        "",
                                        "&7Click to enable this sound effect."
                                );
                        if (isEnabled) {
                            itemFactory.enchant(Enchantment.DURABILITY);
                        }

                        itemFactory.addItemFlag(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES);
                    }



                    addItem(new MenuItem(position, ((material == Material.POTION  || material == Material.SPLASH_POTION) ? potionFactory.build() : itemFactory.build())) {

                        @Override
                        public void click(Player player, ClickType clickType) {
                            data.setSound(sound);
                            Message.sendMessage(player, "setSound", Collections.singletonList(new Placeholder("%sound%", StringUtil.setUppercaseEachStart(sound.name().replace("_", " ")))));
                            player.closeInventory();
                        }
                    });
                    position += 1;
                }
            }
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
                .build()) {

            @Override
            public void click(Player player, ClickType clickType) {
                if (page - 1 > 0) {
                    new EffectSelectGui(player, page - 1, isParticle);
                }
            }
        });

        // Adds an item if the user can go back to a previous page.
        addItem(new MenuItem(51, new ItemFactory(Material.ARROW)
                .setDisplayName("&5&lNext")
                .setLore(((page + 1) <= totalPages ? "&dGo forward a page." : "&dThere are no more pages."))
                .build()) {

            @Override
            public void click(Player player, ClickType clickType) {

                if (((page + 1) <= totalPages)) {
                    new EffectSelectGui(player, page + 1, isParticle);
                }
            }
        });

        openInventory(player);
    }

    private int getPagesByPermission(Player player, int perPage, boolean isParticle) {

        int totalItems;

        if (isParticle) {
            totalItems = player.hasPermission("cursedeffects.particles.*") ? CursedEffectsPlugin.getPlugin().getEffectManager().getAvailableParticles().size() : 0;
        } else {
            totalItems = player.hasPermission("cursedeffects.sounds.*") ? CursedEffectsPlugin.getPlugin().getEffectManager().getAvailableSounds().size() : 0;
        }

        if (isParticle && !player.hasPermission("cursedeffects.particles.*")) {
            for (Particle particle : CursedEffectsPlugin.getPlugin().getEffectManager().getAvailableParticles()) {
                if (player.hasPermission(CursedEffectsPlugin.getPlugin().getConfig().getString("particles." + particle.name() + ".permission"))) {
                    totalItems += 1;
                }
            }
        }

        if (!isParticle && !player.hasPermission("cursedeffects.sounds.*")) {
            for (Sound sound : CursedEffectsPlugin.getPlugin().getEffectManager().getAvailableSounds()) {
                if (player.hasPermission(CursedEffectsPlugin.getPlugin().getConfig().getString("sounds." + sound.name() + ".permission"))) {
                    totalItems += 1;
                }
            }
        }

        int pages = (totalItems / perPage);

        if (pages < 1)
            pages = 1;

        return ((totalItems % perPage == 0 || totalItems <= perPage) ? pages : pages + 1);
    }

    private List<Particle> getParticles(int page, int perPage) {

        List<Particle> effects = new ArrayList<>();

        try {
            for (int i = ((page - 1) * perPage); i < (((page - 1) * perPage) + perPage); i++) {
                Particle particle = CursedEffectsPlugin.getPlugin().getEffectManager().getAvailableParticles().get(i);
                effects.add(particle);
            }
        } catch (IndexOutOfBoundsException e) { /* Ignored */ }

        return effects;
    }

    private List<Sound> getSounds(int page, int perPage) {

        List<Sound> effects = new ArrayList<>();

        try {
            for (int i = ((page - 1) * perPage); i < (((page - 1) * perPage) + perPage); i++) {
                Sound sound = CursedEffectsPlugin.getPlugin().getEffectManager().getAvailableSounds().get(i);
                effects.add(sound);
            }
        } catch (IndexOutOfBoundsException e) { /* Ignored */ }

        return effects;
    }
}
