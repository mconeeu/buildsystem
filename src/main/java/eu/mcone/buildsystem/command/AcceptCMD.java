package eu.mcone.buildsystem.command;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.player.BuildPlayer;
import eu.mcone.buildsystem.worldtools.WorldRole;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.command.CorePlayerCommand;
import eu.mcone.coresystem.api.bukkit.world.WorldCreateProperties;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class AcceptCMD extends CorePlayerCommand {


    public AcceptCMD() {
        super("/accept");
    }

    @Override
    public boolean onPlayerCommand(Player player, String[] args) {

        if (player.hasPermission("buildsystem.accept.apply")) {
            if (args.length == 2) {
                Player target = Bukkit.getPlayer(args[0]);

                if (target != null) {
                    if (!target.getName().equalsIgnoreCase(player.getName())) {
                        BuildPlayer buildPlayer = BuildSystem.getInstance().getBuildPlayer(target);

                        if (args[1].equalsIgnoreCase("yes")) {
                            if (buildPlayer.getAccepted().containsKey(false)) {
                                buildPlayer.getAccepted().put(true, false);
                                BuildSystem.getInstance().getMessenger().send(player, "§2Du hast die Bewerbung von §c" + target.getName() + " §2angenommen und er wurde zum 2 Schritt geleitet!");

                                World world = CoreSystem.getInstance().getWorldManager().createWorld(target.getName(), WorldCreateProperties.builder()
                                        .worldType(WorldType.FLAT)
                                        .allowAnimals(true)
                                        .allowMonsters(true)
                                        .pvp(false)
                                        .autoSave(true)
                                        .generateStructures(false)
                                        .spawnAnimals(false)
                                        .spawnMonsters(false)
                                        .build());

                                BuildSystem.getInstance().getWorldManager().setWorldRole(target.getUniqueId(), target.getName(), world, WorldRole.OWNER);

                                buildPlayer.setBuildWorld(world);

                            } else if (buildPlayer.getAccepted().containsKey(true) && buildPlayer.getAccepted().containsValue(false)) {
                                BuildSystem.getInstance().getMessenger().send(player, "§2Du hast die Bewerbung von §c" + target.getName() + " §2angenommen!");
                                buildPlayer.getAccepted().put(false, true);
                            }
                        } else if (args[1].equalsIgnoreCase("no")) {
                            buildPlayer.setDeny(true);
                            BuildSystem.getInstance().getMessenger().send(player, "§4Du hast die Bewerbung von §c" + target.getName() + " §4abgelehnt!");
                        }
                    } else {
                        BuildSystem.getInstance().getMessenger().send(player, "§4Du darfst nicht dein eigene Builder Bewerbung annehmen!");
                    }
                } else {
                    BuildSystem.getInstance().getMessenger().send(player, "§4Der Spieler ist nicht Online!");
                }
            } else {
                BuildSystem.getInstance().getMessenger().send(player, "§4Bitte benutze §c/accept <Spieler> [<yes|no>]");
            }

        } else {
            BuildSystem.getInstance().getMessenger().send(player, "§4Du hast für diesen Befehl keine Berechtigungen!");
        }
        return false;
    }
}
