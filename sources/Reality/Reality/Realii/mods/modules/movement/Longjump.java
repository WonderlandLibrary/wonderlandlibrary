package Reality.Realii.mods.modules.movement;

import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventMove;
import Reality.Realii.event.events.world.EventPacketRecieve;
import Reality.Realii.event.events.world.EventPacketSend;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.render.ArrayList2;
import Reality.Realii.utils.cheats.player.Helper;
import Reality.Realii.utils.cheats.player.PlayerUtils;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventMove;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.managers.ModuleManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.utils.cheats.world.TimerUtil;
import Reality.Realii.utils.math.MathUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import Reality.Realii.event.events.world.EventMove;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.event.value.Option;
import Reality.Realii.guis.notification.Notification;
import Reality.Realii.guis.notification.NotificationsManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.utils.cheats.player.PlayerUtils;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventMove;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.managers.ModuleManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.utils.cheats.world.TimerUtil;
import Reality.Realii.utils.math.MathUtil;

public class Longjump extends Module{
	private Mode mode = new Mode("Mode", "mode", new String[]{"Vulcan","Invaded","Ncp","WatchdogFast","VerusHigh","Watchdog","Pika","UncpBest","VulcanFast", "Hycraft","HypixelBow","Verus"}, "Vulcan");
	public static Numbers<Number>  speed = new Numbers<>("MotionFlySpeed", 1f, 1f, 30f, 10f);
	   private TimerUtil Startdelay = new TimerUtil();
	   public boolean setback;
	   private int runningTicks = 0;
	private int stage;
	private double x;
	private double z;
	private double y;
	
	
	public Longjump(){
		super("Longjump", ModuleType.Movement);
		this.addValues(this.mode, speed);
		
	}

    @Override
    public void onEnable() {
    	Startdelay.reset();
    	 if (this.mode.getValue().equals("VulcanFast")) {
    	   		runningTicks = 0;
    	   		setback = false;

    	   	    if (mc.thePlayer == null) {
    	   	        setState(false);
    	   	        return;
    	   	    }

    	   	    mc.thePlayer.sendQueue.addToSendQueueWithoutEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2, mc.thePlayer.posZ, false));

    	        

    	        
    	        
    	        
    	   	 }
    	 if (this.mode.getValue().equals("Watchdog")) {
    		 mc.timer.timerSpeed = 0.2f;
    		// mc.thePlayer.onGround = false;
    		 
    	 }
    	 if (this.mode.getValue().equals("Hycraft")) {
    		 
    		 mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.0001, mc.thePlayer.posZ, false));
             mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
             mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
             mc.thePlayer.motionY = 0.0;
             
    	 }
    	 if (this.mode.getValue().equals("Verus")) {
    		 
    		 mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.0001, mc.thePlayer.posZ, false));
             mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
             mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
          
             
    	 }
    	 
    	 
    	  if (this.mode.getValue().equals("Pika")) {
    		 
    		 mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.0001, mc.thePlayer.posZ, false));
             mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
             mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
          
             
    	 }
    	
    	 
    	
    	
        super.onEnable();
       
        
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.mc.timer.timerSpeed = 1.0f;
    	Startdelay.reset();
    }
    
    @EventHandler
    private void onPacketSend(EventPacketSend e) {
    	
    	
    	if (this.mode.getValue().equals("Ncp")) {
    		 if (e.getPacket() instanceof C0BPacketEntityAction) {
                 e.setCancelled(true);
               
   	       }
    	}
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
   	 
    	 if (this.mode.getValue().equals("VulcanFast")) {
    		// if(Startdelay.hasReached(200)) {
    			// NotificationsManager.addNotification(new Notification("Disabled Longjump for you safety", Notification.Type.Alert,3));
    			// this.setEnabled(false);
    		// }
    		
    		 mc.thePlayer.motionY = 0.0;
    		// e.setY(1);
    		
    	 }
    	 if (this.mode.getValue().equals("Watchdog")) {
    		// boolean idk23 = false;
    		// if(mc.thePlayer.motionY > 0.60) {
    			// idk23 = true;
    		// }
    		 
    		 if(mc.thePlayer.hurtTime > 1) {
    		
    			// mc.thePlayer.motionY = 0.50;
    			 mc.timer.timerSpeed = 1.0f;
    		 
    		 }
    		 //if(idk23) {
    			// this.setEnabled(false);
    		// }
    			
    		// }
    	 }
   	 if (this.mode.getValue().equals("VerusHigh")) {
   		mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(null));
   		//if (mc.thePlayer.ticksExisted % 2 == 0) {
   		if(mc.thePlayer.onGround) {
   		 mc.thePlayer.motionY = 2f;
   		}
   		
   		//}
   		
   	  
   
   	 
   		    
   			
   		
   		 
   	 }
    	 if (this.mode.getValue().equals("Ncp")) {
    		
    		 if (!PlayerUtils.isMoving()) {
    			 e.setX(e.getPitch() + (Math.random() - 0.5) / 100);
    			 e.setZ(e.getPitch() + (Math.random() - 0.5) / 100);
    		 }

    		 mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
    	    	
    	if(mc.thePlayer.onGround) {
    		mc.thePlayer.motionY = 0.43;
    		//41
    		
    		
    	}
    	  if (Startdelay.hasReached(480)) {
  		  this.setEnabled(false);
  	  }
    	
    	
    	 }
    	 if (this.mode.getValue().equals("Pika")) {
      	 if(mc.thePlayer.onGround) {
      	   mc.thePlayer.motionY = 2.5;
      	   mc.timer.timerSpeed = 0.4f;
      	 }
      //	if(!mc.thePlayer.onGround) {
      		//if (mc.thePlayer.fallDistance > 9) {
                
             //   mc.thePlayer.fallDistance = 0;
           // }
           // if (mc.thePlayer.ticksExisted % 3 != 0) {
           //     mc.thePlayer.motionY = -0.0972;
          //  } else {
               // mc.thePlayer.motionY += 0.026;
          //  }
          //  mc.timer.timerSpeed = 1.0f;
              
      	//}
      	
      	    		
      	    	
      	
      	 
      	 //  if (mc.thePlayer.fallDistance > 2) {
                 
          //      mc.thePlayer.fallDistance = 0;
           //  }
          //   if (mc.thePlayer.ticksExisted % 3 != 0) {
          //      mc.thePlayer.motionY = -0.0972;
         //  } else {
          //       mc.thePlayer.motionY += 0.026;
           // }
      	 
      	
             
               
      	   
      	   
      	 }
    if (this.mode.getValue().equals("Hycraft")) {
    	mc.thePlayer.motionY = 0.0;	
    
    }
    
    if (this.mode.getValue().equals("UncpBest")) {
    	mc.timer.timerSpeed = 0.3f;
    	//or higher
    }
   
    
 
    	
    this.mc.thePlayer.cameraYaw = (float)(0.09090908616781235 * 1);
    
    if (ArrayList2.Sufix.getValue().equals("On")) {
    	
        
		this.setSuffix(mode.getModeAsString());
	}
		
	 
	 if (ArrayList2.Sufix.getValue().equals("Off")) {
     	
	        
 		this.setSuffix("");
 	}
	 
	  
    
     if (this.mode.getValue().equals("Vulcan")) {
    	if(mc.thePlayer.onGround) {
    		mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 6, mc.thePlayer.posZ);
   		
   	 }
    	if (mc.thePlayer.fallDistance > 2) {
            
            mc.thePlayer.fallDistance = 0;
        }
        if (mc.thePlayer.ticksExisted % 3 != 0) {
            mc.thePlayer.motionY = -0.0972;
        } else {
            mc.thePlayer.motionY += 0.026;
        }
        if(mc.thePlayer.ticksExisted % 21 == 0) {
        	mc.timer.timerSpeed = 3.0f;
        }
        
        if(mc.thePlayer.ticksExisted % 20 == 0) {
        	mc.timer.timerSpeed = 4.5f;
        }
     
        
       
    	  if(mc.thePlayer.ticksExisted % 30 == 0) {
    		  mc.timer.timerSpeed = 0.2f;
    		  mc.thePlayer.motionY = 1.9;
    		
    		  
    	  }
    	  
    	  
    	  if(mc.thePlayer.ticksExisted % 32 == 0) {
    		  mc.thePlayer.motionY = -0.0972;
    	  }
        
    		
      
       
    }
     
     
   
    
    if (this.mode.getValue().equals("Verus")) {
    	 
    	
    	
    	if (mc.thePlayer.onGround) {
    		mc.thePlayer.motionY = 0.42;
    		//mc.thePlayer.motionY = 1.00;
    		
    		
    	}
    
    	
    	
    	
    	//50
    	
    	 
    	
    	
    }
        }
    
    private void setState(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public void onEvent(EventPreUpdate e) {
    	 if (this.mode.getValue().equals("HypixelBow")) {
    	if (mc.thePlayer.hurtTime > 2) {
            mc.thePlayer.motionY = this.mc.thePlayer.motionY + 5.0;
            mc.thePlayer.jump();
            mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.50, this.mc.thePlayer.posZ);
            mc.thePlayer.moveStrafing = 2f;
    	}
    	 }
    	
    	 
    	
    	 
    	
    	
    	
    }
    
   @EventHandler
    private void onPacketSend1(EventPacketRecieve event) {
	   if (this.mode.getValue().equals("Watchdog")) {
     		
 		  if (event.getPacket() instanceof S12PacketEntityVelocity) {
           	
               S12PacketEntityVelocity packet = (S12PacketEntityVelocity)event.getPacket();
              packet.motionY = (int)(6000.0D);

               
               //e.setCancelled(true);
           }
	   }
	   if (this.mode.getValue().equals("VulcanFast")) {
	   if (event.getPacket() instanceof S08PacketPlayerPosLook && !setback) {
           final S08PacketPlayerPosLook S08 = (S08PacketPlayerPosLook) event.getPacket();

           setback= true;
           
           mc.thePlayer.posX = S08.getX();
           mc.thePlayer.posY = S08.getY();
           mc.thePlayer.posZ = S08.getZ();
           return;
       }
	   }
	
   }
   
    

    @EventHandler
    public void onMove(EventMove e) {
    	 if (this.mode.getValue().equals("VulcanFast")) {
    		 mc.timer.timerSpeed = 0.78f;
    	if (!setback) {
    		
    		mc.thePlayer.setMoveSpeed(e, 0.0);
          
        } else {
        	
        	mc.thePlayer.setMoveSpeed(e, 9);
        }
    	//maybe dont set the ground
    	//mc.thePlayer.onGround = false;
    	
   
		//runningTicks++;
    	if(mc.thePlayer.ticksExisted % 6 == 0) {
      //  if (runningTicks >= 6) {
            mc.thePlayer.setSpeed(0.0);
            setState(false);
            return;
        }
        
    	 }
    if (this.mode.getValue().equals("Hycraft")) {
    	
        if (PlayerUtils.isMoving()) {
        mc.thePlayer.setMoveSpeed(e, 0.8);
        this.mc.timer.timerSpeed = 0.6f;
        e.setY(mc.thePlayer.motionY = 0.0F);
        }
    }
    
    
    if (this.mode.getValue().equals("Invaded")) {
    	
  		 if(mc.thePlayer.hurtTime >1) {
  			 mc.thePlayer.motionY = 0.50;
  			 mc.thePlayer.setMoveSpeed(e, 5);
  		 }
  		 
  	 }
        
        
        
       
        
        if (this.mode.getValue().equals("Verus")) {
        	 mc.thePlayer.setMoveSpeed(e, 3);
        	 mc.timer.timerSpeed = 0.7f;
        
        
        
        
        
        	
    
    
   
    
    
        }
        if (this.mode.getValue().equals("VerusHigh")) {
        if(mc.thePlayer.onGround) {
       	 mc.thePlayer.setMoveSpeed(e, 0);
        }
        }
        
        if (this.mode.getValue().equals("UncpBest")) {
        	
        	 mc.thePlayer.setMoveSpeed(e, 2.0);
        	if(mc.thePlayer.onGround) {
        		mc.thePlayer.motionY = 0.42;
        		
        		
        	}
        	
        }
        
        if (this.mode.getValue().equals("Ncp")) {
        	 mc.thePlayer.setMoveSpeed(e, 0.37);
        	 //37
        		//mc.thePlayer.motionY = 0.42;
   	 }
    }
}
