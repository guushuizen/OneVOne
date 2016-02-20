package me.iamguus.ovo.handlers;

import me.iamguus.ovo.classes.GamePlayer;
import me.iamguus.ovo.classes.Kit;
import me.iamguus.ovo.classes.KitPreference;
import me.iamguus.ovo.utils.ConfigUtil;
import me.iamguus.ovo.utils.ItemStackUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Guus on 4-1-2016.
 */
public class KitHandler {

    private static KitHandler instance;

    private List<Kit> defaultKits = new ArrayList<>();

    public void loadKit(ConfigurationSection section) {
        String name = section.getString("name");

        List<ItemStack> tempItems = new ArrayList<ItemStack>();
        for (String s : section.getStringList("items")) {
            tempItems.add(ItemStackUtil.get().deserialize(s));
            System.out.println("added item");
        }
        ItemStack[] items = new ItemStack[] {};
        items = tempItems.toArray(items);

        List<ItemStack> tempArmor = new ArrayList<ItemStack>();
        for (String s : section.getStringList("armor")) {
            tempArmor.add(ItemStackUtil.get().deserialize(s));
        }
        ItemStack[] armor = new ItemStack[] { };
        armor = tempArmor.toArray(armor);

        defaultKits.add(new Kit(name, items, armor));
    }

    public void loadAllKits() {
        if (ConfigUtil.get().getKits().contains("kits")) {
            for (String s : ConfigUtil.get().getKits().getConfigurationSection("kits").getKeys(false)) {
                loadKit(ConfigUtil.get().getKits().getConfigurationSection("kits." + s));
            }
        }
    }

    public Kit getKit(GamePlayer gPlayer1, GamePlayer gPlayer2) {
        if (gPlayer1.getKitPref() == KitPreference.OWNKIT && gPlayer2.getKitPref() == KitPreference.OWNKIT) {
            if (new Random().nextBoolean()) {
                return gPlayer1.getKit();
            } else {
                return gPlayer2.getKit();
            }
        } else
        if (gPlayer1.getKitPref() == KitPreference.OWNKIT && gPlayer2.getKitPref() == KitPreference.IDCKIT) {
            return gPlayer1.getKit();
        } else
        if (gPlayer1.getKitPref() == KitPreference.OWNKIT && gPlayer2.getKitPref() == KitPreference.RANKIT) {
            return gPlayer1.getKit();
        } else
        if (gPlayer1.getKitPref() == KitPreference.IDCKIT && gPlayer2.getKitPref() == KitPreference.OWNKIT) {
            return gPlayer2.getKit();
        } else
        if (gPlayer1.getKitPref() == KitPreference.IDCKIT && gPlayer2.getKitPref() == KitPreference.IDCKIT) {
            return defaultKits.get(new Random().nextInt(defaultKits.size()));
        } else
        if (gPlayer1.getKitPref() == KitPreference.IDCKIT && gPlayer2.getKitPref() == KitPreference.RANKIT) {
            return defaultKits.get(new Random().nextInt(defaultKits.size()));
        } else
        if (gPlayer1.getKitPref() == KitPreference.RANKIT && gPlayer2.getKitPref() == KitPreference.OWNKIT) {
            return gPlayer2.getKit();
        } else
        if (gPlayer1.getKitPref() == KitPreference.RANKIT && gPlayer2.getKitPref() == KitPreference.IDCKIT) {
            return defaultKits.get(new Random().nextInt(defaultKits.size()));
        } else
        if (gPlayer1.getKitPref() == KitPreference.RANKIT && gPlayer2.getKitPref() == KitPreference.RANKIT) {
            return defaultKits.get(new Random().nextInt(defaultKits.size()));
        }

        return defaultKits.get(new Random().nextInt(defaultKits.size()));
    }

    public static KitHandler get() {
        if (instance == null) {
            instance = new KitHandler();
        }

        return instance;
    }
}
