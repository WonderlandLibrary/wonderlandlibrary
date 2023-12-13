package me.robbanrobbin.jigsaw.minimap;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;

public class MinimapInfo {
	
	private ArrayList<MinimapEntry.Block> blockEntries = new ArrayList<MinimapEntry.Block>();
	
	private ArrayList<MinimapEntry.Entity> entityEntries = new ArrayList<MinimapEntry.Entity>();
	
	private ArrayList<MinimapEntry.Arrow> arrowEntries = new ArrayList<MinimapEntry.Arrow>();
	
	public void addColorAndLightData(int x, int z, float red, float green, float blue) {
		blockEntries.add(new MinimapEntry.Block(x, z, red, green, blue));
	}
	
	public void addEntityData(int x, int z, Entity entity) {
		entityEntries.add(new MinimapEntry.Entity(x, z, entity));
	}

	public void addArrowData(int x, int z, EntityArrow arrow) {
		arrowEntries.add(new MinimapEntry.Arrow(x, z, arrow));
	}
	
	public int getBlockEntriesSize() {
		return blockEntries.size();
	}
	
	public int getEntityEntriesSize() {
		return entityEntries.size();
	}
	
	public int getArrowEntriesSize() {
		return arrowEntries.size();
	}
	
	public ArrayList<MinimapEntry.Block> getBlockEntries() {
		return blockEntries;
	}
	
	public ArrayList<MinimapEntry.Entity> getEntityEntries() {
		return entityEntries;
	}
	
	public ArrayList<MinimapEntry.Arrow> getArrowEntries() {
		return arrowEntries;
	}
	
}