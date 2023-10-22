package epsilon.modules.movement;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventMotion;
import epsilon.events.listeners.EventUpdate;
import epsilon.modules.Module;
import epsilon.settings.setting.ModeSetting;
import epsilon.util.MoveUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

public class Step extends Module{

	private double x, y, z;
	public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "NCP", "Hypixel", "Matrix", "Vulcan", "Spartan");
	
	public Step() {
		super("Step", Keyboard.KEY_NONE, Category.MOVEMENT, "Allows you to instantly step up blocks");
		this.addSettings(mode);
	}
	
	public void onDisable() {
		mc.thePlayer.stepHeight = 0.5f;
		mc.timer.timerSpeed =1 ;
	}
	
	public void onEnable() {
		
	}
	
	public void onEvent(Event e) {
		
		MoveUtil moveUtils = new MoveUtil();
		
		
		
		if(e instanceof EventMotion) {
			
			
			
			if(e.isPre()) {

			}
			

			
			
			
		}
		
		
		
	}

}
