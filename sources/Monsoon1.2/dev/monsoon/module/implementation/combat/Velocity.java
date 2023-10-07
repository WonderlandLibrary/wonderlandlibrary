package dev.monsoon.module.implementation.combat;

import dev.monsoon.util.misc.ServerUtil;
import org.lwjgl.input.Keyboard;

import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventPacket;
import dev.monsoon.module.base.Module;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import dev.monsoon.module.enums.Category;

public class Velocity extends Module {
	
	public Velocity() {
		super("Velocity", Keyboard.KEY_NONE, Category.COMBAT);
		this.addSettings();
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventPacket) {
			if(e.isIncoming()) {
				EventPacket event = (EventPacket)e;

				if (event.packet instanceof S12PacketEntityVelocity ) {
					//S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.packet;
					//packet.setMotionX((int) ((packet.func_149411_d() / 100) * 0));
					//packet.setMotionY((int) ((packet.func_149410_e() / 100) * 0));
					//packet.setMotionZ((int) ((packet.func_149409_f() / 100) * 0));
					e.setCancelled(true);
					if(ServerUtil.isRedesky() && mc.thePlayer.onGround) {
						mc.thePlayer.jump();
					}

				}
				else if (event.packet instanceof S27PacketExplosion) {
					e.setCancelled(true);
				}
			}
		}
	}
}
