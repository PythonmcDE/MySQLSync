package net.craftersland.bridge.inventory.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class CommandEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommandSend(final org.bukkit.event.player.PlayerCommandPreprocessEvent e) {
        if(PlayerJoin.invsync != null) {
            if (PlayerJoin.invsync.contains(e.getPlayer())) {
                e.setCancelled(true);
                e.getPlayer().sendMessage("§a§lPythonMC §8» §7Du kannst das derzeit §cnicht §7machen.");
            }
        }
    }

}
