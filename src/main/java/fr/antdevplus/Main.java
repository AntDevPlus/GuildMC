package fr.antdevplus;

import fr.antdevplus.objects.Guild;
import fr.antdevplus.objects.GuildPlayer;
import fr.antdevplus.utils.GuildMCFunctions;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {
    public static File saveGuildDir;
    public static File savePlayerDir;

    public static Main INSTANCE;
    @Override
    public void onEnable() {
        INSTANCE = this;
        System.out.println("[GuildMC] load with success");
        getCommand("guildmc").setExecutor(new Guildmc());
        getServer().getPluginManager().registerEvents(new GuildMCListener(this),this);
        this.saveDefaultConfig();
        saveGuildDir = new File(this.getDataFolder(), "/guilds");
        savePlayerDir = new File(this.getDataFolder(), "/guildPlayers");
        GuildMCFunctions.listAllGuild();
        GuildMCFunctions.listAllPlayerGuild();
    }
}
