/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.worldtools.create;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.World;

@Getter
public enum CustomWorldEnvironment {

    OVERWORLD("Overworld", Material.GRASS, World.Environment.NORMAL),
    NETHER("Nether", Material.NETHERRACK, World.Environment.NETHER),
    END("End", Material.ENDER_STONE, World.Environment.THE_END);

    private final String name;
    private final Material item;
    private final World.Environment environment;

    CustomWorldEnvironment(String name, Material item, World.Environment environment) {
        this.name = name;
        this.item = item;
        this.environment = environment;
    }

}
