package koks.api.pathfinder;

import net.minecraft.util.Vec3i;

public class Node {
	private Node parent;
	private Node child;
	private Vec3i vec;
	private Vec3i finish;
	private Vec3i start;
	private float gcost;
	private float hcost;
	private float fcost;
	private boolean lookedUpFromThere;
	private int pathcost = -1;
	public Node(Vec3i start,Vec3i vec,Vec3i finish,Node parent) {
		this.vec = vec;
		this.finish = finish;
		this.start = start;
		this.parent = parent;
		gcost = (float) Math.sqrt(vec.distanceSq(start));
		if (parent != null) {
			gcost = getPathCost();
		}
		hcost = (float) Math.sqrt(vec.distanceSq(finish));
		fcost = gcost + hcost;
	}
	public Vec3i getVec() {
		return vec;
	}
	public void setVec(Vec3i vec) {
		this.vec = vec;
	}
	public Vec3i getFinish() {
		return finish;
	}
	public void setFinish(Vec3i finish) {
		this.finish = finish;
	}
	public Vec3i getStart() {
		return start;
	}
	public void setStart(Vec3i start) {
		this.start = start;
	}
	public float getGcost() {
		return gcost;
	}
	public void setGcost(float gcost) {
		this.gcost = gcost;
	}
	public float getHcost() {
		return hcost;
	}
	public void setHcost(float hcost) {
		this.hcost = hcost;
	}
	public float getFcost() {
		return fcost;
	}
	public void setFcost(float fcost) {
		this.fcost = fcost;
	}
	public boolean isLookedUpFromThere() {
		return lookedUpFromThere;
	}
	public void setLookedUpFromThere(boolean lookedUpFromThere) {
		this.lookedUpFromThere = lookedUpFromThere;
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		parent.child = this;
		this.parent = parent;
	}
	public float getPathCost() {
		if (pathcost == -1) {
			pathcost = 1;
			if (parent != null) pathcost += parent.getPathCost();
		}
		return pathcost;
	}
	public Node getChild() {
		return child;
	}
	public void setChild(Node child) {
		this.child = child;
	}
}
