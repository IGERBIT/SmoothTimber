/**
 * 
 * @author StevenLPHD
 * 
 */
package com.syntaxphoenix.spigot.smoothtimber.utilities;

import java.util.HashMap;

import org.bukkit.Bukkit;

public class Reflector {
	private static String svrVer;

	public static String getServerVersion() {
		if (svrVer == null) {
			svrVer = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
		}
		return svrVer;
	}

	private static final HashMap<String, Class<?>> cache = new HashMap<>();

	public static Class<?> getNMSClass(String nmsClassString) {
		return getClass("net.minecraft.server." + getServerVersion() + "." + nmsClassString);
	}

	public static Class<?> getCBClass(String cbClassString) {
		return getClass("org.bukkit.craftbukkit." + getServerVersion() + "." + cbClassString);
	}
	
	public static Class<?> getBL1Class(String blClassString){
		return getClass("com.syntaxphoenix.blocky." + blClassString);
	}
	
	public static Class<?> getBL2Class(String blClassString){
		return getClass("com.syntaxphoenix.blockylog." + blClassString);
	}

	public static Class<?> getClass(String classPath) {
		if (cache.containsKey(classPath)) {
			return cache.get(classPath);
		}
		try {
			Class<?> clz = Class.forName(classPath);
			cache.put(classPath, clz);
			return clz;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
