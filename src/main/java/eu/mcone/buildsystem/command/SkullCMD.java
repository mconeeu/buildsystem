/*
 * Copyright (c) 2017 - 2018 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.command;

import eu.mcone.buildsystem.BuildSystem;
import eu.mcone.coresystem.api.bukkit.command.CorePlayerCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullCMD extends CorePlayerCommand {

	public SkullCMD() {
		super("skull", "build.skull");
	}

	@Override
	public boolean onPlayerCommand(Player p, String[] args) {
		if (args.length == 1) {
			ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			SkullMeta sm = (SkullMeta) skull.getItemMeta();
			sm.setOwner(args[0]);
			skull.setItemMeta(sm);
			p.getInventory().addItem(skull);

			BuildSystem.getInstance().getMessager().send(p, "§2Du hast den Kopf von §a" + args[0] + " §2erhalten");
		} else {
			BuildSystem.getInstance().getMessager().send(p, "§4Bitte benutze: §c/skull <Spieler>");
		}
		return true;
	}

}
