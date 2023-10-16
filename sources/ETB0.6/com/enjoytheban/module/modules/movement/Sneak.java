package com.enjoytheban.module.modules.movement;

import java.awt.Color;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.Type;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;

import net.minecraft.network.play.client.C0BPacketEntityAction;

/**
 * Simple sneak
 * @author purity
 */

public class Sneak extends Module {

	public Sneak() {
		super("Sneak", new String[] { "stealth", "snek" }, ModuleType.Movement);
        setColor(new Color(84,194,110).getRGB());
	}

	@EventHandler
	private void onUpdate(EventPreUpdate e) {
		// stop sneaking in pre
		if (e.getType() == Type.PRE) {
			if (mc.thePlayer.isSneaking() || mc.thePlayer.moving())
				return;
			mc.thePlayer.sendQueue.addToSendQueue(
					new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
		// start sneaking in post
		} else if (e.getType() == Type.POST) {
			mc.thePlayer.sendQueue.addToSendQueue(
					new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
		}
	}

	@Override
	public void onDisable() {
		mc.thePlayer.sendQueue.addToSendQueue(
				new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
	}
}