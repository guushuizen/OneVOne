package me.iamguus.ovo.inventories;

import io.netty.buffer.Unpooled;
import me.iamguus.ovo.classes.GamePlayer;
import me.iamguus.ovo.handlers.PlayerHandler;
import net.minecraft.server.v1_8_R2.PacketDataSerializer;
import net.minecraft.server.v1_8_R2.PacketPlayOutCustomPayload;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

/**
 * Created by Guus on 26-1-2016.
 */
public class StatsBook {

    private static StatsBook instance;

    public ItemStack getStatsBook(Player player) {
        GamePlayer gPlayer = PlayerHandler.get().get(player.getUniqueId());
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) item.getItemMeta();
        meta.setAuthor("iAmGuus");
        meta.setTitle("" + player.getName() + "'s stats.");
        meta.addPage(ChatColor.BLUE + StringUtils.center(player.getName() + "'s stats", 14) + "\n\n" +  ChatColor.GRAY + "Kills: " + gPlayer.getKills() + ChatColor.GRAY + "\nDeaths: " + gPlayer.getDeaths());
        item.setItemMeta(meta);

        return item;
    }

    public void openBook(Player player, ItemStack item) {
        int slot = player.getInventory().getHeldItemSlot();
        ItemStack old = player.getInventory().getItem(slot);
        player.getInventory().setItem(slot, item);
        PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(Unpooled.EMPTY_BUFFER));
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
//        player.getInventory().setItem(slot, old);
    }

    public static StatsBook get() {
        if (instance == null) {
            instance = new StatsBook();
        }

        return instance;
    }
}
