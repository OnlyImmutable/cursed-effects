package com.abstractwolf.cursedeffects.utils.itemstack;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class PotionFactory extends Factory<ItemStack> {

    /** {@link ItemMeta} for the {@link ItemStack}. */
    private PotionMeta itemMeta;


    /**
     * Construct a new ItemStack from a material with an amount and byte data..
     */
    public PotionFactory(boolean splash) {
        object = new ItemStack((splash ? Material.SPLASH_POTION : Material.POTION), 1, (byte) 0);
        itemMeta = (PotionMeta) object.getItemMeta();

        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    }
    /**
     * Set the Display name of the {@link ItemStack}.
     * @param displayName - displayname.
     */
    public PotionFactory setDisplayName(String displayName) {
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        return this;
    }

    /**
     * Set the amount of the {@link ItemStack}
     * @param amount - amount
     */
    public PotionFactory setAmount(int amount) {
        object.setAmount(amount);
        return this;
    }

    /**
     * Set the lore of an {@link ItemStack}.
     * @param lore - the lore.
     */
    public PotionFactory setLore(String... lore) {
        List<String> currentLore = new ArrayList<>();
        for (String lorePart : lore) {
            currentLore.add(ChatColor.translateAlternateColorCodes('&', lorePart));
        }
        itemMeta.setLore(currentLore);
        return this;
    }

    /**
     * Append the current lore of an {@link ItemStack}.
     * @param lore - the additions to the lore.
     */
    public PotionFactory appendLore(String... lore) {
        List<String> currentLore = (itemMeta.getLore() == null ? new ArrayList<>() : itemMeta.getLore());
        for (String lorePart : lore) {
            currentLore.add(ChatColor.translateAlternateColorCodes('&', lorePart));
        }
        itemMeta.setLore(currentLore);
        return this;
    }

    /**
     * Enchant the {@link ItemStack}
     * @param enchantments - the enchantments.
     */
    public PotionFactory enchant(Enchantment... enchantments) {
        for (Enchantment enchantment : enchantments) {
            itemMeta.addEnchant(enchantment, 1, false);
        }
        return this;
    }

    /**
     * Add itemflags to {@link ItemStack}
     * @param flags - flags
     */
    public PotionFactory addItemFlag(ItemFlag... flags) {
        itemMeta.addItemFlags(flags);
        return this;
    }

    /**
     * Set the durability of an item.
     * @param durability - durability.
     */
    public PotionFactory setDurability(short durability) {
        object.setDurability(durability);
        return this;
    }

    /**
     * Make the item unbreakable.
     */
    public PotionFactory setUnbreakable() {
        itemMeta.setUnbreakable(true);
        return this;
    }

    public PotionFactory setColour(Color colour) {
        itemMeta.setColor(colour);
        itemMeta.setMainEffect(PotionEffectType.HEAL);
        return this;
    }

    /**
     * Construct the final {@link ItemStack}.
     * @return ItemStack
     */
    @Override
    public ItemStack build() {
        object.setItemMeta(itemMeta);
        return object;
    }
}
