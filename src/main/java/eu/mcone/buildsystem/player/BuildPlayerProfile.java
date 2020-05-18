package eu.mcone.buildsystem.player;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.player.plugin.GamePlayerData;
import eu.mcone.coresystem.api.bukkit.player.profile.PlayerDataProfile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter @Setter
public class BuildPlayerProfile extends PlayerDataProfile {

    private Map<String, WorldRole> allowedWorlds = new HashMap<>();
    private transient Map<World, WorldRole> allowedWoldRoles;

    public BuildPlayerProfile(Player p, Map<String, Location> homes, Map<World, WorldRole> worldRoles) {
        super(p, homes);

        for (Map.Entry<World, WorldRole> entry : worldRoles.entrySet()) {
            allowedWorlds.put(entry.getKey().getName(), entry.getValue());
        }
    }

    @Override
    public void doSetData(Player p) {
        super.doSetData(p);

        allowedWoldRoles = new HashMap<>();
        for (Map.Entry<String, WorldRole> role : allowedWorlds.entrySet()) {
            allowedWoldRoles.put(Bukkit.getWorld(role.getKey()), role.getValue());
        }
    }
}
