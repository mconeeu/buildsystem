/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.inventorys;

import eu.mcone.buildsystem.heads.Head;
import eu.mcone.buildsystem.heads.HeadCategory;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.inventory.CoreItemEvent;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.inventory.anvil.AnvilSlot;
import eu.mcone.coresystem.api.bukkit.inventory.anvil.CoreAnvilInventory;
import eu.mcone.coresystem.api.bukkit.inventory.category.CategoryInventory;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class HeadsInventory extends CategoryInventory {

    private static final ItemStack SEARCH_ITEM = new ItemBuilder(Material.NAME_TAG).displayName("§f§lSuche").lore("§7§oKlicke um nach Köpfen", "§7§ozu Suchen").create();
    private static final ItemStack SEARCH_CATEGORY = new ItemBuilder(Material.NAME_TAG).displayName("§f§lSuche").lore("§7§oDeine Suchergebnisse").create();

    private static final CoreAnvilInventory SEARCH_INVENTORY = CoreSystem.getInstance().createAnvilInventory(event -> {
        String searchText = event.getName();
        Player p = event.getPlayer();

        new HeadsInventory(p, searchText);
    }).setItem(AnvilSlot.INPUT_LEFT, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, DyeColor.LIME.getWoolData()).displayName("?").create());

    public HeadsInventory(Player p) {
        this(p, HeadCategory.values()[0]);
    }

    public HeadsInventory(Player p, HeadCategory category) {
        super("§9§lHead Database", p, category.getItem());

        for (HeadCategory cat : HeadCategory.values()) {
            addCategory(cat.getItem());
        }
        for (Head head : category.getHeads()) {
            ItemStack item = head.getItem();
            addItem(item, getClickEvent(item));
        }

        addCustomPlacedItem(InventorySlot.ROW_6_SLOT_8, SEARCH_ITEM, e -> SEARCH_INVENTORY.open(p));

        openInventory();
    }

    public HeadsInventory(Player p, String search) {
        super("§9§lHeads DB §7| §8Suche", p, SEARCH_CATEGORY);
        search = search.toLowerCase();

        addCategory(SEARCH_CATEGORY);
        for (HeadCategory cat : HeadCategory.values()) {
            addCategory(cat.getItem());
        }

        Set<ItemStack> result = new HashSet<>();
        for (HeadCategory cat : HeadCategory.values()) {
            for (Head head : cat.getHeads()) {
                if (head.getName().toLowerCase().contains(search)) {
                    result.add(head.getItem());
                }
            }
        }

        for (ItemStack item : result) {
            addItem(item, getClickEvent(item));
        }

        openInventory();
    }

    @Override
    protected void openCategoryInventory(ItemStack itemStack, Player player) {
        new HeadsInventory(
                player,
                Objects.requireNonNull(HeadCategory.getCategoryByName(ChatColor.stripColor(
                        itemStack.getItemMeta().getDisplayName()
                )))
        );
    }

    private static CoreItemEvent getClickEvent(ItemStack item) {
        return e -> e.getWhoClicked().getInventory().addItem(item);
    }

}
