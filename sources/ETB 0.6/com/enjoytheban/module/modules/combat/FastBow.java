package com.enjoytheban.module.modules.combat;

import java.awt.Color;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPacketRecieve;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.api.value.Option;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.TimerUtil;

import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class FastBow extends Module {

	private TimerUtil timer = new TimerUtil();
	private Option<Boolean> faithful = new Option("Faithful", "faithful", true);

	int counter;

	public FastBow() {
		super("FastBow", new String[] { "zoombow", "quickbow" }, ModuleType.Combat);
		setColor(new Color(255, 99, 99).getRGB());
		addValues(faithful);
		this.counter = 0;
	}

	private boolean canConsume() {
		return mc.thePlayer.inventory.getCurrentItem() != null
				&& mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow;
	}

	private void killGuardian() {
		if (timer.hasReached(1000L)) {
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
					mc.thePlayer.posY - Double.POSITIVE_INFINITY, mc.thePlayer.posZ, false));
			timer.reset();
		}
	}

	@EventHandler
	private void onUpdate(EventPreUpdate e) {
		if (faithful.getValue()) {
			if (mc.thePlayer.onGround && this.mc.thePlayer.getCurrentEquippedItem() != null
					&& this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow
					&& this.mc.gameSettings.keyBindUseItem.pressed) {
				mc.thePlayer.sendQueue.addToSendQueue(
						new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getCurrentItem()));
				for (int index = 0; index < 16; ++index) {
					if (!mc.thePlayer.isDead) {
						mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(
								this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.09, this.mc.thePlayer.posZ,
								this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch, true));
					}
				}
				this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
						C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
			}
			if (this.mc.thePlayer.onGround && this.mc.thePlayer.getCurrentEquippedItem() != null
					&& this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow
					&& this.mc.gameSettings.keyBindUseItem.pressed) {
				this.mc.thePlayer.sendQueue.addToSendQueue(
						new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getCurrentItem()));
				if (this.mc.thePlayer.ticksExisted % 2 == 0) {
					this.mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 12);
					++this.counter;
					if (this.counter > 0) {
						this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
								this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.0, this.mc.thePlayer.posZ,
								this.mc.thePlayer.onGround));
						this.counter = 0;
					}
					for (int dist = 20, index = 0; index < dist; ++index) {
						this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
								this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0E-12, this.mc.thePlayer.posZ,
								this.mc.thePlayer.onGround));
					}
					this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
							C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
					mc.playerController.onStoppedUsingItem(mc.thePlayer);
				}
			}
		} else {
			if (mc.thePlayer.onGround && canConsume() && mc.gameSettings.keyBindUseItem.pressed) {
				mc.thePlayer.sendQueue
						.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
				for (int i = 0; i < 20; ++i) {
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
							mc.thePlayer.posY + 1.0E-9, mc.thePlayer.posZ, true));
				}
				mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
						C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
			} else {
				mc.timer.timerSpeed = 1f;
			}
		}
	}

	@EventHandler
	public void onRecieve(EventPacketRecieve event) {
		if (event.getPacket() instanceof S18PacketEntityTeleport) {
			final S18PacketEntityTeleport packet = (S18PacketEntityTeleport) event.getPacket();
			if (this.mc.thePlayer != null) {
				packet.setYaw((byte) this.mc.thePlayer.rotationYaw);
			}
			packet.setPitch((byte) this.mc.thePlayer.rotationPitch);
		}
	}
}