package moonsense.mod.impl;

import moonsense.event.EventTarget;
import moonsense.mod.Category;
import moonsense.mod.Mod;
import net.minecraft.potion.Potion;

public class ToggleSprint extends Mod {

	public ToggleSprint() {
		super("ToggleSprint", "Toggles Sprinting On & Off!", Category.MISC);
	}
	
	@EventTarget
	public void onUpdate() {
		if(this.isEnabled()
				&& !mc.thePlayer.isBlocking() 
				&& !mc.thePlayer.isSneaking() 
				&& (mc.thePlayer.motionX != 0 && mc.thePlayer.motionZ != 0) 
				&& !mc.thePlayer.isCollidedHorizontally 
				&& !mc.thePlayer.isPotionActive(Potion.moveSlowdown)
				&& !mc.thePlayer.isPotionActive(Potion.confusion)
				&& !mc.gameSettings.keyBindBack.isKeyDown()) {
			mc.thePlayer.setSprinting(true);
		}

	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		
		mc.thePlayer.setSprinting(false);
	}

}
