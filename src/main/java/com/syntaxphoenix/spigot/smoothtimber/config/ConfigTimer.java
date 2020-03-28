package com.syntaxphoenix.spigot.smoothtimber.config;

import com.syntaxphoenix.spigot.smoothtimber.utilities.PluginUtils;

public class ConfigTimer implements Runnable {

	@Override
	public void run() {
		if(CutterConfig.loaded < CutterConfig.file.lastModified()) {
			PluginUtils.sendConsoleMessage("&8[&5Smooth&dTimber&8] &7Detected a config change, &ereloading config&7...");
			CutterConfig.load();
			PluginUtils.sendConsoleMessage("&8[&5Smooth&dTimber&8] &7Config reloaded &asuccessfully!");
		}
	}

}
