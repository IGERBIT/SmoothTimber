package com.syntaxphoenix.spigot.smoothtimber.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.syntaxphoenix.spigot.smoothtimber.utilities.PluginUtils;

public class CutterConfig {

	static File file = new File("plugins/SmoothTimber", "config.yml");
	static long loaded = -1;
	private static FileConfiguration cfg = new YamlConfiguration();
	
	/*
	 * 
	 */

	public static int VERSION = 2;

	public static int CHECK_RADIUS = 3;
	public static List<String> CUTTER_MATERIALS = new ArrayList<>();

	public static boolean ON_SNEAK = false;
	public static boolean ENABLE_PERMISSIONS = false;

	public static boolean ENABLE_UNBREAKING = true;

	public static boolean ENABLE_LUCK = false;
	public static float LUCK_MULTIPLIER = 1.0f;

	public static boolean EXTENSION_BLOCKY = true;
	
	/*
	 * 
	 */

	public static void load() {
		loadConfig();
		if (loaded == -1)
			PluginUtils.changer.setupConfig();

		if (file.exists())
			VERSION = check("version", 1);
		else
			VERSION = check("version", VERSION);
		
		/*
		 * 
		 */

		if (VERSION == 1) {
			EXTENSION_BLOCKY = get("Extensions.BlockyLog", EXTENSION_BLOCKY);
			ON_SNEAK = get("Cutter.onlyWhileSneaking", ON_SNEAK);
			CHECK_RADIUS = get("Cutter.woodLocateRadius", CHECK_RADIUS);
			ENABLE_PERMISSIONS = get("Cutter.enablePermissions", ENABLE_PERMISSIONS);
			CUTTER_MATERIALS = get("Cutter.AxeMaterials", CUTTER_MATERIALS);
			file.delete();
			cfg = new YamlConfiguration();
		}
		
		/*
		 * 
		 */
		
		CHECK_RADIUS = check("cutter.radius", CHECK_RADIUS);
		CUTTER_MATERIALS = check("cutter.materials", CUTTER_MATERIALS);

		ON_SNEAK = check("options.cutter.sneak", ON_SNEAK);
		ENABLE_PERMISSIONS = check("options.cutter.permissions", ENABLE_PERMISSIONS);

		ENABLE_UNBREAKING = check("enchantments.unbreaking.enabled", ENABLE_UNBREAKING);

		ENABLE_LUCK = check("enchantments.fortune.enabled", ENABLE_LUCK);
		LUCK_MULTIPLIER = check("enchantments.fortune.multiplier", LUCK_MULTIPLIER);

		EXTENSION_BLOCKY = check("extensions.blockylog", EXTENSION_BLOCKY);
		
		/*
		 * 
		 */

		save();
		loaded = file.lastModified();
	}

	@SuppressWarnings("unchecked")
	private static <E> E check(String path, E input) {
		if (cfg.contains(path))
			return (E) get(path);
		set(path, input);
		return input;
	}

	@SuppressWarnings("unchecked")
	private static <E> E get(String path, E input) {
		if (cfg.contains(path))
			return (E) get(path);
		return input;
	}

	private static Object get(String path) {
		return cfg.get(path);
	}

	private static void set(String path, Object input) {
		cfg.set(path, input);
		save();
	}

	public static void save() {
		try {
			if (!file.exists())
				file.createNewFile();
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void loadConfig() {
		if (!file.exists())
			return;
		try {
			cfg.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

}
