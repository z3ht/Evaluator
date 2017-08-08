package com.zinno.evaluator.commands.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zinno.evaluator.Main;
import com.zinno.evaluator.gui.command.CommandGui;
import com.zinno.evaluator.gui.command.CommandType;
import com.zinno.evaluator.util.config.Config;
import com.zinno.evaluator.util.messager.Messager;
import com.zinno.evaluator.util.messager.Priority;

import net.md_5.bungee.api.ChatColor;

public class Report implements CommandExecutor {

	private Main plugin;
	private static List<String> activeReports = new ArrayList<String>();

	public Report(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Reports can not be logged properly if they are not sent from players");
			return true;
		}
		Player player = (Player) sender;
		if (!(player.hasPermission("evaluator.report"))) {
			Messager.onPerms(player);
			return true;
		} else if (player.hasPermission("evaluator.staff")) {
			Messager.onError(player, ChatColor.RED + "Staff members can not send reports.");
			Messager.onError(player,
					ChatColor.RED + "Use the " + ChatColor.BOLD + "NOTE" + ChatColor.RESET + ChatColor.RED + " or "
							+ ChatColor.BOLD + "WARN" + ChatColor.RED.toString() + ChatColor.RED + " command instead");
			return true;
		} else if (args == null || args.length == 0) {
			CommandGui.target(player, CommandType.REPORT);
			return true;
		}

		for (Player target : Bukkit.getOnlinePlayers()) {
			if (target.getName().equalsIgnoreCase(args[0]) || target.getDisplayName().equalsIgnoreCase(args[0])) {
				StringBuilder strBuilder = new StringBuilder();
				for (int i = 1; i < args.length; i++) {
					strBuilder.append(args[i] + " ");
				}
				String reason = strBuilder.toString();

				Messager.toStaff(ChatColor.LIGHT_PURPLE + "Sender: " + ChatColor.BOLD.toString() + ChatColor.DARK_AQUA
						+ player.getDisplayName() + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "   Target: "
						+ ChatColor.BOLD.toString() + ChatColor.RED + target.getDisplayName() + ChatColor.RESET
						+ ChatColor.LIGHT_PURPLE + "   Message: " + ChatColor.GOLD + reason, Priority.MEDIUM);

				Config.log(Pair.of(player.getUniqueId(), player.getName()),
						Pair.of(target.getUniqueId(), target.getName()), reason, CommandType.REPORT, plugin);
				player.sendMessage(this.getPrefix() + ChatColor.GRAY + "Your report has been sent, thanks!");
				activeReports
						.add("Sender: " + player.getName() + " Target: " + target.getName() + " Message: " + reason);

				return true;
			}
		}

		StringBuilder strBuilder = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			strBuilder.append(args[i] + " ");
		}
		String reason = strBuilder.toString();

		Messager.toStaff(ChatColor.LIGHT_PURPLE + "Sender: " + ChatColor.DARK_AQUA.toString() + ChatColor.BOLD
				+ player.getDisplayName() + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "   Message: " + ChatColor.GOLD
				+ reason, Priority.MEDIUM);

		activeReports.add("Sender: " + player.getName() + " Message: " + reason);
		player.sendMessage(this.getPrefix() + ChatColor.GRAY + "Your report has been sent, thanks!");
		Config.log(Pair.of(player.getUniqueId(), player.getName()), null, reason, CommandType.REPORT, plugin);

		return true;
	}

	private String getPrefix() {
		return ChatColor.WHITE + "[" + ChatColor.AQUA.toString() + ChatColor.BOLD + "R" + ChatColor.WHITE + "] "
				+ ChatColor.RESET;
	}
	
	public static List<String> getActiveReports() {
		return activeReports;
	}
}
