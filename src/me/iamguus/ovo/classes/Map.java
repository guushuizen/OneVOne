package me.iamguus.ovo.classes;

import me.iamguus.ovo.utils.ConfigUtil;
import me.iamguus.ovo.utils.ItemStackUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guus on 3-1-2016.
 */
public class Map {

    private String name;
    private ItemStack item;
    private List<Arena> arenas;

    public Map(String name, ItemStack item, List<Arena> arenas) {
        this.name = name;
        this.item = item;
        this.arenas = arenas;
    }

    public String getName() {
        return name;
    }

    public ItemStack getItem() {
        return item;
    }

    public List<Arena> getArenas() {
        return arenas;
    }

    public void save() {
        ConfigUtil.get().getMap().set("maps." + this.name + ".name", this.name);
        ConfigUtil.get().getMap().set("maps." + this.name + ".item", ItemStackUtil.get().serialize(this.item));
        List<String> mapList = new ArrayList<String>();
        for (Arena arena : arenas) {
            mapList.add(arena.getId());
        }
        ConfigUtil.get().getMap().set("maps." + this.name + ".arenas", mapList);
        ConfigUtil.get().saveMap();
    }
}
