package xyz.cucumber.base.module.feat.movement;

import org.lwjgl.input.Keyboard;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.EventType;
import xyz.cucumber.base.events.ext.EventMotion;
import xyz.cucumber.base.events.ext.EventMove;
import xyz.cucumber.base.events.ext.EventPlayerPush;
import xyz.cucumber.base.events.ext.EventReceivePacket;
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

@ModuleInfo(category = Category.MOVEMENT, description = "Allows you to move faster in water", name = "Jesus", key = Keyboard.KEY_NONE)
public class JesusModule extends Mod {

	public BooleanSettings noPush = new BooleanSettings("No Push", true);
	public ModeSettings mode = new ModeSettings("Mode", new String[] { "Intave" });
	public NumberSettings intaveDelay = new NumberSettings("Intave Delay", 2, 1, 10, 1);

	public JesusModule() {
		this.addSettings(mode, intaveDelay, noPush);
	}
	
	@EventListener
	public void onPush(EventPlayerPush e) {
		if(noPush.isEnabled()) {
			e.setCancelled(true);
		}
	}
	
	
	@EventListener
	public void onMotion(EventMotion e) {
		switch(mode.getMode().toLowerCase()) {
        case "intave":
    		if(e.getType() == EventType.PRE) {
	        	this.setInfo(mode.getMode() + "");
	    		if(mc.thePlayer.isInWater()) {
	    			if(mc.thePlayer.ticksExisted % intaveDelay.getValue() == 0 && MovementUtils.isMoving() && mc.thePlayer.hurtTime == 0) {
	        			MovementUtils.strafe(0.25f, (float)MovementUtils.getDirection(RotationUtils.customRots ? RotationUtils.serverYaw : mc.thePlayer.rotationYaw));
	        		}
	    		}
    		}
	        break;
		}
	}
}
