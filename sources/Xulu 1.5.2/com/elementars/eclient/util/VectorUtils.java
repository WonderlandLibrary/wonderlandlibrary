// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ActiveRenderInfo;
import org.lwjgl.util.vector.Vector4f;
import org.lwjgl.util.vector.Matrix4f;

public class VectorUtils implements Helper
{
    static Matrix4f modelMatrix;
    static Matrix4f projectionMatrix;
    
    private static void VecTransformCoordinate(final Vector4f vec, final Matrix4f matrix) {
        final float x = vec.x;
        final float y = vec.y;
        final float z = vec.z;
        vec.x = x * matrix.m00 + y * matrix.m10 + z * matrix.m20 + matrix.m30;
        vec.y = x * matrix.m01 + y * matrix.m11 + z * matrix.m21 + matrix.m31;
        vec.z = x * matrix.m02 + y * matrix.m12 + z * matrix.m22 + matrix.m32;
        vec.w = x * matrix.m03 + y * matrix.m13 + z * matrix.m23 + matrix.m33;
    }
    
    public static Plane toScreen(final double x, final double y, final double z) {
        final Entity view = VectorUtils.mc.getRenderViewEntity();
        if (view == null) {
            return new Plane(0.0, 0.0, false);
        }
        final Vec3d camPos = ActiveRenderInfo.position;
        final Vec3d eyePos = ActiveRenderInfo.projectViewFromEntity(view, (double)VectorUtils.mc.getRenderPartialTicks());
        final float vecX = (float)(camPos.x + eyePos.x - (float)x);
        final float vecY = (float)(camPos.y + eyePos.y - (float)y);
        final float vecZ = (float)(camPos.z + eyePos.z - (float)z);
        final Vector4f pos = new Vector4f(vecX, vecY, vecZ, 1.0f);
        VectorUtils.modelMatrix.load(ActiveRenderInfo.MODELVIEW.asReadOnlyBuffer());
        VectorUtils.projectionMatrix.load(ActiveRenderInfo.PROJECTION.asReadOnlyBuffer());
        VecTransformCoordinate(pos, VectorUtils.modelMatrix);
        VecTransformCoordinate(pos, VectorUtils.projectionMatrix);
        if (pos.w > 0.0f) {
            final Vector4f vector4f = pos;
            vector4f.x *= -100000.0f;
            final Vector4f vector4f2 = pos;
            vector4f2.y *= -100000.0f;
        }
        else {
            final float invert = 1.0f / pos.w;
            final Vector4f vector4f3 = pos;
            vector4f3.x *= invert;
            final Vector4f vector4f4 = pos;
            vector4f4.y *= invert;
        }
        final ScaledResolution res = new ScaledResolution(VectorUtils.mc);
        final float halfWidth = res.getScaledWidth() / 2.0f;
        final float halfHeight = res.getScaledHeight() / 2.0f;
        pos.x = halfWidth + (0.5f * pos.x * res.getScaledWidth() + 0.5f);
        pos.y = halfHeight - (0.5f * pos.y * res.getScaledHeight() + 0.5f);
        boolean bVisible = true;
        if (pos.x < 0.0f || pos.y < 0.0f || pos.x > res.getScaledWidth() || pos.y > res.getScaledHeight()) {
            bVisible = false;
        }
        return new Plane(pos.x, pos.y, bVisible);
    }
    
    public static Plane toScreen(final Vec3d vec) {
        return toScreen(vec.x, vec.y, vec.z);
    }
    
    @Deprecated
    public static ScreenPos _toScreen(final double x, final double y, final double z) {
        final Plane plane = toScreen(x, y, z);
        return new ScreenPos(plane.getX(), plane.getY(), plane.isVisible());
    }
    
    @Deprecated
    public static ScreenPos _toScreen(final Vec3d vec3d) {
        return _toScreen(vec3d.x, vec3d.y, vec3d.z);
    }
    
    @Deprecated
    public static Object vectorAngle(final Vec3d vec3d) {
        return null;
    }
    
    public static Vec3d multiplyBy(final Vec3d vec1, final Vec3d vec2) {
        return new Vec3d(vec1.x * vec2.x, vec1.y * vec2.y, vec1.z * vec2.z);
    }
    
    public static Vec3d copy(final Vec3d toCopy) {
        return new Vec3d(toCopy.x, toCopy.y, toCopy.z);
    }
    
    public static double getCrosshairDistance(final Vec3d eyes, final Vec3d directionVec, final Vec3d pos) {
        return pos.subtract(eyes).normalize().subtract(directionVec).lengthSquared();
    }
    
    static {
        VectorUtils.modelMatrix = new Matrix4f();
        VectorUtils.projectionMatrix = new Matrix4f();
    }
    
    @Deprecated
    public static class ScreenPos
    {
        public final int x;
        public final int y;
        public final boolean isVisible;
        public final double xD;
        public final double yD;
        
        public ScreenPos(final double x, final double y, final boolean isVisible) {
            this.x = (int)x;
            this.y = (int)y;
            this.xD = x;
            this.yD = y;
            this.isVisible = isVisible;
        }
    }
}
