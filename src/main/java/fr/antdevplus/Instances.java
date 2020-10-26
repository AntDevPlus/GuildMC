package fr.antdevplus;

import fr.antdevplus.utils.GuildMCFunctions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Instances implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player sender = (Player) commandSender;
        GuildMCFunctions functions = new GuildMCFunctions();

        if(args[0].equalsIgnoreCase("create") && sender.hasPermission("")){

        }
        return false;
    }
}
