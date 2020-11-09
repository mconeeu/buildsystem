/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.worldtools;

import com.boydti.fawe.FaweAPI;
import com.mongodb.client.MongoCollection;
import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.command.WorldToolsCMD;
import eu.mcone.buildsystem.listener.WorldToolsListener;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import org.bson.UuidRepresentation;
import org.bson.codecs.UuidCodecProvider;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.*;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class WorldToolsManager {

    private static final MongoCollection<WorldRoleEntry> COLLECTION = CoreSystem.getInstance().getMongoDB().withCodecRegistry(
            fromRegistries(getDefaultCodecRegistry(), fromProviders(new UuidCodecProvider(UuidRepresentation.JAVA_LEGACY), PojoCodecProvider.builder().conventions(Conventions.DEFAULT_CONVENTIONS).automatic(true).build()))
    ).getCollection("build_worlds", WorldRoleEntry.class);
    public static final String BUILD_PERMISSION = "build.builder";
    public static final String TEAM_PERMISSIONS = "build.staff";
    private final Map<World, Map<UUID, WorldRoleEntry.WorldPlayerEntry>> worldRoles;

    public WorldToolsManager(BuildSystem system) {
        worldRoles = new HashMap<>();

        FaweAPI.addMaskManager(new MaskManager());
        system.registerCommands(new WorldToolsCMD());
        system.registerEvents(new WorldToolsListener());

        reload();
    }

    public void reload() {
        worldRoles.clear();
        for (WorldRoleEntry worldRoleEntry : COLLECTION.find()) {
            Map<UUID, WorldRoleEntry.WorldPlayerEntry> roles = new HashMap<>();
            for (WorldRoleEntry.WorldPlayerEntry worldRole : worldRoleEntry.getWorldRoles()) {
                roles.put(worldRole.getUuid(), worldRole);
            }

            this.worldRoles.put(Bukkit.getWorld(worldRoleEntry.getWorldName()), roles);
        }
    }

    public Collection<WorldRoleEntry.WorldPlayerEntry> getWorldRoles(World world) {
        return worldRoles.getOrDefault(world, Collections.emptyMap()).values();
    }

    public WorldRole getWorldRole(UUID uuid, World world) {
        WorldRoleEntry.WorldPlayerEntry entry = worldRoles.getOrDefault(world, Collections.emptyMap()).getOrDefault(uuid, null);
        return entry != null ? entry.getRole() : null;
    }

    public void setWorldRole(UUID uuid, String name, World world, WorldRole role) {
        WorldRoleEntry.WorldPlayerEntry entry = new WorldRoleEntry.WorldPlayerEntry(
                uuid, name, role
        );

        if (worldRoles.containsKey(world)) {
            worldRoles.get(world).put(uuid, entry);

            COLLECTION.updateOne(
                    combine(
                            eq("worldName", world.getName()),
                            eq("worldRoles.uuid", uuid)
                    ),
                    set("worldRoles.$.role", role.toString())
            );
        } else {
            WorldRoleEntry worldEntry = new WorldRoleEntry(
                    world.getName(), new ArrayList<>(Collections.singletonList(entry))
            );

            worldRoles.put(world, new HashMap<UUID, WorldRoleEntry.WorldPlayerEntry>(){{put(uuid, entry);}});
            COLLECTION.insertOne(worldEntry);
        }
    }

    public void removeFromWorld(World world, UUID uuid, String name) {
        if (worldRoles.containsKey(world) && worldRoles.get(world).containsKey(uuid)) {
            worldRoles.get(world).remove(uuid);

            COLLECTION.updateOne(
                    eq("worldName", world.getName()),
                    pull("worldRoles", combine(
                            eq("uuid", uuid)
                    ))
            );
        } else {
            throw new IllegalStateException("Could not remove Player "+name+" from world! This player does not have any right on this world!");
        }
    }

}
