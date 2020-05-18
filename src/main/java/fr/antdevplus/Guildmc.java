package fr.antdevplus;

import fr.antdevplus.objects.Guild;
import fr.antdevplus.objects.GuildPlayer;
import fr.antdevplus.objects.GuildRole;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.antdevplus.utils.GuildMCFunctions;

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

                    functions.REQUEST.put(invitePlayer, Guild.getGuildByName(gsender.getGuild()));
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
                    if(functions.REQUEST.containsKey(sender)){

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
                if(players.contains(sender.getName()) && gsender.getGuild() != "default"){

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

            }
        } else {
            sender.sendMessage("§4 Use args: -wand");
        }
        return false;
    }
}
