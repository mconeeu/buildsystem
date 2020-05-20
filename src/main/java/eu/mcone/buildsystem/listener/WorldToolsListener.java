/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.listener;

import eu.mcone.buildsystem.BuildSystem;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class WorldToolsListener implements Listener {

    @EventHandler
    public void BlockBreak(BlockBreakEvent e) {
        checkBuildPermission(e, e.getPlayer());
    }

    @EventHandler
    public void BlockPlace(BlockPlaceEvent e) {
        checkBuildPermission(e, e.getPlayer());
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        checkBuildPermission(e, e.getPlayer());
    }

    @EventHandler
    public void onPick(PlayerPickupItemEvent e) {
        checkBuildPermission(e, e.getPlayer());
    }

    private static void checkBuildPermission(Cancellable e, Player p) {
        if (!BuildSystem.getInstance().getBuildPlayer(p).canBuildOn(p.getWorld())) {
            e.setCancelled(true);
        }
    }

}
