/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.player;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.player.plugin.GamePlayer;
import eu.mcone.coresystem.api.bukkit.player.plugin.GamePlayerData;
import eu.mcone.coresystem.api.bukkit.player.profile.GameProfile;
import eu.mcone.coresystem.api.bukkit.player.profile.PlayerDataProfile;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BuildPlayer extends GamePlayerData<BuildPlayerProfile> {

    private Map<World, WorldRole> allowedWorlds;

    public BuildPlayer(CorePlayer player) {
        super(player);
        BuildSystem.getInstance().registerBuildPlayer(this);
    }

    @Override
    public BuildPlayerProfile reload() {
        BuildPlayerProfile profile = super.reload();
        this.allowedWorlds = profile.getAllowedWoldRoles();

        return profile;
    }

    @Override
    protected BuildPlayerProfile loadData() {
        return BuildSystem.getInstance().loadGameProfile(corePlayer.bukkit(), BuildPlayerProfile.class);
    }

    @Override
    public void saveData() {
        BuildSystem.getInstance().saveGameProfile(new BuildPlayerProfile(corePlayer.bukkit(), homes, allowedWorlds));
    }


    public void promotePermissions(World world, Player promoter) {
        if (promoter.hasPermission("buildsystem.promoter")) {
            if (getWorldPermission(world).equals(WorldRole.GUEST)) {
                allowedWorlds.remove(world);
                allowedWorlds.put(world, WorldRole.BUILDER);
                CoreSystem.getInstance().getMessenger().send(corePlayer.bukkit(), "§aDu hast nun Builder Rechte auf der Welt: §3" + world.getName());
                CoreSystem.getInstance().getMessenger().send(promoter, "§aDu hast ihn auf der §fWelt §azum §fBuilder§a gemacht!");
            } else if (getWorldPermission(world).equals(WorldRole.BUILDER)) {
                allowedWorlds.remove(world);
                allowedWorlds.put(world, WorldRole.OWNER);
                CoreSystem.getInstance().getMessenger().send(corePlayer.bukkit(), "§aDu hast nun Admin Rechte auf der Welt: §3" + world.getName());
                CoreSystem.getInstance().getMessenger().send(promoter, "§aDu hast ihn aufder  §fWelt §azum §fAdmin§a gemacht!");
            } else {
                CoreSystem.getInstance().getMessenger().send(promoter, "§4Der Spieler hat bereits Administrator!");
            }
        } else {
            if (getWorldPermission(world).equals(WorldRole.GUEST)) {
                allowedWorlds.remove(world);
                allowedWorlds.put(world, WorldRole.BUILDER);
                CoreSystem.getInstance().getMessenger().send(corePlayer.bukkit(), "§aDu hast nun Builder Rechte auf der Welt: §3" + world.getName());
                CoreSystem.getInstance().getMessenger().send(promoter, "§aDu hast ihn auf der §fWelt §azum §fBuilder§a gemacht!");
            } else if (getWorldPermission(world).equals(WorldRole.BUILDER)) {
                allowedWorlds.remove(world);
                allowedWorlds.put(world, WorldRole.OWNER);
                corePlayer.bukkit().getInventory().clear();
                CoreSystem.getInstance().getMessenger().send(corePlayer.bukkit(), "§aDu hast nun Admin Rechte auf der Welt: §3" + world.getName());
                CoreSystem.getInstance().getMessenger().send(promoter, "§aDu hast ihn auf der §fWelt §azum §fAdmin§a gemacht, das kannst du nicht mehr ändern!");
            } else {
                CoreSystem.getInstance().getMessenger().send(promoter, "§4Der Spieler hat bereits Admin auf der Welt!");
            }
        }

        saveData();
    }

    public void demotePermissions(World world, Player promoter) {
        if (promoter.hasPermission("buildsystem.demote")) {
            if (getWorldPermission(world).equals(WorldRole.OWNER)) {
                allowedWorlds.remove(world);
                allowedWorlds.put(world, WorldRole.BUILDER);
                CoreSystem.getInstance().getMessenger().send(corePlayer.bukkit(), "§aDu hast nun Builder Rechte auf der Welt: §3" + world.getName());
                CoreSystem.getInstance().getMessenger().send(promoter, "§aDu hast ihn auf der §fWelt §azum §fBuilder§a gemacht!");
            } else if (getWorldPermission(world).equals(WorldRole.BUILDER)) {
                allowedWorlds.remove(world);
                allowedWorlds.put(world, WorldRole.GUEST);
                corePlayer.bukkit().getInventory().clear();
                CoreSystem.getInstance().getMessenger().send(corePlayer.bukkit(), "§aDu hast nun keine Rechte mehr auf der Welt: §3" + world.getName());
                CoreSystem.getInstance().getMessenger().send(promoter, "§aDu hast ihn auf der §fWelt §azum §fSpieler§a gemacht!");
            } else {
                CoreSystem.getInstance().getMessenger().send(promoter, "§4Der Spieler hat bereits keinen Rang auf der Welt!");
            }
        } else {
            if (getWorldPermission(world).equals(WorldRole.OWNER)) {
                CoreSystem.getInstance().getMessenger().send(promoter, "§4Du hast zu wenig Rechte um ihn den Admin Rang zu entfernen!");
            } else if (getWorldPermission(world).equals(WorldRole.BUILDER)) {
                allowedWorlds.remove(world);
                allowedWorlds.put(world, WorldRole.GUEST);
                corePlayer.bukkit().getInventory().clear();
                CoreSystem.getInstance().getMessenger().send(corePlayer.bukkit(), "§aDu hast nun keine Rechte mehr auf der Welt: §3" + world.getName());
                CoreSystem.getInstance().getMessenger().send(promoter, "§aDu hast ihn auf der §fWelt §azum §fSpieler§a gemacht!");
            } else {
                CoreSystem.getInstance().getMessenger().send(promoter, "§4Der Spieler hat bereits keinen Rang auf der Welt!");
            }
        }

        saveData();
    }

    public void setWorldPermission(World world, WorldRole role) {
        CoreSystem.getInstance().getMessenger().send(corePlayer.bukkit(), "§aDu hast nun Bau Rechte auf der Welt: §3" + world.getName());
        corePlayer.bukkit().getInventory().clear();
        allowedWorlds.put(world, role);
        saveData();
    }

    public void removeWorldPermission(World world) {
        CoreSystem.getInstance().getMessenger().send(corePlayer.bukkit(), "§4Du hast nun keine Bau Rechte mehr auf der Welt: §c" + world.getName());
        corePlayer.bukkit().getInventory().clear();
        allowedWorlds.remove(world);
        saveData();
    }

    public boolean hasWorldPermissions() {
        for (World worlds : Bukkit.getWorlds()) {
            if (getWorldPermission(worlds).equals(WorldRole.BUILDER) || getWorldPermission(worlds).equals(WorldRole.OWNER)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public WorldRole getWorldPermission(World world) {
        return allowedWorlds.getOrDefault(world, WorldRole.GUEST);
    }
}
