package com.github.pocketkid2.regionteleport;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.pocketkid2.regionteleport.commands.RegionTeleportCommand;
import com.github.pocketkid2.regionteleport.listeners.MoveListener;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class RegionTeleportPlugin extends JavaPlugin {

	private Map<String, Location> locations;
	private Map<String, String> messages;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onEnable() {
		// Load defaults
		saveDefaultConfig();

		// Load values from config
		locations = (Map) getConfig().getConfigurationSection("locations").getValues(false);
		messages = (Map) getConfig().getConfigurationSection("messages").getValues(false);

		// Register command
		getCommand("regionteleport").setExecutor(new RegionTeleportCommand(this));

		// Register listener
		getServer().getPluginManager().registerEvents(new MoveListener(this), this);

		// Log status
		getLogger().info("Done!");
	}

	@Override
	public void onDisable() {
		// Save values to config
		getConfig().createSection("locations", locations);
		getConfig().createSection("messages", messages);
		saveConfig();

		// Log status
		getLogger().info("Done!");
	}

	/*
	 * API method to add this location to the list
	 */
	public void addLocation(String name, Location loc) {
		locations.put(name, loc);
	}

	/*
	 * API method to add this message to the list
	 */
	public void addMessage(String name, String message) {
		messages.put(name, message);
	}

	/*
	 * API method for checking whether this region name has a location
	 * associated with it
	 */
	public boolean regionHasData(String name) {
		return locations.containsKey(name);
	}

	/*
	 * API method for getting the location associated with this region name
	 */
	public Location getLocation(String name) {
		return locations.get(name);
	}

	/*
	 * API method for getting the location associated with this region name
	 */
	public String getMessage(String name) {
		return messages.get(name);
	}

	/*
	 * API method to get the world guard plugin instance
	 */
	public WorldGuardPlugin getWorldGuard() {
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

		// WorldGuard may not be loaded
		if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
			return null; // Maybe you want throw an exception instead
		}

		return (WorldGuardPlugin) plugin;
	}
}
