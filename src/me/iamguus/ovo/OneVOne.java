package me.iamguus.ovo;

import me.iamguus.ovo.commands.OvOCommands;
import me.iamguus.ovo.commands.StatsCommand;
import me.iamguus.ovo.handlers.*;
import me.iamguus.ovo.listeners.ClickListener;
import me.iamguus.ovo.listeners.GameListener;
import me.iamguus.ovo.listeners.JoinListener;
import me.iamguus.ovo.listeners.MoveListener;
import me.iamguus.ovo.utils.ConfigUtil;
import me.iamguus.ovo.utils.EntityTypes;
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
        getCommand("stats").setExecutor(new StatsCommand());

        MapHandler.get().loadAllMaps();

        ArenaHandler.get().loadAllArenas();

        KitHandler.get().loadAllKits();

        MySQLHandler.get().connect();

        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new ClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new MoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new GameListener(), this);

        PlayerHandler.get().loadAllPlayers();

        for (EntityTypes types : EntityTypes.values()) {
            EntityTypes.addToMaps(types.getCustom(), types.getName(), types.getId());
        }
    }

    public void onDisable() {
        this.p = null;
    }

    public static Plugin getPlugin() { return p; }
}