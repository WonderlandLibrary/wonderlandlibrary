package info.sigmaclient.util.misc;

import info.sigmaclient.util.MinecraftUtil;
import info.sigmaclient.util.NetUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class BlockUtil implements MinecraftUtil {
	/**
	 * Gets the yaw and pitch required to look at a given block
	 * 
	 * @param pos
	 * @return
	 */
	public static float[] getRotationsNeeded(BlockPos pos) {
		double diffX = pos.getX() + 0.5 - mc.thePlayer.posX;
		double diffY = (pos.getY() + 0.5) - (mc.thePlayer.posY + mc.thePlayer.height);
		double diffZ = pos.getZ() + 0.5 - mc.thePlayer.posZ;
		double dist = MathHelper.sqrt_double((diffX * diffX) + (diffZ * diffZ));
		float yaw = (float) ((Math.atan2(diffZ, diffX) * 180.0D) / 3.141592653589793D) - 90.0F;
		float pitch = (float) -((Math.atan2(diffY, dist) * 180.0D) / 3.141592653589793D);
		return new float[] { mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw), mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch) };
	}

	/**
	 * Updates the player's directions.
	 */
	public static float[] updateDirections(BlockPos pos) {
		float[] looks = BlockUtil.getRotationsNeeded(pos);
		if (mc.thePlayer.isCollidedVertically) {
			NetUtil.sendPacketNoEvents(new C03PacketPlayer.C05PacketPlayerLook(looks[0], looks[1], mc.thePlayer.onGround));
		}
		return looks;
	}

	/**
	 * Ensures that the best tool is used for breaking the block.
	 */
	public static void updateTool(BlockPos pos) {
		Block block = mc.theWorld.getBlockState(pos).getBlock();
		float strength = 1.0F;
		int bestItemIndex = -1;
		for (int i = 0; i < 9; i++) {
			ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];
			if (itemStack == null) {
				continue;
			}
			if ((itemStack.getStrVsBlock(block) > strength)) {
				strength = itemStack.getStrVsBlock(block);
				bestItemIndex = i;
			}
		}
		if (bestItemIndex != -1) {
			mc.thePlayer.inventory.currentItem = bestItemIndex;
		}
	}

	// this is darkmagician's. credits to him.
	public static boolean isInLiquid() {
		if (mc.thePlayer.isInWater()) {
			return true;
		}
		boolean inLiquid = false;
		final int y = (int) mc.thePlayer.getEntityBoundingBox().minY;
		for (int x = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; x++) {
			for (int z = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; z++) {
				final Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
				if (block != null && block.getMaterial() != Material.air) {
					if (!(block instanceof BlockLiquid)) return false;
					inLiquid = true;
				}
			}
		}
		return inLiquid;
	}

	// this method is N3xuz_DK's I believe. credits to him.
	public static boolean isOnLiquid() {
		if (mc.thePlayer == null) return false;
		boolean onLiquid = false;
		final int y = (int) mc.thePlayer.getEntityBoundingBox().offset(0.0D, -0.01D, 0.0D).minY;
		for (int x = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; x++) {
			for (int z = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; z++) {
				final Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
				if (block != null && block.getMaterial() != Material.air) {
					if (!(block instanceof BlockLiquid)) return false;
					onLiquid = true;
				}
			}
		}
		return onLiquid;
	}
}
