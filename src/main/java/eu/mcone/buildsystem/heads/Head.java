/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.heads;

import eu.mcone.coresystem.api.bukkit.item.Skull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
@Getter
public class Head {

    private final int id;
    private final String name, hashValue, description;

    public ItemStack getItem() {
        return Skull.fromUrl(getHeadUrl()).setDisplayName("§f"+name+" §8(§7"+id+"§8)").lore("§7§o"+description).getItemStack();
    }

    public String getHeadUrl() {
        return "http://textures.minecraft.net/texture/"+hashValue;
    }

}
