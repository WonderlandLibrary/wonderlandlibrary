package moonsense.gui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import moonsense.utils.RoundedUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;

public class SplashProgress {
	
	private static final int MAX = 7;
	private static int PROGRESS = 0;
	private static String CURRENT = "";
	private static ResourceLocation splash;
	private static UnicodeFontRenderer ufr;
	
	public static void update() {
		if(Minecraft.getMinecraft() == null || Minecraft.getMinecraft().getLanguageManager() == null) {
			return;
		}
		drawSplash(Minecraft.getMinecraft().getTextureManager());
	}
	
	public static void setProgress(int givenProgress, String givenText) {
		PROGRESS = givenProgress;
		CURRENT = givenText;
		update();
	}
	
	public static void drawSplash(TextureManager tm) {
		
		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
		int scaleFactor = scaledResolution.getScaleFactor();
		
		Framebuffer framebuffer = new Framebuffer(scaledResolution.getScaledWidth() * scaleFactor, scaledResolution.getScaledHeight() * scaleFactor, true);
		framebuffer.bindFramebuffer(false);
		
		GlStateManager.matrixMode(GL11.GL_PROJECTION);
		GlStateManager.loadIdentity();
		GlStateManager.ortho(0.0D, (double) scaledResolution.getScaledWidth(), (double) scaledResolution.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		GlStateManager.loadIdentity();
		GlStateManager.translate(0.0F, 0.0F, -2000);
		GlStateManager.disableLighting();
		GlStateManager.disableFog();
		GlStateManager.disableDepth();
		GlStateManager.enableTexture2D();
		
		if(splash == null) {
			splash = new ResourceLocation("moonsense/splash.jpg");
		}
		
		tm.bindTexture(splash);
		
		GlStateManager.resetColor();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, 1920, 1080, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), 1920, 1080);
		drawProgress();
		framebuffer.unbindFramebuffer();
		framebuffer.framebufferRender(scaledResolution.getScaledWidth() * scaleFactor, scaledResolution.getScaledHeight() * scaleFactor);
		
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(516, 0.1F);
		
		Minecraft.getMinecraft().updateDisplay();

	}
	
	private static void drawProgress() {
		
		if(Minecraft.getMinecraft().gameSettings == null || Minecraft.getMinecraft().getTextureManager() == null) {
			return;
		}
		
		if(ufr == null) {
			ufr = UnicodeFontRenderer.getFontOnPC("Arial", 20);
		}
		
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		
		double nProgress = (double)PROGRESS;
		double calc = (nProgress / MAX * sr.getScaledWidth());
		
		Gui.drawRect(0, sr.getScaledHeight() - 35, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0, 0, 0, 50).getRGB());
		
		GlStateManager.resetColor();
		resetTexture();
		
		ufr.drawString(CURRENT, 20, sr.getScaledHeight() - 25, 0xFFFFFFFF);
		
		String step = PROGRESS + "/" + MAX;
		ufr.drawString(step, sr.getScaledWidth() - 20 - ufr.getStringWidth(step), sr.getScaledHeight() - 25, 0xe1e1e1FF);
		
		GlStateManager.resetColor();
		resetTexture();
		
		Gui.drawRect(0, sr.getScaledHeight() - 2, (int)calc, sr.getScaledHeight(), new Color(149, 201, 144).getRGB());
		
		Gui.drawRect(0, sr.getScaledHeight() - 2, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0, 0, 0, 10).getRGB());
		
	}
	
	private static void resetTexture() {
		GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName = -1;
	}
	
}
