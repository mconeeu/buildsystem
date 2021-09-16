/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.worldtools;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public enum WorldState {

    DONE(new ItemBuilder(Material.HARD_CLAY, 1, DyeColor.GREEN.getWoolData()).lore("§a§oFertig").create()),
    IN_WORK(new ItemBuilder(Material.HARD_CLAY, 1, DyeColor.YELLOW.getWoolData()).lore("§e§oIn Arbeit").create()),
    TO_DO(new ItemBuilder(Material.HARD_CLAY, 1, DyeColor.RED.getWoolData()).lore("§c§oTodo").create()),
    CANCELLED(new ItemBuilder(Material.HARD_CLAY, 1, DyeColor.GRAY.getWoolData()).lore("§8§oAbgebrochen").create());

    private final ItemStack item;

    WorldState(ItemStack item) {
        this.item = item;
    }

}
