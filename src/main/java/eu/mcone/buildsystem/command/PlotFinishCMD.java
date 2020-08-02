package eu.mcone.buildsystem.command;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.inventorys.PlotFinishInventory;
import eu.mcone.buildsystem.player.BuildPlayer;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.command.CoreCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlotFinishCMD extends CoreCommand {

    public PlotFinishCMD() {
        super("finish");
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            BuildPlayer buildPlayer = BuildSystem.getInstance().getBuildPlayer(player);

            if (buildPlayer.getAccepted().containsKey(false) || buildPlayer.getAccepted().containsValue(true)) {
                new PlotFinishInventory(player);
            } else {
                BuildSystem.getInstance().getMessenger().send(player, "§cDu hast bereits /finish eingeben!");
            }
        } else {
            BuildSystem.getInstance().getMessenger().send(player, "§4Bitte benutze §c/finish");
        }

        return false;
    }
}
