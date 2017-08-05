package com.zinno.evaluator.commands.subCommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zinno.evaluator.Main;
import com.zinno.evaluator.commands.SubCommand;
import com.zinno.evaluator.util.gui.CommandGui;
import com.zinno.evaluator.util.gui.CommandType;

public class Alert implements SubCommand {
	@Override
	public void runCommand(CommandSender sender, String label, String[] args, Main plugin) {
		CommandGui.target((Player) sender, CommandType.ALERT);
	}

}
