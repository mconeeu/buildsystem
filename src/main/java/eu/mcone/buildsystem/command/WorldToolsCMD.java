package eu.mcone.buildsystem.command;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.inventorys.worldtools.WorldToolsAllWorldsInventory;
import eu.mcone.buildsystem.inventorys.worldtools.WorldToolsInventory;
import eu.mcone.buildsystem.player.BuildPlayer;
import eu.mcone.buildsystem.player.WorldRole;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.command.CorePlayerCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class WorldToolsCMD extends CorePlayerCommand {

    public WorldToolsCMD() {
        super("worldtools");
    }


    @Override
    public boolean onPlayerCommand(Player p, String[] args) {
        BuildPlayer bp = BuildSystem.getInstance().getBuildPlayer(p);

        new WorldToolsInventory(p);


    /*    if (p.hasPermission("coresystem.worldtools.cmd")) {
            if (args.length == 3) {
                Player t = Bukkit.getPlayer(args[1]);
                BuildPlayer bt = BuildSystem.getInstance().getBuildPlayer(t);
                String world = args[2];
                if (!world.equalsIgnoreCase("plots")) {
                    if (t != null) {
                        if (!t.hasPermission("buildsystem.builder")) {
                            if (Bukkit.getWorld(world) != null) {
                                if (args[0].equalsIgnoreCase("add")) {
                                    if (!bt.getWorldPermission(p.getWorld()).equals(WorldRole.BUILDER) || !bt.getWorldPermission(p.getWorld()).equals(WorldRole.OWNER)) {
                                        bt.setWorldPermission(Bukkit.getWorld(world), WorldRole.BUILDER);
                                        CoreSystem.getInstance().getMessenger().send(p, "§aDer Spieler hat nun auf dieser Welt Berechtigungen!");
                                    } else {
                                        CoreSystem.getInstance().getMessenger().send(p, "§4Der Spieler hat auf §cdieser Welt §4bereits Berechtigungen!");
                                    }
                                } else if (args[0].equalsIgnoreCase("remove")) {
                                    if (bt.getWorldPermission(p.getWorld()).equals(WorldRole.BUILDER) || bt.getWorldPermission(p.getWorld()).equals(WorldRole.OWNER)) {
                                        bt.removeWorldPermission(Bukkit.getWorld(world));
                                        CoreSystem.getInstance().getMessenger().send(p, "§aDer Spieler hat nun auf dieser Welt keine Berechtigungen mehr!");
                                    } else {
                                        CoreSystem.getInstance().getMessenger().send(p, "§4Der Spieler hat auf §cdieser Welt §4keine Berechtigungen!");
                                    }
                                } else {
                                    CoreSystem.getInstance().getMessenger().send(p, "§4Bitte benutze: §c/worldtools <add|remove> <[Spieler]> <[Welt]>");
                                }
                            } else {
                                CoreSystem.getInstance().getMessenger().send(p, "§4Diese Welt existiert nicht!");
                            }
                        } else {
                            CoreSystem.getInstance().getMessenger().send(p, "§4Du darfst kein Team-Mitglied Rechten auf der Welt entfernen oder hinzufügen");
                        }
                    } else {
                        CoreSystem.getInstance().getMessenger().send(p, "§4Der Spieler ist Offline");
                    }
                } else {
                    CoreSystem.getInstance().getMessenger().send(p, "§4Auf der Plot Welt kannst du keinen Spieler Rechte geben");
                }
            } else {
                CoreSystem.getInstance().getMessenger().send(p, "§4Bitte benutze: §c/worldtools <add|remove> <[Spieler]> <[Welt]>");
            }
        } else {
            CoreSystem.getInstance().getMessenger().send(p, "§4Du hast keine Berechtigungen auf diesem Befehl!");
        }


     */
        return false;
    }
}
