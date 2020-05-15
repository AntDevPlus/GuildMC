package fr.antdevplus;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.antdevplus.utils.GuildMCFunctions;

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
                    functions.createGuild(args[1], sender);
            }
        } else {
            sender.sendMessage("§4 Use args: -wand");
        }
        return false;
    }
}
