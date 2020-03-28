package com.syntaxphoenix.spigot.smoothtimber.utilities.plugin;

import org.bukkit.plugin.Plugin;

import com.syntaxphoenix.syntaxapi.reflections.ReflectCache;

public class PluginPackage {
	
	private ReflectCache cache = new ReflectCache();
	
	private int version;
	private String name;
	private Plugin plugin;
	
	PluginPackage(Plugin plugin) {
		update(plugin);
	}
	
	/*
	 * 
	 */
	
	final void delete() {
		version = -1;
		plugin = null;
		name = null;
		cache.clear();
		cache = null;
	}

	final void update(Plugin plugin) {
		this.plugin = plugin;
		this.name = plugin.getName();
		this.version = Integer.parseInt(plugin.getDescription().getVersion().split("\\.")[0]);
	}
	
	/*
	 * 
	 */

	public ReflectCache getCache() {
		return cache;
	}
	
	public Plugin getPlugin() {
		return plugin;
	}
	
	public int getVersion() {
		return version;
	}
	
	public String getName() {
		return name;
	}
	
	/*
	 * 
	 */
	
	public boolean isFromPlugin(Plugin plugin) {
		return hasName(plugin.getName());
	}

	public boolean hasName(String name) {
		return this.name.equals(name);
	}

}
