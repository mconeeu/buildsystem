/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.player;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjective;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjectiveEntry;

public class SidebarObjective extends CoreSidebarObjective {

    public SidebarObjective() {
        super("BuildServer");
    }

    @Override
    protected void onRegister(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {
        entry.setTitle("§7§l⚔ "+ BuildSystem.getInstance().getPluginColor()+"§l§n"+BuildSystem.getInstance().getPluginName()+"§7§l ⚔");

        entry.setScore(7, "§7§oBenutze /plot help");
        entry.setScore(6, "§7§ofür Hilfe");
        entry.setScore(5, "");
        entry.setScore(4, "§8» §7Welt:");
        onReload(player, entry);
        entry.setScore(1, "");
        entry.setScore(0, "§f§lMCONE.EU");
    }

    @Override
    protected void onReload(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {
        entry.setScore(3, " §f§o" + player.getWorld().getName());
        entry.setScore(2, " §f§o" + player.getWorld().getVersionString());
    }
}
