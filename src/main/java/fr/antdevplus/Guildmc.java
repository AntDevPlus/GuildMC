package fr.antdevplus;

import com.sk89q.worldedit.WorldEditException;
import fr.antdevplus.gui.RaidGUI;
import fr.antdevplus.objects.Guild;
import fr.antdevplus.objects.GuildPlayer;
import fr.antdevplus.objects.GuildRole;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Player;

import fr.antdevplus.utils.GuildMCFunctions;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class Guildmc implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player sender = (Player) commandSender;
        GuildMCFunctions functions = new GuildMCFunctions();
        if(args.length > 0){
            if(args[0].equalsIgnoreCase("wand") && sender.hasPermission("guildmc.creator")){
                functions.giveGuildWand(sender);
            } else if(args[0].equalsIgnoreCase("spawn")) {
                if (args[1].equalsIgnoreCase("npc") && sender.isOp()) {
                    functions.spawnGuildNPC(sender);
                }
            } else if (args[0].equalsIgnoreCase("create") && args[1] != null) {
                GuildPlayer gsender = GuildPlayer.getGuildPlayer(sender);
                GuildRole grole = gsender.getRole();

                if(grole == GuildRole.NONGUILDED && gsender.getCreator()) {
                    functions.createGuild(args[1], sender);
                } else {
                    String[] messages = {"§6§l[§a§lGuildMC§6§l] §r§eGuildMC", ChatColor.RED + "You must leave your guild before"};
                    for(String i : messages){
                        sender.sendMessage(i);
                    }
                }
            } else if (args[0].equalsIgnoreCase("invite") && args[1] != null) {
                GuildPlayer gsender = GuildPlayer.getGuildPlayer(sender);
                GuildRole grole = gsender.getRole();
                Player invitePlayer = Bukkit.getPlayer(args[1]);
                if (grole == GuildRole.MODERATOR || grole == GuildRole.CREATOR || grole == GuildRole.ADMINISTRATOR){

                    String[] messages = {"§6§l[§a§lGuildMC§6§l] §r§eGuildMC", ChatColor.BLUE + "You have been invited to a new guild"};
                    for(String i : messages){
                        sender.sendMessage(i);
                    }

                    GuildMCFunctions.REQUEST.put(invitePlayer, Guild.getGuildByName(gsender.getGuild()));
                    /*Guild guild = Guild.getGuildByName(gsender.getGuild());
                    guild.addPlayer(GuildPlayer.getGuildPlayer(invitePlayer));
                    Guild.flush(guild);*/
                } else {
                    String[] messages = {"§6§l[§a§lGuildMC§6§l] §r§eGuildMC", ChatColor.RED + "You don't have the permission to do that !"};
                    for(String i : messages){
                        sender.sendMessage(i);
                    }
                }
            } else if(args[0].equalsIgnoreCase("accept")){
                    //sender.sendMessage(functions.REQUEST.toString());//
                    if(GuildMCFunctions.REQUEST.containsKey(sender)){

                        String[] messages = {"§6§l[§a§lGuildMC§6§l] §r§eGuildMC", ChatColor.BLUE + "Welcome in your new guild !"};
                        for(String i : messages){
                            sender.sendMessage(i);
                        }

                        Guild guild = GuildMCFunctions.REQUEST.get(sender);
                        GuildMCFunctions.REQUEST.remove(sender);
                        GuildPlayer gsender = GuildPlayer.getGuildPlayer(sender);
                        guild.addPlayer(gsender);
                        Guild.flush(guild);
                        gsender.setGuild(guild.getName());
                        gsender.setRole(GuildRole.RECRUIT);
                        GuildPlayer.flush(gsender);

                    } else {
                        String[] messages = {"§6§l[§a§lGuildMC§6§l] §r§eGuildMC", ChatColor.RED + "I don't have request for you !"};
                        for(String i : messages){
                            sender.sendMessage(i);
                        }
                    }
            } else if(args[0].equalsIgnoreCase("leave")){
                GuildPlayer gsender = GuildPlayer.getGuildPlayer(sender);
                Guild guild = Guild.getGuildByName(gsender.getGuild());

                Set<String> players = guild.getPlayers();
                if(players.contains(sender.getName()) && !gsender.getGuild().equals("default")){

                    String[] messages = {"§6§l[§a§lGuildMC§6§l] §r§eGuildMC", ChatColor.RED + "You have leave this guild"};
                    for(String i : messages){
                        sender.sendMessage(i);
                    }

                    gsender.setRole(GuildRole.NONGUILDED);
                    gsender.setGuild("Default");
                    players.remove(sender.getName());
                    guild.setPlayers(players);

                    Guild.flush(guild);
                    GuildPlayer.flush(gsender);

                } else {
                    String[] messages = {"§6§l[§a§lGuildMC§6§l] §r§eGuildMC", ChatColor.RED + "You don't have guild"};
                    for(String i : messages){
                        sender.sendMessage(i);
                    }
                }

            } else if (args[0].equalsIgnoreCase("players")) {
                GuildPlayer gsender = GuildPlayer.getGuildPlayer(sender);
                GuildRole grole = gsender.getRole();
                Guild guild = Guild.getGuildByName(gsender.getGuild());
                Set<String> plist = guild.getPlayers();
                for(String i : plist){
                    Player inList = Bukkit.getPlayer(i);
                    if ( inList != null && inList.isOnline()) {
                        sender.sendMessage(ChatColor.GREEN + i);
                    } else {
                        sender.sendMessage(ChatColor.RED + i);
                    }
                }

            } else if (args[0].equalsIgnoreCase("kick")) {
                GuildPlayer gsender = GuildPlayer.getGuildPlayer(sender);
                GuildRole grole = gsender.getRole();
                Guild guild = Guild.getGuildByName(gsender.getGuild());

                if(grole == GuildRole.MODERATOR || grole == GuildRole.ADMINISTRATOR ||grole == GuildRole.CREATOR){
                    Player kicked = Bukkit.getPlayer(args[1]);
                    if (kicked != null){
                        GuildPlayer gkicked = GuildPlayer.getGuildPlayer(kicked);
                        gkicked.setRole(GuildRole.NONGUILDED);
                        gkicked.setGuild(null);
                        Set<String> players = guild.getPlayers();
                        players.remove(kicked.getName());
                        guild.setPlayers(players);
                        sender.sendMessage("§6§l[§a§lGuildMC§6§l] §r§eGuildMC The member have been kicked");
                        Guild.flush(guild);
                        GuildPlayer.flush(gkicked);
                    }
                } else {
                    sender.sendMessage("§6§l[§a§lGuildMC§6§l] §r§eGuildMC" + ChatColor.DARK_RED + "You don't have permission");
                }
            } else if (args[0].equalsIgnoreCase("info")){
                GuildPlayer gsender = GuildPlayer.getGuildPlayer(sender);
                Guild guild = Guild.getGuildByName(gsender.getGuild());

                String[] messages = {"§6§l[§a§lGuildMC§6§l] §r§eGuildMC",
                                        ChatColor.BLUE + "Name: " + ChatColor.YELLOW + guild.getName(),
                                        ChatColor.BLUE + "Description: " + ChatColor.YELLOW + guild.getDescription(),
                                        ChatColor.BLUE + "Level: " + ChatColor.YELLOW + guild.getLevel(),
                                        ChatColor.BLUE + "Experience: " + ChatColor.YELLOW + guild.getExperience(),
                                        ChatColor.BLUE + "Players: "};
                for(String i : messages){
                    sender.sendMessage(i);
                }
                Set<String> plist = guild.getPlayers();
                for(String i : plist){
                    Player inList = Bukkit.getPlayer(i);
                    if ( inList != null && inList.isOnline()) {
                        sender.sendMessage(ChatColor.BLUE + "  ->" + ChatColor.GREEN + i);
                    } else {
                        sender.sendMessage(ChatColor.BLUE + "  ->" +ChatColor.RED + i);
                    }
                }
            } else if (args[0].equalsIgnoreCase("leveling")){
                int x = sender.getLocation().getBlockX();
                int y = sender.getLocation().getBlockY();
                int z = sender.getLocation().getBlockZ();
                sender.playSound(sender.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH,5,2);
                sender.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "title @a times 20 200 20");
                sender.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "title @a subtitle [\"\",{\"text\":\"x\",\"color\":\"aqua\"},{\"text\":\": \"},{\"text\":\""+ x +"\",\"color\":\"yellow\"},{\"text\":\" y\",\"color\":\"aqua\"},{\"text\":\": \"},{\"text\":\""+ y +"\",\"color\":\"yellow\"},{\"text\":\" z\",\"color\":\"aqua\"},{\"text\":\": \"},{\"text\":\""+ z +"\",\"color\":\"yellow\"}]");
                sender.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "title @a title [\"\",{\"text\":\"!! \",\"bold\":true,\"color\":\"green\"},{\"text\":\"New Raid \",\"bold\":true,\"color\":\"gold\"},{\"text\":\"!!\",\"bold\":true,\"color\":\"green\"}]");
                RaidGUI raidGUI = new RaidGUI();
                raidGUI.openInventory(sender);
                try {
                    functions.loadSchematic(sender, "totem");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WorldEditException e) {
                    e.printStackTrace();
                }
            }
        } else {
            sender.sendMessage("§4 Use args: -wand");
        }
        return false;
    }
}
