package com.enjoytheban.module.modules.player;

import java.awt.Color;
import java.util.Objects;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.misc.EventCollideWithBlock;
import com.enjoytheban.api.events.world.EventMove;
import com.enjoytheban.api.events.world.EventPacketRecieve;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.math.MathUtil;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * @author Purity
 * @desc Gives you an outterbody experience implements in EntityPlayerSP
 *       pushOutOfBlocks
 */

public class Freecam extends Module {

	// this will hold out "copied" entity
	private EntityOtherPlayerMP copy;

	public Freecam() {
		super("Freecam", new String[] { "outofbody" }, ModuleType.Player);
		setColor(new Color(221, 214, 51).getRGB());
	}

	private double x, y, z;

	//make a copy and set the xyz pos to our pos
	@Override
	public void onEnable() {
		(copy = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile())).clonePlayer(mc.thePlayer,
				true);
		copy.setLocationAndAngles(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ,
				mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
		copy.rotationYawHead = mc.thePlayer.rotationYawHead;
		copy.setEntityId(-1337);
		copy.setSneaking(mc.thePlayer.isSneaking());
		mc.theWorld.addEntityToWorld(copy.getEntityId(), copy);
		x = mc.thePlayer.posX;
		y = mc.thePlayer.posY;
		z = mc.thePlayer.posZ;
	}

	@EventHandler
	private void onPreMotion(EventPreUpdate e) {
		mc.thePlayer.capabilities.isFlying = true;
		mc.thePlayer.noClip = true;
		mc.thePlayer.capabilities.setFlySpeed((float) 0.1);
		e.setCancelled(true);
	}

	//cancel packets so we dont get banned on guardian
	@EventHandler
	private void onPacketSend(EventPacketRecieve e) {
		if (e.getPacket() instanceof C03PacketPlayer)
			e.setCancelled(true);
	}

	//set the bounding box to null so we can pass thru
	@EventHandler
	private void onBB(EventCollideWithBlock e) {
		e.setBoundingBox(null);
	}

	//basically set the character back and remove the entity + reload renders
	@Override
	public void onDisable() {
		mc.thePlayer.setSpeed(0);
		mc.thePlayer.setLocationAndAngles(copy.posX, copy.posY, copy.posZ, copy.rotationYaw,
				copy.rotationPitch);
		mc.thePlayer.rotationYawHead = copy.rotationYawHead;
		mc.theWorld.removeEntityFromWorld(copy.getEntityId());
		mc.thePlayer.setSneaking(copy.isSneaking());
		copy = null;
		mc.renderGlobal.loadRenderers();
		mc.thePlayer.setPosition(x, y, z);
		mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
				mc.thePlayer.posY + 0.01, mc.thePlayer.posZ, mc.thePlayer.onGround));
		mc.thePlayer.capabilities.isFlying = false;
		mc.thePlayer.noClip = false;
		mc.theWorld.removeEntityFromWorld(-1);
	}
}