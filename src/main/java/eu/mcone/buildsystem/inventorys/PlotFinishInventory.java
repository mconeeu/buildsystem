package eu.mcone.buildsystem.inventorys;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.player.BuildPlayer;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlotFinishInventory extends CoreInventory {

    private final HashMap<Boolean, Boolean> accpet = new HashMap<>();

    public PlotFinishInventory(Player player) {
        super("Plot abschicken", player, InventorySlot.ROW_3, InventoryOption.FILL_EMPTY_SLOTS);

        BuildPlayer buildPlayer = BuildSystem.getInstance().getBuildPlayer(player);

        setItem(InventorySlot.ROW_2_SLOT_5, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 13)
                .displayName("§aAbschicken")
                .lore("§7§oBestätige das du dein Plot/Welt fertig", "§7§ogebaut hast §7§ound es", "§7§ofür deine Bewerbung bereit ist!")
                .create(), e -> {
            player.closeInventory();


            BuildSystem.getInstance().getMessenger().send(player, "§2Du hast §adein Plot/Welt§2 erfolgreich abgeschickt, du kannst es aber §anoch weiterhin bearbeiten§2!");
            BuildSystem.getInstance().getMessenger().send(player, "§2Du wirst §ainformiert §2wenn du es in die nächte Stufe geschafft hast...");

            accpet.put(true, false);

            buildPlayer.setPlotAccepted(accpet);
        });


        openInventory();


    }
}
