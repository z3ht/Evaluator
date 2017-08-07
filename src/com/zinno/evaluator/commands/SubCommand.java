package com.zinno.evaluator.commands;

import org.bukkit.entity.Player;

import com.zinno.evaluator.Main;

public interface SubCommand {
	
	public void runCommand(Player player, String label, String[] args, Main plugin);
	
}
