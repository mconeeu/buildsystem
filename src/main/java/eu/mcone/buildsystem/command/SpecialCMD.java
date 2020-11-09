package eu.mcone.buildsystem.command;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.coresystem.api.bukkit.command.CorePlayerCommand;
import org.bukkit.entity.Player;

public class SpecialCMD extends CorePlayerCommand {

    public SpecialCMD() {
        super("special");
    }

    @Override
    public boolean onPlayerCommand(Player player, String[] strings) {

        if (BuildSystem.getInstance().getSpecialItems().containsKey(player)) {
            BuildSystem.getInstance().getMessenger().send(player, "Dein geladenes Bau Special lautet: §f§l" + BuildSystem.getInstance().getSpecialItems().get(player).toUpperCase());
        } else {
            BuildSystem.getInstance().getMessenger().send(player, "§4Du hast kein Special Item ausgewählt!");
        }
        return false;
    }
}
