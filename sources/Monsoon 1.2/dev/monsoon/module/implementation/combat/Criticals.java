package dev.monsoon.module.implementation.combat;

import dev.monsoon.Monsoon;
import org.lwjgl.input.Keyboard;

import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventPacket;
import dev.monsoon.module.base.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
import dev.monsoon.module.enums.Category;

public class Criticals extends Module {
	
	public Criticals() {
		super("Criticals", Keyboard.KEY_NONE, Category.COMBAT);
	}
	
	public void onEnable() {
		mc.thePlayer.jump();
	}
	
	public void onDisable() {
		
	}
	
	public void onEvent(Event e) {
        if(e instanceof EventPacket) {
            EventPacket event = (EventPacket)e;
            if (event.getPacket() instanceof C03PacketPlayer) {
            	if(Monsoon.killAura.target != null) {
            		C03PacketPlayer packet = (C03PacketPlayer) event.getPacket();
            		packet.onground = false;
            	}
            }
        }
	}
}
