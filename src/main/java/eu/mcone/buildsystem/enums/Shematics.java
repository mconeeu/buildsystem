package eu.mcone.buildsystem.enums;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public enum Shematics {

    TREE_1("Baum-1", "tree_2", true, new ItemBuilder(Material.getMaterial(6)).displayName("§c§lBaum-1").create()),
    TREE_2("Baum-2", "tree_3", true, new ItemBuilder(Material.getMaterial(6), 1).displayName("§c§lBaum-2").create()),
    TREE_3("Baum-3", "tree_3", true, new ItemBuilder(Material.getMaterial(6), 1).displayName("§c§lBaum-3").create()),
    TREE_4("Baum-4", "tree_4", true, new ItemBuilder(Material.getMaterial(6), 1).displayName("§c§lBaum-4").create()),
    TREE_5("Baum-5", "tree_5", true, new ItemBuilder(Material.getMaterial(6), 1).displayName("§c§lBaum-5").create()),
    TREE_6("Baum-6", "tree_6", true, new ItemBuilder(Material.getMaterial(6), 1).displayName("§c§lBaum-6").create()),
    TREE_7("Baum-7", "tree_7", true, new ItemBuilder(Material.getMaterial(6), 1).displayName("§c§lBaum-7").create()),
    FLOWER_1("Blume-1", "pflanze", true, new ItemBuilder(Material.getMaterial(38), 1).displayName("§d§lPlanze-1").create()),
    FLOWER_2("Blume-2", "pflanze2", true, new ItemBuilder(Material.getMaterial(38), 1, 2).displayName("§d§lPlanze-2").create()),
    FLOWER_3("Blume-3", "pflanze3", false, new ItemBuilder(Material.getMaterial(38), 1, 4).displayName("§d§lPlanze-3").create()),
    FLOWER_4("Blume-4", "pflanze4", false, new ItemBuilder(Material.getMaterial(38), 1, 4).displayName("§d§lPlanze-4").create()),
    FLOWER_5("Blume-5", "pflanze5", true, new ItemBuilder(Material.getMaterial(38), 1, 4).displayName("§d§lPlanze-5").create()),
    FLOWER_6("Blume-6", "pflanze6", true, new ItemBuilder(Material.getMaterial(38), 1, 4).displayName("§d§lPlanze-6").create()),
    FLOWER_7("Blume-7", "pflanze7", true, new ItemBuilder(Material.getMaterial(38), 1, 4).displayName("§d§lPlanze-7").create());

    private final String name, shematicName;
    private final Boolean isListed;
    private final ItemStack item;

    Shematics(String name, String shematicName, Boolean isListed, ItemStack item) {
        this.name = name;
        this.shematicName = shematicName;
        this.isListed = isListed;
        this.item = item;
    }
}
