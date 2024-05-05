package epsilon.modules.movement;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventMotion;
import epsilon.modules.Module;
import epsilon.settings.setting.ModeSetting;
import epsilon.util.MoveUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class Jesus extends Module {

	public ModeSetting mode = new ModeSetting("Mode", "Solid", "Solid", "NCP", "Matrix");
	
	public Jesus() {
		super("Jesus", Keyboard.KEY_NONE, Category.MOVEMENT, "Walk on water");
		this.addSettings(mode);
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventMotion) {
			EventMotion event = (EventMotion) e;
			MoveUtil move = new MoveUtil();
			if(e.isPre()) {
				this.displayInfo = mode.getMode();
				if(inWater() || mc.thePlayer.isInWater()) {
					switch(mode.getMode()) {
					
					case "Solid":
						break;
						
					case "Matrix":
						mc.thePlayer.motionY+=0.015;
						if(MoveUtil.getBlockRelativeToPlayer(0, 0.5, 0) instanceof BlockStaticLiquid ) {
							mc.thePlayer.motionY = 0;
							mc.thePlayer.motionX*=1.04;
							mc.thePlayer.motionZ*=1.04;
						}
						
						break;
					
					case "NCP":
						
						if(inWater() ||mc.thePlayer.isInWater()) {
						}
						
						break;
					}
				}
				
			}
		}
	}

	public static boolean inWater() {

		return MoveUtil.getBlockRelativeToPlayer(0, 0, 0) instanceof BlockStaticLiquid ;
	
	}
	
}
