package me.robbanrobbin.jigsaw.client.waypoints;

import net.minecraft.util.math.BlockPos;

public class Waypoint {
	
	private BlockPos blockPos;
	private String name;
	
	public Waypoint(BlockPos blockPos) {
		this.blockPos = blockPos;
	}
	
	public Waypoint(BlockPos blockPos, String name) {
		this.blockPos = blockPos;
		this.name = name;
	}
	
	public BlockPos getBlockPos() {
		return blockPos;
	}
	
	public String getName() {
		return name;
	}
	
}
