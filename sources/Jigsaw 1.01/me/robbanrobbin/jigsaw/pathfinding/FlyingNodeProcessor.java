package me.robbanrobbin.jigsaw.pathfinding;

import me.robbanrobbin.jigsaw.client.tools.Utils;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class FlyingNodeProcessor extends NodeProcessor {

	@Override
	public void process(Node currentNode, Node endNode) {
		for(Node neighbor : getNeighbors(currentNode)) {
			boolean vclipableDown = false;
			boolean vclipableUp = false;
			if((!neighbor.isWalkable() && !((vclipableDown = neighbor.isVclipableDown()) || (vclipableUp = neighbor.isVclipableUp()))) || isNodeClosed(neighbor)) {
				continue;
			}
			if(vclipableDown) {
				neighbor = createNode(neighbor.getBlockpos().up());
			}
			if(vclipableUp) {
				neighbor = createNode(neighbor.getBlockpos().down());
			}

			double tentativeGCost = currentNode.gCost + (Node.distance(currentNode.getBlockpos(), neighbor.getBlockpos())) * getGCostMultiplier(neighbor.getBlockpos());

			if(isNodeOpen(neighbor)) {
				if(neighbor.gCost > tentativeGCost) {
					neighbor.gCost = tentativeGCost;
				}
			}
			else {
				neighbor.gCost = tentativeGCost;
				openNode(neighbor);
			}
			
			neighbor.hCost = Node.distance(neighbor.getBlockpos(), endNode.getBlockpos());
			neighbor.fCost = neighbor.gCost + neighbor.hCost;
			neighbor.parent = currentNode;
		}
	}
	
	@Override
	public Node createNode(BlockPos pos) {
		Node node = new Node(!isStairBlock(Utils.getBlock(pos.down())) && isPassable(Utils.getBlockState(pos)) && isPassable(Utils.getBlockState(pos.up())), pos).setId(pos.hashCode());
		
		node.setVclipableDown(isVclipableDown(node));
		node.setVclipableUp(isVclipableUp(node));
		return node;
	}
	
	@Override
	public boolean isPassable(IBlockState blockState) {
		return blockState.getMaterial() == Material.AIR || (blockState.getMaterial() == Material.PLANTS && !(blockState.getBlock() instanceof BlockLilyPad)) || blockState.getMaterial() == Material.VINE 
				|| blockState.getMaterial() == Material.WATER || blockState.getMaterial() == Material.CIRCUITS || 
				blockState.getBlock() instanceof BlockSign || blockState.getBlock() instanceof BlockWallSign || blockState.getBlock() instanceof BlockLadder;
	}
	
	@Override
	public boolean isWalkable(IBlockState block) {
		return !(block.getMaterial() == Material.AIR) && (block.getBlock() instanceof BlockLilyPad || block.getMaterial() == Material.VINE || block.getBlock() instanceof BlockLadder || block.isFullBlock());
	}
	
	public boolean isVclipableDown(Node node) {
		if(!node.isWalkable()) {
			return false;
		}
		BlockPos blockPos = node.getBlockpos();
		if(isPassable(Utils.getBlockState(blockPos.down()))) {
			return false;
		}
		for(int i = 1; i < 9; i++) {
			BlockPos pos = new BlockPos(blockPos.getX(), blockPos.getY() - i, blockPos.getZ());
			if(isPassable(Utils.getBlockState(pos)) && i < 8
					&& isPassable(Utils.getBlockState(pos.up()))) {
				node.setVclipPosDown(pos);
				return true;
			}
		}
		return false;
	}
	
	public boolean isVclipableUp(Node node) {
		if(!node.isWalkable()) {
			return false;
		}
		BlockPos blockPos = node.getBlockpos();
		if(isPassable(Utils.getBlockState(blockPos.up()))) {
			return false;
		}
		for(int i = 1; i < 9; i++) {
			BlockPos pos = new BlockPos(blockPos.getX(), blockPos.getY() + i, blockPos.getZ());
			if(isPassable(Utils.getBlockState(pos)) && i < 8
					&& isPassable(Utils.getBlockState(pos.up()))) {
				node.setVclipPosUp(pos);
				return true;
			}
		}
		return false;
	}
	
}
