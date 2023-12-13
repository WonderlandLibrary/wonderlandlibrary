package me.robbanrobbin.jigsaw.pathfinding;

import me.robbanrobbin.jigsaw.client.tools.Utils;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class WalkingNodeProcessor extends NodeProcessor {
	
	@Override
	public Node createNode(BlockPos pos) {
		return new Node(isWalkable(Utils.getBlockState(pos.down())) && isPassable(Utils.getBlockState(pos)) && isPassable(Utils.getBlockState(pos.up())), pos).setId(pos.hashCode());
	}
	
	@Override
	public boolean isPassable(IBlockState blockState) {
		return blockState.getMaterial() == Material.AIR || (blockState.getMaterial() == Material.PLANTS && !(blockState.getBlock() instanceof BlockLilyPad)) || blockState.getMaterial() == Material.VINE 
				|| blockState.getMaterial() == Material.WATER || blockState.getMaterial() == Material.CIRCUITS || 
				blockState.getBlock() instanceof BlockSign || blockState.getBlock() instanceof BlockWallSign || blockState.getBlock() instanceof BlockLadder;
	}
	
	@Override
	public boolean isWalkable(IBlockState blockState) {
		return blockState.getMaterial() != Material.AIR && (blockState.getBlock() instanceof BlockLilyPad || blockState.getBlock() instanceof BlockLadder || blockState.isFullBlock());
	}
	
}
