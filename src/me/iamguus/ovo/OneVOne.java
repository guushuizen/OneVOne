package me.iamguus.ovo;

import me.iamguus.ovo.commands.OvOCommands;
import me.iamguus.ovo.handlers.ArenaHandler;
import me.iamguus.ovo.utils.ConfigUtil;
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

        ArenaHandler.get().loadAllArenas();
    }

    public void onDisable() {
        this.p = null;
    }

    public static Plugin getPlugin() { return p; }
}
