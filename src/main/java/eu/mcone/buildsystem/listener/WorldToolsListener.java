/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.listener;

import eu.mcone.buildsystem.BuildSystem;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.*;

public class WorldToolsListener implements Listener {

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

    private static void checkBuildPermission(Cancellable e, Player p) {
        if (!BuildSystem.getInstance().getBuildPlayer(p).canBuildOn(p.getWorld())) {
            e.setCancelled(true);
        }
    }

}
