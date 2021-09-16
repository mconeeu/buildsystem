/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.worldtools.create;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.WorldType;

@Getter
public enum CustomWorldType {

    VOID("Void", Material.COAL, WorldType.FLAT, "2;0;1;"),
    FLAT("Flat", Material.GRASS, WorldType.FLAT, null),
    NORMAL("Normal", Material.STONE, WorldType.NORMAL, null);

    private final String name;
    private final Material item;
    private final WorldType worldType;
    private final String generatorSettings;

    CustomWorldType(String name, Material item, WorldType worldType, String generatorSettings) {
        this.name = name;
        this.item = item;
        this.worldType = worldType;
        this.generatorSettings = generatorSettings;
    }

}
