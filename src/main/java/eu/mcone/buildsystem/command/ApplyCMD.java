package eu.mcone.buildsystem.command;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.inventorys.ApplyInventory;
import eu.mcone.buildsystem.player.BuildPlayer;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.command.CorePlayerCommand;
import org.bukkit.entity.Player;

public class ApplyCMD extends CorePlayerCommand {

    public ApplyCMD() {
        super("apply");
    }

    @Override
    public boolean onPlayerCommand(Player player, String[] args) {
        BuildPlayer buildPlayer = BuildSystem.getInstance().getBuildPlayer(player);

        if (buildPlayer.getAccepted().containsKey(true) && buildPlayer.getAccepted().containsValue(true)) {

            new ApplyInventory(player);

        } else {
            CoreSystem.getInstance().getMessenger().send(player, "§4Du bist in deiner Builder Bewerbung noch nicht so weit!");
        }


        return false;
    }
}
