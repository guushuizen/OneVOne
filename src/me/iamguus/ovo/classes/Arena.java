package me.iamguus.ovo.classes;

import me.iamguus.ovo.utils.ConfigUtil;
import me.iamguus.ovo.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Guus on 2-1-2016.
 */
public class Arena {

    private String id;
    private List<Location> spawnLocs;
    private Location loc1;
    private Location loc2;
    private Map map;
    private List<UUID> ingame;
    private GameState gameState;
    private boolean enabled;
    private Queue<MatchInvite> queue;
    private int startTime;
    private Set<Block> blocks;

    public Arena(String id, List<Location> spawnLocs, Location loc1, Location loc2, Map map) {
        this(id, spawnLocs, loc1, loc2, map, false);
    }

    public Arena(String id, List<Location> spawnLocs, Location loc1, Location loc2, Map map, boolean enabled) {
        this.id = id;
        this.spawnLocs = spawnLocs;
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.map = map;
        this.ingame = new ArrayList<UUID>();
        this.gameState = GameState.EMPTY;
        this.enabled = enabled;
        this.queue = new ArrayBlockingQueue<MatchInvite>(10);
        this.startTime = 0;
        this.blocks = new HashSet<Block>();
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

    public Location getLoc1() { return loc1; }

    public Location getLoc2() { return loc2; }

    public void setLoc1(Location loc1) { this.loc1 = loc1; }

    public void setLoc2(Location loc2) { this.loc2 = loc2; }

    public Map getMap() { return this.map; }

    public List<UUID> getIngame() {
        return ingame;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState state) { this.gameState = state; }

    public boolean isEnabled() {
        return enabled;
    }

    public int getStartTime() { return startTime; }

    public void setStartTime(int startTime) { this.startTime = startTime; }

    public Set<Block> getBlocks() { return blocks; }

    public void setBlocks(Set<Block> blocks) { this.blocks = blocks; }

    public void save() {
        FileConfiguration saveTo = ConfigUtil.get().getArena();
        saveTo.set("arenas." + this.id + ".id", id);
        List<String> spawnLocsSave = new ArrayList<String>();
        for (Location loc : spawnLocs) {
            spawnLocsSave.add(LocationUtil.get().serialize(loc));
        }
        saveTo.set("arenas." + this.id + ".loc1", LocationUtil.get().serialize(loc1));
        saveTo.set("arenas." + this.id + ".loc2", LocationUtil.get().serialize(loc2));
        saveTo.set("arenas." + this.id + ".spawnlocs", spawnLocsSave);
        saveTo.set("arenas." + this.id + ".map", this.map.getName());
        saveTo.set("arenas." + this.id + ".enabled", enabled);
        ConfigUtil.get().saveArena();
    }
}
