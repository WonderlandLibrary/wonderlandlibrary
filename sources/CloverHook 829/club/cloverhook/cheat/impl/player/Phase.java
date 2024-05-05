package club.cloverhook.cheat.impl.player;

import club.cloverhook.Cloverhook;
import club.cloverhook.cheat.Cheat;
import club.cloverhook.cheat.CheatCategory;
import club.cloverhook.cheat.impl.movement.Speed;
import club.cloverhook.event.Stage;
import club.cloverhook.event.minecraft.*;
import club.cloverhook.utils.Stopwatch;
import club.cloverhook.utils.property.impl.StringsProperty;
import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.*;

public class Phase extends Cheat {

	Stopwatch timer;
	private int moveUnder;
	private final StringsProperty prop_mode = new StringsProperty("Mode", "",
			null, false, false, new String[] {"Aris"}, new Boolean[] { true});

	public Phase() {
		super("Phase", "", CheatCategory.PLAYER);
		registerProperties(prop_mode);
		timer = new Stopwatch();
	}

	public String getDirection() {
		return Minecraft.getMinecraft().thePlayer.getHorizontalFacing().getName();
	}

	public boolean isInsideBlock() {
		for (int x = MathHelper.floor_double(
				mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
			for (int y = MathHelper.floor_double(
					mc.thePlayer.getEntityBoundingBox().minY + 1.0D); y < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxY)
					+ 2; ++y) {
				for (int z = MathHelper.floor_double(
						mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ)
						+ 1; ++z) {
					Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
					if (block != null && !(block instanceof BlockAir)) {
						AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.theWorld, new BlockPos(x, y, z),
								mc.theWorld.getBlockState(new BlockPos(x, y, z)));
						if (block instanceof BlockHopper) {
							boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
						}

						if (boundingBox != null && mc.thePlayer.getEntityBoundingBox().intersectsWith(boundingBox))
							return true;
					}
				}
			}
		}
		return false;
	}


	@Collect
	public void onBoundingBox(BoundingBoxEvent boundingBoxEvent) {
		if ((prop_mode.getValue().get("Aris"))
				&& (isInsideBlock() && mc.gameSettings.keyBindJump.pressed || !isInsideBlock() && boundingBoxEvent.getBoundingBox() != null && boundingBoxEvent.getBoundingBox().maxY > mc.thePlayer.getEntityBoundingBox().minY)) {
			if (mc.thePlayer.isMoving()) {
				mc.thePlayer.setSpeed(0.625F);
			}
			mc.thePlayer.motionY = 0;
			boundingBoxEvent.setBoundingBox(null);
		}

	}

	@Collect
	public void onBlockPush(BlockPushEvent e) {
		if (prop_mode.getValue().get("Aris")) {
			e.setCancelled(true);
			mc.thePlayer.motionY = 0;
		}
	}

	@Collect
	public void onProcessPacket(ProcessPacketEvent event) {

		if (event.getPacket() instanceof S08PacketPlayerPosLook) {
			S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
			packet.setPitch(mc.thePlayer.rotationPitch);
			packet.setYaw(mc.thePlayer.rotationYaw);

			if (moveUnder == 2) {
				moveUnder = 1;
			}
		}
	}

//	@Collect
//	public void onRenderInside(EventRenderInsideBlocks e) {
//		if (prop_mode.getValue().get("Infinity") || prop_mode.getValue().get("FMC")) {
//			e.setCancelled(true);
//		}
//	}

	@Collect
	public void onPlayerUpdate(PlayerUpdateEvent playerUpdateEvent) {
		if (playerUpdateEvent.isPre()) {

		} else {
			if (prop_mode.getValue().get("Aris")) {
				setMode("Aris");
				if (mc.thePlayer.isSneaking()) {
					if (!mc.thePlayer.isOnLadder()) {
						mc.thePlayer.setSpeed(mc.thePlayer.isCollidedHorizontally ? .3 : .05);
						mc.thePlayer.getEntityBoundingBox().offset(
								1.2 * Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f)), 0.0,
								1.2 * Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f)));

						if (mc.getCurrentServerData() != null
								&& !mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel")) {
							double offset = 1.35;
							Number playerYaw = mc.thePlayer.getDir(mc.thePlayer.rotationYaw);
							mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
									mc.thePlayer.posX - Math.sin(playerYaw.doubleValue()) * offset,
									mc.thePlayer.posY + .3,
									mc.thePlayer.posZ + Math.cos(playerYaw.doubleValue()) * offset, true));
							mc.thePlayer.setPositionAndUpdate(
									mc.thePlayer.posX - Math.sin(playerYaw.doubleValue()) * offset, mc.thePlayer.posY,
									mc.thePlayer.posZ + Math.cos(playerYaw.doubleValue()) * offset);
						} else {
							double offset = 1.2;
							Number playerYaw = mc.thePlayer.getDir(mc.thePlayer.rotationYaw);
							mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
									mc.thePlayer.posX - Math.sin(playerYaw.doubleValue()) * offset,
									mc.thePlayer.posY + .3,
									mc.thePlayer.posZ + Math.cos(playerYaw.doubleValue()) * offset, true));
							mc.thePlayer.setPositionAndUpdate(
									mc.thePlayer.posX - Math.sin(playerYaw.doubleValue()) * offset, mc.thePlayer.posY,
									mc.thePlayer.posZ + Math.cos(playerYaw.doubleValue()) * offset);
							playerUpdateEvent.setPosX(mc.thePlayer.posX - Math.sin(playerYaw.doubleValue()) * offset);
							playerUpdateEvent.setPosZ(mc.thePlayer.posZ + Math.cos(playerYaw.doubleValue()) * offset);
						}
					}

				}
			}
		}
	}
}
