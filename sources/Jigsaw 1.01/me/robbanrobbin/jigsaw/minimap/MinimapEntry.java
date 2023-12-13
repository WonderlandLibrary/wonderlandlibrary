package me.robbanrobbin.jigsaw.minimap;

import net.minecraft.entity.projectile.EntityArrow;

public class MinimapEntry {
	
	public static class Block extends MinimapEntry {
		
		int x;
		int z;
		float red, green, blue;
		
		public Block(int x, int z, float red, float green, float blue) {
			this.x = x;
			this.z = z;
			this.red = red;
			this.green = green;
			this.blue = blue;
		}
		
		public float getBlue() {
			return blue;
		}
		
		public float getGreen() {
			return green;
		}
		
		public float getRed() {
			return red;
		}
		
		public int getX() {
			return x;
		}
		
		public int getZ() {
			return z;
		}
		
	}
	
	public static class Entity extends MinimapEntry {
		
		int x;
		int z;
		net.minecraft.entity.Entity entity;
		
		public Entity(int x, int z, net.minecraft.entity.Entity entity) {
			this.x = x;
			this.z = z;
			this.entity = entity;
		}
		
		public int getX() {
			return x;
		}
		
		public int getZ() {
			return z;
		}
		
		public net.minecraft.entity.Entity getEntity() {
			return entity;
		}
		
	}
	
	public static class Arrow extends MinimapEntry {
		
		int x;
		int z;
		EntityArrow arrow;
		
		public Arrow(int x, int z, EntityArrow arrow) {
			this.x = x;
			this.z = z;
			this.arrow = arrow;
		}
		
		public int getX() {
			return x;
		}
		
		public int getZ() {
			return z;
		}
		
		public EntityArrow getArrow() {
			return arrow;
		}
		
	}
	
}
