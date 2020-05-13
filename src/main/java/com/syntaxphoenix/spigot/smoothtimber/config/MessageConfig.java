package com.syntaxphoenix.spigot.smoothtimber.config;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MessageConfig {

	static File file = new File("plugins/SmoothTimber", "message.yml");
	static long loaded = -1;
	private static FileConfiguration config = new YamlConfiguration();

	/*
	 * 
	 */

	public static EnumMap<Message, String> MESSAGES = new EnumMap<>(Message.class);

	/*
	 * 
	 */

	public static void load() {
		loadConfig();

		loaded = file.lastModified();

		/*
		 * 
		 */

		for (Message message : Message.values()) {
			MESSAGES.put(message, check(message.id(), message.message()));
		}

		/*
		 * 
		 */

		save();
		loaded = file.lastModified();
	}

	/*
	 * 
	 */

	@SuppressWarnings("unchecked")
	private static <E> E check(String path, E input) {
		if (config.contains(path))
			return (E) get(path);
		set(path, input);
		return input;
	}

	private static Object get(String path) {
		return config.get(path);
	}

	private static void set(String path, Object input) {
		config.set(path, input);
	}

	/*
	 * 
	 */

	public static void save() {
		try {
			if (!file.exists()) {
				File parent = file.getParentFile();
				if (!parent.exists())
					parent.mkdirs();
				file.createNewFile();
			}
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void loadConfig() {
		if (!file.exists())
			return;
		try {
			config.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

}
