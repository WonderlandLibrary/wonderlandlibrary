package epsilon.modules.movement;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventMotion;
import epsilon.events.listeners.EventUpdate;
import epsilon.modules.Module;
import epsilon.settings.setting.ModeSetting;
import epsilon.util.MoveUtil;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

public class Sprint extends Module{
	
	public ModeSetting sprint = new ModeSetting ("Mode", "Legit", "Legit", "Omni");
	MoveUtil move = new MoveUtil();
	public Sprint(){
		super("Sprint", Keyboard.KEY_N, Category.MOVEMENT, "Sprints for you");
		this.addSettings(sprint);
	}
	
	public void onEnable(){
	}
	
	public void onDisable(){
		mc.thePlayer.setSprinting(mc.gameSettings.keyBindSprint.getIsKeyPressed());
		
	}
	
	public void onEvent(Event e){
		if(e instanceof EventUpdate){
			if(e.isPre()){
				if(sprint.getMode() == "Legit") {
					if(mc.thePlayer.moveForward > 0 && !mc.thePlayer.isUsingItem() && !mc.thePlayer.isSneaking() && !mc.thePlayer.isCollidedHorizontally)
						mc.thePlayer.setSprinting(true);
				}else {
					mc.thePlayer.setSprinting(true);
				}
			}
		}
	}

}