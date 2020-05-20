package eu.mcone.buildsystem.command;

import eu.mcone.buildsystem.inventorys.worldtools.WorldToolsTeamInventory;
import eu.mcone.buildsystem.inventorys.worldtools.WorldToolsInventory;
import eu.mcone.coresystem.api.bukkit.command.CorePlayerCommand;
import org.bukkit.entity.Player;

public class WorldToolsCMD extends CorePlayerCommand {

    public WorldToolsCMD() {
        super("worldtools", null, "wt");
    }

    @Override
    public boolean onPlayerCommand(Player p, String[] args) {
        if (!p.hasPermission("build.team")) {
            new WorldToolsInventory(p);
        } else {
            new WorldToolsTeamInventory(p);
        }
        return true;
    }
}
