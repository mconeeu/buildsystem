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
import eu.mcone.buildsystem.util.Objective;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.hologram.HologramManager;
import eu.mcone.coresystem.api.bukkit.npc.NpcManager;
import eu.mcone.coresystem.api.bukkit.player.BukkitCorePlayer;
import eu.mcone.coresystem.api.bukkit.world.LocationManager;
import eu.mcone.coresystem.api.core.player.Group;
import eu.mcone.coresystem.api.core.translation.TranslationField;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Set;

public class BuildSystem extends JavaPlugin implements PermissionsProvider {

    @Getter
    private static BuildSystem instance;
    private static String MainPrefix = "§8[§eBuildSystem§8] ";

    @Getter
    private HologramManager hologramManager;
    @Getter
    private NpcManager npcManager;
    @Getter
    private LocationManager locationManager;

    public void onEnable() {
        instance = this;
        registerTranslations();

        getServer().getConsoleSender().sendMessage(MainPrefix + "§aProviding WEPIF Permissions!");

        Bukkit.getServer().getConsoleSender().sendMessage(MainPrefix + "§aHologram-Manager wird gestartet");
        hologramManager = CoreSystem.getInstance().inititaliseHologramManager("Build");

        Bukkit.getServer().getConsoleSender().sendMessage(MainPrefix + "§aNPC-Manager wird gestartet");
        npcManager = CoreSystem.getInstance().initialiseNpcManager("Build");

        Bukkit.getServer().getConsoleSender().sendMessage(MainPrefix + "§aLocationManager witd initiiert");
        locationManager = CoreSystem.getInstance().initialiseLocationManager("Build").downloadLocations();

        getServer().getConsoleSender().sendMessage(MainPrefix + "§aListener und Events werden geöaden!");
        getServer().getPluginManager().registerEvents(new PlayerChangedWorld(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new SignChange(), this);

        getCommand("tpa").setExecutor(new TpaCMD());
        getCommand("tpaccept").setExecutor(new TpacceptCMD());
        getCommand("tpdeny").setExecutor(new TpdenyCMD());
        getCommand("skull").setExecutor(new SkullCMD());

        Bukkit.getServer().getConsoleSender().sendMessage(MainPrefix + "§aVersion §f" + this.getDescription().getVersion() + "§a wurde aktiviert...");

        for (BukkitCorePlayer p : CoreSystem.getInstance().getOnlineCorePlayers()) {
            p.getScoreboard().setNewObjective(new Objective());
        }
    }

    public void onDisable() {
        Bukkit.getServer().getConsoleSender().sendMessage(MainPrefix + "§cPlugin wurde deaktiviert!");
        npcManager.unsetNPCs();
    }

    private void registerTranslations() {
        CoreSystem.getInstance().getTranslationManager().insertIfNotExists(
                new HashMap<String, TranslationField>(){{
                    put("System-Prefix", new TranslationField("§8[§7§l!§8] §eBuild §8» §7"));
                }}
        );
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
        BukkitCorePlayer p = CoreSystem.getInstance().getCorePlayer(offlinePlayer.getPlayer());
        if (p != null) {
            for (Group g : p.getGroups()) {
                if (g.getName().equalsIgnoreCase(group)) return true;
            }
        }
        return false;
    }

    @Override
    public String[] getGroups(OfflinePlayer offlinePlayer) {
        BukkitCorePlayer p = CoreSystem.getInstance().getCorePlayer(offlinePlayer.getPlayer());
        if (p != null) {
            Set<String> groups = p.getPermissions();
            return groups.toArray(new String[groups.size()]);
        }

        return null;
    }

}
