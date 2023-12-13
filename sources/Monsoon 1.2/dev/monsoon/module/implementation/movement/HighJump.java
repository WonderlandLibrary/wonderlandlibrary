package dev.monsoon.module.implementation.movement;

import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventMotion;
import dev.monsoon.event.listeners.EventUpdate;
import dev.monsoon.module.base.Module;
import dev.monsoon.notification.Notification;
import dev.monsoon.notification.NotificationManager;
import dev.monsoon.notification.NotificationType;
import dev.monsoon.module.setting.impl.ModeSetting;
import dev.monsoon.util.entity.DamageUtil;
import dev.monsoon.util.entity.SpeedUtil;
import dev.monsoon.util.misc.ServerUtil;
import dev.monsoon.util.misc.Timer;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import dev.monsoon.module.enums.Category;

public class HighJump extends Module {

	public Timer timer = new Timer();

	public ModeSetting mode = new ModeSetting("Mode", this, "Zoom", "Redesky", "Zoom", "Hypixel");

	public HighJump() {
		super("HighJump", Keyboard.KEY_NONE, Category.MOVEMENT);
		this.addSettings(mode);
	}

	@Override
	public void onEnable() {
		timer.reset();
		if(ServerUtil.isMineplex()) {
			//DamageUtil.damageMethodOne();
		}
		if(mc.thePlayer.onGround && mode.is("Zoom")) {
			//NotificationManager.show(new Notification(NotificationType.INFO, "HighJump", "ZOOOOOOOOOOOOOOOOOOOOOOOOOOM", 2));
			mc.thePlayer.jump();
			mc.thePlayer.motionY +=2.75f;

			SpeedUtil.setSpeed(6.5f);
			mc.timer.timerSpeed = 1.0f;
			this.toggle();
		}

		if(mode.is("Hypixel") && mc.thePlayer.onGround) {
			this.jumpButHigher();
		}

		//this.toggle();
	}

	public void onDisable() {
		mc.timer.timerSpeed = 1F;
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventMotion && e.isPre()) {
			if(mode.is("Redesky")) {
				if(mc.thePlayer.onGround) {
					mc.thePlayer.motionY = 1.1F;
				} else {
					if(mc.thePlayer.motionY > 0) {
						if(mc.thePlayer.fallDistance > 2.5) {
							mc.thePlayer.motionY *= 1.004F;
						} else {
							mc.thePlayer.motionY *= 1.0005F;
						}

						mc.thePlayer.motionX *= 1.0025F;
						mc.thePlayer.motionZ *= 1.0025F;
					} else {
						mc.timer.timerSpeed = 1F;
					}

				}

			}
		}
		if(e instanceof EventUpdate) {
			if(mode.is("Hypixel")) {
				if(timer.hasTimeElapsed(400, false)) {
					this.toggle();
				}
			}
		}
	}

	protected float func_175134_bD()
	{
		return 0.46F;
	}

	protected void jumpButHigher()
	{
		mc.thePlayer.motionY = (double)this.func_175134_bD();

		if (mc.thePlayer.isPotionActive(Potion.jump))
		{
			mc.thePlayer.motionY += (double)((float)(mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
		}

		if (mc.thePlayer.isSprinting())
		{
			float var1 = mc.thePlayer.rotationYaw * 0.017453292F;
			mc.thePlayer.motionX -= (double)(MathHelper.sin(var1) * 0.2F);
			mc.thePlayer.motionZ += (double)(MathHelper.cos(var1) * 0.2F);
		}

		mc.thePlayer.isAirBorne = true;
	}
}
