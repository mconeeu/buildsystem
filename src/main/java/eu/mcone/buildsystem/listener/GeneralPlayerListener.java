/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.listener;

import com.sk89q.worldedit.bukkit.EditSessionBlockChangeDelegate;
import com.sk89q.worldedit.bukkit.WorldEditAPI;
import com.sk89q.worldedit.bukkit.WorldEditListener;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.player.BuildPlayer;
import eu.mcone.buildsystem.player.WorldRole;
import eu.mcone.buildsystem.util.SidebarObjective;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.event.CorePlayerLoadedEvent;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;

public class GeneralPlayerListener implements Listener {

    @EventHandler
    public void onCorePlayerLoaded(CorePlayerLoadedEvent e) {
        CorePlayer p = e.getPlayer();

        p.getScoreboard().setNewObjective(new SidebarObjective());
        new BuildPlayer(p);
    }

    @EventHandler
    public void BlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        BuildPlayer bp = BuildSystem.getInstance().getBuildPlayer(p);
        if (!p.getWorld().getName().equalsIgnoreCase("plots")) {
            if (!p.hasPermission("coresystem.worldtools.bypass")) {
                if (bp.getWorldPermission(p.getWorld()).equals(WorldRole.BUILDER) || bp.getWorldPermission(p.getWorld()).equals(WorldRole.OWNER)) {
                    e.setCancelled(false);
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void ComamndPreProcess(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        String cmd = e.getMessage();
        BuildPlayer bp = BuildSystem.getInstance().getBuildPlayer(p);
        if (!p.getWorld().getName().equalsIgnoreCase("plots")) {
            if (!p.hasPermission("coresystem.worldtools.bypass")) {
                if (bp.getWorldPermission(p.getWorld()).equals(WorldRole.BUILDER) || bp.getWorldPermission(p.getWorld()).equals(WorldRole.OWNER)) {
                    e.setCancelled(false);
                } else if (cmd.startsWith("//")) {
                    e.setCancelled(true);
                    CoreSystem.getInstance().getMessenger().send(p, "§4Du darfst auf dieser Welt keinen WorldEdit Command benutzen!");
                }
            }
        }
    }

    @EventHandler
    public void BlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        BuildPlayer bp = BuildSystem.getInstance().getBuildPlayer(p);
        if (!p.getWorld().getName().equalsIgnoreCase("plots")) {
            if (!p.hasPermission("coresystem.worldtools.bypass")) {
                if (bp.getWorldPermission(p.getWorld()).equals(WorldRole.BUILDER) || bp.getWorldPermission(p.getWorld()).equals(WorldRole.OWNER)) {
                    e.setCancelled(false);
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }


    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        BuildPlayer bp = BuildSystem.getInstance().getBuildPlayer(p);
        if (e.getClickedInventory() != null) {
            if (!p.getWorld().getName().equalsIgnoreCase("plots")) {
                if (!p.hasPermission("coresystem.worldtools.bypass")) {
                    if (!e.getClickedInventory().getName().startsWith("§fWorldTools")) {
                        if (bp.getWorldPermission(p.getWorld()).equals(WorldRole.BUILDER) || bp.getWorldPermission(p.getWorld()).equals(WorldRole.OWNER)) {
                            e.setCancelled(false);
                        } else {
                            e.setCancelled(true);
                        }
                    } else {
                        e.setCancelled(true);
                    }
                } else {
                    if (!e.getClickedInventory().getName().startsWith("§fWorldTools")) {
                        e.setCancelled(false);
                    } else {
                        e.setCancelled(true);
                    }
                }
            } else {
                if (!e.getClickedInventory().getName().startsWith("§fWorldTools")) {
                    e.setCancelled(false);
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onChangedWorld(PlayerChangedWorldEvent e) {
        CoreSystem.getInstance().getCorePlayer(e.getPlayer()).getScoreboard().getObjective(DisplaySlot.SIDEBAR).reload();
        Player p = e.getPlayer();
        if (!p.hasPermission("coresystem.worldtools.bypass")) {
            p.getInventory().clear();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        BuildPlayer bp = BuildSystem.getInstance().getBuildPlayer(e.getPlayer().getUniqueId());
        BuildSystem.getInstance().unregisterBuildPlayer(bp);
    }

}
