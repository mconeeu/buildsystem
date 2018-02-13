/*
 * Copyright (c) 2017 - 2018 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem;

import com.sk89q.wepif.PermissionsProvider;
import eu.mcone.buildsystem.command.*;
import eu.mcone.buildsystem.listener.PlayerChangedWorld;
import eu.mcone.buildsystem.listener.PlayerJoin;
import eu.mcone.buildsystem.listener.SignChange;
import eu.mcone.buildsystem.util.Objective;
import eu.mcone.coresystem.bukkit.CoreSystem;
import eu.mcone.coresystem.bukkit.command.HoloCMD;
import eu.mcone.coresystem.bukkit.command.NpcCMD;
import eu.mcone.coresystem.bukkit.hologram.HologramManager;
import eu.mcone.coresystem.bukkit.npc.NpcManager;
import eu.mcone.coresystem.bukkit.player.CorePlayer;
import eu.mcone.coresystem.lib.mysql.MySQL_Config;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.util.List;

public class Main extends JavaPlugin implements PermissionsProvider {

    private static Main instance;
    private static String MainPrefix = "§8[§eBuildSystem§8] ";
    public static MySQL_Config config;
    public static HologramManager holo;
    public static NpcManager npcAPI;

    public void onEnable() {
        instance = this;

        getServer().getConsoleSender().sendMessage(MainPrefix + "§aMySQL Config wird geladen!");
        config = new MySQL_Config(CoreSystem.mysql3, "Build", 500);
        registerMySQLConfig();

        Bukkit.getServer().getConsoleSender().sendMessage(MainPrefix + "§aHologram-Manager wird gestartet");
        holo = new HologramManager(CoreSystem.mysql1, "Build");

        Bukkit.getServer().getConsoleSender().sendMessage(MainPrefix + "§aNPC-Manager wird gestartet");
        npcAPI = new NpcManager(CoreSystem.mysql1, "Build");

        getServer().getConsoleSender().sendMessage(MainPrefix + "§aWEPIF PermissionsProvider loaded!");

        getServer().getConsoleSender().sendMessage(MainPrefix + "§aListener und Events werden geöaden!");
        getServer().getPluginManager().registerEvents(new PlayerChangedWorld(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new SignChange(), this);

        getCommand("spawn").setExecutor(new SpawnCMD());
        getCommand("holo").setExecutor(new HoloCMD(holo));
        getCommand("npc").setExecutor(new NpcCMD(npcAPI));
        getCommand("tpa").setExecutor(new TpaCMD());
        getCommand("tpaccept").setExecutor(new TpacceptCMD());
        getCommand("tpdeny").setExecutor(new TpdenyCMD());
        getCommand("skull").setExecutor(new SkullCMD());

        Bukkit.getServer().getConsoleSender().sendMessage(MainPrefix + "§aVersion §f" + this.getDescription().getVersion() + "§a wurde aktiviert...");

        for (CorePlayer p : CoreSystem.getOnlineCorePlayers()) {
            new Objective(p);
            holo.setHolograms(p.bukkit());
        }
    }

    public void onDisable() {
        Bukkit.getServer().getConsoleSender().sendMessage(MainPrefix + "§cPlugin wurde deaktiviert!");
        npcAPI.unsetNPCs();
        holo.unsetHolograms();
    }

    private void registerMySQLConfig() {
        //create table
        config.createTable();

        //system
        config.insertMySQLConfig("System-Prefix", "§8[§7§l!§8] §eBuild §8» §7");

        //store
        config.store();
    }

    @Override
    public boolean hasPermission(String name, String permission) {
        return CoreSystem.getCorePlayer(name).hasPermission(permission);
    }

    @Override
    public boolean hasPermission(String worldName, String name, String permission) {
        return CoreSystem.getCorePlayer(name).hasPermission(permission);
    }

    @Override
    public boolean inGroup(String name, String group) {
        return CoreSystem.getCorePlayer(name).getGroup().equals(group);
    }

    @Override
    public String[] getGroups(String name) {
        List<String> groups = CoreSystem.getCorePlayer(name).getPermissions();
        return groups.toArray(new String[groups.size()]);
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
        CorePlayer p = CoreSystem.getCorePlayer(offlinePlayer.getPlayer());
        return p != null && p.getGroup().equals(group);
    }

    @Override
    public String[] getGroups(OfflinePlayer offlinePlayer) {
        CorePlayer p = CoreSystem.getCorePlayer(offlinePlayer.getPlayer());
        if (p != null) {
            List<String> groups = p.getPermissions();
            return groups.toArray(new String[groups.size()]);
        }

        return null;
    }

    public static Main getInstance() {
        return instance;
    }

}
