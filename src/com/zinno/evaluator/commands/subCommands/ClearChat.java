package com.zinno.evaluator.commands.subCommands;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.zinno.evaluator.Main;
import com.zinno.evaluator.commands.SubCommand;
import com.zinno.evaluator.gui.command.CommandGui;
import com.zinno.evaluator.gui.command.CommandType;
import com.zinno.evaluator.util.Translate;
import com.zinno.evaluator.util.config.Config;
import com.zinno.evaluator.util.messager.Messager;

public class ClearChat implements SubCommand {
	
	@Override
	public void runCommand(Player player, String label, String[] args, Main plugin) {
		if(args.length == 1) {
			CommandGui.target(player, CommandType.CLEARCHAT);
			return;
		}else if(args.length <= 1) {
			Messager.onError(player, ChatColor.RED + "Please provide a reason for clearing the chat");
			return;
		}
			
		for(int c = 0; c < 30; c++)
			Bukkit.broadcastMessage("");
		
		StringBuilder reason = new StringBuilder();
		for(int c = 1; c < args.length; c++)
			reason.append(args[c] + " ");
		
		for(String s : plugin.getConfig().getStringList("ClearChat.GlobalTag"))
			Bukkit.broadcastMessage(Translate.all(s, player.getDisplayName(), null, reason.toString()));
		
		Config.log(Pair.of(player.getUniqueId(), player.getName()), null, reason.toString(), CommandType.CLEARCHAT, plugin);
	}

}
