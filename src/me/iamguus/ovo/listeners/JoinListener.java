package me.iamguus.ovo.listeners;

import me.iamguus.ovo.classes.GamePlayer;
import me.iamguus.ovo.handlers.MatchHandler;
import me.iamguus.ovo.handlers.MySQLHandler;
import me.iamguus.ovo.handlers.PlayerHandler;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Guus on 3-1-2016.
 */
public class JoinListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        MySQLHandler.get().createRowForPlayer(player);

        PlayerHandler.get().loadFromDatabase(player.getUniqueId());

        GamePlayer gPlayer = PlayerHandler.get().get(player.getUniqueId());

        player.setGameMode(GameMode.SURVIVAL);

        player.getInventory().clear();

        player.getEquipment().clear();

        ItemStack repeater = new ItemStack(Material.DIODE);
        ItemMeta repeaterMeta = repeater.getItemMeta();
        repeaterMeta.setDisplayName(ChatColor.RED + "Preferences " + ChatColor.GRAY + "(Click)");
        repeater.setItemMeta(repeaterMeta);
        player.getInventory().setItem(5, repeater);

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta swordMeta = sword.getItemMeta();
        swordMeta.setDisplayName(ChatColor.AQUA + "Invite a player " + ChatColor.GRAY + "(Click on Player)");
        sword.setItemMeta(swordMeta);
        player.getInventory().setItem(3, sword);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        MatchHandler.get().removeFromQueue(event.getPlayer().getUniqueId());
    }
}
