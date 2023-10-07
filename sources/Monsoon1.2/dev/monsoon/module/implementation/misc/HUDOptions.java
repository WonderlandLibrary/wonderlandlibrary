package dev.monsoon.module.implementation.misc;

import dev.monsoon.module.setting.impl.BooleanSetting;
import dev.monsoon.module.setting.impl.NumberSetting;
import org.lwjgl.input.Keyboard;

import dev.monsoon.event.Event;
import dev.monsoon.module.base.Module;
import dev.monsoon.module.setting.impl.ModeSetting;
import dev.monsoon.util.misc.Timer;
import dev.monsoon.module.enums.Category;

public class HUDOptions extends Module {
	
	public ModeSetting color = new ModeSetting("Color", this, "Blue", "Astolfo", "Colorful", "Red", "Blue", "Discord", "Orange", "Green", "White", "Purple");
	public ModeSetting watermarkmode = new ModeSetting("Watermark", this, "Monsoon", "Monsoon", "ZeroDay", "Plain", "Off");
	public ModeSetting arrayPos = new ModeSetting("ArryList Position", this, "Top Right", "Top Right", "Top Left", "Off");
	public ModeSetting fontMode = new ModeSetting("TextMode", this, "Normal", "Normal", "Lowercase", "Chonk");
	public ModeSetting hello = new ModeSetting("Greeting", this, "Off", "Off", "Normal");
	public ModeSetting hotbar = new ModeSetting("Hotbar", this, "Normal", "Normal", "German");
	public NumberSetting arrayBg = new NumberSetting("ArrayList Background", 0, 0, 255, 1, this);
	public BooleanSetting info = new BooleanSetting("Info", true, this);
	
	public HUDOptions() {
		super("HUD Options", Keyboard.KEY_NONE, Category.MISC);
		addSettings(color,watermarkmode,fontMode,hello,hotbar,info,arrayPos);
	}
	
	Timer timer = new Timer();
	
	boolean PlayerEat = false;
	
	public void onEvent(Event e) {
		
	}
	
	
}