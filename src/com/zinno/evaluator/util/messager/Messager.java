package com.zinno.evaluator.util.messager;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Messager {

	public static void toStaff(String message, Priority level) {
		String prefix = "";
		switch (level) {
		case ULTRA:
			prefix = ChatColor.DARK_RED.toString() + ChatColor.BOLD + "[ULTRA] " + ChatColor.RESET;
			break;
		case HIGH:
			prefix = ChatColor.DARK_RED.toString() + ChatColor.BOLD + "[" + ChatColor.RED + "HIGH"
					+ ChatColor.DARK_RED.toString() + ChatColor.BOLD + "] " + ChatColor.RESET;
			break;
		case MEDIUM:
			prefix = ChatColor.DARK_PURPLE + "[" + ChatColor.LIGHT_PURPLE + "MEDIUM" + ChatColor.DARK_PURPLE + "] "
					+ ChatColor.RESET;
			break;
		case LOW:
			prefix = ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "LOW" + ChatColor.DARK_GREEN + "] "
					+ ChatColor.RESET;
			break;
		}
		for (Player staff : Bukkit.getOnlinePlayers()) {
			if (staff.hasPermission("evaluator.staff") && !(Staff.getIgnoreList().contains(staff.getUniqueId()))) {
				staff.sendMessage(prefix + message);
			}
		}
	}

	public static void onError(CommandSender sender, String info) {
		String error = ChatColor.DARK_RED.toString() + ChatColor.BOLD + "[" + ChatColor.RESET + ChatColor.ITALIC
				+ ChatColor.RED + "ERROR" + ChatColor.RESET + ChatColor.DARK_RED.toString() + ChatColor.BOLD + "] "
				+ ChatColor.RESET;
		sender.sendMessage(error + info);
	}

	public static void onPerms(CommandSender sender) {
		String perms = ChatColor.DARK_RED.toString() + ChatColor.BOLD + "[" + ChatColor.RESET + ChatColor.ITALIC
				+ ChatColor.RED + "PERMS" + ChatColor.RESET + ChatColor.DARK_RED.toString() + ChatColor.BOLD + "] "
				+ ChatColor.RESET;
		sender.sendMessage(perms + ChatColor.RED + "Denied Permission");
	}

	public static void onSuccess(CommandSender sender, String info) {
		String success = ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "[" + ChatColor.RESET + ChatColor.ITALIC
				+ ChatColor.GREEN + "SUCCESS" + ChatColor.RESET + ChatColor.DARK_GREEN.toString() + ChatColor.BOLD
				+ "] " + ChatColor.RESET;
		sender.sendMessage(success + info);
	}

	public static void onGeneral(CommandSender sender, String info) {
		String general = ChatColor.BLUE.toString() + ChatColor.BOLD + "[" + ChatColor.RESET + ChatColor.ITALIC
				+ ChatColor.DARK_AQUA + "EVALUATOR" + ChatColor.RESET + ChatColor.BLUE.toString() + ChatColor.BOLD
				+ "] " + ChatColor.RESET;
		sender.sendMessage(general + info);
	}

	public static void onTip(CommandSender sender, String info) {
		String tip = ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "[" + ChatColor.RESET + ChatColor.ITALIC
				+ ChatColor.LIGHT_PURPLE + "TIP" + ChatColor.RESET + ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD
				+ "] " + ChatColor.RESET;
		sender.sendMessage(tip + info);
	}

}
