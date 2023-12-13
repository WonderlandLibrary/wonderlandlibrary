package me.robbanrobbin.jigsaw.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockWall;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

public abstract class NodeProcessor {
	
	public ArrayList<Node> path = new ArrayList<Node>();
	public ArrayList<Node> triedPaths = new ArrayList<Node>();
	
	ArrayList<Node> openNodes = new ArrayList<Node>();
	HashMap<Integer, Node> hashOpenNodes = new HashMap<Integer, Node>();
	HashMap<Integer, Node> hashClosedNodes = new HashMap<Integer, Node>();
	
	private WaitTimer timer = new WaitTimer();
	
	private static Minecraft mc = Minecraft.getMinecraft();
	
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
			if(pos.getY() < nodeBlockPos.getY() && !isPassable(Utils.getBlockState(pos.up(2)))) {
				continue;
			}
			if(pos.getY() > nodeBlockPos.getY() && !isPassable(Utils.getBlockState(nodeBlockPos.up(2)))) {
				continue;
			}
			if(pos.getY() != nodeBlockPos.getY()) {
				continue;
			}
			Node created = createNode(pos);
			if(created != null) {
				neighbors.add(created);
			}
		}
		return neighbors;
	}
	
	long totalTime = 0;
	
	public void closeNode(Node node) {
		openNodes.remove(node);
		hashOpenNodes.remove(node.id);
		hashClosedNodes.put(node.id, node);
	}
	
	public void openNode(Node node) {
		hashOpenNodes.put(node.id, node);
		openNodes.add(node);
	}
	
	public void getPath(BlockPos start, BlockPos finish, int maxComputations) {
		path = new ArrayList<Node>();
		triedPaths = new ArrayList<Node>();
		totalTime = 0;
		hashOpenNodes.clear();
		hashClosedNodes.clear();
		openNodes.clear();
		Node startNode = createNode(start);
		
		Node endNode = createNode(finish);
		
		startNode.gCost = 0;
		startNode.hCost = Node.distance(startNode.getBlockpos(), endNode.getBlockpos());
		startNode.fCost = startNode.gCost + startNode.hCost;
		
		openNode(startNode);
		
		long now1 = System.nanoTime();
		
		int count = 0;
		while(hashOpenNodes.values().size() > 0) {
			
			if(count > maxComputations) {
				path.clear();
				return;
			}
			
			Node currentNode = null;

			double minFCost = Double.POSITIVE_INFINITY;
			for(int i = 0; i < openNodes.size(); i++) {
				Node openNode = openNodes.get(i);
				if(openNode.fCost < minFCost || (openNode.fCost == minFCost && openNode.hCost < currentNode.hCost)) {
					minFCost = openNode.fCost;
					currentNode = openNode;
				}
			}
			
			triedPaths.add(currentNode);
			
//			System.out.println((currentNode != null) + " - " + openNodes.size());

			if(currentNode.getBlockpos().equals(endNode.getBlockpos())) { //path has been found
				endNode.parent = currentNode;
				retracePath(startNode, endNode);
				return;
			}
			
			closeNode(currentNode);
			
			process(currentNode, endNode);
			
			count++;
		}
	}
	
	public void process(Node currentNode, Node endNode) {
		for (Node neighbor : getNeighbors(currentNode)) {

			if (!neighbor.isWalkable() || isNodeClosed(neighbor)) {
				continue;
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
	
	protected boolean isNodeOpen(Node node) {
		boolean open = hashOpenNodes.get(node.id) != null;
		return open;
	}
	
	protected boolean isNodeClosed(Node node) {
		boolean closed = hashClosedNodes.get(node.id) != null;
		return closed;
	}
	
	protected boolean isBlockPosClosed_SLOW(BlockPos blockPos) {
		for(Integer hashCode : hashClosedNodes.keySet()) {
			if(hashCode == blockPos.hashCode()) {
				return true;
			}
		}
		return false;
	}
	
	private void retracePath(Node startNode, Node endNode) {
		ArrayList<Node> path = new ArrayList<Node>();
		
		Node currentNode = endNode;
		
		while(!currentNode.equals(startNode)) {
			path.add(currentNode);
			currentNode = currentNode.parent;
		}
		path.add(startNode);
		Collections.reverse(path);
		
		this.path = path;
	}
	
	public double getGCostMultiplier(BlockPos pos) {
		return 1;
	}
	
	public abstract Node createNode(BlockPos pos);
	
	public abstract boolean isPassable(IBlockState blockState);
	
	public abstract boolean isWalkable(IBlockState blockState);
	
	public boolean isSafe(IBlockState blockState) {
		return !(blockState.getBlock() instanceof BlockLiquid);
	}
	
	public boolean isStairBlock(Block block) {
		return block instanceof BlockFence || block instanceof BlockFenceGate|| block instanceof BlockWall;
	}
	
}
