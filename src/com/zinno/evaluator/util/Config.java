package com.zinno.evaluator.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.zinno.evaluator.Main;

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
	
}
