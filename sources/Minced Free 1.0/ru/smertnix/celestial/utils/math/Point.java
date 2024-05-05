package ru.smertnix.celestial.utils.math;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class Point {
	public static ArrayList<Point> p2 = new ArrayList();
	private double x, y, z;
	public double posX, posY, posZ;
	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		p2.add(this);
	}
	
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}
	public void setZ(double z) {
		this.z = z;
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}
	
	public float getDistanceToEntity(Entity entityIn)
    {
        float f = (float)(this.x - entityIn.posX);
        float f1 = (float)(this.y - entityIn.posY);
        float f2 = (float)(this.z - entityIn.posZ);
        return MathHelper.sqrt(f * f + f1 * f1 + f2 * f2) - (Minecraft.getMinecraft().player.isInWater() ? 0.5f : 0);
    }

    public double getDistance(double x, double y, double z)
    {
        double d0 = this.x - x;
        double d1 = this.y - y;
        double d2 = this.z - z;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }
    
    public static boolean doesExist(Point p) {
    	for (Point p3 : p2) {
    		if (p3.x == p.x && p3.y == p.y && p3.z == p.z) {
    			return true;
    		}
    	}
		return false;
    }
    
    public static boolean doesExist(double x, double y, double z) {
    	for (Point p3 : p2) {
    		if (p3.x == x && p3.y == y && p3.z == z) {
    			return true;
    		}
    	}
		return false;
    }
}