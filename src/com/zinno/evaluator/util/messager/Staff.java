package com.zinno.evaluator.util.messager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Staff {
	
	private static List<UUID> ignoreList = new ArrayList<UUID>();
	
	public static List<UUID> getIgnoreList() {
		return ignoreList;
	}
	
	public static void addIgnoreList(UUID staffID) {
		ignoreList.add(staffID);
	}

}
