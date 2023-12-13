package me.robbanrobbin.jigsaw.framebuffer;

import java.util.ArrayList;

import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.shader.Framebuffer;

public class FramebufferHandler {
	
	public double overridePosX;
	public double overridePosY;
	public double overridePosZ;
	
	public float overrideYaw;
	public float overridePitch;
	
	public boolean overrideThisTick = false;
	
	public void setOverride(double overridePosX, double overridePosY, double overridePosZ, float overrideYaw, float overridePitch) {
		
		this.overridePosX = overridePosX;
		this.overridePosY = overridePosY;
		this.overridePosZ = overridePosZ;
		
		this.overrideYaw = overrideYaw;
		this.overridePitch = overridePitch;
		
		overrideThisTick = true;
		
	}
	
	public boolean rendering = false;
	
	public ArrayList<ToggleableFramebuffer> tfbs = new ArrayList<ToggleableFramebuffer>();
	
	public FramebufferHandler() {
		
	}
	
	public void update() {
		for(ToggleableFramebuffer tfb : tfbs) {
			if(tfb.useRenderTimer()) {
//				tfb.getRenderTimer().tick();
			}
		}
	}
	
	public void newFramebuffer(Module mod, String name, int width, int height, boolean useDepth) {
		
		Framebuffer newBuffer = new Framebuffer(width, height, useDepth);
		newBuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
		
		ToggleableFramebuffer tfb = new ToggleableFramebuffer(mod, name, newBuffer);
		tfbs.add(tfb);
		
		
	}
	
	public void newFramebuffer(Module mod, String name, int width, int height, boolean useDepth, int tickDelay) {
		
		Framebuffer newBuffer = new Framebuffer(width, height, useDepth);
		newBuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
		
		ToggleableFramebuffer tfb = new ToggleableFramebuffer(mod, name, newBuffer, tickDelay);
		tfbs.add(tfb);
		
		
	}
	
	public void updateFramebuffer(String name, int p_i45078_1_, int p_i45078_2_, boolean p_i45078_3_) {
		
		Framebuffer newBuffer = new Framebuffer(p_i45078_1_, p_i45078_2_, p_i45078_3_);
		newBuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
		
		
		ToggleableFramebuffer tfb = getToggleableFramebufferByName(name);
		
		tfb.updateFrameBuffer(newBuffer);
		
	}
	
	public ToggleableFramebuffer getToggleableFramebufferByName(String name) {
		for(ToggleableFramebuffer buf : tfbs) {
			if(buf.getName().equals(name)) {
				return buf;
			}
		}
		return null;
	}
	
}
