package me.iamguus.ovo.listeners;

import me.iamguus.ovo.classes.*;
import me.iamguus.ovo.handlers.*;
import me.iamguus.ovo.inventories.KitMakerGUI;
import me.iamguus.ovo.inventories.PreferencesInventory;
import me.iamguus.ovo.utils.InventoryUtil;
import me.iamguus.ovo.utils.ListUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.ResultSet;
import java.util.List;

/**
 * Created by Guus on 3-1-2016.
 */
public class ClickListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getType() == Material.DIODE) {
            if (player.getItemInHand().hasItemMeta()) {
                if (player.getItemInHand().getItemMeta().getDisplayName().contains("Preferences")) {
                    event.setCancelled(true);
                    player.openInventory(PreferencesInventory.get().getPreferencesInventory(player));
                }
            }
        } else
        if (player.getItemInHand().getType() == Material.WRITTEN_BOOK) {
            player.getInventory().clear(player.getInventory().getHeldItemSlot());
        }

        if (ArenaHandler.get().getArena(player) != null) {
            Arena arena = ArenaHandler.get().getArena(player);
            if (arena.getStartTime() != -1) {
                event.setCancelled(true);
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
                if (event.getInventory().getTitle().equalsIgnoreCase("container.crafting")) {
                    if (ArenaHandler.get().getArena(player) == null) {
                        event.setResult(Event.Result.DENY);
                        event.setCancelled(true);
                    }
                }
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
                } else
                if (event.getInventory().getTitle().equalsIgnoreCase(KitMakerGUI.get().getAgreeGUI().getTitle())) {
                    event.setCancelled(true);
                    if (currentItem.getType() == Material.STAINED_CLAY) {
                        if (currentItem.getDurability() == (short) 5) {
                            String sql = "UPDATE player_kit SET kit_item = '" + InventoryUtil.itemStackArrayToBase64(player.getInventory().getContents()) + "', kit_armor = '" + InventoryUtil.itemStackArrayToBase64(player.getInventory().getArmorContents()) + "' WHERE uuid = '" + player.getUniqueId() + "';";
                            MySQLHandler.get().executeUpdate(sql);

                            gPlayer.getKit().setContents(player.getInventory().getContents());
                            gPlayer.getKit().setArmor(player.getInventory().getArmorContents());

                            player.closeInventory();

                            player.playSound(player.getLocation(), Sound.ANVIL_USE, 10, 10);

                            player.setGameMode(GameMode.SURVIVAL);

                            player.getInventory().clear();

                            player.getInventory().setArmorContents(null);

                            ItemStack repeater = new ItemStack(Material.DIODE);
                            ItemMeta repeaterMeta = repeater.getItemMeta();
                            repeaterMeta.setDisplayName(org.bukkit.ChatColor.RED + "Preferences " + org.bukkit.ChatColor.GRAY + "(Click)");
                            repeater.setItemMeta(repeaterMeta);
                            player.getInventory().setItem(5, repeater);

                            ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
                            ItemMeta swordMeta = sword.getItemMeta();
                            swordMeta.setDisplayName(org.bukkit.ChatColor.AQUA + "Invite a player " + org.bukkit.ChatColor.GRAY + "(Click on Player)");
                            sword.setItemMeta(swordMeta);
                            player.getInventory().setItem(3, sword);
                        } else
                        if (currentItem.getDurability() == (short) 14) {
                            player.closeInventory();

                            player.setGameMode(GameMode.SURVIVAL);

                            player.getInventory().clear();

                            player.getInventory().setArmorContents(null);

                            ItemStack repeater = new ItemStack(Material.DIODE);
                            ItemMeta repeaterMeta = repeater.getItemMeta();
                            repeaterMeta.setDisplayName(org.bukkit.ChatColor.RED + "Preferences " + org.bukkit.ChatColor.GRAY + "(Click)");
                            repeater.setItemMeta(repeaterMeta);
                            player.getInventory().setItem(5, repeater);

                            ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
                            ItemMeta swordMeta = sword.getItemMeta();
                            swordMeta.setDisplayName(org.bukkit.ChatColor.AQUA + "Invite a player " + org.bukkit.ChatColor.GRAY + "(Click on Player)");
                            sword.setItemMeta(swordMeta);
                            player.getInventory().setItem(3, sword);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if (event.getInventory().getTitle().contains(KitMakerGUI.get().getAgreeGUI().getTitle())) {

                player.setGameMode(GameMode.SURVIVAL);

                player.getInventory().clear();

                player.getInventory().setArmorContents(null);

                ItemStack repeater = new ItemStack(Material.DIODE);
                ItemMeta repeaterMeta = repeater.getItemMeta();
                repeaterMeta.setDisplayName(org.bukkit.ChatColor.RED + "Preferences " + org.bukkit.ChatColor.GRAY + "(Click)");
                repeater.setItemMeta(repeaterMeta);
                player.getInventory().setItem(5, repeater);

                ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
                ItemMeta swordMeta = sword.getItemMeta();
                swordMeta.setDisplayName(org.bukkit.ChatColor.AQUA + "Invite a player " + org.bukkit.ChatColor.GRAY + "(Click on Player)");
                sword.setItemMeta(swordMeta);
                player.getInventory().setItem(3, sword);
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


    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (event.getRightClicked() instanceof Player) {
            Player clicked = (Player) event.getRightClicked();
            if (player.getItemInHand().getType() == Material.DIAMOND_SWORD) {
                if (player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().getDisplayName().contains("Invite a player")) {
                    if (MatchHandler.get().getMatchInvite(clicked.getUniqueId(), player.getUniqueId()) == null) {
                        MatchHandler.get().generateInvite(player, clicked);
                        event.setCancelled(true);
                    } else {
                        MatchHandler.get().startMatch(MatchHandler.get().getMatchInvite(clicked.getUniqueId(), player.getUniqueId()));
                        event.setCancelled(true);
                    }
                }
            }
        } else
        if (event.getRightClicked() instanceof Sheep) {
            Sheep zombie = (Sheep) event.getRightClicked();
            if (zombie.getCustomName().contains("Join the queue!")) {
                MatchHandler.get().addToQueue(player.getUniqueId());
            }
        }
    }
}
