package com.zinno.evaluator.commands.commands;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.zinno.evaluator.Main;
import com.zinno.evaluator.util.config.Config;
import com.zinno.evaluator.util.messager.Messager;
import com.zinno.evaluator.util.messager.Priority;

import net.md_5.bungee.api.ChatColor;

public class Report implements CommandExecutor {

	private Main plugin;

	public Report(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Reports can not be logged properly if they are not sent from players");
			return true;
		}
		Player player = (Player) sender;
		if(!(player.hasPermission("evaluator.report"))) {
			Messager.onPerms(player);
			return true;
		}else if(args == null || args.length == 0) {
			Messager.onError(player, ChatColor.RED + "Please Supply a reason for the report");
		}
		File reportFile = new File(plugin.getDataFolder(), "Reports.yml");
		FileConfiguration reportCFG = YamlConfiguration.loadConfiguration(reportFile);
		List<String> reportList = reportCFG.getStringList("Reports");

		for (Player target : Bukkit.getOnlinePlayers()) {
			if (target.getName().equalsIgnoreCase(args[1]) || target.getDisplayName().equalsIgnoreCase(args[1])) {
				StringBuilder strBuilder = new StringBuilder();
				for (int i = 1; i < args.length; i++) {
					strBuilder.append(args[i] + " ");
				}
				String reason = strBuilder.toString();

				Messager.toStaff(ChatColor.LIGHT_PURPLE + "Sender: " + ChatColor.BOLD.toString() + ChatColor.DARK_AQUA
						+ player.getDisplayName() + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "   Target: "
						+ ChatColor.BOLD.toString() + ChatColor.RED + target.getDisplayName() + ChatColor.RESET
						+ ChatColor.LIGHT_PURPLE + "   Message: " + ChatColor.GOLD + reason, Priority.MEDIUM);

				File targetFile = Config.getPlayerFile(player.getUniqueId(), plugin);
				FileConfiguration targetConfig = Config.getFileConfig(targetFile);
				List<String> targetReportList = targetConfig.getStringList("Sent.Reports");
				targetReportList.add("Sender: " + player.getDisplayName() + "   Target: " + target.getDisplayName()
						+ "   Message: " + reason);
				targetConfig.set("Sent.Reports", targetReportList);
				Config.saveFile(targetFile, targetConfig);

				File senderFile = Config.getPlayerFile(player.getUniqueId(), plugin);
				FileConfiguration senderConfig = Config.getFileConfig(senderFile);
				List<String> senderReportList = senderConfig.getStringList("Sent.Reports");
				targetReportList.add("Sender: " + player.getDisplayName() + "   Target: " + target.getDisplayName()
						+ "   Message: " + reason);
				senderConfig.set("Sent.Reports", senderReportList);
				Config.saveFile(senderFile, senderConfig);

				targetReportList.add("Sender: " + player.getDisplayName() + "   Target: " + target.getDisplayName()
						+ "   Message: " + reason);
				reportCFG.set("Reports", reportList);
				Config.saveFile(reportFile, reportCFG);
				return true;
			}
		}

		StringBuilder strBuilder = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			strBuilder.append(args[i] + " ");
		}
		String reason = strBuilder.toString();

		Messager.toStaff(ChatColor.LIGHT_PURPLE + "Sender: " + ChatColor.DARK_AQUA.toString() + ChatColor.BOLD
				+ player.getDisplayName() + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "   Message: " + ChatColor.GOLD
				+ reason, Priority.MEDIUM);

		File playerFile = Config.getPlayerFile(player.getUniqueId(), plugin);
		FileConfiguration playerConfig = Config.getFileConfig(playerFile);
		List<String> playerReportList = playerConfig.getStringList("Sent.Reports");
		playerReportList.add("Sender: " + player.getDisplayName() + "   Message: " + reason);
		playerConfig.set("Sent.Reports", playerReportList);
		Config.saveFile(playerFile, playerConfig);

		reportList.add("Sender: " + player.getDisplayName() + "   Message: " + reason);
		reportCFG.set("Reports", reportList);
		Config.saveFile(reportFile, reportCFG);

		return true;
	}

}
