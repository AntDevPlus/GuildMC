package fr.antdevplus.objects;

import fr.antdevplus.Main;
import fr.antdevplus.json.SerializationManager;
import fr.antdevplus.utils.FileUtils;
import org.bukkit.entity.Player;

import java.io.File;

public class GuildPlayer {

        static Main main;
        static FileUtils fileUtils = new FileUtils();
        static SerializationManager serializationManager = new SerializationManager();

        private String name;
        private GuildRole role;
        private Boolean isCreator;
        private String guildname;

    public GuildPlayer(String name, GuildRole role, Boolean isCreator, String guildname) {
        this.name = name;
        this.role = role;
        this.isCreator = isCreator;
        this.guildname = guildname;
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

    public Boolean getCreator() {
        return isCreator;
    }

    public void setCreator(Boolean creator) {
        isCreator = creator;
    }

    public static GuildPlayer getGuildPlayer(Player player){
        String json = fileUtils.loadContent(new File(main.savePlayerDir, player.getName() + ".json"));
        GuildPlayer guildPlayer = serializationManager.deserializeProfile(json);
        return guildPlayer;
    }
}
