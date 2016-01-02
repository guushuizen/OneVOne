package me.iamguus.ovo.listeners;

import me.iamguus.ovo.handlers.MatchHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * Created by Guus on 2-1-2016.
 */
public class InviteListener implements Listener {

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (event.getRightClicked() instanceof Player) {
            Player clicked = (Player) event.getRightClicked();
            if (player.getItemInHand().getType() == Material.DIAMOND_SWORD) {
                if (player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().getDisplayName().contains("Invite an player")) {
                    if (MatchHandler.get().getMatchInvite(clicked.getUniqueId(), player.getUniqueId()) != null) {

                    }
                }
            }
        }
    }
}
