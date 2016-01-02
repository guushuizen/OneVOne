package me.iamguus.ovo.classes;

import me.iamguus.ovo.handlers.ArenaHandler;
import me.iamguus.ovo.utils.ConfigUtil;
import me.iamguus.ovo.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Guus on 2-1-2016.
 */
public class Arena {

    private String id;
    private List<Location> spawnLocs;
    private List<UUID> ingame;
    private GameState gameState;
    private boolean enabled;
    private Queue<MatchInvite> queue;

    public Arena(String id, List<Location> spawnLocs) {
        this(id, spawnLocs, false);
    }

    public Arena(String id, List<Location> spawnLocs, boolean enabled) {
        this.id = id;
        this.spawnLocs = spawnLocs;
        this.enabled = enabled;
        this.queue = new ArrayBlockingQueue<MatchInvite>(10);

    }

    public String getId() {
        return id;
    }

    public List<Location> getSpawnLocs() {
        return spawnLocs;
    }

    public void setSpawnLocs(List<Location> spawnLocs) {
        this.spawnLocs = spawnLocs;
    }

    public List<UUID> getIngame() {
        return ingame;
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void save() {
        FileConfiguration saveTo = ConfigUtil.get().getArena();
        saveTo.set("arenas." + this.id + ".id", id);
        List<String> spawnLocsSave = new ArrayList<String>();
        for (Location loc : this.spawnLocs) {
            spawnLocsSave.add(LocationUtil.get().serialize(loc));
        }
        saveTo.set("arenas." + this.id + ".spawnlocs", spawnLocsSave);
        saveTo.set("arenas." + this.id + ".enabled", enabled);
        ConfigUtil.get().saveArena();
    }
}
