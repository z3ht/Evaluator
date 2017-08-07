package com.zinno.evaluator.commands.subCommands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.zinno.evaluator.Main;
import com.zinno.evaluator.commands.SubCommand;
import com.zinno.evaluator.gui.command.CommandGui;
import com.zinno.evaluator.gui.command.CommandType;
import com.zinno.evaluator.util.messager.Messager;

import net.md_5.bungee.api.ChatColor;

public class Chat implements SubCommand {

	@Override
	public void runCommand(Player player, String label, String[] args, Main plugin) {
		if(args.length == 1) {
			CommandGui.target(player, CommandType.CHAT);
			return;
		}else if (args.length < 2) {
			Messager.onError(player, ChatColor.RED + "Please enter a message for admin chat");
			return;
		}
		
		StringBuilder message = new StringBuilder();
		for (int c = 1; c < args.length; c++)
			message.append(" " + args[c]);

		Bukkit.broadcast(
				ChatColor.GRAY.toString() + ChatColor.BOLD + "[" + ChatColor.RED + "STAFF" + ChatColor.GRAY.toString()
						+ ChatColor.BOLD + "] " + ChatColor.GOLD + player.getDisplayName() + ":" + ChatColor.YELLOW + message.toString(), "evaluator.staff");
		
	}

}
