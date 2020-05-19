/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.player;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.player.plugin.GamePlayerData;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Map;

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
            if (!corePlayer.hasPermission("buildsystem.builder")) {
                if (getWorldPermission(world).equals(WorldRole.GUEST)) {
                    allowedWorlds.remove(world);
                    allowedWorlds.put(world, WorldRole.BUILDER);
                    if (promoter != corePlayer.bukkit()) {
                        CoreSystem.getInstance().getMessenger().send(corePlayer.bukkit(), "§aDu hast nun den Builder Rang auf der Welt: §3" + world.getName());
                        CoreSystem.getInstance().getMessenger().send(promoter, "§aDu hast den Spieler auf der §fWelt §aden §fBuilder§a Rang gesetzt!");
                    } else {
                        CoreSystem.getInstance().getMessenger().send(corePlayer.bukkit(), "§aDu hast nun den Builder Rang auf der Welt: §3" + world.getName());
                    }
                } else if (getWorldPermission(world).equals(WorldRole.BUILDER)) {
                    allowedWorlds.remove(world);
                    allowedWorlds.put(world, WorldRole.OWNER);
                    if (promoter != corePlayer.bukkit()) {
                        CoreSystem.getInstance().getMessenger().send(corePlayer.bukkit(), "§aDu hast nun den Admin Rang auf der Welt: §3" + world.getName());
                        CoreSystem.getInstance().getMessenger().send(promoter, "§aDu hast den Spieler auf der §fWelt §aden §fAdmin§a Rang gesetzt!");
                    } else {
                        CoreSystem.getInstance().getMessenger().send(corePlayer.bukkit(), "§aDu hast nun den Admin Rang auf der Welt: §3" + world.getName());
                    }
                } else {
                    CoreSystem.getInstance().getMessenger().send(promoter, "§4Der Spieler hat bereits Administrator!");
                }
            } else {
                if (getWorldPermission(world).equals(WorldRole.GUEST)) {
                    allowedWorlds.remove(world);
                    allowedWorlds.put(world, WorldRole.BUILDER);
                    if (promoter != corePlayer.bukkit()) {
                        CoreSystem.getInstance().getMessenger().send(corePlayer.bukkit(), "§aDu hast nun den Builder Rang auf der Welt: §3" + world.getName());
                        CoreSystem.getInstance().getMessenger().send(promoter, "§aDu hast den Spieler auf der §fWelt §aden §fBuilder§a Rang gesetzt!");
                    } else {
                        CoreSystem.getInstance().getMessenger().send(corePlayer.bukkit(), "§aDu hast nun den Builder Rang auf der Welt: §3" + world.getName());
                    }
                } else if (getWorldPermission(world).equals(WorldRole.BUILDER)) {
                    allowedWorlds.remove(world);
                    allowedWorlds.put(world, WorldRole.OWNER);
                    corePlayer.bukkit().getInventory().clear();
                    if (promoter != corePlayer.bukkit()) {
                        CoreSystem.getInstance().getMessenger().send(corePlayer.bukkit(), "§aDu hast nun den Admin Rang auf der Welt: §3" + world.getName());
                        CoreSystem.getInstance().getMessenger().send(promoter, "§aDu hast den Spieler auf der §fWelt §aden §fAdmin§a Rang gesetzt, das kannst du nicht mehr ändern!");
                    } else {
                        CoreSystem.getInstance().getMessenger().send(corePlayer.bukkit(), "§aDu hast nun den Admin Rang auf der Welt: §3" + world.getName());
                    }
                } else {
                    CoreSystem.getInstance().getMessenger().send(promoter, "§4Der Spieler hat bereits §cAdmin §4auf der §cWelt§4!");
                }
            }

            saveData();
        } else {
            CoreSystem.getInstance().getMessenger().send(promoter, "§4Der Team-Builder hat überall Rechte!");
        }
    }

    public void demotePermissions(World world, Player promoter) {
        if (promoter.hasPermission("buildsystem.demote")) {
            if (!corePlayer.hasPermission("buildsystem.builder")) {
                if (getWorldPermission(world).equals(WorldRole.OWNER)) {
                    allowedWorlds.remove(world);
                    allowedWorlds.put(world, WorldRole.BUILDER);
                    if (promoter != corePlayer.bukkit()) {
                        CoreSystem.getInstance().getMessenger().send(corePlayer.bukkit(), "§aDu hast nun Builder Rechte auf der Welt: §3" + world.getName());
                        CoreSystem.getInstance().getMessenger().send(promoter, "§aDu hast den Spieler auf der §fWelt §aden §fBuilder§a Rang gesetzt!");
                    } else {
                        CoreSystem.getInstance().getMessenger().send(corePlayer.bukkit(), "§aDu hast nun Builder Rechte auf der Welt: §3" + world.getName());
                    }
                } else if (getWorldPermission(world).equals(WorldRole.BUILDER)) {
                    allowedWorlds.remove(world);
                    allowedWorlds.put(world, WorldRole.GUEST);
                    corePlayer.bukkit().getInventory().clear();
                    if (promoter != corePlayer.bukkit()) {
                        CoreSystem.getInstance().getMessenger().send(corePlayer.bukkit(), "§aDu hast nun keine Rechte mehr auf der Welt: §3" + world.getName());
                        CoreSystem.getInstance().getMessenger().send(promoter, "§aDu hast den Spieler auf der §fWelt §aden §fSpieler§a Rang gesetzt!");
                    } else {
                        CoreSystem.getInstance().getMessenger().send(corePlayer.bukkit(), "§aDu hast nun den Spieler Rang auf der Welt: §3" + world.getName());
                    }
                } else {
                    CoreSystem.getInstance().getMessenger().send(promoter, "§4Der Spieler hat bereits keinen Rang auf der Welt!");
                }
            } else {
                if (getWorldPermission(world).equals(WorldRole.OWNER)) {
                    CoreSystem.getInstance().getMessenger().send(promoter, "§4Du hast zu wenig Rechte um den Spieler auf der Welt den Admin Rang zu entfernen!");
                } else if (getWorldPermission(world).equals(WorldRole.BUILDER)) {
                    allowedWorlds.remove(world);
                    allowedWorlds.put(world, WorldRole.GUEST);
                    corePlayer.bukkit().getInventory().clear();
                    if (promoter != corePlayer.bukkit()) {
                        CoreSystem.getInstance().getMessenger().send(corePlayer.bukkit(), "§aDu hast nun keine Rechte mehr auf der Welt: §3" + world.getName());
                        CoreSystem.getInstance().getMessenger().send(promoter, "§aDu hast den Spieler auf der §fWelt §aden §fSpieler§a Rang gesetzt!");
                    } else {
                        CoreSystem.getInstance().getMessenger().send(corePlayer.bukkit(), "§aDu hast nun den Spieler Rang auf der Welt: §3" + world.getName());
                    }
                } else {
                    CoreSystem.getInstance().getMessenger().send(promoter, "§4Der Spieler hat bereits §ckeinen Rang §4auf der §cWelt§4!");
                }
            }

            saveData();
        } else {
            CoreSystem.getInstance().getMessenger().send(promoter, "§4Der Team-Builder hat überall Rechte!");
        }
    }

    public String hasWorldPermissions(World world) {
        if (!corePlayer.hasPermission("buildsystem.builder")) {
            if (getWorldPermission(world).equals(WorldRole.OWNER)) {
                return "§cAdmin: " + corePlayer.getName();
            } else if (getWorldPermission(world).equals(WorldRole.BUILDER)) {
                return "§eBuilder: " + corePlayer.getName();
            } else {
                return "§7Spieler: " + corePlayer.getName();
            }
        } else {
            return "§cTeam-Builder" + corePlayer.getName();
        }
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


    public WorldRole getWorldPermission(World world) {
        return allowedWorlds.getOrDefault(world, WorldRole.GUEST);
    }
}
