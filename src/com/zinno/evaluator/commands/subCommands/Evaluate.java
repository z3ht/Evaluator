package com.zinno.evaluator.commands.subCommands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.zinno.evaluator.Main;
import com.zinno.evaluator.commands.SubCommand;
import com.zinno.evaluator.gui.command.CommandGui;
import com.zinno.evaluator.gui.command.CommandType;
import com.zinno.evaluator.util.config.Config;
import com.zinno.evaluator.util.messager.Messager;

import net.md_5.bungee.api.ChatColor;

public class Evaluate implements SubCommand, Listener {

	private static List<String> occupied = new ArrayList<String>();

	@Override
	public void runCommand(Player player, String label, String[] args, Main plugin) {

		if (args.length == 1) {
			CommandGui.target(player, CommandType.EVALUATE);
			return;
		}

		occupied.add(player.getName());

		@SuppressWarnings("deprecation")
		UUID targetID = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
		File targetFile = Config.getPlayerFile(targetID, plugin);
		if (!targetFile.exists()) {
			Messager.onError(player, ChatColor.RED + args[1] + "'s file could not be found");
			return;
		}
		FileConfiguration targetConfig = Config.getFileConfig(targetFile);

		for (int c = 0; c < 100; c++)
			player.sendMessage("");

		List<String> evaluator;
		if (player.hasPermission("evaluator.staff"))
			evaluator = plugin.getConfig().getStringList("Evaluate.Staff");
		else
			evaluator = plugin.getConfig().getStringList("Evaluate.Player");

		player.sendMessage(ChatColor.DARK_GRAY + "--<" + ChatColor.GOLD.toString() + ChatColor.BOLD + "EVALUATOR"
				+ ChatColor.DARK_GRAY + ">--");
		for (String item : evaluator) {
			item = item.replace(item.substring(3), CommandType.valueOf(item.substring(3).toUpperCase()).toConfig());
			item = item.replace("[s]", "Stats.");
			item = item.replace("[r]", "Reports.");

			List<String> currentList = targetConfig.getStringList(item);
			if (currentList == null || currentList.isEmpty())
				continue;
			player.sendMessage(ChatColor.GOLD + " " + item.split("[.]")[1] + ":");
			for (String s : currentList)
				player.sendMessage(ChatColor.YELLOW + "  - " + s);
		}
		player.sendMessage("");
		player.sendMessage(this.getPrefix() + ChatColor.GRAY + "Your chat has been muted");
		player.sendMessage(
				this.getPrefix() + ChatColor.GRAY + "Type " + ChatColor.BOLD + "c" + ChatColor.RESET + ChatColor.GRAY
						+ " or " + ChatColor.BOLD + "cancel" + ChatColor.RESET + ChatColor.GRAY + " to unmute");
		Config.log(Pair.of(player.getUniqueId(), player.getName()), Pair.of(targetID, args[1]), null,
				CommandType.EVALUATE, plugin);
	}

	@EventHandler
	public void asyncPlayerChat(AsyncPlayerChatEvent event) {
		if (!(occupied.contains(event.getPlayer().getName()))) {
			event.getRecipients().removeAll(occupied);
			return;
		}
		event.setCancelled(true);
		if (event.getMessage().equalsIgnoreCase("c") || event.getMessage().equalsIgnoreCase("cancel")) {
			event.getPlayer().sendMessage(this.getPrefix() + ChatColor.GREEN + "Un-muting the chat... Welcome back!");
			occupied.remove(event.getPlayer().getName());
			return;
		}
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		occupied.remove(event.getPlayer().getName());
	}

	@EventHandler
	public void onCommandDispatch(PlayerCommandPreprocessEvent event) {
		occupied.remove(event.getPlayer().getName());
	}

	public String getPrefix() {
		return ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "[" + ChatColor.GOLD + "EVALUATE"
				+ ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "] " + ChatColor.RESET;
	}

}
