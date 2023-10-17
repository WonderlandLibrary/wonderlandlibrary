package de.tired.util.render;


import de.tired.base.interfaces.IHook;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import javax.vecmath.Vector3f;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;

public class ESPUtil implements IHook {
    private static final Frustum frustum = new Frustum();
    private static final FloatBuffer windPos = BufferUtils.createFloatBuffer(4);
    private static final IntBuffer intBuffer = GLAllocation.createDirectIntBuffer(16);
    private static final FloatBuffer floatBuffer1 = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer floatBuffer2 = GLAllocation.createDirectFloatBuffer(16);

    public static boolean isInView(Entity ent) {
        frustum.setPosition(MC.getRenderViewEntity().posX, MC.getRenderViewEntity().posY, MC.getRenderViewEntity().posZ);
        return frustum.isBoundingBoxInFrustum(ent.getEntityBoundingBox()) || ent.ignoreFrustumCheck;
    }

    public static Vector3f executeGLUPorjection(float x, float y, float z, int scaleFactor) {
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, floatBuffer1);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, floatBuffer2);
        GL11.glGetInteger(GL11.GL_VIEWPORT, intBuffer);
        if (GLU.gluProject(x, y, z, floatBuffer1, floatBuffer2, intBuffer, windPos)) {
            return new Vector3f(windPos.get(0) / scaleFactor, (MC.displayHeight - windPos.get(1)) / scaleFactor, windPos.get(2));
        }
        return null;
    }

    public static Double interpolate(double oldValue, double newValue, double interpolationValue){
        return (oldValue + (newValue - oldValue) * interpolationValue);
    }
    public static double[] getInterpolatedPos(Entity entity) {
        float ticks = MC.timer.renderPartialTicks;
        return new double[]{
                interpolate(entity.lastTickPosX, entity.posX, ticks) - MC.getRenderManager().viewerPosX,
                interpolate(entity.lastTickPosY, entity.posY, ticks) - MC.getRenderManager().viewerPosY,
                interpolate(entity.lastTickPosZ, entity.posZ, ticks) - MC.getRenderManager().viewerPosZ
        };
    }

    public static javax.vecmath.Vector4f getEntityPositionsOn2D(Entity entity) {
        final double[] interpolatedPos = getInterpolatedPos(entity);
        final double entityRenderWidth = entity.width / 1.5;
        final AxisAlignedBB bb = new AxisAlignedBB(interpolatedPos[0] - entityRenderWidth, interpolatedPos[1], interpolatedPos[2] - entityRenderWidth, interpolatedPos[0] + entityRenderWidth, interpolatedPos[1] + entity.height + (entity.isSneaking() ? -0.3 : 0.18), interpolatedPos[2] + entityRenderWidth).expand(0.15, 0.15, 0.15);

        final List<Vector3f> vector3fs = Arrays.asList(new Vector3f((float) bb.minX, (float) bb.minY, (float) bb.minZ), new Vector3f((float) bb.minX, (float) bb.maxY, (float) bb.minZ), new Vector3f((float) bb.maxX, (float) bb.minY, (float) bb.minZ), new Vector3f((float) bb.maxX, (float) bb.maxY, (float) bb.minZ), new Vector3f((float) bb.minX, (float) bb.minY, (float) bb.maxZ), new Vector3f((float) bb.minX, (float) bb.maxY, (float) bb.maxZ), new Vector3f((float) bb.maxX, (float) bb.minY, (float) bb.maxZ), new Vector3f((float) bb.maxX, (float) bb.maxY, (float) bb.maxZ));

        javax.vecmath.Vector4f pos = new javax.vecmath.Vector4f(Float.MAX_VALUE, Float.MAX_VALUE, -1.0f, -1.0f);

        for (Vector3f vector3f : vector3fs) {
            final ScaledResolution scaledResolution = new ScaledResolution(MC);
            vector3f = executeGLUPorjection(vector3f.x, vector3f.y, vector3f.z, scaledResolution.getScaleFactor());
            if (vector3f != null && vector3f.z >= 0.0 && vector3f.z < 1.0) {
                pos.x = Math.min(vector3f.x, pos.x);
                pos.y = Math.min(vector3f.y, pos.y);
                pos.z = Math.max(vector3f.x, pos.z);
                pos.w = Math.max(vector3f.y, pos.w);
            }
        }
        return pos;
    }
}