package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.modules.target.AuraUtils;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.util.Vec3;

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

		Vec3 toFace = new Vec3(posX + (en.posX - en.lastTickPosX) * 10, posY + (en.getEyeHeight() / 2f),
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
		sendPacketFinal(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
		super.onLateUpdate();
	}

	@Override
	public void onPacketSent(AbstractPacket packetIn) {
		if(mc.thePlayer != null) {
			if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() != null
					&& mc.thePlayer.getHeldItem().getItem() instanceof ItemFishingRod) {
				if (packetIn instanceof C08PacketPlayerBlockPlacement) {
					packetIn.cancel();
					captured = true;
				}
			}
		}
		super.onPacketSent(packetIn);
	}

}
