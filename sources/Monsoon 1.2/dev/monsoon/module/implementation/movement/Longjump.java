package dev.monsoon.module.implementation.movement;

import dev.monsoon.event.listeners.EventMotion;
import dev.monsoon.event.listeners.EventRenderPlayer;
import dev.monsoon.module.setting.impl.ModeSetting;
import dev.monsoon.module.setting.impl.PlaceholderSetting;
import dev.monsoon.util.entity.DamageUtil;
import dev.monsoon.util.entity.MotionUtils;
import dev.monsoon.util.entity.SpeedUtil;
import dev.monsoon.util.misc.PacketUtil;
import dev.monsoon.util.misc.ServerUtil;
import dev.monsoon.util.misc.Timer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventUpdate;
import dev.monsoon.module.base.Module;
import dev.monsoon.module.setting.impl.NumberSetting;
import dev.monsoon.module.enums.Category;

public class Longjump extends Module {

	public NumberSetting timeroflj = new NumberSetting("Timer", 1, 0.1, 10, 0.1, this);
	public NumberSetting amount = new NumberSetting("Amount", 0.285,  0.01, 2, 0.001, this);
	public PlaceholderSetting placeholder1 = new PlaceholderSetting("Test", this);
	public ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "Mineplex", "Hypixel");
	public Timer timer = new Timer(), mineplexTimer = new Timer();

	public static transient int lastSlot = -1;

	protected boolean boosted = false, doneBow = false;
	protected double startY = 0;
	protected double motionVa = 2.8;

	double distanceX = 0;
	double distanceZ = 0;
	double oldPosY = 0;
	double yPos = 0;

	public Longjump() {
		super("LongJump", Keyboard.KEY_NONE, Category.MOVEMENT);
		this.addSettings(timeroflj,amount,mode);
		this.disableOnLagback = true;
	}

	@Override
	public void onEnable() {
		timer.reset();
		if(ServerUtil.isMineplex() || mode.is("Mineplex")) {
			//DamageUtil.damageMethodOne();
			//mc.thePlayer.posY -= 4;
			//mc.timer.timerSpeed = 0.1f;
		}

		if(ServerUtil.isHypixel()) {
			DamageUtil.damageMethodThree();
		}

		doneBow = false;
		if(mode.is("Hypixel")) {
			selfBow();
		}

		lastSlot = -1;
		oldPosY = mc.thePlayer.posY;
		yPos = mc.thePlayer.posY;
		distanceX = mc.thePlayer.posX;
		distanceZ = mc.thePlayer.posZ;

	}

	public void onDisable() {
		//Monsoon.blink.setEnabled(false);
		//mc.thePlayer.setSpeed(0);
		mc.timer.timerSpeed = 1F;
		lastSlot = -1;
		mc.thePlayer.speedInAir = 0.02F;
		this.boosted = false;
		this.motionVa = 2.8;
		this.mc.thePlayer.motionY = 0;
		mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
		mc.thePlayer.capabilities.isFlying = false;
		mc.thePlayer.capabilities.allowFlying = false;
		mc.timer.timerSpeed = 1F;
		mc.thePlayer.speedInAir = 0.02F;
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(ServerUtil.isMineplex()|| mode.is("Mineplex")) {
				amount.setValue(0.275);
			}
			if(!mode.is("Redesky") && !mode.is("Hypixel"))
				mc.thePlayer.jumpMovementFactor = (float) amount.getValue();
			if(mc.thePlayer.onGround && !(ServerUtil.isMineplex()|| mode.is("Mineplex")) && !mode.is("Redesky") && !mode.is("Hypixel")) {
				mc.thePlayer.jump();
				mc.timer.timerSpeed = (float) timeroflj.getValue();
			}
			if(mode.is("Normal")) {
				mc.thePlayer.motionX *= 0.80F;
				mc.thePlayer.motionY += 0.007F;
				mc.thePlayer.motionZ *= 0.80F;
				//mc.thePlayer.ticksExisted = 6;
			} else if(mode.is("Mineplex")) {
				float hSpeed = 0.26F;
				if(!timer.hasTimeElapsed(600, false)) {
					BlockPos blockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY - 1.0D, mc.thePlayer.posZ);
					Vec3 vec = new Vec3(blockPos.x, blockPos.y, blockPos.z).addVector(0.4000000059604645D, 0.4000000059604645D, 0.4000000059604645D);
					mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, null, blockPos, EnumFacing.UP, new Vec3(vec.xCoord * 0.4000000059604645D, vec.yCoord * 0.4000000059604645D, vec.zCoord * 0.4000000059604645D));
					hSpeed += 0.095D;
					MotionUtils.setMotion(mc.thePlayer.ticksExisted % 2 == 0 ? -hSpeed : hSpeed);
					if (hSpeed >= 3.5) {
						MotionUtils.setMotion(0.0F);
						mc.thePlayer.motionY = mc.thePlayer.motionY = 0.42F;
						return;
					}
				} else {
					if (mc.thePlayer.onGround) {
						mc.thePlayer.jump();
						SpeedUtil.setSpeed(0.1F);
					}
					PacketUtil.sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
					//VanillaSpeed.setSpeed((float) (mineplexSpeed *= 0.979));
					if (mc.thePlayer.motionY > 0) {
						mc.thePlayer.motionY *= 1.001F;
					}
					SpeedUtil.setSpeed(0.35F);
					if (mc.thePlayer.motionY < -0.05) {
						//mc.thePlayer.motionY += 0.001F;
					} else if (mc.thePlayer.motionY < 0.01 && mc.thePlayer.motionY > -0.01) {
						mc.thePlayer.motionY += 0.01F;
					}
				}

			}
			if (mode.is("Hypixel")) {
				//Monsoon.sendMessage(doneBow + "");
				if(doneBow && timer.hasTimeElapsed(800, false)) {
					if(mc.thePlayer.onGround) {
						mc.thePlayer.jump();
						mineplexTimer.reset();
					}


					mc.gameSettings.keyBindForward.pressed = true;

					if(timer.hasTimeElapsed(1900, false) && mc.thePlayer.onGround) {
						this.toggle();
					}
				} else if(doneBow && !timer.hasTimeElapsed(1400, false)) {
					mc.gameSettings.keyBindBack.pressed = false;
					mc.gameSettings.keyBindForward.pressed = false;
					mc.gameSettings.keyBindRight.pressed = false;
					mc.gameSettings.keyBindLeft.pressed = false;
				}
			}
			if(!mode.is("Hypixel")) {
				if ((mc.thePlayer.onGround && timer.hasTimeElapsed(200, false)) || (mc.thePlayer.isCollidedHorizontally && timer.hasTimeElapsed(200, false))) {
					if (!ServerUtil.isMineplex() && !mode.is("Mineplex") && !mode.is("Hypixel")) {
						this.toggle();
					}
				}
			}
			if((mc.thePlayer.onGround && mineplexTimer.hasTimeElapsed(900, false)) || (mc.thePlayer.isCollidedHorizontally && mineplexTimer.hasTimeElapsed(900, false))) {
				//DamageUtil.damageMethodOne();
				//this.toggle();
			}
		}
		if(e instanceof EventMotion) {
			if (mode.is("Hypixel")) {
				if(doneBow && timer.hasTimeElapsed(800, false)) {
					if (mc.thePlayer.onGround) {
						mc.thePlayer.jump();
					}

					mc.timer.timerSpeed = 1.1f;
					//mc.thePlayer.setMotion(0.60f, 0.02, 0.60f);
					mc.thePlayer.motionY += 0.03;
					SpeedUtil.setSpeed(0.60f);
					if(timer.hasTimeElapsed(1800, false))
						this.toggle();
				}
			}
		}
		if(e instanceof EventRenderPlayer) {
			if(!doneBow) {
				((EventRenderPlayer) e).setPitch(-90);
			}
		}
	}

	public void selfBow() {
		Timer fuck = new Timer();
		fuck.reset();
		int oldSlot = mc.thePlayer.inventory.currentItem;

		mc.gameSettings.keyBindBack.pressed = false;
		mc.gameSettings.keyBindForward.pressed = false;
		mc.gameSettings.keyBindRight.pressed = false;
		mc.gameSettings.keyBindLeft.pressed = false;
		Thread thread = new Thread(){
			public void run(){
				int oldSlot = mc.thePlayer.inventory.currentItem;
				ItemStack block = mc.thePlayer.getCurrentEquippedItem();

				if (block != null) {
					block = null;
				}
				int slot = mc.thePlayer.inventory.currentItem;
				for (short g = 0; g < 9; g++) {

					if (mc.thePlayer.inventoryContainer.getSlot(g + 36).getHasStack()
							&& mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().getItem() instanceof ItemBow
							&& mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().stackSize != 0
							&& (block == null
							|| (block.getItem() instanceof ItemBow))) {

						slot = g;
						block = mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack();

					}

				}

				PacketUtil.sendPacket(new C09PacketHeldItemChange(slot));
				mc.thePlayer.inventory.currentItem = slot;
				mc.gameSettings.keyBindUseItem.pressed = true;
				try {
					Thread.sleep(130);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				PacketUtil.sendPacket(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, -90, true));
				mc.gameSettings.keyBindUseItem.pressed = false;

				try {
					Thread.sleep(180);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				doneBow = true;

				PacketUtil.sendPacket(new C09PacketHeldItemChange(oldSlot));
				mc.thePlayer.inventory.currentItem = oldSlot;
			}
		};

		thread.start();

		mc.gameSettings.keyBindBack.pressed = false;
		mc.gameSettings.keyBindForward.pressed = false;
		mc.gameSettings.keyBindRight.pressed = false;
		mc.gameSettings.keyBindLeft.pressed = false;

		PacketUtil.sendPacket(new C09PacketHeldItemChange(oldSlot));
		mc.thePlayer.inventory.currentItem = oldSlot;
	}

}
