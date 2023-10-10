package Reality.Realii.mods.modules.render;

import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.event.value.Option;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;

public class Bobbing
extends Module {
	private Option Ground = new Option("OnGround", true);
	private Option Reak = new Option("Real", true);
    private Numbers<Number> boob = new Numbers<Number>("Amount", "Amount", 1.0, 0.1, 5.0, 0.5);

    public Bobbing() {
        super("Bobbing", ModuleType.Render);
        this.addValues(this.boob,Ground,Reak);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate event) {
    	 if ((boolean) Reak.getValue()) {
    		 
    		  if (this.mc.thePlayer.onGround) {
    	            this.mc.thePlayer.cameraYaw = (float)(0.09090908616781235 * 1.0);
    	        } 
    		  if(!mc.thePlayer.onGround) {
    			  this.mc.thePlayer.cameraYaw = (float)(0.09090908616781235 * 0.0);
    		  }
    	 }
        if ((boolean) Ground.getValue()) {
        if (this.mc.thePlayer.onGround) {
            this.mc.thePlayer.cameraYaw = (float)(0.09090908616781235 * this.boob.getValue().floatValue());
        }
        } else {
        	this.mc.thePlayer.cameraYaw = (float)(0.09090908616781235 * this.boob.getValue().floatValue());
        }
    }
}

