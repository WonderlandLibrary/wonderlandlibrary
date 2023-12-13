package vip.radium.utils.render;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;

public final class OGLUtils {

    private static final FloatBuffer windowPosition = GLAllocation.createDirectFloatBuffer(4);
    private static final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private static final FloatBuffer modelMatrix = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer projectionMatrix = GLAllocation.createDirectFloatBuffer(16);
    private static final float[] BUFFER = new float[3];

    private OGLUtils() {
    }

    public static void enableBlending() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void disableTexture2D() {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    public static void enableTexture2D() {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void enableDepth() {
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    public static void disableDepth() {
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    public static void preDraw(int color, int mode) {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        color(color);
        glBegin(mode);
    }

    public static void postDraw() {
        glEnd();
        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
    }

    public static void color(int color) {
        glColor4ub(
                (byte) (color >> 16 & 0xFF),
                (byte) (color >> 8 & 0xFF),
                (byte) (color & 0xFF),
                (byte) (color >> 24 & 0xFF));
    }

    public static void startScissorBox(ScaledResolution sr,
                                       int x,
                                       int y,
                                       int width,
                                       int height) {
        int sf = sr.getScaleFactor();
        GL11.glScissor(
                x * sf,
                (sr.getScaledHeight() - (y + height)) * sf,
                width * sf,
                height * sf);
    }

    public static void startScissorBox(LockedResolution lr,
                                       int x,
                                       int y,
                                       int width,
                                       int height) {
        GL11.glScissor(
                x * LockedResolution.SCALE_FACTOR,
                (lr.getHeight() - (y + height)) * LockedResolution.SCALE_FACTOR,
                width * LockedResolution.SCALE_FACTOR,
                height * LockedResolution.SCALE_FACTOR);
    }

    public static float[] project2D(float x,
                                    float y,
                                    float z,
                                    int scaleFactor) {
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelMatrix);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projectionMatrix);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);

        if (GLU.gluProject(x, y, z,
                modelMatrix, projectionMatrix, viewport, windowPosition)) {
            BUFFER[0] = windowPosition.get(0) / scaleFactor;
            BUFFER[1] = (Display.getHeight() - windowPosition.get(1)) / scaleFactor;
            BUFFER[2] = windowPosition.get(2);
            return BUFFER;
        }

        return null;
    }
}
