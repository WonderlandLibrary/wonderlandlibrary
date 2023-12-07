package Velo.impl.Modules.visuals;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.api.Module.Module.Category;
import Velo.impl.Event.EventRender;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.ModeSetting;
import Velo.impl.Settings.NumberSetting;

public class BlockOverlay extends Module {
	
	public static ModeSetting mode = new ModeSetting("Mode", "Custom Color", "Custom Color", "Rainbow", "Chill Rainbow");
	public static NumberSetting red = new NumberSetting("Red",  255, 1, 255, 1);
	public static NumberSetting blue = new NumberSetting("Blue",  255, 1, 255, 1);
	public static NumberSetting green = new NumberSetting("Green",  255, 1, 255, 1);
	
	public static boolean isEnabled = false;
	
	public BlockOverlay() {
		super("BlockOverlay", "BlockOverlay "+ mode.getMode(), Keyboard.KEY_NONE, Category.VISUALS);
		this.loadSettings(mode, red, blue, green);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		isEnabled = false;
	}
	
	public void onUpdate(EventUpdate event) {
		isEnabled = true;
		this.setName("BlockOverlay");
		this.setDisplayName("BlockOverlay");
	}
}
