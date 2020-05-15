package fr.antdevplus.objects;

import fr.antdevplus.utils.GuildMCFunctions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Set;

public class GuildPlayer {

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
}
