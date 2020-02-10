package com.abstractwolf.cursedeffects.utils.menu;

import com.abstractwolf.cursedeffects.CursedEffectsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public abstract class MenuFactory implements InventoryHolder
{

    private final Inventory inventory;

    private final String title;
    private final int rows;

    private List<MenuItem> items;

    private MenuItem border;

    /**
     * Construct the MenuManager.
     *
     * @param title - the title.
     * @param rows  - the rows.
     */
    public MenuFactory(String title, int rows)
    {

        this.title = title;
        this.rows = rows;

        items = new ArrayList<>();
        inventory = Bukkit.createInventory(this, (rows * 9), ChatColor.translateAlternateColorCodes('&', title));

    }

    /**
     * Add a newdb item to the inventory.
     *
     * @param item - the item.
     */
    public void addItem(MenuItem item)
    {

        items.add(item);

    }

    /**
     * Open inventory.
     *
     * @param player - the player who you open it for.
     */
    public void openInventory(Player player)
    {

        if (inventory == null)
            return;

        inventory.clear();

        List<Integer> takenSlots = new ArrayList<>();

        for (MenuItem item : items)
        {

            inventory.setItem(item.getIndex(), item.getItemStack());
            takenSlots.add(item.getIndex());

        }

        if (border != null)
        {

            int i = getRows() * 9;

            for (int j = 0; j < i; j++)
            {

                if (!takenSlots.contains(j))
                {

                    MenuItem menuItem = new MenuItem(j, border.getItemStack())
                    {

                        @Override
                        public void click(Player player, ClickType clickType)
                        {

                        }

                    };
                    inventory.setItem(j, menuItem.getItemStack());

                }

            }

        }

        new BukkitRunnable()
        {

            @Override
            public void run()
            {

                player.openInventory(inventory);

            }

        }.runTaskLater(CursedEffectsPlugin.getPlugin(), 5L);

    }

    /**
     * Get the inventory instance.
     *
     * @return Inventory
     */
    @Override
    public Inventory getInventory()
    {

        return inventory;

    }

    /**
     * Get the inventory title.
     *
     * @return Title
     */
    public String getTitle()
    {

        return title;

    }

    /**
     * Get the number of rows.
     *
     * @return Number of rows (1 - 6).
     */
    public int getRows()
    {

        return rows;

    }

    /**
     * Get items in the inventory.
     *
     * @return MenuItem - list
     */
    public List<MenuItem> getItems()
    {

        return items;

    }

    public MenuItem getBorder()
    {

        return border;

    }

    public void setBorder(MenuItem border)
    {

        this.border = border;

    }

}
