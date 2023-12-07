package Velo.impl.Modules.combat;
import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.impl.Event.EventReceivePacket;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.ModeSetting;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity extends Module {
	
	public static ModeSetting mode = new ModeSetting("Mode", "Normal", "Normal", "Reverse", "Redesky");
	public static NumberSetting hmodify = new NumberSetting("Horizontal", 0, 0, 1, 0.01);
	public static NumberSetting vmodify = new NumberSetting("Vertical", 0, 0, 1, 0.01);
	
	public Velocity() {
		super("Velocity", "Velocity", Keyboard.KEY_NONE, Category.COMBAT);
		this.loadSettings(mode, hmodify, vmodify);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	public void onEventReceivePacket(EventReceivePacket event) {
		if(mode.equalsIgnorecase("Normal")) {
		
			if(vmodify.getValue() == 0 && hmodify.getValue() == 0) {
		if(event.packet instanceof S27PacketExplosion) {
			event.setCancelled(true);
			return;
		}
		}
	
		}
		if(event.getPacket() instanceof S12PacketEntityVelocity) {
			if(mode.equalsIgnorecase("Normal")) {
				S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();
				
				if(packet.entityID != mc.thePlayer.getEntityId()) {
					return;
				}
		
				if(vmodify.getValue() == 0 && hmodify.getValue() == 0) {
					event.setCancelled(true);
					return;
				}
				packet.motionX *= hmodify.getValue();
				packet.motionY *= vmodify.getValue();
				packet.motionZ *= hmodify.getValue();
			}
			if(mode.equalsIgnorecase("Redesky")) {
				S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();
				if(packet.entityID != mc.thePlayer.getEntityId()) {
					return;
				}
				packet.motionX = (int) 1.0E-10;
				packet.motionZ = (int) 1.0E-10;
				//mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(1.0E-50, mc.thePlayer.posY, 1.0E-50, true));
			}
			if(mode.equalsIgnorecase("Reverse")) {
				S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();
				if(packet.entityID != mc.thePlayer.getEntityId()) {
					return;
				}
				packet.motionX = -packet.motionX;
				packet.motionZ = -packet.motionZ;
			}
		}
		if(event.getPacket() instanceof S27PacketExplosion) {
			event.setCancelled(true);
		}
	}
	
	public void onUpdate(EventUpdate event) {
		this.setDisplayName("Velocity " + mode.modes.get(mode.index));
	}
}
