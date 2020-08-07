/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.player;

import lombok.Getter;

@Getter
public enum BuildTopic {

    OLD("Altmodisch"),
    MODERN("Modern"),
    SPACE("Weltall"),
    TOWN("Stadt"),
    BIG_VILLA("große Villa"),
    JOONGLE("Jungle"),
    ASIA("Asien");

    private final String name;

    BuildTopic(String name) {
        this.name = name;
    }

}
