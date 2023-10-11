/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.utils;

import cow.milkgod.cheese.events.Event3D;
import cow.milkgod.cheese.utils.RenderBox;
import java.awt.Color;
import java.awt.Rectangle;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public final class RenderUtils {
    public static final RenderItem RENDER_ITEM = new RenderItem(Minecraft.getMinecraft().renderEngine, Minecraft.getMinecraft().modelManager);
    private static ScaledResolution scaledResolution;

    public static void drawSearchBlock(Block block, BlockPos blockPos, Event3D event) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        GlStateManager.pushMatrix();
        GL11.glLineWidth(1.0f);
        GlStateManager.disableDepth();
        RenderUtils.disableLighting();
        double var8 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)Event3D.getPartialTicks();
        double var9 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)Event3D.getPartialTicks();
        double var10 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)Event3D.getPartialTicks();
        RenderGlobal.drawOutlinedBoundingBox(block.getSelectedBoundingBox(Minecraft.getMinecraft().theWorld, blockPos).expand(0.0020000000949949026, 0.0020000000949949026, 0.0020000000949949026).offset(- var8, - var9, - var10), -1);
        GlStateManager.popMatrix();
    }

    public static void drawEsp(EntityLivingBase ent, float pTicks, int hexColor, int hexColorIn) {
        if (!ent.isEntityAlive()) {
            return;
        }
        double x2 = RenderUtils.getDiff(ent.lastTickPosX, ent.posX, pTicks, RenderManager.renderPosX);
        double y2 = RenderUtils.getDiff(ent.lastTickPosY, ent.posY, pTicks, RenderManager.renderPosY);
        double z2 = RenderUtils.getDiff(ent.lastTickPosZ, ent.posZ, pTicks, RenderManager.renderPosZ);
        RenderUtils.boundingBox(ent, x2, y2, z2, hexColor, hexColorIn);
    }

    public static void boundingBox(Entity entity, double x2, double y2, double z2, int color, int colorIn) {
        GlStateManager.pushMatrix();
        GL11.glLineWidth(1.0f);
        AxisAlignedBB var11 = entity.getEntityBoundingBox();
        AxisAlignedBB var12 = new AxisAlignedBB(var11.minX - entity.posX + x2, var11.minY - entity.posY + y2, var11.minZ - entity.posZ + z2, var11.maxX - entity.posX + x2, var11.maxY - entity.posY + y2, var11.maxZ - entity.posZ + z2);
        if (color != 0) {
            GlStateManager.disableDepth();
            RenderUtils.filledBox(var12, colorIn, true);
            RenderUtils.disableLighting();
            RenderGlobal.drawOutlinedBoundingBox(var12, color);
        }
        GlStateManager.popMatrix();
    }

    public static void enableLighting() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glMatrixMode(5890);
        GL11.glLoadIdentity();
        float var3 = 0.0039063f;
        GL11.glScalef(0.0039063f, 0.0039063f, 0.0039063f);
        GL11.glTranslatef(8.0f, 8.0f, 8.0f);
        GL11.glMatrixMode(5888);
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glTexParameteri(3553, 10242, 10496);
        GL11.glTexParameteri(3553, 10243, 10496);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public static void disableLighting() {
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(3553);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
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

    public static void enableGL3D(float lineWidth) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        Minecraft.getMinecraft().entityRenderer.func_175072_h();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(lineWidth);
    }

    public static void disableGL3D() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void drawRect(float x2, float y2, float x1, float y1, int color) {
        RenderUtils.enableGL2D();
        RenderUtils.glColor(color);
        RenderUtils.drawRect(x2, y2, x1, y1);
        RenderUtils.disableGL2D();
    }

    public static void drawBorderedRect(float x2, float y2, float x1, float y1, float width, int internalColor, int borderColor) {
        RenderUtils.enableGL2D();
        RenderUtils.glColor(internalColor);
        RenderUtils.drawRect(x2 + width, y2 + width, x1 - width, y1 - width);
        RenderUtils.glColor(borderColor);
        RenderUtils.drawRect(x2 + width, y2, x1 - width, y2 + width);
        RenderUtils.drawRect(x2, y2, x2 + width, y1);
        RenderUtils.drawRect(x1 - width, y2, x1, y1);
        RenderUtils.drawRect(x2 + width, y1 - width, x1 - width, y1);
        RenderUtils.disableGL2D();
    }

    public static void drawBorderedRect(int x2, int y2, int x1, int y1, int insideC, int borderC) {
        RenderUtils.enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        RenderUtils.drawVLine(x2 *= 2, y2 *= 2, (y1 *= 2) - 1, borderC);
        RenderUtils.drawVLine((x1 *= 2) - 1, y2, y1, borderC);
        RenderUtils.drawHLine(x2, x1 - 1, y2, borderC);
        RenderUtils.drawHLine(x2, x1 - 2, y1 - 1, borderC);
        RenderUtils.drawRect(x2 + 1, y2 + 1, x1 - 1, y1 - 1, insideC);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        RenderUtils.disableGL2D();
    }

    public static void drawBorderedRectReliant(float x2, float y2, float x1, float y1, float lineWidth, int inside, int border) {
        RenderUtils.enableGL2D();
        RenderUtils.drawRect(x2, y2, x1, y1, inside);
        RenderUtils.glColor(border);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(3);
        GL11.glVertex2f(x2, y2);
        GL11.glVertex2f(x2, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y2);
        GL11.glVertex2f(x2, y2);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        RenderUtils.disableGL2D();
    }

    public static void drawGradientBorderedRectReliant(float x2, float y2, float x1, float y1, float lineWidth, int border, int bottom, int top) {
        RenderUtils.enableGL2D();
        RenderUtils.drawGradientRect(x2, y2, x1, y1, top, bottom);
        RenderUtils.glColor(border);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(3);
        GL11.glVertex2f(x2, y2);
        GL11.glVertex2f(x2, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y2);
        GL11.glVertex2f(x2, y2);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        RenderUtils.disableGL2D();
    }

    public static void drawRoundedRect(float x2, float y2, float x1, float y1, int borderC, int insideC) {
        RenderUtils.enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        RenderUtils.drawVLine(x2 *= 2.0f, (y2 *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
        RenderUtils.drawVLine((x1 *= 2.0f) - 1.0f, y2 + 1.0f, y1 - 2.0f, borderC);
        RenderUtils.drawHLine(x2 + 2.0f, x1 - 3.0f, y2, borderC);
        RenderUtils.drawHLine(x2 + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
        RenderUtils.drawHLine(x2 + 1.0f, x2 + 1.0f, y2 + 1.0f, borderC);
        RenderUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y2 + 1.0f, borderC);
        RenderUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
        RenderUtils.drawHLine(x2 + 1.0f, x2 + 1.0f, y1 - 2.0f, borderC);
        RenderUtils.drawRect(x2 + 1.0f, y2 + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        RenderUtils.disableGL2D();
    }

    public static void drawBorderedRect(Rectangle rectangle, float width, int internalColor, int borderColor) {
        float x2 = rectangle.x;
        float y2 = rectangle.y;
        float x22 = rectangle.x + rectangle.width;
        float y22 = rectangle.y + rectangle.height;
        RenderUtils.enableGL2D();
        RenderUtils.glColor(internalColor);
        RenderUtils.drawRect(x2 + width, y2 + width, x22 - width, y22 - width);
        RenderUtils.glColor(borderColor);
        RenderUtils.drawRect(x2 + 1.0f, y2, x22 - 1.0f, y2 + width);
        RenderUtils.drawRect(x2, y2, x2 + width, y22);
        RenderUtils.drawRect(x22 - width, y2, x22, y22);
        RenderUtils.drawRect(x2 + 1.0f, y22 - width, x22 - 1.0f, y22);
        RenderUtils.disableGL2D();
    }

    public static void drawGradientRect(float x2, float y2, float x1, float y1, int topColor, int bottomColor) {
        RenderUtils.enableGL2D();
        GL11.glShadeModel(7425);
        GL11.glBegin(7);
        RenderUtils.glColor(topColor);
        GL11.glVertex2f(x2, y1);
        GL11.glVertex2f(x1, y1);
        RenderUtils.glColor(bottomColor);
        GL11.glVertex2f(x1, y2);
        GL11.glVertex2f(x2, y2);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        RenderUtils.disableGL2D();
    }

    public static void drawGradientHRect(float x2, float y2, float x1, float y1, int topColor, int bottomColor) {
        RenderUtils.enableGL2D();
        GL11.glShadeModel(7425);
        GL11.glBegin(7);
        RenderUtils.glColor(topColor);
        GL11.glVertex2f(x2, y2);
        GL11.glVertex2f(x2, y1);
        RenderUtils.glColor(bottomColor);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y2);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        RenderUtils.disableGL2D();
    }

    public static void drawGradientRect(double x2, double y2, double x22, double y22, int col1, int col2) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        RenderUtils.glColor(col1);
        GL11.glVertex2d(x22, y2);
        GL11.glVertex2d(x2, y2);
        RenderUtils.glColor(col2);
        GL11.glVertex2d(x2, y22);
        GL11.glVertex2d(x22, y22);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }

    public static void drawGradientBorderedRect(double x2, double y2, double x22, double y22, float l1, int col1, int col2, int col3) {
        RenderUtils.enableGL2D();
        GL11.glPushMatrix();
        RenderUtils.glColor(col1);
        GL11.glLineWidth(1.0f);
        GL11.glBegin(1);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y22);
        GL11.glVertex2d(x22, y22);
        GL11.glVertex2d(x22, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x22, y2);
        GL11.glVertex2d(x2, y22);
        GL11.glVertex2d(x22, y22);
        GL11.glEnd();
        GL11.glPopMatrix();
        RenderUtils.drawGradientRect(x2, y2, x22, y22, col2, col3);
        RenderUtils.disableGL2D();
    }

    public static void drawStrip(int x2, int y2, float width, double angle, float points, float radius, int color) {
        float a2;
        float xc2;
        float yc2;
        int i2;
        float f1 = (float)(color >> 24 & 255) / 255.0f;
        float f2 = (float)(color >> 16 & 255) / 255.0f;
        float f3 = (float)(color >> 8 & 255) / 255.0f;
        float f4 = (float)(color & 255) / 255.0f;
        GL11.glPushMatrix();
        GL11.glTranslated(x2, y2, 0.0);
        GL11.glColor4f(f2, f3, f4, f1);
        GL11.glLineWidth(width);
        if (angle > 0.0) {
            GL11.glBegin(3);
            i2 = 0;
            while ((double)i2 < angle) {
                a2 = (float)((double)i2 * (angle * 3.141592653589793 / (double)points));
                xc2 = (float)(Math.cos(a2) * (double)radius);
                yc2 = (float)(Math.sin(a2) * (double)radius);
                GL11.glVertex2f(xc2, yc2);
                ++i2;
            }
            GL11.glEnd();
        }
        if (angle < 0.0) {
            GL11.glBegin(3);
            i2 = 0;
            while ((double)i2 > angle) {
                a2 = (float)((double)i2 * (angle * 3.141592653589793 / (double)points));
                xc2 = (float)(Math.cos(a2) * (double)(- radius));
                yc2 = (float)(Math.sin(a2) * (double)(- radius));
                GL11.glVertex2f(xc2, yc2);
                --i2;
            }
            GL11.glEnd();
        }
        RenderUtils.disableGL2D();
        GL11.glDisable(3479);
        GL11.glPopMatrix();
    }

    public static void drawHLine(float x2, float y2, float x1, int y1) {
        if (y2 < x2) {
            float var5 = x2;
            x2 = y2;
            y2 = var5;
        }
        RenderUtils.drawRect(x2, x1, y2 + 1.0f, x1 + 1.0f, y1);
    }

    public static void drawVLine(float x2, float y2, float x1, int y1) {
        if (x1 < y2) {
            float var5 = y2;
            y2 = x1;
            x1 = var5;
        }
        RenderUtils.drawRect(x2, y2 + 1.0f, x2 + 1.0f, x1, y1);
    }

    public static void drawHLine(float x2, float y2, float x1, int y1, int y22) {
        if (y2 < x2) {
            float var5 = x2;
            x2 = y2;
            y2 = var5;
        }
        RenderUtils.drawGradientRect(x2, x1, y2 + 1.0f, x1 + 1.0f, y1, y22);
    }

    public static void drawRect(float x2, float y2, float x1, float y1, float r2, float g2, float b2, float a2) {
        RenderUtils.enableGL2D();
        GL11.glColor4f(r2, g2, b2, a2);
        RenderUtils.drawRect(x2, y2, x1, y1);
        RenderUtils.disableGL2D();
    }

    public static void drawRect(float x2, float y2, float x1, float y1) {
        GL11.glBegin(7);
        GL11.glVertex2f(x2, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y2);
        GL11.glVertex2f(x2, y2);
        GL11.glEnd();
    }

    public static void drawCircle(float cx, float cy2, float r2, int num_segments, int c2) {
        GL11.glPushMatrix();
        cx *= 2.0f;
        cy2 *= 2.0f;
        float f2 = (float)(c2 >> 24 & 255) / 255.0f;
        float f22 = (float)(c2 >> 16 & 255) / 255.0f;
        float f3 = (float)(c2 >> 8 & 255) / 255.0f;
        float f4 = (float)(c2 & 255) / 255.0f;
        float theta = (float)(6.2831852 / (double)num_segments);
        float p2 = (float)Math.cos(theta);
        float s = (float)Math.sin(theta);
        float x2 = r2 *= 2.0f;
        float y2 = 0.0f;
        RenderUtils.enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GL11.glColor4f(f22, f3, f4, f2);
        GL11.glBegin(2);
        int ii2 = 0;
        while (ii2 < num_segments) {
            GL11.glVertex2f(x2 + cx, y2 + cy2);
            float t = x2;
            x2 = p2 * x2 - s * y2;
            y2 = s * t + p2 * y2;
            ++ii2;
        }
        GL11.glEnd();
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        RenderUtils.disableGL2D();
        GL11.glPopMatrix();
    }

    public static void drawFullCircle(int cx, int cy2, double r2, int c2) {
        r2 *= 2.0;
        cx *= 2;
        cy2 *= 2;
        float f2 = (float)(c2 >> 24 & 255) / 255.0f;
        float f22 = (float)(c2 >> 16 & 255) / 255.0f;
        float f3 = (float)(c2 >> 8 & 255) / 255.0f;
        float f4 = (float)(c2 & 255) / 255.0f;
        RenderUtils.enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GL11.glColor4f(f22, f3, f4, f2);
        GL11.glBegin(6);
        int i2 = 0;
        while (i2 <= 360) {
            double x2 = Math.sin((double)i2 * 3.141592653589793 / 180.0) * r2;
            double y2 = Math.cos((double)i2 * 3.141592653589793 / 180.0) * r2;
            GL11.glVertex2d((double)cx + x2, (double)cy2 + y2);
            ++i2;
        }
        GL11.glEnd();
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        RenderUtils.disableGL2D();
    }

    public static void glColor(Color color) {
        GL11.glColor4f((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
    }

    public static void glColor(int hex) {
        float alpha = (float)(hex >> 24 & 255) / 255.0f;
        float red = (float)(hex >> 16 & 255) / 255.0f;
        float green = (float)(hex >> 8 & 255) / 255.0f;
        float blue = (float)(hex & 255) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static void glColor(float alpha, int redRGB, int greenRGB, int blueRGB) {
        float red = 0.003921569f * (float)redRGB;
        float green = 0.003921569f * (float)greenRGB;
        float blue = 0.003921569f * (float)blueRGB;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static void updateScaledResolution() {
        scaledResolution = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
    }

    public static ScaledResolution getScaledResolution() {
        return scaledResolution;
    }

    public static void prepareScissorBox(float x2, float y2, float x22, float y22) {
        RenderUtils.updateScaledResolution();
        int factor = scaledResolution.getScaleFactor();
        GL11.glScissor((int)(x2 * (float)factor), (int)(((float)scaledResolution.getScaledHeight() - y22) * (float)factor), (int)((x22 - x2) * (float)factor), (int)((y22 - y2) * (float)factor));
    }

    public static void drawOutlinedBox(AxisAlignedBB boundingBox) {
        if (boundingBox == null) {
            return;
        }
        GL11.glBegin(3);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxZ, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxZ, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glEnd();
        GL11.glBegin(3);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxZ, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxZ, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glEnd();
        GL11.glBegin(1);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxZ, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxZ, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxZ, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxZ, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glEnd();
    }

    public static void drawBox(RenderBox box) {
        GL11.glEnable(1537);
        if (box == null) {
            return;
        }
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glEnd();
    }

    public static void filledBox(AxisAlignedBB aa2, int color, boolean shouldColor) {
        GlStateManager.pushMatrix();
        float var11 = (float)(color >> 24 & 255) / 255.0f;
        float var12 = (float)(color >> 16 & 255) / 255.0f;
        float var13 = (float)(color >> 8 & 255) / 255.0f;
        float var14 = (float)(color & 255) / 255.0f;
        Tessellator var15 = Tessellator.getInstance();
        WorldRenderer t = var15.getWorldRenderer();
        if (shouldColor) {
            GlStateManager.color(var12, var13, var14, var11);
        }
        int draw = 7;
        t.startDrawing(7);
        t.addVertex(aa2.minX, aa2.minY, aa2.minZ);
        t.addVertex(aa2.minX, aa2.maxY, aa2.minZ);
        t.addVertex(aa2.maxX, aa2.minY, aa2.minZ);
        t.addVertex(aa2.maxX, aa2.maxY, aa2.minZ);
        t.addVertex(aa2.maxX, aa2.minY, aa2.maxZ);
        t.addVertex(aa2.maxX, aa2.maxY, aa2.maxZ);
        t.addVertex(aa2.minX, aa2.minY, aa2.maxZ);
        t.addVertex(aa2.minX, aa2.maxY, aa2.maxZ);
        var15.draw();
        t.startDrawing(7);
        t.addVertex(aa2.maxX, aa2.maxY, aa2.minZ);
        t.addVertex(aa2.maxX, aa2.minY, aa2.minZ);
        t.addVertex(aa2.minX, aa2.maxY, aa2.minZ);
        t.addVertex(aa2.minX, aa2.minY, aa2.minZ);
        t.addVertex(aa2.minX, aa2.maxY, aa2.maxZ);
        t.addVertex(aa2.minX, aa2.minY, aa2.maxZ);
        t.addVertex(aa2.maxX, aa2.maxY, aa2.maxZ);
        t.addVertex(aa2.maxX, aa2.minY, aa2.maxZ);
        var15.draw();
        t.startDrawing(7);
        t.addVertex(aa2.minX, aa2.maxY, aa2.minZ);
        t.addVertex(aa2.maxX, aa2.maxY, aa2.minZ);
        t.addVertex(aa2.maxX, aa2.maxY, aa2.maxZ);
        t.addVertex(aa2.minX, aa2.maxY, aa2.maxZ);
        t.addVertex(aa2.minX, aa2.maxY, aa2.minZ);
        t.addVertex(aa2.minX, aa2.maxY, aa2.maxZ);
        t.addVertex(aa2.maxX, aa2.maxY, aa2.maxZ);
        t.addVertex(aa2.maxX, aa2.maxY, aa2.minZ);
        var15.draw();
        t.startDrawing(7);
        t.addVertex(aa2.minX, aa2.minY, aa2.minZ);
        t.addVertex(aa2.maxX, aa2.minY, aa2.minZ);
        t.addVertex(aa2.maxX, aa2.minY, aa2.maxZ);
        t.addVertex(aa2.minX, aa2.minY, aa2.maxZ);
        t.addVertex(aa2.minX, aa2.minY, aa2.minZ);
        t.addVertex(aa2.minX, aa2.minY, aa2.maxZ);
        t.addVertex(aa2.maxX, aa2.minY, aa2.maxZ);
        t.addVertex(aa2.maxX, aa2.minY, aa2.minZ);
        var15.draw();
        t.startDrawing(7);
        t.addVertex(aa2.minX, aa2.minY, aa2.minZ);
        t.addVertex(aa2.minX, aa2.maxY, aa2.minZ);
        t.addVertex(aa2.minX, aa2.minY, aa2.maxZ);
        t.addVertex(aa2.minX, aa2.maxY, aa2.maxZ);
        t.addVertex(aa2.maxX, aa2.minY, aa2.maxZ);
        t.addVertex(aa2.maxX, aa2.maxY, aa2.maxZ);
        t.addVertex(aa2.maxX, aa2.minY, aa2.minZ);
        t.addVertex(aa2.maxX, aa2.maxY, aa2.minZ);
        var15.draw();
        t.startDrawing(7);
        t.addVertex(aa2.minX, aa2.maxY, aa2.maxZ);
        t.addVertex(aa2.minX, aa2.minY, aa2.maxZ);
        t.addVertex(aa2.minX, aa2.maxY, aa2.minZ);
        t.addVertex(aa2.minX, aa2.minY, aa2.minZ);
        t.addVertex(aa2.maxX, aa2.maxY, aa2.minZ);
        t.addVertex(aa2.maxX, aa2.minY, aa2.minZ);
        t.addVertex(aa2.maxX, aa2.maxY, aa2.maxZ);
        t.addVertex(aa2.maxX, aa2.minY, aa2.maxZ);
        var15.draw();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
    }

    private static double getDiff(double lastI, double i2, float ticks, double ownI) {
        return lastI + (i2 - lastI) * (double)ticks - ownI;
    }

    public static void drawBeacon(BlockPos pos, int color, int colorIn, float partialTicks) {
        GlStateManager.pushMatrix();
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        double x2 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
        double y2 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
        double z2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;
        GL11.glLineWidth(1.0f);
        AxisAlignedBB var11 = new AxisAlignedBB(pos.getX() + 1, pos.getY(), pos.getZ() + 1, pos.getX(), pos.getY() + 200, pos.getZ());
        AxisAlignedBB var12 = new AxisAlignedBB(var11.minX - x2, var11.minY - y2, var11.minZ - z2, var11.maxX - x2, var11.maxY - y2, var11.maxZ - z2);
        if (color != 0) {
            GlStateManager.disableDepth();
            RenderUtils.filledBox(var12, colorIn, true);
            RenderUtils.disableLighting();
            RenderGlobal.drawOutlinedBoundingBox(var12, color);
        }
        GlStateManager.popMatrix();
    }
}

