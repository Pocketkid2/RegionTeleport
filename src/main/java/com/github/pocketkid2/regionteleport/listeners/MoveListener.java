package com.github.pocketkid2.regionteleport.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.github.pocketkid2.regionteleport.RegionTeleportPlugin;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class MoveListener implements Listener {

	private RegionTeleportPlugin plugin;

	public MoveListener(RegionTeleportPlugin pl) {
		plugin = pl;
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if (event.getTo().getBlock() != event.getFrom().getBlock()) {
			RegionContainer container = plugin.getWorldGuard().getRegionContainer();
			RegionQuery query = container.createQuery();
			ApplicableRegionSet set = query.getApplicableRegions(event.getTo());
			for (ProtectedRegion region : set) {
				if (plugin.regionHasData(region.getId())) {
					event.setTo(plugin.getLocation(region.getId()));
					if (plugin.getMessage(region.getId()) != null) {
						event.getPlayer().sendMessage(plugin.getMessage(region.getId()));
					}
				}
			}
		}
	}

}
