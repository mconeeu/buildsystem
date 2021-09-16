package eu.mcone.buildsystem.inventorys.worldtools;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.player.BuildPlayer;
import eu.mcone.buildsystem.worldtools.WorldCategory;
import eu.mcone.buildsystem.worldtools.WorldConfig;
import eu.mcone.buildsystem.worldtools.WorldRole;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.inventory.category.CategoryInventory;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class WorldToolsInventory extends CategoryInventory {

    public WorldToolsInventory(Player p) {
        this(p, BuildSystem.getInstance().getWorldManager().getWorldCategories().iterator().next());
    }

    public WorldToolsInventory(Player p, WorldCategory category) {
        super("Welten", p, category.calculateItemStack());
        BuildPlayer bp = BuildSystem.getInstance().getBuildPlayer(p);
        CorePlayer cp = bp.getCorePlayer();

        for (WorldCategory cat : BuildSystem.getInstance().getWorldManager().getWorldCategories()) {
            addCategory(cat.calculateItemStack());
        }

        for (CoreWorld world : category.getWorlds()) {
            WorldConfig config = BuildSystem.getInstance().getWorldManager().getWorldConfig(world);
            boolean currentWorld = cp.getWorld().equals(world);
            WorldRole role = bp.getWorldRole(world);

            if (currentWorld || role != null) {
                addItem(
                        ItemBuilder.wrap(config.getState().getItem())
                                .displayName("§3§l" + world.getName())
                                .lore(role == null
                                        ? new String[]{"§7§oDu bist nur Besucher!"}
                                        : getLore(bp, world)
                                )
                                .enchantments(new HashMap<Enchantment, Integer>() {{
                                    if (cp.getWorld().equals(world)) {
                                        put(Enchantment.DURABILITY, 1);
                                    }
                                }})
                                .itemFlags(ItemFlag.HIDE_ENCHANTS)
                                .create(),
                        role != null ? e -> {
                            if (e.getClick().equals(ClickType.LEFT)) {
                                world.teleportSilently(p, "spawn");
                            } else if (e.getClick().equals(ClickType.RIGHT) && role.equals(WorldRole.OWNER)) {
                                new WorldToolsPlayersInventory(p, world);
                            }
                        } : null
                );
            }
        }

        if (p.hasPermission("build.team")) {
            addCustomPlacedItem(
                    InventorySlot.ROW_6_SLOT_3,
                    new ItemBuilder(Material.ANVIL)
                            .displayName("§a§lNeue Welt erstellen")
                            .create(),
                    e -> new WorldCreateInventory(p)
            );
        }

        openInventory();
    }

    @Override
    protected void openCategoryInventory(ItemStack itemStack, Player player) {
        new WorldToolsInventory(player, BuildSystem.getInstance().getWorldManager().getWorldCategory(itemStack.getType()));
    }

    private static String[] getLore(BuildPlayer bp, CoreWorld world) {
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
