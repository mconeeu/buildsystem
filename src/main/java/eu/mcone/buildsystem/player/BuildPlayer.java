/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.player;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.enums.BuildTheme;
import eu.mcone.buildsystem.worldtools.WorldRole;
import eu.mcone.buildsystem.worldtools.WorldToolsManager;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.player.plugin.GamePlayerData;
import eu.mcone.coresystem.api.bukkit.player.profile.PlayerDataProfile;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.World;

import java.util.HashMap;

public class BuildPlayer extends GamePlayerData<BuildPlayerDataProfile> {

    static {
        PlayerDataProfile.preventTeleport(true);
    }

    @Getter
    /* first boolean plot accepted, second boolean world accepted */
    private HashMap<Boolean, Boolean> accepted;
    @Getter
    /* world theme */
    private Boolean deny;
    @Getter
    /* world theme */
    private String thema;
    @Getter
    @Setter
    /* player builder apply world */
    private World buildWorld;

    public BuildPlayer(CorePlayer player) {
        super(player);
        BuildSystem.getInstance().registerBuildPlayer(this);
    }

    @Override
    public BuildPlayerDataProfile reload() {
        BuildPlayerDataProfile profile = super.reload();
        accepted = profile.getAccepted();

        return profile;
    }

    @Override
    protected BuildPlayerDataProfile loadData() {
        return BuildSystem.getInstance().loadGameProfile(corePlayer.bukkit(), BuildPlayerDataProfile.class);
    }

    @Override
    public void saveData() {
        BuildSystem.getInstance().saveGameProfile(new BuildPlayerDataProfile(corePlayer.bukkit(), BuildSystem.getInstance().getHomeManager(corePlayer.bukkit()).getHomes(), accepted, thema, buildWorld, deny));
    }

    public void setPlotAccepted(HashMap<Boolean, Boolean> accept) {
        accepted = accept;
        saveData();
    }

    public void setThema(BuildTheme buildTheme) {
        thema = buildTheme.getName();
        saveData();
    }

    public void setDeny(boolean bo) {
        deny = bo;
        saveData();
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

    public String getWorldRoleLabel(World world) {
        WorldRole role = getWorldRole(world);

        return corePlayer.hasPermission(WorldToolsManager.BUILD_PERMISSION)
                ? "§cTeam-Builder"
                : (role != null
                ? role.getLabel()
                : "§7§oBesucher");
    }

}
