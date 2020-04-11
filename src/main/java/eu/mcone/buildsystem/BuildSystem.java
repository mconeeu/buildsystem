/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem;

import com.sk89q.wepif.PermissionsProvider;
import eu.mcone.buildsystem.command.PlotFinishCMD;
import eu.mcone.buildsystem.command.SkullCMD;
import eu.mcone.buildsystem.listener.GeneralPlayerListener;
import eu.mcone.buildsystem.listener.SecretSignsListener;
import eu.mcone.buildsystem.listener.WeatherChangeListener;
import eu.mcone.buildsystem.player.BuildPlayer;
import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.player.profile.interfaces.HomeManager;
import eu.mcone.coresystem.api.bukkit.player.profile.interfaces.HomeManagerGetter;
import eu.mcone.coresystem.api.bukkit.spawnable.ListMode;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.coresystem.api.core.player.Group;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class BuildSystem extends CorePlugin implements PermissionsProvider, HomeManagerGetter {

    @Getter
    private static BuildSystem instance;

    @Getter
    private eu.mcone.coresystem.api.bukkit.world.BuildSystem buildSystem;
    @Getter
    private CoreWorld plotWorld;
    private List<BuildPlayer> players;

    public BuildSystem() {
        super(Gamemode.BUILD, "build.prefix");
    }

    public void onEnable() {
        instance = this;
        players = new ArrayList<>();
        plotWorld = CoreSystem.getInstance().getWorldManager().getWorld("plots");
        CoreSystem.getInstance().getWorldManager().enableUploadCommand(true);

        sendConsoleMessage("§aProviding WEPIF Permissions!");

        sendConsoleMessage("§aListener und Events werden geöaden!");
        registerEvents(
                new GeneralPlayerListener(),
                new SecretSignsListener(),
                new WeatherChangeListener()
        );
        registerCommands(
                new SkullCMD(),
                new PlotFinishCMD()
        );
        CoreSystem.getInstance().enableSpawnCommand(this, plotWorld, 0);
        CoreSystem.getInstance().enableHomeSystem(this, this, 0);
        CoreSystem.getInstance().enableTpaSystem(this, 0);

        sendConsoleMessage("§aBuildSystem wird geladen!");
        buildSystem = CoreSystem.getInstance().initialiseBuildSystem(
                eu.mcone.coresystem.api.bukkit.world.BuildSystem.BuildEvent.BLOCK_BREAK,
                eu.mcone.coresystem.api.bukkit.world.BuildSystem.BuildEvent.BLOCK_PLACE,
                eu.mcone.coresystem.api.bukkit.world.BuildSystem.BuildEvent.INTERACT
        );
        buildSystem.setWorlds(ListMode.BLACKLIST, plotWorld.bukkit());
        buildSystem.setUseBuildPermissionNodes(true);

        sendConsoleMessage("§aVersion §f" + this.getDescription().getVersion() + "§a wurde aktiviert...");
    }

    public void onDisable() {
        sendConsoleMessage("§cPlugin wurde deaktiviert!");
    }

    @Override
    public boolean hasPermission(String name, String permission) {
        return CoreSystem.getInstance().getCorePlayer(name).hasPermission(permission);
    }

    @Override
    public boolean hasPermission(String worldName, String name, String permission) {
        return CoreSystem.getInstance().getCorePlayer(name).hasPermission(permission);
    }

    @Override
    public boolean inGroup(String name, String group) {
        for (Group g : CoreSystem.getInstance().getCorePlayer(name).getGroups()) {
            if (g.getName().equalsIgnoreCase(group)) return true;
        }
        return false;
    }

    @Override
    public String[] getGroups(String name) {
        Set<String> groups = CoreSystem.getInstance().getCorePlayer(name).getPermissions();
        return groups.toArray(new String[0]);
    }

    @Override
    public boolean hasPermission(OfflinePlayer offlinePlayer, String permission) {
        Player p = offlinePlayer.getPlayer();
        return p != null && p.hasPermission(permission);
    }

    @Override
    public boolean hasPermission(String worldName, OfflinePlayer offlinePlayer, String permission) {
        Player p = offlinePlayer.getPlayer();
        return p != null && p.hasPermission(permission);
    }

    @Override
    public boolean inGroup(OfflinePlayer offlinePlayer, String group) {
        CorePlayer p = CoreSystem.getInstance().getCorePlayer(offlinePlayer.getPlayer());
        if (p != null) {
            for (Group g : p.getGroups()) {
                if (g.getName().equalsIgnoreCase(group)) return true;
            }
        }
        return false;
    }

    @Override
    public String[] getGroups(OfflinePlayer offlinePlayer) {
        CorePlayer p = CoreSystem.getInstance().getCorePlayer(offlinePlayer.getPlayer());
        if (p != null) {
            Set<String> groups = p.getPermissions();
            return groups.toArray(new String[0]);
        }

        return null;
    }

    public BuildPlayer getBuildPlayer(UUID uuid) {
        for (BuildPlayer bp : players) {
            if (bp.getCorePlayer().getUuid().equals(uuid)) {
                return bp;
            }
        }
        return null;
    }

    public BuildPlayer getBuildPlayer(String name) {
        for (BuildPlayer bp : players) {
            if (bp.getCorePlayer().getName().equals(name)) {
                return bp;
            }
        }
        return null;
    }

    public Collection<BuildPlayer> getBuildPlayers() {
        return players;
    }

    public void registerBuildPlayer(BuildPlayer sp) {
        players.add(sp);
    }

    public void unregisterBuildPlayer(BuildPlayer sp) {
        players.remove(sp);
    }

    @Override
    public HomeManager getHomeManager(Player player) {
        return getBuildPlayer(player.getUniqueId());
    }

}
