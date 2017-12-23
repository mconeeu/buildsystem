package eu.mcone.buildsystem.util;

import eu.mcone.bukkitcoresystem.player.CorePlayer;
import eu.mcone.bukkitcoresystem.scoreboard.ObjectiveHandler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

public class Objective implements ObjectiveHandler {

    private String world;

    public Objective(CorePlayer p) {
        p.getScoreboard().setNewObjective(DisplaySlot.SIDEBAR, this, "Plot", "BuildServer");
    }

    @Override
    public void register(CorePlayer p, Scoreboard sb) {
        world = "§f§o"+p.bukkit().getWorld().getName();

        org.bukkit.scoreboard.Objective o = sb.getObjective(DisplaySlot.SIDEBAR);
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
    public void reload(CorePlayer p, Scoreboard sb) {
        org.bukkit.scoreboard.Objective o = sb.getObjective(DisplaySlot.SIDEBAR);
        sb.resetScores(world);

        world = "§f§o"+p.bukkit().getWorld().getName();
        o.getScore(world).setScore(2);
    }

}
