package Velo.impl.Modules.player;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.impl.Event.EventSendPacket;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.ModeSetting;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NoFall extends Module {
	
	public static ModeSetting mode = new ModeSetting("Mode", "Packet", "Packet", "NoGround", "SpoofGround");
	
	public NoFall() {
		super("NoFall", "NoFall", Keyboard.KEY_NONE, Category.PLAYER);
		this.loadSettings(mode);
	}
	
	public void onUpdate(EventUpdate event) {
		if(mode.equalsIgnorecase("Packet")) {
			if(mc.thePlayer.fallDistance > 2) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
			}
		}
	}
	
	public void onEventSendPacket(EventSendPacket event) {
		if(mode.equalsIgnorecase("NoGround")) {
			if(event.getPacket() instanceof C03PacketPlayer) {
				C03PacketPlayer c03 = (C03PacketPlayer) event.getPacket();
				c03.onGround = false;
			}
		}
		if(mode.equalsIgnorecase("SpoofGround")) {
			if(event.getPacket() instanceof C03PacketPlayer) {
				C03PacketPlayer c03 = (C03PacketPlayer) event.getPacket();
				if(mc.thePlayer.fallDistance > 2) {
					c03.onGround = true;
				}
			}
		}
	}
}
