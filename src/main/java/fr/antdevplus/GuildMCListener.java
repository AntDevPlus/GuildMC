package fr.antdevplus;

import fr.antdevplus.gui.MasterGUI;
import fr.antdevplus.json.SerializationManager;
import fr.antdevplus.objects.guild.Guild;
import fr.antdevplus.objects.guild.GuildPlayer;
import fr.antdevplus.objects.guild.GuildRole;
import fr.antdevplus.utils.FileUtils;
import fr.antdevplus.utils.GuildMCFunctions;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;

/**
 * This file is the Listener of this plugin, working with the server Listener of Bukkit/Spigot
 */
public class GuildMCListener implements Listener {

    private Main plugin;
    public GuildMCListener(Main main) {
        this.plugin = main;
    }

    @EventHandler //click on NPCMaster
    public void onPlayerInterac(PlayerInteractEntityEvent event){

        if(event.getRightClicked().getType() == EntityType.VILLAGER)
        {
            Player p = event.getPlayer();
            Villager npc = (Villager) event.getRightClicked();
            if(npc.getCustomName().contains("Master")){
                MasterGUI gui = new MasterGUI();
                gui.openInventory(p);
            }
        }
    }

    /**
     * Player click item on this inventory
     * @param e
     * InventoryClickEvent
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e)
    {
        Player player = (Player) e.getWhoClicked();
        if(player.getOpenInventory().getTitle() == "§6§l[§a§lGuildMC§6§l] §r§eGuild Master" || player.getOpenInventory().getTitle() == "§6§l[§a§lGuildMC§6§l] §r§eRaid GUI"){
            if(e.getCurrentItem().getType() == Material.NETHER_STAR){
                GuildMCFunctions functions = new GuildMCFunctions();
                functions.addPlayerToCreatorList(player);
            }
            e.setCancelled(true);
        }
    }

    /**
     * Event when a player join the server
     * @param e
     */
    @EventHandler
    public void onPlayerConnect(PlayerJoinEvent e){
        Player p = e.getPlayer();
        GuildMCFunctions functions = new GuildMCFunctions();
        if (p.hasPlayedBefore()){
            functions.displayGuildInfo(p);
            GuildPlayer gp = GuildPlayer.getGuildPlayer(p);
            if(gp.getRole() != GuildRole.NONGUILDED) {
                Guild guild = Guild.getGuildByName(gp.getGuild());
                String[] messages = {"§6§l[§a§lGuildMC§6§l] §r§e==-==-==-==-==-==-==-==-==-==-==", ChatColor.BLUE + "-= " + guild.getName() + " =-", ChatColor.AQUA + "Your role: " + gp.getRole(), ChatColor.GREEN + "Guild Level: " + guild.getExperience()};
                for (String i : messages) {
                    p.sendMessage(i);
                }
            } else {
                p.sendMessage("§f[§aGuildMC§f] You don't have guild.");
            }
        } else {
            GuildPlayer newgplayer = new GuildPlayer(p.getName(),GuildRole.NONGUILDED,false, "default");
            SerializationManager serializationManager = new SerializationManager();
            String json = serializationManager.serializeGuildProfile(newgplayer);
            File file = new File(Main.savePlayerDir, p.getName()+".json");
            FileUtils.save(file,json);
        }

    }

    /**
     * When entity was killed
     * @param e
     */
    @EventHandler
    void OnEntityDeath(EntityDeathEvent e){
        Entity killer = e.getEntity().getKiller();
        int exp = e.getDroppedExp();
        if(killer instanceof Player){
            GuildMCFunctions functions = new GuildMCFunctions();
            functions.spawnExperiencesInfos((Player) killer, exp);
        }

    }

    /**
     * When sign have been changed
     * @param e
     */
    @EventHandler
    void onSignChange(SignChangeEvent e){
        Player player = e.getPlayer();
        if (player.hasPermission("guildmc.createRaid")){
            if (e.getLine(0).equalsIgnoreCase("[RAID]")) {
                e.setLine(0,ChatColor.WHITE + "[" + ChatColor.GOLD + "RAID" + ChatColor.WHITE + "]");
                e.setLine(1, ChatColor.WHITE + e.getLine(1));
                e.setLine(2, ChatColor.WHITE + e.getLine(2));
                e.setLine(3, ChatColor.WHITE + e.getLine(3));
                GuildMCFunctions functions = new GuildMCFunctions();
                functions.displayRaidInfos(player);
            }
        } else {
            e.setCancelled(true);
        }
    }

    /**
     * When player interact with entity
     * @param e
     */
    @EventHandler
    void onPlayerInteract(PlayerInteractEvent e){
        Player player = e.getPlayer();
        Block rawblock = e.getClickedBlock();
        Material block = null;
        if(rawblock != null){ block = rawblock.getType();}
        if(block != null && block == Material.DARK_OAK_WALL_SIGN){
            Sign sign = (Sign) e.getClickedBlock().getState();
            if(sign.getLine(0).equalsIgnoreCase(ChatColor.WHITE + "[" + ChatColor.GOLD + "RAID" + ChatColor.WHITE + "]"))
            {
                player.sendMessage("gg");
            }
        }
    }
}
