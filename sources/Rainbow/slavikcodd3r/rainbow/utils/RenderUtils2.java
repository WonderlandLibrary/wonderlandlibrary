package slavikcodd3r.rainbow.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.opengl.GL11;
import java.awt.Color;

public class RenderUtils2
{
    static int fade;
    static boolean fadeIn;
    static boolean fadeOut;
    
    public static void box(double x, double y2, double z, double x2, double y22, double z2, final Color color) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        final double n = x;
        final double x3;
        x = (x3 = n - RenderManager.renderPosX);
        final double n2 = y2;
        final double y23;
        y2 = (y23 = n2 - RenderManager.renderPosY);
        final double n3 = z;
        final double z3;
        z = (z3 = n3 - RenderManager.renderPosZ);
        final double n4 = x2;
        final double x4;
        x2 = (x4 = n4 - RenderManager.renderPosX);
        final double n5 = y22;
        final double y24;
        y22 = (y24 = n5 - RenderManager.renderPosY);
        final double n6 = z2;
        GL11.glColor4d(0.0, 0.0, 0.0, 0.2);
        z2 = n6 - RenderManager.renderPosZ;
        drawColorBox(new AxisAlignedBB(x3, y23, z3, x4, y24, z2));
        RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x, y2, z, x2, y22, z2), color.getRGB());
        GL11.glColor4d(0.0, 0.0, 0.0, 0.2);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }
    
    public static void drawColorBox(final AxisAlignedBB axisalignedbb) {
        final Tessellator ts = Tessellator.getInstance();
        final WorldRenderer wr = ts.getWorldRenderer();
        wr.startDrawingQuads();
        wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        ts.draw();
        wr.startDrawingQuads();
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        ts.draw();
        wr.startDrawingQuads();
        wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        ts.draw();
        wr.startDrawingQuads();
        wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        ts.draw();
        wr.startDrawingQuads();
        wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        ts.draw();
        wr.startDrawingQuads();
        wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        ts.draw();
    }
    
    public static void draw2DRect(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final double var5 = left;
            left = right;
            right = var5;
        }
        if (top < bottom) {
            final double var5 = top;
            top = bottom;
            bottom = var5;
        }
        final float var6 = (color >> 24 & 0xFF) / 255.0f;
        final float var7 = (color >> 16 & 0xFF) / 255.0f;
        final float var8 = (color >> 8 & 0xFF) / 255.0f;
        final float var9 = (color & 0xFF) / 255.0f;
        final Tessellator var10 = Tessellator.getInstance();
        final WorldRenderer var11 = var10.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var7, var8, var9, var6);
        var11.startDrawingQuads();
        var11.addVertex(left, bottom, 0.0);
        var11.addVertex(right, bottom, 0.0);
        var11.addVertex(right, top, 0.0);
        var11.addVertex(left, top, 0.0);
        var10.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawRect(final double x, final double y2, final double x2, final double y22, final int color) {
        final float red = (color >> 24 & 0xFF) / 255.0f;
        final float green = (color >> 16 & 0xFF) / 255.0f;
        final float blue = (color >> 8 & 0xFF) / 255.0f;
        final float alpha = (color & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(green, blue, alpha, red);
        GL11.glBegin(7);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x, y22);
        GL11.glVertex2d(x2, y22);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    public static double getAnimationState(double animation, final double finalState, final double speed) {
        final float add = (float)(0.01 * speed);
        animation = ((animation < finalState) ? ((animation + add < finalState) ? (animation += add) : finalState) : ((animation - add > finalState) ? (animation -= add) : finalState));
        return animation;
    }
    
    public static void drawBorderedCorneredRect(final float x, final float y2, final float x2, final float y22, final float lineWidth, final int lineColor, final int bgColor) {
        drawRect(x, y2, x2, y22, bgColor);
        drawRect(x - lineWidth, y2 - lineWidth, x2 + lineWidth, y2, lineColor);
        drawRect(x - lineWidth, y2, x, y22, lineColor);
        drawRect(x - lineWidth, y22, x2 + lineWidth, y22 + lineWidth, lineColor);
        drawRect(x2, y2, x2 + lineWidth, y22, lineColor);
    }
    
    public static void drawOutlineRect(final float x, final float y2, final float x2, final float y22, final float lineWidth, final int lineColor) {
        drawRect((int)x - (int)lineWidth, (int)y2 - (int)lineWidth, (int)x2 + (int)lineWidth, (int)y2, lineColor);
        drawRect((int)x - (int)lineWidth, (int)y2, (int)x, (int)y22, lineColor);
        drawRect((int)x - (int)lineWidth, (int)y22, (int)x2 + (int)lineWidth, (int)y22 + (int)lineWidth, lineColor);
        drawRect((int)x2, (int)y2, (int)x2 + (int)lineWidth, (int)y22, lineColor);
    }
    
    public static void drawBorderedRect(final float x, final float y2, final float x2, final float y22, final float l1, final int col1, final int col2) {
        drawRect(x, y2, x2, y22, col2);
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x, (double)y22);
        GL11.glVertex2d((double)x2, (double)y22);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glVertex2d((double)x, (double)y22);
        GL11.glVertex2d((double)x2, (double)y22);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    public static void drawGradientRect(final float x, final float y2, final float x1, final float y1, final int topColor, final int bottomColor) {
        enableGL2D();
        GL11.glShadeModel(7425);
        GL11.glBegin(7);
        ColorUtils.glColor(topColor);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        ColorUtils.glColor(bottomColor);
        GL11.glVertex2f(x1, y2);
        GL11.glVertex2f(x, y2);
        GL11.glEnd();
        GL11.glShadeModel(7425);
        disableGL2D();
    }
    
    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
    
    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    
    public static void renderEspBox(final BlockPos blockPos, final Color color) {
        final double n = blockPos.getX();
        final double x = n - RenderManager.renderPosX;
        final double n2 = blockPos.getY();
        final double y2 = n2 - RenderManager.renderPosY;
        final double n3 = blockPos.getZ();
        final double z = n3 - RenderManager.renderPosZ;
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(0.0f, 0.0f, 0.0f, 0.4f);
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        color(color.hashCode(), 1.0f);
        RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x, y2, z, x + 1.0, y2 + 1.0, z + 1.0), -1);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }
    
    public static void color(final int color, final float alpha) {
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }
    
    public static void drawBorderedCircle(final float x, final float y2, final float radius, final int outsideC, final int insideC) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        drawCircle(x, y2, radius, insideC);
        drawUnfilledCircle(x, y2, radius, 1.0f, outsideC);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }
    
    public static void drawUnfilledCircle(final float x, final float y2, final float radius, final float lineWidth, final int color) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glLineWidth(lineWidth);
        GL11.glEnable(2848);
        GL11.glBegin(2);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d(x + Math.sin(i * 3.141592653589793 / 180.0) * radius, y2 + Math.cos(i * 3.141592653589793 / 180.0) * radius);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
    }
    
    public static void drawAnimatedUnfilledCircle(final float x, final float y2, final float radius, final float lineWidth, final int color) {
    }
    
    public static void drawCircle(final float x, final float y2, final float radius, final int color) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d(x + Math.sin(i * 3.141592653589793 / 180.0) * radius, y2 + Math.cos(i * 3.141592653589793 / 180.0) * radius);
        }
        GL11.glEnd();
    }
    
    public static void drawFloatCircle(final float x, final float y2, final float radius, final int color) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d(x + Math.sin(i * 3.141526 / 180.0) * radius, y2 + Math.cos(i * 3.141526 / 180.0) * radius);
        }
        GL11.glEnd();
    }
    
    public static void prepareScissorBox(final float x, final float y2, final float x2, final float y22) {
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        final int factor = sr.getScaleFactor();
        GL11.glScissor((int)(x * factor), (int)((sr.getScaledHeight() - y22) * factor), (int)((x2 - x) * factor), (int)((y22 - y2) * factor));
    }
    
    public static void drawEsp(final Entity o, final int c2) {
        box(o.posX - 0.5, o.posY, o.posZ - 0.5, o.posX + 0.5, o.posY + 2.0, o.posZ + 0.5, new Color(c2));
    }
    
    public static void drawItemEsp(final EntityItem e, final int c) {
       box(e.posX - 0.2, e.posY + 0.699999988079071, e.posZ - 0.2, e.posX + 0.2, e.posY + 0.2, e.posZ + 0.2, new Color(c));
    }
    
    static {
        RenderUtils2.fade = 0;
        RenderUtils2.fadeIn = true;
        RenderUtils2.fadeOut = false;
    }
}
