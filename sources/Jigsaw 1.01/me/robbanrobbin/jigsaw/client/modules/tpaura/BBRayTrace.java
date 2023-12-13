package me.robbanrobbin.jigsaw.client.modules.tpaura;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class BBRayTrace {
	
	private Minecraft mc = Minecraft.getMinecraft();
	
	private int highestHitBlockHeight;
	private BlockPos highestBlock = null;
	private boolean hitBlock;
	private ArrayList<BlockPos> hitBlocks = new ArrayList<BlockPos>();
	
	public BBRayTrace(Vec3d pos1, Vec3d pos2, int checks, double bbSize) {
		AxisAlignedBB bb = mc.player.boundingBox;
		bb.expand(bbSize, 0, bbSize);
		double xDist = pos2.x - pos1.x;
		double yDist = pos2.y - pos1.y;
		double zDist = pos2.z - pos1.z;
		for(int i = 0; i < checks; i++) {
			bb = bb.offset((zDist / checks) * i, ((yDist / checks) * i) + 0.05, (xDist / checks) * i);
			for(BlockPos pos : mc.world.getCollidingBlockPositions(mc.player, bb)) {
				if(!hitBlocks.contains(pos)) {
					hitBlocks.add(pos);
				}
			}
		}
		if(hitBlocks.isEmpty()) {
			hitBlock = false;
			return;
		}
		hitBlock = true;
		int maxHeight = -1000;
		for(BlockPos pos : hitBlocks) {
			if(pos.getY() > maxHeight) {
				maxHeight = pos.getY();
				highestBlock = pos;
			}
		}
	}
	
	public boolean didHitBlock() {
		return hitBlock;
	}
	
	public ArrayList<BlockPos> getHitBlocks() {
		return hitBlocks;
	}
	
	public int getHighestHitBlockHeight() {
		return highestHitBlockHeight;
	}
	
	public BlockPos getHighestBlock() {
		return highestBlock;
	}
	
}
