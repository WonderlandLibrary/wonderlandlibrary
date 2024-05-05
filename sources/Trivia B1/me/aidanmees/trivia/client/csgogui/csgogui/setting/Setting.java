package me.aidanmees.trivia.client.csgogui.csgogui.setting;

import java.util.ArrayList;

import me.aidanmees.trivia.module.Module;

public class Setting {

	public static String name;
	public static Module parent;
	public static String displayName;
	public static String mode;

	public static String sval;
	public static ArrayList<String> options;
	public static String standardsval;

	
	public static boolean bval;
	public static boolean standardbval;
	
	
	public static double dval;
	public static double min;
	public static double max;
	public static double standarddval;
	public static boolean onlyint = false;

	public Setting(String name, String displayName, Module parent, String sval, ArrayList<String> options) {
		this.name = name;
		this.displayName = displayName;
		this.parent = parent;
		this.sval = sval;
		this.standardsval = sval;
		this.options = options;
		this.mode = "Combo";
	}

	public Setting(String name, String displayName, Module parent, boolean bval) {
		this.name = name;
		this.displayName = displayName;
		this.standardbval = bval;
		this.parent = parent;
		this.bval = bval;
		this.mode = "Check";
		
	}

	public Setting(String name, String displayName, Module parent, double dval, double min, double max,
			boolean onlyint) {
		this.name = name;
		this.displayName = displayName;
		this.parent = parent;
		this.dval = dval;
		this.standarddval = dval;
		this.min = min;
		this.max = max;
		this.onlyint = onlyint;
		this.mode = "Slider";
	}

	public String getName() {
		return name;
	}

	public Module getParentMod() {
		return parent;
	}

	public String getValString() {
		return this.sval;
	}

	public void setValString(String in) {
		this.sval = in;
	}

	public ArrayList<String> getOptions() {
		return this.options;
	}

	public boolean getValBoolean() {
		return this.bval;
	}

	public void setValBoolean(boolean in) {
		this.bval = in;
	}

	public double getValDouble() {
		if (this.onlyint) {
			this.dval = (int) dval;
		}
		return this.dval;
	}

	public void setValDouble(double in) {
		this.dval = in;
	}

	public double getMin() {
		return this.min;
	}

	public double getMax() {
		return this.max;
	}

	public boolean isCombo() {
		return this.mode.equalsIgnoreCase("Combo") ? true : false;
	}

	public boolean isCheck() {
		return this.mode.equalsIgnoreCase("Check") ? true : false;
	}

	public boolean isSlider() {
		return this.mode.equalsIgnoreCase("Slider") ? true : false;
	}

	public boolean onlyInt() {
		return this.onlyint;
	}
}
