/*
 * Copyright (c) 2017 - 2018 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.command;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.coresystem.api.bukkit.command.CorePlayerCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TpdenyCMD extends CorePlayerCommand {

    public TpdenyCMD() {
        super("tpdeny");
    }

    @Override
    public boolean onPlayerCommand(Player p, String[] args) {
        if (args.length == 1) {
            Player t = Bukkit.getPlayer(args[0]);

            if (t != null) {
                if (TpaCMD.players.containsKey(t.getName()) && TpaCMD.players.get(t.getName()).contains(p.getName())) {
                    BuildSystem.getInstance().getMessager().send(p, "§7Du hast die Teleportanfrage von §f"+t.getName()+"§7 abgelehnt!");
                    BuildSystem.getInstance().getMessager().send(t, "§f"+args[0]+"§7 hat deine Anfrage §cabgelehnt§7!");
                    TpaCMD.players.get(t.getName()).remove(p.getName());
                } else {
                    BuildSystem.getInstance().getMessager().send(p, "§4Dieser Spieler hat dir keine Teleportanfrage geschickt!");
                }
            } else {
                BuildSystem.getInstance().getMessager().send(p, "§4Dieser Spieler ist nicht online!");
            }
        } else {
            BuildSystem.getInstance().getMessager().send(p, "§4Bitte benutze: §c/tpdeny <Spieler>");
        }
        return true;
    }

}
