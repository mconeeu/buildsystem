package eu.mcone.buildsystem.listener;

import eu.mcone.bukkitcoresystem.CoreSystem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.scoreboard.DisplaySlot;

public class PlayerChangedWorld implements Listener {

    @EventHandler
    public void on(PlayerChangedWorldEvent e) {
        CoreSystem.getCorePlayer(e.getPlayer()).getScoreboard().getObjective(DisplaySlot.SIDEBAR).reload();
    }

}
