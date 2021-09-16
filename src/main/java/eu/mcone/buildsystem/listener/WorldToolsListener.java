/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.listener;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.worldtools.WorldConfig;
import eu.mcone.buildsystem.worldtools.WorldToolsManager;
import eu.mcone.coresystem.api.bukkit.event.world.WorldCreateEvent;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.*;

@RequiredArgsConstructor
public class WorldToolsListener implements Listener {

    private final WorldToolsManager manager;

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!BuildSystem.getInstance().getBuildPlayer(e.getPlayer()).canSeeOn(e.getPlayer().getWorld())) {
            e.getPlayer().teleport(BuildSystem.getInstance().getPlotWorld().getLocation("spawn"));
            BuildSystem.getInstance().getMessenger().send(e.getPlayer(), "§4Du benötigst den §cGast Rang§4 auf der Welt!");
        }
    }

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

    @EventHandler
    public void on(PlayerArmorStandManipulateEvent e) {
        checkBuildPermission(e, e.getPlayer());
    }

    @EventHandler
    public void on(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        if (!p.getWorld().getName().equals(BuildSystem.getInstance().getPlotWorld().getName())) {
            checkBuildPermission(e, e.getPlayer());
        }
    }

    @EventHandler
    public void on(EntityInteractEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            checkBuildPermission(e, p);
        }
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            checkBuildPermission(e, ((Player) e.getDamager()).getPlayer());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (!p.getWorld().getName().equals(BuildSystem.getInstance().getPlotWorld().getName())) {
            checkBuildPermission(e, e.getPlayer());
        }
    }

    @EventHandler
    public void onWorldLoad(WorldCreateEvent e) {
        CoreWorld w = e.getWorld();
        WorldConfig config = manager.getWorldConfig(w);

        if (config == null) {
            manager.registerNewWorld(w);
        }
    }

    private static void checkBuildPermission(Cancellable e, Player p) {
        if (!BuildSystem.getInstance().getBuildPlayer(p).canBuildOn(p.getWorld())) {
            e.setCancelled(true);
        }
    }

}
