/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.listener;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.player.BuildPlayer;
import eu.mcone.buildsystem.player.BuildTablist;
import eu.mcone.buildsystem.player.SidebarObjective;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.event.CorePlayerLoadedEvent;
import eu.mcone.coresystem.api.bukkit.event.PlayerVanishEvent;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GeneralPlayerListener implements Listener {

    @EventHandler
    public void onCorePlayerLoaded(CorePlayerLoadedEvent e) {
        CorePlayer p = e.getPlayer();
        new BuildPlayer(p);

        p.setScoreboard(new BuildTablist());
        p.getScoreboard().setNewObjective(new SidebarObjective());
    }

    @EventHandler
    public void onChangedWorld(PlayerChangedWorldEvent e) {
        CoreSystem.getInstance().getCorePlayer(e.getPlayer()).getScoreboard().reload();
    }

    @EventHandler
    public void onVanish(PlayerVanishEvent e) {
        for (CorePlayer cp : CoreSystem.getInstance().getOnlineCorePlayers()) {
            cp.getScoreboard().reload();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        BuildPlayer bp = BuildSystem.getInstance().getBuildPlayer(e.getPlayer().getUniqueId());
        bp.saveData();
        BuildSystem.getInstance().unregisterBuildPlayer(bp);
    }

}
