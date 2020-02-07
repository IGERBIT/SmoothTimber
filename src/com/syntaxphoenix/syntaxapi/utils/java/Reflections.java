package com.syntaxphoenix.syntaxapi.utils.java;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflections {

	public static boolean hasSameArguments(Class<?>[] compare1, Class<?>[] compare2) {
		if (compare1.length == 0 && compare2.length == 0) {
			return true;
		} else if (compare1.length != compare2.length) {
			return false;
		}
		for(Class<?> arg1 : compare1) {
			boolean found = false;
			for(Class<?> arg2 : compare2) {
				if(arg2.isAssignableFrom(arg1)) {
					found = true;
					break;
				}
			}
			if(!found) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * 
	 */

	public static Object execute(Object source, Method method, Object... arguments) {
		if (method != null) {
			boolean access;
			if (!(access = method.isAccessible())) {
				method.setAccessible(true);
			}
			try {
				return method.invoke(source, arguments);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				if (!access) {
					method.setAccessible(access);
				}
				e.printStackTrace();
			}
			if (!access) {
				method.setAccessible(access);
			}
		}
		return null;
	}

}
