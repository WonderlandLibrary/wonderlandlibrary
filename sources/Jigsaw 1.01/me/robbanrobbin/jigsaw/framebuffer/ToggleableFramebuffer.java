package me.robbanrobbin.jigsaw.framebuffer;

import me.robbanrobbin.jigsaw.client.TickTimer;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.shader.Framebuffer;

public class ToggleableFramebuffer {
	
	private boolean enabled;
	public Framebuffer frameBuffer;
	private Module module;
	private String name;
	private boolean renderToScreen;
	private TickTimer renderTimer;
	private boolean useRenderTimer;
	private int tickDelay;
	
	public ToggleableFramebuffer(Module module, String name, Framebuffer framebuffer) {
		this.module = module;
		this.name = name;
		this.frameBuffer = framebuffer;
		enabled = true;
		renderToScreen = true;
	}
	
	public ToggleableFramebuffer(Module module, String name, Framebuffer framebuffer, int tickDelay) {
		this(module, name, framebuffer);
		this.tickDelay = tickDelay;
		renderTimer = new TickTimer();
		useRenderTimer = true;
	}
	
	public void updateFrameBuffer(Framebuffer frameBuffer) {
		this.frameBuffer.deleteFramebuffer();
		this.frameBuffer = frameBuffer;
	}
	
	public Framebuffer getFramebuffer() {
		return frameBuffer;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public Module getModule() {
		return module;
	}
	
	public String getName() {
		return name;
	}
	
	public void setRenderToScreen(boolean renderToScreen) {
		this.renderToScreen = renderToScreen;
	}
	
	public boolean renderToScreen() {
		return renderToScreen;
	}
	
	public boolean useRenderTimer() {
		return useRenderTimer;
	}
	
	public TickTimer getRenderTimer() {
		return renderTimer;
	}
	
	public int getTickDelay() {
		return tickDelay;
	}
	
}
