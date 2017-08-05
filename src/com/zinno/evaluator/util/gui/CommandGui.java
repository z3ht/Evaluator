package com.zinno.evaluator.util.gui;

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
			if (GuiStorage.containsStaffTarget(player.getName()))
				Messager.onGeneral(player,
						ChatColor.AQUA + "Please enter the " + ChatColor.BOLD + "reason " + ChatColor.RESET
								+ ChatColor.AQUA + "for punishing " + ChatColor.BOLD
								+ GuiStorage.getStaffTargets().get(player.getName()));
			else
				Messager.onGeneral(player, ChatColor.AQUA + "Please enter the required arguments for " + ChatColor.BOLD
						+ type.toString().toLowerCase());
		} else
			Messager.onGeneral(player, ChatColor.AQUA + "Please enter the " + ChatColor.BOLD + "name " + ChatColor.RESET
					+ ChatColor.AQUA + "of the player you would like to " + type.toString().toLowerCase());
		Messager.onTip(player, ChatColor.GRAY + "Type " + ChatColor.BOLD + "'c'" + ChatColor.RESET + ChatColor.GRAY
				+ " to cancel the command");
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
		player.sendMessage(message);
		if (message == null || message.equals("") || message.equalsIgnoreCase("c")) {
			Messager.onSuccess(player, ChatColor.GREEN.toString() + ChatColor.ITALIC + type.toString());
			Messager.onSuccess(player, ChatColor.GREEN + "You have successfully cancelled the command execution");
			GuiStorage.delActiveUser(player.getName());
			return;
		}
		if (type.requiresName() && !(GuiStorage.containsStaffTarget(player.getName()))) {
			if (message.contains(" ")) {
				player.sendMessage("uh oh");
				dispatchError(player, type);
				return;
			}
			for (Player target : Bukkit.getOnlinePlayers()) {
				if (target.getName().equalsIgnoreCase(message)) {
					player.sendMessage("yo");
					if (!(type.requiresReason())) {
						if (player.hasPermission("evaluator.admin")) {
							player.performCommand("admin " + type.toString() + " " + target.getName());
							GuiStorage.delActiveUser(player.getName());
							Messager.onSuccess(player, ChatColor.GREEN + " --  THE COMMAND HAS BEEN SENT  -- ");
							return;
						} else if (player.hasPermission("evaluator.mod")) {
							player.performCommand("mod " + type.toString() + " " + target.getName());
							GuiStorage.delActiveUser(player.getName());
							Messager.onSuccess(player, ChatColor.GREEN + " --  THE COMMAND HAS BEEN SENT  -- ");
							return;
						} else if (player.hasPermission("evaluator.helper")) {
							player.performCommand("helper " + type.toString() + " " + target.getName());
							GuiStorage.delActiveUser(player.getName());
							Messager.onSuccess(player, ChatColor.GREEN + " --  THE COMMAND HAS BEEN SENT  -- ");
							return;
						}
					}
					GuiStorage.addStaffTarget(player.getName(), target.getName());
					target(player, type);
					return;
				}
			}
			dispatchError(player, type);
			return;
		}
		if (type.requiresName()) {
			if (player.hasPermission("evaluator.admin"))
				player.performCommand("admin " + type.toString() + " "
						+ GuiStorage.getStaffTargets().get(player.getName()) + " " + message);
			else if (player.hasPermission("evaluator.mod"))
				player.performCommand("mod " + type.toString() + " "
						+ GuiStorage.getStaffTargets().get(player.getName()) + " " + message);
			else if (player.hasPermission("evaluator.helper"))
				player.performCommand("helper " + type.toString() + " "
						+ GuiStorage.getStaffTargets().get(player.getName()) + " " + message);
			GuiStorage.delStaffTarget(player.getName());
			GuiStorage.delActiveUser(player.getName());
			Messager.onSuccess(player, ChatColor.GREEN + " --  THE COMMAND HAS BEEN SENT  -- ");
			return;
		}
		if (player.hasPermission("evaluator.admin"))
			player.performCommand("admin " + type.toString() + " " + message);
		else if (player.hasPermission("evaluator.mod"))
			player.performCommand("mod " + type.toString() + " " + message);
		else if (player.hasPermission("evaluator.helper"))
			player.performCommand("helper " + type.toString() + " " + message);
		GuiStorage.delActiveUser(player.getName());
		Messager.onSuccess(player, ChatColor.GREEN + " --  THE COMMAND HAS BEEN SENT  -- ");
		return;
	}

	private void dispatchError(Player player, CommandType type) {
		player.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "-----");
		Messager.onError(player, ChatColor.RED.toString() + ChatColor.ITALIC + type.toString());
		if (GuiStorage.getStaffTargets().get(player) == null)
			Messager.onError(player, ChatColor.RED + "Please enter the name of the player you would like to punish");
		else
			Messager.onError(player,
					ChatColor.GREEN + "Please enter the arguments required for " + ChatColor.BOLD + type);
		Messager.onError(player, ChatColor.GOLD + "Try Again: (Or type " + ChatColor.BOLD + "c" + ChatColor.RESET
				+ ChatColor.GOLD + " to cancel");
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
