package xyz.cucumber.base.module.feat.player;

import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.EventType;
import xyz.cucumber.base.events.ext.EventLook;
import xyz.cucumber.base.events.ext.EventMotion;
import xyz.cucumber.base.events.ext.EventTick;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.utils.game.InventoryUtils;
import xyz.cucumber.base.utils.math.RotationUtils;

@ModuleInfo(category = Category.PLAYER, description = "Automatically put water when you are on fire", name = "Extinguish")
public class ExtinguishModule extends Mod{

	public boolean canWork;
	
	@EventListener
	public void onTick(EventTick e) {
		if(canWork) {
    		RotationUtils.customRots = true;
        	RotationUtils.serverYaw = mc.thePlayer.rotationYaw;
        	RotationUtils.serverPitch = 90f;
    	}
    	
    	int slot = InventoryUtils.getBucketSlot();
    	if(slot == -1 || !mc.thePlayer.isBurning())return;
    	
    	mc.thePlayer.inventory.currentItem = slot;
    	
    	canWork = true;
    	
    	RotationUtils.customRots = true;
    	RotationUtils.serverYaw = mc.thePlayer.rotationYaw;
    	RotationUtils.serverPitch = 90f;
	}
	
	@EventListener
	public void onMotion(EventMotion e) {
		if(canWork) {
        	if(e.getType() == EventType.PRE) {
        		EventMotion event = (EventMotion)e;
        		event.setPitch(RotationUtils.serverPitch);
        	}else {
        		mc.rightClickMouse();
        		
        		if(!mc.thePlayer.isBurning()) {
        			mc.rightClickMouse();
        			
        			canWork = false;
        			
        			RotationUtils.customRots = false;
                	RotationUtils.serverYaw = mc.thePlayer.rotationYaw;
                	RotationUtils.serverPitch = mc.thePlayer.rotationPitch;
        			
        			return;
        		}
        	}
    	}
	}
	@EventListener
	public void onLook(EventLook e) {
		if(canWork) {
    		EventLook event = (EventLook)e;
    		
    		event.setPitch(RotationUtils.serverPitch);
    	}
	}
	
}
