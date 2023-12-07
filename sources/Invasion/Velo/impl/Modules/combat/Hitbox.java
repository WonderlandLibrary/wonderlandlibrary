package Velo.impl.Modules.combat;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.impl.Event.EventSendPacket;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class Hitbox extends Module {
	
	
	public static NumberSetting hitbox = new NumberSetting("Size", 0.4f, 0.1f, 5f, 0.1f);
	
	public Hitbox() {
		super("Hitbox", "Hitbox", Keyboard.KEY_NONE, Category.COMBAT);
		this.loadSettings(hitbox);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	
	
}
