/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.player;

import com.intellectualcrafters.plot.object.Plot;
import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.worldtools.WorldRole;
import eu.mcone.buildsystem.worldtools.WorldToolsManager;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.player.plugin.GamePlayerData;
import eu.mcone.coresystem.api.bukkit.player.profile.PlayerDataProfile;
import lombok.Getter;
import org.bukkit.World;

public class BuildPlayer extends GamePlayerData<BuildPlayerDataProfile> {

    static {
        PlayerDataProfile.preventTeleport(true);
    }

    @Getter
    private Plot finishedPlot;

    public BuildPlayer(CorePlayer player) {
        super(player);
        BuildSystem.getInstance().registerBuildPlayer(this);
    }

    @Override
    public BuildPlayerDataProfile reload() {
        BuildPlayerDataProfile profile = super.reload();
        finishedPlot = profile.getFinishedPlot();

        return profile;
    }

    @Override
    protected BuildPlayerDataProfile loadData() {
        return BuildSystem.getInstance().loadGameProfile(corePlayer.bukkit(), BuildPlayerDataProfile.class);
    }

    @Override
    public void saveData() {
        BuildSystem.getInstance().saveGameProfile(new BuildPlayerDataProfile(
                corePlayer.bukkit(),
                BuildSystem.getInstance().getHomeManager(corePlayer.bukkit()).getHomes(),
                finishedPlot
        ));
    }

    public void setFinishedPlot(Plot plot) {
        this.finishedPlot = plot;
        saveData();
    }

    public boolean hasFinished() {
        return finishedPlot != null;
    }

    public WorldRole getWorldRole(World world) {
        return corePlayer.bukkit().hasPermission(WorldToolsManager.BUILD_PERMISSION)
                ? WorldRole.OWNER
                : BuildSystem.getInstance().getWorldManager().getWorldRole(corePlayer.getUuid(), world);
    }

    public boolean hasAccessTo(World world) {
        return getWorldRole(world) != null || corePlayer.hasPermission(WorldToolsManager.BUILD_PERMISSION);
    }

    public boolean canBuildOn(World world) {
        if (world.getName().equals("plots") || corePlayer.hasPermission(WorldToolsManager.BUILD_PERMISSION)) {
            return true;
        } else {
            WorldRole role = getWorldRole(world);
            return role != null && (role.equals(WorldRole.BUILDER) || role.equals(WorldRole.OWNER));
        }
    }

    public boolean canSeeOn(World world) {
        if (world.getName().equals("plots") || corePlayer.hasPermission(WorldToolsManager.BUILD_PERMISSION) || corePlayer.hasPermission(WorldToolsManager.TEAM_PERMISSIONS)) {
            return true;
        } else {
            WorldRole role = getWorldRole(world);
            return role != null && (role.equals(WorldRole.BUILDER) || role.equals(WorldRole.OWNER) || role.equals(WorldRole.GUEST));
        }
    }

    public String getWorldRoleLabel(World world) {
        WorldRole role = getWorldRole(world);

        return corePlayer.hasPermission(WorldToolsManager.BUILD_PERMISSION)
                ? "§cTeam-Builder"
                : (role != null
                ? role.getLabel()
                : "§7§oBesucher");
    }

}
