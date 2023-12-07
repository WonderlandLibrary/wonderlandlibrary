package Velo.impl.Modules.combat;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.impl.Event.EventSendPacket;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class Reach extends Module {
	
	
	public static NumberSetting distance = new NumberSetting("Distance", 0.4f, 0.1f, 1f, 0.1f);
	
	public Reach() {
		super("Reach", "Reach", Keyboard.KEY_NONE, Category.COMBAT);
		this.loadSettings(distance);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	
	
}
