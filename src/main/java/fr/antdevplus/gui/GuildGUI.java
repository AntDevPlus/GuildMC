package fr.antdevplus.gui;

import fr.antdevplus.objects.guild.Guild;
import fr.antdevplus.objects.guild.GuildPlayer;
import fr.antdevplus.objects.relation.Relation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GuildGUI implements InventoryHolder, Listener {
    private final Inventory inv;
    public GuildGUI() {
        // Create a new inventory, with "this" owner for comparison with other inventories, a size of nine, called example
        inv = Bukkit.createInventory(this, 9, "§f[§6Informations§f]");
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
        inv.setItem(0,createGuiItem(Material.NETHER_STAR, "§dInformations"));
        inv.setItem(2,createGuiItem(Material.PLAYER_HEAD, "§aPlayers"));
        inv.setItem(8,createGuiItem(Material.DARK_OAK_SIGN, "§6Influences"));
        for(int i : new int[] {1,3,4,5,7}){
            inv.setItem(i,createGuiItem(Material.PURPLE_STAINED_GLASS_PANE, "§f[§6Informations§f]"));
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
        Player player = (Player) ent;
        GuildPlayer guildplayer = GuildPlayer.getGuildPlayer(player);
        Guild guild = Guild.getGuildByName(guildplayer.getGuild());
        inv.setItem(0,createGuiItem(Material.NETHER_STAR, "§dInformations", "-> " + " §e"+guild.getName(), "-> " +" §e" + guild.getLevel() + " level(s)"));
        inv.setItem(6,createGuiItem(Material.EXPERIENCE_BOTTLE, "§eExperience",guild.getExperience() + " exp"));
        inv.setItem(8,createGuiItem(Material.DARK_OAK_SIGN, "§6Influences", new Relation().Influence(guild) + " influence points"));
        ent.openInventory(inv);
    }
}
