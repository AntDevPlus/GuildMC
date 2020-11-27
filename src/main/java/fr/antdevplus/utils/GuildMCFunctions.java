package fr.antdevplus.utils;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import fr.antdevplus.Main;
import fr.antdevplus.json.SerializationManager;
import fr.antdevplus.objects.guild.Guild;
import fr.antdevplus.objects.guild.GuildPlayer;
import fr.antdevplus.objects.guild.GuildRole;
import fr.antdevplus.objects.instance.Instance;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author AntDevPlus
 * @version 1.0
 * This class references all function of the plugin
 */
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

    /**
     * Give the powerfull wand to op
     * @param gamemaster
     * Just a bukkit/spigot player
     * @see Player
     */
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

    /**
     * Spawn the NPC "Master of Guild" who display MasterGUi
     * @param gamemaster
     * krakito
     * @see Player
     * @see fr.antdevplus.gui.MasterGUI
     */
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

    /**
     * Save player in List who player have the creator power
     * @param player
     * the player that must be add in creatorList
     */
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

    /**
     * Create {@link Guild} by a creator
     * @param guildName
     * {@link Guild} name
     * @param creator
     * Player who is in creatorList
     */
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

    /**
     * Save all {@link Guild} names in a list
     */
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

    /**
     * Stock all {@link GuildPlayer}
     */
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

    /**
     * Display to player informations from his {@link Guild}
     * @param sender
     * {@link Player}
     */
    public void displayGuildInfo(Player sender){
        GuildPlayer gsender = GuildPlayer.getGuildPlayer(sender);
        Guild guild = Guild.getGuildByName(gsender.getGuild());

        String[] messages = {ChatColor.LIGHT_PURPLE + guild.getName(),
                ChatColor.BLUE + "Name: " + ChatColor.WHITE + guild.getName(),
                ChatColor.BLUE + "Description: " + ChatColor.WHITE + guild.getDescription(),
                ChatColor.BLUE + "Level: " + ChatColor.WHITE + guild.getLevel(),
                ChatColor.BLUE + "Experience: " + ChatColor.WHITE + guild.getExperience(),
                ChatColor.BLUE + "Players: "};
        for(String i : messages){
            sender.sendMessage(i);
        }
        Set<String> plist = guild.getPlayers();
        for(String i : plist){
            Player inList = Bukkit.getPlayer(i);
            if ( inList != null && inList.isOnline()) {
                sender.sendMessage(ChatColor.WHITE + "  -> " + ChatColor.GREEN + i);
            } else {
                sender.sendMessage(ChatColor.WHITE + "  -> " +ChatColor.RED + i);
            }
        }
    }

    /**
     * Invite {@link Player} but this method have been abandonned
     * @param guildPlayer
     * {@link GuildPlayer}
     * @param guild
     * {@link Guild}
     */
    public void invitePlayer(GuildPlayer guildPlayer, Guild guild){

    }

    /**
     * Send to {@link GuildPlayer} a little message who say the xp give to his {@link Guild}
     * @param player
     * Spigot player
     * @param exp
     * Generaly xp drops by entity who has been killed.
     */
    public void spawnExperiencesInfos(Player player, float exp){
        GuildPlayer gplayer = GuildPlayer.getGuildPlayer(player);
        if(gplayer.getRole() != GuildRole.NONGUILDED) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder("§f[§a+§f] §c" + exp +"§f exp").create());
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 2);

            Guild guild = Guild.getGuildByName(gplayer.getGuild());
            guild.setExperience(guild.getExperience() + exp);
            Guild.flush(guild);
        }

    }

    /**
     *
     * @param player
     * Spigot/Bukkit Player
     * @param structure
     * structure extract from schematics name
     * @throws IOException
     * If the schematics file can't be find
     * @throws WorldEditException
     * Please see the WorldEdit documentation
     */
    public void loadSchematic(Player player, String structure) throws IOException, WorldEditException {

        Location location = player.getLocation();
        WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");

        Clipboard clipboard;

        File file = new File(worldEditPlugin.getDataFolder(), "/schematics/"+structure+".schem");
        if (!file.exists()){System.out.println("EXISTE PAS !!!!!");}
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            clipboard = reader.read();
        }
        World world = new BukkitWorld(player.getWorld());
        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1)) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(location.getX(), location.getY(), location.getZ()))
                    .ignoreAirBlocks(false)
                    .build();
            Operations.complete(operation);
        }
    }

    /**
     * Send Informations when RAID comes
     * @param sender
     * Spigot/Bukkit Player
     */
    public void displayRaidInfos(Player sender) {
        int x = sender.getLocation().getBlockX();
        int y = sender.getLocation().getBlockY();
        int z = sender.getLocation().getBlockZ();
        sender.playSound(sender.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH,5,2);
        sender.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "title @a times 20 200 20");
        sender.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "title @a subtitle [\"\",{\"text\":\"x\",\"color\":\"aqua\"},{\"text\":\": \"},{\"text\":\""+ x +"\",\"color\":\"yellow\"},{\"text\":\" y\",\"color\":\"aqua\"},{\"text\":\": \"},{\"text\":\""+ y +"\",\"color\":\"yellow\"},{\"text\":\" z\",\"color\":\"aqua\"},{\"text\":\": \"},{\"text\":\""+ z +"\",\"color\":\"yellow\"}]");
        sender.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "title @a title [\"\",{\"text\":\"!! \",\"bold\":true,\"color\":\"green\"},{\"text\":\"New Raid \",\"bold\":true,\"color\":\"gold\"},{\"text\":\"!!\",\"bold\":true,\"color\":\"green\"}]");
    }

    public void createWorld(String instanceName, Player player){
        if (Bukkit.getWorld(instanceName) == null ) {
            WorldCreator worldcreator = new WorldCreator(instanceName);
            worldcreator.type(WorldType.FLAT);
            worldcreator.generateStructures(false);
            worldcreator.createWorld();
            player.teleport(Bukkit.getWorld(instanceName).getSpawnLocation());
            Bukkit.getLogger().warning("A new instance have been created !");
            Instance instance = new Instance(instanceName, Bukkit.getWorld(instanceName).getSpawnLocation(), 5, null);
            Instance.flush(instance);
        } else {
            player.teleport(Bukkit.getWorld(instanceName).getSpawnLocation());
            Instance instance = new Instance(instanceName, Bukkit.getWorld(instanceName).getSpawnLocation(), 5, null);
            Instance.flush(instance);
        }
    }
}
