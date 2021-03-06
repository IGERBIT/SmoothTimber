package com.syntaxphoenix.spigot.smoothtimber.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
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

	public static int CHECK_RADIUS = 2;
	public static List<String> CUTTER_MATERIALS = new ArrayList<>();

	public static boolean ON_SNEAK = false;
	public static boolean TOGGLEABLE = true;
	public static boolean ENABLE_PERMISSIONS = false;
	public static boolean SYNC_BLOCK_DETECTION = false;

	public static boolean ENABLE_UNBREAKING = true;

	public static boolean ENABLE_LUCK = false;
	public static double LUCK_MULTIPLIER = 1.0;

	public static boolean ENABLE_EXCLUSION = false;
	public static List<Material> EXCLUDED_MATERIALS = new ArrayList<>();

	public static boolean ENABLE_WORLD = false;
	public static boolean ENABLE_WORLD_BLACKLIST = false;
	public static List<String> WORLD_LIST;

	public static boolean EXTENSION_BLOCKY = true;

	/*
	 * 
	 */

	public static void load() {
		loadConfig();
		if (loaded == -1)
			PluginUtils.CHANGER.setupConfig();

		if (file.exists())
			VERSION = check("version", 1);
		loaded = file.lastModified();

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

		VERSION = check("version", VERSION);

		CHECK_RADIUS = check("cutter.radius", CHECK_RADIUS);
		CUTTER_MATERIALS = check("cutter.materials", CUTTER_MATERIALS);

		ON_SNEAK = check("options.cutter.sneak", ON_SNEAK);
		TOGGLEABLE = check("options.cutter.toggleable", TOGGLEABLE);
		ENABLE_PERMISSIONS = check("options.cutter.permissions", ENABLE_PERMISSIONS);
		SYNC_BLOCK_DETECTION = check("options.cutter.sync-detection", SYNC_BLOCK_DETECTION);

		ENABLE_UNBREAKING = check("enchantments.unbreaking.enabled", ENABLE_UNBREAKING);

		ENABLE_LUCK = check("enchantments.fortune.enabled", ENABLE_LUCK);
		LUCK_MULTIPLIER = check("enchantments.fortune.multiplier", LUCK_MULTIPLIER);

		ENABLE_EXCLUSION = check("exclusion.enabled", ENABLE_EXCLUSION);
		EXCLUDED_MATERIALS = check("exclusion.list", EXCLUDED_MATERIALS);

		ENABLE_WORLD = check("worlds.enabled", ENABLE_WORLD);
		ENABLE_WORLD_BLACKLIST = check("worlds.blacklist", ENABLE_WORLD_BLACKLIST);
		WORLD_LIST = check("worlds.list", worldStringList());

		EXTENSION_BLOCKY = check("extensions.blockylog", EXTENSION_BLOCKY);
		
		/*
		 * 
		 */
		
		if(CHECK_RADIUS < 0) {
			set("cutter.radius", CHECK_RADIUS = 1);
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
	private static List<Material> check(String path, List<Material> input) {
		if (cfg.contains(path)) {
			List<String> list = (List<String>) get(path);
			ArrayList<Material> output = new ArrayList<>();
			for (String value : list)
				output.add(Material.valueOf(value.toUpperCase()));
			return output;
		} else {
			ArrayList<String> write = new ArrayList<>();
			if (!input.isEmpty())
				for (Material material : input)
					write.add(material.name());
			cfg.set(path, write);
			return input;
		}
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

	/*
	 * 
	 */

	private static List<String> worldStringList() {

		if (WORLD_LIST != null)
			return WORLD_LIST;

		WORLD_LIST = new ArrayList<>();
		for (World world : Bukkit.getWorlds()) {
			WORLD_LIST.add(world.getName());
		}
		return WORLD_LIST;
	}

}
