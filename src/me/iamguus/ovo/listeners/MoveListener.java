package me.iamguus.ovo.listeners;

import com.mewin.WGRegionEvents.events.RegionEnterEvent;
import com.mewin.WGRegionEvents.events.RegionLeaveEvent;
import com.mewin.WGRegionEvents.events.RegionLeftEvent;
import me.iamguus.ovo.OneVOne;
import me.iamguus.ovo.classes.Arena;
import me.iamguus.ovo.classes.GamePlayer;
import me.iamguus.ovo.classes.GameState;
import me.iamguus.ovo.handlers.ArenaHandler;
import me.iamguus.ovo.handlers.PlayerHandler;
import me.iamguus.ovo.inventories.KitMakerGUI;
import me.iamguus.ovo.utils.ConfigUtil;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Guus on 4-1-2016.
 */
public class MoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (ArenaHandler.get().getArena(player) != null) {
            Arena arena = ArenaHandler.get().getArena(player);
            if (arena.getGameState() == GameState.STARTING) {
                Location from = event.getFrom();
                Location to = event.getTo();
                if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
                    event.setTo(from);
                }
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (ArenaHandler.get().getArena(player) == null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onRegionEnter(RegionEnterEvent event) {
        Player player = event.getPlayer();
        if (event.getRegion().getId().contains(ConfigUtil.get().getConfig().getString("kit-region"))) {
            GamePlayer gPlayer = PlayerHandler.get().get(player.getUniqueId());
            player.setGameMode(GameMode.CREATIVE);
            player.getInventory().clear();
            player.getInventory().setContents(gPlayer.getKit().getContents());
            player.getInventory().setArmorContents(gPlayer.getKit().getArmor());
            player.sendMessage(ChatColor.GREEN + "Welcome to the KitMaker area, here you can change your kit to whatever you like!");
        }
    }

    @EventHandler
    public void onRegionLeave(final RegionLeaveEvent event) {
        final Player player = event.getPlayer();
        if (event.getRegion().getId().contains(ConfigUtil.get().getConfig().getString("kit-region"))) {
            if (event.isCancellable()) {
                new BukkitRunnable() {
                    public void run() {
                        event.setCancelled(true);
                        player.openInventory(KitMakerGUI.get().getAgreeGUI());
                    }
                }.runTaskLater(OneVOne.getPlugin(), 1L);
            }
        }
    }
}
