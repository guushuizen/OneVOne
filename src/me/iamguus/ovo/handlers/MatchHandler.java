package me.iamguus.ovo.handlers;

import me.iamguus.ovo.OneVOne;
import me.iamguus.ovo.classes.*;
import me.iamguus.ovo.classes.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Created by Guus on 2-1-2016.
 */
public class MatchHandler {

    private static MatchHandler instance;

    private List<GamePlayer> queue = new ArrayList<GamePlayer>();

    private Set<MatchInvite> matchInvites = new HashSet<MatchInvite>();

    public Set<MatchInvite> getMatchInvites() { return this.matchInvites; }

    public MatchInvite getMatchInvite(UUID inviter, UUID invited) {
        for (MatchInvite invites : matchInvites) {
            if (invites.getInviter().equals(inviter) && invites.getInvited().equals(invited)) {
                return invites;
            }
        }
        return null;
    }

    public Set<MatchInvite> getMatchInvite(UUID invited) {
        Set<MatchInvite> invites = new HashSet<MatchInvite>();
        for (MatchInvite loop : matchInvites) {
            if (loop.getInvited().equals(invited) || loop.getInviter().equals(invited)) {
                invites.add(loop);
            }
        }

        return invites;
    }

    public void generateInvite(final Player inviter, final Player invited) {
        if (getMatchInvite(inviter.getUniqueId(), invited.getUniqueId()) == null) {
            final MatchInvite invite = new MatchInvite(inviter.getUniqueId(), invited.getUniqueId());
            matchInvites.add(invite);

            inviter.sendMessage(ChatColor.GREEN + "Successfully invited " + invited.getName() + " for a private 1v1!");
            invited.sendMessage(ChatColor.GREEN + inviter.getName() + " invited you for a private 1v1! Right click " + inviter.getName() + " with your diamond sword to accept the invite.");

            new BukkitRunnable() {
                public void run() {
                    if (matchInvites.contains(invite)) {
                        matchInvites.remove(invite);
                        inviter.sendMessage(ChatColor.BLUE + "Your invite to " + invited.getName() + " has expired! ");
                    }
                }
            }.runTaskLater(OneVOne.getPlugin(), 1200L);
        } else {
            inviter.sendMessage(ChatColor.RED + "You can only send this person an request once a minute!");
        }
    }

    public void startMatch(MatchInvite invite) {
        final GamePlayer gInviter = PlayerHandler.get().get(invite.getInviter());
        final GamePlayer gInvited = PlayerHandler.get().get(invite.getInvited());
        final Player bInviter = Bukkit.getPlayer(gInviter.getUuid());
        final Player bInvited = Bukkit.getPlayer(gInvited.getUuid());

        Map map = getCommonMap(gInviter, gInvited);
        final Arena arena = getEmptyArena(map);

        if (arena == null) {
            bInvited.sendMessage(ChatColor.BLUE + "I am sorry, we can't find an empty arena at the moment!");
            bInviter.sendMessage(ChatColor.BLUE + "I am sorry, we can't find an empty arena at the moment!");
            matchInvites.remove(invite);
            return;
        }

        matchInvites.remove(invite);

        arena.getIngame().add(gInviter.getUuid());
        arena.getIngame().add(gInvited.getUuid());

        for (MatchInvite inv : getMatchInvite(invite.getInvited())) matchInvites.remove(inv);
        for (MatchInvite inv : getMatchInvite(invite.getInviter())) matchInvites.remove(inv);

        queue.remove(gInviter);
        queue.remove(gInvited);

        Kit kit = KitHandler.get().getKit(gInviter, gInvited);

        bInviter.teleport(arena.getSpawnLocs().get(0));
        bInvited.teleport(arena.getSpawnLocs().get(1));

        bInviter.setGameMode(GameMode.SURVIVAL);
        bInvited.setGameMode(GameMode.SURVIVAL);

        bInvited.getInventory().clear();
        bInviter.getInventory().clear();

        bInvited.getInventory().setContents(kit.getContents());
        bInviter.getInventory().setContents(kit.getContents());

        bInvited.getInventory().setArmorContents(kit.getArmor());
        bInviter.getInventory().setArmorContents(kit.getArmor());

        bInvited.updateInventory();
        bInviter.updateInventory();

        bInviter.sendMessage(ChatColor.GREEN + "The game is starting in 5 seconds!");

        arena.setGameState(GameState.STARTING);

        arena.setStartTime(5);

        new BukkitRunnable() {
            public void run() {
                bInvited.sendMessage(ChatColor.GREEN + "The game is starting in " + arena.getStartTime());
                bInviter.sendMessage(ChatColor.GREEN + "The game is starting in " + arena.getStartTime());

                bInvited.playSound(bInvited.getLocation(), Sound.CLICK, 1, 10);
                bInviter.playSound(bInviter.getLocation(), Sound.CLICK, 1, 10);
                if (arena.getStartTime() == 0) {
                    this.cancel();
                    arena.setGameState(GameState.INGAME);
                    bInvited.sendMessage(ChatColor.GREEN + "The game has begun! May the best win!");
                    bInviter.sendMessage(ChatColor.GREEN + "The game has begun! May the best win!");
                }
                arena.setStartTime(arena.getStartTime() - 1);
            }
        }.runTaskTimer(OneVOne.getPlugin(), 0L, 20L);
    }

    public void addToQueue(UUID uuid) {
        GamePlayer gPlayer = PlayerHandler.get().get(uuid);
        if (!queue.contains(gPlayer)) {
            queue.add(gPlayer);
            Bukkit.getPlayer(uuid).sendMessage(ChatHandler.get().sendMessage("duel.added-to-queue"));
            if (queue.size() == 2) {
                GamePlayer opponent = queue.get(0);
                MatchInvite invite = new MatchInvite(gPlayer.getUuid(), opponent.getUuid());
                Player bPlayer = Bukkit.getPlayer(gPlayer.getUuid());
                Player bOpponent = Bukkit.getPlayer(opponent.getUuid());

                bPlayer.sendMessage(ChatColor.GREEN + "You and " + bOpponent.getName() + " are dueling! Get ready to be teleported!");
                bOpponent.sendMessage(ChatColor.GREEN + "You and " + bPlayer.getName() + " are dueling! Get ready to be teleported!");

                matchInvites.add(invite);

                startMatch(invite);
            }
        } else {
            Bukkit.getPlayer(uuid).sendMessage(ChatColor.RED + "You are already in the queue!");
        }
    }

    public void removeFromQueue(UUID uuid) {
        queue.remove(PlayerHandler.get().get(uuid));
    }

    public Map getCommonMap(GamePlayer player1, GamePlayer player2) {
        for (Map map : MapHandler.get().getMaps()) {
            if (player1.getMapPref().contains(map) && player2.getMapPref().contains(map)) {
                return map;
            }
        }
        return MapHandler.get().getMaps().get(new Random().nextInt(MapHandler.get().getMaps().size()));
    }

    public Arena getEmptyArena(Map map) {
        for (Arena arena : map.getArenas()) {
            if (arena.getGameState() == GameState.EMPTY) {
                return arena;
            }
        }
        return null;
    }

    public static MatchHandler get() {
        if (instance == null) {
            instance = new MatchHandler();
        }

        return instance;
    }
}
