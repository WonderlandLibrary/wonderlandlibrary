package com.enjoytheban.module.modules.combat;

import java.awt.Color;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.api.value.Option;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

/*
 * Created by Jutting on Oct 10, 2018
 */

public class Regen extends Module {

	private Option<Boolean> guardian = new Option("Guardian", "guardian", true);

	public Regen() {
		super("Regen", new String[] { "fastgen" }, ModuleType.Combat);
		setColor(new Color(208, 30, 142).getRGB());
	}

	@EventHandler
	private void onUpdate(EventPreUpdate event) {
		if (mc.thePlayer.onGround && this.mc.thePlayer.getHealth() < 8 * 2.0
				&& this.mc.thePlayer.getFoodStats().getFoodLevel() > 17 && this.mc.thePlayer.isCollidedVertically) {
			for (int i = 0; i < 60; ++i) {
				mc.thePlayer.sendQueue.addToSendQueue(
						new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-9,
								mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
				mc.thePlayer.motionX = 0;
				mc.thePlayer.motionZ = 0;
			}
			if (guardian.getValue()) {
				if (mc.thePlayer.ticksExisted % 3 == 0) {
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
							mc.thePlayer.posY - 999, mc.thePlayer.posZ, true));
				}
			}
			mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
					C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
		}
	}
}