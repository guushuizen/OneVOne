package me.iamguus.ovo.inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Created by Guus on 4-1-2016.
 */
public class KitMakerGUI {

    private static KitMakerGUI instance;

    public Inventory getAgreeGUI() {
        Inventory inv = Bukkit.createInventory(null, 27, "Save progress?");

        ItemStack no = new ItemStack(Material.STAINED_CLAY);
        ItemMeta noMeta = no.getItemMeta();
        no.setDurability((short) 14);
        noMeta.setDisplayName(ChatColor.RED + "No, thank you!");
        noMeta.setLore(Arrays.asList("", ChatColor.GRAY + "This will undo all the changes", ChatColor.GRAY + "you made to your kit.", "", ChatColor.GRAY + "Are you sure?"));
        no.setItemMeta(noMeta);
        inv.setItem(15, no);

        ItemStack yes = new ItemStack(Material.STAINED_CLAY);
        ItemMeta yesMeta = yes.getItemMeta();
        yes.setDurability((short) 5);
        yesMeta.setDisplayName(ChatColor.GREEN + "Yes please!");
        yesMeta.setLore(Arrays.asList("", ChatColor.GRAY + "This will save all the changes", ChatColor.GRAY + "you made to your kit.", "", ChatColor.GRAY + "Are you sure?"));
        yes.setItemMeta(yesMeta);
        inv.setItem(11, yes);

        return inv;
    }

    public static KitMakerGUI get() {
        if (instance == null) {
            instance = new KitMakerGUI();
        }

        return instance;
    }
}
