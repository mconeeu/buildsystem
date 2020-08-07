package eu.mcone.buildsystem.inventorys;

import com.intellectualcrafters.plot.object.Plot;
import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.player.BuildPlayer;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PlotFinishInventory extends CoreInventory {

    public PlotFinishInventory(Player p, BuildPlayer bp, Plot plot) {
        super("Plot abschicken", p, InventorySlot.ROW_3, InventoryOption.FILL_EMPTY_SLOTS);

        setItem(InventorySlot.ROW_2_SLOT_5, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 13)
                .displayName("§aAbschicken")
                .lore("§7§oBestätige das du dein Plot/Welt fertig", "§7§ogebaut hast §7§ound es", "§7§ofür deine Bewerbung bereit ist!")
                .create(), e -> {
            p.closeInventory();

            BuildSystem.getInstance().getMessenger().send(p, "§2Du hast dein Plot mit der ID §a"+plot.getId()+"§2 erfolgreich abgeschickt, du kannst es aber noch weiterhin bearbeiten!");
            BuildSystem.getInstance().getMessenger().send(p, "§2Wir werden dich per E-Mail kontaktieren, falls du es weiter geschafft hast!");

            bp.setFinishedPlot(plot);
        });

        openInventory();
    }

}
