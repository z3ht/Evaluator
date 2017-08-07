package com.zinno.evaluator.commands.subCommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.zinno.evaluator.Main;
import com.zinno.evaluator.commands.SubCommand;
import com.zinno.evaluator.util.messager.Messager;

import net.md_5.bungee.api.ChatColor;

public class CycleTeleport implements SubCommand {

	private static HashMap<String, List<Player>> teleportMap = new HashMap<>();

	@Override
	public void runCommand(Player player, String label, String[] args, Main plugin) {
		if (args.length != 1) {
			Messager.onError(player, ChatColor.RED + "No command arguments required, ignoring them.");
		}
		
		List<Player> teleportOrder;
		if (teleportMap.containsKey(player.getName()))
			teleportOrder = updateTeleportMap(teleportMap.get(player.getName()));
		else
			teleportOrder = generateTeleportMap();

		if(teleportOrder.isEmpty()) {
			Messager.onError(player, ChatColor.RED + "No targets found! The cycle could not be created");
			teleportMap.remove(player.getName());
			return;
		}

		player.teleport(teleportOrder.get(0));
		teleportOrder.remove(0);
		player.sendMessage(this.getPrefix() + ChatColor.GRAY + "Teleport Successful");
		if (teleportOrder.isEmpty()) {
			player.sendMessage(this.getPrefix() + ChatColor.GRAY + "The teleport cycle is complete. Refreshing...");
			teleportOrder = generateTeleportMap();
			if(teleportOrder.isEmpty()) {
				Messager.onError(player, ChatColor.RED + "No targets found! The cycle could not be created");
				teleportMap.remove(player.getName());
				return;
			}
		}
		teleportMap.put(player.getName(), teleportOrder);
	}

	private List<Player> updateTeleportMap(List<Player> teleportOrder) {
		do {
			if (teleportOrder.get(0).isOnline()) {
				break;
			}
			teleportOrder.remove(0);
			if (teleportOrder.isEmpty())
				break;
		} while (true);
		return teleportOrder;
	}

	private List<Player> generateTeleportMap() {
		List<Player> order = new ArrayList<Player>();
		for (Player p : Bukkit.getOnlinePlayers())
			if(!p.hasPermission("evaluator.staff"))
				order.add(p);
		Collections.shuffle(order);
		return order;
	}

	private String getPrefix() {
		return ChatColor.GRAY.toString() + ChatColor.BOLD + "[" + ChatColor.LIGHT_PURPLE + "TP"
				+ ChatColor.GRAY.toString() + ChatColor.BOLD + "] " + ChatColor.RESET;
	}
}
