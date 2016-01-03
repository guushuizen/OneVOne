package me.iamguus.ovo.classes;

import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

/**
 * Created by Guus on 3-1-2016.
 */
public class GamePlayer {

    private UUID uuid;
    private KitPreference kitpref;
    private List<Map> mappref;
    private ItemStack[] kitItems;
    private ItemStack[] armorItems;

    public GamePlayer(UUID uuid, int kitpref, List<Map> mappref, ItemStack[] kitItems, ItemStack[] armorItems) {
        this.uuid = uuid;
        this.kitpref = KitPreference.getByID(kitpref);
        this.mappref = mappref;
        this.kitItems = kitItems;
        this.armorItems = armorItems;
    }

    public UUID getUuid() {
        return uuid;
    }

    public KitPreference getKitPref() {
        return kitpref;
    }

    public List<Map> getMapPref() { return mappref; }

    public ItemStack[] getKitItems() {
        return kitItems;
    }

    public ItemStack[] getArmorItems() {
        return armorItems;
    }
}
