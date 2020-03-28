package com.syntaxphoenix.spigot.smoothtimber.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

import com.syntaxphoenix.spigot.smoothtimber.utilities.PluginUtils;

import org.bukkit.event.server.PluginDisableEvent;

public class PluginLoadListener implements Listener {

	@EventHandler
	public void onEnable(PluginEnableEvent event) {
		if(!event.getPlugin().getName().equals("BlockyLog"))
			return;
		PluginUtils.SETTINGS.updatePlugin(event.getPlugin(), true);
	}
	
	@EventHandler
	public void onDisable(PluginDisableEvent event) {
		if(!event.getPlugin().getName().equals("BlockyLog"))
			return;
		PluginUtils.SETTINGS.updatePlugin(event.getPlugin(), false);
	}

}
