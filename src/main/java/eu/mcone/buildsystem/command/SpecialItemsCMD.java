package eu.mcone.buildsystem.command;

import eu.mcone.buildsystem.inventorys.ItemsInventory;
import eu.mcone.coresystem.api.bukkit.command.CorePlayerCommand;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SpecialItemsCMD extends CorePlayerCommand {

    public SpecialItemsCMD() {
        super("specialitems", "build.specialitems","si");
    }

    @Override
    public boolean onPlayerCommand(Player player, String[] strings) {
        new ItemsInventory(player);
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
        return false;
    }
}
