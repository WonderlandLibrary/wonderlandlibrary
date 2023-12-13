package me.robbanrobbin.jigsaw.client.modules;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import com.google.gson.JsonSyntaxException;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;

public class BlurBufferMod extends Module {
	
	public static Framebuffer blurBuffer;
	
	public static WaitTimer blurBufferTimer = new WaitTimer();
	public static ShaderGroup blurShaderGroup;
	
	public static boolean blurBufferRendered = false;

	public BlurBufferMod() {
		super("BlurBufferMod", Keyboard.KEY_NONE, Category.HIDDEN);
	}
	
	@Override
	public void onClientLoad() {
		blurBuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, false);
		blurBuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
		super.onClientLoad();
	}
	
	@Override
	public void onResize(int width, int height) {
		blurBuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, false);
		blurBuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
		this.loadShader(Jigsaw.blurShader, blurBuffer);
		blurBufferRendered = false;
		super.onResize(width, height);
	}
	
	@Override
	public void onRender() {
		mc.entityRenderer.useShader = false;
		if(this.getBlurShaderGroup() == null) {
			this.loadShader(Jigsaw.blurShader, blurBuffer);
		}
		super.onRender();
	}
	
	public static void overrideTimer() {
		blurBufferTimer.time = 0;
	}
	
	@Override
	public boolean dontToggleOnLoadModules() {
		return true;
	}

	@Override
	public boolean getEnableAtStartup() {
		return true;
	}
	
	public static boolean isBlurBufferRendered() {
		return blurBufferRendered;
	}
	
	public static void setBlurBufferRendered() {
		BlurBufferMod.blurBufferRendered = true;
	}
	
	public void loadShader(ResourceLocation resourceLocationIn, Framebuffer framebuffer)
    {
        if (OpenGlHelper.isFramebufferEnabled())
        {
            try {
            	this.blurShaderGroup = new ShaderGroup(this.mc.getTextureManager(), this.mc.getResourceManager(), framebuffer, resourceLocationIn);
                this.blurShaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
            }
            catch(Exception e) {
            	e.printStackTrace();
            }
        }
    }
	
	public static ShaderGroup getBlurShaderGroup() {
		return blurShaderGroup;
	}

}
