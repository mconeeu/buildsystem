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
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

public class GeneralPlayerListener implements Listener {

    @EventHandler
    public void onCorePlayerLoaded(CorePlayerLoadedEvent e) {
        CorePlayer p = e.getPlayer();
        new BuildPlayer(p);

        BuildPlayer buildPlayer = BuildSystem.getInstance().getBuildPlayer(p.bukkit());


        if (buildPlayer.getAccepted().containsKey(true) && buildPlayer.getAccepted().containsValue(false)) {
            CoreSystem.getInstance().getMessenger().send(p.bukkit(), "§2Dein §aPlot§2 wurde erfolgreich §aangenommen§2!");
            CoreSystem.getInstance().getMessenger().send(p.bukkit(), "§2Mit /apply erhälts du weite Informationen");
        } else if (buildPlayer.getDeny()) {
            CoreSystem.getInstance().getMessenger().send(p.bukkit(), "§4Deine Builder Bewerbung wurde ablehnt!");
        }

        p.setScoreboard(new BuildTablist());
        p.getScoreboard().setNewObjective(new SidebarObjective());
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        e.blockList().clear();
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

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        BuildPlayer bp = BuildSystem.getInstance().getBuildPlayer(e.getPlayer().getUniqueId());
        if (bp.canBuildOn(bp.bukkit().getWorld())) {
            e.setCancelled(false);
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e) {
        BuildPlayer bp = BuildSystem.getInstance().getBuildPlayer(e.getPlayer().getUniqueId());
        if (bp.canBuildOn(bp.bukkit().getWorld())) {
            e.setCancelled(false);
            return;
        }
        e.setCancelled(true);
    }

}
