package com.zinno.evaluator.commands.subCommands;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.zinno.evaluator.Main;
import com.zinno.evaluator.commands.SubCommand;
import com.zinno.evaluator.gui.command.CommandGui;
import com.zinno.evaluator.gui.command.CommandType;
import com.zinno.evaluator.util.Translate;
import com.zinno.evaluator.util.config.Config;

public class Alert implements SubCommand {
	@Override
	public void runCommand(Player player, String label, String[] args, Main plugin) {
		if (args.length == 1) {
			CommandGui.target(player, CommandType.ALERT);
			return;
		}
		
		StringBuilder reason = new StringBuilder();
		for(int c = 1; c < args.length; c++)
			reason.append(args[c] + " ");
		
		switch (label.toLowerCase()) {
		case "helper":
			for (String m : plugin.getConfig().getStringList("Alert.HelperTag"))
				Bukkit.broadcastMessage(Translate.Color(Translate.Reason(m, reason.toString())));
			break;
		case "mod":
			for (String m : plugin.getConfig().getStringList("Alert.ModTag"))
				Bukkit.broadcastMessage(Translate.Color(Translate.Reason(m, reason.toString())));
			break;
		case "admin":
			for (String m : plugin.getConfig().getStringList("Alert.AdminTag"))
				Bukkit.broadcastMessage(Translate.Color(Translate.Reason(m, reason.toString())));
			break;
		}
		Config.log(Pair.of(player.getUniqueId(), player.getName()), null, reason.toString(), CommandType.ALERT, plugin);
	}
}
