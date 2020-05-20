package eu.mcone.buildsystem.inventorys.worldtools;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.worldtools.WorldRole;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.item.Skull;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public class WorldToolsChooseRoleInventory extends CoreInventory {

    public WorldToolsChooseRoleInventory(Player player, World world, UUID targetUuid, String targetName) {
        super("§f§lWelt-Rechte", player, InventorySlot.ROW_1, InventoryOption.FILL_EMPTY_SLOTS);

        setItem(InventorySlot.ROW_1_SLOT_1, new ItemBuilder(Material.BARRIER).displayName("§4Aus Welt entfernen").create(), e -> {
            player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 1);
            BuildSystem.getInstance().getMessenger().send(player, "§2Der Spieler§a "+targetName+"§2 hat nun keine Rolle mehr auf der Welt "+world.getName());

            BuildSystem.getInstance().getWorldManager().removeFromWorld(world, targetUuid, targetName);
            new WorldToolsChooseRoleInventory(player, world, targetUuid, targetName);
        });

        for (int i = 0, x = InventorySlot.ROW_1_SLOT_2; i < WorldRole.values().length; i++, x++) {
            WorldRole role = WorldRole.values()[i];

            setItem(
                    x,
                    new ItemBuilder(role.getItem()).displayName(role.getLabel()).create(),
                    e -> {
                        player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 1);
                        BuildSystem.getInstance().getMessenger().send(player, "§2Der Spieler§a "+targetName+"§2 hat nun die Rolle§f "+role.getLabel()+"§2 auf der Welt "+world.getName());

                        BuildSystem.getInstance().getWorldManager().setWorldRole(targetUuid, targetName, world, role);
                        new WorldToolsChooseRoleInventory(player, world, targetUuid, targetName);
                    }
            );
        }

        setItem(
                InventorySlot.ROW_1_SLOT_8,
                new Skull(targetName)
                        .toItemBuilder()
                        .displayName("§7Du editierst gerade")
                        .lore("§f§l"+targetName)
                        .create(),
                e -> new WorldToolsPlayersInventory(player, world)
        );
        setItem(InventorySlot.ROW_1_SLOT_9, BACK_ITEM, e -> new WorldToolsPlayersInventory(player, world));

        openInventory();
    }



}
