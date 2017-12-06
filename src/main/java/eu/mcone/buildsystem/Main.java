package eu.mcone.buildsystem;

import com.sk89q.wepif.PermissionsProvider;
import eu.mcone.bukkitcoresystem.CoreSystem;
import eu.mcone.bukkitcoresystem.player.CorePlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Main extends JavaPlugin implements PermissionsProvider {

    private static Main instance;
    private static String MainPrefix = "§8[§eBuildSystem§8] ";

    public void onEnable() {
        instance = this;

        getServer().getConsoleSender().sendMessage(MainPrefix + "§aWEPIF PermissionsProvider loaded!");
        Bukkit.getServer().getConsoleSender().sendMessage(MainPrefix + "§aVersion §f" + this.getDescription().getVersion() + "§a wurde aktiviert...");
    }

    public void onDisable() {
        Bukkit.getServer().getConsoleSender().sendMessage(MainPrefix + "§cPlugin wurde deaktiviert!");
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
