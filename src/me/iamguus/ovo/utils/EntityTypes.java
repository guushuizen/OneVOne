package me.iamguus.ovo.utils;

import net.minecraft.server.v1_8_R2.Entity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;

import java.util.Map;

/**
 * Created by Guus on 2-1-2016.
 */
public enum EntityTypes {
    //NAME("Entity name", Entity ID, yourcustomclass.class);
    CUSTOM_ZOMBIE("Zombie", 54, CustomZombie.class); //You can add as many as you want.

    private EntityTypes(String name, int id, Class<? extends Entity> custom) {
        addToMaps(custom, name, id);
    }

    public static void spawnEntity(Entity entity, Location loc) {
        entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftWorld) loc.getWorld()).getHandle().addEntity(entity);
    }

    private static void addToMaps(Class clazz, String name, int id) {
        //getPrivateField is the method from above.
        //Remove the lines with // in front of them if you want to override default entities (You'd have to remove the default entity from the map first though).
        ((Map) CustomZombie.getPrivateField("c", net.minecraft.server.v1_8_R2.EntityTypes.class, null)).put(name, clazz);
        ((Map) CustomZombie.getPrivateField("d", net.minecraft.server.v1_8_R2.EntityTypes.class, null)).put(clazz, name);
        //((Map)getPrivateField("e", net.minecraft.server.v1_7_R4.EntityTypes.class, null)).put(Integer.valueOf(id), clazz);
        ((Map) CustomZombie.getPrivateField("f", net.minecraft.server.v1_8_R2.EntityTypes.class, null)).put(clazz, Integer.valueOf(id));
        //((Map)getPrivateField("g", net.minecraft.server.v1_7_R4.EntityTypes.class, null)).put(name, Integer.valueOf(id));
    }
}