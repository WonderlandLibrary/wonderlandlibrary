package com.enjoytheban.utils.render.gl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.rendering.EventRender3D;
import com.enjoytheban.utils.math.Vec3f;

import net.minecraft.client.renderer.GlStateManager;

/**
 * OpenGL utils
 *
 * @author Brady
 * @since 2/20/2017 12:00 PM
 */
public final class GLUtils {

    private GLUtils() {}

    public static final FloatBuffer MODELVIEW = BufferUtils.createFloatBuffer(16);
    public static final FloatBuffer PROJECTION = BufferUtils.createFloatBuffer(16);
    public static final IntBuffer VIEWPORT = BufferUtils.createIntBuffer(16);
    public static final FloatBuffer TO_SCREEN_BUFFER = BufferUtils.createFloatBuffer(3);
    public static final FloatBuffer TO_WORLD_BUFFER = BufferUtils.createFloatBuffer(3);

    // Calls clinit
    public static void init() {}    
    
    /**
     * Parses the RGBA values from a hex value
     *
     * @param hex The hex value
     * @return The parsed RGBA array
     */
    public static float[] getColor(int hex) {
        return new float[] {
                (hex >> 16 & 0xFF) / 255F,
                (hex >> 8 & 0xFF) / 255F,
                (hex & 0xFF) / 255F,
                (hex >> 24 & 0xFF) / 255F
        };
    }
    
    /**
     * Sets the color from a hex value
     *
     * @param hex The hex value
     */
    public static void glColor(int hex) {
        float[] color = getColor(hex);
        GlStateManager.color(color[0], color[1], color[2], color[3]);
    }

    /**
     * Rotates on the X axis at the X, Y, and Z
     * coordinates that are provided.
     *
     * @param angle The amount being rotated
     * @param x The x position being rotated on
     * @param y The y position being rotated on
     * @param z The z position being rotated on
     */
    public static void rotateX(float angle, double x, double y, double z) {
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(angle, 1, 0, 0);
        GlStateManager.translate(-x, -y, -z);
    }

    /**
     * Rotates on the Y axis at the X, Y, and Z
     * coordinates that are provided.
     *
     * @param angle The amount being rotated
     * @param x The x position being rotated on
     * @param y The y position being rotated on
     * @param z The z position being rotated on
     */
    public static void rotateY(float angle, double x, double y, double z) {
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(angle, 0, 1, 0);
        GlStateManager.translate(-x, -y, -z);
    }

    /**
     * Rotates on the Z axis at the X, Y, and Z
     * coordinates that are provided.
     *
     * @param angle The amount being rotated
     * @param x The x position being rotated on
     * @param y The y position being rotated on
     * @param z The z position being rotated on
     */
    public static void rotateZ(float angle, double x, double y, double z) {
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(angle, 0, 0, 1);
        GlStateManager.translate(-x, -y, -z);
    }

    /**
     * Converts a Vec3f to its screen projection
     *
     * @see GLUtils#toScreen(double, double, double)
     *
     * @return Screen projected coordinates
     */
    public static Vec3f toScreen(Vec3f pos) {
        return GLUtils.toScreen(pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * Projects the specified XYZ world position to a
     * 2D position representing as screen coordinates.
     * Projected positions that are outside of the
     * viewing frustum can be calculated by checking
     * the returned Z value.
     *
     * @return Screen projected coordinates
     */
    public static Vec3f toScreen(double x, double y, double z) {
        boolean result = GLU.gluProject((float) x, (float) y, (float) z, MODELVIEW, PROJECTION, VIEWPORT, (FloatBuffer) TO_SCREEN_BUFFER.clear());
        if (result) {
            return new Vec3f(TO_SCREEN_BUFFER.get(0), Display.getHeight() - TO_SCREEN_BUFFER.get(1), TO_SCREEN_BUFFER.get(2));
        }
        return null;
    }

    /**
     * Converts a Vec3f to its world projection
     *
     * @see GLUtils#toWorld(double, double, double)
     *
     * @return World projected coordinates
     */
    public static Vec3f toWorld(Vec3f pos) {
        return GLUtils.toWorld(pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * Projects the specified XYZ screen position to
     * a 3D position representing a world position. Can
     * be used to calculate what object the mouse over in
     * the 3D game world using raytracing.
     *
     * @return World projected coordinates
     */
    public static Vec3f toWorld(double x, double y, double z) {
        boolean result = GLU.gluUnProject((float) x, (float) y, (float) z, MODELVIEW, PROJECTION, VIEWPORT, (FloatBuffer) TO_WORLD_BUFFER.clear());
        if (result) {
            return new Vec3f(TO_WORLD_BUFFER.get(0), TO_WORLD_BUFFER.get(1), TO_WORLD_BUFFER.get(2));
        }
        return null;
    }

    /**
     * Gets the Model View Matrix as a FloatBuffer
     *
     * @return The Model View Matrix
     */
    public static FloatBuffer getModelview() {
        return MODELVIEW;
    }

    /**
     * Gets the Projection Matrix as a FloatBuffer
     *
     * @return The Projection Matrix
     */
    public static FloatBuffer getProjection() {
        return PROJECTION;
    }

    /**
     * Gets the Render Viewport as an IntegerBuffer
     *
     * @return The Viewport
     */
    public static IntBuffer getViewport() {
        return VIEWPORT;
    }
}