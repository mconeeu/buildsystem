/*
 * Copyright (c) 2017 - 2018 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.util;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjective;

public class Objective extends CoreSidebarObjective {

    public Objective() {
        super("Plot-BuildServer");
    }

    @Override
    public void onRegister(CorePlayer player) {
        setDisplayName("§7§l⚔ §e§l§nBuild Server");

        setScore(7, "");
        setScore(6, "§7§oBenutze /plot help");
        setScore(5, "§7§ofür Hilfe");
        setScore(4, "");
        setScore(3, "§8» §7Welt:");
        setScore(2, "§f§o"+player.getWorld().getName());
        setScore(1, "");
        setScore(0, "§f§lMCONE.EU");
    }

    @Override
    public void onReload(CorePlayer player) {
        setScore(2, "§f§o"+player.getWorld().getName());
    }

}
