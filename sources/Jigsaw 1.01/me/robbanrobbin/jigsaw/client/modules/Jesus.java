package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.events.PreMotionEvent;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.init.MobEffects;

public class Jesus extends Module {

	public static boolean enabled;
	private double moveSpeed;
	private WaitTimer timer = new WaitTimer();

	public Jesus() {
		super("Jesus", Keyboard.KEY_NONE, Category.MOVEMENT, "Allows you to walk on liquids.");
	}

	@Override
	public void onDisable() {
		enabled = false;
		super.onDisable();
	}

	@Override
	public void onEnable() {
		timer.reset();
		enabled = true;
		super.onEnable();
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		enabled = !(mc.gameSettings.keyBindSneak.pressed || mc.player.isInWater() || mc.player.isInLava());
		
		if (Utils.isPlayerOnLiqud() && timer.hasTimeElapsed(100, false) && enabled) {
			event.y -= 0.005;
			timer.reset();
		}
		else if(!Utils.isPlayerOnLiqud()) {
			timer.reset();
		}
		
		if(!mc.player.isMovingXYZ(0.01)) {
			event.cancel();
		}
		super.onUpdate(event);
	}

	@Override
	public void onPreMotion(PreMotionEvent event) {
		if(!enabled && (mc.player.isInWater() || mc.player.isInLava()) && !mc.gameSettings.keyBindSneak.pressed) {
			event.y = 0.1;
			mc.player.motionY = 0;
		}
		super.onPreMotion(event);
	}

	@Override
	public String[] getModes() {
		return super.getModes();
	}

	@Override
	public String getAddonText() {
		return super.getAddonText();
	}

	private double getBaseMoveSpeed() {
		double baseSpeed = 0.2873D;
		if (mc.player.isPotionActive(MobEffects.SPEED)) {
			int amplifier = mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
			baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
		}
		return baseSpeed;
	}

}
