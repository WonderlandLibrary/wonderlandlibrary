package Velo.impl.Modules.player;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.api.Util.Other.other.PlayerUtil;
import Velo.impl.Event.EventPostMotion;
import Velo.impl.Event.EventPreMotion;
import Velo.impl.Event.EventReceivePacket;
import Velo.impl.Event.EventSendPacket;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.ModeSetting;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlowDown extends Module {
	
	public static ModeSetting mode = new ModeSetting("Mode", "Ncp", "Ncp", "Hypixel","Aac", "Dev", "Dev2", "Dev3", "Dev4");
	public static boolean isEnabled = false;
	
	public NoSlowDown() {
		super("NoSlowDown", "NoSlowDown", Keyboard.KEY_NONE, Category.PLAYER);
		this.loadSettings(mode);
	}
	
	public void onDisable() {
		isEnabled = false;
	}
	
	public void onUpdate(EventUpdate event) {
		isEnabled = true;
		if(mc.thePlayer.isUsingItem() && mc.thePlayer.isMoving()) {
			mc.thePlayer.setSprinting(true);
		}
		if(mode.equalsIgnorecase("Hypixel")) {
     
                if (mc.thePlayer.isMoving() && mc.thePlayer.isBlocking()) {
                    mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                }
          
            	  if (mc.thePlayer.isMoving() && mc.thePlayer.isBlocking()) {
                       mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
            	  }
		}
	}
	
	public void onPreMotionUpdate(EventPreMotion event) {
		if(mode.equalsIgnorecase("Ncp")) {
			try {
				if (mc.thePlayer.isUsingItem()) {
					
				}
			} catch(Exception e1) {
				
			}
		}
	}
	
	public void onPostMotionUpdate(EventPostMotion event) {
		if(mode.equalsIgnorecase("Ncp")) {
			try {
				if (mc.thePlayer.isUsingItem()) {
                   
				}
			} catch(Exception e1) {
				
			}
		}
	}
	
	public void onEventSendPacket(EventSendPacket event) {
		if(mode.equalsIgnorecase("Ncp")) {
			if(event.getPacket() instanceof C00PacketKeepAlive) {
				//event.setCancelled(true);
			}
			if(event.getPacket() instanceof C03PacketPlayer) {
				C03PacketPlayer c03 = (C03PacketPlayer) event.getPacket();
				//c03.onGround = false;
			}
			if(event.getPacket() instanceof C0FPacketConfirmTransaction) {
				//event.setCancelled(true);
			}
		}
	}
	
	public void onEventReceivePacket(EventReceivePacket event) {
		
	}
	
	
	   public boolean isMoving() {
           return mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f;
       }
}
