package com.syntaxphoenix.spigot.smoothtimber.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.syntaxphoenix.spigot.smoothtimber.utilities.PluginUtils;

public class CutterConfig {
	
	private static File f = new File("plugins/SmoothTimber", "config.yml");
	private static FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
	
	public static boolean extension_blocky = true;
	public static boolean permissionsEnabled = false;
	public static boolean onSneak = false;
	public static List<String> cutterMaterials = new ArrayList<>();
	
	public static void load() {
		PluginUtils.changer.setupConfig();
		extension_blocky = check("Extensions.BlockyLog", extension_blocky);
		onSneak = check("Cutter.onlyWhileSneaking", onSneak);
		permissionsEnabled = check("Cutter.enablePermissions", permissionsEnabled);
		cutterMaterials = check("Cutter.AxeMaterials", cutterMaterials);
	}
	
	@SuppressWarnings("unchecked")
	private static <E> E check(String path, E input) {
		if(cfg.contains(path)) return (E) get(path);
		set(path, input);
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
			cfg.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
