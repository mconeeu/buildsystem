package eu.mcone.buildsystem.inventorys.worldtools;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.worldtools.WorldRole;
import eu.mcone.buildsystem.worldtools.WorldRoleEntry;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.inventory.anvil.AnvilSlot;
import eu.mcone.coresystem.api.bukkit.inventory.anvil.CoreAnvilInventory;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.item.Skull;
import eu.mcone.coresystem.api.bukkit.player.OfflineCorePlayer;
import eu.mcone.coresystem.api.core.exception.PlayerNotResolvedException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WorldToolsPlayersInventory extends CoreInventory {

    private static final CoreAnvilInventory ANVIL_INVENTORY = CoreSystem.getInstance().createAnvilInventory(event -> {
        String targetName = event.getName();

        Player p = event.getPlayer();
        Player t = Bukkit.getPlayer(targetName);

        UUID uuid;
        String name;
        World w = WorldToolsPlayersInventory.toSetWorlds.get(p);

        try {
            if (t != null) {
                uuid = t.getUniqueId();
                name = t.getName();

                if (t.hasPermission("build.builder")) {
                    p.closeInventory();
                    BuildSystem.getInstance().getMessenger().send(p, "§4Du hast keine Berechtigung die Rolle von §cTeam-Buildern§4 zu ändern!");
                    return;
                }
            } else {
                OfflineCorePlayer offlinePlayer = CoreSystem.getInstance().getOfflineCorePlayer(targetName);
                uuid = offlinePlayer.getUuid();
                name = offlinePlayer.getName();

                if (offlinePlayer.hasPermission("build.builder")) {
                    p.closeInventory();
                    BuildSystem.getInstance().getMessenger().send(p, "§4Du hast keine Berechtigung die Rolle von §cTeam-Buildern§4 zu ändern!");
                    return;
                }
            }

            if (p.getUniqueId().equals(uuid)) {
                BuildSystem.getInstance().getMessenger().send(p, "§4Du kannst nicht deine eigene Rolle ändern!");
                return;
            }

            BuildSystem.getInstance().getWorldManager().setWorldRole(uuid, name, w, WorldRole.GUEST);
            p.playSound(p.getLocation(), Sound.NOTE_STICKS, 1, 1);
            BuildSystem.getInstance().getMessenger().send(p, "§2Der Spieler§a "+name+"§2 hat nun die§7 Gast§2 Rolle auf der Welt "+w.getName());
            WorldToolsPlayersInventory.toSetWorlds.remove(p);
            new WorldToolsChooseRoleInventory(p, w, uuid, name);
        } catch (PlayerNotResolvedException e) {
            BuildSystem.getInstance().getMessenger().send(p, "§4Der Spieler mit dem Namen§c "+targetName+"§4 konnte nicht gefunden werden: §7§o"+e.getMessage());
        } finally {
            WorldToolsPlayersInventory.toSetWorlds.remove(p);
        }
    }).setItem(AnvilSlot.INPUT_LEFT, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 13).create());

    private static final Map<Player, World> toSetWorlds = new HashMap<>();

    public WorldToolsPlayersInventory(Player player, World world) {
        super(
                "§3§lWelt-Rollen",
                player,
                calculateSize(world),
                InventoryOption.FILL_EMPTY_SLOTS
        );

        int i = 0;
        for (WorldRoleEntry.WorldPlayerEntry worldRole : BuildSystem.getInstance().getWorldManager().getWorldRoles(world)) {
            setItem(
                    i,
                    new Skull(worldRole.getName())
                            .toItemBuilder()
                            .displayName("§f" + worldRole.getName())
                            .lore(
                                    "§7Rolle: §f" + worldRole.getRole().getLabel(),
                                    "",
                                    "§8» §f§nLinksklick§8 | §7§oRolle verändern"
                            )
                            .create(),
                    e -> new WorldToolsChooseRoleInventory(player, world, worldRole.getUuid(), worldRole.getName())
            );
            i++;
        }

        setItem(
                i,
                Skull.fromUrl("http://textures.minecraft.net/texture/a67d813ae7ffe5be951a4f41f2aa619a5e3894e85ea5d4986f84949c63d7672e")
                        .toItemBuilder()
                        .displayName("§eSpieler hinzufügen")
                        .create(),
                e -> {
                    toSetWorlds.put(player, world);
                    ANVIL_INVENTORY.open(player);
                }
        );

        setItem(getInventory().getSize() - 1, BACK_ITEM, e -> new WorldToolsInventory(player));
        openInventory();
    }

    private static int calculateSize(World world) {
        return Math.min(
                (((BuildSystem.getInstance().getWorldManager().getWorldRoles(world).size() - 1) / 9) + 2) * 9,
                InventorySlot.ROW_6
        );
    }

}
