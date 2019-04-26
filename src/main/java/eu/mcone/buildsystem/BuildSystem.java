/*
 * Copyright (c) 2017 - 2018 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem;

import com.sk89q.wepif.PermissionsProvider;
import eu.mcone.buildsystem.command.SkullCMD;
import eu.mcone.buildsystem.listener.GeneralPlayerListener;
import eu.mcone.buildsystem.listener.SecretSignsListener;
import eu.mcone.buildsystem.listener.WeatherChangeListener;
import eu.mcone.buildsystem.util.Objective;
import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.coresystem.api.core.player.Group;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Set;

public class BuildSystem extends CorePlugin implements PermissionsProvider {

    @Getter
    private static BuildSystem instance;
    @Getter
    private CoreWorld world;

    public BuildSystem() {
        super("buildsystem", ChatColor.YELLOW, "build.prefix");
    }

    public void onEnable() {
        instance = this;
        world = CoreSystem.getInstance().getWorldManager().getWorld("plots");
        CoreSystem.getInstance().getWorldManager().enableUploadCommand(true);
        CoreSystem.getInstance().getTranslationManager().loadCategories(this);

        sendConsoleMessage("§aProviding WEPIF Permissions!");

        sendConsoleMessage("§aListener und Events werden geöaden!");
        registerEvents(
                new GeneralPlayerListener(),
                new SecretSignsListener(),
                new WeatherChangeListener()
        );
        registerCommands(
                new SkullCMD()
        );
        CoreSystem.getInstance().enableSpawnCommand(this, world, 0);
        CoreSystem.getInstance().enableHomeSystem(this, 0);
        CoreSystem.getInstance().enableTpaSystem(this, 0);

        sendConsoleMessage("§aVersion §f" + this.getDescription().getVersion() + "§a wurde aktiviert...");

        for (CorePlayer p : CoreSystem.getInstance().getOnlineCorePlayers()) {
            p.getScoreboard().setNewObjective(new Objective());
        }
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

}
