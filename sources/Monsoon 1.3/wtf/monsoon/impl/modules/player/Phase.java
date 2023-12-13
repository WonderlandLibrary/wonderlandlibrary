package wtf.monsoon.impl.modules.player;

import org.lwjgl.input.Keyboard;

import wtf.monsoon.api.event.EventTarget;
import wtf.monsoon.api.event.impl.EventPreMotion;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.impl.ModeSetting;
import wtf.monsoon.api.util.misc.Timer;
import net.minecraft.network.play.client.C03PacketPlayer;
import wtf.monsoon.api.Wrapper;

public class Phase extends Module {
	
	double yaw;
	double oldX;
	double oldZ;
	double d4;
	double d5;
	
	ModeSetting mode = new ModeSetting("Mode", this, "Hypixel", "Hypixel", "Packet", "Packetless");
	
	Timer timer = new Timer();
	
	public Phase() {
		super("Phase", "Phase through blocks", Keyboard.KEY_NONE, Category.PLAYER);
		this.addSettings(mode);
	}	
	
	public void onEnable() {
		super.onEnable();
		if(this.mode.is("Hypixel")) {
			Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY - 4, Wrapper.mc.thePlayer.posZ, true));
			Wrapper.mc.thePlayer.setPosition(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY - 4, Wrapper.mc.thePlayer.posZ);
			this.toggle();
		}
		
	}
	
	@EventTarget
	public void onMotion(EventPreMotion e) {

    	if(this.mode.is("Packet")) {
    		double strength = 0.6d;
			Wrapper.mc.thePlayer.stepHeight = 0;
			double mx = Math.cos(Math.toRadians(Wrapper.mc.thePlayer.rotationYaw + 90.0F));
			double mz = Math.sin(Math.toRadians(Wrapper.mc.thePlayer.rotationYaw + 90.0F));
			double x = (double) Wrapper.mc.thePlayer.movementInput.moveForward * strength * mx + (double) Wrapper.mc.thePlayer.movementInput.moveStrafe * strength * mz;
			double z = (double) Wrapper.mc.thePlayer.movementInput.moveForward * strength * mz - (double) Wrapper.mc.thePlayer.movementInput.moveStrafe * strength * mx;

			if (Wrapper.mc.thePlayer.isCollidedHorizontally && !Wrapper.mc.thePlayer.isOnLadder()) {
				Wrapper.mc.thePlayer.sendQueue.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX + x, Wrapper.mc.thePlayer.posY, Wrapper.mc.thePlayer.posZ + z, false));
				Wrapper.mc.thePlayer.sendQueue.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY + 3, Wrapper.mc.thePlayer.posZ, false));
				Wrapper.mc.thePlayer.setPosition(Wrapper.mc.thePlayer.posX + x, Wrapper.mc.thePlayer.posY, Wrapper.mc.thePlayer.posZ + z);
			}
		}
		if(this.mode.is("Packetless")) {
			double strength = 0.6d;
			Wrapper.mc.thePlayer.stepHeight = 0;
			double mx = Math.cos(Math.toRadians(Wrapper.mc.thePlayer.rotationYaw + 90.0F));
			double mz = Math.sin(Math.toRadians(Wrapper.mc.thePlayer.rotationYaw + 90.0F));
			double x = (double) Wrapper.mc.thePlayer.movementInput.moveForward * strength * mx + (double) Wrapper.mc.thePlayer.movementInput.moveStrafe * strength * mz;
			double z = (double) Wrapper.mc.thePlayer.movementInput.moveForward * strength * mz - (double) Wrapper.mc.thePlayer.movementInput.moveStrafe * strength * mx;

			if (Wrapper.mc.thePlayer.isCollidedHorizontally && !Wrapper.mc.thePlayer.isOnLadder()) {
				Wrapper.mc.thePlayer.setPosition(Wrapper.mc.thePlayer.posX + x, Wrapper.mc.thePlayer.posY, Wrapper.mc.thePlayer.posZ + z);
				Wrapper.mc.thePlayer.setPosition(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY, Wrapper.mc.thePlayer.posZ);
				Wrapper.mc.thePlayer.setPosition(Wrapper.mc.thePlayer.posX + x, Wrapper.mc.thePlayer.posY, Wrapper.mc.thePlayer.posZ + z);
			}
		}
    
	}
	
	
}
