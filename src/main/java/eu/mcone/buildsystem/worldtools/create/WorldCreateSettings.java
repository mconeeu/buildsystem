/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.worldtools.create;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class WorldCreateSettings {

    private CustomWorldType type = CustomWorldType.VOID;
    private CustomWorldEnvironment environment = CustomWorldEnvironment.OVERWORLD;

}
