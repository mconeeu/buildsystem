package eu.mcone.buildsystem.inventorys.worldtools;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class WorldToolsChoosePermsInventory extends CoreInventory {

    public WorldToolsChoosePermsInventory(Player player) {
        super("§fWorldTools - §oAuswahl", player, InventorySlot.ROW_3, InventoryOption.FILL_EMPTY_SLOTS);


        setItem(InventorySlot.ROW_2_SLOT_3, new ItemBuilder(Material.INK_SACK, 1, 10)
                .displayName("§fRechte hinzufügen").create(), e -> {
            new WorldToolsAddPermsInventory(player);
            player.playSound(player.getLocation(), Sound.CLICK,1,1);
        });

        setItem(InventorySlot.ROW_2_SLOT_7, new ItemBuilder(Material.INK_SACK, 1, 1)
                .displayName("§fRechte entfernen").create(), e -> {
            new WorldToolsRemovePermsInventory(player);
            player.playSound(player.getLocation(), Sound.CLICK,1,1);
        });

        openInventory();

    }
}
