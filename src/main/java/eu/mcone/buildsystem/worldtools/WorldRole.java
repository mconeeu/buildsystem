/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.worldtools;

import lombok.Getter;
import org.bukkit.Material;

@Getter
public enum WorldRole {

    OWNER("§cOwner", "§8[O] ", Material.DIAMOND_HELMET),
    BUILDER("§eBuilder", "§8[B] ", Material.DIAMOND_PICKAXE),
    GUEST("§7Gast", "§8[G] ", Material.ENDER_PEARL);

    private final String label, prefix;
    private final Material item;

    WorldRole(String label, String prefix, Material item) {
        this.label = label;
        this.prefix = prefix;
        this.item = item;
    }

}
