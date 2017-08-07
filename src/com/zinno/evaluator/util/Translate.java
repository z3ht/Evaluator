package com.zinno.evaluator.util;

import net.md_5.bungee.api.ChatColor;

public class Translate {
	
	private Translate() {}
	
	private final static int day = 86400000;
	private final static int hour = 72000;
	private final static int minute = 1200;
	private final static int second = 20;
	
	public static String Reason(String rawText, String message) {
		if(rawText == null || message == null)
			return "";
		return rawText.replace("{message}", message);
	}
	
	//Translates %player% to target player and %admin% to sender name
	public static String Names(String rawText, String senderName, String targetName) {
		if(rawText == null)
			return "";
		if(targetName != null)
			rawText = rawText.replace("{player}", targetName);
		if(senderName != null)
			rawText = rawText.replace("{admin}", senderName);
		return rawText;
	}
	
	//Translates & to color compatible symbols
	public static String Color(String rawText) {
		if(rawText == null)
			return "";
		String translatedText = ChatColor.translateAlternateColorCodes('&', rawText);
		return translatedText;
	}
	
	// Translates time to milliseconds
	public static int Time(String rawText) {
		int total = 0;
		if(!(rawText.contains("%"))) {
			throw new IllegalArgumentException("No time codes found! Use '%'");
		} else if(rawText.contains(" ")) {
			throw new IllegalArgumentException("Do not use spaces to seperate times! Use '%' instead");
		}
		String[] formattedText = rawText.split("%");
		for(String timeAmount : formattedText) {
			if(timeAmount.isEmpty())continue;
			switch(timeAmount.charAt(0)) {
			case 'd':
				total += Integer.parseInt(timeAmount.substring(1, timeAmount.length())) * day;
				break;
			case 'h':
				total += Integer.parseInt(timeAmount.substring(1, timeAmount.length())) * hour;
				break;
			case 'm':
				total += Integer.parseInt(timeAmount.substring(1, timeAmount.length())) * minute;
				break;
			case 's':
				total += Integer.parseInt(timeAmount.substring(1, timeAmount.length())) * second;
				break;
			}
		}
		if(total == 0) {
			throw new IllegalArgumentException("No Translations Found");
		}
		return total;
	}

	public static String all(String message, String sender, String target, String reason) {
		return Translate.Reason(Translate.Names(Translate.Color(message), sender, target), reason);
	}
}
