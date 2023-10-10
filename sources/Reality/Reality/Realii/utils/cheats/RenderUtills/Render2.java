package Reality.Realii.utils.cheats.RenderUtills;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;

import java.awt.Color;
import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import Reality.Realii.utils.cheats.player.Helper;
import net.minecraft.client.renderer.GlStateManager;

public class Render2 {
	
	public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        return createFrameBuffer(framebuffer, false);
    }

    public static Framebuffer createFrameBuffer(Framebuffer framebuffer, boolean depth) {
        if (needsNewFramebuffer(framebuffer)) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(Helper.mc.displayWidth, Helper.mc.displayHeight, depth);
        }
        return framebuffer;
    }
    public static void setAlphaLimit(float limit) {
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, (float) (limit * .01));
    }
    public static void resetColor() {
        GlStateManager.color(1, 1, 1, 1);
    }
    public static void glColor(int color) {
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;
        int a = color >> 24 & 0xFF;
        GL11.glColor4f(r / 255.0F, g / 255.0F, b / 255.0F, a / 255.0F);
      }
    public static boolean needsNewFramebuffer(Framebuffer framebuffer) {
        return framebuffer == null || framebuffer.framebufferWidth != Helper.mc.displayWidth || framebuffer.framebufferHeight != Helper.mc.displayHeight;
    }
	
	 public static void color(int color, float alpha) {
	        float r = (float) (color >> 16 & 255) / 255.0F;
	        float g = (float) (color >> 8 & 255) / 255.0F;
	        float b = (float) (color & 255) / 255.0F;
	        GlStateManager.color(r, g, b, alpha);
	    }

	    // Colors the next texture without a specified alpha value
	    public static void color(int color) {
	        color(color, (float) (color >> 24 & 255) / 255.0F);
	    }
	 public static void drawFilledCircleNoGL(final int x, final int y, final double r, final int c) {
	        final float f = ((c >> 24) & 0xff) / 255F;
	        final float f1 = ((c >> 16) & 0xff) / 255F;
	        final float f2 = ((c >> 8) & 0xff) / 255F;
	        final float f3 = (c & 0xff) / 255F;

	        GL11.glColor4f(f1, f2, f3, f);
	        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

	        for (int i = 0; i <= 360 / 20; i++) {
	            final double x2 = Math.sin(((i * 20 * Math.PI) / 180)) * r;
	            final double y2 = Math.cos(((i * 20 * Math.PI) / 180)) * r;
	            GL11.glVertex2d(x + x2, y + y2);
	        }

	        GL11.glEnd();

	    }
	 
	  

	 
	 public static void drawFilledCircleNoGL(final int x, final int y, final double r, final int c, final int quality) {
	        final float f = ((c >> 24) & 0xff) / 255F;
	        final float f1 = ((c >> 16) & 0xff) / 255F;
	        final float f2 = ((c >> 8) & 0xff) / 255F;
	        final float f3 = (c & 0xff) / 255F;

	        GL11.glColor4f(f1, f2, f3, f);
	        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

	        for (int i = 0; i <= 360 / quality; i++) {
	            final double x2 = Math.sin(((i * quality * Math.PI) / 180)) * r;
	            final double y2 = Math.cos(((i * quality * Math.PI) / 180)) * r;
	            GL11.glVertex2d(x + x2, y + y2);
	        }

	        GL11.glEnd();
	    }
	 
	  public static Color interpolateColorsBackAndForth(int speed, int index, Color start, Color end, boolean trueColor) {
	        int angle = (int) (((System.currentTimeMillis()) / speed + index) % 360);
	        angle = (angle >= 180 ? 360 - angle : angle) * 2;
	        return trueColor ? interpolateColorHue(start, end, angle / 360f) : interpolateColorC(start, end, angle / 360f);
	    }
	  
	   public static Color interpolateColorC(Color color1, Color color2, float amount) {
	        amount = Math.min(1, Math.max(0, amount));
	        return new Color(interpolateInt(color1.getRed(), color2.getRed(), amount),
	        		interpolateInt(color1.getGreen(), color2.getGreen(), amount),
	                interpolateInt(color1.getBlue(), color2.getBlue(), amount),
	                interpolateInt(color1.getAlpha(), color2.getAlpha(), amount));
	    }

	    public static Color interpolateColorHue(Color color1, Color color2, float amount) {
	        amount = Math.min(1, Math.max(0, amount));

	        float[] color1HSB = Color.RGBtoHSB(color1.getRed(), color1.getGreen(), color1.getBlue(), null);
	        float[] color2HSB = Color.RGBtoHSB(color2.getRed(), color2.getGreen(), color2.getBlue(), null);

	        Color resultColor = Color.getHSBColor(interpolateFloat(color1HSB[0], color2HSB[0], amount),
	        		interpolateFloat(color1HSB[1], color2HSB[1], amount), interpolateFloat(color1HSB[2], color2HSB[2], amount));

	        return applyOpacity(resultColor, interpolateInt(color1.getAlpha(), color2.getAlpha(), amount) / 255f);
	    }
	    
	    
	    
	   
	 
	 public static int applyOpacity(int color, float opacity) {
	        Color old = new Color(color);
	        return applyOpacity(old, opacity).getRGB();
	    }
	 
	
	 public static Color applyOpacity(Color color, float opacity) {
	        opacity = Math.min(1, Math.max(0, opacity));
	        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (color.getAlpha() * opacity));
	    }
	 
	 public static Double interpolate(double oldValue, double newValue, double interpolationValue){
	        return (oldValue + (newValue - oldValue) * interpolationValue);
	    }

	    public static float interpolateFloat(float oldValue, float newValue, double interpolationValue){
	        return interpolate(oldValue, newValue, (float) interpolationValue).floatValue();
	    }
	    
	    public static int interpolateInt(int oldValue, int newValue, double interpolationValue){
	        return interpolate(oldValue, newValue, (float) interpolationValue).intValue();
	    }
	    
	    public static double ticks = 0;
	    public static long lastFrame = 0;
	    
	 public static void drawCircle(Entity entity, float partialTicks, double raddito, int color) {
	      
	        ticks += .004 * (System.currentTimeMillis() - lastFrame);

	        lastFrame = System.currentTimeMillis();

	        GL11.glPushMatrix();
	        GL11.glDisable(GL11.GL_TEXTURE_2D);
	        GL11.glEnable(GL11.GL_BLEND);
	        GlStateManager.color(1, 1, 1, 1);
	        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	        GL11.glDisable(GL11.GL_DEPTH_TEST);
	        GL11.glDepthMask(false);
	        GL11.glShadeModel(GL11.GL_SMOOTH);
	        GlStateManager.disableCull();

	        final double x = interpolate(entity.lastTickPosX, entity.posX, Helper.mc.timer.renderPartialTicks) - Helper.mc.getRenderManager().renderPosX;
	        final double y = interpolate(entity.lastTickPosY, entity.posY, Helper.mc.timer.renderPartialTicks) - Helper.mc.getRenderManager().renderPosY + Math.sin(ticks) + 1;
	        final double z = interpolate(entity.lastTickPosZ, entity.posZ, Helper.mc.timer.renderPartialTicks) - Helper.mc.getRenderManager().renderPosZ;

	        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);

	        for (float i = 0; i < (Math.PI * 2); i += (Math.PI * 2) / 64.F) {

	            final double vecX = x + raddito * Math.cos(i);
	            final double vecZ = z + raddito * Math.sin(i);

	            color(color, 0);
	            //2.7 is shader size
	            GL11.glVertex3d(vecX, y - Math.sin(ticks + 1) / 2.7f, vecZ);

	            color(color, .52f * 3);


	            GL11.glVertex3d(vecX, y, vecZ);
	        }

	        GL11.glEnd();


	        GL11.glEnable(GL11.GL_LINE_SMOOTH);
	        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
	        GL11.glLineWidth(1.5f);
	        GL11.glBegin(GL11.GL_LINE_STRIP);
	        GlStateManager.color(1, 1, 1, 1);
	        color(color, .5f * 3);
	        for (int i = 0; i <= 180; i++) {
	        	GL11.glVertex3d(x - Math.sin(i * MathHelper.PI2 / 90) * raddito, y, z + Math.cos(i * MathHelper.PI2 / 90) * raddito);
	        }
	        GL11.glEnd();

	        GL11.glShadeModel(GL11.GL_FLAT);
	        GL11.glDepthMask(true);
	        GL11.glEnable(GL11.GL_DEPTH_TEST);
	        GlStateManager.enableCull();
	        GL11.glDisable(GL11.GL_LINE_SMOOTH);
	        GL11.glEnable(GL11.GL_TEXTURE_2D);
	        GL11.glPopMatrix();
	        GL11.glColor4f(1f, 1f, 1f, 1f);
	    }
}
