package Velo.impl.Modules.player;

import org.lwjgl.input.Keyboard;

import Velo.api.Main.Main;
import Velo.api.Module.Module;
import Velo.api.Module.ModuleManager;
import Velo.api.Util.Other.ChatUtil;
import Velo.impl.Event.EventMovement;
import Velo.impl.Event.EventSendPacket;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.ModeSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class AntiVoid extends Module {
	
	public static ModeSetting mode = new ModeSetting("Mode", "Hypixel", "Vanilla", "Hypixel", "Watchdog", "Redesky", "Latingamers");
	
	public AntiVoid() {
		super("AntiVoid", "AntiVoid", Keyboard.KEY_NONE, Category.PLAYER);
		this.loadSettings(mode);
	}
	
    private boolean motion;
    private BlockPos lastSafePos;
	
	@Override
	public void onEventSendPacket(EventSendPacket event) {
		if((ModuleManager.get("Longjump").isEnabled() || ModuleManager.get("Flight").isEnabled())) {
			return;
		}
		
		if((!ModuleManager.get("Longjump").isEnabled() || !ModuleManager.get("Flight").isEnabled())) {
		if(this.mode.equalsIgnorecase("Watchdog")) {
			 if(!isBlockUnder() && mc.thePlayer != null) {
	             if (event.getPacket() instanceof C03PacketPlayer) {
	                 event.setCancelled(true);
	             }
			 }
	         }
			}
		super.onEventSendPacket(event);
	}

	
	
	@Override
	public void onUpdate(EventUpdate event) {
		if((ModuleManager.get("Longjump").isEnabled() || ModuleManager.get("Flight").isEnabled())) {
			return;
		}
		
		
		if(mc.thePlayer == null || mc.theWorld == null ) {
			this.isToggled =false;
		}
		 if(this.mode.equalsIgnorecase("Vanilla")) {
		        if (mc.thePlayer.onGround) {
		        	
		        	lastSafePos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
		        }
		        if(!isBlockUnder()) {
		        	
		            if(mc.thePlayer.fallDistance > 2.9f) {
		            	if (motion) {

		                    mc.thePlayer.setPosition(lastSafePos.getX(), lastSafePos.getY(), lastSafePos.getZ());
		            		motion = false;
		            	} else { 
		                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(lastSafePos.getX(), lastSafePos.getY(), lastSafePos.getZ(), true));
		                    mc.thePlayer.fallDistance = 0;
		            	}
		            } else {
		            	motion = true;
		            }
		        } else {
		        	motion = true;
		        }
			 }
		 
			 if(this.mode.equalsIgnorecase("Watchdog")) {
					if((ModuleManager.get("Longjump").isEnabled() || ModuleManager.get("Flight").isEnabled()) || (mc.thePlayer != null && mc.theWorld != null )) {
						return;
					}
					if(mc.thePlayer != null && mc.theWorld != null && (!ModuleManager.get("Longjump").isEnabled() || !ModuleManager.get("Flight").isEnabled())) {
				 if (mc.thePlayer.onGround) {
	                   lastSafePos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
	               }
	               if(!isBlockUnder()) {
	                   if(mc.thePlayer.fallDistance > 5.9f) {
	                       if (motion) {

	                           mc.thePlayer.setPosition(lastSafePos.getX(), lastSafePos.getY(), lastSafePos.getZ());
	                           motion = false;
	                       } else { 
	                           mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(lastSafePos.getX(), lastSafePos.getY(), lastSafePos.getZ(), true));
	                           mc.thePlayer.fallDistance = 0;
	                       }
	                   } else {
	                       motion = true;
	                   }
	               } else {
	                   motion = true;
	               } 
					}
			 }
			 if(this.mode.equalsIgnorecase("Latingamers")) {
	              if (mc.thePlayer.fallDistance > 3.0f && !isBlockUnder()) {
	                  if(!mc.thePlayer.onGround) {
	                      mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + 5.5f, mc.thePlayer.posY + mc.thePlayer.posY, mc.thePlayer.posZ + 5.5f, true));
	                  }
	              }
	          }
		
		 if(this.mode.equalsIgnorecase("Redesky")) {
			 if (mc.thePlayer.onGround) {
                 lastSafePos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
             }
             if(!isBlockUnder()) {
                 if(mc.thePlayer.fallDistance > 5.9f) {
                     if (motion) {

                         mc.thePlayer.setPosition(lastSafePos.getX(), lastSafePos.getY(), lastSafePos.getZ());
                         motion = false;
                     } else { 
                         mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(lastSafePos.getX(), lastSafePos.getY(), lastSafePos.getZ(), true));
                         mc.thePlayer.fallDistance = 0;
                     }
                 } else {
                     motion = true;
                 }
             } else {
                 motion = true;
             }  
				 }
			 
		super.onUpdate(event);
	}
	
	
	@Override
	public void onMovementUpdate(EventMovement event) {
		if(ModuleManager.get("Longjump").isEnabled() || ModuleManager.get("Flight").isEnabled()) {
			return;
		}
		 if(this.mode.equalsIgnorecase("Hypixel")) {
			 if (mc.thePlayer.fallDistance > 3.0F && !isBlockUnder() && mc.thePlayer.ticksExisted % 3 == 0 && !Main.moduleManager.get("Flight").isToggled())
		          event.setY(event.getY() + 10.0D); 
			
		 }
		super.onMovementUpdate(event);
	}
	
	
	public static boolean isBlockUnder() {
        if(Minecraft.getMinecraft().thePlayer.posY < 0 && Minecraft.getMinecraft().thePlayer == null)
            return false;
        for(int off = 0; off < (int)Minecraft.getMinecraft().thePlayer.posY+2; off += 2){
            AxisAlignedBB bb = Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offset(0, -off, 0);
            if(!Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(Minecraft.getMinecraft().thePlayer, bb).isEmpty()){
                return true;
            }
        }
        return false;
    }
	
}
