package eu.mcone.buildsystem.inventorys.worldtools;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class WorldToolsChoosePermsInventory extends CoreInventory {

    public WorldToolsChoosePermsInventory(Player player) {
        super("§fWorldTools", player, InventorySlot.ROW_3, InventoryOption.FILL_EMPTY_SLOTS);


        setItem(InventorySlot.ROW_2_SLOT_3, new ItemBuilder(Material.INK_SACK, 1, 0)
                .displayName("§fRechte hinzufügen").create(), e -> {
            if (player.hasPermission("worldtools.promote")) {
                new WorldToolsAddPermsInventory(player);
            } else {
                new WorldToolsAddUserPermsInventory(player);
            }
        });

        setItem(InventorySlot.ROW_2_SLOT_7, new ItemBuilder(Material.INK_SACK, 1, 0)
                .displayName("§fRechte entfernen").create(), e -> {
            if (player.hasPermission("worldtools.demote")) {
                new WorldToolsRemovePermsInventory(player);
            } else {
                new WorldToolsRemoveUserPermsInventory(player);
            }
        });

        openInventory();

    }
}
