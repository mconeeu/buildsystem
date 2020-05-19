package eu.mcone.buildsystem.inventorys.worldtools;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.player.BuildPlayer;
import eu.mcone.buildsystem.player.WorldRole;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WorldRanksInventory extends CoreInventory {

    private int i = 0;

    public WorldRanksInventory(Player player) {
        super("§fWorldTools - §oAlle Ränge", player, InventorySlot.ROW_6, InventoryOption.FILL_EMPTY_SLOTS);
        for (World worlds : Bukkit.getWorlds()) {
            if (player.hasPermission("buildsystem.ranks")) {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    BuildPlayer buildPlayers = BuildSystem.getInstance().getBuildPlayer(all);
                    if (buildPlayers.getWorldPermission(worlds).equals(WorldRole.BUILDER) || buildPlayers.getWorldPermission(worlds).equals(WorldRole.OWNER)) {
                        setItem(i, new ItemBuilder(Material.WOOD, 1, 0).displayName("§f" + worlds.getName()).lore(buildPlayers.hasWorldPermissions(worlds), "", "§8» §f§nLinksklick§8 | §7§oDemote", "§8» §f§nRechtsklick§8 | §7§oPromote").create(), e -> {
                                    player.closeInventory();

                                    if (player.hasPermission("permission.ranks.change")) {
                                        player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 1);
                                        if (e.getClick().isRightClick()) {
                                            buildPlayers.promotePermissions(worlds, player);
                                        } else if (e.getClick().isLeftClick()) {
                                            buildPlayers.demotePermissions(worlds, player);
                                        }
                                    } else {
                                        CoreSystem.getInstance().getMessenger().send(player, "§4Du musst dafür §cWelt Admin§4 sein!");
                                        player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM, 1, 1);
                                    }
                                }
                        );
                        i++;
                    }
                }
            } else {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    BuildPlayer buildPlayers = BuildSystem.getInstance().getBuildPlayer(all);
                    BuildPlayer buildPlayer = BuildSystem.getInstance().getBuildPlayer(player);
                    if (buildPlayer.getWorldPermission(worlds).equals(WorldRole.OWNER) || buildPlayer.getWorldPermission(worlds).equals(WorldRole.BUILDER)) {
                        if (buildPlayers.getWorldPermission(worlds).equals(WorldRole.BUILDER) || buildPlayers.getWorldPermission(worlds).equals(WorldRole.OWNER)) {
                            setItem(i, new ItemBuilder(Material.WOOD, 1, 0).displayName("§f" + worlds.getName()).lore(buildPlayers.hasWorldPermissions(worlds), "", "§8» §f§nLinksklick§8 | §7§oDemote", "§8» §f§nRechtsklick§8 | §7§oPromote").create(), e -> {
                                        player.closeInventory();
                                        if (buildPlayer.getWorldPermission(worlds).equals(WorldRole.OWNER)) {
                                            player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 1);
                                            if (e.getClick().isRightClick()) {
                                                buildPlayers.promotePermissions(worlds, player);
                                            } else if (e.getClick().isLeftClick()) {
                                                buildPlayers.demotePermissions(worlds, player);
                                            }
                                        } else {
                                            CoreSystem.getInstance().getMessenger().send(player, "§4Du musst dafür §cWelt Admin§4 sein!");
                                            player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM, 1, 1);
                                        }
                                    }
                            );
                            i++;
                        }
                    }
                }
            }
        }

        setItem(InventorySlot.ROW_6_SLOT_9, new ItemBuilder(Material.IRON_DOOR, 1, 0).displayName("§7Zurück").create(),
                e -> new WorldToolsInventory(player));


        openInventory();
    }
}
