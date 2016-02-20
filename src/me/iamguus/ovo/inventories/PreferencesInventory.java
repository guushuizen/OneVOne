package me.iamguus.ovo.inventories;

import me.iamguus.ovo.classes.KitPreference;
import me.iamguus.ovo.classes.Map;
import me.iamguus.ovo.handlers.MapHandler;
import me.iamguus.ovo.handlers.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Created by Guus on 2-1-2016.
 */
public class PreferencesInventory {

    private static PreferencesInventory instance;

    public Inventory getPreferencesInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "Your Preferences");

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i = 0; i < 9; i++) {
            inv.setItem(i, glass);
        }

        inv.setItem(9, glass);
        inv.setItem(17, glass);

        for (int i = 18; i < 27; i++) {
            inv.setItem(i, glass);
        }

        inv.setItem(12, getItem(PlayerHandler.get().get(player.getUniqueId()).getKitPref()));

        ItemStack mapPref = new ItemStack(Material.PAPER);
        ItemMeta mapMeta = mapPref.getItemMeta();
        mapMeta.setDisplayName(ChatColor.BLUE + "Map Preferences");
        mapMeta.setLore(Arrays.asList(ChatColor.GRAY + "Change the maps you like and dislike."));
        mapPref.setItemMeta(mapMeta);
        inv.setItem(14, mapPref);

        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, glass);
            }
        }

        return inv;
    }

    public ItemStack getItem(KitPreference pref) {
        if (pref.getId() == 1) {
            ItemStack item = new ItemStack(Material.INK_SACK);
            ItemMeta im = item.getItemMeta();
            item.setDurability((short) 1);
            im.setDisplayName(ChatColor.RED + "Own kit");
            im.setLore(Arrays.asList(ChatColor.GRAY + "Gives you your own kit", ChatColor.GRAY + "during a 1v1 fight.", "", ChatColor.GRAY + "Click to change."));
            item.setItemMeta(im);
            return item;
        } else
        if (pref.getId() == 2) {
            ItemStack item = new ItemStack(Material.INK_SACK);
            ItemMeta im = item.getItemMeta();
            item.setDurability((short) 11);
            im.setDisplayName(ChatColor.YELLOW + "IDC kit");
            im.setLore(Arrays.asList(ChatColor.GRAY + "Can give you your own kit", ChatColor.GRAY + "or your opponent's kit.", "", ChatColor.GRAY + "Click to change."));
            item.setItemMeta(im);
            return item;
        } else
        if (pref.getId() == 3) {
            ItemStack item = new ItemStack(Material.INK_SACK);
            ItemMeta im = item.getItemMeta();
            item.setDurability((short) 6);
            im.setDisplayName(ChatColor.BLUE + "Random Kit");
            im.setLore(Arrays.asList(ChatColor.GRAY + "Gives you your opponent's kit", ChatColor.GRAY + "or a pre-made kit.", "", ChatColor.GRAY + "Click to change."));
            item.setItemMeta(im);
            return item;
        }

        return null;
    }

    public Inventory getMapPreferencesInv(Player player) {
        int maps = MapHandler.get().getMaps().size();
        int size = 54;

        if (maps <= 3) {
            size = 9;
        } else if (maps <= 6) {
            size = 18;
        } else if (maps <= 9) {
            size = 27;
        } else if (maps <= 12) {
            size = 36;
        } else if (maps <= 15) {
            size = 45;
        } else if (maps <= 18) {
            size = 54;
        }

        Inventory inv = Bukkit.createInventory(null, size, "Map Preferences");

        int[] slots = new int[] { 0, 3, 6, 9, 12, 15, 18, 21, 24, 27, 30, 33, 36, 39, 42, 45, 48, 51 };

        for (int i = 0; i < MapHandler.get().getMaps().size(); i++) {
            Map map = MapHandler.get().getMaps().get(i);
            inv.setItem(slots[i], map.getItem());

            ItemStack item = new ItemStack(Material.INK_SACK);
            ItemMeta im = item.getItemMeta();

            if (PlayerHandler.get().get(player.getUniqueId()).getMapPref().contains(map)) {
                item.setDurability((short) 10);
                im.setDisplayName(ChatColor.GREEN + map.getName());
                im.setLore(Arrays.asList(ChatColor.GRAY + "I like it!", "", ChatColor.GRAY + "Click to dislike."));
            } else {
                item.setDurability((short) 8);
                im.setDisplayName(ChatColor.RED + map.getName());
                im.setLore(Arrays.asList(ChatColor.GRAY + "I don't like it!", "", ChatColor.GRAY + "Click to like."));
            }

            item.setItemMeta(im);
            inv.setItem(slots[i] + 1, item);
        }

        return inv;
    }

    public static PreferencesInventory get() {
        if (instance == null) {
            instance = new PreferencesInventory();
        }

        return instance;
    }
}
