package com.enjoytheban.module.modules.movement;

import java.awt.Color;

import com.enjoytheban.Client;
import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.Type;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.module.modules.combat.Killaura;
import com.enjoytheban.utils.Helper;

import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * An NCP noslowdown that works on hypixel
 * 
 * @author Purity
 * @called EntityPlayerSP onLivingUpdate
 */

public class NoSlow extends Module {

	public NoSlow() {
		super("NoSlow", new String[] { "noslowdown" }, ModuleType.Movement);
		setColor(new Color(216, 253, 100).getRGB());
	}

	@EventHandler
	private void onUpdate(EventPreUpdate e) {
		if (mc.thePlayer.isBlocking() && !Helper.onServer("invaded") && !Helper.onServer("hypixel") && !Helper.onServer("faithful") && !Helper.onServer("mineman")) {
			mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
					C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
		}
	}
}