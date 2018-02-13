/*
 * Copyright (c) 2017 - 2018 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.listener;

import eu.mcone.coresystem.bukkit.CoreSystem;
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
