package me.iamguus.ovo.commands;

import me.iamguus.ovo.classes.Arena;
import me.iamguus.ovo.classes.Map;
import me.iamguus.ovo.handlers.ArenaHandler;
import me.iamguus.ovo.handlers.ChatHandler;
import me.iamguus.ovo.handlers.MapHandler;
import me.iamguus.ovo.utils.*;
import net.minecraft.server.v1_8_R2.EnumColor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("lobby")) {
                        ConfigUtil.get().setLobby(player.getLocation());
                        player.sendMessage(ChatHandler.get().sendMessage("commands.lobby.success"));
                        return true;
                    } else
                    if (args[0].equalsIgnoreCase("zombie")) {
                        CustomSheep sheep = new CustomSheep(player.getWorld());
                        sheep.setCustomName(ChatColor.GREEN + "Join the queue!");
                        sheep.setCustomNameVisible(true);
                        sheep.setColor(EnumColor.RED);
                        Location loc = player.getLocation();
                        EntityTypes.spawnEntity(sheep, loc);
//                        ((CraftWorld) loc.getWorld()).getHandle().addEntity(sheep);
//                        sheep.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
                        System.out.println(LocationUtil.get().serialize(sheep.getBukkitEntity().getLocation()));
                        player.sendMessage(ChatHandler.get().sendMessage("commands.zombie.success"));
                        return true;
                    }
                } else
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("createmap")) {
                        String name = args[1];
                        Map map = new Map(name, new ItemStack(Material.DIAMOND_SWORD), new ArrayList<Arena>());
                        map.save();
                        MapHandler.get().getMaps().add(map);
                        player.sendMessage(ChatHandler.get().sendMessage("commands.createmap.success").replaceAll("%map%", name));
                    } else // ovo setloc1 Dome1
                    if (args[0].equalsIgnoreCase("setloc1")) {
                        Arena arena = ArenaHandler.get().getArena(args[1]);
                        if (arena != null) {
                            Block targetBlock = player.getTargetBlock((Set<Material>) null, 6);
                            if (targetBlock != null) {
                                arena.setLoc1(targetBlock.getLocation());
                                arena.save();
                                player.sendMessage(ChatColor.GREEN + "Successfully set corner 1 to the block you're looking at!");
                            }
                        } else {
                            player.sendMessage(ChatHandler.get().sendMessage("commands.spawnpoints.arena-not-found").replaceAll("%arena%", args[1]));
                            return false;
                        }
                    } else // ovo setloc1 Dome1
                        if (args[0].equalsIgnoreCase("setloc2")) {
                            Arena arena = ArenaHandler.get().getArena(args[1]);
                            if (arena != null) {
                                Block targetBlock = player.getTargetBlock((Set<Material>) null, 6);
                                if (targetBlock != null) {
                                    arena.setLoc2(targetBlock.getLocation());
                                    arena.save();
                                    player.sendMessage(ChatColor.GREEN + "Successfully set corner 2 to the block you're looking at!");
                                }
                            } else {
                                player.sendMessage(ChatHandler.get().sendMessage("commands.spawnpoints.arena-not-found").replaceAll("%arena%", args[1]));
                                return false;
                            }
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
                                    List<Location> spawnLocs = arena.getSpawnLocs();
                                    spawnLocs.add(index - 1, player.getLocation());
                                    arena.setSpawnLocs(spawnLocs);
                                    arena.save();
                                    player.sendMessage(ChatHandler.get().sendMessage("commands.spawnpoints.success").replaceAll("%index%", index + "").replaceAll("%arena%", arena.getId()));
                                    return true;
                                } else {
                                    player.sendMessage(ChatHandler.get().sendMessage("commands.spawnpoints.incorrect-index"));
                                    return false;
                                }
                            } else {
                                player.sendMessage(ChatHandler.get().sendMessage("commands.spawnpoints.not-correct-index").replaceAll("%arg%", args[2]));
                                return false;
                            }
                        } else {
                            player.sendMessage(ChatHandler.get().sendMessage("commands.spawnpoints.arena-not-found").replaceAll("%arena%", args[1]));
                            return false;
                        }
                    } else
                    if (args[0].equalsIgnoreCase("createarena")) {
                        String name = args[1];
                        String mapname = args[2];
                        if (MapHandler.get().getByName(mapname) != null) {
                            Map map = MapHandler.get().getByName(mapname);
                            Arena arena = new Arena(name, new ArrayList<Location>(), null, null, map);
                            map.getArenas().add(arena);
                            arena.save();
                            map.save();
                            player.sendMessage(ChatColor.GREEN + "Successfully created arena " + name);
                            ArenaHandler.get().getArenas().add(arena);
                            return true;
                        }
                    }
                }
            } else {
                player.sendMessage(ChatHandler.get().sendMessage("commands.no-permission"));
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
