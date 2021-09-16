/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.worldtools;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorldCategory {

    private ObjectId id;
    private String name, description;
    private Material item;

    public WorldCategory(String name, Material item) {
        this.name = name;
        this.item = item;
    }

    WorldCategory(String name, String description, Material item) {
        this.name = name;
        this.description = description;
        this.item = item;
    }

    public ItemStack calculateItemStack() {
        return new ItemBuilder(item).displayName("§f§l"+name).create();
    }

    public Set<CoreWorld> getWorlds() {
        Set<CoreWorld> worlds = new HashSet<>();

        for (CoreWorld world : CoreSystem.getInstance().getWorldManager().getWorlds()) {
            if (!world.equals(BuildSystem.getInstance().getPlotWorld())) {
                WorldConfig config = BuildSystem.getInstance().getWorldManager().getWorldConfig(world);

                if (config != null) {
                    String category = config.getCategory();

                    if (category != null) {
                        if (id != null ? id.toString().equals(category) : name.equalsIgnoreCase(category)) {
                            worlds.add(world);
                        }
                    } else if (equals(WorldToolsManager.MISCELLANEOUS_CATEGORY)) {
                        worlds.add(world);
                    }
                } else throw new IllegalStateException("Could not get Config for World " + world.getName() + "!");
            }
        }

        return worlds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorldCategory that = (WorldCategory) o;
        return name.equals(that.name) &&
                item == that.item;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, item);
    }

}
