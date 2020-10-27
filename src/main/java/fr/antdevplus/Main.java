package fr.antdevplus;

import fr.antdevplus.objects.Guild;
import fr.antdevplus.objects.GuildPlayer;
import fr.antdevplus.utils.GuildMCFunctions;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Set;

public class Main extends JavaPlugin {
    public static File saveGuildDir;
    public static File savePlayerDir;
    public static Main INSTANCE;
    public static File saveInstancesDir;
    public static File saveSchematicsDir;
    public static File saveInstanceMobsDir;

    public void createDir(){
        if (!savePlayerDir.exists()){
            savePlayerDir.mkdir();
        }
        if (!saveGuildDir.exists()){
            saveGuildDir.mkdir();
        }
        if (!saveInstanceMobsDir.exists()){
            saveInstanceMobsDir.mkdir();
        }
        if (!saveInstancesDir.exists()){
            saveInstancesDir.mkdir();
        }
        if (!saveSchematicsDir.exists()){
            saveInstancesDir.mkdir();
        }
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        System.out.println("[GuildMC] load with success");
        getCommand("guildmc").setExecutor(new Guildmc());
        getCommand("instances").setExecutor(new Instances());
        getServer().getPluginManager().registerEvents(new GuildMCListener(this),this);
        this.saveDefaultConfig();
        saveGuildDir = new File(this.getDataFolder(), "/guilds");
        savePlayerDir = new File(this.getDataFolder(), "/guildPlayers");
        saveInstancesDir = new File(this.getDataFolder(), "/instances");
        saveSchematicsDir = new File(this.getDataFolder(), "/schematics");
        saveInstanceMobsDir = new File(this.getDataFolder(), "/instanceMobs");
        createDir();
        GuildMCFunctions.listAllGuild();
        GuildMCFunctions.listAllPlayerGuild();
    }
}
