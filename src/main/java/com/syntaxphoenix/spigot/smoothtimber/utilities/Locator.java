/**
 * 
 * @author StevenLPHD
 * 
 */
package com.syntaxphoenix.spigot.smoothtimber.utilities;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import com.syntaxphoenix.spigot.smoothtimber.config.CutterConfig;
import com.syntaxphoenix.spigot.smoothtimber.version.manager.VersionChanger;
import com.syntaxphoenix.syntaxapi.reflections.AbstractReflect;
import com.syntaxphoenix.syntaxapi.reflections.Reflect;

public class Locator {

	public final static HashMap<String, AbstractReflect> REFLECTS = new HashMap<>();

	protected static boolean blockylog = false;
	protected static int version = 0;

	public static void locateWood(Location start, List<Location> current) {
		int radius = CutterConfig.checkRadius;
		if (blockylog) {
			if (version == 1) {
				locateBlocky1(start, radius, current);
			} else if (version == 2) {
				locateBlocky2(start, radius, current);
			}
		} else {
			locateOnly(start, radius, current);
		}
	}

	private static void locateOnly(Location start, int radius, List<Location> current) {
		VersionChanger change = PluginUtils.changer;
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

	private static void locateBlocky1(Location start, int radius, List<Location> current) {
		VersionChanger change = PluginUtils.changer;
		World w = start.getWorld();
		int x = start.getBlockX();
		int y = start.getBlockY();
		int z = start.getBlockZ();

		AbstractReflect wref = REFLECTS.get("world");
		AbstractReflect cref = REFLECTS.get("chunk");

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
						locateBlocky1(l, radius, current);
					}
				}
			}
		}
	}
	
	private static void locateBlocky2(Location start, int radius, List<Location> current) {
		VersionChanger change = PluginUtils.changer;
		World w = start.getWorld();
		int x = start.getBlockX();
		int y = start.getBlockY();
		int z = start.getBlockZ();

		AbstractReflect apiref = REFLECTS.get("api");
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
						locateBlocky2(l, radius, current);
					}
				}
			}
		}
	}

	public static void generateReflect() {
		if (version == 1) {
			REFLECTS.put("world",
					new Reflect(Reflector.getBL1Class("log.BlockyWorld")).searchMethod("get", "get", World.class)
							.searchMethod("chunk", "getChunk", int.class, int.class)
							.searchMethod("contains", "containsChunk", int.class, int.class));
			REFLECTS.put("chunk", new Reflect(Reflector.getBL1Class("log.BlockyChunk")).searchMethod("contains",
					"containsBlock", int.class, int.class, int.class));
		} else if (version == 2) {
			REFLECTS.put("api", new Reflect(Reflector.getBL2Class("BlockyApi")).searchMethod("api", "getApi").searchMethod("placed", "isPlayerPlaced",
					Location.class));
		}
	}

}
