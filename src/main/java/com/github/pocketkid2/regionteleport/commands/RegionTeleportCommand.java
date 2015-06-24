package com.github.pocketkid2.regionteleport.commands;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.pocketkid2.regionteleport.RegionTeleportPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RegionTeleportCommand implements CommandExecutor {

	private RegionTeleportPlugin plugin;

	public RegionTeleportCommand(RegionTeleportPlugin pl) {
		plugin = pl;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Check for player
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a player!");
			return true;
		}

		// Get player object
		Player player = (Player) sender;

		// Check for a region name
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Not enough arguments!");
			return false;
		}

		// Get the region manager for the world
		Location loc = player.getLocation();
		RegionManager manager = plugin.getWorldGuard().getRegionManager(loc.getWorld());

		// Try and get the region
		ProtectedRegion region = manager.getRegion(args[0]);
		if (region == null) {
			sender.sendMessage(ChatColor.RED + "That region doesn't exist!");
			return false;
		}

		// Get the message if possible
		String message = null;
		if (args.length > 1) {
			message = getMessage(args);
			message = ChatColor.translateAlternateColorCodes('&', message);
		}

		plugin.addLocation(region.getId(), loc);
		if (message != null) {
			plugin.addMessage(region.getId(), message);
		}
		player.sendMessage(String.format(ChatColor.AQUA + "Added your location to region " + ChatColor.GREEN + "%s" + ChatColor.AQUA + " with message " + ChatColor.BLUE + "%s", region.getId(), message));
		return true;
	}

	/*
	 * Removes the first argument and joins the others together
	 */
	private String getMessage(String[] args) {
		String[] strings = Arrays.copyOfRange(args, 1, args.length);
		return String.join(" ", strings);
	}
}
