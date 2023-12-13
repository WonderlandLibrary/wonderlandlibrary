package me.robbanrobbin.jigsaw.gui.custom.clickgui.utils;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.util.BufferedImageUtil;

import com.jhlabs.image.BoxBlurFilter;
import com.jhlabs.image.GaussianFilter;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.modules.BlurBufferMod;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.Component;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.Container;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.SettingContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;

public class GuiUtils {
	
	public static float partialTicks = 1f;
	
	public static void translate(Component comp, boolean reverse) {
		Container parent = comp.getParent();
		if(parent != null) {
			GL11.glTranslated((reverse ? -parent.getX() : parent.getX()), (reverse ? -parent.getY() : parent.getY()), 0);
			Container parent2 = comp.getParent().getParent();
			if(parent2 != null) {
				GL11.glTranslated((reverse ? -parent2.getX() : parent2.getX()), (reverse ? -parent2.getY() : parent2.getY()), 0);
			}
		}
		if(comp.getPreY() != -999) {
			double x = comp.getX();
			double y = comp.getPreY() + (comp.getY() - comp.getPreY()) * Minecraft.getMinecraft().timer.renderPartialTicks;
			GL11.glTranslated((reverse ? -x : x), (reverse ? -y : y), 0);
//			System.out.println("pre: " + comp.getPreY() + " post: " + comp.getY());
		}
		else {
			double x = comp.getX();
			double y = comp.getY();
			GL11.glTranslated((reverse ? -x : x), (reverse ? -y : y), 0);
		}
	}
	
	public static Point calculateMouseLocation() {
		Minecraft minecraft = Minecraft.getMinecraft();
		int scale = minecraft.gameSettings.guiScale;
		if (scale == 0)
			scale = 1000;
		int scaleFactor = 0;
		while (scaleFactor < scale && minecraft.displayWidth / (scaleFactor + 1) >= 320
				&& minecraft.displayHeight / (scaleFactor + 1) >= 240)
			scaleFactor++;
		scaleFactor = 2;
		return new Point(Mouse.getX() / scaleFactor,
				minecraft.displayHeight / scaleFactor - Mouse.getY() / scaleFactor - 1);
	}
	
	public static void renderShadowVertical(double startAlpha, int size, double posX, double posY1, double posY2, boolean right, boolean edges) {
		renderShadowVertical(startAlpha, size, posX, posY1, posY2, right, edges, 0f, 0f, 0f);
	}
	
	public static void renderShadowVertical(double startAlpha, int size, double posX, double posY1, double posY2, boolean right, boolean edges, float red, float green, float blue) {
		
		double alpha = startAlpha;

		GlStateManager.alphaFunc(GL11.GL_GREATER, 0f);
		GL11.glLineWidth(1f);
		
		if(right) {
			for(double x = 0.5; x < size; x += 0.5) {
				
				GL11.glColor4d(red, green, blue, alpha);
				GL11.glBegin(GL11.GL_LINES);
				{
					glVertex2d(posX + x, posY1 - (edges ? x : 0));
					glVertex2d(posX + x, posY2 + (edges ? x : 0));
				}
				GL11.glEnd();
				
				double equation = startAlpha - (x / (double)size);
				alpha = equation;
			}
		}
		else {
			for(double x = 0; x < size; x += 0.5) {
				
				GL11.glColor4d(red, green, blue, alpha);
				GL11.glBegin(GL11.GL_LINES);
				{
					glVertex2d(posX - x, posY1 - (edges ? x : 0));
					glVertex2d(posX - x, posY2 + (edges ? x : 0));
				}
				GL11.glEnd();
				
				double equation = startAlpha - (x / (double)size);
				alpha = equation;
			}
		}
		
		
	}
	
	public static void renderShadowHorizontal(double startAlpha, int size, double posY, double posX1, double posX2, boolean up, boolean edges) {
		renderShadowHorizontal(startAlpha, size, posY, posX1, posX2, up, edges, 0f, 0f, 0f);
	}
	
	public static void renderShadowHorizontal(double startAlpha, int size, double posY, double posX1, double posX2, boolean up, boolean edges, float red, float green, float blue) {
		
		double alpha = startAlpha;

		GlStateManager.alphaFunc(GL11.GL_GREATER, 0f);
		GL11.glLineWidth(1f);
		
		if(!up) {
			for(double y = 0; y < size; y += 0.5) {
				
				GL11.glColor4d(red, green, blue, alpha);
				GL11.glBegin(GL11.GL_LINES);
				{
					glVertex2d(posX1 - (edges ? y : 0), posY + y);
					glVertex2d(posX2 + (edges ? y : 0), posY + y);
				}
				GL11.glEnd();
				
				double equation = startAlpha - (y / (double)size);
				alpha = equation;
			}
		}
		else {
			for(double y = 0.5; y < size; y += 0.5) {
				
				GL11.glColor4d(red, green, blue, alpha);
				GL11.glBegin(GL11.GL_LINES);
				{
					glVertex2d(posX1 - (edges ? y : 0) - 0.5, posY - y);
					glVertex2d(posX2 + (edges ? y : 0) - 0.5, posY - y);
				}
				GL11.glEnd();
				
				double equation = startAlpha - (y / (double)size);
				alpha = equation;
			}
		}
		
	}
	
	public static void enableDefaults() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL_CULL_FACE);
	}
	
	public static void disableDefaults() {
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL_CULL_FACE);
	}

	public static void enableTextureDefaults() {
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
	}

	public static void setColor(Color foreground) {
		GL11.glColor4f((float)foreground.getRed() / 255f, (float)foreground.getGreen() / 255f, (float)foreground.getBlue() / 255f, (float)foreground.getAlpha() / 255f);
	}
	
	public static void setColor(Color foreground, float alpha) {
		GL11.glColor4f((float)foreground.getRed() / 255f, (float)foreground.getGreen() / 255f, (float)foreground.getBlue() / 255f, alpha);
	}
	
	public static void setColorsBasedOnSettingContainer(Component component) {
		if(component.getParent() instanceof SettingContainer) {
			GuiUtils.setSettingContainerColor();
		}
		else {
			GL11.glColor4f(
					component.getBackground().getRed() / 255f,
					component.getBackground().getGreen() / 255f, 
					component.getBackground().getBlue() / 255f,
					ClientSettings.getBackGroundGuiColor().getAlpha() / 255f);
		}
	}
	
	public static void drawBlurBuffer(int x1, int y1, int x2, int y2, boolean setupOverlayRendering) {
		if(!ClientSettings.enableBlur) {
			return;
		}
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution resolution = new ScaledResolution(mc);
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		scissorHelper(x1, y1, x2, y2);
		drawBlurBuffer(setupOverlayRendering);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}
	
	public static void drawBlurBuffer(boolean setupOverlayRendering) {
		if(!ClientSettings.enableBlur || !BlurBufferMod.isBlurBufferRendered()) {
			return;
		}
		GL11.glPushMatrix();
		BlurBufferMod.blurBuffer.framebufferRenderExt(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, true);
		GL11.glPopMatrix();
		if(setupOverlayRendering) {
			Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
		}
		GlStateManager.enableDepth();
	}
	
	public static void drawBlurBufferNoSetupMatrix(boolean setupOverlayRendering) {
		if(!ClientSettings.enableBlur || !BlurBufferMod.isBlurBufferRendered()) {
			return;
		}
		GL11.glPushMatrix();
		BlurBufferMod.blurBuffer.framebufferRenderExtNoSetupMatrix(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, true, true);
		GL11.glPopMatrix();
		if(setupOverlayRendering) {
			Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
		}
		GlStateManager.enableDepth();
	}
	
	public static void scissorHelper(int x1, int y1, int x2, int y2) {
		x2 -= x1;
		y2 -= y1;
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution resolution = new ScaledResolution(mc);
		GL11.glScissor(x1 * resolution.getScaleFactor(), 
				mc.displayHeight - y1 * resolution.getScaleFactor() - y2 * resolution.getScaleFactor(), 
				x2 * resolution.getScaleFactor(), 
				y2 * resolution.getScaleFactor()
				);
	}
	
	public static void updateBlurBuffer(boolean setupOverlayRendering) {
		if(!ClientSettings.enableBlur || Jigsaw.isFrameBufferRendering()) {
			return;
		}
		Minecraft mc = Minecraft.getMinecraft();
        /**/
		//TODO Jigsaw blurbuffer
        
        if(Jigsaw.loaded && mc.player != null && mc.player.ticksExisted > 10 && BlurBufferMod.blurBufferTimer.hasTimeElapsed(1000 / ClientSettings.blurBufferFPS, true)) {
        	mc.getFramebuffer().unbindFramebuffer();
			
			BlurBufferMod.blurBuffer.bindFramebuffer(true);
			
			mc.getFramebuffer().framebufferRenderExt(mc.displayWidth, mc.displayHeight, true);
			
			if (OpenGlHelper.shadersSupported) {
				if (BlurBufferMod.getBlurShaderGroup() != null)
				{
					BlurBufferMod.setBlurBufferRendered();
					GlStateManager.matrixMode(5890);
                    GlStateManager.pushMatrix();
                    GlStateManager.loadIdentity();
                    BlurBufferMod.getBlurShaderGroup().loadShaderGroup(partialTicks);
                    GlStateManager.popMatrix();
				}
			}
			
			BlurBufferMod.blurBuffer.unbindFramebuffer();
    		mc.getFramebuffer().bindFramebuffer(true);
    		
            if(setupOverlayRendering) {
            	mc.entityRenderer.setupOverlayRendering();
            }
        }
	}
	
	public static int getPreferredFontARGBColor() {
		return 0xffffffff;
	}
	
	public static int toARGB(Color c) {
		return c.getAlpha() << 24 | c.getRed() << 16 | c.getGreen() << 8 | c.getBlue();
	}
	
	public static void setSettingContainerColor() {
		if(ClientSettings.enableBlur) {
			if(ClientSettings.glassMode) {
				GuiUtils.setColor(ClientSettings.getBackGroundGuiColor(), 0.2f);
			}
			else {
				GuiUtils.setColor(ClientSettings.getBackGroundGuiColor(), 0.5f);
			}
		}
		else {
			GuiUtils.setColor(ClientSettings.getBackGroundGuiColor(), 0.7f);
		}
	}
	
	public static void drawBlurredShadow(int x, int y, int width, int height, int blurRadius) {
		drawBlurredShadow(x, y, width, height, blurRadius, ClientSettings.glassMode ? new Color(0, 0, 0, 100) : new Color(0, 0, 0, 179));
	}
	
	private static HashMap<Integer, Integer> shadowCache = new HashMap<Integer, Integer>();
	
	public static void drawBlurredShadow(int x, int y, int width, int height, int blurRadius, Color color) {

		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.01f);
		
		width = width + blurRadius * 2;
		height = height + blurRadius * 2;
		x = x - blurRadius;
		y = y - blurRadius;
		
		float _X = x - 0.25f;
		float _Y = y + 0.25f;
		
		int identifier = width * height + width + color.hashCode() * blurRadius + blurRadius;

		glEnable(GL_TEXTURE_2D);
		glDisable(GL_CULL_FACE);
		glEnable(GL_ALPHA_TEST);
		glEnable(GL_BLEND);
		
		int texId = -1;
		if(shadowCache.containsKey(identifier)) {
			texId = shadowCache.get(identifier);
			
			GlStateManager.bindTexture(texId);
		}
		else {
			BufferedImage original = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			
			Graphics g = original.getGraphics();
			g.setColor(color);
			g.fillRect(blurRadius, blurRadius, width - blurRadius * 2, height - blurRadius * 2);
			g.dispose();
			
			GaussianFilter op = new GaussianFilter(blurRadius);

			BufferedImage blurred = op.filter(original, null);
			
			texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);
			shadowCache.put(identifier, texId);
		}
		
		glColor4f(1f, 1f, 1f, 1f);
		
		glBegin(GL11.GL_QUADS);
		glTexCoord2f(0, 0); // top left
		glVertex2f(_X, _Y);

		glTexCoord2f(0, 1); // bottom left
		glVertex2f(_X, _Y + height);

		glTexCoord2f(1, 1); // bottom right
		glVertex2f(_X + width, _Y + height);

		glTexCoord2f(1, 0); // top right
		glVertex2f(_X + width, _Y);
		glEnd();
		
		glDisable(GL_TEXTURE_2D);
	}
	
	public static HashMap<Integer, Integer> blurSpotCache = new HashMap<Integer, Integer>();
	
	public static void blurSpot(int x, int y, int width, int height, int blurRadius, int iterations) {
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution sr = new ScaledResolution(mc);
		double scale = 1d / sr.getScaleFactor();
		
		width*=sr.getScaleFactor();
		height*=sr.getScaleFactor();
		
		int imageDownscale = 2;
		
		final int imageWidth = width / imageDownscale;
		final int imageHeight = height / imageDownscale;
		
		int bpp = 3; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
		
		int identifier = (x * y * width * height * blurRadius) + width + height + blurRadius + x + y;
		
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_CULL_FACE);
		glEnable(GL_ALPHA_TEST);
		glEnable(GL_BLEND);
		
		int texId = -1;
		if(blurSpotCache.containsKey(identifier)) {
			texId = blurSpotCache.get(identifier);
			
			GlStateManager.bindTexture(texId);
		}
		else {

			ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp).order(ByteOrder.nativeOrder());
			GL11.glReadPixels(x, mc.displayHeight - y - height, width, height, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, buffer );
			
			BufferedImage original = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			   
			for(int xIndex = 0; xIndex < width; xIndex++) 
			{
			    for(int yIndex = 0; yIndex < height; yIndex++)
			    {
			        int i = (xIndex + (width * yIndex)) * bpp;
			        int r = buffer.get(i) & 0xFF;
			        int g = buffer.get(i + 1) & 0xFF;
			        int b = buffer.get(i + 2) & 0xFF;
			        original.setRGB(xIndex, height - (yIndex + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
			    }
			}
			
			BoxBlurFilter op = new BoxBlurFilter(blurRadius, blurRadius, iterations);
			
			BufferedImage image = new BufferedImage(imageWidth, imageHeight, original.getType());
			Graphics g = image.getGraphics();
			g.drawImage(original, 0, 0, imageWidth, imageHeight, null);
			g.dispose();
			
			BufferedImage blurred = op.filter(image, null);
			
			texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);
			blurSpotCache.put(identifier, texId);
		}
		
		GL11.glPushMatrix();
		GL11.glScaled(scale, scale, scale);
		GL11.glColor4f(1f, 1f, 1f, 1f);
		
		glBegin(GL11.GL_QUADS);
		glTexCoord2f(0, 0); // top left
		glVertex2f(x, y);

		glTexCoord2f(0, 1); // bottom left
		glVertex2f(x, y + height);

		glTexCoord2f(1, 1); // bottom right
		glVertex2f(x + width, y + height);

		glTexCoord2f(1, 0); // top right
		glVertex2f(x + width, y);
		glEnd();
		
		GL11.glPopMatrix();
		
		glDisable(GL_TEXTURE_2D);
		
	}
	
}
