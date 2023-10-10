package Reality.Realii.mods.modules.combat;

import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

import com.mojang.realmsclient.gui.ChatFormatting;

import Reality.Realii.Client;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventMove;
import Reality.Realii.event.events.world.EventPacketRecieve;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.mods.modules.movement.Speed;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.movement.LagBackChecker;
import Reality.Realii.mods.modules.render.ArrayList2;
import Reality.Realii.utils.cheats.player.Helper;
import Reality.Realii.utils.cheats.player.PlayerUtils;


public class Velocity
extends Module {
  
    private Numbers<Number> vertical = new Numbers<Number>("vertical", "vertical", 0.0, 0.0, 100.0, 5.0);
    private Numbers<Number> horizontal = new Numbers<Number>("horizontal", "horizontal", 0.0, 0.0, 100.0, 5.0);
    public static Mode Mode = new Mode("Mode", "Mode", new String[]{"Hypixel","Watchdog","BlocksMc","HypixelBasic","Mmc","Packet","Vulcan","HypixelAdvanced","Karhu","Reverse","Timer"}, "Packet");
    private int counter;
    public Velocity() {
        super("Velocity", ModuleType.Combat);
        this.addValues(horizontal, vertical, Mode);
    }

    @EventHandler
    private void onPacket(EventPacketRecieve e) {
    	 if (this.Mode.getValue().equals("Mmc")) {
    	  if (e.getPacket() instanceof S12PacketEntityVelocity) {
    		  if (mc.thePlayer.ticksExisted % 2 == 0) {
    			  e.setCancelled(true);
    		  }
    	  }
    	 }
    	  
    	
    	  if (this.Mode.getValue().equals("Packet")) {
        if (e.getPacket() instanceof S12PacketEntityVelocity) {


                //S12PacketEntityVelocity packet = (S12PacketEntityVelocity)e.getPacket();
               // packet.motionX = (int)(this.horizontal.getValue().floatValue() / 100);
               // packet.motionY = (int)(this.vertical.getValue().floatValue()/ 100);
               // packet.motionZ = (int)(this.horizontal.getValue().floatValue()/ 100);
                //e.setCancelled(true);
        	
        	 S12PacketEntityVelocity packet = (S12PacketEntityVelocity)e.getPacket();
             packet.motionX = (int)(this.horizontal.getValue().floatValue() / 100);
             packet.motionY = (int)(this.vertical.getValue().floatValue()/ 100);
             packet.motionZ = (int)(this.horizontal.getValue().floatValue()/ 100);
            }
        }
    	  
    	  if (this.Mode.getValue().equals("BlocksMc")) {
    		  
    		 if(!Client.instance.getModuleManager().getModuleByClass(Speed.class).isEnabled()) {
    	        if (e.getPacket() instanceof S12PacketEntityVelocity) {
    	        	
    	        	 S12PacketEntityVelocity packet = (S12PacketEntityVelocity)e.getPacket();
    	             packet.motionX = (int)(90);
    	             packet.motionY = (int)(90);
    	             packet.motionZ = (int)(90);
    	            }
    	        }
    	  }
    	  
    	  if (this.Mode.getValue().equals("Vulcan")) {
    			
    	        if (e.getPacket() instanceof S12PacketEntityVelocity) {
    	        	if (e.getPacket() instanceof C0FPacketConfirmTransaction) {
    	  	            e.setCancelled(true);
    	    		}

                     
    	                S12PacketEntityVelocity packet = (S12PacketEntityVelocity)e.getPacket();
    	                packet.motionX = (int)(100);
    	                packet.motionY = (int)(100);
    	                packet.motionZ = (int)(100);
    	                
    	        
    	        }
    	            
    	        }
    	  
    	  if (this.Mode.getValue().equals("Reverse")) {
    		
    	        if (e.getPacket() instanceof S12PacketEntityVelocity) {

    	        	
    	                S12PacketEntityVelocity packet = (S12PacketEntityVelocity)e.getPacket();
    	          
    	                packet.motionX = -packet.motionX;
    	                packet.motionZ = -packet.motionZ;
    	                //e.setCancelled(true);
    	        }
    	        }
    	  
    	  
    	  if (this.Mode.getValue().equals("Watchdog")) {
      		
    		  if (e.getPacket() instanceof S12PacketEntityVelocity) {
              	
                  S12PacketEntityVelocity packet = (S12PacketEntityVelocity)e.getPacket();
                  packet.motionX = (int)(100f);
                  packet.motionZ = (int)(100f);
                  //e.setCancelled(true);
                  
              }
    		  if (mc.thePlayer.ticksExisted % 3 == 0) {
    		  if (e.getPacket() instanceof S12PacketEntityVelocity) {
    			e.setCancelled(true);
    		  }
    	  }
    		  
  	        }
    	  
    	  if (this.Mode.getValue().equals("Karhu")) {
    		  if (e.getPacket() instanceof S12PacketEntityVelocity) {

    			 
	                S12PacketEntityVelocity packet = (S12PacketEntityVelocity)e.getPacket();
	                packet.motionX = (int)(100);
	                //packet.motionY = (int)(100);
	                packet.motionZ = (int)(100);
	             //if(mc.thePlayer.onGround && mc.thePlayer.hurtTime >1) {
    			//  mc.thePlayer.motionY = 0.30;
    			  //if(mc.thePlayer.motionY < 30) {
    				//  mc.thePlayer.motionY = -0.1;
    			  //}
    			 
	            // }
	             
	             //OR JUST 0.40 VELO
	            
	        }
    		 
    		  
    		  
    	  }
    	            
    	        
    	  
    	  if (this.Mode.getValue().equals("Timer")) {
    		
    			  
    		  
    		
    	        if (e.getPacket() instanceof S12PacketEntityVelocity) {

    	        	   
    	                S12PacketEntityVelocity packet = (S12PacketEntityVelocity)e.getPacket();
    	                
    	                packet.motionX = (int)(100);
    	                packet.motionY = (int)(100);
    	                packet.motionZ = (int)(100);
    	                
    	                if(mc.thePlayer.hurtTime > -1) {
    	                  mc.timer.timerSpeed = 1.0f;
    	                }
    	                if(mc.thePlayer.hurtTime > 3) {
    	                mc.timer.timerSpeed = 0.7f;
    	                }
    	                //e.setCancelled(true);
    	        
    	        }
    	            
    	        }
    	  
       	  if (this.Mode.getValue().equals("Hypixel")) {
       		  
              if (e.getPacket() instanceof S12PacketEntityVelocity) {
            	  if (mc.thePlayer.onGround = false) {
            		  S12PacketEntityVelocity packet = (S12PacketEntityVelocity)e.getPacket();
                      packet.motionX = (int)(100f);
                      packet.motionY = (int)(100f);
                      packet.motionZ = (int)(100f);
            		  
            	  }
            	 
            	  if (mc.thePlayer.onGround = true) {


                      S12PacketEntityVelocity packet = (S12PacketEntityVelocity)e.getPacket();
                      packet.motionX = (int)(100f);
                      packet.motionZ = (int)(100f);
                      //e.setCancelled(true);
                  }
              }
              }
       	  
       	if (this.Mode.getValue().equals("HypixelBasic")) {
     		  
            if (e.getPacket() instanceof S12PacketEntityVelocity) {
            	
                    S12PacketEntityVelocity packet = (S12PacketEntityVelocity)e.getPacket();
                    packet.motionX = (int)(100f);
                    packet.motionZ = (int)(100f);
                    //e.setCancelled(true);
                }
            }
       	
    	if (this.Mode.getValue().equals("HypixelAdvanced")) {
    		if (mc.thePlayer.onGround) {
            if (e.getPacket() instanceof S12PacketEntityVelocity) {
            	 
            	
                    S12PacketEntityVelocity packet = (S12PacketEntityVelocity)e.getPacket();
                    packet.motionX = (int)(100f);
                    packet.motionZ = (int)(100f);
                    //e.setCancelled(true);
                
            }
    		}
    		
    		if (!mc.thePlayer.onGround) {
    			 S12PacketEntityVelocity packet = (S12PacketEntityVelocity)e.getPacket();
                 packet.motionX = (int)(100f);
                 packet.motionY = (int)(100f);
                 packet.motionZ = (int)(100f);
    			
    		}
    		
            }
            }
    
  
    @EventHandler
    public void onUpdate(EventPreUpdate e) {
    
    	if (this.Mode.getValue().equals("Watchdog")) {
    		if(mc.thePlayer.hurtTime >1 && mc.thePlayer.ticksExisted % 3 == 0) {
    			//counter++;
    			//counter++;
      			//Helper.sendMessage(ChatFormatting.RED + "" +ChatFormatting.ITALIC +"KnockBack Tick " + counter );
    			
    		}
      		if(mc.thePlayer.hurtTime >1 && mc.thePlayer.ticksExisted % 7 == 0) {
      			
      			counter++;
      			Helper.sendMessage(ChatFormatting.RED + "" +ChatFormatting.ITALIC +"KnockBack Tick " + counter );
      		}
    	}
    	if (ArrayList2.Sufix.getValue().equals("On")) {
        	
            
    		this.setSuffix(Mode.getModeAsString());
    	}
    		
    	 
    	 if (ArrayList2.Sufix.getValue().equals("Off")) {
         	
    	        
     		this.setSuffix("");
     	}
            
        
   
    	
}
    
}


