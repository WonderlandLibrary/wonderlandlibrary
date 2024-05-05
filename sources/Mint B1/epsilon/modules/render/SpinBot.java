package epsilon.modules.render;

import org.lwjgl.input.Keyboard;

import epsilon.events.Event;
import epsilon.events.listeners.EventMotion;
import epsilon.modules.Module;

public class SpinBot extends Module {
	
	private float setYaw = 0;

	public SpinBot() {
		super("SpinBot", Keyboard.KEY_NONE, Category.RENDER, "You spin me right round baby right round");
	}
	
	public void onDisable() { setYaw = 0; }
	
	public void onEvent(Event e) {
		if(e instanceof EventMotion) {
			
			if(mc.gameSettings.thirdPersonView!=0) {
				mc.thePlayer.rotationYawHead = setYaw;
				mc.thePlayer.renderYawOffset = -setYaw;
				mc.thePlayer.rotationPitchHead = 90;
			}	
			setYaw+=9;
			
		}
	}

}
