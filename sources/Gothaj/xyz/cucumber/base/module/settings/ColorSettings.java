package xyz.cucumber.base.module.settings;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ColorSettings extends ModuleSettings {

	private String mode = "Static";

	private List<String> modes = Arrays.asList(new String[] { "Static", "Rainbow", "Mix" ,"Sky" });

	private int mainColor, secondaryColor;

	private int alpha;

	public ColorSettings(String name, String mode, int mainColor, int secondaryColor, int alpha) {
		this.name = name;
		this.mode = mode;
		this.mainColor = mainColor;
		this.secondaryColor = secondaryColor;
		if (alpha > 100)
			alpha = 100;
		this.alpha = alpha;
		this.category = null;
	}
	public ColorSettings(String name,Supplier<Boolean> visibility, String mode, int mainColor, int secondaryColor, int alpha) {
		this.name = name;
		this.mode = mode;
		this.mainColor = mainColor;
		this.secondaryColor = secondaryColor;
		if (alpha > 100)
			alpha = 100;
		this.alpha = alpha;
		this.category = null;
		this.visibility = visibility;
	}

	public ColorSettings(String category, String name, String mode, int mainColor, int secondaryColor, int alpha) {
		this.name = name;
		this.mode = mode;
		this.mainColor = mainColor;
		this.secondaryColor = secondaryColor;
		if (alpha > 100)
			alpha = 100;
		this.alpha = alpha;
		this.category = category;
	}

	public void cycleModes() {
		if (modes.indexOf(mode) == modes.size() - 1) {
			mode = modes.get(0);
			return;
		}
		mode = modes.get(modes.indexOf(mode) + 1);
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public List<String> getModes() {
		return modes;
	}

	public void setModes(List<String> modes) {
		this.modes = modes;
	}

	public int getMainColor() {
		return mainColor;
	}

	public void setMainColor(int mainColor) {
		this.mainColor = mainColor;
	}

	public int getSecondaryColor() {
		return secondaryColor;
	}

	public int getFinalColor(float offset) {
		return 0;
	}

	public void setSecondaryColor(int secondaryColor) {
		this.secondaryColor = secondaryColor;
	}

	public int getAlpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}
}
