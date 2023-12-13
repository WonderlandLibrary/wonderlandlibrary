package wtf.monsoon.impl.modules.movement;

import org.lwjgl.input.Keyboard;

import wtf.monsoon.api.event.EventTarget;
import wtf.monsoon.api.event.impl.EventPreMotion;
import wtf.monsoon.api.event.impl.EventUpdate;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.impl.ModeSetting;
import net.minecraft.network.play.client.C03PacketPlayer;
import wtf.monsoon.api.Wrapper;
import wtf.monsoon.api.util.misc.PacketUtil;


public class Step extends Module {

	public ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "Motion", "Mineplex", "NCP");

	public Step() {
		super("Step", "Step higher", Keyboard.KEY_NONE, Category.MOVEMENT);
		this.addSettings(mode);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate e) {

		if(this.mode.is("Normal")) {
			Wrapper.mc.thePlayer.stepHeight = 1F;
		}
		if(mode.is("Mineplex")) {
			float[] stepValues = new float[]{0.42F, 0.75F, 1.09F};
			for (float value : stepValues) {
				Wrapper.mc.thePlayer.sendQueue.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY + value, Wrapper.mc.thePlayer.posZ, false));
			}
		}

		if(mode.is("NCP")) {
			float realHeight = mc.thePlayer.stepHeight;
			if (mc.thePlayer.isCollidedHorizontally) {
				float[] stepValues = new float[]{0.42F, 0.75F};
				for (float value : stepValues) {
					PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + value, mc.thePlayer.posZ, false));
				}
			}
		}

		this.setSuffix(mode.getValueName());
	
	}
	
	@EventTarget
	public void onPreMotion(EventPreMotion e) {
		if(this.mode.is("Motion")) {
			if(Wrapper.mc.thePlayer.isCollidedHorizontally) {
				Wrapper.mc.thePlayer.motionY = 0.2f;
			}
		}
	}
	
	
	@Override
	public void onDisable() {
		super.onDisable();
		if(this.mode.is("Normal")) {
			if(Wrapper.mc.thePlayer != null)
				Wrapper.mc.thePlayer.stepHeight = 0.6F;
		}
	}

}
