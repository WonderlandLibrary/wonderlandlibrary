package com.enjoytheban.module.modules.world;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPacketSend;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.mojang.authlib.GameProfile;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;

/**
 * blink hack
 * @author purity
 */

public class Blink extends Module {
	
	private EntityOtherPlayerMP blinkEntity;
	private List<Packet> packetList;

	public Blink() {
		super("Blink", new String[] { "blonk" }, ModuleType.Player);
		this.packetList = new ArrayList<Packet>();
	}

	@Override
	public void onEnable() {
		this.setColor(new Color(200,100,200).getRGB());
		if (mc.thePlayer == null) {
			return;
		}
		this.blinkEntity = new EntityOtherPlayerMP(mc.theWorld, new GameProfile(new UUID(69L, 96L), "Blink"));
		this.blinkEntity.inventory = mc.thePlayer.inventory;
		this.blinkEntity.inventoryContainer = mc.thePlayer.inventoryContainer;
		this.blinkEntity.setPositionAndRotation(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ,
				mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
		this.blinkEntity.rotationYawHead = mc.thePlayer.rotationYawHead;
		mc.theWorld.addEntityToWorld(this.blinkEntity.getEntityId(), this.blinkEntity);
	}

	@EventHandler
	private void onPacketSend(EventPacketSend event) {
		if (event.getPacket() instanceof C0BPacketEntityAction || event.getPacket() instanceof C03PacketPlayer
				|| ((event.getPacket() instanceof C02PacketUseEntity
						|| event.getPacket() instanceof C0APacketAnimation))
				|| (event.getPacket() instanceof C08PacketPlayerBlockPlacement)) {
			this.packetList.add(event.getPacket());
			event.setCancelled(true);
		}
	}

	@Override
	public void onDisable() {
		for (Packet packet : this.packetList) {
			mc.getNetHandler().addToSendQueue(packet);
		}
		this.packetList.clear();
		mc.theWorld.removeEntityFromWorld(this.blinkEntity.getEntityId());
	}
}
