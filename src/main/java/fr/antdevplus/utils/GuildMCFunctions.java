package fr.antdevplus.utils;

import fr.antdevplus.Main;
import fr.antdevplus.json.SerializationManager;
import fr.antdevplus.objects.Guild;
import fr.antdevplus.objects.GuildPlayer;
import fr.antdevplus.objects.GuildRole;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.*;

public class GuildMCFunctions {

    public static Set<Guild> GUILDLIST = new HashSet<Guild>();
    public static Set<GuildPlayer> GUILDPLAYERLIST = new HashSet<GuildPlayer>();
    public static HashMap<Player, Guild> REQUEST = new HashMap<Player, Guild>();
    static Main main;
    public static ArrayList<String> CREATOR_LIST = new ArrayList<String>();
    SerializationManager serializationManager = new SerializationManager();
    FileUtils fileUtils = new FileUtils();

    public GuildMCFunctions() {
    }

    public void giveGuildWand(Player gamemaster){
        String WAND_NAME = "§6§l[§a§lGuildMC§6§l] §r§eMaster Wand";

        ItemStack wand = new ItemStack(Material.STICK, 1);
        ItemMeta wandMeta = wand.getItemMeta();
        wandMeta.setDisplayName(WAND_NAME);
        wandMeta.setLore(Collections.singletonList("This is a admin tool."));
        wandMeta.addEnchant(Enchantment.SILK_TOUCH,1,true);
        wand.setItemMeta(wandMeta);
        gamemaster.getInventory().addItem(wand);
    }
    public void spawnGuildNPC(Player gamemaster){
        Villager guildnpc = (Villager) gamemaster.getWorld().spawnEntity(gamemaster.getLocation(), EntityType.VILLAGER);
        guildnpc.setCanPickupItems(false);
        guildnpc.setAI(false);
        guildnpc.setInvulnerable(true);
        guildnpc.setCustomName("§6§l[§a§lGuildMC§6§l] §r§eMaster of Guild");
        guildnpc.setCustomNameVisible(true);
        guildnpc.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200000, 255));
    }
    public void displayCreatorInformations(Player player){
        String[] messages = {"§6§l[§a§lGuildMC§6§l] §r§eMaster of Guild", ChatColor.BLUE + "use /guildmc create [name]"};
        for(String i : messages){
            player.sendMessage(i);
        }
    }
    public void addPlayerToCreatorList(Player player){

        GuildPlayer gplayer = GuildPlayer.getGuildPlayer(player);

        if(gplayer.getCreator()){
            String[] messages = {"§6§l[§a§lGuildMC§6§l] §r§eGuildMC", ChatColor.DARK_GREEN + "You have already the orignal guild power"};
            for(String i : messages){
                player.sendMessage(i);
            }
        } else {
            String[] messages = {"§6§l[§a§lGuildMC§6§l] §r§eGuildMC", ChatColor.DARK_GREEN + "You can create guild now !"};
            for(String i : messages){
                player.sendMessage(i);
                gplayer.setCreator(true);
                GuildPlayer.flush(gplayer);
            }

        }
    }
    public void createGuild(String guildName, Player creator){

        final File guildfile = new File(main.saveGuildDir, guildName +".json");
        final File guildplayerfile = new File(main.savePlayerDir, creator.getName() +".json");
        final SerializationManager serializationManager = new SerializationManager();
        final Set<String> members = new HashSet<String>();

        members.add(creator.getName());
        final Guild newguild = new Guild(guildName, "",1,00.0f, members,null);
        final GuildPlayer gcreator = new GuildPlayer(creator.getName(), GuildRole.CREATOR, true, newguild.getName());

        String guildjson = serializationManager.serializeGuild(newguild);
        FileUtils.save(guildfile, guildjson);

        String guildplayerjson = serializationManager.serializeGuildProfile(gcreator);
        FileUtils.save(guildplayerfile, guildplayerjson);

        String[] messages = {"§6§l[§a§lGuildMC§6§l] §r§eGuildMC", ChatColor.BLUE + "Your Guild guild have been created"};
        for(String i : messages){
            creator.sendMessage(i);
        }
        for( Player iplayer : Bukkit.getOnlinePlayers() ){
            iplayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder("A new Guild Have been created: " + newguild.getName()).color(net.md_5.bungee.api.ChatColor.AQUA).create());
        }
    }
    public static void listAllGuild(){
        SerializationManager serializationManager = new SerializationManager();
        FileUtils guildFileSystem = new FileUtils();
        File guilddir = new File(main.saveGuildDir,"");
        File[] files= guilddir.listFiles();
        for(File i : files){
                Guild guild = serializationManager.deserializeGuild(guildFileSystem.loadContent(i));
                GUILDLIST.add(guild);
        }
    }
    public static void listAllPlayerGuild(){
        SerializationManager serializationManager = new SerializationManager();
        FileUtils guildFileSystem = new FileUtils();
        File guilddir = new File(main.savePlayerDir,"");
        File[] files= guilddir.listFiles();
        for(File i : files){
            GuildPlayer guildPlayer = serializationManager.deserializeProfile(guildFileSystem.loadContent(i));
            GUILDPLAYERLIST.add(guildPlayer);
        }
    }
    public void displayGuildInfo(Player player){
        String json = fileUtils.loadContent(new File(main.savePlayerDir, player.getName() + ".json"));
        GuildPlayer guildPlayer = serializationManager.deserializeProfile(json);
            //player.sendMessage(guildPlayer.getRole().toString());
        }
    public void invitePlayer(GuildPlayer guildPlayer, Guild guild){

    }
    public void spawnExperiencesInfos(Player player, float exp){
        GuildPlayer gplayer = GuildPlayer.getGuildPlayer(player);
        if(gplayer.getRole() != GuildRole.NONGUILDED) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder("You have kill entity, the experience given by this one partly returns to your guild").color(net.md_5.bungee.api.ChatColor.GREEN).create());
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 2);

            Guild guild = Guild.getGuildByName(gplayer.getGuild());
            guild.setExperience(guild.getExperience() + exp);
            Guild.flush(guild);
        }

    }
    
}
