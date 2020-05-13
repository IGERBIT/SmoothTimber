/**
 * 
 * @author StevenLPHD
 * 
 */
package com.syntaxphoenix.spigot.smoothtimber.utilities;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import com.syntaxphoenix.spigot.smoothtimber.config.CutterConfig;
import com.syntaxphoenix.spigot.smoothtimber.utilities.plugin.PluginPackage;
import com.syntaxphoenix.spigot.smoothtimber.utilities.plugin.PluginSettings;
import com.syntaxphoenix.spigot.smoothtimber.version.manager.VersionChanger;
import com.syntaxphoenix.syntaxapi.reflections.AbstractReflect;
import com.syntaxphoenix.syntaxapi.reflections.ReflectCache;

public class Locator {

	private static PluginSettings SETTINGS = PluginUtils.SETTINGS;

	public static void locateWood(Location start, List<Location> current) {
		int radius = CutterConfig.CHECK_RADIUS;
		PluginPackage pack = SETTINGS.getPackage("BlockyLog");

		if (pack == null) {
			locateOnly(start, radius, current);
			return;
		}

		checkCache(pack);
		if (pack.getVersion() == 1) {
			locateBlocky1(pack.getCache(), start, radius, current);
		} else if (pack.getVersion() == 2) {
			locateBlocky2(pack.getCache(), start, radius, current);
		}
	}

	private static void locateOnly(Location start, int radius, List<Location> current) {
		VersionChanger change = PluginUtils.CHANGER;
		World w = start.getWorld();
		int x = start.getBlockX();
		int y = start.getBlockY();
		int z = start.getBlockZ();

		for (int cx = x - radius; cx <= x + radius; cx++) {
			for (int cz = z - radius; cz <= z + radius; cz++) {
				boolean checkLoc = true;
				if (cx == x && cz == z) {
					checkLoc = false;
				}
				Location l = new Location(w, cx, y, cz);
				if (change.isWoodBlock(l.getBlock())) {
					if (current.contains(l)) {
						continue;
					}
					current.add(l);
					if (checkLoc) {
						locateOnly(l, radius, current);
					}
				}
			}
		}
	}

	private static void locateBlocky1(ReflectCache cache, Location start, int radius, List<Location> current) {
		VersionChanger change = PluginUtils.CHANGER;
		World w = start.getWorld();
		int x = start.getBlockX();
		int y = start.getBlockY();
		int z = start.getBlockZ();

		AbstractReflect wref = cache.get("world").get();
		AbstractReflect cref = cache.get("chunk").get();

		Object bw = wref.run("get", w);
		for (int cx = x - radius; cx <= x + radius; cx++) {
			for (int cz = z - radius; cz <= z + radius; cz++) {
				boolean checkLoc = true;
				if (cx == x && cz == z) {
					checkLoc = false;
				}
				Location l = new Location(w, cx, y, cz);
				if (change.isWoodBlock(l.getBlock())) {
					Chunk c = l.getChunk();
					if ((boolean) wref.run(bw, "contains", c.getX(), c.getZ())) {
						Object bc = wref.run(bw, "chunk", c.getX(), c.getZ());
						if ((boolean) cref.run(bc, "contains", cx, y, cz)) {
							continue;
						}
					}
					if (current.contains(l)) {
						continue;
					}
					current.add(l);
					if (checkLoc) {
						locateBlocky1(cache, l, radius, current);
					}
				}
			}
		}
	}

	private static void locateBlocky2(ReflectCache cache, Location start, int radius, List<Location> current) {
		VersionChanger change = PluginUtils.CHANGER;
		World w = start.getWorld();
		int x = start.getBlockX();
		int y = start.getBlockY();
		int z = start.getBlockZ();

		AbstractReflect apiref = cache.get("api").get();
		Object api = apiref.run("api");

		for (int cx = x - radius; cx <= x + radius; cx++) {
			for (int cz = z - radius; cz <= z + radius; cz++) {
				boolean checkLoc = true;
				if (cx == x && cz == z) {
					checkLoc = false;
				}
				Location l = new Location(w, cx, y, cz);
				if (change.isWoodBlock(l.getBlock())) {
					if ((boolean) apiref.run(api, "placed", l)) {
						continue;
					}
					if (current.contains(l)) {
						continue;
					}
					current.add(l);
					if (checkLoc) {
						locateBlocky2(cache, l, radius, current);
					}
				}
			}
		}
	}

	public static boolean isPlayerPlaced(Location location) {
		PluginPackage pack = SETTINGS.getPackage("BlockyLog");

		if (pack == null)
			return false;

		int version = pack.getVersion();
		checkCache(pack);
		if (version == 1) {
			AbstractReflect wref = pack.getCache().get("world").get();
			AbstractReflect cref = pack.getCache().get("chunk").get();
			Chunk chunk = location.getChunk();
			Object world = wref.run("get", location.getWorld());
			if (!(boolean) wref.run(world, "contains", chunk.getX(), chunk.getZ())) {
				return false;
			}
			return (boolean) cref.run(wref.run(world, "get", chunk.getX(), chunk.getZ()), "contains",
					location.getBlockX() - chunk.getX() * 16, location.getBlockY(),
					location.getBlockZ() - chunk.getZ() * 16);
		} else if (version == 2) {
			AbstractReflect apiref = pack.getCache().get("api").get();
			return (boolean) apiref.run(apiref.run("api"), "placed", location);
		}
		return false;
	}

	private static void checkCache(PluginPackage pack) {
		int version = pack.getVersion();
		ReflectCache cache = pack.getCache();
		if (version == 1) {
			cache.create("world", Reflector.getBL1Class("log.BlockyWorld")).searchMethod("get", "get", World.class)
					.searchMethod("chunk", "getChunk", int.class, int.class)
					.searchMethod("contains", "containsChunk", int.class, int.class);
			cache.create("chunk", Reflector.getBL1Class("log.BlockyChunk")).searchMethod("contains", "containsBlock",
					int.class, int.class, int.class);
		} else if (version == 2) {
			cache.create("api", Reflector.getBL2Class("BlockyApi")).searchMethod("api", "getApi").searchMethod("placed",
					"isPlayerPlaced", Location.class);
		}
	}

}
