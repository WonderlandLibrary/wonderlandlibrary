package com.enjoytheban.module.modules.player;

import java.awt.Color;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.api.value.Option;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class FastUse extends Module {

	private Option<Boolean> guardian = new Option("Guardian", "guardian", true);

	public FastUse() {
		super("FastUse", new String[] { "fasteat", "fuse" }, ModuleType.Player);
		addValues(guardian);
	}

	private boolean canConsume() {
		return mc.thePlayer.getCurrentEquippedItem() != null
				&& mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemPotion
				|| mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemFood;
	}

	@EventHandler
	private void onUpdate(EventPreUpdate e) {
		this.setColor(new Color(100, 200, 200).getRGB());
		if(guardian.getValue()) {
			if (mc.thePlayer.onGround) {
				if (mc.thePlayer.getItemInUseDuration() == 1 && Minecraft.getMinecraft().gameSettings.keyBindUseItem.pressed
						&& !(mc.thePlayer.getItemInUse().getItem() instanceof ItemBow)
						&& !(mc.thePlayer.getItemInUse().getItem() instanceof ItemSword)) {
					for (int i = 0; i < 40; ++i) {
						mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(
								mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround));
						if (guardian.getValue()) {
							if (mc.thePlayer.ticksExisted % 2 == 0) {
								mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
										mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ, false));
							}
						}
					}
					mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
							C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
				}
			}
		} else {
			if (mc.thePlayer.onGround) {
				if (mc.thePlayer.getItemInUseDuration() == 16 && Minecraft.getMinecraft().gameSettings.keyBindUseItem.pressed
						&& !(mc.thePlayer.getItemInUse().getItem() instanceof ItemBow)
						&& !(mc.thePlayer.getItemInUse().getItem() instanceof ItemSword)) {
					for (int i = 0; i < 17; ++i) {
						mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(
								mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround));
					}
					mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
							C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
				}
			}
		}
	}
}