// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.utils;

import org.lwjgl.util.Point;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.augustus.modules.Module;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;

public class RenderHelper
{
    public static void drawOutlinedBoundingBox(final AxisAlignedBB var0) {
        final Tessellator var = Tessellator.getInstance();
        final WorldRenderer var2 = var.getWorldRenderer();
        var2.begin(3, DefaultVertexFormats.POSITION);
        var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
        var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
        var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
        var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
        var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
        var.draw();
        var2.begin(3, DefaultVertexFormats.POSITION);
        var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
        var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
        var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
        var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
        var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
        var.draw();
        var2.begin(1, DefaultVertexFormats.POSITION);
        var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
        var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
        var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
        var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
        var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
        var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
        var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
        var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
        var.draw();
    }
    
    public static void drawBoundingBox(final AxisAlignedBB var0) {
        final Tessellator var = Tessellator.getInstance();
        final WorldRenderer var2 = var.getWorldRenderer();
        var2.begin(7, DefaultVertexFormats.POSITION);
        var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
        var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
        var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
        var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
        var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
        var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
        var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
        var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
        var.draw();
        var2.begin(7, DefaultVertexFormats.POSITION);
        var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
        var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
        var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
        var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
        var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
        var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
        var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
        var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
        var.draw();
        var2.begin(7, DefaultVertexFormats.POSITION);
        var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
        var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
        var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
        var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
        var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
        var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
        var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
        var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
        var.draw();
        var2.begin(7, DefaultVertexFormats.POSITION);
        var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
        var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
        var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
        var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
        var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
        var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
        var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
        var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
        var.draw();
        var2.begin(7, DefaultVertexFormats.POSITION);
        var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
        var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
        var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
        var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
        var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
        var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
        var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
        var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
        var.draw();
        var2.begin(7, DefaultVertexFormats.POSITION);
        var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
        var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
        var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
        var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
        var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
        var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
        var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
        var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
        var.draw();
    }
    
    public static void drawRect(final float var0, final float var1, final float var2, final float var3, final int var4) {
        final float var5 = (var4 >> 24 & 0xFF) / 255.0f;
        final float var6 = (var4 >> 16 & 0xFF) / 255.0f;
        final float var7 = (var4 >> 8 & 0xFF) / 255.0f;
        final float var8 = (var4 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(var6, var7, var8, var5);
        GL11.glBegin(7);
        GL11.glVertex2d((double)var2, (double)var1);
        GL11.glVertex2d((double)var0, (double)var1);
        GL11.glVertex2d((double)var0, (double)var3);
        GL11.glVertex2d((double)var2, (double)var3);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    public static void drawRect2(final float var0, final float var1, final float var2, final float var3, final int var4) {
        final float var5 = (var4 >> 24 & 0xFF) / 0.0f;
        final float var6 = (var4 >> 16 & 0xFF) / 255.0f;
        final float var7 = (var4 >> 8 & 0xFF) / 255.0f;
        final float var8 = (var4 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(var6, var7, var8, var5);
        GL11.glBegin(7);
        GL11.glVertex2d((double)var2, (double)var1);
        GL11.glVertex2d((double)var0, (double)var1);
        GL11.glVertex2d((double)var0, (double)var3);
        GL11.glVertex2d((double)var2, (double)var3);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    public static void drawOutlinedBlockESP(final double var0, final double var2, final double var4, final float var6, final float var7, final float var8, final float var9, final float var10) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(var10);
        GL11.glColor4f(var6, var7, var8, var9);
        drawOutlinedBoundingBox(new AxisAlignedBB(var0, var2, var4, var0 + 1.0, var2 + 1.0, var4 + 1.0));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawBlockESP(final double var0, final double var2, final double var4, final float var6, final float var7, final float var8, final float var9, final float var10, final float var11, final float var12, final float var13, final float var14) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(var6, var7, var8, var9);
        drawBoundingBox(new AxisAlignedBB(var0, var2, var4, var0 + 1.0, var2 + 1.0, var4 + 1.0));
        GL11.glLineWidth(var14);
        GL11.glColor4f(var10, var11, var12, var13);
        drawOutlinedBoundingBox(new AxisAlignedBB(var0, var2, var4, var0 + 1.0, var2 + 1.0, var4 + 1.0));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawSolidBlockESP(final double var0, final double var2, final double var4, final float var6, final float var7, final float var8, final float var9) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(var6, var7, var8, var9);
        drawBoundingBox(new AxisAlignedBB(var0, var2, var4, var0 + 1.0, var2 + 1.0, var4 + 1.0));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawOutlinedEntityESP(final double var0, final double var2, final double var4, final double var6, final double var8, final float var10, final float var11, final float var12, final float var13) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(var10, var11, var12, var13);
        drawOutlinedBoundingBox(new AxisAlignedBB(var0 - var6, var2, var4 - var6, var0 + var6, var2 + var8, var4 + var6));
        GL11.glColor4f(var10, var11, var12, 0.2f);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawSolidEntityESP(final double var0, final double var2, final double var4, final double var6, final double var8, final float var10, final float var11, final float var12, final float var13) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(var10, var11, var12, var13);
        drawBoundingBox(new AxisAlignedBB(var0 - var6, var2, var4 - var6, var0 + var6, var2 + var8, var4 + var6));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawEntityESP(final double var0, final double var2, final double var4, final double var6, final double var8, final float var10, final float var11, final float var12, final float var13, final float var14, final float var15, final float var16, final float var17, final float var18) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(var10, var11, var12, var13);
        drawBoundingBox(new AxisAlignedBB(var0 - var6, var2, var4 - var6, var0 + var6, var2 + var8, var4 + var6));
        GL11.glLineWidth(var18);
        GL11.glColor4f(var14, var15, var16, var17);
        drawOutlinedBoundingBox(new AxisAlignedBB(var0 - var6, var2, var4 - var6, var0 + var6, var2 + var8, var4 + var6));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void render2DESP(final EntityPlayer var0, final Module var1, final boolean var2, final int var3, final int var4) {
        final Minecraft var5 = Minecraft.getMinecraft();
        final double var6 = var0.lastTickPosX + (var0.posX - var0.lastTickPosX) * var5.timer.renderPartialTicks - var5.getRenderManager().renderPosX;
        final double var7 = var0.lastTickPosY + (var0.posY - var0.lastTickPosY) * var5.timer.renderPartialTicks - var5.getRenderManager().renderPosY;
        final double var8 = var0.lastTickPosZ + (var0.posZ - var0.lastTickPosZ) * var5.timer.renderPartialTicks - var5.getRenderManager().renderPosZ;
        GL11.glPushMatrix();
        GL11.glTranslated(var6, var7 - 0.2, var8);
        GL11.glScalef(0.03f, 0.03f, 0.03f);
        GL11.glRotated((double)(-var5.getRenderManager().playerViewY), 0.0, 1.0, 0.0);
        GlStateManager.disableDepth();
        drawRect(21.0f, -1.0f, 26.0f, 20.0f, var4);
        drawRect(6.0f, -1.0f, 25.0f, 3.0f, var4);
        drawRect(21.0f, 0.0f, 25.0f, 21.0f, var3);
        drawRect(5.0f, 0.0f, 25.0f, 3.0f, var3);
        drawRect(21.0f, 56.0f, 26.0f, 77.0f, var4);
        drawRect(6.0f, 73.0f, 25.0f, 77.0f, var4);
        drawRect(21.0f, 55.0f, 25.0f, 76.0f, var3);
        drawRect(5.0f, 73.0f, 25.0f, 76.0f, var3);
        drawRect(-21.0f, 77.0f, -22.0f, 56.0f, var4);
        drawRect(-22.0f, 73.0f, -2.0f, 77.0f, var4);
        drawRect(-21.0f, 55.0f, -17.0f, 76.0f, var3);
        drawRect(-21.0f, 73.0f, -1.0f, 76.0f, var3);
        drawRect(-22.0f, -1.0f, -19.0f, 20.0f, var4);
        drawRect(-22.0f, -1.0f, -2.0f, 3.0f, var4);
        drawRect(-21.0f, 0.0f, -17.0f, 21.0f, var3);
        drawRect(-21.0f, 0.0f, -1.0f, 3.0f, var3);
        GlStateManager.enableDepth();
        GL11.glPopMatrix();
    }
    
    public static void drawTracerLine(final double var0, final double var2, final double var4, final float var6, final float var7, final float var8, final float var9, final float var10) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(var10);
        GL11.glColor4f(var6, var7, var8, var9);
        GL11.glBegin(2);
        GL11.glVertex3d(0.0, 0.0 + Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0);
        GL11.glVertex3d(var0, var2, var4);
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawCircle(final float var0, final float var1, final double var2, final int var4, final int var5) {
        final float var6 = (var5 >> 24 & 0xFF) / 255.0f;
        final float var7 = (var5 >> 16 & 0xFF) / 255.0f;
        final float var8 = (var5 >> 8 & 0xFF) / 255.0f;
        final float var9 = (var5 & 0xFF) / 255.0f;
        final float var10 = (var4 >> 24 & 0xFF) / 255.0f;
        final float var11 = (var4 >> 16 & 0xFF) / 255.0f;
        final float var12 = (var4 >> 8 & 0xFF) / 255.0f;
        final float var13 = (var4 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(var11, var12, var13, var10);
        GL11.glBegin(2);
        for (int var14 = 0; var14 <= 360; ++var14) {
            final double var15 = Math.sin(var14 * 3.141592653589793 / 180.0) * var2;
            final double var16 = Math.cos(var14 * 3.141592653589793 / 180.0) * var2;
            GL11.glVertex2d(var0 + var15, var1 + var16);
        }
        GL11.glEnd();
        GL11.glColor4f(var7, var8, var9, var6);
        GL11.glBegin(9);
        for (int var17 = 0; var17 <= 360; ++var17) {
            final double var18 = Math.sin(var17 * 3.141592653589793 / 180.0) * var2;
            final double var19 = Math.cos(var17 * 3.141592653589793 / 180.0) * var2;
            GL11.glVertex2d(var0 + var18, var1 + var19);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
    
    public static void drawString(final double var0, final String var2, final float var3, final float var4, final int var5) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(var0, var0, var0);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(var2, var3, var4, var5);
        GlStateManager.popMatrix();
    }
    
    public static void DrawFontWithShadow(final double var0, final String var2, final int var3, final int var4, final int var5, final int var6) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(var0, var0, var0);
        Minecraft.getMinecraft().fontRendererObj.drawString(var2, var3 - 1, var4 - 1, var6);
        Minecraft.getMinecraft().fontRendererObj.drawString(var2, var3, var4, var5);
        GlStateManager.popMatrix();
    }
    
    public static void drawLine(final Point var0, final Point var1) {
        GL11.glColor3f(0.0f, 1.0f, 0.2f);
        GL11.glBegin(3);
        GL11.glVertex2d((double)var0.getX(), (double)var0.getY());
        GL11.glVertex2d((double)var1.getX(), (double)var1.getY());
        GL11.glEnd();
    }
}
