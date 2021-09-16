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
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import lombok.Getter;
import org.bson.UuidRepresentation;
import org.bson.codecs.UuidCodecProvider;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.*;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class WorldToolsManager {

    private static final MongoCollection<WorldConfig> WORLDS_COLLECTION = CoreSystem.getInstance().getMongoDB().withCodecRegistry(
            fromRegistries(getDefaultCodecRegistry(), fromProviders(new UuidCodecProvider(UuidRepresentation.JAVA_LEGACY), PojoCodecProvider.builder().conventions(Conventions.DEFAULT_CONVENTIONS).automatic(true).build()))
    ).getCollection("build_worlds", WorldConfig.class);
    private static final MongoCollection<WorldCategory> WORLD_CATEGORIES_COLLECTION = CoreSystem.getInstance().getMongoDB().withCodecRegistry(
            fromRegistries(getDefaultCodecRegistry(), fromProviders(new UuidCodecProvider(UuidRepresentation.JAVA_LEGACY), PojoCodecProvider.builder().conventions(Conventions.DEFAULT_CONVENTIONS).automatic(true).build()))
    ).getCollection("build_world_categories", WorldCategory.class);

    public static final String BUILD_PERMISSION = "build.builder", TEAM_PERMISSIONS = "build.staff";
    public static final WorldCategory MISCELLANEOUS_CATEGORY = new WorldCategory("Verschiedenes", Material.DEAD_BUSH);

    @Getter
    private final Set<WorldConfig> worldConfigs;
    @Getter
    private final Set<WorldCategory> worldCategories;

    public WorldToolsManager(BuildSystem system) {
        worldConfigs = new HashSet<>();
        worldCategories = new HashSet<>();

        FaweAPI.addMaskManager(new MaskManager());
        system.registerCommands(new WorldToolsCMD());
        system.registerEvents(new WorldToolsListener(this));

        reload();
    }

    public void reload() {
        worldCategories.clear();
        worldConfigs.clear();

        worldCategories.add(MISCELLANEOUS_CATEGORY);
        for (Gamemode gamemode : Gamemode.values()) {
            worldCategories.add(
                    new WorldCategory(gamemode.getName(), "§7§oSpielmodus "+gamemode.getName(), gamemode.getItem())
            );
        }
        for (WorldCategory category : WORLD_CATEGORIES_COLLECTION.find()) {
            worldCategories.add(category);
        }

        Set<CoreWorld> worlds = new HashSet<>(CoreSystem.getInstance().getWorldManager().getWorlds());
        worlds.remove(BuildSystem.getInstance().getPlotWorld());
        for (WorldConfig config : WORLDS_COLLECTION.find()) {
            worldConfigs.add(config);

            CoreWorld world = CoreSystem.getInstance().getWorldManager().getWorldById(config.getWorldId());
            if (world != null) {
                worlds.remove(world);
            }
        }

        if (worlds.size() > 0) {
            List<WorldConfig> configs = new ArrayList<>();
            for (CoreWorld world : worlds) {
                configs.add(constructConfig(world));
            }

            WORLDS_COLLECTION.insertMany(configs);
        }
    }

    public WorldConfig getWorldConfig(World world) {
        return getWorldConfig(CoreSystem.getInstance().getWorldManager().getWorld(world));
    }

    public WorldConfig getWorldConfig(CoreWorld world) {
        return getWorldConfig(world.getId());
    }

    public WorldConfig getWorldConfig(String id) {
        for (WorldConfig config : worldConfigs) {
            if (config.getWorldId().equals(id)) {
                return config;
            }
        }

        return null;
    }

    public void setWorldRole(World world, UUID uuid, WorldRole role) {
        setWorldRole(
                CoreSystem.getInstance().getWorldManager().getWorld(world),
                uuid,
                role
        );
    }

    public void setWorldRole(CoreWorld world, UUID uuid, WorldRole role) {
        setWorldRole(world.getId(), uuid, role);
    }

    public void setWorldRole(String worldId, UUID uuid, WorldRole role) {
        WorldConfig config = getWorldConfig(worldId);

        if (config != null) {
            config.updateWorldRole(uuid, role);
            WORLDS_COLLECTION.replaceOne(
                    eq("worldId", config.getWorldId()),
                    config
            );
        } else throw new IllegalArgumentException("Could not set WorldRole for "+uuid+" to "+role+". WorldConfig for worldId "+worldId+" is not loaded!");
    }

    public void removeFromWorld(World world, UUID uuid) {
        removeFromWorld(CoreSystem.getInstance().getWorldManager().getWorld(world), uuid);
    }

    public void removeFromWorld(CoreWorld world, UUID uuid) {
        removeFromWorld(world.getId(), uuid);
    }

    public void removeFromWorld(String worldId, UUID uuid) {
        WorldConfig config = getWorldConfig(worldId);

        if (config != null) {
            config.removeFromWorld(uuid);
            WORLDS_COLLECTION.replaceOne(
                    eq("worldId", config.getWorldId()),
                    config
            );
        } else throw new IllegalArgumentException("Could not remove "+uuid+" from World. WorldConfig for worldId "+worldId+" is not loaded!");
    }

    public WorldCategory getWorldCategory(String id) {
        for (WorldCategory cat : worldCategories) {
            if (cat.getId().toString().equals(id)) {
                return cat;
            }
        }

        return null;
    }

    public WorldCategory getWorldCategory(Material material) {
        for (WorldCategory cat : worldCategories) {
            if (cat.getItem().equals(material)) {
                return cat;
            }
        }

        return null;
    }

    public void addWorldCategory(WorldCategory category) {
        if (getWorldCategory(category.getName()) == null && getWorldCategory(category.getItem()) == null) {
            worldCategories.add(category);
            WORLD_CATEGORIES_COLLECTION.insertOne(category);
        } else throw new IllegalArgumentException("Could not add WorldCatgory "+category.getName()+". Name or Material already exists!");
    }

    public boolean removeWorldCategory(WorldCategory category) {
        if (worldCategories.contains(category)) {
            worldCategories.remove(category);
            WORLD_CATEGORIES_COLLECTION.deleteOne(eq("name", category.getName()));

            return true;
        } else return false;
    }

    public void registerNewWorld(CoreWorld world) {
        WorldConfig config = constructConfig(world);

        worldConfigs.add(config);
        WORLDS_COLLECTION.insertOne(config);
    }

    public Set<CoreWorld> getDynamicWorldLoadBlacklist() {
        Set<CoreWorld> blacklist = new HashSet<>(Collections.singleton(BuildSystem.getInstance().getPlotWorld()));

        for (WorldConfig config : worldConfigs) {
            if (config.isPreventDynamicLoading()) {
                CoreWorld world = CoreSystem.getInstance().getWorldManager().getWorldById(config.getWorldId());

                if (world != null) {
                    blacklist.add(world);
                }
            }
        }

        return blacklist;
    }

    private WorldConfig constructConfig(CoreWorld world) {
        return new WorldConfig(world.getId(), null, WorldState.IN_WORK, new HashMap<>(), false);
    }

}
