/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.inventorys.worldtools;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.worldtools.WorldRole;
import eu.mcone.buildsystem.worldtools.create.CustomWorldEnvironment;
import eu.mcone.buildsystem.worldtools.create.CustomWorldType;
import eu.mcone.buildsystem.worldtools.create.WorldCreateSettings;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.inventory.anvil.AnvilSlot;
import eu.mcone.coresystem.api.bukkit.inventory.anvil.CoreAnvilInventory;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.world.WorldCreateProperties;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class WorldCreateInventory extends CoreInventory {

    private static final Map<Player, WorldCreateSettings> properties = new HashMap<>();

    private static final CoreAnvilInventory WORLD_CREATE_INVENTORY = CoreSystem.getInstance().createAnvilInventory(event -> {
        String worldName = event.getName();
        Player p = event.getPlayer();

        p.closeInventory();

        WorldCreateSettings settings = properties.get(p);
        World world = CoreSystem.getInstance().getWorldManager().createWorld(
                worldName,
                WorldCreateProperties
                        .builder()
                        .worldType(settings.getType().getWorldType())
                        .environment(settings.getEnvironment().getEnvironment())
                        .build(),
                p
        );
        BuildSystem.getInstance().getWorldManager().setWorldRole(world, p.getUniqueId(), WorldRole.OWNER);
    }).setItem(AnvilSlot.INPUT_LEFT, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, DyeColor.LIME.getWoolData()).displayName(" ").create());

    public WorldCreateInventory(Player p) {
        super("§a§lWelt erstellen", p, InventorySlot.ROW_2, InventoryOption.FILL_EMPTY_SLOTS);
        WorldCreateSettings settings = new WorldCreateSettings();
        properties.put(p, settings);

        setItem(
                InventorySlot.ROW_1_SLOT_1,
                new ItemBuilder(Material.DEAD_BUSH)
                        .displayName("§9§lWelten Typ")
                        .lore("§7§oWähle einen Weltentyp", "§7§ofür deine neue Welt aus!")
                        .create()
        );
        setItem(
                InventorySlot.ROW_1_SLOT_2,
                new ItemBuilder(Material.NETHER_STAR)
                        .displayName("§e§lWelten Art")
                        .lore("§7§oWähle eine Weltenart", "§7§ofür deine neue Welt aus!")
                        .create()
        );
        setSettingsItems(p, settings);

        setItem(
                InventorySlot.ROW_1_SLOT_9,
                new ItemBuilder(Material.NAME_TAG)
                        .displayName("§f§lWähle einen Weltentyp")
                        .create()
        );
        setItem(
                InventorySlot.ROW_2_SLOT_9,
                new ItemBuilder(Material.INK_SACK, 1, DyeColor.LIME.getWoolData())
                        .displayName("§a§lWelt erstellen")
                        .create(),
                e -> WORLD_CREATE_INVENTORY.open(p)
        );

        openInventory();
    }

    private void setSettingsItems(Player p, WorldCreateSettings settings) {
        setItem(
                InventorySlot.ROW_2_SLOT_1,
                new ItemBuilder(settings.getType().getItem())
                        .displayName("§f§l"+settings.getType().getName())
                        .create(),
                e -> {
                    settings.setType(
                            getNextEntry(CustomWorldType.values(), settings.getType())
                    );
                    setSettingsItems(p, settings);
                    p.updateInventory();
                }
        );
        setItem(
                InventorySlot.ROW_2_SLOT_2,
                new ItemBuilder(settings.getEnvironment().getItem())
                        .displayName("§f§l"+settings.getEnvironment().getName())
                        .create(),
                e -> {
                    settings.setEnvironment(
                            getNextEntry(CustomWorldEnvironment.values(), settings.getEnvironment())
                    );
                    setSettingsItems(p, settings);
                    p.updateInventory();
                }
        );
    }

    private static <T extends Enum<?>> T getNextEntry(T[] values, T entry) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(entry)) {
                if (i < values.length-1) {
                    return values[++i];
                } else {
                    return values[0];
                }
            }
        }

        return null;
    }

}
