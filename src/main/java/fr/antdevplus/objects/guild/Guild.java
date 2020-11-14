package fr.antdevplus.objects.guild;

import fr.antdevplus.Main;
import fr.antdevplus.json.SerializationManager;
import fr.antdevplus.utils.FileUtils;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Set;

public class Guild {

    static Main main;
    static FileUtils fileUtils = new FileUtils();
    static SerializationManager serializationManager = new SerializationManager();

    private String name;
    private String description;
    private int level;
    private float experience;
    private Set<String> players;
    private Set<ItemStack> guildchest;

    public Guild(String name, String description, int level, float experience, Set<String> players, Set<ItemStack> guildchest) {
        this.name = name;
        this.description = description;
        this.level = level;
        this.experience = experience;
        this.players = players;
        this.guildchest = guildchest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public float getExperience() {
        return experience;
    }

    public void setExperience(float experience) {
        this.experience = experience;
    }

    public void addExperience(float additionnal){
        float experience = this.getExperience();
        experience = experience + additionnal;
        this.setExperience(experience);
    }

    public Set<String> getPlayers() {
        return players;
    }

    public void setPlayers(Set<String> players) {
        this.players = players;
    }

    public void addPlayer(GuildPlayer guildPlayer){
        Set<String> players = this.getPlayers();
        players.add(guildPlayer.getName());
        this.setPlayers(players);
    }

    public Set<ItemStack> getGuildchest() {
        return guildchest;
    }

    public void setGuildchest(Set<ItemStack> guildchest) {
        this.guildchest = guildchest;
    }

    public void addItemInGuildInventory(ItemStack item){
        Set<ItemStack> inventoryguild = this.getGuildchest();
        inventoryguild.add(item);
        this.setGuildchest(inventoryguild);
    }
    public static Guild getGuildByName(String guildname){
        String json = fileUtils.loadContent(new File(main.saveGuildDir, guildname + ".json"));
        Guild guild = serializationManager.deserializeGuild(json);
        return guild;
    }
    public static void flush(Guild guild){
        final File guildfile = new File(main.saveGuildDir, guild.getName() +".json");
        String json = serializationManager.serializeGuild(guild);
        fileUtils.save(guildfile, json);
    }
}
