package rip.helium.utils;

import com.sun.javafx.geom.Vec3d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import javax.vecmath.Vector3d;
import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;


public class RenderUtility {
    private final static Minecraft mc = Minecraft.getMinecraft();
    private final static Frustum frustrum = new Frustum();
    private final static IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private final static FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
    private final static FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static float calculateCompensation(float target, float current, long delta, double speed) {
        float diff = current - target;
        if (delta < 1) {
            delta = 1;
        }
        if (delta > 1000) {
            delta = 16;
        }
        if (diff > speed) {
            double xD = (speed * delta / (1000 / 60) < 0.5 ? 0.5 : speed * delta / (1000 / 60));
            current -= xD;
            if (current < target) {
                current = target;
            }
        } else if (diff < -speed) {
            double xD = (speed * delta / (1000 / 60) < 0.5 ? 0.5 : speed * delta / (1000 / 60));
            current += xD;
            if (current > target) {
                current = target;
            }
        } else {
            current = target;
        }
        return current;
    }

    public static void drawTexturedModalRect(final int x, final int y, final int u, final int v, final int width, final int height, final float zLevel) {
        final float var7 = 0.00390625f;
        final float var8 = 0.00390625f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(x + 0, y + height, zLevel).tex((u + 0) * var7, (v + height) * var8).endVertex();
        worldRenderer.pos(x + width, y + height, zLevel).tex((u + width) * var7, (v + height) * var8).endVertex();
        worldRenderer.pos(x + width, y + 0, zLevel).tex((u + width) * var7, (v + 0) * var8).endVertex();
        worldRenderer.pos(x + 0, y + 0, zLevel).tex((u + 0) * var7, (v + 0) * var8).endVertex();
        tessellator.draw();
    }

    public static ScaledResolution getResolution() {
        return new ScaledResolution(mc);
    }

    public static Vec3d to2D(double x, double y, double z) {
        FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);

        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelView);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);

        boolean result = GLU.gluProject((float) x, (float) y, (float) z, modelView, projection, viewport, screenCoords);
        if (result) {
            return new Vec3d(screenCoords.get(0), Display.getHeight() - screenCoords.get(1), screenCoords.get(2));
        }
        return null;
    }

    public static void drawArrow(float x, float y, int hexColor) {
        GL11.glPushMatrix();
        GL11.glScaled(1.3, 1.3, 1.3);

        x /= 1.3;
        y /= 1.3;
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        hexColor(hexColor);
        GL11.glLineWidth(2);

        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x + 3, y + 4);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d(x + 3, y + 4);
        GL11.glVertex2d(x + 6, y);
        GL11.glEnd();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glPopMatrix();
    }

    public static void drawTracerPointer(float x, float y, float size, float widthDiv, float heightDiv, int color) {
        boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        GL11.glPushMatrix();
        hexColor(color);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x - size / widthDiv, y + size);
        GL11.glVertex2d(x, y + size / heightDiv);
        GL11.glVertex2d(x + size / widthDiv, y + size);
        GL11.glVertex2d(x, y);
        GL11.glEnd();
        GL11.glColor4f(0, 0, 0, 0.8f);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x - size / widthDiv, y + size);
        GL11.glVertex2d(x, y + size / heightDiv);
        GL11.glVertex2d(x + size / widthDiv, y + size);
        GL11.glVertex2d(x, y);
        GL11.glEnd();
        GL11.glPopMatrix();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        if (!blend)
            GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    public static void hexColor(int hexColor) {
        float red = (hexColor >> 16 & 0xFF) / 255.0F;
        float green = (hexColor >> 8 & 0xFF) / 255.0F;
        float blue = (hexColor & 0xFF) / 255.0F;
        float alpha = (hexColor >> 24 & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static int getRainbow(int speed, int offset) {
        float hue = (System.currentTimeMillis() + offset) % speed;
        hue /= speed;
        return Color.getHSBColor(hue, 0.75f, 1f).getRGB();

    }

    public static float[] getRGBAs(int rgb) {
        return new float[]{((rgb >> 16) & 255) / 255F, ((rgb >> 8) & 255) / 255F, (rgb & 255) / 255F, ((rgb >> 24) & 255) / 255F};
    }

    public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
    }

    public static void OutlinedBB(AxisAlignedBB bb, float width, int color) {
        enable3D();
        glLineWidth(width);
        color(color);
        drawOutlinedBoundingBox(bb);
        disable3D();
    }

    public static void BB(AxisAlignedBB bb, int color) {
        enable3D();
        color(color);
        drawBoundingBox(bb);
        disable3D();
    }

    public static void enable3D() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LINE_SMOOTH);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
    }

    public static void disable3D() {
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDisable(GL_BLEND);
    }

    public static void color(int color) {
        GL11.glColor4f((color >> 16 & 0xFF) / 255f, (color >> 8 & 0xFF) / 255f, (color & 0xFF) / 255f, (color >> 24 & 0xFF) / 255f);
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();

    }

    public static void drawBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
    }

    public static Vector3d project(double x, double y, double z) {
        FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
        GL11.glGetFloat(2982, modelview);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        if (GLU.gluProject((float) x, (float) y, (float) z, modelview, projection, viewport, vector)) {
            return new Vector3d(vector.get(0) / getResolution().getScaleFactor(),
                    (Display.getHeight() - vector.get(1)) / getResolution().getScaleFactor(),
                    vector.get(2));
        }
        return null;
    }


    public static void drawCheckMark(float x, float y, int width, int color) {
        float f = (color >> 24 & 255) / 255.0f;
        float f1 = (color >> 16 & 255) / 255.0f;
        float f2 = (color >> 8 & 255) / 255.0f;
        float f3 = (color & 255) / 255.0f;
        GL11.glPushMatrix();
        glDisable(3553);
        glEnable(2848);
        glBlendFunc(770, 771);
        GL11.glLineWidth(2.2f);
        GL11.glBegin(3);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(x + width - 6.5, y + 3);
        GL11.glVertex2d(x + width - 11.5, y + 10);
        GL11.glVertex2d(x + width - 13.5, y + 8);
        GL11.glEnd();
        glEnable(3553);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static boolean isInViewFrustrum(Entity entity) {
        return isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    public static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        frustrum.setPosition(current.posX, current.posY, current.posZ);
        return frustrum.isBoundingBoxInFrustum(bb);
    }

    public static void prepareScissorBox(ScaledResolution sr, float x, float y, float width, float height) {
        float x2 = x + width;
        float y2 = y + height;
        int factor = sr.getScaleFactor();
        GL11.glScissor((int) (x * factor), (int) ((ScaledResolution.getScaledHeight() - y2) * factor), (int) ((x2 - x) * factor), (int) ((y2 - y) * factor));
    }

    public static void drawBorderedRect(double x, double y, double width, double height, double lineSize, int borderColor, int color) {
        Gui.drawRect(x, y, x + width, y + height, color);
        Gui.drawRect(x, y, x + width, y + lineSize, borderColor);
        Gui.drawRect(x, y, x + lineSize, y + height, borderColor);
        Gui.drawRect(x + width, y, x + width - lineSize, y + height, borderColor);
        Gui.drawRect(x, y + height, x + width, y + height - lineSize, borderColor);
    }

    public static void drawBordered(double x, double y, double x2, double y2, double thickness, int inside, int outline) {
        double fix = 0.0;
        if (thickness < 1.0) {
            fix = 1.0;
        }
        drawRect2(x + thickness, y + thickness, x2 - thickness, y2 - thickness, inside);
        drawRect2(x, y + 1.0 - fix, x + thickness, y2, outline);
        drawRect2(x, y, x2 - 1.0 + fix, y + thickness, outline);
        drawRect2(x2 - thickness, y, x2, y2 - 1.0 + fix, outline);
        drawRect2(x + 1.0 - fix, y2 - thickness, x2, y2, outline);
    }

    public static void drawRect(double x, double y, double width, double height, int color) {
        Gui.drawRect(x, y, x + width, y + height, color);
    }

    public static void drawRect2(double x, double y, double x2, double y2, int color) {
        Gui.drawRect(x, y, x2, y2, color);
    }


    public static void drawBorderedRoundedRect(float x, float y, float width, float height, float radius, float borderSize, int borderC, int insideC) {
        drawRoundedRect(x, y, width, height, radius, borderC);
        drawRoundedRect(x + borderSize, y + borderSize, width - (borderSize * 2), height - (borderSize * 2), radius, insideC);
    }

    public static void drawRoundedRectWithShadow(double x, double y, double width, double height, double radius, int color) {
        drawRoundedRect(x + 2, y + 1, width, height + 1, radius, new Color(0).getRGB());
        drawRoundedRect(x, y, width, height, radius, color);
    }

    public static void drawRoundedRect(double x, double y, double width, double height, double radius, int color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        double x1 = x + width;
        double y1 = y + height;
        float f = (color >> 24 & 0xFF) / 255.0F;
        float f1 = (color >> 16 & 0xFF) / 255.0F;
        float f2 = (color >> 8 & 0xFF) / 255.0F;
        float f3 = (color & 0xFF) / 255.0F;
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);

        x *= 2;
        y *= 2;
        x1 *= 2;
        y1 *= 2;

        glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(f1, f2, f3, f);
        glEnable(GL11.GL_LINE_SMOOTH);

        GL11.glBegin(GL11.GL_POLYGON);

        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x + radius + +(Math.sin((i * Math.PI / 180)) * (radius * -1)), y + radius + (Math.cos((i * Math.PI / 180)) * (radius * -1)));
        }

        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x + radius + (Math.sin((i * Math.PI / 180)) * (radius * -1)), y1 - radius + (Math.cos((i * Math.PI / 180)) * (radius * -1)));
        }

        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x1 - radius + (Math.sin((i * Math.PI / 180)) * radius), y1 - radius + (Math.cos((i * Math.PI / 180)) * radius));
        }

        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x1 - radius + (Math.sin((i * Math.PI / 180)) * radius), y + radius + (Math.cos((i * Math.PI / 180)) * radius));
        }

        GL11.glEnd();

        glEnable(GL11.GL_TEXTURE_2D);
        glDisable(GL11.GL_LINE_SMOOTH);
        glEnable(GL11.GL_TEXTURE_2D);

        GL11.glScaled(2, 2, 2);

        GL11.glPopAttrib();
        GL11.glColor4f(1, 1, 1, 1);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

    }
}
