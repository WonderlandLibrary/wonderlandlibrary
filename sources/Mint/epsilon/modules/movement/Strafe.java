package epsilon.modules.movement;

import org.lwjgl.input.Keyboard;

import epsilon.events.Event;
import epsilon.events.listeners.EventUpdate;
import epsilon.modules.Module;
import epsilon.settings.setting.ModeSetting;
import epsilon.util.MoveUtil;

public class Strafe extends Module{
	public MoveUtil move = new MoveUtil();
	
	public ModeSetting mode = new ModeSetting ("StrafeType", "Always", "Always", "Ground", "Rising");
	
	public Strafe(){
		super("Strafe", Keyboard.KEY_NONE, Category.MOVEMENT, "Removes slowdown when you change camera direction");
		this.addSettings(mode);
	}
	
	public void onDisable() {
	}
	
	public void onEvent(Event e){
		if(e instanceof EventUpdate){
			this.displayInfo = mode.getMode();
			switch(mode.getMode()) {
			case "Always":

				move.strafe();
				
				break;
				
			case "Ground":
				if(mc.thePlayer.onGround) 
					move.strafe();
				break;
				
			case "Rising":
				
				if(mc.thePlayer.fallDistance<=0) move.strafe();
				
				break;
			}
				
		}
	}
}