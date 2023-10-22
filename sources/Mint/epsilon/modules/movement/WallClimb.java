package epsilon.modules.movement;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventMotion;
import epsilon.modules.Module;
import epsilon.settings.setting.ModeSetting;

public class WallClimb extends Module {

	public ModeSetting mode = new ModeSetting ("Mode", "Vanilla", "Vanilla", "Vulcan", "Verus");
	
	public WallClimb() {
		super("WallClimb", Keyboard.KEY_NONE, Category.MOVEMENT, "Allows you to climb up walls");
		this.addSettings(mode);
	}
	
	public void onDisable() {
		mc.timer.timerSpeed = 1;
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventMotion) {
			EventMotion event = (EventMotion)e;
			if(e.isPre()) {
    			this.displayInfo = mode.getMode();
				if(mc.thePlayer.isCollidedHorizontally && mc.gameSettings.keyBindForward.getIsKeyPressed()) {
					switch(mode.getMode()) {
					case "Vanilla":
						mc.thePlayer.motionY = 0.42f;
						break;
						
					case "Vulcan":
						
						if(mc.thePlayer.ticksExisted%4==0 && mc.thePlayer.motionY<0.3f) {
							mc.thePlayer.motionY += 0.42f;
							event.setOnGround(true);
						}	
						if(mc.thePlayer.motionY>0.42f)
							mc.thePlayer.motionY-=0.1;
						
						if(mc.thePlayer.motionY<0.15f)
							mc.thePlayer.motionY +=0.1f;
						
						break;
						
					case "Verus":
						
						if(mc.thePlayer.ticksExisted%2==0)
							mc.thePlayer.motionY = 0.42f;
						
						break;
					
					}	
				}	
			}
		}
	}

}
