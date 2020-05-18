package eu.mcone.buildsystem.inventorys.worldtools;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.player.BuildPlayer;
import eu.mcone.buildsystem.player.WorldRole;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WorldToolsAllWorldsInventory extends CoreInventory {

    private int i = 0;

    public WorldToolsAllWorldsInventory(Player player) {
        super("§fWorldTools", player, InventorySlot.ROW_5, InventoryOption.FILL_EMPTY_SLOTS);
        BuildPlayer buildPlayer = BuildSystem.getInstance().getBuildPlayer(player);

        for (World world : Bukkit.getWorlds()) {
            if (buildPlayer.getWorldPermission(world).equals(WorldRole.BUILDER) || buildPlayer.getWorldPermission(world).equals(WorldRole.OWNER)) {
                setItem(i, new ItemBuilder(Material.GRASS, 1, 0).displayName("§f" + world.getName()).create(), e -> {
                            player.closeInventory();
                            player.teleport(CoreSystem.getInstance().getWorldManager().getWorld(world.getName()).getLocation("spawn"));
                        }
                );
                i++;
            }
        }


        setItem(InventorySlot.ROW_5_SLOT_9,
                new ItemBuilder(Material.IRON_DOOR, 1, 0).displayName("§7Zurück").create(),
                e -> new WorldToolsInventory(player));


        openInventory();
    }
}
