package eu.mcone.buildsystem.inventorys.worldtools;

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
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class WorldToolsInventory extends CoreInventory {

    public WorldToolsInventory(Player player) {
        super("§fWorldTools", player, InventorySlot.ROW_3, InventoryOption.FILL_EMPTY_SLOTS);

        setItem(InventorySlot.ROW_2_SLOT_3, new ItemBuilder(Material.GRASS, 1, 0)
                .displayName("§fDeine Welten").create(), e -> {
            new WorldToolsAllWorldsInventory(player);
            player.playSound(player.getLocation(), Sound.CLICK,1,1);
        });

        setItem(InventorySlot.ROW_2_SLOT_5, new ItemBuilder(Material.COOKIE, 1, 0)
                .displayName("§fAlle Ränge").create(), e -> {
            new WorldRanksInventory(player);
            player.playSound(player.getLocation(), Sound.CLICK,1,1);
        });

        setItem(InventorySlot.ROW_2_SLOT_7, new ItemBuilder(Material.TRIPWIRE_HOOK, 1, 0)
                .displayName("§fAdmin Rechte").create(), e -> {
            new WorldToolsChoosePermsInventory(player);
            player.playSound(player.getLocation(), Sound.CLICK,1,1);
        });


        openInventory();
    }
}
