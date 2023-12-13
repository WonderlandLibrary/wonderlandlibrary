package net.minecraft.client.renderer;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

public class RenderHelper {
    /**
     * Float buffer used to set OpenGL material colors
     */
    private static FloatBuffer colorBuffer = GLAllocation.createDirectFloatBuffer(16);
    private static final Vec3 field_82884_b = (new Vec3(0.20000000298023224D, 1.0D, -0.699999988079071D)).normalize();
    private static final Vec3 field_82885_c = (new Vec3(-0.20000000298023224D, 1.0D, 0.699999988079071D)).normalize();
    private static final String __OBFID = "CL_00000629";

    /**
     * Disables the OpenGL lighting properties enabled by enableStandardItemLighting
     */
    public static void disableStandardItemLighting() {
        GlStateManager.disableLighting();
        GlStateManager.disableBooleanStateAt(0);
        GlStateManager.disableBooleanStateAt(1);
        GlStateManager.disableColorMaterial();
    }

    /**
     * Sets the OpenGL lighting properties to the values used when rendering blocks as items
     */
    public static void enableStandardItemLighting() {
        GlStateManager.enableLighting();
        GlStateManager.enableBooleanStateAt(0);
        GlStateManager.enableBooleanStateAt(1);
        GlStateManager.enableColorMaterial();
        GlStateManager.colorMaterial(1032, 5634);
        float var0 = 0.4F;
        float var1 = 0.6F;
        float var2 = 0.0F;
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, setColorBuffer(field_82884_b.xCoord, field_82884_b.yCoord, field_82884_b.zCoord, 0.0D));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, setColorBuffer(var1, var1, var1, 1.0F));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, setColorBuffer(var2, var2, var2, 1.0F));
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, setColorBuffer(field_82885_c.xCoord, field_82885_c.yCoord, field_82885_c.zCoord, 0.0D));
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, setColorBuffer(var1, var1, var1, 1.0F));
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPECULAR, setColorBuffer(var2, var2, var2, 1.0F));
        GlStateManager.shadeModel(7424);
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, setColorBuffer(var0, var0, var0, 1.0F));
    }

    /**
     * Update and return colorBuffer with the RGBA values passed as arguments
     */
    private static FloatBuffer setColorBuffer(double p_74517_0_, double p_74517_2_, double p_74517_4_, double p_74517_6_) {
        return setColorBuffer((float) p_74517_0_, (float) p_74517_2_, (float) p_74517_4_, (float) p_74517_6_);
    }

    /**
     * Update and return colorBuffer with the RGBA values passed as arguments
     */
    private static FloatBuffer setColorBuffer(float p_74521_0_, float p_74521_1_, float p_74521_2_, float p_74521_3_) {
        colorBuffer.clear();
        colorBuffer.put(p_74521_0_).put(p_74521_1_).put(p_74521_2_).put(p_74521_3_);
        colorBuffer.flip();
        return colorBuffer;
    }


    public static void drawRect(float x, float y, float x1, float y1) {
        GL11.glBegin((int) 7);
        GL11.glVertex2f((float) x, (float) y1);
        GL11.glVertex2f((float) x1, (float) y1);
        GL11.glVertex2f((float) x1, (float) y);
        GL11.glVertex2f((float) x, (float) y);
        GL11.glEnd();
    }

    public static void glColor(int hex) {
        float alpha = (float) (hex >> 24 & 255) / 255.0f;
        float red = (float) (hex >> 16 & 255) / 255.0f;
        float green = (float) (hex >> 8 & 255) / 255.0f;
        float blue = (float) (hex & 255) / 255.0f;
        GL11.glColor4f((float) red, (float) green, (float) blue, (float) alpha);
    }

    public static void drawOutlineBox(AxisAlignedBB axisalignedbb, float width, int color) {
        GL11.glLineWidth((float) width);
        GL11.glEnable((int) 2848);
        GL11.glEnable((int) 2881);
        GL11.glHint((int) 3154, (int) 4354);
        GL11.glHint((int) 3155, (int) 4354);
        RenderHelper.glColor(color);
        RenderHelper.drawOutlinedBox(axisalignedbb);
        GL11.glDisable((int) 2848);
        GL11.glDisable((int) 2881);
    }

    public static void drawCrosses(AxisAlignedBB axisalignedbb, float width, int color) {
        GL11.glLineWidth((float) width);
        GL11.glEnable((int) 2848);
        GL11.glEnable((int) 2881);
        GL11.glHint((int) 3154, (int) 4354);
        GL11.glHint((int) 3155, (int) 4354);
        RenderHelper.glColor(color);
        RenderHelper.drawCrosses(axisalignedbb);
        GL11.glDisable((int) 2848);
        GL11.glDisable((int) 2881);
    }

    public static void drawCompleteBox(AxisAlignedBB axisalignedbb, float width, int insideColor, int borderColor) {
        GL11.glLineWidth((float) width);
        GL11.glEnable((int) 2848);
        GL11.glEnable((int) 2881);
        GL11.glHint((int) 3154, (int) 4354);
        GL11.glHint((int) 3155, (int) 4354);
        RenderHelper.glColor(insideColor);
        RenderHelper.drawBox(axisalignedbb);
        RenderHelper.glColor(borderColor);
        RenderHelper.drawOutlinedBox(axisalignedbb);
        RenderHelper.drawCrosses(axisalignedbb);
        GL11.glDisable((int) 2848);
        GL11.glDisable((int) 2881);
    }

    public static void drawCrosses(AxisAlignedBB box) {
        if (box == null) {
            return;
        }
        GL11.glBegin((int) 1);
        GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.maxZ);
        GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.minZ);
        GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.minZ);
        GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.maxZ);
        GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.minZ);
        GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.minZ);
        GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.maxZ);
        GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.maxZ);
        GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.maxZ);
        GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.minZ);
        GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.minZ);
        GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.maxZ);
        GL11.glEnd();
    }

    public static void drawBox(AxisAlignedBB axisalignedbb, int color) {
        GL11.glEnable((int) 2848);
        GL11.glEnable((int) 2881);
        GL11.glHint((int) 3154, (int) 4354);
        GL11.glHint((int) 3155, (int) 4354);
        RenderHelper.glColor(color);
        RenderHelper.drawBox(axisalignedbb);
        GL11.glDisable((int) 2848);
        GL11.glDisable((int) 2881);
    }

    public static void drawOutlinedBox(AxisAlignedBB box) {
        if (box == null) {
            return;
        }
        GL11.glBegin((int) 3);
        GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.minZ);
        GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.minZ);
        GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.maxZ);
        GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.maxZ);
        GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.minZ);
        GL11.glEnd();
        GL11.glBegin((int) 3);
        GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.minZ);
        GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.minZ);
        GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.maxZ);
        GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.maxZ);
        GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.minZ);
        GL11.glEnd();
        GL11.glBegin((int) 1);
        GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.minZ);
        GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.minZ);
        GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.minZ);
        GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.minZ);
        GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.maxZ);
        GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.maxZ);
        GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.maxZ);
        GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.maxZ);
        GL11.glEnd();
    }

    public static void drawBox(AxisAlignedBB box) {
        if (box == null) {
            return;
        }
        GL11.glBegin((int) 7);
        GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.maxZ);
        GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.maxZ);
        GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.maxZ);
        GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.maxZ);
        GL11.glEnd();
        GL11.glBegin((int) 7);
        GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.maxZ);
        GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.maxZ);
        GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.maxZ);
        GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.maxZ);
        GL11.glEnd();
        GL11.glBegin((int) 7);
        GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.minZ);
        GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.maxZ);
        GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.maxZ);
        GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.minZ);
        GL11.glEnd();
        GL11.glBegin((int) 7);
        GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.maxZ);
        GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.minZ);
        GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.minZ);
        GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.maxZ);
        GL11.glEnd();
        GL11.glBegin((int) 7);
        GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.maxZ);
        GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.minZ);
        GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.minZ);
        GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.maxZ);
        GL11.glEnd();
        GL11.glBegin((int) 7);
        GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.minZ);
        GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.maxZ);
        GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.maxZ);
        GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.minZ);
        GL11.glEnd();
        GL11.glBegin((int) 7);
        GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.minZ);
        GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.minZ);
        GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.minZ);
        GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.minZ);
        GL11.glEnd();
        GL11.glBegin((int) 7);
        GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.minZ);
        GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.minZ);
        GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.minZ);
        GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.minZ);
        GL11.glEnd();
        GL11.glBegin((int) 7);
        GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.minZ);
        GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.minZ);
        GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.maxZ);
        GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.maxZ);
        GL11.glEnd();
        GL11.glBegin((int) 7);
        GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.minZ);
        GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.minZ);
        GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.maxZ);
        GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.maxZ);
        GL11.glEnd();
        GL11.glBegin((int) 7);
        GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.minZ);
        GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.minZ);
        GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.maxZ);
        GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.maxZ);
        GL11.glEnd();
        GL11.glBegin((int) 7);
        GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.minZ);
        GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.minZ);
        GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.maxZ);
        GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.maxZ);
        GL11.glEnd();
    }

    public static void rectangle(double left, double top, double right, double bottom, int color) {
        double var5;
        if (left < right) {
            var5 = left;
            left = right;
            right = var5;
        }
        if (top < bottom) {
            var5 = top;
            top = bottom;
            bottom = var5;
        }
        float var11 = (float) (color >> 24 & 255) / 255.0f;
        float var6 = (float) (color >> 16 & 255) / 255.0f;
        float var7 = (float) (color >> 8 & 255) / 255.0f;
        float var8 = (float) (color & 255) / 255.0f;
        WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var6, var7, var8, var11);
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertex(left, bottom, 0.0);
        worldRenderer.addVertex(right, bottom, 0.0);
        worldRenderer.addVertex(right, top, 0.0);
        worldRenderer.addVertex(left, top, 0.0);
        Tessellator.getInstance().draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    /**
     * Sets OpenGL lighting for rendering blocks as items inside GUI screens (such as containers).
     */
    public static void enableGUIStandardItemLighting() {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(-30.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(165.0F, 1.0F, 0.0F, 0.0F);
        enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}