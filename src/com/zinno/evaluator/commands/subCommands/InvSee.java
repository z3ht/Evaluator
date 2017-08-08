package com.zinno.evaluator.commands.subCommands;

import java.util.HashMap;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

import com.zinno.evaluator.Main;
import com.zinno.evaluator.commands.SubCommand;
import com.zinno.evaluator.gui.command.CommandGui;
import com.zinno.evaluator.gui.command.CommandType;
import com.zinno.evaluator.util.config.Config;

import net.md_5.bungee.api.ChatColor;

public class InvSee implements SubCommand, Listener {
	
	private static HashMap<Inventory, Player> invSeeStorage = new HashMap<>();
	
	private Player player;
	private Player target;
	
	@Override
	public void runCommand(Player player, String label, String[] args, Main plugin) {
		if (args.length == 1) {
			CommandGui.target(player, CommandType.INVSEE);
			return;
		}
		this.player = player;
		for (Player target : Bukkit.getOnlinePlayers()) {
			if (target.getName().equalsIgnoreCase(args[1]) || target.getDisplayName().equalsIgnoreCase(args[1])) {
				this.target = target;
				Inventory inv = clonePlayerInv(target.getInventory());
				invSeeStorage.put(inv, target);
				player.openInventory(inv);
				player.sendMessage(this.getPrefix() + ChatColor.GRAY + "You are viewing " + ChatColor.BOLD
						+ target.getName() + ChatColor.RESET + ChatColor.GRAY + "'s inventory");
				Config.log(Pair.of(player.getUniqueId(), player.getName()), Pair.of(target.getUniqueId(), target.getName()), null, CommandType.INVSEE, plugin);
				return;
			}
		}
	}

	private Inventory clonePlayerInv(PlayerInventory rawInv) {
		Inventory newInv = Bukkit.createInventory(player, 36, target.getName() + "'s inventory:");
		final int invSize = 36;
		for (int c = 0; c < invSize; c++) {
			if(c > 8)
				newInv.setItem((c-9)%invSize, rawInv.getItem(c));
			else
				newInv.setItem((c+27)%invSize, rawInv.getItem(c));
		}
		return newInv;
	}
	
	private void setChestInventory(Inventory targetInv, Inventory chestInv) {
		final int invSize = 36;
		for(int c = 0; c < invSize; c++) {
			if(c > 8)
				targetInv.setItem((c+9)%36, chestInv.getItem(c));
			else
				targetInv.setItem(Math.abs((c-27)%36), chestInv.getItem(c));
		}
	}

	private String getPrefix() {
		return ChatColor.WHITE + "[" + ChatColor.RED.toString() + ChatColor.BOLD + "SPY" + ChatColor.WHITE + "] "
				+ ChatColor.RESET;
	}
	
	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent event) {
		if(!(invSeeStorage.containsKey(event.getInventory())))
			return;
		this.setChestInventory(invSeeStorage.get(event.getInventory()).getInventory(), event.getInventory());
		invSeeStorage.remove(event.getInventory());
		event.getPlayer().sendMessage(this.getPrefix() + ChatColor.GRAY + "Very sneaky... See you again soon!");
	}

}