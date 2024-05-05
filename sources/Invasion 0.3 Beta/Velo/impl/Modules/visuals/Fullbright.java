package Velo.impl.Modules.visuals;
import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;

public class Fullbright extends Module {
	
	public Fullbright() {
		super("Fullbright", "Fullbright", Keyboard.KEY_NONE, Category.VISUALS);
	}
	
	public void onEnable() {
		mc.gameSettings.gammaSetting = 100;
	}
	
	public void onDisable() {
		mc.gameSettings.gammaSetting = 0.5f;
	}
}
