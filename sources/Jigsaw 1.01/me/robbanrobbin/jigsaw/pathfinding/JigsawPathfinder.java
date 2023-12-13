package me.robbanrobbin.jigsaw.pathfinding;

import me.robbanrobbin.jigsaw.client.tools.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class JigsawPathfinder {
	
	public static final class NodeProcessors {
		
		public static final FlyingNodeProcessor FLYING = new FlyingNodeProcessor();
		public static final WalkingNodeProcessor WALKING = new WalkingNodeProcessor();
		public static final MiningNodeProcessor MINING = new MiningNodeProcessor();
		public static final MineplexTpAuraNodeProcessor MINEPLEX_TPAURA = new MineplexTpAuraNodeProcessor();
		
	}
	
	private Minecraft mc;
	private int maxComputations;
	private NodeProcessor nodeProcessor;
	
	public JigsawPathfinder(Minecraft mc, int maxComputations, NodeProcessor nodeProcessor) {
		this.mc = mc;
		this.maxComputations = maxComputations;
		this.nodeProcessor = nodeProcessor;
	}
	
	public PathfinderResult findPath(Vec3d from, Vec3d to) {
		BlockPos targetBlockPos = Utils.getBlockPos(to);
		BlockPos fromBlockPos = Utils.getBlockPos(from);
		
		BlockPos finalBlockPos = targetBlockPos;
		
		if(!nodeProcessor.isPassable(Utils.getBlockState(targetBlockPos))) {
			finalBlockPos = targetBlockPos.up();
			if(!nodeProcessor.isPassable(Utils.getBlockState(targetBlockPos.up()))) {
				finalBlockPos = targetBlockPos.up(2);
			}
		}
		
		nodeProcessor.getPath(fromBlockPos, finalBlockPos, maxComputations);
		return new PathfinderResult(nodeProcessor.path, !nodeProcessor.path.isEmpty());
	}
	
	public NodeProcessor getNodeProcessor() {
		return nodeProcessor;
	}
	
}
