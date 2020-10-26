package fr.antdevplus.objects;

import fr.antdevplus.Main;
import fr.antdevplus.json.SerializationManager;
import fr.antdevplus.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.io.File;

public class InstanceMob {

    static FileUtils fileUtils = new FileUtils();
    static SerializationManager serializationManager = new SerializationManager();
    static LivingEntity entity;

    private String mobName;
    private double lifePoint;
    private String entityType;
    //Location
    private String worldName;
    private int x, y, z;

    public InstanceMob(String mobName, int lifePoint, String entityType, String worldName, int x, int y, int z) {
        this.mobName = mobName;
        this.lifePoint = lifePoint;
        this.entityType = entityType;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        World world = Bukkit.getWorld(worldName);
        assert world != null;
        entity = (LivingEntity) world.spawnEntity(new Location(Bukkit.getWorld(worldName), x, y, z), EntityType.valueOf(EntityType.class, entityType));
        entity.setCustomName(mobName);
        entity.setMaxHealth(lifePoint);
        entity.setHealth(lifePoint);
        entity.setCustomNameVisible(true);
    }

    public static LivingEntity getEntity() {
        return entity;
    }

    public static void setEntity(LivingEntity entity) {
        InstanceMob.entity = entity;
    }

    public String getMobName() {
        return mobName;
    }

    public void setMobName(String mobName) {
        this.mobName = mobName;
    }

    public double getLifePoint() {
        return lifePoint;
    }

    public void setLifePoint(double lifePoint) {
        this.lifePoint = lifePoint;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public static InstanceMob getInstanceMobByName(String instanceMobName){
        String json = fileUtils.loadContent(new File(Main.saveInstanceMobsDir, instanceMobName + ".json"));
        InstanceMob instanceMob = serializationManager.deserializeInstanceMob(json);
        return instanceMob;
    }
    public static void flush(InstanceMob instanceMob){
        final File instanceMobFile = new File(Main.saveInstanceMobsDir, instanceMob.getMobName() +".json");
        String json = serializationManager.serializeInstanceMob(instanceMob);
        fileUtils.save(instanceMobFile, json);
    }
}
