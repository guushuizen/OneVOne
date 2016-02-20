package me.iamguus.ovo.listeners;

import me.iamguus.ovo.OneVOne;
import me.iamguus.ovo.classes.Arena;
import me.iamguus.ovo.classes.GamePlayer;
import me.iamguus.ovo.classes.GameState;
import me.iamguus.ovo.handlers.ArenaHandler;
import me.iamguus.ovo.handlers.ChatHandler;
import me.iamguus.ovo.handlers.MySQLHandler;
import me.iamguus.ovo.handlers.PlayerHandler;
import me.iamguus.ovo.utils.ConfigUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

/**
 * Created by Guus on 4-1-2016.
 */
public class GameListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        event.setDeathMessage(null);
        final Arena arena = ArenaHandler.get().getArena(player);
        if (arena != null) {
            event.getDrops().clear();
            final Player opponent = Bukkit.getPlayer(getOpponent(arena.getIngame(), player.getUniqueId()));

            System.out.println(opponent == null);
            System.out.println(ChatHandler.get().sendMessage("duel.you-lost"));

//            new DecimalFormat("#.0").format(opponent.getHealth() / 2), opponent.getName()
            player.sendMessage(ChatHandler.get().sendMessage("duel.you-lost").replaceAll("%opponent%", opponent.getName()).replaceAll("%health%", new DecimalFormat("#.0").format(opponent.getHealth() / 2)));

            arena.getIngame().remove(player.getUniqueId());
            opponent.sendMessage(ChatHandler.get().sendMessage("duel.you-won"));

            GamePlayer won = PlayerHandler.get().get(opponent.getUniqueId());
            GamePlayer lost = PlayerHandler.get().get(player.getUniqueId());

            String wonSQL = "UPDATE players SET kills = " + (won.getKills() + 1) + " WHERE uuid = '" + won.getUuid() + "';";
            String lostSQL = "UPDATE players SET deaths = " + (lost.getDeaths() + 1) + " WHERE uuid = '" + lost.getUuid() + "';";

            MySQLHandler.get().executeUpdate(wonSQL);
            MySQLHandler.get().executeUpdate(lostSQL);

            won.setKills(won.getKills() + 1);
            lost.setDeaths(lost.getDeaths() + 1);

            Firework fw = (Firework) opponent.getWorld().spawnEntity(opponent.getLocation(), EntityType.FIREWORK);
            FireworkMeta fwm = fw.getFireworkMeta();

            FireworkEffect fwe = FireworkEffect.builder().withColor(Color.AQUA).withColor(Color.BLACK).withColor(Color.RED).withColor(Color.WHITE).with(FireworkEffect.Type.STAR).build();
            fwm.addEffect(fwe);
            fwm.setPower(1);
            fw.setFireworkMeta(fwm);

            new BukkitRunnable() {
                public void run() {
                    opponent.teleport(ConfigUtil.get().getLobby());
                    opponent.sendMessage(ChatHandler.get().sendMessage("duel.bye-won"));
                    opponent.getInventory().clear();
                    opponent.getInventory().setArmorContents(null);
                    heal(opponent);
                    ItemStack repeater = new ItemStack(Material.DIODE);
                    ItemMeta repeaterMeta = repeater.getItemMeta();
                    repeaterMeta.setDisplayName(ChatColor.RED + "Preferences " + ChatColor.GRAY + "(Click)");
                    repeater.setItemMeta(repeaterMeta);
                    opponent.getInventory().setItem(5, repeater);

                    ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
                    ItemMeta swordMeta = sword.getItemMeta();
                    swordMeta.setDisplayName(ChatColor.AQUA + "Invite a player " + ChatColor.GRAY + "(Click on Player)");
                    sword.setItemMeta(swordMeta);
                    opponent.getInventory().setItem(3, sword);
                    arena.getIngame().remove(opponent.getUniqueId());
                    arena.setGameState(GameState.EMPTY);

                    for (Block b : arena.getBlocks()) {
                        b.setType(Material.AIR);
                    }

                    for (Entity ent : player.getWorld().getEntities()) {
                        if (ent instanceof Arrow || ent instanceof Item) {
                            if (ArenaHandler.get().isInside(ent.getLocation(), arena.getLoc1(), arena.getLoc2())) {
                                ent.remove();
                            }
                        }
                    }
                }
            }.runTaskLater(OneVOne.getPlugin(), 100L);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        player.getInventory().clear();
        player.getEquipment().clear();
        player.teleport(ConfigUtil.get().getLobby());
        ItemStack repeater = new ItemStack(Material.DIODE);
        ItemMeta repeaterMeta = repeater.getItemMeta();
        repeaterMeta.setDisplayName(ChatColor.RED + "Preferences " + ChatColor.GRAY + "(Click)");
        repeater.setItemMeta(repeaterMeta);
        player.getInventory().setItem(5, repeater);

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta swordMeta = sword.getItemMeta();
        swordMeta.setDisplayName(ChatColor.AQUA + "Invite a player " + ChatColor.GRAY + "(Click on Player)");
        sword.setItemMeta(swordMeta);
        player.getInventory().setItem(3, sword);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(false);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Arena arena = ArenaHandler.get().getArena(player);
        if (arena != null) {
            if (arena.getBlocks().contains(event.getBlock())) {
                event.setCancelled(false);
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Arena arena = ArenaHandler.get().getArena(player);
        if (arena != null) {
            arena.getBlocks().add(event.getBlock());
        }
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        Arena arena = ArenaHandler.get().getArena(event.getBlock().getLocation());
        if (arena != null) {
            if (!arena.getBlocks().contains(event.getBlock())) {
                arena.getBlocks().add(event.getBlock());
                System.out.println("added " + event.getBlock().getType().name());
            }
            arena.getBlocks().add(event.getToBlock());
            System.out.println("added " + event.getToBlock().getType().name());

        }
    }



    private UUID getOpponent(List<UUID> ingame, UUID player) {
        for (UUID uuid : ingame) {
            if (!uuid.equals(player)) {
                return uuid;
            }
        }

        return null;
    }

    private void heal(Player player) {
        player.setHealth(player.getMaxHealth());
        for (PotionEffect pe : player.getActivePotionEffects()) {
            player.removePotionEffect(pe.getType());
        }
        player.setFoodLevel(20);
    }
}
