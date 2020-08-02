package eu.mcone.buildsystem.player;

import eu.mcone.coresystem.api.bukkit.player.profile.GameProfile;
import eu.mcone.coresystem.api.bukkit.player.profile.PlayerDataProfile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class BuildPlayerDataProfile extends PlayerDataProfile {

    private HashMap<Boolean, Boolean> accepted;
    private String thema;
    private World buildWorld;
    private Boolean deny;

    BuildPlayerDataProfile(Player p, Map<String, Location> homes, HashMap<Boolean, Boolean> accept, String thema, World buildWorld, Boolean deny) {
        super(p, homes);

        this.accepted = accept;
        this.thema = thema;
        this.buildWorld = buildWorld;
        this.deny = deny;
    }

    @Override
    public void doSetData(Player player) {}

}
