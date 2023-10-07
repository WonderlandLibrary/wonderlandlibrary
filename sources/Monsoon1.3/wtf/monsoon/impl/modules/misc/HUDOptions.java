package wtf.monsoon.impl.modules.misc;

import org.lwjgl.input.Keyboard;

import wtf.monsoon.api.event.EventTarget;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.impl.BooleanSetting;
import wtf.monsoon.api.setting.impl.ModeSetting;
import wtf.monsoon.api.setting.impl.NumberSetting;


public class HUDOptions extends Module {
	
	public ModeSetting color = new ModeSetting("Color", this, "Blue", "Astolfo", "Colorful", "Red", "Blue", "Discord", "Orange", "Green", "White", "Purple");
	public ModeSetting watermarkmode = new ModeSetting("Watermark", this, "Monsoon", "Monsoon", "ZeroDay", "Plain", "$$ BALLI $$", "Off");
	public ModeSetting hotbar = new ModeSetting("Hotbar", this, "Normal", "Normal", "German");
	public ModeSetting font = new ModeSetting("Font", this, "Monsoon", "Monsoon", "Moon");
	public NumberSetting arraybg = new NumberSetting("ArrayList opacity", 0, 0, 255, 1, this);
	public BooleanSetting info = new BooleanSetting("Info", true, this);
	
	public HUDOptions() {
		super("HUD Options", "Customize the HUD", Keyboard.KEY_NONE, Category.MISC);
		addSettings(color,watermarkmode,hotbar,info,font,arraybg);
	}
	
	
}