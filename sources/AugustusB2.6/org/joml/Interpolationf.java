// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

public class Interpolationf
{
    public static float interpolateTriangle(final float v0X, final float v0Y, final float f0, final float v1X, final float v1Y, final float f1, final float v2X, final float v2Y, final float f2, final float x, final float y) {
        final float v12Y = v1Y - v2Y;
        final float v21X = v2X - v1X;
        final float v02X = v0X - v2X;
        final float yv2Y = y - v2Y;
        final float xv2X = x - v2X;
        final float v02Y = v0Y - v2Y;
        final float invDen = 1.0f / (v12Y * v02X + v21X * v02Y);
        final float l1 = (v12Y * xv2X + v21X * yv2Y) * invDen;
        final float l2 = (v02X * yv2Y - v02Y * xv2X) * invDen;
        return l1 * f0 + l2 * f1 + (1.0f - l1 - l2) * f2;
    }
    
    public static Vector2f interpolateTriangle(final float v0X, final float v0Y, final float f0X, final float f0Y, final float v1X, final float v1Y, final float f1X, final float f1Y, final float v2X, final float v2Y, final float f2X, final float f2Y, final float x, final float y, final Vector2f dest) {
        final float v12Y = v1Y - v2Y;
        final float v21X = v2X - v1X;
        final float v02X = v0X - v2X;
        final float yv2Y = y - v2Y;
        final float xv2X = x - v2X;
        final float v02Y = v0Y - v2Y;
        final float invDen = 1.0f / (v12Y * v02X + v21X * v02Y);
        final float l1 = (v12Y * xv2X + v21X * yv2Y) * invDen;
        final float l2 = (v02X * yv2Y - v02Y * xv2X) * invDen;
        final float l3 = 1.0f - l1 - l2;
        dest.x = l1 * f0X + l2 * f1X + l3 * f2X;
        dest.y = l1 * f0Y + l2 * f1Y + l3 * f2Y;
        return dest;
    }
    
    public static Vector2f dFdxLinear(final float v0X, final float v0Y, final float f0X, final float f0Y, final float v1X, final float v1Y, final float f1X, final float f1Y, final float v2X, final float v2Y, final float f2X, final float f2Y, final Vector2f dest) {
        final float v12Y = v1Y - v2Y;
        final float v02Y = v0Y - v2Y;
        final float den = v12Y * (v0X - v2X) + (v2X - v1X) * v02Y;
        final float l3_1 = den - v12Y + v02Y;
        final float invDen = 1.0f / den;
        dest.x = invDen * (v12Y * f0X - v02Y * f1X + l3_1 * f2X) - f2X;
        dest.y = invDen * (v12Y * f0Y - v02Y * f1Y + l3_1 * f2Y) - f2Y;
        return dest;
    }
    
    public static Vector2f dFdyLinear(final float v0X, final float v0Y, final float f0X, final float f0Y, final float v1X, final float v1Y, final float f1X, final float f1Y, final float v2X, final float v2Y, final float f2X, final float f2Y, final Vector2f dest) {
        final float v21X = v2X - v1X;
        final float v02X = v0X - v2X;
        final float den = (v1Y - v2Y) * v02X + v21X * (v0Y - v2Y);
        final float l3_1 = den - v21X - v02X;
        final float invDen = 1.0f / den;
        dest.x = invDen * (v21X * f0X + v02X * f1X + l3_1 * f2X) - f2X;
        dest.y = invDen * (v21X * f0Y + v02X * f1Y + l3_1 * f2Y) - f2Y;
        return dest;
    }
    
    public static Vector3f interpolateTriangle(final float v0X, final float v0Y, final float f0X, final float f0Y, final float f0Z, final float v1X, final float v1Y, final float f1X, final float f1Y, final float f1Z, final float v2X, final float v2Y, final float f2X, final float f2Y, final float f2Z, final float x, final float y, final Vector3f dest) {
        final Vector3f t = dest;
        interpolationFactorsTriangle(v0X, v0Y, v1X, v1Y, v2X, v2Y, x, y, t);
        return dest.set(t.x * f0X + t.y * f1X + t.z * f2X, t.x * f0Y + t.y * f1Y + t.z * f2Y, t.x * f0Z + t.y * f1Z + t.z * f2Z);
    }
    
    public static Vector3f interpolationFactorsTriangle(final float v0X, final float v0Y, final float v1X, final float v1Y, final float v2X, final float v2Y, final float x, final float y, final Vector3f dest) {
        final float v12Y = v1Y - v2Y;
        final float v21X = v2X - v1X;
        final float v02X = v0X - v2X;
        final float yv2Y = y - v2Y;
        final float xv2X = x - v2X;
        final float v02Y = v0Y - v2Y;
        final float invDen = 1.0f / (v12Y * v02X + v21X * v02Y);
        dest.x = (v12Y * xv2X + v21X * yv2Y) * invDen;
        dest.y = (v02X * yv2Y - v02Y * xv2X) * invDen;
        dest.z = 1.0f - dest.x - dest.y;
        return dest;
    }
}
