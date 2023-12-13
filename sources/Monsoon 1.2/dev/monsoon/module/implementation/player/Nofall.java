package dev.monsoon.module.implementation.player;

import dev.monsoon.event.listeners.EventMotion;
import org.lwjgl.input.Keyboard;

import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventUpdate;
import dev.monsoon.module.base.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
import dev.monsoon.module.enums.Category;

public class Nofall extends Module {

	int packetsent = 0;

	public Nofall() {
		super("Nofall", Keyboard.KEY_NONE, Category.PLAYER);
	}


	public void onEvent(Event e) {
		if(e instanceof EventMotion && e.isPre()) {
			if(mc.thePlayer.fallDistance > 3) {
				((EventMotion) e).setOnGround(true);
			}
		}
	}
	
}
