package eu.mcone.buildsystem.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum BuildTheme {

    OLD("Altmodisch"),
    MODERN("Modern"),
    SPACE("Weltall"),
    TOWN("Stadt"),
    BIG_VILLA("große Villa"),
    JOONGLE("Jungle"),
    ASIA("Asien");


    private final String name;

    BuildTheme(String name) {
        this.name = name;
    }


    }
