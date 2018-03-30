/*
 * Copyright (c) 2017 - 2018 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.command;

import eu.mcone.buildsystem.BuildSystem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpacceptCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (args.length == 1) {
                Player t = Bukkit.getPlayer(args[0]);

                if (t != null) {
                    if (TpaCMD.players.containsKey(t.getName()) && TpaCMD.players.get(t.getName()).contains(p.getName())) {
                        p.sendMessage(BuildSystem.config.getConfigValue("System-Prefix") + "§2Du hast die Teleportanfrage von "+t.getName()+" angenommen! Teleportiere...");
                        t.sendMessage(BuildSystem.config.getConfigValue("System-Prefix") + "§a"+p.getName()+"§2 hat deine Anfrage angenommen! Du wirst teleportiert...");
                        t.teleport(p.getLocation());
                        TpaCMD.players.get(t.getName()).remove(p.getName());
                    } else {
                        p.sendMessage(BuildSystem.config.getConfigValue("System-Prefix") + "§4Dieser Spieler hat dir keine Teleportanfrage geschickt!");
                    }
                } else {
                    p.sendMessage(BuildSystem.config.getConfigValue("System-Prefix") + "§4Dieser Spieler ist nicht online!");
                }
            } else {
                p.sendMessage(BuildSystem.config.getConfigValue("System-Prefix") + "§4Bitte benutze: §c/tpaccept <Spieler>");
            }
        }
        return true;
    }
}
