/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.heads;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

@Getter
public enum HeadCategory {

    ALPHABET("Alphabet", ChatColor.GRAY, new ItemBuilder(Material.ITEM_FRAME).displayName("§f§lAlphabet").create() /*"http://textures.minecraft.net/texture/a67d813ae7ffe5be951a4f41f2aa619a5e3894e85ea5d4986f84949c63d7672e"*/),
    ANIMALS("Tiere", ChatColor.GREEN, new ItemBuilder(Material.MONSTER_EGG).displayName("§f§lTiere").create() /*"http://textures.minecraft.net/texture/5d6c6eda942f7f5f71c3161c7306f4aed307d82895f9d2b07ab4525718edc5"*/),
    BLOCKS("Blöcke", ChatColor.RED, new ItemBuilder(Material.DIRT).displayName("§f§lBlöcke").create() /*"http://textures.minecraft.net/texture/11ed9abf51fe4ea84cfcb27297f1bc54cd382edf85e7bd6e75ecca2b806611"*/),
    DECORATION("Dekoration", ChatColor.GOLD, new ItemBuilder(Material.PAINTING).displayName("§f§lDekoration").create() /*"http://textures.minecraft.net/texture/ce22391e35a3e5bcee89db312e874fdc9d9e7a6351314b82bda97fbd2be87eb8"*/),
    FOOD_DRINKS("Essen/Trinken", ChatColor.YELLOW, new ItemBuilder(Material.COOKED_BEEF).displayName("§f§lEssen/Trinken").create() /*"http://textures.minecraft.net/texture/51997da64043b284822115643a654fdc4e8a7226664b48a4e1dbb557b5c0fe14"*/),
    HUMANS("Menschen", ChatColor.BLUE, new ItemBuilder(Material.STONE_PICKAXE).displayName("§f§lMenschen").create() /*"http://textures.minecraft.net/texture/eb7af9e4411217c7de9c60acbd3c3fd6519783332a1b3bc56fbfce90721ef35"*/),
    HUMANOID("Menschlich", ChatColor.DARK_AQUA, new ItemBuilder(Material.STONE_AXE).displayName("§f§lMenschlich").create() /*"http://textures.minecraft.net/texture/822d8e751c8f2fd4c8942c44bdb2f5ca4d8ae8e575ed3eb34c18a86e93b"*/),
    MISCELLANEOUS("Verschiedenes", ChatColor.LIGHT_PURPLE, new ItemBuilder(Material.DEAD_BUSH).displayName("§f§lVerschiedenes").create() /*"http://textures.minecraft.net/texture/52e98165deef4ed621953921c1ef817dc638af71c1934a4287b69d7a31f6b8"*/),
    MONSTERS("Monster", ChatColor.DARK_GREEN, new ItemBuilder(Material.MOB_SPAWNER).displayName("§f§lMonster").create() /*"http://textures.minecraft.net/texture/56fc854bb84cf4b7697297973e02b79bc10698460b51a639c60e5e417734e11"*/),
    PLANTS("Pflanzen", ChatColor.GREEN, new ItemBuilder(Material.YELLOW_FLOWER).displayName("§f§lPflanzen").create() /*"http://textures.minecraft.net/texture/cbb311f3ba1c07c3d1147cd210d81fe11fd8ae9e3db212a0fa748946c3633"*/);

    private final String name;
    private final ChatColor color;
    private final ItemStack item;
    private final Set<Head> heads;

    HeadCategory(String name, ChatColor color, ItemStack item) {
        this.name = name;
        this.color = color;
        this.item = item;
        this.heads = new HashSet<>();
    }

    public void addHead(Head head) {
        this.heads.add(head);
    }

    public String getLabel() {
        return color+"§l"+name;
    }

    public static HeadCategory getCategoryByName(String name) {
        for (HeadCategory cat : values()) {
            if (cat.name.equals(name)) {
                return cat;
            }
        }

        return null;
    }

}
