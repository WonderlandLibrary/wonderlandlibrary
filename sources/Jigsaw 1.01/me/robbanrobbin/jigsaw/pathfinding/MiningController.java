package me.robbanrobbin.jigsaw.pathfinding;

import me.robbanrobbin.jigsaw.client.tools.AIUtils;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public abstract class MiningController extends PathfinderController {

	public MiningController(PathfinderResult result) {
		super(result);
	}

	@Override
	public void update() {
		
		if(positions.size() == 0) {
			onFinish();
			return;
		}
		
		Vec3d getToVec = positions.get(positionIndex);
		BlockPos getToBlockPos = Utils.getBlockPos(getToVec);
		BlockPos playerBlockPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
		
		if(playerBlockPos.getX() == getToBlockPos.getX() && playerBlockPos.getY() == getToBlockPos.getY() && playerBlockPos.getZ() == getToBlockPos.getZ()) { //player is at next location
			if(positionIndex + 1 >= positions.size() - 1) {
				onFinish();
				return;
			}
			positionIndex++;
			return;
		}
		
		
		if(getToBlockPos.getY() - playerBlockPos.getY() >= 2) {
			onFinish();
			return;
		}
		
		if(playerBlockPos.getX() == getToBlockPos.getX() && playerBlockPos.getY() - 1 == getToBlockPos.getY() && playerBlockPos.getZ() == getToBlockPos.getZ() 
				&& !Utils.isBlockPosAir(playerBlockPos.add(0, -1, 0))) { 
			AIUtils.mineBlock(getToBlockPos);
			return;
		}

		Utils.facePos(getToVec);
		
		boolean passable = true;
		for(int i = 2; i >= 0; i--) {
			if(i == 2 && (getToBlockPos.getY() != playerBlockPos.getY() - 1)) {
				continue;
			}
			BlockPos blockPos = getToBlockPos.up(i);
			IBlockState blockState = Utils.getBlockState(blockPos);
			if(!JigsawPathfinder.NodeProcessors.WALKING.isPassable(blockState)) {
				AIUtils.mineBlock(blockPos);
				passable = false;
				break;
			}
		}
		
		if(passable) {
			if(mc.player.onGround && getToVec.y == playerBlockPos.getY() + 1) {
				if(!JigsawPathfinder.NodeProcessors.WALKING.isPassable(Utils.getBlockState(playerBlockPos.up(2)))) {
					AIUtils.mineBlock(playerBlockPos.up(2));
					passable = false;
				}
			}
			if(mc.player.onGround && getToVec.y < mc.player.posY) {
				if(!JigsawPathfinder.NodeProcessors.WALKING.isPassable(Utils.getBlockState(getToBlockPos))) {
					AIUtils.mineBlock(getToBlockPos);
					passable = false;
				}
			}
		}
		
		if(passable) { //mine block if it is not passable
			if(mc.player.onGround && getToVec.y > mc.player.posY) {
				mc.player.jump();
			}
			else {
				if(mc.player.onGround) {
					mc.player.moveFlying(0, 0, 1f, 0.1f);
				}
				else {
					mc.player.moveFlying(0, 0, 0.5f, 0.1f);
				}
			}
		}
		
	}
	
}
