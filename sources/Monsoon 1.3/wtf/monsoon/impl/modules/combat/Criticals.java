package wtf.monsoon.impl.modules.combat;

import org.lwjgl.input.Keyboard;


import net.minecraft.network.play.client.C03PacketPlayer;
import wtf.monsoon.Monsoon;
import wtf.monsoon.api.event.EventTarget;
import wtf.monsoon.api.event.impl.EventSendPacket;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;

public class Criticals extends Module {
	
	public Criticals() {
		super("Criticals", "Always land a critical hit.", Keyboard.KEY_NONE, Category.COMBAT);
	}
	
	public void onEnable() {
		mc.thePlayer.jump();
	}
	
	public void onDisable() {
		
	}

	@EventTarget
	public void onPacket(EventSendPacket e) {
		if (e.getPacket() instanceof C03PacketPlayer) {
			if(Monsoon.INSTANCE.manager.killAura.target != null) {
				C03PacketPlayer packet = (C03PacketPlayer) e.getPacket();
				packet.onGround = false;
			}
		}
	}
}
