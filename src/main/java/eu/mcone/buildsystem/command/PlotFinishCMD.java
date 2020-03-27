package eu.mcone.buildsystem.command;

import eu.mcone.buildsystem.inventorys.PlotFinishInventory;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.command.CoreCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.swing.*;

public class PlotFinishCMD extends CoreCommand {

    public PlotFinishCMD() {
        super("finish");
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        Player player = (Player)sender;

        if (args.length == 0) {
            new PlotFinishInventory(player);
        } else {
            CoreSystem.getInstance().getMessager().send(player,"§4Bitte benutze §c/finish");
        }



        return false;
    }
}
