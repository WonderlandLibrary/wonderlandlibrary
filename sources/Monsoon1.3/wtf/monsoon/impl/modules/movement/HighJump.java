package wtf.monsoon.impl.modules.movement;

import org.lwjgl.input.Keyboard;

import wtf.monsoon.api.event.EventTarget;
import wtf.monsoon.api.event.impl.EventUpdate;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.impl.ModeSetting;
import wtf.monsoon.api.util.entity.DamageUtil;
import wtf.monsoon.api.util.entity.SpeedUtil;
import wtf.monsoon.api.util.misc.ServerUtil;
import wtf.monsoon.api.util.misc.Timer;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import wtf.monsoon.api.Wrapper;

public class HighJump extends Module {

	public Timer timer = new Timer();

	public ModeSetting mode = new ModeSetting("Mode", this, "Zoom", "Zoom", "Hypixel", "Mineplex", "Redesky");

	public HighJump() {
		super("HighJump", "ZOOOM", Keyboard.KEY_NONE, Category.MOVEMENT);
		this.addSettings(mode);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		timer.reset();
		if(mode.is("Mineplex")) {
			//DamageUtil.damageMethodOne();
			mc.thePlayer.motionY += 0.6;
			this.toggle();
		}
		if(mc.thePlayer.onGround && mode.is("Zoom")) {
			//NotificationManager.show(new Notification(NotificationType.INFO, "HighJump", "ZOOOOOOOOOOOOOOOOOOOOOOOOOOM", 2));
			mc.thePlayer.jump();
			mc.thePlayer.motionY +=1.75f;

			SpeedUtil.setSpeed(6.5f);
			mc.timer.timerSpeed = 1.0f;
			this.toggle();
		}

		if(mode.is("Redesky")) {
			mc.thePlayer.jump();
		}

		if(mode.is("Hypixel") && mc.thePlayer.onGround) {
			this.jumpButHigher();
		}

		//this.toggle();
	}

	public void onDisable() {
		super.onDisable();
		mc.timer.timerSpeed = 1F;
	}
	
	@EventTarget
	public void onUpdate(EventUpdate e) {
		if(mode.is("Hypixel")) {
			if(timer.hasTimeElapsed(400, false)) {
				this.toggle();
			}
		}
		if(mode.is("Redesky")) {
			mc.thePlayer.motionY *= 1.16f;
			SpeedUtil.setSpeed(0.45f);
			if(timer.hasTimeElapsed(700, false)) {
				mc.timer.timerSpeed = 1.3f;
			} else {
				mc.timer.timerSpeed = 0.8f;
			}
			if(timer.hasTimeElapsed(400, false) && mc.thePlayer.onGround) {
				this.toggle();
			}
		}
	}

	protected float func_175134_bD()
	{
		return 0.43F;
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
