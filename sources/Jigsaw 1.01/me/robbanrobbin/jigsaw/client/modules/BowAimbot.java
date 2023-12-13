package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.events.PacketEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.Vec3d;

public class BowAimbot extends Module {

	private static Entity en;
	private static double sideMultiplier;
	private static double upMultiplier;
	private static double upPredict;
	public static boolean isValid;
	private static Vec3d toFace = null;

	public BowAimbot() {
		super("BowAimbot", Keyboard.KEY_NONE, Category.COMBAT,
				"Tries to aim your bow when you use it. ");
	}

	@Override
	public void onDisable() {

		isValid = false;

		super.onDisable();
	}

	@Override
	public void onEnable() {
		toFace = null;
		isValid = false;

		super.onEnable();
	}

	@Override
	public void onUpdate() {
		en = null;
		if (!(mc.currentScreen == null)) {
			isValid = false;
			return;
		}
		try {
			ItemStack heldItem = Utils.returnItemStackIfPlayerHoldingItem(Items.BOW);
			if (heldItem == null) {
				isValid = false;
				return;
			}
			if (heldItem.getItem() == null) {
				isValid = false;
				return;
			}
			if (!(mc.player.isHandActive())) {
				isValid = false;
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			isValid = false;
			return;
		}

		ArrayList<EntityLivingBase> ens = Utils.getClosestEntities(100);
		double minDistance = 999;
		for (EntityLivingBase en : ens) {
			
			Vec3d futurePosition = new Vec3d(en.predictedPosX, en.predictedPosY, en.predictedPosZ);
			
			if (mc.world.rayTraceBlocks(
					new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ),
					futurePosition.addVector(0, en.getEyeHeight(), 0), false, true, false) != null) {
				continue;
			}
			
			if (mc.player.getDistanceToEntity(en) < minDistance) {
				minDistance = mc.player.getDistanceToEntity(en);
				this.en = en;
			}
			
		}
		if (en == null) {
			isValid = false;
			return;
		}
		
		Vec3d futurePosition = new Vec3d(en.predictedPosX, en.predictedPosY, en.predictedPosZ);
		
		if (mc.world.rayTraceBlocks(
				new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ),
				futurePosition.addVector(0, en.getEyeHeight(), 0), false, true, false) != null) {
			en = null;
			isValid = false;
		}
		if (en == null) {
			isValid = false;
			return;
		}
		
		isValid = true;
		
		double distanceToFuturePosition = mc.player.getDistance(futurePosition.x, futurePosition.y, futurePosition.z);
		
		sideMultiplier = distanceToFuturePosition / ((distanceToFuturePosition / 2) / 1) * 5;
		upMultiplier = (mc.player.getDistanceSq(futurePosition.x, futurePosition.y, futurePosition.z) / 320) * 1.1;
		upPredict = 5;
		generateToFace();
		if (this.currentMode.equalsIgnoreCase("Client")) {
			if (ClientSettings.smoothAim) {
				Utils.smoothFacePos(toFace, 2);
			} else {
				Utils.facePos(toFace);
			}
		}
		super.onUpdate();
	}

	@Override
	public String[] getModes() {
		return new String[] { "Packet", "Client" };
	}

	@Override
	public String getAddonText() {
		return this.currentMode;
	}

	private static void generateToFace() {
		
		Vec3d futurePosition = new Vec3d(en.predictedPosX, en.predictedPosY, en.predictedPosZ);
		
		double xPos = futurePosition.x;
		double yPos = futurePosition.y;
		double zPos = futurePosition.z;
		toFace = new Vec3d((xPos - 0.5) + (xPos - en.lastPredictedPosX) * sideMultiplier,
				yPos + upMultiplier,
				(zPos - 0.5) + (zPos - en.lastPredictedPosZ) * sideMultiplier);
	}

	public static boolean getShouldChangePackets() {
		if(!isValid) {
			return false;
		}
		if (mc.currentScreen != null) {
			return false;
		}
		ItemStack heldItem = Utils.returnItemStackIfPlayerHoldingItem(Items.BOW);
		if (heldItem == null) {
			return false;
		}
		if (!(mc.player.isHandActive())) {
			return false;
		}
		if (Jigsaw.getModuleByName("BowAimbot").getCurrentMode().equals("Client")) {
			return false;
		}
		return true;
	}

	@Override
	public void onPacketSent(PacketEvent event) {
		Packet packet = event.getPacket();
		if (getShouldChangePackets() && toFace != null) {
			if (packet instanceof CPacketPlayer) {
				event.cancel();
				CPacketPlayer.PositionRotation playerPacket = new CPacketPlayer.PositionRotation();
				playerPacket.rotating = true;
				playerPacket.moving = true;
				playerPacket.x = mc.player.posX;
				playerPacket.y = mc.player.posY;
				playerPacket.z = mc.player.posZ;
				playerPacket.onGround = mc.player.onGround;
				float[] rots = Utils.getFacePos(toFace);
				playerPacket.yaw = rots[0];
				playerPacket.pitch = rots[1];
				sendPacketFinal(playerPacket);
			}
		}
		super.onPacketSent(event);

	}

}
