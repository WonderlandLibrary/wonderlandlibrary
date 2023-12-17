// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.movement;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.util.math.Vec3d;
import ru.tuskevich.util.Utility;

public class RenderUtilitys implements Utility
{
    public static Vec3d vectorTo2D(final int scaleFactor, final double x, final double y, final double z) {
        final float xPos = (float)x;
        final float yPos = (float)y;
        final float zPos = (float)z;
        final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
        final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
        final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
        final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
        GL11.glGetFloat(2982, modelview);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        if (GLU.gluProject(xPos, yPos, zPos, modelview, projection, viewport, vector)) {
            return new Vec3d(vector.get(0) / scaleFactor, (Display.getHeight() - vector.get(1)) / scaleFactor, vector.get(2));
        }
        return null;
    }
}
