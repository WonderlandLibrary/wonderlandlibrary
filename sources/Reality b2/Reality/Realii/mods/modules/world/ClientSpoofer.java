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
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import java.io.ByteArrayOutputStream;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ClientSpoofer extends Reality.Realii.mods.Module {
	public Mode mode = new Mode("Mode", "Mode", new String[]{"Lunar","Badlion","CB","LabyMod"}, "Lunar");
  

    public ClientSpoofer() {
        super("ClientSpoofer", ModuleType.World);
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
    
    		 
             

            
         
     }
    
    @EventHandler
    private void onPacketSend(EventPacketSend e) {
    	 if (e.getPacket() instanceof C17PacketCustomPayload) {
    	 ByteBuf message = Unpooled.buffer();
    	 
    	 if (this.mode.getValue().equals("LabyMod")) {
			  message.writeBytes("LMC".getBytes());
	}
    	 
    	if (this.mode.getValue().equals("Badlion")) {
    	      message.writeBytes("badlion".getBytes());
    	}
    	
    	if (this.mode.getValue().equals("CB")) {
   			  message.writeBytes("CB".getBytes());
   	}
    	
    	if (this.mode.getValue().equals("Lunar")) {
    	            mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("REGISTER", new PacketBuffer(message)));
    	            message.writeBytes("Lunar-Client".getBytes());
    	    }
    	}
    }
}
    	
    	
    
    


 
    

