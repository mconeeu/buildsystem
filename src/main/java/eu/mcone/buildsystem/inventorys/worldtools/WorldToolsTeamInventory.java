package eu.mcone.buildsystem.inventorys.worldtools;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.worldtools.WorldRole;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.broadcast.SimpleBroadcast;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.inventory.anvil.AnvilSlot;
import eu.mcone.coresystem.api.bukkit.inventory.anvil.CoreAnvilInventory;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.world.WorldCreateProperties;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

public class WorldToolsTeamInventory extends CoreInventory {

    private static final CoreAnvilInventory ANVIL_INVENTORY = CoreSystem.getInstance().createAnvilInventory(event -> {
        String worldName = event.getName();

        Player p = event.getPlayer();

        p.closeInventory();
        BuildSystem.getInstance().getMessenger().send(p, "§cDas kann ein paar Sekunden dauern..");
        BuildSystem.getInstance().getMessenger().broadcast(new SimpleBroadcast("§4Der Spieler §c" + p.getName() + "§4 hat eine neue Welt erstellt!"));

        World world = CoreSystem.getInstance().getWorldManager().createWorld(worldName, WorldCreateProperties.builder()
                .worldType(WorldType.FLAT)
                .allowAnimals(true)
                .allowMonsters(true)
                .pvp(false)
                .autoSave(true)
                .generateStructures(false)
                .spawnAnimals(false)
                .spawnMonsters(false)
                .build());
        BuildSystem.getInstance().getWorldManager().setWorldRole(p.getUniqueId(), p.getName(), world, WorldRole.OWNER);
        BuildSystem.getInstance().getMessenger().send(p, "§aDu hast erfolgreich die Welt §f" + world.getName() + "§a erstellt!");

    }).setItem(AnvilSlot.INPUT_LEFT, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 13).displayName("?").create());

    public WorldToolsTeamInventory(Player player) {
        super("§fWähle aus", player, InventorySlot.ROW_3, InventoryOption.FILL_EMPTY_SLOTS);


        setItem(InventorySlot.ROW_2_SLOT_3, new ItemBuilder(Material.GRASS).displayName("§fDeine Welten").create(), e -> {
            player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 1);
            new WorldToolsInventory(player);
        });

        setItem(InventorySlot.ROW_2_SLOT_7, new ItemBuilder(Material.ANVIL).displayName("§fWelt erstellen").create(), e -> {
            player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 1);
            ANVIL_INVENTORY.open(player);

        });

        openInventory();
    }
}
