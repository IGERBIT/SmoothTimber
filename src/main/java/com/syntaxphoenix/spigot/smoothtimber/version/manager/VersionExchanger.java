package com.syntaxphoenix.spigot.smoothtimber.version.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.syntaxphoenix.spigot.smoothtimber.config.Message;
import com.syntaxphoenix.spigot.smoothtimber.utilities.PluginUtils;
import com.syntaxphoenix.spigot.smoothtimber.version.changer.*;
import com.syntaxphoenix.syntaxapi.utils.java.Strings;

public class VersionExchanger {

	public static String getMinecraftVersion() {
		return Bukkit.getVersion().split(" ")[2].replace(")", "");
	}

	public static VersionChanger getVersionChanger(String minecraft) {
		MCVersion core = MCVersion.fromString(minecraft);
		if (core == MCVersion.v1_8x) {
			PluginUtils.sendConsoleMessage(Message.GLOBAL_PREFIX.colored() + ' ' + Message.VERSION_SUPPORTED
					.colored(new String[][] { { "%minecraft%", minecraft }, { "%core%", core.name() } }));
			return new v1_8xChanger();
		} else if (core == MCVersion.v1_9x) {
			PluginUtils.sendConsoleMessage(Message.GLOBAL_PREFIX.colored() + ' ' + Message.VERSION_SUPPORTED
					.colored(new String[][] { { "%minecraft%", minecraft }, { "%core%", core.name() } }));
			return new v1_9xChanger();
		} else if (core == MCVersion.v1_11x) {
			PluginUtils.sendConsoleMessage(Message.GLOBAL_PREFIX.colored() + ' ' + Message.VERSION_SUPPORTED
					.colored(new String[][] { { "%minecraft%", minecraft }, { "%core%", core.name() } }));
			return new v1_11xChanger();
		} else if (core == MCVersion.v1_13x) {
			PluginUtils.sendConsoleMessage(Message.GLOBAL_PREFIX.colored() + ' ' + Message.VERSION_SUPPORTED
					.colored(new String[][] { { "%minecraft%", minecraft }, { "%core%", core.name() } }));
			return new v1_13xChanger();
		}
		PluginUtils.sendConsoleMessage(Message.GLOBAL_PREFIX.colored() + ' '
				+ Message.VERSION_UNSUPPORTED.colored(new String[] { "%minecraft%", minecraft }));
		PluginUtils.sendConsoleMessage(Message.GLOBAL_PREFIX.colored() + ' ' + Message.VERSION_NEED222UPDATE.colored());
		PluginUtils.sendConsoleMessage(
				Message.GLOBAL_PREFIX.colored() + ' ' + Message.VERSION_SUPPORTED.colored(new String[] { "%versions%",
						Strings.toString(MCVersion.getSupportedVersions(), Message.GLOBAL_LIST222SPLIT.message()) }));
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
