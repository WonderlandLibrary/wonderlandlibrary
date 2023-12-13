package me.robbanrobbin.jigsaw.pathfinding;

import java.util.ArrayList;

import me.robbanrobbin.jigsaw.client.tools.Utils;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class MiningNodeProcessor extends NodeProcessor {
	
	@Override
	protected ArrayList<Node> getNeighbors(Node node) {
		ArrayList<Node> neighbors = new ArrayList<Node>();
		BlockPos nodeBlockPos = node.getBlockpos();
		if(node.isVclipableDown()) {
			neighbors.add(createNode(node.getVclipPosDown()));
		}
		if(node.isVclipableUp()) {
			neighbors.add(createNode(node.getVclipPosUp()));
		}
		for(BlockPos pos : BlockPos.getAllInBox(nodeBlockPos.add(1, 1, 1), nodeBlockPos.add(-1, -1, -1))) {
			if(pos.equals(nodeBlockPos)) {
				continue;
			}
			if(pos.getX() > nodeBlockPos.getX() && pos.getZ() > nodeBlockPos.getZ()) {
				continue;
			}
			if(pos.getX() < nodeBlockPos.getX() && pos.getZ() < nodeBlockPos.getZ()) {
				continue;
			}
			if(pos.getX() < nodeBlockPos.getX() && pos.getZ() > nodeBlockPos.getZ()) {
				continue;
			}
			if(pos.getX() > nodeBlockPos.getX() && pos.getZ() < nodeBlockPos.getZ()) {
				continue;
			}
//			if(pos.getY() < nodeBlockPos.getY() && (pos.getX() != nodeBlockPos.getX() || pos.getZ() != nodeBlockPos.getZ())) {
//				continue;
//			}
//			if(pos.getY() == nodeBlockPos.getY() + 1 && !(pos.getX() == nodeBlockPos.getX() && pos.getZ() == nodeBlockPos.getZ())) {
//				continue;
//			}
			if(pos.getY() > nodeBlockPos.getY() && pos.getX() == nodeBlockPos.getX() && pos.getZ() == nodeBlockPos.getZ()) {
				continue;
			}
			if(isBlockPosClosed_SLOW(pos.down(2))) {
				continue;
			}
			if(pos.getY() > nodeBlockPos.getY() && Utils.getBlock(nodeBlockPos.up(2)) == Blocks.BEDROCK) {
				continue;
			}
			if(pos.getY() < nodeBlockPos.getY() && Utils.getBlock(pos.up(2)) == Blocks.BEDROCK) {
				continue;
			}
			Node created = createNode(pos);
			if(created != null) {
				neighbors.add(created);
			}
		}
		return neighbors;
	}
	
	@Override
	public double getGCostMultiplier(BlockPos pos) {
		return Utils.isBlockPosAir(pos) && Utils.isBlockPosAir(pos.up()) ? 0.1 : 10;
	}
	
	@Override
	public Node createNode(BlockPos pos) {
		boolean walkable = true;
		if(walkable) {
			if(!isWalkable(Utils.getBlockState(pos.down()))) {
				walkable = false;
			}
		}
		if(walkable) {
			if(!isPassable(Utils.getBlockState(pos))) {
				walkable = false;
			}
		}
		if(walkable) {
			if(!isPassable(Utils.getBlockState(pos.up()))) {
				walkable = false;
			}
		}
		if(walkable) {
			if(!isSafe(Utils.getBlockState(pos.up(2)))) {
				walkable = false;
			}
		}
		for(int i = 0; i < 2; i++) {
			if(walkable) {
				if(!isSafe(Utils.getBlockState(pos.north().up(i)))) {
					walkable = false;
				}
			}
			else {
				break;
			}
		}
		for(int i = 0; i < 2; i++) {
			if(walkable) {
				if(!isSafe(Utils.getBlockState(pos.south().up(i)))) {
					walkable = false;
				}
			}
			else {
				break;
			}
		}
		for(int i = 0; i < 2; i++) {
			if(walkable) {
				if(!isSafe(Utils.getBlockState(pos.east().up(i)))) {
					walkable = false;
				}
			}
			else {
				break;
			}
		}
		for(int i = 0; i < 2; i++) {
			if(walkable) {
				if(!isSafe(Utils.getBlockState(pos.west().up(i)))) {
					walkable = false;
				}
			}
			else {
				break;
			}
		}
		return new Node(walkable, pos).setId(pos.hashCode());
	}
	
	@Override
	public boolean isPassable(IBlockState blockState) {
		return blockState.getMaterial() != Material.LAVA && blockState.getMaterial() != Material.WATER && blockState.getBlock() != Blocks.BEDROCK;
	}
	
	@Override
	public boolean isWalkable(IBlockState blockState) {
		return blockState.getMaterial() != Material.AIR && (blockState.getBlock() instanceof BlockLilyPad || blockState.getBlock() instanceof BlockLadder || blockState.isFullBlock());
	}
	
}
