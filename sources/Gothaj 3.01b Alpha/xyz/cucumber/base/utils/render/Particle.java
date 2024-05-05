package xyz.cucumber.base.utils.render;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import xyz.cucumber.base.utils.RenderUtils;

public class Particle {
	
	private double x,y, speed;
	
	private int size;
	
	private int color;
	
	private float yaw;
	
	private float time;
	
	private int timeFinal;

	public Particle(double x, double y, double speed, int size, int color, float yaw, float time) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.size = size;
		this.color = color;
		this.yaw = yaw;
		this.time = System.nanoTime()/1000000f+time;
		timeFinal = (int) time;
	}

	public boolean draw() {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		if(time < System.nanoTime()/1000000f || x < 0 || y < 0 || x > sr.getScaledWidth() || y > sr.getScaledHeight()) {
			return true;
		}
		Color c = new Color(color);
		
		int alpha = (int) (255F/timeFinal*((int)(time-System.nanoTime()/1000000f)));
		
		if(alpha < 0) return true;
		if(alpha > 255) alpha = 255;
		
		int finalc = new Color(c.getRed(),c.getGreen(),c.getBlue(),alpha).getRGB();
		this.update();
		RenderUtils.drawPoint(x,y,finalc,size);
		
		return false;
	}
	
	public void update() {
		x += Math.sin(Math.toRadians(yaw))*speed;
		y += Math.cos(Math.toRadians(yaw))*speed;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
	
	
}
