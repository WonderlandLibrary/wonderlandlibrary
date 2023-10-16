package com.enjoytheban.module.modules.movement;

import java.awt.Color;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.api.value.Numbers;
import com.enjoytheban.api.value.Option;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Step extends Module {

	private Numbers<Integer> height = new Numbers("Height", "height", 1.0, 1.0, 10.0, 0.5);
	private Option<Boolean> ncp = new Option("NCP", "ncp", false);

	public Step() {
		super("Step", new String[] { "autojump" }, ModuleType.Movement);
		setColor(new Color(165, 238, 65).getRGB());
		addValues(ncp);
	}

	@Override
	public void onDisable() {
		mc.thePlayer.stepHeight = 0.6f;
	}

	@EventHandler
	private void onUpdate(EventPreUpdate e) {
		if (ncp.getValue()) {
			mc.thePlayer.stepHeight = 0.6f;
			if ((mc.thePlayer.isCollidedHorizontally) && (mc.thePlayer.onGround) && (mc.thePlayer.isCollidedVertically)
					&& (mc.thePlayer.isCollided)) {
				if ((mc.thePlayer.isCollidedHorizontally) && (mc.thePlayer.onGround)
						&& (mc.thePlayer.isCollidedVertically) && (mc.thePlayer.isCollided)) {
					mc.thePlayer.setSprinting(true);
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
							mc.thePlayer.posX, mc.thePlayer.posY + 0.42D, mc.thePlayer.posZ,
							mc.thePlayer.onGround));
					mc.thePlayer.setSprinting(true);
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
							mc.thePlayer.posX, mc.thePlayer.posY + 0.753D, mc.thePlayer.posZ,
							mc.thePlayer.onGround));
					mc.thePlayer.setSprinting(true);
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42D,
							mc.thePlayer.posZ);
					mc.timer.timerSpeed = 0.5f;
					mc.thePlayer.setSprinting(true);
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								Thread.sleep(100L);
							} catch (InterruptedException ex) {
								ex.printStackTrace();
							}
							mc.timer.timerSpeed = 1.0f;
						}
					}).start();
				}
			}
		} else {
			mc.thePlayer.stepHeight = 1.0f;
		}
	}
}
