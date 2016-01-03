package me.iamguus.ovo.commands;

import com.google.common.collect.Lists;
import me.iamguus.ovo.classes.Arena;
import me.iamguus.ovo.classes.Map;
import me.iamguus.ovo.handlers.ArenaHandler;
import me.iamguus.ovo.handlers.MapHandler;
import me.iamguus.ovo.handlers.MySQLHandler;
import me.iamguus.ovo.utils.CustomZombie;
import me.iamguus.ovo.utils.EntityTypes;
import me.iamguus.ovo.utils.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Guus on 2-1-2016.
 */
public class OvOCommands implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You can't run this commands since you are not a player!");
            return false;
        }

        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("ovo")) {
            if (player.hasPermission("ovo.admin")) {
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("createmap")) {
                        String name = args[1];
                        Map map = new Map(name, new ItemStack(Material.DIAMOND_SWORD), Lists.newArrayList());
                        map.save();
                        MapHandler.get().getMaps().add(map);
                        player.sendMessage(ChatColor.GREEN + "Successfully created map " + name + "!");
                    }
                } else
                if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("spawnpoints")) {
                        String name = args[1];
                        Arena arena = ArenaHandler.get().getArena(name);
                        if (arena != null) {
                            if (isInt(args[2])) {
                                int index = Integer.parseInt(args[2]);
                                if (index == 1 || index == 2) {
                                    arena.getSpawnLocs().add(index - 1, player.getLocation());
                                    arena.save();
                                    player.sendMessage(ChatColor.GREEN + "Successfully set spawn location " + index + " for arena " + arena.getId() + " at your current location.");
                                    return true;
                                } else {
                                    player.sendMessage(ChatColor.RED + "Index can only be 1 or 2!");
                                    return false;
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + args[2] + "is not an correct integer!");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Arena " + args[1] + "could not be found!");
                            return false;
                        }
                    } else
                    if (args[0].equalsIgnoreCase("createarena")) {
                        String name = args[1];
                        String mapname = args[2];
                        if (MapHandler.get().getByName(mapname) != null) {
                            Map map = MapHandler.get().getByName(mapname);
                            Arena arena = new Arena(name, Lists.newArrayList(), map);
                            map.getArenas().add(arena);
                            arena.save();
                            player.sendMessage(ChatColor.GREEN + "Successfully created arena " + name);
                            ArenaHandler.get().getArenas().add(arena);
                            return true;
                        }
//                        Arena arena = new Arena(name, Lists.newArrayList());
//                        arena.save();
//                        player.sendMessage(ChatColor.GREEN + "Successfully created arena " + name);
//                        ArenaHandler.get().getArenas().add(arena);
//                        return true;
                    }
                } else
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("zombie")) {
                        CustomZombie zombie = new CustomZombie(player.getWorld());
                        zombie.setCustomName(ChatColor.GREEN + "Join the queue!");
                        zombie.setCustomNameVisible(true);
                        EntityTypes.spawnEntity(zombie, player.getLocation());
//                        player.sendMessage();
                    } else
                    if (args[0].equalsIgnoreCase("inventory")) {
                        try {
                            ResultSet rs = MySQLHandler.get().executeQuery("SELECT kit_item FROM player_kit WHERE `uuid` = '" + player.getUniqueId() + "'");
                            rs.next();
                            String inv = new String(rs.getBlob("kit_item").getBytes(1l, (int) rs.getBlob("kit_item").length()));
                            ItemStack[] decodedInv = InventoryUtil.itemStackArrayFromBase64(inv);
                            player.getInventory().setContents(decodedInv);

                            ResultSet armorRS = MySQLHandler.get().executeQuery("SELECT kit_armor FROM player_kit WHERE `uuid` = '" + player.getUniqueId() + "'");
                            armorRS.next();
                            String armor = new String(armorRS.getBlob("kit_armor").getBytes(1l, (int) armorRS.getBlob("kit_armor").length()));
                            ItemStack[] armorInv = InventoryUtil.itemStackArrayFromBase64(armor);
                            player.getInventory().setArmorContents(armorInv);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "You are not allowed to execute this command!");
                return false;
            }
        }

        return false;
    }

    public boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
}
