package eu.mcone.buildsystem.command;

import eu.mcone.buildsystem.Main;
import eu.mcone.bukkitcoresystem.CoreSystem;
import eu.mcone.bukkitcoresystem.util.LocationFactory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!CoreSystem.cooldown.canExecute(this.getClass(), p)) return true;
            CoreSystem.cooldown.addPlayer(p.getUniqueId(), this.getClass());

            if (args.length == 0) {
                Location loc = LocationFactory.getConfigLocation(Main.config, "Location-Spawn");

                if (loc != null) {
                    p.teleport(loc);
                    p.sendMessage(Main.config.getConfigValue("System-Prefix") + "§2Du wurdest zum Spawn teleportiert");
                } else {
                    p.sendMessage(Main.config.getConfigValue("System-Prefix") + "§4Du kannst gerade nicht zum Spawn teleportiert werden.");
                }
                return true;
            } else if (args.length == 1 && args[0].equalsIgnoreCase("set")) {
                if (p.hasPermission("build.set.spawn")) {
                    LocationFactory.updateConfigLocation(p.getLocation(), Main.config, "Location-Spawn");

                    p.sendMessage(Main.config.getConfigValue("System-Prefix") + "§2Der Spawn wurde erfolgreich gesetzt!");
                    return true;
                }
            }

            p.sendMessage(Main.config.getConfigValue("System-Prefix") + "§4Benutze §c/spawn §4um dich zum Spawn zu teleportieren");
        } else {
            Bukkit.getConsoleSender().sendMessage(Main.config.getConfigValue("System-Prefix") + "§4Dieser Befehl kann nur von einem Spieler ausgeführt werden!");
        }
        return true;
    }

}
