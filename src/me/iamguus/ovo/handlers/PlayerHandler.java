package me.iamguus.ovo.handlers;

import me.iamguus.ovo.classes.Map;
import me.iamguus.ovo.classes.GamePlayer;
import me.iamguus.ovo.utils.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Guus on 3-1-2016.
 */
public class PlayerHandler {

    private static PlayerHandler instance;

    private java.util.Map<UUID, GamePlayer> playerMap = new HashMap<UUID, GamePlayer>();

    public void loadFromDatabase(UUID uuid) {
        int kitpref;
        List<me.iamguus.ovo.classes.Map> mappref = new ArrayList<me.iamguus.ovo.classes.Map>();
        ItemStack[] items;
        ItemStack[] armor;

        try {
            ResultSet playersRS = MySQLHandler.get().executeQuery("SELECT * FROM players WHERE uuid = '" + uuid + "';");
            playersRS.next();
            kitpref = playersRS.getInt("kitpref");
            String temp = playersRS.getString("mappref");
            for (String s : temp.split(",")) {
                mappref.add(MapHandler.get().getByName(s));
            }
            int kills = playersRS.getInt("kills");
            int deaths = playersRS.getInt("deaths");

            ResultSet kitRS = MySQLHandler.get().executeQuery("SELECT * FROM player_kit WHERE uuid = '" + uuid + "';");
            kitRS.next();
            String itemString = new String(kitRS.getBlob("kit_item").getBytes(1l, (int) kitRS.getBlob("kit_item").length()));
            items = InventoryUtil.itemStackArrayFromBase64(itemString);

            String armorString = new String(kitRS.getBlob("kit_armor").getBytes(1l, (int) kitRS.getBlob("kit_armor").length()));
            armor = InventoryUtil.itemStackArrayFromBase64(armorString);
            GamePlayer gamePlayer = new GamePlayer(uuid, kitpref, mappref, items, armor, kills, deaths);
            playerMap.put(uuid, gamePlayer);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            loadFromDatabase(player.getUniqueId());
        }
    }

    public GamePlayer get(UUID uuid) {
        return playerMap.get(uuid);
    }

    public static PlayerHandler get() {
        if (instance == null) {
            instance = new PlayerHandler();
        }

        return instance;
    }
}
