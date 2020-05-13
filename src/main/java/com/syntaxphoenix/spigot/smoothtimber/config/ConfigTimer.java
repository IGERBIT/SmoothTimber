package com.syntaxphoenix.spigot.smoothtimber.config;

import com.syntaxphoenix.spigot.smoothtimber.utilities.PluginUtils;

public class ConfigTimer implements Runnable {

	@Override
	public void run() {
		if (CutterConfig.loaded < CutterConfig.file.lastModified()) {
			PluginUtils.sendConsoleMessage(Message.GLOBAL_PREFIX.colored() + ' '
					+ Message.RELOAD_NEEDED.colored(new String[][] { { "%type0", Message.TYPE_SETTING.message() },
							{ "%type1", Message.TYPE_SETTINGS.message() } }));
			CutterConfig.load();
			PluginUtils.sendConsoleMessage(Message.GLOBAL_PREFIX.colored() + ' '
					+ Message.RELOAD_DONE.colored(new String[] { "%type", Message.TYPE_SETTINGS.message() }));
		}
		if (MessageConfig.loaded < MessageConfig.file.lastModified()) {
			PluginUtils.sendConsoleMessage(Message.GLOBAL_PREFIX.colored() + ' '
					+ Message.RELOAD_NEEDED.colored(new String[][] { { "%type0", Message.TYPE_MESSAGE.message() },
							{ "%type1", Message.TYPE_MESSAGES.message() } }));
			MessageConfig.load();
			PluginUtils.sendConsoleMessage(Message.GLOBAL_PREFIX.colored() + ' '
					+ Message.RELOAD_DONE.colored(new String[] { "%type", Message.TYPE_MESSAGES.message() }));
		}
	}

}
