package eu.mcone.buildsystem.listener;

import eu.mcone.buildsystem.util.Objective;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;

public class GeneralPlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        CoreSystem.getInstance().getCorePlayer(e.getPlayer()).getScoreboard().setNewObjective(new Objective());
    }

    @EventHandler
    public void onChangedWorld(PlayerChangedWorldEvent e) {
        CoreSystem.getInstance().getCorePlayer(e.getPlayer()).getScoreboard().getObjective(DisplaySlot.SIDEBAR).reload();
    }

}
