/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.heads;

import eu.mcone.buildsystem.BuildSystem;

import java.io.*;

public class HeadDatabase {

    /*
     * Powered by minecraft-heads.com:
     * https://minecraft-heads.com/csv/2020-01-31-IUgRbJoHRbVhjKnOlkmH/Custom-Head-DB.csv
     * https://minecraft-heads.4lima.de/csv/2020-01-31-IUgRbJoHRbVhjKnOlkmH/Custom-Head-DB.csv
     */
    private static final InputStream INTERNAL_HEAD_DATABASE = BuildSystem.getInstance().getResource("head_database.csv");
    private final BuildSystem plugin;

    public HeadDatabase(BuildSystem plugin) {
        this.plugin = plugin;

        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        reload();
    }

    public void reload() {
        try {
            Reader headDatabase;
            File serverDb = new File(plugin.getDataFolder(), "head_database.csv");
            if (serverDb.exists()) {
                plugin.sendConsoleMessage("§2Using "+serverDb.getPath()+" as Head DB.");
                headDatabase = new FileReader(serverDb);
            } else {
                plugin.sendConsoleMessage("§2Using internal Head DB.");
                headDatabase = new InputStreamReader(INTERNAL_HEAD_DATABASE);
            }

            for (HeadCategory cat : HeadCategory.values()) {
                cat.getHeads().clear();
            }

            BufferedReader reader = new BufferedReader(headDatabase);
            String entry;
            while ((entry = reader.readLine()) != null && entry.contains(";")) {
                String[] head = entry.split(";");

                if (head.length >= 4) {
                    try {
                        HeadCategory category = HeadCategory.valueOf(head[0].replace("-", "_").toUpperCase());
                        category.addHead(
                                new Head(Integer.parseInt(head[1]), head[2], head[3], head[4])
                        );
                    } catch (IllegalArgumentException e) {
                        plugin.sendConsoleMessage("§cCould not load Head "+head[2]+". Category "+head[0]+" does not exist!");
                    }
                }
            }

            reader.close();
            headDatabase.close();

            int headCount = 0;
            for (HeadCategory cat : HeadCategory.values()) {
                headCount += cat.getHeads().size();
            }

            plugin.sendConsoleMessage("§2Successfully loaded "+headCount+" heads!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
