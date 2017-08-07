package com.zinno.evaluator.util.config;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.zinno.evaluator.Main;
import com.zinno.evaluator.gui.commandgui.CommandType;
import com.zinno.evaluator.util.messager.Messager;

import net.md_5.bungee.api.ChatColor;

public class Config {
	
	private Config() {}
	
	public static File getPlayerFile(UUID playerID, Main plugin) {
		String playerUUID = playerID.toString();
		File playerFile =  new File(plugin.getDataFolder() + File.separator + "Player Database", playerUUID + ".yml");
        return playerFile;
	}
	
	public static File getReportFile(Main plugin) {							// TODO: if(not used in class other than reports) then (remove)
		File reportFile = new File(plugin.getDataFolder(), "Reports.yml");
		return reportFile;
	}
	
	public static FileConfiguration getFileConfig(File file) {
		return YamlConfiguration.loadConfiguration(file);
	}
	
	public static void saveFile(File file, FileConfiguration config) {
		try {
			config.save(file);
		} catch (IOException e) {
			Bukkit.broadcast(ChatColor.RED + "File: " + file.getName() + " could not be saved", "evaluator.debug");
			e.printStackTrace();
		}
	}
	
	public static void log(Pair<UUID, String> senderInfo, Pair<UUID, String> targetInfo, String reason, CommandType type, Main plugin) {
		String message = plugin.getConfig().getString(type.toConfig() + ".ConfigNote");
		if(message == null) {
			Bukkit.broadcast(ChatColor.RED + "A problem occurred when logging " + type, "evaluator.debug");
			return;
		}
		if(senderInfo != null)
			message = message.replace("{admin}", senderInfo.getRight());
		if(targetInfo != null)
			message = message.replace("{player}", targetInfo.getRight());
		if(reason != null)
			message = message.replace("{message}", reason);
		
		List<String> configWatchers = ConfigStorage.getViewers();
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(configWatchers.contains(player.getName()))
				Messager.onGeneral(player, message);
		}
		
		if(senderInfo != null) {
			File senderFile = Config.getPlayerFile(senderInfo.getLeft(), plugin);
			FileConfiguration senderConfig = Config.getFileConfig(senderFile);
			
			List<String>senderList = senderConfig.getStringList("Reports." + type.toConfig());
			senderList.add(message);
			senderConfig.set("Reports." + type.toConfig(), senderList);
			Config.saveFile(senderFile, senderConfig);
		}
		
		if(targetInfo != null) {
			File targetFile = Config.getPlayerFile(targetInfo.getLeft(), plugin);
			FileConfiguration targetConfig = Config.getFileConfig(targetFile);
			
			List<String>targetList = targetConfig.getStringList("Stats." + type.toConfig());
			targetList.add(message);
			targetConfig.set("Stats." + type.toConfig(), targetList);
			Config.saveFile(targetFile, targetConfig);
		}
	}
	
}