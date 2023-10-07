package wtf.monsoon.impl.modules.movement;

import wtf.monsoon.Monsoon;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.*;
import org.lwjgl.input.Keyboard;

import wtf.monsoon.api.event.EventTarget;
import wtf.monsoon.api.event.impl.EventPreMotion;
import wtf.monsoon.api.event.impl.EventRenderPlayer;
import wtf.monsoon.api.event.impl.EventUpdate;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.impl.ModeSetting;
import wtf.monsoon.api.setting.impl.NumberSetting;
import wtf.monsoon.api.setting.impl.PlaceholderSetting;
import wtf.monsoon.api.util.entity.DamageUtil;
import wtf.monsoon.api.util.entity.SpeedUtil;
import wtf.monsoon.api.util.misc.PacketUtil;
import wtf.monsoon.api.util.misc.ServerUtil;
import wtf.monsoon.api.util.misc.Timer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;


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
		super("LongJump", "Jump longer", Keyboard.KEY_NONE, Category.MOVEMENT);
		this.addSettings(timeroflj,amount,mode);
		this.disableOnLagback = true;
	}

	@Override
	public void onEnable() {
		super.onEnable();
		timer.reset();
		if(mode.is("Mineplex")) {
			//DamageUtil.damageMethodOne();
			//mc.thePlayer.jump();
		}

		if(ServerUtil.isHypixel()) {
			//DamageUtil.damageMethodThree();
		}

		doneBow = false;
		if(mode.is("Hypixel") || mode.is("Mineplex")) {
			selfBow();
		}

		lastSlot = -1;
		oldPosY = mc.thePlayer.posY;
		yPos = mc.thePlayer.posY;
		distanceX = mc.thePlayer.posX;
		distanceZ = mc.thePlayer.posZ;

	}

	public void onDisable() {
		super.onDisable();
		//Monsoon.blink.setEnabled(false);
		//mc.thePlayer.setSpeed(0);
		//if(mode.is("Mineplex")) DamageUtil.damageMethodOne();
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
		mc.thePlayer.setVelocity(0, 0, 0);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate e) {

		if(mode.is("Mineplex")) {
			//amount.setValue(0.275);
		}
		if(!mode.is("Redesky") && !mode.is("Hypixel") && !mode.is("Mineplex"))
			mc.thePlayer.jumpMovementFactor = (float) amount.getValue();
		if(mc.thePlayer.onGround && !mode.is("Mineplex") && !mode.is("Redesky") && !mode.is("Hypixel")) {
			mc.thePlayer.jump();
			mc.timer.timerSpeed = (float) timeroflj.getValue();
		}
		if(mode.is("Normal")) {
			mc.thePlayer.motionX *= 0.80F;
			mc.thePlayer.motionY += 0.007F;
			mc.thePlayer.motionZ *= 0.80F;
			//mc.thePlayer.ticksExisted = 6;
		}
		if(mode.is("Mineplex")) {
			if(doneBow && mc.thePlayer.hurtTime > 0) {

				mc.gameSettings.keyBindForward.pressed = true;

			} else if(doneBow && !timer.hasTimeElapsed(1400, false)) {
				mc.gameSettings.keyBindBack.pressed = false;
				mc.gameSettings.keyBindForward.pressed = false;
				mc.gameSettings.keyBindRight.pressed = false;
				mc.gameSettings.keyBindLeft.pressed = false;
			}
			if(timer.hasTimeElapsed(1700, false) && mc.thePlayer.onGround && doneBow) {
				this.toggle();
			}
		}
		if (mode.is("Hypixel")) {
			if(doneBow && mc.thePlayer.hurtTime > 0) {

				mc.gameSettings.keyBindForward.pressed = true;

			} else if(doneBow && !timer.hasTimeElapsed(1400, false)) {
				mc.gameSettings.keyBindBack.pressed = false;
				mc.gameSettings.keyBindForward.pressed = false;
				mc.gameSettings.keyBindRight.pressed = false;
				mc.gameSettings.keyBindLeft.pressed = false;
			}
			if(timer.hasTimeElapsed(1700, false) && mc.thePlayer.onGround && doneBow) {
				this.toggle();
			}
		}
		if(!mode.is("Hypixel") && !mode.is("Mineplex")) {
			if ((mc.thePlayer.onGround && timer.hasTimeElapsed(200, false)) || (mc.thePlayer.isCollidedHorizontally && timer.hasTimeElapsed(200, false))) {
				if (!mode.is("Mineplex") && !mode.is("Hypixel")) {
					this.toggle();
				}
			}
		}
		if((mc.thePlayer.onGround && mineplexTimer.hasTimeElapsed(900, false)) || (mc.thePlayer.isCollidedHorizontally && mineplexTimer.hasTimeElapsed(900, false))) {
			//DamageUtil.damageMethodOne();
			//this.toggle();
		}
	
	}
	
	@EventTarget
	public void onPreMotion(EventPreMotion e) {
		if (mode.is("Hypixel")) {
			Monsoon.sendMessage(mc.thePlayer.hurtTime + "");
			if(doneBow && mc.thePlayer.hurtTime > 0) {
				if (mc.thePlayer.onGround) {
					mc.thePlayer.jump();
					mc.thePlayer.jumpMovementFactor = 0.03f;
				} else if (mc.thePlayer.motionY > 0.0) {
					mc.thePlayer.motionX *= 1.10049999856948853;
					mc.thePlayer.motionZ *= 1.10049999856948853;
					mc.thePlayer.motionY += 0.028099999910593033;
					mc.thePlayer.speedInAir = 0.04f;
				} else {
					mc.thePlayer.speedInAir = 0.03f;
				}
			}
		}

		if (mode.is("Mineplex")) {
			Monsoon.sendMessage(mc.thePlayer.hurtTime + "");
			if(doneBow && mc.thePlayer.hurtTime > 0) {
				if (mc.thePlayer.onGround) {
					mc.thePlayer.jump();
					mc.thePlayer.jumpMovementFactor = 0.03f;
				} else if (mc.thePlayer.motionY > 0.0) {
					mc.thePlayer.motionX *= 1.30049999856948853;
					mc.thePlayer.motionZ *= 1.30049999856948853;
					mc.thePlayer.motionY += 0.028099999910593033;
					mc.thePlayer.speedInAir = 0.04f;
				} else {
					mc.thePlayer.speedInAir = 0.03f;
				}
			}
		}
	}
	
	@EventTarget
	public void onRenderPlayer(EventRenderPlayer e) {
		if(!doneBow && mode.is("Hypixel")) {
			e.setPitch(-90);
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
					Thread.sleep(160);
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
