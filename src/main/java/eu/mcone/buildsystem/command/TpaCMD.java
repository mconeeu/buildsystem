package eu.mcone.buildsystem.command;

import eu.mcone.buildsystem.Main;
import javafx.scene.chart.BubbleChart;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class TpaCMD implements CommandExecutor {

    public static Map<String, List<String>> players = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (args.length == 1) {
                Player t = Bukkit.getPlayer(args[0]);
                if (t != null) {
                    if (players.containsKey(p.getName()) && players.get(p.getName()).contains(t.getName())) {
                        p.sendMessage(Main.config.getConfigValue("System-Prefix") + "§4Du hast diesem Spieler bereits eine Teleportanfrage geschickt!");
                    } else if (t == p) {
                        p.sendMessage(Main.config.getConfigValue("System-Prefix") + "§4Du kannst dir nicht selbst eine Teleportanfrage schicken!");
                    } else {
                        List<String> requests = players.getOrDefault(p.getName(), new ArrayList<>());
                        requests.add(t.getName());
                        players.put(p.getName(), requests);

                        t.sendMessage(Main.config.getConfigValue("System-Prefix") + "§7Du hast eine Teleportanfrage von §f"+p.getName()+"§7 erhalten!");
                        TextComponent tc0 = new TextComponent(TextComponent.fromLegacyText(Main.config.getConfigValue("System-Prefix")));

                        TextComponent tc = new TextComponent();
                        tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept "+p.getName()));
                        tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY+p.getName()+" zu mir teleportieren").create()));
                        tc.setText(ChatColor.DARK_GREEN+"§a[Annehmen]");

                        TextComponent tc1 = new TextComponent();
                        tc1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny "+p.getName()));
                        tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY+p.getName()+" ablehnen").create()));
                        tc1.setText(ChatColor.DARK_GREEN+" §c[Ablehnen]");

                        tc.addExtra(tc1);
                        tc0.addExtra(tc);
                        t.spigot().sendMessage(tc0);
                        p.sendMessage(Main.config.getConfigValue("System-Prefix") + "§2Du hast §a"+args[0]+"§2 eine Teleportanfrage geschickt!");
                    }
                } else {
                    p.sendMessage(Main.config.getConfigValue("System-Prefix") + "§4Dieser Spieler ist nicht online!");
                }
            } else {
                p.sendMessage(Main.config.getConfigValue("System-Prefix") + "§4Bitte benutze: §c/tpa <Spieler>");
            }
        }
        return true;
    }
}
