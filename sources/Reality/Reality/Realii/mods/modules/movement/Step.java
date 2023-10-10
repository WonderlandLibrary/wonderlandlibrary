
package Reality.Realii.mods.modules.movement;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.event.value.Option;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.render.ArrayList2;

public class Step
extends Module {
    private Numbers<Number> height = new Numbers<Number>("Height", "height", 1.0, 1.0, 10.0, 0.5);
    public Mode mode = new Mode("Mode", "Mode", new String[]{"Npc","Vulcan","Vannila"}, "Vannila");
    private Option<Boolean> sufix = new Option<Boolean>("ShowSufix", "ShowSufix", false);

    public Step() {
        super("Step", ModuleType.Movement);
        this.addValues(this.mode, height);
    }

    @Override
    public void onDisable() {
        
    }

    @EventHandler
    private void onUpdate(EventPreUpdate e) {
    	if (ArrayList2.Sufix.getValue().equals("On")) {
        	
            
    		this.setSuffix(this.mode.getModeAsString());
    	}
    		
    	 
    	 if (ArrayList2.Sufix.getValue().equals("Off")) {
         	
    	        
     		this.setSuffix("");
     	}
    	 
    	 if (this.mode.getValue().equals("Vulcan")) {
    		 if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround) {
    			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 2, mc.thePlayer.posZ);
    	           
    		 }
    	 }
    }
}
    	



