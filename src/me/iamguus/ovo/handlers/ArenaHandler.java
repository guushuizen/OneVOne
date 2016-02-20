package me.iamguus.ovo.handlers;

import me.iamguus.ovo.classes.Map;
import me.iamguus.ovo.classes.Arena;
import me.iamguus.ovo.utils.ConfigUtil;
import me.iamguus.ovo.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Guus on 2-1-2016.
 */
public class ArenaHandler {

    private List<Arena> arenas = new ArrayList<Arena>();

    private static ArenaHandler instance;

    public List<Arena> getArenas() { return this.arenas; }

    public Arena getArena(Player player) {
        for (Arena arena : arenas) {
            if (arena.getIngame().contains(player.getUniqueId())) {
                return arena;
            }
        }

        return null;
    }

    public Arena getArena(String name) {
        for (Arena arena : arenas) {
            if (arena.getId().contains(name)) {
                return arena;
            }
        }

        return null;
    }

    public Arena getArena(Location loc) {
        for (Arena arena : arenas) {
            if (isInside(loc, arena.getLoc1(), arena.getLoc2())) {
                return arena;
            }
        }

        return null;
    }

    public Arena getRandomArena() {
        return getArenas().get(new Random().nextInt(getArenas().size()));
    }

    public void loadAllArenas() {
        if (ConfigUtil.get().getArena().contains("arenas")) {
            for (String s : ConfigUtil.get().getArena().getConfigurationSection("arenas").getKeys(false)) {
                loadArenaFromConfig(ConfigUtil.get().getArena().getConfigurationSection("arenas." + s));
            }
        }
    }

    public Arena loadArenaFromConfig(ConfigurationSection section) {
        String id = section.getString("id");
        List<Location> spawnLocs = new ArrayList<Location>();
        for (String s : section.getStringList("spawnlocs")) {
            spawnLocs.add(LocationUtil.get().deserialize(s));
            System.out.println("added loc");
        }
        Location loc1 = LocationUtil.get().deserialize(section.getString("loc1"));
        Location loc2 = LocationUtil.get().deserialize(section.getString("loc2"));
        boolean enabled = section.getBoolean("enabled");
        Map map = MapHandler.get().getByName(section.getString("map"));
        Arena arena = new Arena(id, spawnLocs, loc1, loc2, map, enabled);
        map.getArenas().add(arena);
        arenas.add(arena);
        return arena;
    }

    public static ArenaHandler get() {
        if (instance == null) {
            instance = new ArenaHandler();
        }

        return instance;
    }

    public boolean isInside(Location loc, Location l1, Location l2) {
        int x1 = Math.min(l1.getBlockX(), l2.getBlockX());
        int y1 = Math.min(l1.getBlockY(), l2.getBlockY());
        int z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
        int x2 = Math.max(l1.getBlockX(), l2.getBlockX());
        int y2 = Math.max(l1.getBlockY(), l2.getBlockY());
        int z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());

        return loc.getX() >= x1 && loc.getX() <= x2 && loc.getY() >= y1 && loc.getY() <= y2 && loc.getZ() >= z1 && loc.getZ() <= z2;
    }
}
