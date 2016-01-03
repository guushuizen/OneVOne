package me.iamguus.ovo.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

/**
 * Created by Guus on 2-1-2016.
 */
public class ConfigUtil {

    private static ConfigUtil instance;

    private File pluginDir;

    private File arenaFile;
    private FileConfiguration arena;

    private File configFile;
    private FileConfiguration config;

    private File mapFile;
    private FileConfiguration map;

    public void setup(Plugin p) {
        this.pluginDir = p.getDataFolder();
        if (!this.pluginDir.exists()) {
            this.pluginDir.mkdir();
        }

        arenaFile = new File(this.pluginDir, "arenas.yml");
        if (!arenaFile.exists()) {
            try {
                arenaFile.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        arena = YamlConfiguration.loadConfiguration(arenaFile);

        configFile = new File(this.pluginDir, "config.yml");
        if (!configFile.exists()) {
            p.saveResource("config.yml", true);
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        mapFile = new File(this.pluginDir, "maps.yml");
        if (!mapFile.exists()) {
            try {
                mapFile.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        map = YamlConfiguration.loadConfiguration(mapFile);
    }

    public FileConfiguration getArena() {
        return arena;
    }

    public File getArenaFile() {
        return arenaFile;
    }

    public void saveArena() {
        try {
            this.arena.save(this.arenaFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public File getConfigFile() {
        return configFile;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            this.config.save(this.configFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public File getMapFile() {
        return mapFile;
    }

    public FileConfiguration getMap() {
        return map;
    }

    public void saveMap() {
        try {
            this.map.save(this.mapFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static ConfigUtil get() {
        if (instance == null) {
            instance = new ConfigUtil();
        }

        return instance;
    }
}
