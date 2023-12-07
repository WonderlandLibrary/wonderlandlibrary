package Velo.api.Util.Render;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import Velo.api.ClickGui.Util.ClickGuiRenderUtils;
import Velo.api.Util.Other.ColorUtil;
import Velo.api.Util.Other.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;

public class RenderUtil {
	public static Minecraft mc = Minecraft.getMinecraft();
	   private static final Frustum frustrum = new Frustum();
	   private static ScaledResolution scaledResolution;
    public static void startSmooth() {
        glEnable(GL_LINE_SMOOTH);
        glEnable(GL_POLYGON_SMOOTH);
        glEnable(GL_POINT_SMOOTH);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
        glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
    }
    
    
    public static double interpolateScale(double current, double old, double scale) {
        return old + (current - old) * scale;
     }
    
    public static void drawImage(final int x, final int y, final int width, final int height, final ResourceLocation image, Color color) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float) width, (float) height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }
    
    public static void drawImage(float x, float y, final int width, final int height, final ResourceLocation image, Color color) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float) width, (float) height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    public static void drawImage( int x,  int y,  int width,  int height,  ResourceLocation image) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1, 1, 1, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float) width, (float) height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }
    
    
    public static void drawRect(double x, double y, double width, double height, int color) {
        float f = (color >> 24 & 0xFF) / 255.0F;
        float f1 = (color >> 16 & 0xFF) / 255.0F;
        float f2 = (color >> 8 & 0xFF) / 255.0F;
        float f3 = (color & 0xFF) / 255.0F;
        GL11.glColor4f(f1, f2, f3, f);
        Gui.drawRect(x, y, x + width, y + height, color);
    }
    
    
    
    public static void drawGradientRect(double d, int top, double e, int bottom, int startColor, int endColor)
	 {
	        float f = (float)(startColor >> 24 & 255) / 255.0F;
	        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
	        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
	        float f3 = (float)(startColor & 255) / 255.0F;
	        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
	        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
	        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
	        float f7 = (float)(endColor & 255) / 255.0F;
	        GlStateManager.disableTexture2D();
	        GlStateManager.enableBlend();
	        GlStateManager.disableAlpha();
	        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	        GlStateManager.shadeModel(7425);
	        Tessellator tessellator = Tessellator.getInstance();
	        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
	        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
	        worldrenderer.pos((double)e, (double)top, (double)0).color(f1, f2, f3, f).endVertex();
	        worldrenderer.pos((double)d, (double)top, (double)0).color(f1, f2, f3, f).endVertex();
	        worldrenderer.pos((double)d, (double)bottom, (double)0).color(f5, f6, f7, f4).endVertex();
	        worldrenderer.pos((double)e, (double)bottom, (double)0).color(f5, f6, f7, f4).endVertex();
	        tessellator.draw();
	        GlStateManager.shadeModel(7424);
	        GlStateManager.disableBlend();
	        GlStateManager.enableAlpha();
	        GlStateManager.enableTexture2D();
	    }
    
    public static void drawCheckMark(float x, float y, int width, int color) {
        float f = (color >> 24 & 255) / 255.0f;
        float f1 = (color >> 16 & 255) / 255.0f;
        float f2 = (color >> 8 & 255) / 255.0f;
        float f3 = (color & 255) / 255.0f;
        GL11.glPushMatrix();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(3553);
        glEnable(2848);
        glBlendFunc(770, 771);
        GL11.glLineWidth(1.5f);
        GL11.glBegin(3);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(x + width - 6.5, y + 3);
        GL11.glVertex2d(x + width - 11.5, y + 10);
        GL11.glVertex2d(x + width - 13.5, y + 8);
        GL11.glEnd();
        glEnable(3553);
        glDisable(GL_BLEND);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
    
	   public static void glColor(int hex) {
		      float alpha = (float)(hex >> 24 & 255) / 255.0F;
		      float red = (float)(hex >> 16 & 255) / 255.0F;
		      float green = (float)(hex >> 8 & 255) / 255.0F;
		      float blue = (float)(hex & 255) / 255.0F;
		      GL11.glColor4f(red, green, blue, alpha);
		   }
	   

	    public static void pre3D() {
	    	GL11.glPushMatrix();
           GlStateManager.disableAlpha();
           GlStateManager.enableBlend();
           GlStateManager.blendFunc(770, 771);
           GlStateManager.disableTexture2D();
           GlStateManager.disableDepth();
           GL11.glEnable(2848);
	    }

	    public static void post3D() {
           GL11.glDisable(2848);
           GlStateManager.enableDepth();
           GlStateManager.enableTexture2D();
           GlStateManager.enableBlend();
           GlStateManager.enableAlpha();
           GL11.glPopMatrix();
	    }
  
    public static void drawRoundedRect2(int x, int y, int width, int height, int cornerRadius, Color color) {
        Gui.drawRect(x, y + cornerRadius, x + cornerRadius, y + height - cornerRadius, color.getRGB());
        Gui.drawRect(x + cornerRadius, y, x + width - cornerRadius, y + height, color.getRGB());
        Gui.drawRect(x + width - cornerRadius, y + cornerRadius, x + width, y + height - cornerRadius, color.getRGB());
        
        drawArc(x + cornerRadius, y + cornerRadius, cornerRadius, 0, 90, color);
        drawArc(x + width - cornerRadius, y + cornerRadius, cornerRadius, 270, 360, color);
        drawArc(x + width - cornerRadius, y + height - cornerRadius, cornerRadius, 180, 270, color);
        drawArc(x + cornerRadius, y + height - cornerRadius, cornerRadius, 90, 180, color);
    }
    
    
    
    public static void drawRoundedRect2(float x, float y, float width, float height, Color color) {
        Gui.drawRect(x, y + 3, x + 3, y + height - 3, color.getRGB());
        Gui.drawRect(x + 3, y, x + width - 3, y + height, color.getRGB());
        Gui.drawRect(x + width - 3, y + 3, x + width, y + height - 3, color.getRGB());
        
        drawArc(x + 3, y + 3, 3, 0, 90, color);
        drawArc(x + width - 3, y + 3, 3, 270, 360, color);
        drawArc(x + width - 3, y + height - 3, 3, 180, 270, color);
        drawArc(x + 3, y + height - 3, 3, 90, 180, color);
    }
    
    
   public static void drawArc(float f, float g, int radius, int startAngle, int endAngle, Color color) {
        
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, (float) color.getAlpha() / 255);
        
        WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();

        worldRenderer.begin(6, DefaultVertexFormats.POSITION);
        worldRenderer.pos(f, g, 0).endVertex();

        for (int i = (int) (startAngle / 360.0 * 100); i <= (int) (endAngle / 360.0 * 100); i++) {
            double angle = (Math.PI * 2 * i / 100) + Math.toRadians(180);
            worldRenderer.pos(f + Math.sin(angle) * radius, g + Math.cos(angle) * radius, 0).endVertex();
        }
        
        Tessellator.getInstance().draw();
        
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawArc(int x, int y, int radius, int startAngle, int endAngle, Color color) {
        
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, (float) color.getAlpha() / 255);
        
        WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();

        worldRenderer.begin(6, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x, y, 0).endVertex();

        for (int i = (int) (startAngle / 360.0 * 100); i <= (int) (endAngle / 360.0 * 100); i++) {
            double angle = (Math.PI * 2 * i / 100) + Math.toRadians(180);
            worldRenderer.pos(x + Math.sin(angle) * radius, y + Math.cos(angle) * radius, 0).endVertex();
        }
        
        Tessellator.getInstance().draw();
        
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static double interpolate(double old, double current, double scale) {
        return old + (current - old) * scale;
    }
    public static void prepareScissorBox(float x, float y, float x2, float y2) {
        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        GL11.glScissor((int)(x * (float)factor), (int)(((float)scale.getScaledHeight() - y2) * (float)factor), (int)((x2 - x) * (float)factor), (int)((y2 - y) * (float)factor));
     }
	  public static void drawRoundedRect(float left, float top, float right, float bottom, int smooth, Color color) {
		    Gui.drawRect((left + smooth), top, (right - smooth), bottom, color.getRGB());
		    Gui.drawRect(left, (top + smooth), right, (bottom - smooth), color.getRGB());
		    drawFilledCircle((int)left + smooth, (int)top + smooth, smooth, color);
		    drawFilledCircle((int)right - smooth, (int)top + smooth, smooth, color);
		    drawFilledCircle((int)right - smooth, (int)bottom - smooth, smooth, color);
		    drawFilledCircle((int)left + smooth, (int)bottom - smooth, smooth, color);
		  }
    
	  
	  public static void drawFilledCircle(int xx, int yy, float radius, Color color) {
		    int sections = 50;
		    double dAngle = 6.283185307179586D / sections;
		    GL11.glPushAttrib(8192);
		    GL11.glEnable(3042);
		    GL11.glDisable(3553);
		    GL11.glBlendFunc(770, 771);
		    GL11.glEnable(2848);
		    GL11.glBegin(6);
		    for (int i = 0; i < sections; i++) {
		      float x = (float)(radius * Math.sin(i * dAngle));
		      float y = (float)(radius * Math.cos(i * dAngle));
		      GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
		      GL11.glVertex2f(xx + x, yy + y);
		    } 
		    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		    GL11.glEnd();
		    GL11.glPopAttrib();
		  }
	  
	  
    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float)(col1 >> 24 & 255) / 255.0F;
        float f1 = (float)(col1 >> 16 & 255) / 255.0F;
        float f2 = (float)(col1 >> 8 & 255) / 255.0F;
        float f3 = (float)(col1 & 255) / 255.0F;
        float f4 = (float)(col2 >> 24 & 255) / 255.0F;
        float f5 = (float)(col2 >> 16 & 255) / 255.0F;
        float f6 = (float)(col2 >> 8 & 255) / 255.0F;
        float f7 = (float)(col2 & 255) / 255.0F;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
     }
    
	  public static void rectangle(double left, double top, double right, double bottom, int color) {
		    Gui.drawRect(left, top, right, bottom, color);
		  }
	 public static void rectangleBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
		    rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
		    rectangle(x + width, y, x1 - width, y + width, borderColor);
		    rectangle(x, y, x + width, y1, borderColor);
		    rectangle(x1 - width, y, x1, y1, borderColor);
		    rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
		  }
    
public static void drawLine(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        
        minX -= Minecraft.getMinecraft().getRenderManager().renderPosX;
        minY -= Minecraft.getMinecraft().getRenderManager().renderPosY;
        minZ -= Minecraft.getMinecraft().getRenderManager().renderPosZ;
        
        maxX -= Minecraft.getMinecraft().getRenderManager().renderPosX;
        maxY -= Minecraft.getMinecraft().getRenderManager().renderPosY;
        maxZ -= Minecraft.getMinecraft().getRenderManager().renderPosZ;
        
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLineWidth(3.0F);
		GL11.glColor4d(0, 1, 0, 0.15F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		//drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
		GL11.glColor4d(1, 1, 1, 0.5F);
        
        worldrenderer.begin(1, DefaultVertexFormats.POSITION);
        worldrenderer.pos(minX, minY, minZ).endVertex();
        worldrenderer.pos(maxX, maxY, maxZ).endVertex();
        tessellator.draw();
        
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1, 1, 1, 1);
        
	}
    
public static void drawBorderedRect1(double x, double y, double width, double height, double lineSize, int borderColor, int color) {
    Gui.drawRect(x, y, x + width, y + height, color);
    Gui.drawRect(x, y, x + width, y + lineSize, borderColor);
    Gui.drawRect(x, y, x + lineSize, y + height, borderColor);
    Gui.drawRect(x + width, y, x + width - lineSize, y + height, borderColor);
    Gui.drawRect(x, y + height, x + width, y + height - lineSize, borderColor);
}


    public static void drawBorderedRect(final float x, final float y, final float x2, final float y2, final float width,
            final int color1, final int color2) {
Gui.drawRect(x, y, x2, y2, color2);
glEnable(GL_BLEND);
glDisable(GL_TEXTURE_2D);
glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
glEnable(GL_LINE_SMOOTH);

ClickGuiRenderUtils.color(color1);
glLineWidth(width);
glBegin(1);
glVertex2d(x, y);
glVertex2d(x, y2);
glVertex2d(x2, y2);
glVertex2d(x2, y);
glVertex2d(x, y);
glVertex2d(x2, y);
glVertex2d(x, y2);
glVertex2d(x2, y2);
glEnd();

glEnable(GL_TEXTURE_2D);
glDisable(GL_BLEND);
glDisable(GL_LINE_SMOOTH);
}
    
    

	public static void drawBorderedRect(double left, double top, double right, double bottom, double borderWidth, int insideColor, int borderColor, boolean borderIncludedInBounds) {
        Gui.drawRect(left - (!borderIncludedInBounds ? borderWidth : 0), top - (!borderIncludedInBounds ? borderWidth : 0), right + (!borderIncludedInBounds ? borderWidth : 0), bottom + (!borderIncludedInBounds ? borderWidth : 0), borderColor);
        Gui.drawRect(left + (borderIncludedInBounds ? borderWidth : 0), top + (borderIncludedInBounds ? borderWidth : 0), right - ((borderIncludedInBounds ? borderWidth : 0)), bottom - ((borderIncludedInBounds ? borderWidth : 0)), insideColor);
    }
  

    

    public static void endSmooth() {
        glDisable(GL_LINE_SMOOTH);
        glDisable(GL_POLYGON_SMOOTH);
        glEnable(GL_POINT_SMOOTH);
    }
	

	
	public static void drawTracerLine(double x, double y, double z, float red, float green, float blue, float alpha, float lineWdith) {
		GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        // GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(lineWdith);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(2);
        GL11.glVertex3d(0.0D, 0.0D + Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0D);
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        // GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
	}
    public static void drawCircle(int x, int y, double r, int c)
    {
        float f = ((c >> 24) & 0xff) / 255F;
        float f1 = ((c >> 16) & 0xff) / 255F;
        float f2 = ((c >> 8) & 0xff) / 255F;
        float f3 = (c & 0xff) / 255F;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(GL11.GL_LINE_LOOP);

        for (int i = 0; i <= 360; i++)
        {
            double x2 = Math.sin(((i * Math.PI) / 180)) * r;
            double y2 = Math.cos(((i * Math.PI) / 180)) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }

        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void drawFilledCircle(int x, int y, double r, int c)
    {
        float f = ((c >> 24) & 0xff) / 255F;
        float f1 = ((c >> 16) & 0xff) / 255F;
        float f2 = ((c >> 8) & 0xff) / 255F;
        float f3 = (c & 0xff) / 255F;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        for (int i = 0; i <= 360; i++)
        {
            double x2 = Math.sin(((i * Math.PI) / 180)) * r;
            double y2 = Math.cos(((i * Math.PI) / 180)) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }

        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }
    
    public static void dr(double i, double j, double k, double l, int i1)
    {
        if (i < k)
        {
            double j1 = i;
            i = k;
            k = j1;
        }

        if (j < l)
        {
            double k1 = j;
            j = l;
            l = k1;
        }

        float f = ((i1 >> 24) & 0xff) / 255F;
        float f1 = ((i1 >> 16) & 0xff) / 255F;
        float f2 = ((i1 >> 8) & 0xff) / 255F;
        float f3 = (i1 & 0xff) / 255F;
        Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(f1, f2, f3, f);
        worldRenderer.draw(7, GL11.GL_QUADS);
        worldRenderer.pos(i, l, 0.0D);
        worldRenderer.pos(k, l, 0.0D);
        worldRenderer.pos(k, j, 0.0D);
        worldRenderer.pos(i, j, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }
	
    
    public static boolean isInViewFrustrum(Entity entity) {
        return isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
     }

     private static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        frustrum.setPosition(current.posX, current.posY, current.posZ);
        return frustrum.isBoundingBoxInFrustum(bb);
     }
 
    public static void draw2DImage(ResourceLocation image, int x, int y, int width, int height, Color c) {
    
    	GL11.glDisable(GL11.GL_DEPTH_TEST);
    	GL11.glEnable(GL11.GL_BLEND);
    	GL11.glDepthMask(false);
    	OpenGlHelper.glBlendFunc(770, 771, 1, 0);
    	GL11.glColor4f(c.getRed() / 255f, c.getGreen()/255f, c.getBlue() / 255f, c.getAlpha());
    	mc.getTextureManager().bindTexture(image);
    	Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
    
    	GL11.glDepthMask(true);
    	GL11.glDisable(GL11.GL_BLEND);
    	GL11.glEnable(GL11.GL_DEPTH_TEST);
    	GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    

    public static void draw2DImage(ResourceLocation image, float x, float y, int width, int height, Color c) {
    
    	GL11.glDisable(GL11.GL_DEPTH_TEST);
    	GL11.glEnable(GL11.GL_BLEND);
    	GL11.glDepthMask(false);
    	OpenGlHelper.glBlendFunc(770, 771, 1, 0);
    	GL11.glColor4f(c.getRed() / 255f, c.getGreen()/255f, c.getBlue() / 255f, c.getAlpha());
    	mc.getTextureManager().bindTexture(image);
    	Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
    
    	GL11.glDepthMask(true);
    	GL11.glDisable(GL11.GL_BLEND);
    	GL11.glEnable(GL11.GL_DEPTH_TEST);
    	GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static boolean isHovered(double x, double y, double width, double height, int mouseX, int mouseY) {
		return mouseX > x && mouseY > y && mouseX < width && mouseY < height;
    	
    	
    }
    public static void entityESPBox(final Entity entity, final int mode) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        if (mode == 0) {
            GL11.glColor4d((double)(1.0f - Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) / 40.0f), (double)(Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) / 40.0f), 0.0, 0.5);
        }
        else if (mode == 1) {
            GL11.glColor4d(0.0, 0.0, 1.0, 0.5);
        }
        else if (mode == 2) {
            GL11.glColor4d(1.0, 1.0, 0.0, 0.5);
        }
        else if (mode == 3) {
            GL11.glColor4d(1.0, 0.0, 0.0, 0.5);
        }
        else if (mode == 4) {
            GL11.glColor4d(0.0, 1.0, 0.0, 0.5);
        }
        Minecraft.getMinecraft().getRenderManager();
       // RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(entity.boundingBox.minX - 0.05 - entity.posX + (entity.posX - Minecraft.getMinecraft().getRenderManager().renderPosX), entity.boundingBox.minY - entity.posY + (entity.posY - Minecraft.getMinecraft().getRenderManager().renderPosY), entity.boundingBox.minZ - 0.05 - entity.posZ + (entity.posZ - Minecraft.getMinecraft().getRenderManager().renderPosZ), entity.boundingBox.maxX + 0.05 - entity.posX + (entity.posX - Minecraft.getMinecraft().getRenderManager().renderPosX), entity.boundingBox.maxY + 0.1 - entity.posY + (entity.posY - Minecraft.getMinecraft().getRenderManager().renderPosY), entity.boundingBox.maxZ + 0.05 - entity.posZ + (entity.posZ - Minecraft.getMinecraft().getRenderManager().renderPosZ)), mode);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }
    public static float[] rgba(int color) {
    	if ((color & -67108864) == 0) {
    		color |= -16777216;
    	}
    		float alpha = (color >> 24 & 255) / 255f;
            float red = (color >> 16 & 255) / 255F;
            float green = (color >> 8 & 255) / 255f;
            float blue = (color & 255) / 255f;

            return new float[] { red, green, blue, alpha};
    }

   
    public static void scissor(double x, double y, double width, double height) {
        ScaledResolution sr = new ScaledResolution(mc);
        final double scale = sr.getScaleFactor();

        y = sr.getScaledHeight() - y;

        x *= scale;
        y *= scale;
        width *= scale;
        height *= scale;

        glScissor((int) x, (int) (y - height), (int) width, (int) height);
    }

	public static void drawBorderedRect(double d, double e, float f, float g, double h, double i, float j, float k) {
		// TODO Auto-generated method stub
		
	}

	public static void drawCircle(float x, float y, float r, int c, int color) {
		 float f = ((c >> 24) & 0xff) / 255F;
	        float f1 = ((c >> 16) & 0xff) / 255F;
	        float f2 = ((c >> 8) & 0xff) / 255F;
	        float f3 = (c & 0xff) / 255F;
	        GL11.glEnable(GL11.GL_BLEND);
	        GL11.glDisable(GL11.GL_TEXTURE_2D);
	        GL11.glEnable(GL11.GL_LINE_SMOOTH);
	        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	        GL11.glColor4f(f1, f2, f3, f);
	        GL11.glBegin(GL11.GL_LINE_LOOP);

	        for (int i = 0; i <= 360; i++)
	        {
	            double x2 = Math.sin(((i * Math.PI) / 180)) * r;
	            double y2 = Math.cos(((i * Math.PI) / 180)) * r;
	            GL11.glVertex2d(x + x2, y + y2);
	        }

	        GL11.glEnd();
	        GL11.glDisable(GL11.GL_LINE_SMOOTH);
	        GL11.glEnable(GL11.GL_TEXTURE_2D);
	        GL11.glDisable(GL11.GL_BLEND);
		
	}


	public static void drawFilledCircle(float x, float y, int r, int c) {
        float f = ((c >> 24) & 0xff) / 255F;
        float f1 = ((c >> 16) & 0xff) / 255F;
        float f2 = ((c >> 8) & 0xff) / 255F;
        float f3 = (c & 0xff) / 255F;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        for (int i = 0; i <= 360; i++)
        {
            double x2 = Math.sin(((i * Math.PI) / 180)) * r;
            double y2 = Math.cos(((i * Math.PI) / 180)) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }

        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
		
	}
	
	
	
	public static int drawHealth(EntityLivingBase entityLivingBase) {
	      float health = entityLivingBase.getHealth();
	      float maxHealth = entityLivingBase.getMaxHealth();
	      return Color.HSBtoRGB(Math.max(0.0F, Math.min(health, maxHealth) / maxHealth) / 3.0F, 1.0F, 1.0F) | -16777216;
	   }

	   public static Color drawHealth(float health, float maxHealth) {
	      float[] fractions = new float[]{0.0F, 0.5F, 1.0F};
	      Color[] colors = new Color[]{new Color(108, 0, 0), new Color(255, 51, 0), Color.GREEN};
	      float progress = health / maxHealth;
	      return ColorUtil.blendColors(fractions, colors, progress).brighter();
	   }
	
   
    
   
}
