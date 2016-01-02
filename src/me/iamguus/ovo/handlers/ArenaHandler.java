package me.iamguus.ovo.handlers;

import me.iamguus.ovo.classes.Arena;
import me.iamguus.ovo.utils.ConfigUtil;
import me.iamguus.ovo.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Guus on 2-1-2016.
 */
public class ArenaHandler {

    private Set<Arena> arenas = new HashSet<Arena>();

    private static ArenaHandler instance;

    public Set<Arena> getArenas() { return this.arenas; }

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

    public void loadAllArenas() {
        for (String s : ConfigUtil.get().getArena().getConfigurationSection("arenas").getKeys(false)) {
            loadArenaFromConfig(ConfigUtil.get().getArena().getConfigurationSection("arenas." + s));
        }
    }

    public Arena loadArenaFromConfig(ConfigurationSection section) {
        String id = section.getString("id");
        List<Location> spawnLocs = new ArrayList<Location>();
        for (String s : section.getStringList("locs")) {
            spawnLocs.add(LocationUtil.get().deserialize(s));
        }
        boolean enabled = section.getBoolean("enabled");
        Arena arena = new Arena(id, spawnLocs, enabled);
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
