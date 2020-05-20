/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.worldtools;

import com.boydti.fawe.object.FawePlayer;
import com.boydti.fawe.regions.FaweMask;
import com.boydti.fawe.regions.FaweMaskManager;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.regions.CuboidRegion;
import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.player.BuildPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MaskManager extends FaweMaskManager<Player> {

    public MaskManager() {
        super("BuildTools");
    }

    @Override
    public FaweMask getMask(FawePlayer<Player> player, MaskType type) {
        Player p = player.parent;
        BuildPlayer bp = BuildSystem.getInstance().getBuildPlayer(p);

        if (!p.getWorld().equals(BuildSystem.getInstance().getPlotWorld().bukkit()) && bp.canBuildOn(p.getWorld())) {
            Location pos1 = new Location(p.getWorld(), Integer.MIN_VALUE, 0, Integer.MIN_VALUE);
            Location pos2 = new Location(p.getWorld(), Integer.MAX_VALUE, 255, Integer.MAX_VALUE);

            return new FaweMask(new CuboidRegion(
                    new BlockVector(pos1.getBlockX(), pos1.getBlockY(), pos1.getBlockZ()),
                    new BlockVector(pos2.getBlockX(), pos2.getBlockY(), pos2.getBlockZ())
            ), "BuildTools-"+p.getWorld().getName());
        } else {
            return null;
        }
    }

}
