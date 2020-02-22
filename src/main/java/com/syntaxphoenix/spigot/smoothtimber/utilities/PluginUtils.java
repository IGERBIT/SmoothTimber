package com.syntaxphoenix.spigot.smoothtimber.utilities;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.syntaxphoenix.spigot.smoothtimber.SmoothTimber;
import com.syntaxphoenix.spigot.smoothtimber.config.CutterConfig;
import com.syntaxphoenix.spigot.smoothtimber.listener.BlockBreakListener;
import com.syntaxphoenix.spigot.smoothtimber.stats.SyntaxPhoenixStats;
import com.syntaxphoenix.spigot.smoothtimber.version.manager.VersionChanger;
import com.syntaxphoenix.spigot.smoothtimber.version.manager.VersionExchanger;

import net.md_5.bungee.api.ChatColor;

public class PluginUtils {

	public static PluginUtils utils;
	public static SmoothTimber m;
	public static VersionChanger changer;
	public static SyntaxPhoenixStats stats;

	public static void setUp(SmoothTimber main) {
		m = main;
		utils = new PluginUtils();
	}

	public PluginUtils() {
		changer = VersionExchanger.getVersionChanger(VersionExchanger.getMinecraftVersion());
		if (changer != null) {
			CutterConfig.load();
			registerListener();
			stats = new SyntaxPhoenixStats("7vTfe4hf", SmoothTimber.m);
		}
	}

	/*
	 * 
	 */

	private void checkPlugins(PluginManager pm) {
		Plugin blocky;
		if((blocky = pm.getPlugin("BlockyLog")) != null) {
			if(CutterConfig.extension_blocky) {
				Locator.blockylog = true;
				Locator.version = Integer.parseInt(blocky.getDescription().getVersion().split("\\.")[0]);
				Locator.generateReflect();
			}
		}
	}

	private void registerListener() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new BlockBreakListener(), SmoothTimber.m);
		
		checkPlugins(pm);
	}

	/*
	 * Console Util
	 */

	public static void sendConsoleMessage(String message) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

}
