package wtf.monsoon.impl.modules.misc;

import org.lwjgl.input.Keyboard;

import wtf.monsoon.api.event.EventTarget;
import wtf.monsoon.api.event.impl.EventUpdate;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.impl.ModeSetting;
import wtf.monsoon.api.setting.impl.NumberSetting;
import wtf.monsoon.api.util.misc.Timer;

public class BlockAnimation extends Module {
	
	public static int BlockAnimationInt = 0;
	
	
	public ModeSetting animation = new ModeSetting("Block", this, "Monsoon", "Exhibition","Fan", "Cloud", "Monsoon", "Whiz", "1.7", "Astro", "Sigma", "Astolfo", "Remix");
	public ModeSetting swingAnimation = new ModeSetting("Swing", this, "Normal", "Normal", "Smooth");
	public NumberSetting speed = new NumberSetting("Slowdown", 1.2, .1, 3.50, 0.01, this);
	public NumberSetting scale = new NumberSetting("Scale", 0.4, 0.1, 1, 0.01, this);
	public NumberSetting height = new NumberSetting("Y Pos", 0.52, 0.10, 1, 0.01, this);
	public NumberSetting width = new NumberSetting("X Pos", 0.56, 0.10, 1, 0.01, this);
	
	public BlockAnimation() {
		super("Animations", "Customize animations", Keyboard.KEY_NONE, Category.MISC);
		this.addSettings(animation,swingAnimation,speed,scale,height,width);
	}
	
	Timer timer = new Timer();
	
	@EventTarget
	public void onUpdate(EventUpdate e) {
		this.setSuffix(animation.getMode());
	}
}