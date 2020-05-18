package eu.mcone.buildsystem.inventorys.worldtools;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.player.BuildPlayer;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.inventory.anvil.AnvilSlot;
import eu.mcone.coresystem.api.bukkit.inventory.anvil.CoreAnvilInventory;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class WorldToolsRemovePermsInventory extends CoreInventory {

    private int i = 0;
    private static final HashMap<Player, World> World = new HashMap<>();

    private static final CoreAnvilInventory ANVIL_INVENTORY = CoreSystem.getInstance().createAnvilInventory(event -> {
        String name = event.getName();

        Player player = event.getPlayer();
        Player target = Bukkit.getPlayer(name);

        if (target != null && !target.equals(event.getPlayer())) {

            if (event.getSlot().equals(AnvilSlot.OUTPUT)) {
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 1);
                BuildPlayer buildtarger = BuildSystem.getInstance().getBuildPlayer(target);
                buildtarger.demotePermissions(World.get(player), player);
                World.remove(player);
            }
        } else {
            CoreSystem.getInstance().getMessenger().send(player, "§4Dieser Spieler ist nicht Online!");
            player.closeInventory();
            World.remove(player);
        }
    }).setItem(AnvilSlot.INPUT_LEFT, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 13).displayName("?").create());

    public WorldToolsRemovePermsInventory(Player player) {
        super("§fWorldTools", player, InventorySlot.ROW_5, InventoryOption.FILL_EMPTY_SLOTS);

        for (World world : Bukkit.getWorlds()) {
            setItem(i, new ItemBuilder(Material.GRASS, 1, 0).displayName("§f" + world.getName()).create(), e -> {
                        player.closeInventory();
                        ANVIL_INVENTORY.open(player);
                        World.put(player, world);
                    }
            );
            i++;
        }


        setItem(InventorySlot.ROW_5_SLOT_9,
                new ItemBuilder(Material.IRON_DOOR, 1, 0).displayName("§7Zurück").create(),
                e -> new WorldToolsInventory(player));

        openInventory();

    }
}
