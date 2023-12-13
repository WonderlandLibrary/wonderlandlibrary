package com.enjoytheban.utils.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.lwjgl.opengl.GL11;

import com.enjoytheban.utils.Helper;
import com.enjoytheban.utils.math.Vec2f;
import com.enjoytheban.utils.math.Vec3f;
import com.enjoytheban.utils.render.gl.GLClientState;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;
import pw.knx.feather.tessellate.Tessellation;


/**
 * Rendering Utils for Render Hacks/OpenGL
 * @author Purity
 */

public class RenderUtil {
    
    /**
     * Instance of the Tessellator
     */
    public static final Tessellation tessellator = Tessellation.createExpanding(4, 1, 2);

    /**
     * Stores ClientState Gl Caps when setting up
     */
    private static final List<Integer> csBuffer = new ArrayList<>();

    /**
     * Method reference to {@code GlStateManager#glEnableClientState(int)}
     */
    private static final Consumer<Integer> ENABLE_CLIENT_STATE = GL11::glEnableClientState;

    /**
     * Method reference to {@code GlStateManager#glDisableClientState(int)}
     */
    private static final Consumer<Integer> DISABLE_CLIENT_STATE = GL11::glEnableClientState;
	
	//Gets the width of the current minecraft resolution
	public static int width() {
		return new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth();
	}

	//Gets the height of the current minecraft resolution
	public static int height() {
		return new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight();
	}
	
	//Method for entity interpolation
    public static double interpolation(double newPos, double oldPos) {
        return oldPos + (newPos - oldPos) * Helper.mc.timer.renderPartialTicks;
    }
    
    //returns the hex as an rgb value
    public static int getHexRGB(int hex) {
        return 0xFF000000 | hex;
    }

    //draw rects
    public static void drawRect(double left, double top, double right, double bottom, int color)
    {
        if (left < right)
        {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom)
        {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.startDrawing(7);
        worldrenderer.addVertex((double)left, (double)bottom, 0.0D);
        worldrenderer.addVertex((double)right, (double)bottom, 0.0D);
        worldrenderer.addVertex((double)right, (double)top, 0.0D);
        worldrenderer.addVertex((double)left, (double)top, 0.0D);
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawCustomImage(final double x, final double y, final double xwidth, final double ywidth, final ResourceLocation image) {
        final double par1 = x + xwidth;
        final double par2 = y + ywidth;
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        final Tessellator var3 = Tessellator.getInstance();
        final WorldRenderer var4 = var3.getWorldRenderer();
        var4.startDrawingQuads();
        var4.addVertexWithUV(x, par2, 0.0, 0.0, 1.0);
        var4.addVertexWithUV(par1, par2, 0.0, 1.0, 1.0);
        var4.addVertexWithUV(par1, y, 0.0, 1.0, 0.0);
        var4.addVertexWithUV(x, y, 0.0, 0.0, 0.0);
        var3.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, int col1, int col2)
    {
        drawRect(x, y, x2, y2, col2);

        float f = (col1 >> 24 & 0xFF) / 255.0F;
        float f1 = (col1 >> 16 & 0xFF) / 255.0F;
        float f2 = (col1 >> 8 & 0xFF) / 255.0F;
        float f3 = (col1 & 0xFF) / 255.0F;

        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);

        GL11.glPushMatrix();
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();

        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    //pre/post shit
    public static void pre() {
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
    }
    
    public static void post() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glColor3d(1.0, 1.0, 1.0);
    }
    
    public static void startDrawing(){       
        GL11.glEnable(3042);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(2929);
        Helper.mc.entityRenderer.setupCameraTransform(Helper.mc.timer.renderPartialTicks,0);
    }
    public static void stopDrawing(){
        GL11.glDisable(3042);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(2929);
    }

    public static Color blend(Color color1, Color color2, double ratio) {
        float r = (float) ratio;
        float ir = (float) 1.0 - r;

        float rgb1[] = new float[3];
        float rgb2[] = new float[3];

        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);

        Color color = new Color(rgb1[0] * r + rgb2[0] * ir,
                rgb1[1] * r + rgb2[1] * ir,
                rgb1[2] * r + rgb2[2] * ir);
        return color;
    }
    
    
    /**
     * Draws a line from one Vec2 to another
     *
     * @param start Starting Vec2
     * @param end Ending Vec2
     * @param width Line width
     */
    public static void drawLine(Vec2f start, Vec2f end, float width) {
        drawLine(start.getX(), start.getY(), end.getX(), end.getY(), width);
    }

    /**
     * Draws a line from one Vec3f to another
     *
     * @param start Starting Vec3f
     * @param end Ending Vec3f
     * @param width Line width
     */
    public static void drawLine(Vec3f start, Vec3f end, float width) {
        drawLine((float) start.getX(), (float) start.getY(), (float) start.getZ(), (float) end.getX(), (float) end.getY(), (float) end.getZ(), width);
    }

    /**
     * Draws a line from one position to another
     *
     * @param x Start X
     * @param y Start Y
     * @param x1 End X
     * @param y1 End Y
     * @param width Line width
     */
    public static void drawLine(float x, float y, float x1, float y1, float width) {
        drawLine(x, y, 0, x1, y1, 0, width);
    }

    /**
     * Draws a line from one position to another
     *
     * @param x Start X
     * @param y Start Y
     * @param z Start X
     * @param x1 End X
     * @param y1 End Y
     * @param z1 End Z
     * @param width Line width
     */
    public static void drawLine(float x, float y, float z, float x1, float y1, float z1, float width) {
        GL11.glLineWidth(width);

        setupRender(true);
        setupClientState(GLClientState.VERTEX, true);

        tessellator
                .addVertex(x, y, z)
                .addVertex(x1, y1, z1)
                .draw(GL11.GL_LINE_STRIP);

        setupClientState(GLClientState.VERTEX, false);
        setupRender(false);
    }
    
    /**
     * Enables/Disables the specified client state cap.
     *
     * @param enabled The new enabled state of the specified client state cap
     */
    public static void setupClientState(GLClientState state, boolean enabled) {
        csBuffer.clear();
        if (state.ordinal() > 0)
            csBuffer.add(state.getCap());

        csBuffer.add(GL11.GL_VERTEX_ARRAY);
        csBuffer.forEach(enabled ? ENABLE_CLIENT_STATE : DISABLE_CLIENT_STATE);
    }
    
    /**
     * Called before rendering. Enables blending,
     * line smoothing, disables 2D texturing,
     * depth and the depth mask.
     *
     * @param start true if we are starting to render, false if we are finishing
     */
    public static void setupRender(boolean start) {
        if (start) {
            GlStateManager.enableBlend();
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();

            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        } else {
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GlStateManager.enableDepth();
        }
        GlStateManager.depthMask(!start);
    }
}