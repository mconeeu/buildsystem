package eu.mcone.buildsystem.player;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotPlayer;
import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.coresystem.api.bukkit.player.profile.PlayerDataProfile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class BuildPlayerDataProfile extends PlayerDataProfile {

    private String finishedPlotId = null;
    private transient Plot finishedPlot = null;

    BuildPlayerDataProfile(Player p, Map<String, Location> homes, Plot finishedPlot) {
        super(p, homes);

        if (finishedPlot != null) {
            this.finishedPlotId = finishedPlot.getId().toString();
        }
    }

    @Override
    public void doSetData(Player player) {
        Plot plot = null;

        if (finishedPlotId != null) {
            for (Plot playerPlot : PlotPlayer.wrap(player).getPlots()) {
                if (playerPlot.getId().toString().equals(finishedPlotId)) {
                    plot = playerPlot;
                }
            }

            if (plot == null) {
                BuildSystem.getInstance().sendConsoleMessage("§cPlot with id "+finishedPlotId+" from player "+player.getName()+" is marked as finished but does not exist! Deleting finish mark.");
                finishedPlotId = null;
            }
        }

        this.finishedPlot = plot;
    }

}
