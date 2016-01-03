package me.iamguus.ovo.classes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

/**
 * Created by Guus on 3-1-2016.
 */
public enum KitPreference {

    OWNKIT(1, "Own kit", new ItemStack(Material.INK_SACK)),
    IDCKIT(2, "IDC kit", new ItemStack(Material.INK_SACK)),
    RANKIT(3, "Random kit", new ItemStack(Material.INK_SACK));

    private int id;
    private String name;
    private ItemStack item;

    private KitPreference(int id, String name, ItemStack item) {
        this.id = id;
        this.name = name;
        this.item = item;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ItemStack getItem() {
        return item;
    }

    public static KitPreference getByID(int id) {
        for (KitPreference pref : values()) {
            if (pref.getId() == id) {
                return pref;
            }
        }
        return null;
    }
}
