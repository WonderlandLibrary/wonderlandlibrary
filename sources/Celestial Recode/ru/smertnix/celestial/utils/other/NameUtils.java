package ru.smertnix.celestial.utils.other;

import ru.smertnix.celestial.Celestial;

public class NameUtils {
	public static String name = "tuskevich";
	public static String uid = "1";

	public static String getName() {
		return name;
	}
	
	public static String getUID() {
		return uid;
	}

	public static void setName(String name2) {
		name = name2;
	}
	
	public static void setUID(String uid2) {
		uid = uid2;
		Celestial.uid = uid2;
	}
}
