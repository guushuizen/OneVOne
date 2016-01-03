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
        for (String s : section.getStringList("locs")) {
            spawnLocs.add(LocationUtil.get().deserialize(s));
        }
        boolean enabled = section.getBoolean("enabled");
        Map map = MapHandler.get().getByName(section.getString("map"));
        Arena arena = new Arena(id, spawnLocs, map, enabled);
        arenas.add(arena);
        return arena;
    }

    public static ArenaHandler get() {
        if (instance == null) {
            instance = new ArenaHandler();
        }

        return instance;
    }
}
