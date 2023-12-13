// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;

public class AngleHelper
{
    public static final long DEFAULT_N = 1000000000L;
    public static final double DEFAULT_EPSILON = 1.0E-9;
    public static final double TWO_PI = 6.283185307179586;
    public static final double HALF_PI = 1.5707963267948966;
    public static final double QUARTER_PI = 0.7853981633974483;
    
    public static double roundAngle(final double a, final long n) {
        return Math.round(a * n) / (double)n;
    }
    
    public static double roundAngle(final double a) {
        return roundAngle(a, 1000000000L);
    }
    
    public static boolean isAngleEqual(final double a1, final double a2, final double epsilon) {
        return Double.compare(a1, a2) == 0 || Math.abs(a1 - a2) < epsilon;
    }
    
    public static boolean isAngleEqual(final double a1, final double a2) {
        return isAngleEqual(a1, a2, 1.0E-4);
    }
    
    public static boolean isEqual(final Angle ang1, final Angle ang2) {
        final Angle a1 = ang1.normalize();
        final Angle a2 = ang2.same(a1).normalize();
        return isAngleEqual(a1.getPitch(), a2.getPitch()) && isAngleEqual(a1.getYaw(), a2.getYaw()) && isAngleEqual(a1.getRoll(), a2.getRoll());
    }
    
    public static double normalizeInRadians(double ang) {
        while (ang > 3.141592653589793) {
            ang -= 6.283185307179586;
        }
        while (ang < -3.141592653589793) {
            ang += 6.283185307179586;
        }
        return ang;
    }
    
    public static float normalizeInRadians(float ang) {
        while (ang > 3.141592653589793) {
            ang -= (float)6.283185307179586;
        }
        while (ang < -3.141592653589793) {
            ang += (float)6.283185307179586;
        }
        return ang;
    }
    
    public static double normalizeInDegrees(final double ang) {
        return MathHelper.wrapDegrees(ang);
    }
    
    public static float normalizeInDegrees(final float ang) {
        return MathHelper.wrapDegrees(ang);
    }
    
    public static Angle getAngleFacingInRadians(final Vec3d vector) {
        double yaw;
        double pitch;
        if (vector.x == 0.0 && vector.z == 0.0) {
            yaw = 0.0;
            pitch = 1.5707963267948966;
        }
        else {
            yaw = Math.atan2(vector.z, vector.x) - 1.5707963267948966;
            final double mag = Math.sqrt(vector.x * vector.x + vector.z * vector.z);
            pitch = -Math.atan2(vector.y, mag);
        }
        return Angle.radians((float)pitch, (float)yaw);
    }
    
    public static Angle getAngleFacingInDegrees(final Vec3d vector) {
        return getAngleFacingInRadians(vector).inDegrees();
    }
}
