package Velo.api.Util.Other.other;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import Velo.api.Util.Render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class EntityUtil {
	
	static Minecraft mc = Minecraft.getMinecraft();
	
    
	
	public static void drawCircle(Entity entity, float partialTicks, double rad) {
		  GL11.glPushMatrix();
		  GL11.glDisable(GL11.GL_TEXTURE_2D);
	        RenderUtil.startSmooth();
	        GL11.glDisable(GL11.GL_DEPTH_TEST);
	        GL11. glDepthMask(false);
	        GL11.glLineWidth(4.0f);
	        GL11.glBegin(GL11.GL_LINE_STRIP);

	        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
	        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;
	        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;

	        final float r = ((float) 1 / 255) * Color.WHITE.getRed();
	        final float g = ((float) 1 / 255) * Color.WHITE.getGreen();
	        final float b = ((float) 1 / 255) * Color.WHITE.getBlue();

	        final double pix2 = Math.PI * 2.0D;

	        for (int i = 0; i <= 90; ++i) {
	          //  GL11.glColor3f(r, g, b);
	            GL11.glVertex3d(x + rad * Math.cos(i * pix2 / 45.0), y, z + rad * Math.sin(i * pix2 / 45.0));
	        }

	        GL11.glEnd();
	        GL11.glDepthMask(true);
	        GL11.glEnable(GL11.GL_DEPTH_TEST);
	        RenderUtil.endSmooth();
	        GL11.glEnable(GL11.GL_TEXTURE_2D);
	        GL11.glPopMatrix();
	}
	
}
