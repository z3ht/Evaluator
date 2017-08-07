package com.zinno.evaluator;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.zinno.evaluator.commands.commands.Report;
import com.zinno.evaluator.commands.hubcommand.Admin;
import com.zinno.evaluator.commands.hubcommand.Helper;
import com.zinno.evaluator.commands.hubcommand.HubCommand;
import com.zinno.evaluator.commands.hubcommand.Mod;
import com.zinno.evaluator.commands.subCommands.Alert;
import com.zinno.evaluator.commands.subCommands.Ban;
import com.zinno.evaluator.commands.subCommands.Chat;
import com.zinno.evaluator.commands.subCommands.ClearChat;
import com.zinno.evaluator.commands.subCommands.CycleTeleport;
import com.zinno.evaluator.commands.subCommands.Evaluate;
import com.zinno.evaluator.commands.subCommands.Freeze;
import com.zinno.evaluator.commands.subCommands.InvSee;
import com.zinno.evaluator.commands.subCommands.Kick;
import com.zinno.evaluator.commands.subCommands.LogConfig;
import com.zinno.evaluator.commands.subCommands.Mute;
import com.zinno.evaluator.commands.subCommands.Note;
import com.zinno.evaluator.commands.subCommands.OpenChat;
import com.zinno.evaluator.commands.subCommands.Reports;
import com.zinno.evaluator.commands.subCommands.RestrictChat;
import com.zinno.evaluator.commands.subCommands.SMute;
import com.zinno.evaluator.commands.subCommands.Spy;
import com.zinno.evaluator.commands.subCommands.Strip;
import com.zinno.evaluator.commands.subCommands.TempBan;
import com.zinno.evaluator.commands.subCommands.Toggle;
import com.zinno.evaluator.commands.subCommands.Track;
import com.zinno.evaluator.commands.subCommands.Unban;
import com.zinno.evaluator.commands.subCommands.Vanish;
import com.zinno.evaluator.commands.subCommands.Warn;
import com.zinno.evaluator.commands.subCommands.Xray;
import com.zinno.evaluator.gui.commandgui.CommandGui;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		registerConfig();
		registerCommands();
		registerSubCommands();
		registerEvents();
	}

	@Override
	public void onDisable() {
		finalize();
	}
	

	private void registerEvents() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new CommandGui(), this);
		pm.registerEvents(new Ban(this), this);
	}
	
	private void registerConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	private void registerCommands() {
		getCommand("admin").setExecutor(new Admin(this));
		getCommand("mod").setExecutor(new Mod(this));
		getCommand("helper").setExecutor(new Helper(this));
		getCommand("report").setExecutor(new Report(this));
	}
	
	private void registerSubCommands() {
		addSubCommands(new Admin());
		addSubCommands(new Mod());
		addSubCommands(new Helper());
	}
	
	@Override
	public void finalize() {
		
	}
	
	private void addSubCommands(HubCommand classType) {
		String configName = classType.getConfigName();
		for(String availableSubCommand : this.getConfig().getStringList(configName + ".AvailableCommands")) {
			switch(availableSubCommand) {
			case "Reports":
				classType.addSubCommand(Arrays.asList("reports", "r", "viewreports", "vr", "activereports", "ar"), new Reports());
				break;
			case "LogConfig":
				classType.addSubCommand(Arrays.asList("logconfig", "log", "lg"), new LogConfig());
				break;
			case "Freeze":
				classType.addSubCommand(Arrays.asList("freeze", "f"), new Freeze());
				break;
			case "Xray":
				classType.addSubCommand(Arrays.asList("xray"), new Xray());
				break;
			case "Evaluate":
				classType.addSubCommand(Arrays.asList("evaluate", "eval", "e"), new Evaluate());
				break;
			case "Note":
				classType.addSubCommand(Arrays.asList("note", "n"), new Note());
				break;
			case "Warn":
				classType.addSubCommand(Arrays.asList("warn", "w"), new Warn());
				break;
			case "Mute":
				classType.addSubCommand(Arrays.asList("mute", "m"), new Mute());
				break;
			case "SMute":
				classType.addSubCommand(Arrays.asList("silentmute", "smute", "sm"), new SMute());
				break;
			case "Kick":
				classType.addSubCommand(Arrays.asList("kick"), new Kick());
				break;
			case "TempBan":
				classType.addSubCommand(Arrays.asList("temporaryban", "tempban", "tban"), new TempBan());
				break;
			case "Ban":
				classType.addSubCommand(Arrays.asList("ban"), new Ban());
				break;
			case "Unban":
				classType.addSubCommand(Arrays.asList("unban", "pardon"), new Unban());
				break;
			case "Strip":
				classType.addSubCommand(Arrays.asList("strip", "s"), new Strip());
				break;
			case "ClearChat":
				classType.addSubCommand(Arrays.asList("clearchat", "cchat", "cc"), new ClearChat());
				break;
			case "RestrictChat":
				classType.addSubCommand(Arrays.asList("restrictChat", "rchat", "rc"), new RestrictChat());
				break;
			case "OpenChat":
				classType.addSubCommand(Arrays.asList("openchat", "ochat", "oc"), new OpenChat());
				break;
			case "Spy":
				classType.addSubCommand(Arrays.asList("spy", "follow", "mount"), new Spy());
				break;
			case "Track":
				classType.addSubCommand(Arrays.asList("track", "highlight"), new Track());
				break;
			case "Vanish":
				classType.addSubCommand(Arrays.asList("vanish", "v"), new Vanish());
				break;
			case "Toggle":
				classType.addSubCommand(Arrays.asList("toggle", "t"), new Toggle());
				break;
			case "InvSee":
				classType.addSubCommand(Arrays.asList("invsee", "echest", "is", "ec"), new InvSee());
				break;
			case "CycleTeleport":
				classType.addSubCommand(Arrays.asList("cycleteleport", "randomteleport", "cycletp", "randomtp", "ctp", "randtp"), new CycleTeleport());
				break;
			case "Chat":
				classType.addSubCommand(Arrays.asList("chat", "achat", "mchat", "hchat"), new Chat());
				break;
			case "Alert":
				classType.addSubCommand(Arrays.asList("alert", "broadcast"), new Alert());
				break;
			}
		}
	}
}
