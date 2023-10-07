package dev.monsoon.module.implementation.player;

import org.lwjgl.input.Keyboard;

import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventMotion;
import dev.monsoon.event.listeners.EventPacket;
import dev.monsoon.module.base.Module;
import dev.monsoon.module.setting.impl.ModeSetting;
import dev.monsoon.util.misc.Timer;
import net.minecraft.network.play.client.C03PacketPlayer;
import dev.monsoon.module.enums.Category;

public class Phase extends Module {
	
	double yaw;
	double oldX;
	double oldZ;
	double d4;
	double d5;
	
	ModeSetting mode = new ModeSetting("Mode", this, "Hypixel", "Hypixel", "Packet", "Packetless");
	
	Timer timer = new Timer();
	
	public Phase() {
		super("Phase", Keyboard.KEY_NONE, Category.PLAYER);
		this.addSettings(mode);
	}	
	
	public void onEnable() {
		if(this.mode.is("Hypixel")) {
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 4, mc.thePlayer.posZ, true));
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 4, mc.thePlayer.posZ);
			this.toggle();
		}
		
	}
	
	
	
	public void onEvent(final Event e) {
        if (e instanceof EventMotion && e.isPre()) {
        	if(this.mode.is("Redesky")) {
	            final double oldPosX = this.mc.thePlayer.posX;
	            final double oldPosZ = this.mc.thePlayer.posZ;
	            if (this.mc.thePlayer.onGround && this.mc.thePlayer.isCollidedVertically) {
	                this.mc.thePlayer.setPosition(oldPosX, this.mc.thePlayer.posY - 3.0E-7, oldPosZ);
	                this.mc.thePlayer.motionY = -0.47999998927116394;
	            }
        	}
        	if(this.mode.is("Packet")) {
        		double strength = 0.6d;
				mc.thePlayer.stepHeight = 0;
				double mx = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
				double mz = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
				double x = (double) mc.thePlayer.movementInput.moveForward * strength * mx + (double) mc.thePlayer.movementInput.moveStrafe * strength * mz;
				double z = (double) mc.thePlayer.movementInput.moveForward * strength * mz - (double) mc.thePlayer.movementInput.moveStrafe * strength * mx;

				if (mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isOnLadder()) {
					mc.thePlayer.sendQueue.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z, false));
					mc.thePlayer.sendQueue.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3, mc.thePlayer.posZ, false));
					mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
				}
			}
			if(this.mode.is("Packetless")) {
				double strength = 0.6d;
				mc.thePlayer.stepHeight = 0;
				double mx = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
				double mz = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
				double x = (double) mc.thePlayer.movementInput.moveForward * strength * mx + (double) mc.thePlayer.movementInput.moveStrafe * strength * mz;
				double z = (double) mc.thePlayer.movementInput.moveForward * strength * mz - (double) mc.thePlayer.movementInput.moveStrafe * strength * mx;

				if (mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isOnLadder()) {
					mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
					mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
				}
			}
        }
        if (e instanceof EventPacket) {
        	if(e.isOutgoing()) {
        		if(this.mode.is("Redesky")) {
		            final double oldPosY = this.mc.thePlayer.posY;
		            final double oldPosX2 = this.mc.thePlayer.posX;
		            final double oldPosZ2 = this.mc.thePlayer.posZ;
		            final double yaw = Math.toRadians(this.mc.thePlayer.rotationYaw);
		            final double xDirection = -Math.sin(yaw);
		            final double zDirection = Math.cos(yaw);
		            if (((EventPacket)e).getPacket() instanceof C03PacketPlayer) {
		                final C03PacketPlayer c03PacketPlayer;
		                final C03PacketPlayer c03 = c03PacketPlayer = (C03PacketPlayer)((EventPacket)e).getPacket();
		                c03PacketPlayer.y -= 6.0E-8;
		            }
        		}
	        }
        }
    }
	
}
