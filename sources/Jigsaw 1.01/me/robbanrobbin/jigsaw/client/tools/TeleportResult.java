package me.robbanrobbin.jigsaw.client.tools;

import java.util.ArrayList;

import me.robbanrobbin.jigsaw.pathfinding.Node;
import net.minecraft.util.math.Vec3d;

public class TeleportResult {
	
	public ArrayList<Vec3d> positions;
	public ArrayList<Vec3d> positionsBack;
	public ArrayList<Node> triedPaths;
	public Vec3d lastPos;
	public ArrayList<Node> path;
	public boolean foundPath;
	
	public TeleportResult(ArrayList<Vec3d> positions, ArrayList<Vec3d> positionsBack, ArrayList<Node> triedPaths, ArrayList<Node> path, Vec3d lastPos, boolean foundPath) {
		this.positions = positions;
		this.positionsBack = positionsBack;
		this.triedPaths = triedPaths;
		this.path = path;
		this.foundPath = foundPath;
		this.lastPos = lastPos;
	}
	
}
