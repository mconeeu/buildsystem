package eu.mcone.buildsystem.listener;

import eu.mcone.buildsystem.Main;
import eu.mcone.buildsystem.util.Objective;
import eu.mcone.bukkitcoresystem.CoreSystem;
import eu.mcone.bukkitcoresystem.player.CorePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void on(PlayerJoinEvent e) {
        CorePlayer p = CoreSystem.getCorePlayer(e.getPlayer());
        new Objective(p);
        Main.holo.setHolograms(e.getPlayer());
    }

}
