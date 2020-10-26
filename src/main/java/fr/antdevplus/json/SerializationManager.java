package fr.antdevplus.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.antdevplus.objects.Guild;
import fr.antdevplus.objects.GuildPlayer;
import fr.antdevplus.objects.InstanceMob;

public class SerializationManager {

    private Gson gson;

    public SerializationManager() {
        this.gson = createGsonInstance();
    }

    private Gson createGsonInstance() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .disableHtmlEscaping()
                .create();
    }

    public String serializeGuild(Guild guild){
        return this.gson.toJson(guild);
    }

    public Guild deserializeGuild(String json){
        return this.gson.fromJson(json, Guild.class);
    }

    public String serializeGuildProfile(GuildPlayer guildPlayer){
        return this.gson.toJson(guildPlayer);
    }

    public GuildPlayer deserializeProfile(String json){
        return this.gson.fromJson(json, GuildPlayer.class);
    }

    public String serializeInstanceMob(InstanceMob instanceMob){
        return this.gson.toJson(instanceMob);
    }

    public InstanceMob deserializeInstanceMob(String json){
        return this.gson.fromJson(json, InstanceMob.class);
    }

}
