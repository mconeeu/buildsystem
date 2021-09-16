/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.command;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.buildsystem.inventorys.HeadsInventory;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.command.CorePlayerCommand;
import eu.mcone.coresystem.api.bukkit.item.Skull;
import org.bukkit.entity.Player;

public class HeadCMD extends CorePlayerCommand {

	public HeadCMD() {
		super("head", "build.skull", "heads", "skull", "skulls");
	}

	@Override
	public boolean onPlayerCommand(Player p, String[] args) {
		if (args.length == 0) {
			new HeadsInventory(p);
		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("reload")) {
				BuildSystem.getInstance().getMessenger().sendSuccess(p, "Reloading Head Database...");
				BuildSystem.getInstance().getHeadDatabase().reload();
			} else {
				p.getInventory().addItem(new Skull(args[0]).getItemStack());
				CoreSystem.getInstance().getMessenger().send(p, "§2Du hast den Kopf von §a" + args[0] + " §2erhalten");
			}
		} else {
			CoreSystem.getInstance().getMessenger().send(p, "§4Bitte benutze: §c/skull <Spieler>");
		}
		return true;
	}

}
