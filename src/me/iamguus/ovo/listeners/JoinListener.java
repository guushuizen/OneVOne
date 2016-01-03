package me.iamguus.ovo.listeners;

import me.iamguus.ovo.classes.GamePlayer;
import me.iamguus.ovo.handlers.MySQLHandler;
import me.iamguus.ovo.handlers.PlayerHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Guus on 3-1-2016.
 */
public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        MySQLHandler.get().createRowForPlayer(player);

        PlayerHandler.get().loadFromDatabase(player.getUniqueId());

        GamePlayer gPlayer = PlayerHandler.get().get(player.getUniqueId());

        player.getInventory().clear();

        ItemStack repeater = new ItemStack(Material.DIODE);
        ItemMeta repeaterMeta = repeater.getItemMeta();
        repeaterMeta.setDisplayName(ChatColor.RED + "Preferences " + ChatColor.GRAY + "(Right Click)");
        repeater.setItemMeta(repeaterMeta);
        player.getInventory().setItem(5, repeater);
    }
}
