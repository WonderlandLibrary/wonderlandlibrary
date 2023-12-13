package wtf.monsoon.impl.modules.combat;

import org.lwjgl.input.Keyboard;

import wtf.monsoon.api.event.EventTarget;
import wtf.monsoon.api.event.impl.EventPostMotion;
import wtf.monsoon.api.event.impl.EventPreMotion;
import wtf.monsoon.api.event.impl.EventReceivePacket;
import wtf.monsoon.api.event.impl.EventUpdate;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import wtf.monsoon.api.setting.impl.ModeSetting;

public class Velocity extends Module {

	public ModeSetting mode = new ModeSetting("Mode", this, "Packet", "Packet", "Motion", "Redesky");
	
	public Velocity() {
		super("Velocity", "Kill entities around you", Keyboard.KEY_NONE, Category.COMBAT);
		this.addSettings(mode);
	}
	
	@EventTarget
	public void onPacket(EventReceivePacket e) {
		if(mode.is("Packet")) {
			if (e.getPacket() instanceof S12PacketEntityVelocity) {
				e.setCancelled(true);
			}
			if (e.getPacket() instanceof S27PacketExplosion) {
				e.setCancelled(true);
			}
		}
	}

	@EventTarget
	public void onPreMotion(EventPreMotion e) {

	}

	@EventTarget
	public void onPostMotion(EventPostMotion e) {
		if(mode.is("Redesky")) {
			if (mc.thePlayer.hurtTime == mc.thePlayer.maxHurtTime && mc.thePlayer.maxHurtTime > 0) {
				mc.thePlayer.motionX *= 0.35;
				mc.thePlayer.motionY *= 0;
				mc.thePlayer.motionZ *= 0.35;
			}
		}
	}

	@EventTarget
	public void onUpdate(EventUpdate e) {
		if(mode.is("Motion")) {
			if (mc.thePlayer.hurtTime == mc.thePlayer.maxHurtTime && mc.thePlayer.maxHurtTime > 0) {
				mc.thePlayer.motionX *= 0;
				mc.thePlayer.motionY *= 0;
				mc.thePlayer.motionZ *= 0;
			}
		}
	}
	

}
