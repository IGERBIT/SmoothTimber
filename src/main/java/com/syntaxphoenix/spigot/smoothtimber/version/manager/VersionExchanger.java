package com.syntaxphoenix.spigot.smoothtimber.version.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.syntaxphoenix.spigot.smoothtimber.utilities.PluginUtils;
import com.syntaxphoenix.spigot.smoothtimber.version.changer.*;
import com.syntaxphoenix.syntaxapi.utils.java.Strings;

public class VersionExchanger {

	public static String getMinecraftVersion() {
		return Bukkit.getVersion().split(" ")[2].replace(")", "");
	}

	public static VersionChanger getVersionChanger(String version) {
		MCVersion ver = MCVersion.fromString(version);
		if (ver == MCVersion.v1_8x) {
			PluginUtils.sendConsoleMessage(
					"&8[&5Smooth&dTimber&8] &7You're currently using the &asupported&7 Minecraft Version &a" + version
							+ " &7(&2Core " + ver.name() + "&7)");
			return new v1_8xChanger();
		} else if (ver == MCVersion.v1_9x) {
			PluginUtils.sendConsoleMessage(
					"&8[&5Smooth&dTimber&8] &7You're currently using the &asupported&7 Minecraft Version &a" + version
							+ " &7(&2Core " + ver.name() + "&7)");
			return new v1_9xChanger();
		} else if (ver == MCVersion.v1_11x) {
			PluginUtils.sendConsoleMessage(
					"&8[&5Smooth&dTimber&8] &7You're currently using the &asupported&7 Minecraft Version &a" + version
							+ " &7(&2Core " + ver.name() + "&7)");
			return new v1_11xChanger();
		} else if (ver == MCVersion.v1_13x) {
			PluginUtils.sendConsoleMessage(
					"&8[&5Smooth&dTimber&8] &7You're currently using the &asupported&7 Minecraft Version &a" + version
							+ " &7(&2Core " + ver.name() + "&7)");
			return new v1_13xChanger();
		}
		PluginUtils.sendConsoleMessage(
				"&8[&5Smooth&dTimber&8] &7You're currently using the &4unsupported&7 Minecraft Version &4" + version);
		PluginUtils.sendConsoleMessage(
				"&8[&5Smooth&dTimber&8] &7If you want to use &5Smooth&dTimber &7you need to update your server to a supported Minecraft Version");
		PluginUtils.sendConsoleMessage("&8[&5Smooth&dTimber&8] &7Supported Versions are: &a"
				+ Strings.toString(MCVersion.getSupportedVersions(), "&7, &a"));
		Bukkit.getPluginManager().disablePlugin(PluginUtils.MAIN);
		return null;
	}

	public static boolean checkPermission(WoodType type, Player play) {
		if (play.hasPermission("smoothtimber.*")) {
			return true;
		} else if (play.hasPermission("smoothtimber." + type.name().toLowerCase())) {
			return true;
		}
		return false;
	}

}
