package com.zinno.evaluator.gui.command;

import java.util.HashMap;

public class GuiStorage {
	private GuiStorage() {}
	
	private static HashMap<String, CommandType> activeUsers = new HashMap<>();
	private static HashMap<String, String> staffTargets = new HashMap<>();
	
	public static void addActiveUser(String name, CommandType type) {
		activeUsers.put(name, type);
	}
	public static void delActiveUser(String name) {
		activeUsers.remove(name);
	}
	public static boolean containsActiveUser(String name) {
		if(activeUsers.containsKey(name))
			return true;
		return false;
	}
	public static HashMap<String, CommandType> getActiveUsers() {
		return activeUsers;
	}
	
	public static void addStaffTarget(String staffName, String targetName) {
		staffTargets.put(staffName, targetName);
	}
	public static void delStaffTarget(String staffName) {
		staffTargets.remove(staffName);
	}
	public static boolean containsStaffTarget(String name) {
		if(staffTargets.containsKey(name))
			return true;
		return false;
	}
	public static HashMap<String, String> getStaffTargets() {
		return staffTargets;
	}
	
}
