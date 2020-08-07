package eu.mcone.buildsystem.command;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotPlayer;
import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.inventorys.PlotFinishInventory;
import eu.mcone.buildsystem.player.BuildPlayer;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.command.CorePlayerCommand;
import org.bukkit.entity.Player;

public class FinishCMD extends CorePlayerCommand {

    public FinishCMD() {
        super("finish", null, "apply");
    }

    @Override
    public boolean onPlayerCommand(Player p, String[] args) {
        BuildPlayer bp = BuildSystem.getInstance().getBuildPlayer(p);

        if (!bp.hasFinished()) {
            if (CoreSystem.getInstance().getCorePlayer(p).hasLinkedOneGamingAccount()) {
                Plot plot = PlotPlayer.wrap(p).getCurrentPlot();
                new PlotFinishInventory(p, bp, plot);
            } else {
                BuildSystem.getInstance().getMessenger().send(p, "Bitte verlinke deinen Minecraft Account über§c https://id.onegaming.group §4bvor du dein Plot zur Bewerbung einschickst!");
            }
        } else {
            BuildSystem.getInstance().getMessenger().send(p, "§4Du hast bereits dein Plot mit der ID §c" + bp.getFinishedPlot().getId() + "§4 zur Bewebung eingeschickt!");
        }

        return true;
    }

}
