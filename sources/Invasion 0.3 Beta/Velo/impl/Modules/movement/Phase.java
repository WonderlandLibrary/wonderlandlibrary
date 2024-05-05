package Velo.impl.Modules.movement;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.impl.Event.EventSendPacket;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.ModeSetting;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Phase extends Module {
	
	int ticks = 0;
	
	
	
	public static ModeSetting M = new ModeSetting("Mode", "Redesky", "Redesky");
	public static List<C03PacketPlayer> incomingpackets = new ArrayList<C03PacketPlayer>();
	
	public Phase() {
		super("Phase", "Phase", Keyboard.KEY_NONE, Category.MOVEMENT);
		this.loadSettings(M);
	}
	
	public void onDisable() {
		mc.timer.timerSpeed = 1.0F;
		ticks = 0;
		incomingpackets.clear();
	}
	
	public void onUpdate(EventUpdate event) {
		ticks++;
		double oldPosY = mc.thePlayer.posY;
		double oldPosX = mc.thePlayer.posX;
		double oldPosZ = mc.thePlayer.posZ;
		
		mc.thePlayer.posY = mc.thePlayer.lastTickPosY;
		
		if(mc.thePlayer.isCollidedVertically) {
			mc.thePlayer.motionY = -0.42F;
			mc.timer.timerSpeed = 1F;
		} else {
			mc.timer.timerSpeed = 1F;
		}
		if(ticks > 7) {
			incomingpackets.clear();
			this.toggle();
		} else {
			mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(oldPosX, oldPosY - 0.00000001D, oldPosZ, true));
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.00000001D, mc.thePlayer.posZ);
			mc.thePlayer.motionY = -0.42F;
			//mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(oldPosX, oldPosY - 0.00000001D, oldPosZ, true));
		}
	}
	
	public void onEventSendPacket(EventSendPacket event) {
		if(event.getPacket() instanceof C03PacketPlayer) {
			C03PacketPlayer c03 = (C03PacketPlayer) event.getPacket();
		}
	}
}
