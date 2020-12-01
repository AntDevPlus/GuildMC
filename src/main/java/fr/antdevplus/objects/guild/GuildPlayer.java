package fr.antdevplus.objects.guild;

import fr.antdevplus.Main;
import fr.antdevplus.json.SerializationManager;
import fr.antdevplus.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: AntDev+
 * @version: 1.0
 */
public class GuildPlayer {

        static Main main;
        static FileUtils fileUtils = new FileUtils();
        static SerializationManager serializationManager = new SerializationManager();

        private String name;
        private GuildRole role;
        private Boolean isCreator;
        private String guildname;
        private Set<String> PlayerKilled;

    /**
     * @param name
     * Name of player
     * @param role
     * Role of the player
     * @see GuildRole
     * @param isCreator
     * If he has creator power
     * @param guildname
     * Name of the guild
     */
    public GuildPlayer(String name, GuildRole role, Boolean isCreator, String guildname) {
        this.name = name;
        this.role = role;
        this.isCreator = isCreator;
        this.guildname = guildname;
        this.PlayerKilled = new HashSet<>();
    }

    public String getGuild() {
        return guildname;
    }

    public void setGuild(String guild) {
        this.guildname = guild;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GuildRole getRole() {
        return role;
    }

    public void setRole(GuildRole role) {
        this.role = role;
    }

    public void addKill(String name){
        this.PlayerKilled.add(name);
    }

    public Boolean getCreator() {
        return isCreator;
    }

    public void setCreator(Boolean creator) {
        isCreator = creator;
    }

    /**
     * Method to unserialize GuildPlayer from JSON
     * @param player
     * Minecraft player instance
     * @see Player
     * @return GuildPlayer deserialize from JSON
     */
    public static GuildPlayer getGuildPlayer(Player player){
        File file = new File(main.savePlayerDir, player.getName() + ".json");
        String json = fileUtils.loadContent(file);
        GuildPlayer guildPlayer = serializationManager.deserializeProfile(json);
        return guildPlayer;
    }

    /**
     * Method to serialize the JSON of GuildPlayer
     * @see FileUtils
     * @param guildPlayer
     * serialize the GuildPlayer
     * Uses: GuildPlayer.flush(guildPlayer) = Generates in /GuildPlayers/player.json
     */
    public static void flush(GuildPlayer guildPlayer){
        final File guildfile = new File(Main.savePlayerDir, guildPlayer.getName() +".json");
        String json = serializationManager.serializeGuildProfile(guildPlayer);
        FileUtils.save(guildfile, json);
    }
}
