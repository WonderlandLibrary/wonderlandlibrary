package dev.monsoon.module.implementation.movement;

import dev.monsoon.event.listeners.EventMotion;
import dev.monsoon.module.setting.impl.ModeSetting;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventUpdate;
import dev.monsoon.module.base.Module;
import dev.monsoon.module.enums.Category;

public class Step extends Module {

	public ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "Motion", "Mineplex", "NCP");

	public Step() {
		super("Step", Keyboard.KEY_NONE, Category.MOVEMENT);
		this.addSettings(mode);
	}
	
	@Override
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(this.mode.is("Normal")) {
				mc.thePlayer.stepHeight = 1F;
			}
			if(mode.is("Mineplex")) {
				float[] stepValues = new float[]{0.42F, 0.75F, 1.09F};
				for (float value : stepValues) {
					mc.thePlayer.sendQueue.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + value, mc.thePlayer.posZ, false));
				}
			}

			if(mode.is("Mineplex")) {
				float[] stepValues = new float[]{0.25F, 0.15F};
				for (float value : stepValues) {
					mc.thePlayer.sendQueue.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + value, mc.thePlayer.posZ, false));
				}
			}

			this.setSuffix(mode.getValueName());
		}
		if (e instanceof EventMotion && e.isPre()) {
			if(this.mode.is("Motion")) {
				if(mc.thePlayer.isCollidedHorizontally) {
					mc.thePlayer.motionY = 0.2f;
				}
			}
		}
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		if(this.mode.is("Normal")) {
			mc.thePlayer.stepHeight = 0.6F;
		}
	}

}
