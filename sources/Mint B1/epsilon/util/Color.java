package epsilon.util;
import java.util.ArrayList;
import java.util.Arrays;

import epsilon.ui.HUD;
import net.minecraft.client.Minecraft;

public enum Color {

	ARRAY_LIST_MODULE_NAMES(0xff54F56F),
	HUD(0xff00e6a8);

	private int color;
	private Color(int color) {
		this.color = color;
	}
	public int getColor() {
		return color;
	}
}