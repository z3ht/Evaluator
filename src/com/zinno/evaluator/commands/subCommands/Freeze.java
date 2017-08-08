package com.zinno.evaluator.commands.subCommands;

import java.util.HashMap;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.zinno.evaluator.Main;
import com.zinno.evaluator.commands.SubCommand;
import com.zinno.evaluator.gui.command.CommandGui;
import com.zinno.evaluator.gui.command.CommandType;
import com.zinno.evaluator.util.config.Config;
import com.zinno.evaluator.util.messager.Messager;

import net.md_5.bungee.api.ChatColor;

public class Freeze implements SubCommand, Listener {

	private static HashMap<String, String> freezeList = new HashMap<String, String>();

	@Override
	public void runCommand(Player player, String label, String[] args, Main plugin) {
		if (args.length == 1) {
			CommandGui.target(player, CommandType.FREEZE);
			return;
		}
		for (Player target : Bukkit.getOnlinePlayers()) {
			if (target.getName().equalsIgnoreCase(args[1]) | target.getDisplayName().equalsIgnoreCase(args[1])) {
				if(target.hasPermission("evaluator.staff")) {
					Messager.onError(player, ChatColor.RED + "You can not freeze staff members! That includes " + ChatColor.BOLD + args[1]);
					return;
				}
				if (freezeList.containsKey(target.getName())) {
					freezeList.remove(target.getName());
					player.sendMessage(this.getPrefix() + ChatColor.GRAY.toString() + ChatColor.BOLD + target.getName()
							+ ChatColor.GRAY + " has thawed out");
					target.sendMessage(this.getPrefix() + ChatColor.GRAY.toString() + ChatColor.BOLD + player.getName()
							+ ChatColor.GRAY + " has unfrozen you");
					return;
				}
				freezeList.put(target.getName(), player.getName());
				player.sendMessage(this.getPrefix() + ChatColor.GRAY.toString() + ChatColor.BOLD + target.getName()
						+ ChatColor.GRAY + " is now frozen, brr...");
				target.sendMessage(this.getPrefix() + ChatColor.GRAY.toString() + ChatColor.BOLD + player.getName()
						+ ChatColor.GRAY + " has frozen you");
				
				Config.log(Pair.of(player.getUniqueId(), player.getName()), Pair.of(target.getUniqueId(), target.getName()), null, CommandType.FREEZE, plugin);

				final String prefix = this.getPrefix();
				new BukkitRunnable() {
					public void run() {
						if (!freezeList.containsKey(target.getName()))
							this.cancel();
						player.sendMessage(prefix + ChatColor.GRAY.toString() + ChatColor.BOLD + target.getName()
								+ ChatColor.GRAY + " is still frozen, brr...");
						player.sendMessage(prefix + ChatColor.GRAY + "Type " + ChatColor.ITALIC + "'/" + label + " unfreeze " + target.getName()
								+ "'" + ChatColor.RESET + ChatColor.GRAY + " to begin thawwing " + target.getName());
					}
				}.runTaskTimer(plugin, 200, 200);
				return;
			}
		}
		player.sendMessage(this.getPrefix() + "The specified target, " + args[1] + " could not be found.");
	}

	@EventHandler
	public void onMoveEvent(PlayerMoveEvent event) {
		if (!(freezeList.containsKey(event.getPlayer().getName())))
			return;

		event.setCancelled(true);
		event.getPlayer().sendMessage(this.getPrefix() + ChatColor.GRAY.toString() + ChatColor.BOLD
				+ freezeList.get(event.getPlayer().getName()) + ChatColor.GRAY + " has frozen you");
		event.getPlayer().sendMessage(this.getPrefix() + ChatColor.GRAY + "You can not move");
	}

	@EventHandler
	public void onCommandEvent(PlayerCommandPreprocessEvent event) {
		if (!(freezeList.containsKey(event.getPlayer().getName())))
			return;

		event.setCancelled(true);
		event.getPlayer().sendMessage(this.getPrefix() + ChatColor.GRAY.toString() + ChatColor.BOLD
				+ freezeList.get(event.getPlayer().getName()) + ChatColor.GRAY + " has frozen you");
		event.getPlayer().sendMessage(this.getPrefix() + ChatColor.GRAY + "You can not send commands");
	}
	
	@EventHandler
	public void onPickUpItemEvent(PlayerPickupItemEvent event) {
		if (!(freezeList.containsKey(event.getPlayer().getName())))
			return;
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onDropItemEvent(PlayerDropItemEvent event) {
		if (!(freezeList.containsKey(event.getPlayer().getName())))
			return;
		event.setCancelled(true);
	}
	
	private String getPrefix() {
		return ChatColor.AQUA + "[" + ChatColor.WHITE.toString() + ChatColor.BOLD + "F" + ChatColor.AQUA + "] "
				+ ChatColor.RESET;
	}
}
