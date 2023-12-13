package me.robbanrobbin.jigsaw.client.tools;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class AIUtils {
	
	private static Minecraft mc = Minecraft.getMinecraft();
	
	public static void mineBlock(BlockPos pos) {
		Block block = Utils.getBlock(pos);
		if(block instanceof BlockLiquid) {
			return;
		}
		
		RayTraceResult rayTrace = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), Utils.getVec3d(pos).addVector(0.5, 0.5, 0.5), false, true, true);
		
		if(rayTrace == null) {
			return;
		}
		if(rayTrace.getBlockPos() == null) {
			return;
		}
		
		Utils.faceBlock(rayTrace.getBlockPos());
		mc.player.swingArm(EnumHand.MAIN_HAND);
		mc.playerController.onPlayerDamageBlock(rayTrace.getBlockPos(), EnumFacing.UP);
	}
	
}
