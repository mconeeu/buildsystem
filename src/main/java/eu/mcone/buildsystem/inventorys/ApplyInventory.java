package eu.mcone.buildsystem.inventorys;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.player.BuildPlayer;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ApplyInventory extends CoreInventory {

    public ApplyInventory(Player player) {
        super("§fApply", player, InventorySlot.ROW_3, InventoryOption.FILL_EMPTY_SLOTS);

        BuildPlayer buildPlayer = BuildSystem.getInstance().getBuildPlayer(player);

        setItem(InventorySlot.ROW_2_SLOT_5, new ItemBuilder(Material.GRASS).displayName("§fDeine Vorbau Welt").lore("§2Thema: §a" + buildPlayer.getThema(),"", "§8» §f§nLinksklick§8 | §7§oTeleportieren").create(), e -> {
            if (CoreSystem.getInstance().getWorldManager().getWorld(buildPlayer.getBuildWorld().getName()) != null) {
                CoreSystem.getInstance().getWorldManager().getWorld(buildPlayer.getBuildWorld().getName()).teleportSilently(player, "spawn");
                BuildSystem.getInstance().getMessenger().send(player, "§2Dein Thema: §a" + buildPlayer.getThema());
                BuildSystem.getInstance().getMessenger().send(player, "§2Wenn du denkst das deine Welt fertig ist benutze §a/finish");
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 1);
            } else {
                BuildSystem.getInstance().getMessenger().send(player, "§4Deine Welt wird erstellt...");
                player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
            }
        });

        openInventory();
    }
}
