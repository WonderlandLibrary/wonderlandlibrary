package Reality.Realii.mods.modules.player;


import Reality.Realii.anticheat.StrafeA;
import Reality.Realii.event.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import Reality.Realii.event.events.misc.EventChat;
import Reality.Realii.event.events.world.EventPacketRecieve;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.event.value.Option;
import Reality.Realii.guis.notification.Notification;
import Reality.Realii.guis.notification.NotificationsManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.render.ArrayList2;
import Reality.Realii.utils.cheats.player.Helper;
import Reality.Realii.utils.cheats.player.PlayerUtils;
import Reality.Realii.utils.cheats.world.TimerUtil;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
public class CheatDetector extends Module {
	   private TimerUtil OfGroundTime = new TimerUtil();
	   
	   long lastTime = 0;
    public CheatDetector() {
        super("CheatDetector", ModuleType.Player);
       
    }
    
   
  
    private int numTicks;
    private int lastPosX;
    private int lastPosZ;
    
  

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
    	  Minecraft mc = Minecraft.getMinecraft();
    	  
    	   
           long deltaTime = System.currentTimeMillis() - lastTime;

           for (Object obj : mc.theWorld.playerEntities) {
               EntityPlayer player = (EntityPlayer) obj;

              
            
               if (deltaTime < 40) {
            	   if (mc.thePlayer.ticksExisted % 5 == 0) {
                   Helper.sendMessage(EnumChatFormatting.RED + "Player " + player.getName() + " Failed TimerA");
            	   }
               }
           }
           
           

           lastTime = System.currentTimeMillis();
    	

           for (final EntityPlayer player : mc.theWorld.playerEntities) {

    	    
    	       // StrafeA strafeChecker = new StrafeA();
    	       // strafeChecker.FukcingStarfe();
    	        
    	      
    	        if(player.isSprinting()) {
    	        	if(player.isSwingInProgress) {
    	        		if (mc.thePlayer.ticksExisted % 50 == 0) {
        	          //      Helper.sendMessage(EnumChatFormatting.RED + "Player " + mc.thePlayer.getName() + " Might be using keepsrint");
        	            	}
    	        	}
    	        }
    	        int currPosX = (int) mc.thePlayer.posX;
    	        int currPosZ = (int) mc.thePlayer.posZ;
    	        

    	        if (currPosX != lastPosX || currPosZ != lastPosZ) {
    	            int deltaX = currPosX - lastPosX;
    	            int deltaZ = currPosZ - lastPosZ;

    	            double distance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
    	            double speed = distance / numTicks;

    	            if (Math.abs(deltaX) > 3 || Math.abs(deltaZ) > 3) {
    	            	if (mc.thePlayer.ticksExisted % 3 == 0) {
    	                Helper.sendMessage(EnumChatFormatting.RED + "Player " + mc.thePlayer.getName() + " Failed FlyB");
    	            	}
    	            }

    	            lastPosX = currPosX;
    	            lastPosZ = currPosZ;
    	            numTicks = 0;
    	        }

    	        numTicks++;
    	        if(player.onGround) {
    	        	OfGroundTime.reset();
    	        }
    	        //2
    	        if(player.fallDistance > 1.8) {
    	        	OfGroundTime.reset();
    	        }
    	     
    	    
    	        if (OfGroundTime.hasReached(590) && !player.onGround) {
    	        	if (mc.thePlayer.ticksExisted % 3 == 0) {
    	        	Helper.sendMessage((EnumChatFormatting.RED  +"Player " + player.getName() + " Failed FlyA"));
    	        	}
  	    		  
  	    	  }
    	        if(player.motionY > 0.41999998688697815F) {
    	        	if (mc.thePlayer.ticksExisted % 3 == 0) {
    	        	Helper.sendMessage((EnumChatFormatting.RED  +"Player " + player.getName() + " Failed FlyC"));
    	        	}
    	        }
    	        
    	        if(player.motionY > 0.41999998688697815F) {
    	        	if (mc.thePlayer.ticksExisted % 3 == 0) {
    	        	Helper.sendMessage((EnumChatFormatting.RED  +"Player " + player.getName() + " Failed FlyE"));
    	        	}
    	        }
    	  
    	    }
    }

    
}
