package me.iamguus.ovo.classes;

import org.bukkit.Bukkit;
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
    private Kit kit;
    private int kills;
    private int deaths;

    public GamePlayer(UUID uuid, int kitpref, List<Map> mappref, ItemStack[] kitItems, ItemStack[] armorItems, int kills, int deaths) {
        this.uuid = uuid;
        this.kitpref = KitPreference.getByID(kitpref);
        this.mappref = mappref;
        this.kit = new Kit(Bukkit.getPlayer(uuid).getName() + "'s kit", kitItems, armorItems);
        this.kills = kills;
        this.deaths = deaths;
    }

    public UUID getUuid() {
        return uuid;
    }

    public KitPreference getKitPref() {
        return kitpref;
    }

    public List<Map> getMapPref() { return mappref; }

    public Kit getKit() { return this.kit; }

    public int getKills() { return kills; }

    public int getDeaths() { return deaths; }

    public void setKills(int kills) { this.kills = kills; }

    public void setDeaths(int deaths) { this.deaths = deaths; }
}
