package com.zinno.evaluator.gui.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.zinno.evaluator.util.messager.Messager;

import net.md_5.bungee.api.ChatColor;

public class CommandGui implements Listener {

	public static void target(Player player, CommandType type) {
		for (int c = 0; c < 50; c++)
			player.sendMessage("");
		Messager.onGeneral(player, ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "  ----- " + ChatColor.GOLD
				+ ChatColor.BOLD + type.toString() + ChatColor.DARK_GRAY + ChatColor.BOLD + " -----  ");
		if (GuiStorage.containsActiveUser(player.getName())) {
			if (type.requiresName())
				Messager.onGeneral(player,
						ChatColor.AQUA + "Please enter the " + ChatColor.BOLD + "reason " + ChatColor.RESET
								+ ChatColor.AQUA + "for punishing " + ChatColor.BOLD
								+ GuiStorage.getStaffTargets().get(player.getName()));
			else
				Messager.onGeneral(player, ChatColor.AQUA + "Please enter the required arguments for " + ChatColor.BOLD
						+ type.toString().toLowerCase());
		} else {
			if (type.requiresName())
				Messager.onGeneral(player,
						ChatColor.AQUA + "Please enter the " + ChatColor.BOLD + "name" + ChatColor.RESET
								+ ChatColor.AQUA + " of the player you would like to " + type.toString().toLowerCase());
			else
				Messager.onGeneral(player, ChatColor.AQUA + "Please enter the required arguments for " + ChatColor.BOLD
						+ type.toString().toLowerCase());
		}

		Messager.onTip(player, ChatColor.GRAY + "Type " + ChatColor.BOLD + "'c'" + ChatColor.RESET + ChatColor.GRAY
				+ " or " + ChatColor.BOLD + "'cancel'" + ChatColor.RESET + ChatColor.GRAY + " to cancel the command");
		GuiStorage.addActiveUser(player.getName(), type);
	}

	@EventHandler
	public void asyncChatEvent(AsyncPlayerChatEvent event) {
		if (!(GuiStorage.containsActiveUser(event.getPlayer().getName()))) {
			for (String mutedStaff : GuiStorage.getActiveUsers().keySet())
				event.getRecipients().remove(Bukkit.getPlayer(mutedStaff));
			return;
		}
		event.setCancelled(true);
		Player player = event.getPlayer();
		CommandType type = GuiStorage.getActiveUsers().get(player.getName());
		String message = event.getMessage();
		for (int c = 0; c < 50; c++)
			player.sendMessage("");
		if (message == null || message.equals("") || message.equalsIgnoreCase("c")
				|| message.equalsIgnoreCase("cancel")) {
			Messager.onSuccess(player, ChatColor.GREEN + "You have successfully " + ChatColor.BOLD + "cancelled"
					+ ChatColor.RESET + ChatColor.GREEN + " the " + type.toString().toLowerCase() + " command");
			GuiStorage.delActiveUser(player.getName());
			return;
		}
		if (type.requiresName() && !(GuiStorage.containsStaffTarget(player.getName()))) {
			if (message.contains(" ")) {
				dispatchPlayerFoundError(player, message, type);
				return;
			}
			for (Player target : Bukkit.getOnlinePlayers()) {
				if (target.getName().equalsIgnoreCase(message)) {
					if (!(type.requiresReason())) {
						runCommand(player, type, target.getName(), null);
						return;
					}
					GuiStorage.addStaffTarget(player.getName(), target.getName());
					target(player, type);
					return;
				}
			}
			dispatchPlayerFoundError(player, message, type);
			return;
		}
		if (type.requiresName()) {
			runCommand(player, type, GuiStorage.getStaffTargets().get(player.getName()), message);
			return;
		}
		runCommand(player, type, null, message);
		return;
	}

	private void runCommand(Player player, CommandType type, String target, String reason) {
		StringBuilder rank = new StringBuilder();
		if (player.hasPermission("evaluator.admin"))
			rank.append("admin");
		else if (player.hasPermission("evaluator.mod"))
			rank.append("mod");
		else if (player.hasPermission("evaluator.helper"))
			rank.append("helper");
		if (target != null && reason != null)
			player.performCommand(rank.toString() + " " + type.toString() + " " + target + " " + reason);
		else if (target != null)
			player.performCommand(rank.toString() + " " + type.toString() + " " + target);
		else if (reason != null)
			player.performCommand(rank.toString() + " " + type.toString() + " " + reason);
		else {
			Messager.onError(player, ChatColor.RED + "An unknown error occurred when executing the command");
			Messager.onError(player, ChatColor.RED + "The command has been cancelled");
		}
		GuiStorage.delStaffTarget(player.getName());
		GuiStorage.delActiveUser(player.getName());
	}

	private void dispatchPlayerFoundError(Player player, String message, CommandType type) {
		Messager.onError(player, ChatColor.RED + "The player " + ChatColor.BOLD + message + ChatColor.RESET
				+ ChatColor.RED + " could not be found.");
		Messager.onError(player, ChatColor.RED + "Please enter the " + ChatColor.BOLD + "name" + ChatColor.RESET
				+ ChatColor.RED + " of the player you would like to " + type.toString().toLowerCase());
		Messager.onTip(player, ChatColor.GRAY + "Type " + ChatColor.BOLD + "'c'" + ChatColor.RESET + ChatColor.GRAY
				+ " or " + ChatColor.BOLD + "'cancel'" + ChatColor.RESET + ChatColor.GRAY + " to cancel the command");
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		GuiStorage.delActiveUser(event.getPlayer().getName());
		GuiStorage.delStaffTarget(event.getPlayer().getName());
	}

	@EventHandler
	public void onCommandDispatch(PlayerCommandPreprocessEvent event) {
		GuiStorage.delActiveUser(event.getPlayer().getName());
		GuiStorage.delStaffTarget(event.getPlayer().getName());
	}

}
