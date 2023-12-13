package dev.monsoon.module.implementation.movement;

import dev.monsoon.event.listeners.EventPacket;
import dev.monsoon.util.entity.DamageUtil;
import dev.monsoon.util.entity.MotionUtils;
import dev.monsoon.util.entity.MovementUtil;
import dev.monsoon.util.entity.SpeedUtil;
import dev.monsoon.util.misc.PacketUtil;
import net.minecraft.network.Packet;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;

import dev.monsoon.Monsoon;
import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventMotion;
import dev.monsoon.event.listeners.EventUpdate;
import dev.monsoon.module.base.Module;
import dev.monsoon.module.setting.impl.BooleanSetting;
import dev.monsoon.module.setting.impl.ModeSetting;
import dev.monsoon.module.setting.impl.NumberSetting;
import dev.monsoon.util.misc.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

import java.util.ArrayList;
import dev.monsoon.module.enums.Category;

public class Fly extends Module {

	Timer timer = new Timer();
	Timer blinkTimer = new Timer();
	private float hSpeed;
	private float ySpeed;

	private boolean done;

	public ModeSetting mode = new ModeSetting("Mode", this,"Vanilla", "Vanilla", "Hypixel", "CubeCraft", "PearlHypixel", "Airwalk", "Mineplex", "MCCentral", "RedeskyBlink","VerusFast", "VerusInf", "VerusJump", "SurvivalDub", "Dev", "SpacePotato");
	public BooleanSetting disableOnDeath = new BooleanSetting("Disable on death", true, this);
	public ModeSetting damageMode = new ModeSetting("Damage mode", this,"None", "None", "Verus", "One", "Two", "Three");
	public NumberSetting health = new NumberSetting("Damage Amount", 1, 1, 20, 1, this, false);
	public NumberSetting speed = new NumberSetting("Speed", 2.2, 0.1, 15, 0.1, this);
	public ModeSetting blink = new ModeSetting("Blink mode", this,"None", "None", "Normal", "Pulse");
	public NumberSetting pulseDelay = new NumberSetting("Pulse Delay", 200, 5, 3000, 1, this, false);

	private boolean clicked, hasBlinkedMineplex = false;
	public static transient int lastSlot = -1;
	private ArrayList<Packet> savedPackets = new ArrayList<Packet>();

	public Fly() {
		super("Fly", Keyboard.KEY_NONE, Category.MOVEMENT);
		this.addSettings(mode,speed,damageMode,health,blink,pulseDelay);
	}

	public void onEnable() {

		if(mc.thePlayer.getHealth() > 2 && !mode.is("Hypixel")) {
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

		if(mode.is("Hypixel")) {
			mc.thePlayer.jump();
			DamageUtil.damageMethodThree();
		}

		hSpeed = 0.26F;
		done = false;
		hasBlinkedMineplex = false;
		blinkTimer.reset();
		if(mode.is("Mineplex")) {
			//DamageUtil.damageMethodOne();
			//Monsoon.manager.blink.setEnabled(true);
		}
		//mc.thePlayer.jump();
		if(this.mode.is("Test"))
			damageHypixel(1);
		clicked = false;
		lastSlot = -1;
		if(blink.is("Normal") || mode.is("RedeskyBlink")) {
			mc.thePlayer.jump();
			Monsoon.blink.setEnabled(true);
		}
		if(mode.is("Verus")) {
			SpeedUtil.setSpeed(0);
			mc.thePlayer.motionX = 0;
			SpeedUtil.setSpeed(0);
			mc.thePlayer.motionY = 0;
			SpeedUtil.setSpeed(0);
			mc.thePlayer.motionZ = 0;
			SpeedUtil.setSpeed(0);
			mc.thePlayer.jump();
			SpeedUtil.setSpeed(0);
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

	}


	public void onDisable() {
		//stopBlink();
		clicked = false;
		lastSlot = -1;
		SpeedUtil.setSpeed(0.15f);
		if(blink.is("Normal") || blink.is("Pulse")) {
			stopBlink();
			Monsoon.blink.setEnabled(false);
		}
		if(mode.is("VerusFast")) {
			mc.thePlayer.onGround = true;
			Monsoon.manager.blink.setEnabled(true);
			Monsoon.manager.blink.setEnabled(false);
		}
		mc.thePlayer.capabilities.isFlying = false;
		mc.timer.timerSpeed = 1.0F;
		mc.thePlayer.capabilities.setFlySpeed(0.045F);
		if (mode.is("VerusFast")) {
			mc.thePlayer.motionX *= 0.01F;
			mc.thePlayer.motionZ *= 0.01F;
		}
		mc.thePlayer.capabilities.isFlying = false;
        mc.timer.timerSpeed = 1.0f;
        if (this.mode.is("RedeSky")) {
            mc.thePlayer.setVelocity(0.0, 0.0, 0.0);
        }
	}

	public void onEvent(Event e) {
		if (e instanceof EventMotion) {
			if (this.mode.is("Redesky") && e.isPre()) {
				//mc.thePlayer.cameraYaw = -00.2f;
				if (timer.hasTimeElapsed(200, false)) {
					mc.timer.timerSpeed = 0.05f;
					mc.thePlayer.motionY = 0.0F;
					//mc.thePlayer.capabilities.allowFlying = true;
					mc.thePlayer.capabilities.isFlying = true;
					mc.thePlayer.jumpMovementFactor = 0.06F;
					mc.thePlayer.speedInAir = 0.08F;
					if (timer.hasTimeElapsed(800, true)) {
						mc.timer.timerSpeed = 2F;
						//if(timer.hasTimeElapsed(500, true)) {
						//    mc.timer.timerSpeed = 0.3f;
						//}
					}
				}
			}
			if(this.mode.is("SurvivalDub") && e.isPre()) {
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
			if(mode.is("Mineplex") && e.isPre()) {
				double flySpeed = 2.1F;
				if (!this.done && mc.thePlayer.onGround && MovementUtil.isMoving()) {
					mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(airSlot()));
					BlockPos blockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY - 1.0D, mc.thePlayer.posZ);
					Vec3 vec = new Vec3(blockPos.x, blockPos.y, blockPos.z).addVector(0.4000000059604645D, 0.4000000059604645D, 0.4000000059604645D);
					mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, null, blockPos, EnumFacing.UP, new Vec3(vec.xCoord * 0.4000000059604645D, vec.yCoord * 0.4000000059604645D, vec.zCoord * 0.4000000059604645D));
					this.hSpeed += 0.095D;
					MotionUtils.setMotion(mc.thePlayer.ticksExisted % 2 == 0 ? -this.hSpeed : this.hSpeed);
					if (this.hSpeed >= 3.8) {
						MotionUtils.setMotion(0.0F);
						mc.thePlayer.motionY = mc.thePlayer.motionY = 0.42F;
						this.done = true;
						return;
					}
				} else {
					mc.thePlayer.jump();
					mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
					this.hSpeed *= 0.88D;
					MovementInput movementInput = mc.thePlayer.movementInput;
					mc.thePlayer.motionY = movementInput.jump ? 0.42F : movementInput.sneak ? -0.42F : 0;
					if (hSpeed < 0.5F) {
						hSpeed = 0.14F;
					}
					MotionUtils.setMotion(this.hSpeed / 2);
				}
			}
			boolean tower = (mc.thePlayer.movementInput.jump && !mc.thePlayer.movementInput.sneak && (mode.is("SpacePotato") || mode.is("Test") | mode.is("Airwalk") || this.mode.is("PearlHypixel")));
			if (tower) {
				mc.thePlayer.motionY = 0.72f;
			}

			boolean towerDown = (mc.thePlayer.movementInput.sneak  && !mc.thePlayer.movementInput.jump && (mode.is("SpacePotato") || mode.is("Test") | mode.is("Airwalk") || this.mode.is("PearlHypixel")));
			if (towerDown) {
				mc.thePlayer.motionY = -0.72f;
			}
			if(mode.is("VerusFast")) {
				if(timer.hasTimeElapsed(600, true)) {
					mc.thePlayer.setPosition(mc.thePlayer.posX,mc.thePlayer.posY - 0.2f, mc.thePlayer.posZ);
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.2f, mc.thePlayer.posZ, true));
				}
				//e.setCancelled(true);
			}
			if(mode.is("VerusJump")) {
				if(timer.hasTimeElapsed(545, true)) {
					mc.thePlayer.jump();
					mc.thePlayer.onGround = true;
					mc.timer.timerSpeed = 1F;
				}
			}
		}
		if(e instanceof EventPacket) {
			if(mode.is("VerusFast")) {
				if (((EventPacket<?>) e).getPacket() instanceof C03PacketPlayer) {
					C03PacketPlayer packet = (C03PacketPlayer) ((EventPacket<?>) e).getPacket();

					//mc.thePlayer.onGround = true;
					mc.thePlayer.motionY = 0;
					packet.onground = true;
				}
			}
			if(mode.is("VerusInf")) {
				if (((EventPacket<?>) e).getPacket() instanceof C03PacketPlayer) {
					C03PacketPlayer packet = (C03PacketPlayer) ((EventPacket<?>) e).getPacket();

					mc.thePlayer.motionY = 0;
					packet.onground = true;
					//mc.thePlayer.onGround = true;
				}
				if (((EventPacket<?>) e).getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition) {
					C03PacketPlayer.C04PacketPlayerPosition packet = ( C03PacketPlayer.C04PacketPlayerPosition) ((EventPacket<?>) e).getPacket();

					mc.thePlayer.motionY = 0;
					packet.onground = true;
				}
			}
		}
		if (e instanceof EventUpdate) {
			this.setSuffix(mode.getValueName());

			if(blinkTimer.hasTimeElapsed((long) pulseDelay.getValue(), true) && blink.is("Pulse")) {
				Monsoon.manager.blink.setEnabled(false);
				Monsoon.manager.blink.setEnabled(true);
			}

			if(done && timer.hasTimeElapsed(150, true)) {
				Monsoon.manager.blink.setEnabled(false);
				Monsoon.manager.blink.setEnabled(true);
			}

			if(mode.is("VerusFast")) {
				if(!timer.hasTimeElapsed(1100, false)) {
					SpeedUtil.setSpeed(1.5f);
				} else {
					SpeedUtil.setSpeed(0.6f);
				}
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
				mc.thePlayer.motionY = 0F;
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
				mc.thePlayer.motionY = 0F;
				mc.thePlayer.onGround = true;
				SpeedUtil.setSpeed(1.8f);
			}
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

	private void startBlink(EventPacket e) {
		if(e.isOutgoing()) {
			Packet packet = ((EventPacket) e).getPacket();

			if(packet instanceof C03PacketPlayer){
				savedPackets.add(packet);
				e.setCancelled(true);
			}
		}
	}


}
