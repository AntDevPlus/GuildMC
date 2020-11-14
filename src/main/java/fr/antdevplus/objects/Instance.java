package fr.antdevplus.objects;

import fr.antdevplus.Main;
import fr.antdevplus.json.SerializationManager;
import fr.antdevplus.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.util.Iterator;

/**
 * Raid / Dungeons
 */
public class Instance {

    private String Name;
    private String WorldName;
    private int X;
    private int Y;
    private int Z;
    private Iterator<String> mobs;
    private int MaxPlayers;

    public Instance(String name, Location location, int maxplayers, Iterator<InstanceMob> mobs){
        this.Name = name;
        this.X = location.getBlockX();
        this.Y = location.getBlockY();
        this.Z = location.getBlockZ();
        this.WorldName = location.getWorld().getName();
        this.MaxPlayers = maxplayers;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public org.bukkit.Location getLocation() {
        Location loc = new Location(Bukkit.getWorld(WorldName), X, Y, Z);
        return loc;
    }

    public int getMaxPlayers() {
        return MaxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        MaxPlayers = maxPlayers;
    }

    public static void flush(Instance instance){
        final File instanceFile = new File(Main.saveInstancesDir, instance.Name +".json");
        SerializationManager serializationManager = new SerializationManager();
        String json = serializationManager.serializeInstance(instance);
        FileUtils fileUtils = new FileUtils();
        fileUtils.save(instanceFile, json);
    }
}
