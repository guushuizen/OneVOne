package me.iamguus.ovo;

import me.iamguus.ovo.commands.OvOCommands;
import me.iamguus.ovo.handlers.ArenaHandler;
import me.iamguus.ovo.handlers.MapHandler;
import me.iamguus.ovo.handlers.MySQLHandler;
import me.iamguus.ovo.handlers.PlayerHandler;
import me.iamguus.ovo.listeners.ClickListener;
import me.iamguus.ovo.listeners.JoinListener;
import me.iamguus.ovo.utils.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Guus on 2-1-2016.
 */
public class OneVOne extends JavaPlugin {

    private static Plugin p;

    public void onEnable() {
        this.p = this;

        ConfigUtil.get().setup(p);

        getCommand("ovo").setExecutor(new OvOCommands());

        MapHandler.get().loadAllMaps();

        ArenaHandler.get().loadAllArenas();

        MySQLHandler.get().connect();

        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new ClickListener(), this);

        PlayerHandler.get().loadAllPlayers();
    }

    public void onDisable() {
        this.p = null;
    }

    public static Plugin getPlugin() { return p; }
}