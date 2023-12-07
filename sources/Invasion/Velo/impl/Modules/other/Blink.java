package Velo.impl.Modules.other;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.impl.Event.EventSendPacket;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.BooleanSetting;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Blink extends Module {
	
	public static boolean isEnabled = false;
	
	public static List<C03PacketPlayer> incomingpackets = new ArrayList<C03PacketPlayer>();
	
	public Blink() {
		super("Blink", "Blink", Keyboard.KEY_NONE, Category.OTHER);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		for(C03PacketPlayer p : incomingpackets) {
			mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(p);
		}
		incomingpackets.clear();
		isEnabled = false;
	}
	
	public void onUpdate(EventUpdate event) {
		isEnabled = true;
	}
	
	public void onEventSendPacket(EventSendPacket event) {
		if(event.getPacket() instanceof C03PacketPlayer) {
			C03PacketPlayer c03 = (C03PacketPlayer) event.getPacket();
			incomingpackets.add(c03);
			event.setCancelled(true);
		}
	}
}
