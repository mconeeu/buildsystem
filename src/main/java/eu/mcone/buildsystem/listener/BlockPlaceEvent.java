package eu.mcone.buildsystem.listener;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.enums.Shematics;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class BlockPlaceEvent implements Listener {

    public static ItemStack sponge = new ItemBuilder(Material.SPONGE).displayName("§e§lBau-Special").enchantment(Enchantment.SILK_TOUCH, 1).itemFlags(ItemFlag.HIDE_ENCHANTS).create();
    private final Random RANDOM = new Random();

    private int getRandomNumberInRange() {
        return RANDOM.nextInt((10 - 1) + 1) + 1;
    }

    @EventHandler
    public void onPlace(org.bukkit.event.block.BlockPlaceEvent e) {
        Player p = e.getPlayer();

        Block block = e.getBlock();
        Material material = e.getBlockPlaced().getType();


        if (material.equals(sponge.getType())) {
            if (BuildSystem.getInstance().getSpecialItems().containsKey(p)) {
                for (Shematics shematics : Shematics.values()) {
                    if (shematics.getShematicName().equals(BuildSystem.getInstance().getSpecialItems().get(p))) {

                        block.setType(Material.AIR);
                        p.teleport(block.getLocation());
                        p.performCommand("/schem clear");

                        if (shematics.getShematicName().equals("pflanze2")) {
                            if (getRandomNumberInRange() < 5) {
                                p.performCommand("/schem load pflanze4");
                            } else {
                                p.performCommand("/schem load pflanze3");
                            }
                            p.performCommand("/paste -a");
                        }

                        p.performCommand("/schem load " + shematics.getShematicName());
                        p.performCommand("/paste -a");

                        BuildSystem.getInstance().getMessenger().send(p, "§a" + shematics.getName() + " wurde gesetzt!");


                        break;
                    }
                }
            } else {
                BuildSystem.getInstance().getMessenger().send(p, "§4Du hast kein Special Item ausgewählt!");
            }
        }
    }
}

