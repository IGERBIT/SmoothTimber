package com.syntaxphoenix.spigot.smoothtimber.config;

import net.md_5.bungee.api.ChatColor;

public enum Message {

	// Global
	GLOBAL_PREFIX("&5Smooth&dTimber &8||"),

	// Reload
	RELOAD_NEEDED("&7Detected a %type0 change, &ereloading %type1&7..."),
	RELOAD_DONE("&7%type reloaded &asuccessfully!"),

	// Types
	TYPE_MESSAGE("message"), TYPE_MESSAGES("messages"), TYPE_SETTING("setting"), TYPE_SETTINGS("settings"),

	// Time
	TIME_SECOND("second"), TIME_SECONDS("seconds"),

	// Tools
	TOOLS_WOODCHOPPER("woodchopper"),

	// Toggle
	TOGGLE_ON_FOREVER("&7You enabled your &a%tool&7!"),
	TOGGLE_ON_TIMED("&7You enabled your &a%tool &7for &2%time&7!"),
	TOGGLE_OFF("&7You disabled your &c%tool&7!"),
	TOGGLE_DISABLED("&7Toggling is disabled!"),
	
	// Command
	COMMAND_ONLY_PLAYER("&7Only a &dplayer &7can run this command!"), 
	COMMAND_WIP("&7This command is work in progress!"), 
	COMMAND_NONEXISTENT("&7This command doesn't exist!"),

	/*
	 * 
	 */
	;

	private final String message;

	/*
	 * 
	 */

	private Message() {
		message = "";
	}

	private Message(String message) {
		this.message = message;
	}

	/*
	 * 
	 */

	public final String id() {
		return name().toLowerCase().replace('_', '.');
	}

	/*
	 * 
	 */

	public final String message() {
		String configured = MessageConfig.MESSAGES.get(this);
		return configured != null ? configured : message;
	}

	public final String message(String[] replace) {
		return message().replace(replace[0], replace[1]);
	}

	public final String message(String[][] replace) {
		String message = message();
		for (String[] value : replace)
			message = message.replace(value[0], value[1]);
		return message;
	}

	/*
	 * 
	 */

	public final String colored() {
		return ChatColor.translateAlternateColorCodes('&', message());
	}

	public final String colored(String[] replace) {
		return ChatColor.translateAlternateColorCodes('&', message(replace));
	}

	public final String colored(String[][] replace) {
		return ChatColor.translateAlternateColorCodes('&', message(replace));
	}

	/*
	 * 
	 */

	public static final Message fromId(String id) {
		return valueOf(id.replace('.', '_').toUpperCase());
	}

}
