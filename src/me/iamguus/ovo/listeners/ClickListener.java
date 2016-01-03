package me.iamguus.ovo.listeners;

import me.iamguus.ovo.classes.GamePlayer;
import me.iamguus.ovo.classes.KitPreference;
import me.iamguus.ovo.classes.Map;
import me.iamguus.ovo.handlers.MapHandler;
import me.iamguus.ovo.handlers.MySQLHandler;
import me.iamguus.ovo.handlers.PlayerHandler;
import me.iamguus.ovo.inventories.PreferencesInventory;
import me.iamguus.ovo.utils.ListUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.util.List;

/**
 * Created by Guus on 3-1-2016.
 */
public class ClickListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getType() == Material.DIODE) {
            if (player.getItemInHand().hasItemMeta()) {
                if (player.getItemInHand().getItemMeta().getDisplayName().contains("Preferences")) {
                    event.setCancelled(true);
                    player.openInventory(PreferencesInventory.get().getPreferencesInventory(player));
                }
            }
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            GamePlayer gPlayer = PlayerHandler.get().get(player.getUniqueId());
            if (event.getSlotType() != InventoryType.SlotType.OUTSIDE) {
                ItemStack currentItem = event.getCurrentItem();
                if (event.getInventory().getTitle().contains("Your Preferences")) {
                    event.setCancelled(true);
                    if (currentItem.getType() == Material.INK_SACK) {
                        int next = getNext(currentItem);
                        event.getInventory().setItem(event.getSlot(), PreferencesInventory.get().getItem(KitPreference.getByID(next)));
                        MySQLHandler.get().executeUpdate("UPDATE players SET kitpref = " + next + " WHERE uuid = '" + player.getUniqueId() + "'");
                        PlayerHandler.get().loadFromDatabase(player.getUniqueId());
                    } else
                    if (currentItem.getType() == Material.PAPER) {
                        player.closeInventory();
                        player.openInventory(PreferencesInventory.get().getMapPreferencesInv(player));
                    }
                } else
                if (event.getInventory().getTitle().contains("Map Preferences")) {
                    event.setCancelled(true);
                    if (currentItem != null) {
                        if (currentItem.getType() != Material.INK_SACK) {
                            ItemStack dye = event.getInventory().getItem(event.getSlot() + 1);
                            Map map = MapHandler.get().getByName(ChatColor.stripColor(dye.getItemMeta().getDisplayName()));
                            boolean likes = gPlayer.getMapPref().contains(map);
                            if (likes) {
                                gPlayer.getMapPref().remove(map);
                            } else {
                                gPlayer.getMapPref().add(map);
                            }
                            String sql = "UPDATE players SET mappref = '" + ListUtil.get().serialize(gPlayer.getMapPref()) + "' WHERE uuid = '" + player.getUniqueId() + "';";
                            MySQLHandler.get().executeUpdate(sql);
                        } else if (currentItem.getType() == Material.INK_SACK) {
                            Map map = MapHandler.get().getByName(ChatColor.stripColor(currentItem.getItemMeta().getDisplayName()));
                            boolean likes = gPlayer.getMapPref().contains(map);
                            if (likes) {
                                gPlayer.getMapPref().remove(map);
                            } else {
                                gPlayer.getMapPref().add(map);
                            }
                            System.out.println(gPlayer.getMapPref().size());
                            String sql = "UPDATE players SET mappref = '" + ListUtil.get().serialize(gPlayer.getMapPref()) + "' WHERE uuid = '" + player.getUniqueId() + "';";
                            MySQLHandler.get().executeUpdate(sql);
                        }
                        event.getInventory().setContents(PreferencesInventory.get().getMapPreferencesInv(player).getContents());
                    }
                }
            }
        }
    }

    private int getNext(ItemStack item) {
        if (item.getDurability() == (short) 1) {
            return 2;
        } else
        if (item.getDurability() == (short) 11) {
            return 3;
        } else
        if (item.getDurability() == (short) 6) {
            return 1;
        }
        return 1;
    }
}
