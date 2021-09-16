package eu.mcone.buildsystem.inventorys.worldtools;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.worldtools.WorldRole;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.inventory.anvil.AnvilSlot;
import eu.mcone.coresystem.api.bukkit.inventory.anvil.CoreAnvilInventory;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.item.Skull;
import eu.mcone.coresystem.api.bukkit.player.OfflineCorePlayer;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.coresystem.api.core.exception.PlayerNotResolvedException;
import group.onegaming.networkmanager.core.api.database.Database;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Updates.combine;

public class WorldToolsPlayersInventory extends CoreInventory {

    private static final MongoCollection<Document> USERINFO;

    static {
        MongoCollection<Document> userinfo = null;

        try {
            Method dbGetter = CoreSystem.getInstance().getClass().getDeclaredMethod("getMongoDB", Database.class);
            MongoDatabase mc1system = (MongoDatabase) dbGetter.invoke(CoreSystem.getInstance(), Database.SYSTEM);
            userinfo = mc1system.getCollection("userinfo");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            USERINFO = userinfo;
        }
    }

    private static final CoreAnvilInventory ANVIL_INVENTORY = CoreSystem.getInstance().createAnvilInventory(event -> {
        String targetName = event.getName();

        Player p = event.getPlayer();
        Player t = Bukkit.getPlayer(targetName);

        UUID uuid;
        String name;
        CoreWorld w = WorldToolsPlayersInventory.toSetWorlds.get(p);

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

            BuildSystem.getInstance().getWorldManager().setWorldRole(w, uuid, WorldRole.GUEST);
            p.playSound(p.getLocation(), Sound.NOTE_STICKS, 1, 1);
            BuildSystem.getInstance().getMessenger().send(p, "§2Der Spieler§a " + name + "§2 hat nun die§7 Gast§2 Rolle auf der Welt " + w.getName());
            WorldToolsPlayersInventory.toSetWorlds.remove(p);
            new WorldToolsChooseRoleInventory(p, w, uuid, name);
        } catch (PlayerNotResolvedException e) {
            BuildSystem.getInstance().getMessenger().send(p, "§4Der Spieler mit dem Namen§c " + targetName + "§4 konnte nicht gefunden werden: §7§o" + e.getMessage());
        } finally {
            WorldToolsPlayersInventory.toSetWorlds.remove(p);
        }
    }).setItem(AnvilSlot.INPUT_LEFT, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 13).displayName("?").create());

    private static final Map<Player, CoreWorld> toSetWorlds = new HashMap<>();

    public WorldToolsPlayersInventory(Player player, CoreWorld world) {
        super(
                "§3§lWelt-Rollen",
                player,
                calculateSize(world),
                InventoryOption.FILL_EMPTY_SLOTS
        );

        Map<String, WorldRole> roles = BuildSystem.getInstance().getWorldManager().getWorldConfig(world).getWorldRoles();
        Map<String, String> names = new HashMap<>();

        List<Bson> query = new ArrayList<>();
        roles.keySet().forEach(uuid -> query.add(eq("uuid", uuid)));
        for (Document document : USERINFO.find(combine(query)).projection(include("uuid", "name"))) {
            names.put(document.getString("uuid"), document.getString("name"));
        }

        int i = 0;
        for (Map.Entry<String, WorldRole> role : BuildSystem.getInstance().getWorldManager().getWorldConfig(world).getWorldRoles().entrySet()) {
            String name = names.get(role.getKey());

            setItem(
                    i,
                    new Skull(name)
                            .toItemBuilder()
                            .displayName("§f" + name)
                            .lore(
                                    "§7Rolle: §f" + role.getValue().getLabel(),
                                    "",
                                    "§8» §f§nLinksklick§8 | §7§oRolle verändern"
                            )
                            .create(),
                    e -> new WorldToolsChooseRoleInventory(player, world, UUID.fromString(role.getKey()), name)
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

    private static int calculateSize(CoreWorld world) {
        return Math.min(
                (((BuildSystem.getInstance().getWorldManager().getWorldConfig(world).getWorldRoles().size() - 1) / 9) + 2) * 9,
                InventorySlot.ROW_6
        );
    }

}
