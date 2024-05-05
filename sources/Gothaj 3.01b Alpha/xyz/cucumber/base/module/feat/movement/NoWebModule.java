package xyz.cucumber.base.module.feat.movement;

import org.lwjgl.input.Keyboard;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventMotion;
import xyz.cucumber.base.events.ext.EventMove;
import xyz.cucumber.base.events.ext.EventMoveButton;
import xyz.cucumber.base.events.ext.EventSendPacket;
import xyz.cucumber.base.events.ext.EventTick;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.MovementUtils;
import xyz.cucumber.base.utils.math.RotationUtils;

@ModuleInfo(category = Category.MOVEMENT, description = "Allows you to move faster in web", name = "No Web", key = Keyboard.KEY_NONE)
public class NoWebModule extends Mod {
	
     public ModeSettings mode = new ModeSettings("Mode", new String[] {"Intave", "Vanilla"});
     public BooleanSettings shift = new BooleanSettings("k", false);
	

	public boolean sneaking;

    public NoWebModule()
    {
        this.addSettings(mode , shift);
    }
	
	public void onDisable() {
		mc.timer.timerSpeed = 1f;
	}
	
	@EventListener
	public void onTick(EventTick e)  {
		this.setInfo(mode.getMode());
		
		if(mc.thePlayer.isInWeb) {
	    	if(mc.thePlayer.isInWeb && shift.isEnabled()) {
	    		mc.gameSettings.keyBindSneak.pressed = true;
	    		sneaking = true;
	    	}
		}else {
			if(sneaking) {
				mc.gameSettings.keyBindSneak.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode());
				sneaking = false;
			}
		}
    	
    	switch(mode.getMode().toLowerCase()) {
    		case "intave":
				if(MovementUtils.isMoving() && mc.thePlayer.isInWeb) {
					mc.gameSettings.keyBindJump.pressed = false;
					if(!mc.thePlayer.onGround) {
						mc.timer.timerSpeed = 1f;
						if(mc.thePlayer.ticksExisted % 2 == 0) {
							
							MovementUtils.strafe(0.65f, (float)MovementUtils.getDirection(RotationUtils.customRots ? RotationUtils.serverYaw : mc.thePlayer.rotationYaw));
		        		}else if(mc.thePlayer.ticksExisted % 5 == 0) {
		        			MovementUtils.strafe(0.65f, (float)MovementUtils.getDirection(RotationUtils.customRots ? RotationUtils.serverYaw : mc.thePlayer.rotationYaw));
		        		}
					}else {
						MovementUtils.strafe(0.35f, (float)MovementUtils.getDirection(RotationUtils.customRots ? RotationUtils.serverYaw : mc.thePlayer.rotationYaw));
						mc.thePlayer.jump();
						mc.thePlayer.jump();
						mc.thePlayer.jump();
					}
					
					if(!mc.thePlayer.isSprinting()) {
						mc.thePlayer.motionX *= 0.75;
						mc.thePlayer.motionZ *= 0.75;
		        	}
				}
    			break;
   	
    		case "vanilla":
    			if(MovementUtils.isMoving() && mc.thePlayer.isInWeb) {
    				mc.thePlayer.isInWeb = false;
    			  }
    			}	
    	}
}