package today.flux.module.implement.Render;

import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.ColorValue;

import java.awt.*;

public class ChestESP extends Module {
	public static ColorValue chestESPColours = new ColorValue("StorageESP", "ChestESP", Color.ORANGE);

	public static BooleanValue Chest = new BooleanValue("StorageESP", "Chest", true);
	public static BooleanValue EnderChest = new BooleanValue("StorageESP", "EnderChest", true);

	public ChestESP() {
		super("StorageESP", Category.Render, false);
	}
}