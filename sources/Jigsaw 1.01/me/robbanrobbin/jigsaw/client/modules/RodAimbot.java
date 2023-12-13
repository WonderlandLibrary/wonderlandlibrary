package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.events.PacketEvent;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;

public class RodAimbot extends Module {

	boolean captured = false;
	
	boolean sendPacketBack = false;

	EntityLivingBase en = null;

	public RodAimbot() {
		super("RodAimbot", Keyboard.KEY_NONE, Category.COMBAT, "Aims for the closest entity when you use your rod.");
	}

	@Override
	public void onToggle() {
		en = null;
		super.onToggle();
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		if (!captured) {
			return;
		}
		en = Utils.getClosestEntity(10);
		if (en == null) {
			en = null;
			captured = false;
			sendPacketBack = true;
			return;
		}
		if (mc.currentScreen != null) {
			return;
		}

		double posX = en.posX - 0.5;
		double posY = en.posY;
		double posZ = en.posZ - 0.5;

		Vec3d toFace = new Vec3d(posX + (en.posX - en.lastTickPosX) * 10, posY + (en.getEyeHeight() / 2f),
				posZ + (en.posZ - en.lastTickPosZ) * 10);

		float[] rots = Utils.getFacePos(toFace);

		event.yaw = rots[0];
		event.pitch = rots[1];
		
		sendPacketBack = true;

		super.onUpdate(event);
	}

	@Override
	public void onLateUpdate() {
		if (!sendPacketBack) {
			return;
		}
		sendPacketBack = false;
		captured = false;
		sendPacketFinal(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
		super.onLateUpdate();
	}

	@Override
	public void onPacketSent(PacketEvent event) {
		Packet packet = event.getPacket();
		if(mc.player != null) {
			if (mc.player.getHeldItem(EnumHand.MAIN_HAND) != null && mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() != null
					&& mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemFishingRod) {
				if (packet instanceof CPacketPlayerTryUseItem) {
					event.cancel();
					captured = true;
				}
			}
		}
		super.onPacketSent(event);
	}

}
