package wtf.monsoon.impl.modules.misc;

import org.lwjgl.input.Keyboard;

import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.impl.ModeSetting;
import wtf.monsoon.api.setting.impl.NumberSetting;

public class TargetHUD extends Module {

	public ModeSetting mode = new ModeSetting("Mode", this,"Monsoon","Monsoon");
	public NumberSetting targetX = new NumberSetting("TargetHUD X", 508, 0, 1000, 1, this);
	public NumberSetting targetY = new NumberSetting("TargetHUD Y", 288, 0, 1000, 1, this);

	public TargetHUD() {
		super("TargetHUD", "Displays the KillAura's Target", Keyboard.KEY_NONE, Category.RENDER);
		this.addSettings(mode,targetX,targetY);
	}


	
}
