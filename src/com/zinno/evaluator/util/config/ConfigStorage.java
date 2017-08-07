package com.zinno.evaluator.util.config;

import java.util.ArrayList;
import java.util.List;

public class ConfigStorage {
	private ConfigStorage() {}
	
	private static List<String> viewers = new ArrayList<String>();
	
	public static void addViewer(String name) {
		viewers.add(name);
	}

	public static List<String> getViewers() {
		return viewers;
	}

}
