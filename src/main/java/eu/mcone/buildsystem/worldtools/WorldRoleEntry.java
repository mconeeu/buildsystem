/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.worldtools;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class WorldRoleEntry {

    private String worldName;
    private List<WorldPlayerEntry> worldRoles;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter @Setter
    public static class WorldPlayerEntry {
        private UUID uuid;
        private String name;
        private WorldRole role;
    }

}
