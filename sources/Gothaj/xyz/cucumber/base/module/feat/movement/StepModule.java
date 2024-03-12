package xyz.cucumber.base.module.feat.movement;

import org.lwjgl.input.Keyboard;

import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.EventType;
import xyz.cucumber.base.events.ext.EventMotion;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.utils.MovementUtils;

@ModuleInfo(category = Category.MOVEMENT, description = "Increase step height for blocks", name = "Step", key = Keyboard.KEY_NONE)
public class StepModule extends Mod{

	@EventListener
	public void onMotion(EventMotion e) {
		if(mc.thePlayer.ridingEntity != null) {
			if(e.getType() == EventType.PRE) {
				if(!mc.thePlayer.onGround) {
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY+1, mc.thePlayer.posX);
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY-2, mc.thePlayer.posX);
					double y = mc.thePlayer.posY-2;
					mc.thePlayer.setPosition(mc.thePlayer.posX, 0, mc.thePlayer.posX);
					mc.thePlayer.setPosition(mc.thePlayer.posX, y, mc.thePlayer.posX);
					mc.timer.timerSpeed = 1.5F;
					
				}
			}else  {
				if(!mc.thePlayer.onGround) {
					mc.timer.timerSpeed = 1F;
				}
			}
		}
	}
	
}
