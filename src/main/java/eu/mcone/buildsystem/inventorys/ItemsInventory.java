package eu.mcone.buildsystem.inventorys;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.enums.Shematics;
import eu.mcone.buildsystem.listener.BlockPlaceEvent;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import org.bukkit.entity.Player;

public class ItemsInventory extends CoreInventory {

    public ItemsInventory(Player player) {
        super("§e§lBau-Specials", player, InventorySlot.ROW_3, InventoryOption.FILL_EMPTY_SLOTS);


        int i = 0;
        for (Shematics shematics : Shematics.values()) {
            if (shematics.getIsListed()) {
                setItem(i, shematics.getItem(), e -> {
                    player.closeInventory();
                    if (!BuildSystem.getInstance().getSpecialItems().containsKey(player)) {
                        player.getInventory().addItem(BlockPlaceEvent.sponge);
                    }
                    BuildSystem.getInstance().getSpecialItems().put(player, shematics.getShematicName());
                    BuildSystem.getInstance().getMessenger().sendSuccess(player, "Du hast das Shematic §a§l" + shematics.getName().toUpperCase() + "§2 ausgewähl!");
                });

                i++;
            }
        }


        openInventory();
    }
}