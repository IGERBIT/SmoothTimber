package com.syntaxphoenix.spigot.smoothtimber.utilities;

import static com.syntaxphoenix.spigot.smoothtimber.SmoothTimber.COMMANDS;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

import com.syntaxphoenix.spigot.smoothtimber.SmoothTimber;
import com.syntaxphoenix.spigot.smoothtimber.command.CommandRedirect;
import com.syntaxphoenix.spigot.smoothtimber.command.commands.*;
import com.syntaxphoenix.spigot.smoothtimber.config.*;
import com.syntaxphoenix.spigot.smoothtimber.listener.*;
import com.syntaxphoenix.spigot.smoothtimber.stats.SyntaxPhoenixStats;
import com.syntaxphoenix.spigot.smoothtimber.utilities.plugin.PluginSettings;
import com.syntaxphoenix.spigot.smoothtimber.version.manager.VersionChanger;
import com.syntaxphoenix.spigot.smoothtimber.version.manager.VersionExchanger;

import net.md_5.bungee.api.ChatColor;

public class PluginUtils {

	public static final BukkitScheduler SCHEDULER = Bukkit.getScheduler();
	public static final PluginSettings SETTINGS = new PluginSettings();
	
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
			MessageConfig.load();
			registerCommands();
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
		PluginManager pluginManager = Bukkit.getPluginManager();
		for (Plugin plugin : pluginManager.getPlugins()) {
			if (plugin == null)
				continue;
			if (plugin.getName().equals("BlockyLog")) {
				SETTINGS.updatePlugin(plugin, plugin.isEnabled());
			}
		}
	}

	private void registerListener() {
		PluginManager pluginManager = Bukkit.getPluginManager();
		pluginManager.registerEvents(new BlockBreakListener(), MAIN);
		pluginManager.registerEvents(new BlockFallListener(), MAIN);
		pluginManager.registerEvents(new PluginLoadListener(), MAIN);
	}
	
	private void registerCommands() {
		CommandRedirect command = new CommandRedirect();
		
		PluginCommand plugin = MAIN.getCommand("smoothtimber");
		plugin.setExecutor(command);
		plugin.setTabCompleter(command);
		
		/*
		 * 
		 */
		
		COMMANDS.register(new HelpCommand(), "help", "?");
		COMMANDS.register(new ToggleCommand(), "toggle");
		
	}

	private void registerTasks() {

		SCHEDULER.runTaskTimerAsynchronously(MAIN, new ConfigTimer(), 20, 60);

	}
	
	/*
	 * Task Util
	 */
	
	public static <E> E getObjectFromMainThread(Supplier<E> supply) {
		return getObjectFromMainThread(supply, 50);
	}
	
	public static <E> E getObjectFromMainThread(Supplier<E> supply, long wait) {
		CountDownLatch latch = new CountDownLatch(1);
		StoredObject<E> value = new StoredObject<>();
		SCHEDULER.runTask(MAIN, () -> {
			value.setObject(supply.get());
		});
		try {
			latch.await(wait, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return value.getObject();
	}

	/*
	 * Console Util
	 */

	public static void sendConsoleMessage(String message) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

}
