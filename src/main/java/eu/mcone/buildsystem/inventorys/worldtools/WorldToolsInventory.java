package eu.mcone.buildsystem.inventorys.worldtools;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.player.BuildPlayer;
import eu.mcone.buildsystem.worldtools.WorldRole;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;

import java.util.HashMap;

public class WorldToolsInventory extends CoreInventory {

    public WorldToolsInventory(Player p) {
        super(
                "§3§lDeine Welten",
                p,
                (((Bukkit.getWorlds().size() - 1) / 9) + 1) * 9,
                InventoryOption.FILL_EMPTY_SLOTS
        );
        BuildPlayer bp = BuildSystem.getInstance().getBuildPlayer(p);

        int i = 0;
        for (World world : Bukkit.getWorlds()) {
            if (bp.hasAccessTo(world) || p.getWorld().equals(world)) {
                setItem(
                        i,
                        new ItemBuilder(Material.GRASS)
                                .displayName("§3§l" + world.getName())
                                .lore(p.getWorld().equals(world) && bp.getWorldRole(world) == null
                                        ? new String[]{"§7§oDu bist nur Besucher!"}
                                        : getLore(bp, world)
                                )
                                .enchantments(new HashMap<Enchantment, Integer>() {{
                                    if (p.getWorld().equals(world)) {
                                        put(Enchantment.DURABILITY, 1);
                                    }
                                }})
                                .itemFlags(ItemFlag.HIDE_ENCHANTS)
                                .create(),
                        !p.getWorld().equals(world) || bp.getWorldRole(world) != null ? e -> {
                            if (e.getClick().equals(ClickType.LEFT)) {
                                p.teleport(world.getSpawnLocation());
                            } else if (e.getClick().equals(ClickType.RIGHT) && bp.getWorldRole(world).equals(WorldRole.OWNER)) {
                                new WorldToolsPlayersInventory(p, world);
                            }
                        } : null
                );
                i++;
            }
        }

        openInventory();
    }

    private static String[] getLore(BuildPlayer bp, World world) {
        return bp.getWorldRole(world).equals(WorldRole.OWNER)
                ? new String[]{
                "§7Dein Rang: " + bp.getWorldRoleLabel(world),
                "",
                "§8» §f§nLinksklick§8 | §7§oTeleportieren",
                "§8» §f§nRechtsklick§8 | §7§oRollen bearbeiten"
        }
                : new String[]{
                bp.getWorldRoleLabel(world),
                "",
                "§8» §f§nLinksklick§8 | §7§oTeleportieren"
        };
    }

}
