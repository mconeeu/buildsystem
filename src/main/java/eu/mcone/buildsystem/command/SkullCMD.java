/*
 * Copyright (c) 2017 Dominik L., Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.buildsystem.command;

import eu.mcone.bukkitcoresystem.CoreSystem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullCMD implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p =(Player) sender;
			if (!CoreSystem.cooldown.canExecute(this.getClass(), p)) return true;
			CoreSystem.cooldown.addPlayer(p.getUniqueId(), this.getClass());

			if (p.hasPermission("build.skull")) {
				if (args.length == 1) {
					ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
					SkullMeta sm = (SkullMeta) skull.getItemMeta();
					sm.setOwner(args[0]);
					skull.setItemMeta(sm);
					p.getInventory().addItem(skull);

					p.sendMessage(CoreSystem.config.getConfigValue("Prefix") + "§2Du hast den Kopf von §a" + args[0] + " §2erhalten");
				} else {

					p.sendMessage(CoreSystem.config.getConfigValue("Prefix") + "§4Bitte benutze: §c/skull <Spieler>");
				}

			} else {
				p.sendMessage(CoreSystem.config.getConfigValue("Prefix") + "§4Du haste Berechtigung diesen Befehl!");
			}
		} else {
			Bukkit.getConsoleSender().sendMessage(CoreSystem.config.getConfigValue("Prefix") + "§4Dieser Befehl kann nur von einem Spieler ausgeführt werden!");
			return true;
		}
		return false;
	}
}
