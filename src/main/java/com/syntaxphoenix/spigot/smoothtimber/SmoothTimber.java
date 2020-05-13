package com.syntaxphoenix.spigot.smoothtimber;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.syntaxphoenix.spigot.smoothtimber.event.AsyncPlayerChopTreeEvent;
import com.syntaxphoenix.spigot.smoothtimber.toggle.ToggleStorage;
import com.syntaxphoenix.spigot.smoothtimber.utilities.PluginUtils;
import com.syntaxphoenix.spigot.smoothtimber.version.manager.VersionChanger;
import com.syntaxphoenix.syntaxapi.command.CommandManager;

public class SmoothTimber extends JavaPlugin {
	
	public static final CommandManager COMMANDS = new CommandManager();

	public static SmoothTimber MAIN;
	public static ToggleStorage STORAGE;

	public void onLoad() {
		MAIN = this;
	}

	public void onEnable() {
		PluginUtils.setUp(MAIN);
		STORAGE = new ToggleStorage(this);
	}

	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
	}

	public static boolean triggerChopEvent(Player player, Location location, VersionChanger change, ItemStack tool,
			ArrayList<Location> woodBlocks) {
		AsyncPlayerChopTreeEvent event = new AsyncPlayerChopTreeEvent(player, location, change, tool, woodBlocks);
		Bukkit.getPluginManager().callEvent(event);
		return event.isCancelled();
	}

}
