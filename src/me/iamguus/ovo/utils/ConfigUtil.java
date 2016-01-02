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

    public static ConfigUtil get() {
        if (instance == null) {
            instance = new ConfigUtil();
        }

        return instance;
    }
}
