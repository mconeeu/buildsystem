/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.worldtools;

import eu.mcone.buildsystem.BuildSystem;
import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter @Setter
public class WorldConfig {

    private final String worldId, category;
    private final WorldState state;
    private final Map<String, WorldRole> worldRoles;
    private final boolean preventDynamicLoading;

    @BsonCreator
    public WorldConfig(
            @BsonProperty("worldId") String worldId,
            @BsonProperty("category") String category,
            @BsonProperty("state") WorldState state,
            @BsonProperty("worldRoles") Map<String, WorldRole> worldRoles,
            @BsonProperty("preventDynamicLoading") boolean preventDynamicLoading
    ) {
        this.worldId = worldId;
        this.category = category;
        this.state = state;
        this.worldRoles = new HashMap<>();
        for (Map.Entry<String, WorldRole> entry : worldRoles.entrySet()) {
            this.worldRoles.put(entry.getKey(), entry.getValue());
        }
        this.preventDynamicLoading = preventDynamicLoading;
    }

    public WorldRole calculateWorldRole(UUID uuid) {
        return worldRoles.getOrDefault(uuid.toString(), null);
    }

    public WorldRole getWorldRole(UUID uuid) {
        return worldRoles.getOrDefault(uuid.toString(), null);
    }

    public WorldCategory calculateWorldCategory() {
        return category != null ? BuildSystem.getInstance().getWorldManager().getWorldCategory(category) : null;
    }

    void updateWorldRole(UUID uuid, WorldRole role) {
        worldRoles.put(uuid.toString(), role);
    }

    void removeFromWorld(UUID uuid) {
        worldRoles.remove(uuid.toString());
    }

}
