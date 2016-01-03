package me.iamguus.ovo.handlers;

import com.google.common.collect.Lists;
import me.iamguus.ovo.classes.Map;
import me.iamguus.ovo.utils.ConfigUtil;
import me.iamguus.ovo.utils.ItemStackUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Guus on 3-1-2016.
 */
public class MapHandler {

    private static MapHandler instance;

    private List<Map> maps = new ArrayList<Map>();

    public void loadMap(ConfigurationSection section) {
        String name = section.getString("name");
        ItemStack item = ItemStackUtil.get().deserialize(section.getString("item"));

        Map map = new Map(name, item, Lists.newArrayList());
        maps.add(map);
    }

    public void loadAllMaps() {
        if (ConfigUtil.get().getMap().contains("maps")) {
            for (String s : ConfigUtil.get().getMap().getConfigurationSection("maps").getKeys(false)) {
                loadMap(ConfigUtil.get().getMap().getConfigurationSection("maps." + s));
            }
        }
    }

    public Map getByName(String name) {
        for (Map map : maps) {
            if (map.getName().contains(name)) {
                return map;
            }
        }
        return null;
    }

    public Map getRandomMap() {
        return getMaps().get(new Random().nextInt(getMaps().size()));
    }

    public List<Map> getMaps() { return this.maps; }

    public static MapHandler get() {
        if (instance == null) {
            instance = new MapHandler();
        }

        return instance;
    }
}
