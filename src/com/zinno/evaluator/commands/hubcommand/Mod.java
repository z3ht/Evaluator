package com.zinno.evaluator.commands.hubcommand;

import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zinno.evaluator.Main;
import com.zinno.evaluator.commands.SubCommand;
import com.zinno.evaluator.gui.inventory.ModInventory;
import com.zinno.evaluator.util.Translate;

import net.md_5.bungee.api.ChatColor;

public class Mod implements CommandExecutor, HubCommand {

	private static HashMap<List<String>, SubCommand> subCommand = new HashMap<List<String>, SubCommand>();
	private Main plugin;
	
	// Use this constructor for command execution
	public Mod(Main plugin) {
		this.plugin = plugin;
	}
	
	// Use this constructor for everything else
	public Mod() {}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Translate.Color(plugin.getConfig().getString("Mod.Permission Message")));
		}
		Player player = (Player) sender;
		if(!(player.hasPermission("evaluator.mod"))) {
			player.sendMessage(Translate.Color(plugin.getConfig().getString("Mod.Permission Message")));
			return true;
		}
		if(args == null || args.length == 0) {
			ModInventory modInv = new ModInventory(plugin, player);
			player.openInventory(modInv.getInv());
			return true;
		}
		for(List<String> alias : subCommand.keySet()) {
			if(alias.contains(args[0].toLowerCase())) {
				subCommand.get(alias).runCommand(player, label, args, plugin);
				return true;
			}
		}
		player.sendMessage(ChatColor.RED + "Command not found");
		return true;
	}
	
	@Override
	public void addSubCommand(List<String> alias, SubCommand subCommand) {
		Mod.subCommand.put(alias, subCommand);
	}

	@Override
	public String getConfigName() {
		return "Mod";
	}

}
