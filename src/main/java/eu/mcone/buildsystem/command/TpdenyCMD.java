package eu.mcone.buildsystem.command;

import eu.mcone.buildsystem.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpdenyCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (args.length == 1) {
                Player t = Bukkit.getPlayer(args[0]);

                if (t != null) {
                    if (TpaCMD.players.containsKey(t.getName()) && TpaCMD.players.get(t.getName()).contains(p.getName())) {
                        p.sendMessage(Main.config.getConfigValue("System-Prefix") + "§7Du hast die Teleportanfrage von §f"+t.getName()+"§7 abgelehnt!");
                        t.sendMessage(Main.config.getConfigValue("System-Prefix") + "§f"+args[0]+"§7 hat deine Anfrage §cabgelehnt§7!");
                        TpaCMD.players.get(t.getName()).remove(p.getName());
                    } else {
                        p.sendMessage(Main.config.getConfigValue("System-Prefix") + "§4Dieser Spieler hat dir keine Teleportanfrage geschickt!");
                    }
                } else {
                    p.sendMessage(Main.config.getConfigValue("System-Prefix") + "§4Dieser Spieler ist nicht online!");
                }
            } else {
                p.sendMessage(Main.config.getConfigValue("System-Prefix") + "§4Bitte benutze: §c/tpdeny <Spieler>");
            }
        }
        return true;
    }
}
