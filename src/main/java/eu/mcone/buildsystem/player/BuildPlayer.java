package eu.mcone.buildsystem.player;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.player.plugin.GamePlayerData;
import eu.mcone.coresystem.api.bukkit.player.profile.PlayerDataProfile;

public class BuildPlayer extends GamePlayerData<PlayerDataProfile> {

    public BuildPlayer(CorePlayer player) {
        super(player);
        BuildSystem.getInstance().registerBuildPlayer(this);
    }

    @Override
    protected PlayerDataProfile loadData() {
        return BuildSystem.getInstance().loadGameProfile(corePlayer.bukkit(), PlayerDataProfile.class);
    }

    @Override
    public void saveData() {
        BuildSystem.getInstance().saveGameProfile(new PlayerDataProfile(corePlayer.bukkit(), homes));
    }

}
