/*
 * Copyright (c) 2017 - 2018 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem;

import com.sk89q.wepif.PermissionsProvider;
import eu.mcone.buildsystem.command.SkullCMD;
import eu.mcone.buildsystem.command.TpaCMD;
import eu.mcone.buildsystem.command.TpacceptCMD;
import eu.mcone.buildsystem.command.TpdenyCMD;
import eu.mcone.buildsystem.listener.PlayerChangedWorld;
import eu.mcone.buildsystem.listener.PlayerJoin;
import eu.mcone.buildsystem.listener.SignChange;
import eu.mcone.buildsystem.listener.WeatherChange;
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
        super("BuildSystem", ChatColor.YELLOW, "build.prefix");
    }

    public void onEnable() {
        instance = this;
        world = CoreSystem.getInstance().getWorldManager().getWorld("plots");
        CoreSystem.getInstance().getWorldManager().enableUploadCommand(true);
        CoreSystem.getInstance().getTranslationManager().loadCategories(this);

        sendConsoleMessage("§aProviding WEPIF Permissions!");

        sendConsoleMessage("§aListener und Events werden geöaden!");
        getServer().getPluginManager().registerEvents(new PlayerChangedWorld(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new SignChange(), this);
        getServer().getPluginManager().registerEvents(new WeatherChange(), this);

        CoreSystem.getInstance().enableSpawnCommand(world);
        getCommand("tpa").setExecutor(new TpaCMD());
        getCommand("tpaccept").setExecutor(new TpacceptCMD());
        getCommand("tpdeny").setExecutor(new TpdenyCMD());
        getCommand("skull").setExecutor(new SkullCMD());

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
