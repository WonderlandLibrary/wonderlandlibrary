package wtf.monsoon.impl.modules.movement;

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;

import wtf.monsoon.Monsoon;
import wtf.monsoon.api.event.EventTarget;
import wtf.monsoon.api.event.impl.*;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.impl.BooleanSetting;
import wtf.monsoon.api.setting.impl.ModeSetting;
import wtf.monsoon.api.setting.impl.NumberSetting;
import wtf.monsoon.api.util.entity.DamageUtil;
import wtf.monsoon.api.util.entity.MovementUtil;
import wtf.monsoon.api.util.entity.SpeedUtil;
import wtf.monsoon.api.util.misc.PacketUtil;
import wtf.monsoon.api.util.misc.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import wtf.monsoon.api.util.world.WorldUtil;
import wtf.monsoon.impl.ui.notification.Notification;
import wtf.monsoon.impl.ui.notification.NotificationManager;
import wtf.monsoon.impl.ui.notification.NotificationType;

public class Fly extends Module {

	Timer timer = new Timer();
	Timer blinkTimer = new Timer();
	Timer mineplexTimer = new Timer();
	private long mptime;
	private float hSpeed;
	private float ySpeed;

	private boolean done, back;

	public ModeSetting mode = new ModeSetting("Mode", this,"Vanilla", "Vanilla", "Hypixel", "PearlHypixel", "MineplexDev", "ACRBow", "Packet", "bridger.land", "Airwalk", "VerusFast", "VerusInf", "VerusJump", "SurvivalDub", "SpacePotato");
	public BooleanSetting disableOnDeath = new BooleanSetting("Disable on death", true, this);
	public ModeSetting damageMode = new ModeSetting("Damage mode", this,"None", "None", "Verus", "One", "Two", "Three");
	public NumberSetting health = new NumberSetting("Damage Amount", 1, 1, 20, 1, this, false);
	public NumberSetting speed = new NumberSetting("Speed", 2.2, 0.1, 15, 0.1, this);
	public ModeSetting blink = new ModeSetting("Blink mode", this,"None", "None", "Normal", "Pulse");
	public NumberSetting pulseDelay = new NumberSetting("Pulse Delay", 200, 5, 3000, 1, this, false);

	private boolean clicked, hasBlinkedMineplex = false, doneBow = false;
	public static transient int lastSlot = -1;
	private ArrayList<Packet> savedPackets = new ArrayList<Packet>();
	float multiplier = 1.0F;
	int boostTicks = 0;

	public Fly() {
		super("Fly", "Fly very fast!", Keyboard.KEY_NONE, Category.MOVEMENT);
		this.addSettings(mode,speed,damageMode,health,blink,pulseDelay);
	}

	public void onEnable() {
		super.onEnable();

		hSpeed = (float) mc.thePlayer.posY;

		if(mc.thePlayer.getHealth() > 2 && !mode.is("Hypixel") && !mode.is("VerusFast")) {
			switch (damageMode.getMode()) {
				case "One":
					DamageUtil.damageMethodOne();
					break;
				case "Two":
					DamageUtil.damageMethod2(health.getValue());
					break;
				case "Three":
					DamageUtil.damageMethodThree();
					break;
				case "Verus":
					DamageUtil.damageVerus();
					break;
				case "None":

					break;
			}
		}

		this.multiplier = 0.0F;

		if(mode.is("Hypixel")) {
			mc.thePlayer.jump();
			DamageUtil.damageMethodThree();
		}

		mineplexTimer.reset();
		mptime = 500;

		hSpeed = 0.26F;
		done = false;
		hasBlinkedMineplex = false;
		blinkTimer.reset();
		if(mode.is("Mineplex")) {
			//DamageUtil.damageMethodOne();
			//Monsoon.manager.blink.setEnabled(true);
		}

		doneBow = false;

		if(mode.is("ACRBow")) {
			selfBow();
		}
		//mc.thePlayer.jump();
		if(this.mode.is("Test"))
			damageHypixel(1);
		clicked = false;
		lastSlot = -1;
		if(blink.is("Normal") || mode.is("RedeskyBlink")) {
			mc.thePlayer.jump();
			Monsoon.INSTANCE.manager.blink.setEnabled(true);
		}
		if(mode.is("VerusFast")) {
			if(!mc.thePlayer.onGround) {
				NotificationManager.show(new Notification(NotificationType.INFO, "Verus Fly", "You need to be on the ground!", 1));
				this.toggle();
			}
			boostTicks = 0;
			DamageUtil.damageVerus();
		}
		timer.reset();
		if (mode.is("Redesky") || mode.is("RedeSkyFast")) {
			mc.thePlayer.jump();
			mc.thePlayer.addVelocity(0.0, 0.5, 0.0);
		}
		 if (this.mode.is("RedeSkyFast") || this.mode.is("RedeSky")) {
	            mc.thePlayer.addVelocity(0.0, 0.5, 0.0);
	        }
		 if (this.mode.is("PearlHypixel")) {
	            int oldSlot = mc.thePlayer.inventory.currentItem;
	            ItemStack block = mc.thePlayer.getCurrentEquippedItem();

				if (block != null) {
					block = null;
				}
				int slot = mc.thePlayer.inventory.currentItem;
				for (short g = 0; g < 9; g++) {

					if (mc.thePlayer.inventoryContainer.getSlot(g + 36).getHasStack()
							&& mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().getItem() instanceof ItemEnderPearl
							&& mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().stackSize != 0
							&& (block == null
							|| (block.getItem() instanceof ItemEnderPearl))) {

						slot = g;
						block = mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack();

					}

				}
				BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
				mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C09PacketHeldItemChange(slot));
				mc.thePlayer.inventory.currentItem = slot;
				mc.thePlayer.swingItem();
			 	mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, 90, false));
				mc.rightClickMouse();
				mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C09PacketHeldItemChange(oldSlot));
				mc.thePlayer.inventory.currentItem = oldSlot;
				clicked = true;
				lastSlot = slot;
		}

		 if(mode.is("MineplexDev")) {
			 mc.thePlayer.sendQueue.addToSendQueue(new C18PacketSpectate(mc.thePlayer.getGameProfile().getId()));
			// mc.thePlayer.sendQueue.addToSendQueue(new C00PacketKeepAlive(10000000));
		 }

	}


	public void onDisable() {
		super.onDisable();
		Monsoon.INSTANCE.eventManager.unregister(this);
		//stopBlink();
		clicked = false;
		doneBow = false;
		lastSlot = -1;
		SpeedUtil.setSpeed(0.15f);
		if(blink.is("Normal") || blink.is("Pulse")) {
			stopBlink();
			Monsoon.INSTANCE.manager.blink.setEnabled(false);
		}

		if(mode.is("VerusFast")) {
			NotificationManager.show(new Notification(NotificationType.INFO, "Verus Fly", "Do not enable fly for 3 seconds.", 1));
		}

		mc.thePlayer.speedInAir = 0.02F;

		this.multiplier = 1.0F;

		if(mode.is("VerusFast")) {
			if (boostTicks > 0) {
				mc.thePlayer.motionX = 0;
				mc.thePlayer.motionZ = 0;
			}
		}
		mc.thePlayer.capabilities.isFlying = false;
		mc.timer.timerSpeed = 1.0F;
		mc.thePlayer.capabilities.setFlySpeed(0.045F);
		mc.thePlayer.capabilities.isFlying = false;
        mc.timer.timerSpeed = 1.0f;
        if (this.mode.is("RedeSky")) {
            mc.thePlayer.setVelocity(0.0, 0.0, 0.0);
        }
	}
	
	@EventTarget
	public void onPreMotion(EventPreMotion e) {

		if (this.mode.is("bridger.land")) {

		}


		if(this.mode.is("SurvivalDub")) {
			double survivalDubSpeed;

			if(mc.thePlayer.onGround) {
				mc.thePlayer.jump();
				survivalDubSpeed = 1.4;
			} else {
				survivalDubSpeed = 0.25;

				if (survivalDubSpeed == 0.25) {
					mc.thePlayer.setMotion(mc.thePlayer.motionX, -0.005, mc.thePlayer.motionZ);
					mc.thePlayer.setMotion(mc.thePlayer.motionX, 0, mc.thePlayer.motionZ);
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 3.33315597345063e-11, mc.thePlayer.posZ);

				}
			}
		}
		if(mode.is("VerusJump")) {
			if(timer.hasTimeElapsed(545, true)) {
				mc.thePlayer.jump();
				mc.thePlayer.onGround = true;
				mc.timer.timerSpeed = 1F;
			}
		}
	}
	
	@EventTarget
	public void onSendPacket(EventSendPacket e) {
		if (this.mode.is("bridger.land")) {
			if(e.getPacket() instanceof C00PacketKeepAlive) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventTarget
	public void onSendPacket(EventReceivePacket e) {
		if(mode.is("VerusFast")) {
			if (e.getPacket() instanceof C03PacketPlayer && timer.hasTimeElapsed(300, false)) {
				C03PacketPlayer packet = (C03PacketPlayer) e.getPacket();

				packet.onGround = true;
			}
		}
		if(mode.is("VerusInf")) {
			if (e.getPacket() instanceof C03PacketPlayer) {
				C03PacketPlayer packet = (C03PacketPlayer) e.getPacket();

				mc.thePlayer.motionY = 0;
				packet.onGround = true;
				//mc.thePlayer.onGround = true;
			}
			if (e.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition) {
				C03PacketPlayer.C04PacketPlayerPosition packet = (C03PacketPlayer.C04PacketPlayerPosition) e.getPacket();

				mc.thePlayer.motionY = 0;
				packet.onGround = true;
			}
		}
		if (this.mode.is("bridger.land")) {
			if(e.getPacket() instanceof C00PacketKeepAlive) {
				e.setCancelled(true);
			}
		}

		if(mode.is("MineplexDev")) {
			if(e.getPacket() instanceof C03PacketPlayer) {
				C03PacketPlayer packet = new C03PacketPlayer();
				packet.onGround = false;
			}
		}
	}
	
	@EventTarget
	public void onUpdate(EventUpdate e) {
		int boostdonecount = 0;

		this.setSuffix(mode.getValueName());

		
		if(blinkTimer.hasTimeElapsed((long) pulseDelay.getValue(), true) && blink.is("Pulse")) {
			Monsoon.INSTANCE.manager.blink.setEnabled(false);
			Monsoon.INSTANCE.manager.blink.setEnabled(true);
		}

		if(mode.is("ACRBow")) {
			boolean boosted = this.mc.thePlayer.hurtTime > 0;
			if (boosted && doneBow) {
				mc.thePlayer.jump();
				mc.gameSettings.keyBindForward.pressed = true;
				mc.thePlayer.motionY = 0;
				//this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + this.mc.thePlayer.motionX * 10.0, this.mc.thePlayer.posY - this.mc.thePlayer.motionY, this.mc.thePlayer.posZ + this.mc.thePlayer.motionZ * 10.0);
			}
		}

		if(mode.is("MineplexDev")) {
			mc.thePlayer.fallDistance = 0;
			for(int i = 0; i < 5; i++) {
				mc.thePlayer.sendQueue.addToSendQueue(new C18PacketSpectate(mc.thePlayer.getGameProfile().getId()));
				C13PacketPlayerAbilities packet = new C13PacketPlayerAbilities();
				//PacketUtil.sendPacketNoEvent(new C16PacketClientStatus());
				packet.setFlying(true);
				packet.setAllowFlying(true);
				PacketUtil.sendPacketNoEvent(packet);
			}
			if(timer.hasTimeElapsed(50, true)) {
				//mc.thePlayer.sendQueue.addToSendQueue(new C00PacketKeepAlive(10000000));
			}
			if(mc.thePlayer.ticksExisted % 4 == 0) {
				PacketUtil.sendPacketNoEvent(new C0FPacketConfirmTransaction());
				PacketUtil.sendPacketNoEvent(new C03PacketPlayer());
			}

			mc.thePlayer.motionY = 0;
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-10D, mc.thePlayer.posZ);
			PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.0E-10D, mc.thePlayer.posZ, true));
		}

		if(mode.is("Packet")) {
			this.mc.thePlayer.capabilities.isFlying = false;
			this.mc.thePlayer.motionY = 0.0;
			if (this.mc.gameSettings.keyBindForward.pressed) {
				this.mc.timer.timerSpeed = 1.0f;
				this.mc.thePlayer.motionX = 0.0;
				this.mc.thePlayer.motionZ = 0.0;
				this.mc.thePlayer.motionY = 0.0;
				this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + this.mc.thePlayer.getLookVec().xCoord * 7.0, this.mc.thePlayer.posY + 0.0000007, this.mc.thePlayer.posZ + this.mc.thePlayer.getLookVec().zCoord * 7.0, false));
			}
			if (this.mc.gameSettings.keyBindBack.pressed) {
				this.mc.timer.timerSpeed = 1.0f;
				this.mc.thePlayer.motionX = 0.0;
				this.mc.thePlayer.motionZ = 0.0;
				this.mc.thePlayer.motionY = 0.0;
				this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + this.mc.thePlayer.getLookVec().xCoord * -5.0, this.mc.thePlayer.posY + 0.0000007, this.mc.thePlayer.posZ + this.mc.thePlayer.getLookVec().zCoord * -5.0, false));
			}
			this.mc.thePlayer.motionX = 0.0;
			this.mc.thePlayer.motionZ = 0.0;
		}

		if(this.mode.is("CubeCraft")) {
			double teleportV = 1;

			double posX = MovementUtil.getPosForSetPosX(teleportV);
			double posZ = MovementUtil.getPosForSetPosZ(teleportV);

			//0.5 is slower but looks better
			this.mc.timer.timerSpeed = 0.6F;
			this.mc.thePlayer.motionY =- 0.25;

			if(this.mc.thePlayer.fallDistance >= 0.8f) {
				this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + posX, this.mc.thePlayer.posY + (this.mc.thePlayer.fallDistance - 0.15), this.mc.thePlayer.posZ + posZ);
				this.mc.thePlayer.fallDistance = 0;
				return;
			}
		}
		if(mode.is("MCCentral")) {
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.2f, mc.thePlayer.posZ, true));
			mc.thePlayer.capabilities.isFlying = true;
			mc.thePlayer.capabilities.setFlySpeed(0.25f);
		}
		if (disableOnDeath.isEnabled()) {
			if (mc.thePlayer.getHealth() <= 0 || mc.thePlayer.isDead) {
				enabled = false;
			}
		}
		if (this.mode.is("SpacePotato")) {
			if (timer.hasTimeElapsed(1000, false)) {
				mc.timer.timerSpeed = 1.4f;
			} else {
				mc.timer.timerSpeed = 0.1f;
			}
		}

		if (mode.is("Vanilla")) {
			//mc.thePlayer.cameraYaw = 0.1f;
			mc.thePlayer.capabilities.isFlying = true;
			mc.timer.timerSpeed = 1F;
			mc.thePlayer.capabilities.setFlySpeed((float) speed.getValue());
		}
		if (this.mode.is("PearlHypixel") || this.mode.is("Bedwars")) {
			if (clicked) {
				mc.thePlayer.cameraYaw = 0.1f;
				//mc.thePlayer.capabilities.isFlying = true;
				mc.thePlayer.motionY = 0;
				SpeedUtil.setSpeed((float) speed.getValue());
				//mc.thePlayer.setVelocity(0,0,0);
				mc.timer.timerSpeed = 1.2f;
			}
		}
		if (mode.is("Airwalk")) {
			//mc.thePlayer.cameraYaw = 0.1f;
			mc.thePlayer.motionY = mc.gameSettings.keyBindJump.isKeyDown() ? 0.72 : mc.gameSettings.keyBindSneak.isKeyDown() ? -0.72 : 0;
			mc.thePlayer.onGround = true;
			SpeedUtil.setSpeed((float) speed.getValue());
		}
		if (mode.is("Test")) {
			mc.thePlayer.motionY = 0F;
			mc.thePlayer.onGround = true;
			SpeedUtil.setSpeed((float) speed.getValue());
		}
		if(mode.is("Hypixel")) {
			if(!mc.thePlayer.onGround) {
				mc.thePlayer.motionY = -0.05;
				//mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.00007, mc.thePlayer.posZ);
				SpeedUtil.setSpeed(0.28f);
				//PacketUtil.sendPacketNoEvent(new C03PacketPlayer(true));
			}
		}
		if (mode.is("RedeskyBlink")) {
			mc.timer.timerSpeed = 1.75f;
			mc.thePlayer.motionY = 0;
			mc.thePlayer.capabilities.setFlySpeed(0.05f);
			mc.thePlayer.capabilities.isFlying = true;
		}
		if (mode.is("SpacePotato")) {
			mc.thePlayer.motionY = mc.gameSettings.keyBindJump.isKeyDown() ? 0.72 : mc.gameSettings.keyBindSneak.isKeyDown() ? -0.72 : 0;
			mc.thePlayer.onGround = true;
			SpeedUtil.setSpeed(1.8f);
		}
		if(mode.is("VerusFast")) {
			mc.thePlayer.motionX = 0;
			mc.thePlayer.motionY = 0;
			mc.thePlayer.motionZ = 0;

			if (mc.thePlayer.hurtTime > 0) {
				boostTicks = 20;
			}

			double motion;
			if (boostTicks > 0) {
				motion = speed.getValue();
				boostTicks--;
			} else {
				motion = 0.45;
				if(timer.hasTimeElapsed(900, false) && mc.theWorld.getBlockState(mc.thePlayer.getPosition().add(0,-1,0)).getBlock() == Blocks.air) {
					//NotificationManager.show(new Notification(NotificationType.INFO, "Verus Fly", "You need to land soon!", 1));
				}
			}
			mc.thePlayer.setMotion(motion, 0, motion);
		}

		if(mode.is("bridger.land")) {
			double y = mc.thePlayer.posY + 1.0E-10D;
			mc.thePlayer.setPosition(mc.thePlayer.posX, y, mc.thePlayer.posZ);
			mc.thePlayer.motionY = 0;
			if(timer.hasTimeElapsed(3000, true)) {
				BlockPos pos = WorldUtil.getForwardBlock(1);
				mc.thePlayer.setPosition(mc.thePlayer.posX + 1, y, mc.thePlayer.posZ);
				mc.thePlayer.motionY = 0;
			}
		}
	
	}

	@EventTarget
	public void onMovement(EventMove e) {

	}

	public static void setMotion(EventMove event, float moveSpeed) {
		MovementInput movementInput = mc.thePlayer.movementInput;
		double moveForward = movementInput.moveForward;
		double moveStrafe = movementInput.moveStrafe;
		double rotationYaw = mc.thePlayer.rotationYaw;
		if (moveForward == 0.0D && moveStrafe == 0.0D) {
			event.setX(0.0D);
			event.setZ(0.0D);
		} else {
			if (moveStrafe > 0) {
				moveStrafe = 1;
			} else if (moveStrafe < 0) {
				moveStrafe = -1;
			}
			if (moveForward != 0.0D) {
				if (moveStrafe > 0.0D) {
					rotationYaw += (moveForward > 0.0D ? -45 : 45);
				} else if (moveStrafe < 0.0D) {
					rotationYaw += (moveForward > 0.0D ? 45 : -45);
				}
				moveStrafe = 0.0D;
				if (moveForward > 0.0D) {
					moveForward = 1.0D;
				} else if (moveForward < 0.0D) {
					moveForward = -1.0D;
				}
			}
			double cos = Math.cos(Math.toRadians(rotationYaw + 90.0F));
			double sin = Math.sin(Math.toRadians(rotationYaw + 90.0F));
			event.setX(moveForward * moveSpeed * cos
					+ moveStrafe * moveSpeed * sin);
			event.setZ(moveForward * moveSpeed * sin
					- moveStrafe * moveSpeed * cos);
		}
	}

	
	public static void damageHypixel(double damage) {
		
		Minecraft mc = Minecraft.getMinecraft();
		
		if (damage > MathHelper.floor_double(mc.thePlayer.getMaxHealth()))
			damage = MathHelper.floor_double(mc.thePlayer.getMaxHealth());

		double offset = 0.0625;
		//offset = 0.015625;
		if (mc.thePlayer != null && mc.getNetHandler() != null) {
			for (short i = 0; i <= ((3 + damage) / offset); i++) {
				mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY + ((offset / 2) * 1), mc.thePlayer.posZ, false));
				mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY + ((offset / 2) * 2), mc.thePlayer.posZ, false));
				mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY, mc.thePlayer.posZ, (i == ((3 + damage) / offset))));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX,
						mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, (i == ((3 + damage) / offset))));
			}
		}
	}

	private int airSlot() {
		for (int j = 0; j < 8; ++j) {
			if (mc.thePlayer.inventory.mainInventory[j] == null) {
				return j;
			}
		}
		return -10;
	}
	
	

	public ItemStack setPearlStack() {
		
		ItemStack block = mc.thePlayer.getCurrentEquippedItem();
		
		if (block != null && block.getItem() != null && !(block.getItem() instanceof ItemEnderPearl)) {
			block = null;
		}
		
		int slot = mc.thePlayer.inventory.currentItem;
		
		for (short g = 0; g < 9; g++) {
			
			if (mc.thePlayer.inventoryContainer.getSlot(g + 36).getHasStack()
					&& mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().getItem() instanceof ItemEnderPearl
					&& mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().stackSize != 0
					&& (block == null
					|| (block.getItem() instanceof ItemBlock && mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().stackSize >= block.stackSize))) {
				
				slot = g;
				block = mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack();
				
			}
			
		}
		if (lastSlot != slot) {
			mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C09PacketHeldItemChange(slot));
			lastSlot = slot;
		}
		return block;
	}

	private void stopBlink() {
		for(Packet packet : savedPackets){
			mc.thePlayer.sendQueue.addToSendQueue(packet);
		}

		savedPackets.clear();
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
