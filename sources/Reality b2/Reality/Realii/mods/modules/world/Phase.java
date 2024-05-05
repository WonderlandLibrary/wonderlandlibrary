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

public class Phase extends Reality.Realii.mods.Module {
	public Mode mode = new Mode("Mode", "Mode", new String[]{"Down"}, "Down");
  

    public Phase() {
        super("Phase", ModuleType.World);
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
    	if (this.mode.getValue().equals("Down")) {
    	
    		// if (mc.gameSettings.keyBindSneak.isPressed()) {
    		  //mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + -3, mc.thePlayer.posZ);
    		// }
             
    		 
    		 boolean canSetPosition = true;
    		 for (int i = 1; i <= 3; i++) {
    		     if (!mc.theWorld.isAirBlock(mc.thePlayer.getPosition().down(i))) {
    		         canSetPosition = false;
    		         break;
    		         
    		     }
    		 }
    		 if (canSetPosition && mc.gameSettings.keyBindSneak.isPressed()) {
    		mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + -3, mc.thePlayer.posZ);
    			 
    		 }
             

            
         
     }
    	
    	
    
    }
}

 
    

