package Reality.Realii.mods.modules.world;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventPacketSend;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.render.ArrayList2;

public class Spider extends Reality.Realii.mods.Module {
	public Mode mode = new Mode("Mode", "Mode", new String[]{"Vulcan","Vulcan2", "Vannila"}, "Vannila");
  

    public Spider() {
        super("Spider", ModuleType.World);
        addValues(mode);
    }
    
   


    @EventHandler
    public void onUpdate(EventPreUpdate e) {
    	 if (ArrayList2.Sufix.getValue().equals("On")) {
         	
             
     		this.setSuffix(mode.getModeAsString());
     	}
     		
     	 
     	 if (ArrayList2.Sufix.getValue().equals("Off")) {
          	
     	        
      		this.setSuffix("");
      	}
    	if (this.mode.getValue().equals("Vulcan")) {
       
    	 if (mc.thePlayer.isCollidedHorizontally) {
             if (mc.thePlayer.ticksExisted % 2 == 0) {
                mc.thePlayer.onGround = true;
                mc.thePlayer.motionY = 0.41999998688697815F;
             }

             final double yaw = mc.thePlayer.getDirection();
             e.setX(e.getPitch() - -MathHelper.sin((float) yaw) * 0.1f);
             e.setZ(e.getYaw() - MathHelper.cos((float) yaw) * 0.1f);
         }
     }
    	
    	if (this.mode.getValue().equals("Vulcan2")) {
    	       
       	 if (mc.thePlayer.isCollidedHorizontally) {
       	  if (mc.thePlayer.ticksExisted % 2 == 0) {
       		  mc.thePlayer.onGround = true;
               mc.thePlayer.motionY = 0.6969696;
               mc.thePlayer.moveForward = 0;
       	  }
            }
    	}
    }
    
}

 
    

