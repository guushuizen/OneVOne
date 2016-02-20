package me.iamguus.ovo.commands;

import me.iamguus.ovo.inventories.StatsBook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Guus on 26-1-2016.
 */
public class StatsCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You can't run this command as a non-player");
            return false;
        }

        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("stats")) {
            if (args.length == 0) {
                StatsBook.get().openBook(player, StatsBook.get().getStatsBook(player));
            } else if (args.length == 1) {
                String name = args[0];
                if (Bukkit.getPlayer(name) != null) {
                    Player toUse = Bukkit.getPlayer(name);
                    StatsBook.get().openBook(player, StatsBook.get().getStatsBook(toUse));
                    return true;
                } else {
                    player.sendMessage(ChatColor.RED + "Player " + args[0] + " was not found!");
                    return false;
                }
            }
            return false;
        }

        return false;
    }
}
