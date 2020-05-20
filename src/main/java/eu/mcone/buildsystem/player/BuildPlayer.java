/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.player;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.worldtools.WorldRole;
import eu.mcone.buildsystem.worldtools.WorldToolsManager;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.player.plugin.GamePlayerData;
import eu.mcone.coresystem.api.bukkit.player.profile.PlayerDataProfile;
import org.bukkit.World;

public class BuildPlayer extends GamePlayerData<PlayerDataProfile> {

    static {
        PlayerDataProfile.preventTeleport(true);
    }

    public BuildPlayer(CorePlayer player) {
        super(player);
        BuildSystem.getInstance().registerBuildPlayer(this);
    }

    @Override
    protected PlayerDataProfile loadData() {
        return BuildSystem.getInstance().loadGameProfile(corePlayer.bukkit(), PlayerDataProfile.class);
    }

    @Override
    public void saveData() {
        BuildSystem.getInstance().saveGameProfile(new PlayerDataProfile(corePlayer.bukkit(), homes));
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
