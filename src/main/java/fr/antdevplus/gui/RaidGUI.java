package fr.antdevplus.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class RaidGUI implements InventoryHolder, Listener {
    private final Inventory inv;
    public RaidGUI()
    {
        // Create a new inventory, with "this" owner for comparison with other inventories, a size of nine, called example
        inv = Bukkit.createInventory(this, 9, "§6§l[§a§lGuildMC§6§l] §r§eRaid GUI");
        // Put the items into the inventory
        initializeItems();
    }

    @Override
    public Inventory getInventory()
    {
        return inv;
    }

    // You can call this whenever you want to put the items in
    public void initializeItems()
    {
        inv.setItem(2,createGuiItem(Material.GOLD_BLOCK, "§6§l[§a§lGuildMC§6§l] §r§eTotem", "§aCreate a Totem", "LOOT: OK"));
        inv.setItem(4,createGuiItem(Material.IRON_CHESTPLATE, "§6§l[§a§lGuildMC§6§l] §r§eRaid Heroic", "§aCreate a Heroic Raid", "LOOT: GOOD"));
        inv.setItem(6,createGuiItem(Material.BARRIER, "§6§l[§a§lGuildMC§6§l] §r§eRaid Legendary", "§cCreate a Legendary Raid", "LOOT: GREAT"));
        for(int i : new int[] {0, 1, 3, 5, 7, 8}){
            inv.setItem(i,createGuiItem(Material.GRAY_STAINED_GLASS_PANE, "§6§l[§a§lGuildMC§6§l]", ""));
        }
    }

    // Nice little method to create a gui item with a custom name, and description
    protected ItemStack createGuiItem(final Material material, final String name, final String... lore)
    {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);
        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent)
    {
        ent.openInventory(inv);
    }
}
