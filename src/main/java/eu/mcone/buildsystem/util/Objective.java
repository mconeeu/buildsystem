/*
 * Copyright (c) 2017 - 2018 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.util;

import eu.mcone.coresystem.api.bukkit.scoreboard.CoreObjective;
import org.bukkit.scoreboard.DisplaySlot;

public class Objective extends CoreObjective {

    private String world;

    public Objective() {
        super(DisplaySlot.SIDEBAR, "Plot", "BuildServer");
    }

    @Override
    public void register() {
        world = "§f§o"+getPlayer().bukkit().getWorld().getName();

        org.bukkit.scoreboard.Objective o = getScoreboard().getObjective(DisplaySlot.SIDEBAR);
        o.setDisplayName("§7§l⚔ §e§l§nBuild Server");

        o.getScore("§0").setScore(7);
        o.getScore("§7§oBenutze /plot help").setScore(6);
        o.getScore("§7§ofür Hilfe").setScore(5);
        o.getScore("§1").setScore(4);
        o.getScore("§8» §7Welt:").setScore(3);
        o.getScore(world).setScore(2);
        o.getScore("§2").setScore(1);
        o.getScore("§f§lMCONE.EU").setScore(0);
    }

    @Override
    public void reload() {
        org.bukkit.scoreboard.Objective o = getScoreboard().getObjective(DisplaySlot.SIDEBAR);
        getScoreboard().resetScores(world);

        world = "§f§o"+getPlayer().bukkit().getWorld().getName();
        o.getScore(world).setScore(2);
    }
}
