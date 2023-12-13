package me.robbanrobbin.jigsaw.pathfinding;

import java.util.ArrayList;

public class PathfinderResult {
	
	private ArrayList<Node> path;
	private boolean foundPath;
	
	public PathfinderResult(ArrayList<Node> path, boolean foundPath) {
		this.path = path;
		this.foundPath = foundPath;
	}
	
	public ArrayList<Node> getPath() {
		return path;
	}
	
	public boolean didFindPath() {
		return foundPath;
	}
	
}
