package com.syntaxphoenix.spigot.smoothtimber.utilities;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

import com.syntaxphoenix.spigot.smoothtimber.SmoothTimber;
import com.syntaxphoenix.spigot.smoothtimber.config.*;
import com.syntaxphoenix.spigot.smoothtimber.listener.*;
import com.syntaxphoenix.spigot.smoothtimber.stats.SyntaxPhoenixStats;
import com.syntaxphoenix.spigot.smoothtimber.utilities.plugin.PluginSettings;
import com.syntaxphoenix.spigot.smoothtimber.version.manager.VersionChanger;
import com.syntaxphoenix.spigot.smoothtimber.version.manager.VersionExchanger;

import net.md_5.bungee.api.ChatColor;

public class PluginUtils {

	public static PluginSettings SETTINGS = new PluginSettings();;
	public static PluginUtils UTILS;
	public static SmoothTimber MAIN;
	public static VersionChanger CHANGER;
	public static SyntaxPhoenixStats STATS;

	public static void setUp(SmoothTimber main) {
		MAIN = main;
		UTILS = new PluginUtils();
	}

	public PluginUtils() {
		CHANGER = VersionExchanger.getVersionChanger(VersionExchanger.getMinecraftVersion());
		if (CHANGER != null) {
			CutterConfig.load();
			registerListener();
			registerTasks();
			checkPlugins();
			STATS = new SyntaxPhoenixStats("7vTfe4hf", MAIN);
		}
	}

	/*
	 * 
	 */

	private void checkPlugins() {
		PluginManager pm = Bukkit.getPluginManager();
		for (Plugin plugin : pm.getPlugins()) {
			if (plugin == null)
				continue;
			if (plugin.getName().equals("BlockyLog")) {
				SETTINGS.updatePlugin(plugin, plugin.isEnabled());
			}
		}
	}

	private void registerListener() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new BlockBreakListener(), MAIN);
		pm.registerEvents(new BlockFallListener(), MAIN);
		pm.registerEvents(new PluginLoadListener(), MAIN);
	}

	private void registerTasks() {
		BukkitScheduler scheduler = Bukkit.getScheduler();

		scheduler.runTaskTimerAsynchronously(MAIN, new ConfigTimer(), 20, 60);

	}

	/*
	 * Console Util
	 */

	public static void sendConsoleMessage(String message) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

}
