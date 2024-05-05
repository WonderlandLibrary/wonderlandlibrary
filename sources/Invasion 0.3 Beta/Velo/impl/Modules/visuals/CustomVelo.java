package Velo.impl.Modules.visuals;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.impl.Event.EventPostMotion;
import Velo.impl.Event.EventPreMotion;
import Velo.impl.Event.EventSendPacket;
import Velo.impl.Settings.BooleanSetting;
import net.minecraft.network.play.client.C03PacketPlayer;

public class CustomVelo extends Module {
	
	public static BooleanSetting pitchrotation = new BooleanSetting("Pitch Rotation", true);
	
	public static boolean isEnabled = false;
	
	public static float yaw = 0, pitch = 0, lastTickPitch = 0;
	
	public CustomVelo() {
		super("CustomVelo", "CustomVelo", Keyboard.KEY_NONE, Category.VISUALS);
		this.loadSettings(pitchrotation);
	}
	
	public void onDisable() {
		isEnabled = false;
	}
	
	public void onPostMotionUpdate(EventPostMotion event) {
		this.setDisplayName("F5 Rotation");
		this.setName("F5 Rotation");
		isEnabled = true;
		if(mc.thePlayer.isBlocking()) {
			//mc.thePlayer.swingItem();
		}
	}
	
	public void onEventSendPacket(EventSendPacket event) {
		if(event.getPacket() instanceof C03PacketPlayer) {
			C03PacketPlayer c03 = (C03PacketPlayer) event.getPacket();
		}
	}
}
