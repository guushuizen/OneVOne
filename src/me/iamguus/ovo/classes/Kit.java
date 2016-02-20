package me.iamguus.ovo.classes;

import org.bukkit.inventory.ItemStack;

/**
 * Created by Guus on 4-1-2016.
 */
public class Kit {

    private String name;
    private ItemStack[] contents;
    private ItemStack[] armor;

    public Kit(String name, ItemStack[] contents, ItemStack[] armor) {
        this.name = name;
        this.contents = contents;
        this.armor = armor;
    }

    public String getName() { return this.name; }

    public ItemStack[] getContents() {
        return contents;
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public void setContents(ItemStack[] contents) { this.contents = contents; }

    public void setArmor(ItemStack[] armor) { this.armor = armor; }
}
