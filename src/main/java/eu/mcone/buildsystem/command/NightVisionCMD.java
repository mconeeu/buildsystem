package eu.mcone.buildsystem.command;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.coresystem.api.bukkit.command.CorePlayerCommand;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class NightVisionCMD extends CorePlayerCommand {

    private final ArrayList<Player> list = new ArrayList<>();

    public NightVisionCMD() {
        super("nightvision", "build.nightvision");
    }

    @Override
    public boolean onPlayerCommand(Player player, String[] strings) {

        if (!list.contains(player)) {

            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
            BuildSystem.getInstance().getMessenger().sendSuccess(player, "Du hast Nachtsicht erhalten!");

            list.add(player);

        } else {

            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            BuildSystem.getInstance().getMessenger().sendSuccess(player, "Du nun keine Nachtsicht mehr!");

            list.remove(player);

        }

        return false;
    }
}
