package Reality.Realii.mods.modules.player;

import net.minecraft.block.BlockAir;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventPacketSend;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.render.ArrayList2;
import Reality.Realii.utils.cheats.world.TimerUtil;

import java.util.ArrayList;

public class NoFall
        extends Module {
    private Mode mode = new Mode("Mode", "Mode", new String[]{"Hypixel","Vulcan", "Spoof","Verus",""}, "Hypixel");
    private ArrayList<Packet> packets = new ArrayList<>();
    private TimerUtil timer = new TimerUtil();

    public NoFall() {
        super("NoFall", ModuleType.Player);
        this.addValues(mode);
    }

    @EventHandler
    private void onUpdate(EventPacketSend e) {
        if (mode.getValue().equals("Spoof")) {
            if (this.mc.thePlayer.fallDistance > 2.5f) {
                if (e.getPacket() instanceof C03PacketPlayer) {
                    C03PacketPlayer c = (C03PacketPlayer) e.getPacket();
                    c.onGround = mc.thePlayer.ticksExisted % 2 == 0;
                    e.setPacket(c);
                }
            }

        }
        
        if (mode.getValue().equals("Vulcan")) {
        	  if (mc.thePlayer.fallDistance > 2) {
        	  if(mc.thePlayer.ticksExisted % 2 == 0) {
  			 
        		  if(!mc.thePlayer.onGround) {
        			  if (mc.thePlayer.fallDistance > 2) {
  	            
        				  mc.thePlayer.fallDistance = 0;
        			  }
        			  if (mc.thePlayer.ticksExisted % 3 != 0) {
  	        	
        				  mc.thePlayer.motionY = -0.0872;
  	        	
        			  }
        			  else {
        				  mc.thePlayer.motionY += 0.026;
  	        }
        		  }
  		}
  		   }
        	 
        	  
            if (this.mc.thePlayer.fallDistance > 2.5f) {
            	mc.thePlayer.onGround = true;
                if (e.getPacket() instanceof C03PacketPlayer) {
                    C03PacketPlayer c = (C03PacketPlayer) e.getPacket();
                    c.onGround = mc.thePlayer.ticksExisted % 2 == 0;
                    e.setPacket(c);
                }
            }

        }
      
       
      
                
        
        if (mode.getValue().equals("Verus")) {
        	  
            if (this.mc.thePlayer.fallDistance > 2.5f) {
                if (e.getPacket() instanceof C03PacketPlayer) {
                    C03PacketPlayer c = (C03PacketPlayer) e.getPacket();
                    c.onGround = mc.thePlayer.ticksExisted % 2 == 0;
                    e.setPacket(c);
                  
                }
            }

        }
        if (ArrayList2.Sufix.getValue().equals("On")) {
        	
            
    		this.setSuffix(this.mode.getModeAsString());
    	}
    		
    	 
    	 if (ArrayList2.Sufix.getValue().equals("Off")) {
         	
    	        
     		this.setSuffix("");
     	}
        //this.setSuffix(mode.getModeAsString()+ "  sex");
    
      
       
       
                
        
       
        	
    }
        	
    
    


    

    float dis = 0;

    @EventHandler
    private void onUpdate(EventPreUpdate e) {
        if (this.mode.getValue().equals("Hypixel") && !mc.thePlayer.capabilities.isFlying && !mc.thePlayer.capabilities.disableDamage) {
            if (mc.thePlayer.fallDistance > 2.75f + getActivePotionEffect() * 0.2f && timer.delay(100)) {
                mc.thePlayer.sendQueue.addToSendQueueWithoutEvent(new C03PacketPlayer(true));
                timer.reset();
            }
        }
    }

    private boolean isBlockUnder() {
        for (int i = (int) (mc.thePlayer.posY - 1.0); i > 0; --i) {
            BlockPos pos = new BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ);
            if (!(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)) {
                return true;
            }
        }
        return false;
    }

    private int getActivePotionEffect() {
        if (mc.thePlayer.isPotionActive(Potion.jump)) {
            return mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        }
        return 0;
    }
}

