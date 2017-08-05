package com.zinno.evaluator.commands;

import org.bukkit.command.CommandSender;

import com.zinno.evaluator.Main;

public interface SubCommand {
	
	public void runCommand(CommandSender sender, String label, String[] args, Main plugin);
	
}
