package com.zinno.evaluator.commands.subCommands;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.BanList;
import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.zinno.evaluator.Main;
import com.zinno.evaluator.commands.SubCommand;
import com.zinno.evaluator.gui.command.CommandGui;
import com.zinno.evaluator.gui.command.CommandType;
import com.zinno.evaluator.util.Translate;
import com.zinno.evaluator.util.config.Config;
import com.zinno.evaluator.util.messager.Messager;

import net.md_5.bungee.api.ChatColor;

public class Ban implements SubCommand, Listener {

	Main plugin;

	public Ban(Main plugin) { // Use for events
		this.plugin = plugin;
	}

	public Ban() {
	} // Use only for initializing the sub command

	@Override
	public void runCommand(Player player, String label, String[] args, Main plugin) {
		if (args.length == 1)
			CommandGui.target(player, CommandType.BAN);
		else if (args.length == 2) {
			Messager.onError(player, ChatColor.RED + "Please supply a reason for banning " + ChatColor.BOLD + args[1]);
			return;
		}

		boolean isFound = false;
		UUID targetID = null;
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getName().equalsIgnoreCase(args[1])) {
				args[1] = p.getName();
				targetID = p.getUniqueId();
				isFound = true;
				break;
			}
		}
		if (!isFound) {
			Messager.onError(player, ChatColor.RED + "The player " + ChatColor.BOLD + args[1] + ChatColor.RESET
					+ ChatColor.RED + " could not be found");
			return;
		}

		StringBuilder rawReason = new StringBuilder();
		for (int c = 2; c < args.length; c++)
			rawReason.append(" " + args[c]);
		String reason = rawReason.toString().replace(" -g", "");
		reason = reason.replace(" -G", "");
		StringBuilder banMessage = new StringBuilder();
		for (String s : plugin.getConfig().getStringList("Ban.NoJoinMessage"))
			banMessage.append(Translate.all(s, player.getDisplayName(), args[1], reason) + "\n");

		Bukkit.getBanList(BanList.Type.NAME).addBan(targetID.toString(), reason, null, player.getName());
		Bukkit.getPlayer(args[1]).kickPlayer(banMessage.toString());
		Config.log(Pair.of(player.getUniqueId(), player.getName()), Pair.of(targetID, args[1]), reason,
				CommandType.BAN, plugin);
		if(args[args.length-1].equalsIgnoreCase("-g")) {
			for (String s : plugin.getConfig().getStringList("Ban.GlobalTag"))
				Bukkit.broadcastMessage(Translate.all(s, player.getDisplayName(), args[1], reason));
		}
		Messager.onSuccess(player, ChatColor.GOLD.toString() + ChatColor.BOLD + args[1] + ChatColor.RESET
				+ ChatColor.GOLD + " has been banned! For:" + ChatColor.BOLD + reason);
		Messager.onTip(player, ChatColor.GRAY + "Undo by typing /" + label + " unban " + args[1]);
	}

	@EventHandler
	public void onPlayerJoin(PlayerLoginEvent event) {
		UUID targetID = event.getPlayer().getUniqueId();
		if (!Bukkit.getBanList(Type.NAME).isBanned(targetID.toString()))
			return;

		List<String> configNote = Config.getFileConfig(Config.getPlayerFile(targetID, plugin))
				.getStringList("Stats.Ban");
		String[] configNoteArray = configNote.get(configNote.size() - 1).split(" ");

		String sender = configNoteArray[1];
		String target = configNoteArray[3];
		StringBuilder reason = new StringBuilder();
		for (int c = 5; c < configNoteArray.length; c++)
			reason.append(" " + configNoteArray[c]);

		StringBuilder banMessage = new StringBuilder();
		for (String s : plugin.getConfig().getStringList("Ban.NoJoinMessage"))
			banMessage.append(
					Translate.all(s, sender, target, reason.toString()) + "\n");

		event.setKickMessage(banMessage.toString());
		event.setResult(Result.KICK_OTHER);
	}
}
