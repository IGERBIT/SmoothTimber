package com.syntaxphoenix.spigot.smoothtimber.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

import com.syntaxphoenix.spigot.smoothtimber.utilities.PluginUtils;
import com.syntaxphoenix.spigot.smoothtimber.utilities.plugin.PluginSettings;

import org.bukkit.event.server.PluginDisableEvent;

public class PluginLoadListener implements Listener {
	
	private final PluginSettings settings = PluginUtils.SETTINGS;

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onEnable(PluginEnableEvent event) {
		if(!settings.searchPackage(event.getPlugin()).isPresent())
			return;
		PluginUtils.SETTINGS.updatePlugin(event.getPlugin(), true);
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onDisable(PluginDisableEvent event) {
		if(!settings.searchPackage(event.getPlugin()).isPresent())
			return;
		PluginUtils.SETTINGS.updatePlugin(event.getPlugin(), false);
	}

}
