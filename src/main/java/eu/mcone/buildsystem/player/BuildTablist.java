/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.player;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreScoreboard;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreScoreboardEntry;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.coresystem.api.core.player.Group;

import java.util.UUID;

public class BuildTablist extends CoreScoreboard {

    @Override
    public void modifyTeam(CorePlayer owner, CorePlayer player, CoreScoreboardEntry team) {
        Group g = player.isNicked() ? player.getNick().getGroup() : player.getMainGroup();
        CoreWorld w = owner.getWorld();

        String prefix = g.getPrefix(), suffix = player.isVanished() ? " §3§lⓋ" : "";
        int priority = !player.isNicked() && player.getUuid().equals(UUID.fromString("44b8a5d6-c2c3-4576-997f-71b94f5eb7e0"))
                ? 0
                : g.getScore();

        if (!w.equals(BuildSystem.getInstance().getPlotWorld()) || owner.hasPermission("build.builder")) {
            suffix = suffix + " §3["+(player.getWorld().getName().substring(0, Math.min(16 - (suffix.length() + 5), player.getWorld().getName().length())))+"]";
        }

        team.prefix(prefix).suffix(suffix).priority(priority);
    }

}
