package fr.antdevplus.objects;

import org.bukkit.Location;

import java.util.Iterator;

public class Instance {

    private String Name;
    private Location Location;
    private int MaxPlayers;

    public Instance(String name, Location location, int maxplayers, Iterator<InstanceMob> mobs){
        this.Name = name;
        this.Location = location;
        this.MaxPlayers = maxplayers;
    }

}
