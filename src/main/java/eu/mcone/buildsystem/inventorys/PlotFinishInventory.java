package eu.mcone.buildsystem.inventorys;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PlotFinishInventory extends CoreInventory {

    public PlotFinishInventory(Player player) {
        super("Plot abschicken", player, InventorySlot.ROW_3, InventoryOption.FILL_EMPTY_SLOTS);


        setItem(InventorySlot.ROW_2_SLOT_5, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 13)
                .displayName("§aAbschicken")
                .lore("§7§oBestätige das du dein Plot fertig", "§7§ogebaut hast §7§ound es", "§7§ofür deine Bewerbung bereit ist!")
                .create(), e -> {
            player.closeInventory();
            CoreSystem.getInstance().getMessenger().send(player, "§aDu hast dein Plot erfolgreich abgeschickt, du kannst es aber noch weiterhin bearbeiten!");
        });


        openInventory();


    }
}
