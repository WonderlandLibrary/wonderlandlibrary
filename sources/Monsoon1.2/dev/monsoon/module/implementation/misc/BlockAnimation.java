package dev.monsoon.module.implementation.misc;

import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventUpdate;
import org.lwjgl.input.Keyboard;

import dev.monsoon.module.base.Module;
import dev.monsoon.module.setting.impl.ModeSetting;
import dev.monsoon.module.setting.impl.NumberSetting;
import dev.monsoon.util.misc.Timer;
import dev.monsoon.module.enums.Category;

public class BlockAnimation extends Module {
	
	public static int BlockAnimationInt = 0;
	
	
	public ModeSetting animation = new ModeSetting("Block", this, "Monsoon", "Exhibition", "Exhi2", "Fan", "Cloud", "Monsoon", "Whiz", "1.7", "Astro", "Sigma", "Astolfo", "WOOOOOOOOOOOOOOOOOOO");
	public ModeSetting swingAnimation = new ModeSetting("Swing", this, "Normal", "Normal", "Smooth");
	public NumberSetting speed = new NumberSetting("Slowdown", 1.2, .1, 3.50, 0.01, this);
	public NumberSetting scale = new NumberSetting("Scale", 0.4, 0.1, 1, 0.01, this);
	public NumberSetting height = new NumberSetting("Y Pos", 0.52, 0.10, 1, 0.01, this);
	public NumberSetting width = new NumberSetting("X Pos", 0.56, 0.10, 1, 0.01, this);
	
	public BlockAnimation() {
		super("Animations", Keyboard.KEY_NONE, Category.MISC);
		this.addSettings(animation,swingAnimation,speed,scale,height,width);
	}
	
	Timer timer = new Timer();
	
	boolean PlayerEat = false;

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			this.setSuffix(animation.getMode());
		}
	}
}