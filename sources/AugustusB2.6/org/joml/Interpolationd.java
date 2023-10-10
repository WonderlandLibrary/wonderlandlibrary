// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

public class Interpolationd
{
    public static double interpolateTriangle(final double v0X, final double v0Y, final double f0, final double v1X, final double v1Y, final double f1, final double v2X, final double v2Y, final double f2, final double x, final double y) {
        final double v12Y = v1Y - v2Y;
        final double v21X = v2X - v1X;
        final double v02X = v0X - v2X;
        final double yv2Y = y - v2Y;
        final double xv2X = x - v2X;
        final double v02Y = v0Y - v2Y;
        final double invDen = 1.0 / (v12Y * v02X + v21X * v02Y);
        final double l1 = (v12Y * xv2X + v21X * yv2Y) * invDen;
        final double l2 = (v02X * yv2Y - v02Y * xv2X) * invDen;
        return l1 * f0 + l2 * f1 + (1.0 - l1 - l2) * f2;
    }
    
    public static Vector2d interpolateTriangle(final double v0X, final double v0Y, final double f0X, final double f0Y, final double v1X, final double v1Y, final double f1X, final double f1Y, final double v2X, final double v2Y, final double f2X, final double f2Y, final double x, final double y, final Vector2d dest) {
        final double v12Y = v1Y - v2Y;
        final double v21X = v2X - v1X;
        final double v02X = v0X - v2X;
        final double yv2Y = y - v2Y;
        final double xv2X = x - v2X;
        final double v02Y = v0Y - v2Y;
        final double invDen = 1.0 / (v12Y * v02X + v21X * v02Y);
        final double l1 = (v12Y * xv2X + v21X * yv2Y) * invDen;
        final double l2 = (v02X * yv2Y - v02Y * xv2X) * invDen;
        final double l3 = 1.0 - l1 - l2;
        dest.x = l1 * f0X + l2 * f1X + l3 * f2X;
        dest.y = l1 * f0Y + l2 * f1Y + l3 * f2Y;
        return dest;
    }
    
    public static Vector2d dFdxLinear(final double v0X, final double v0Y, final double f0X, final double f0Y, final double v1X, final double v1Y, final double f1X, final double f1Y, final double v2X, final double v2Y, final double f2X, final double f2Y, final Vector2d dest) {
        final double v12Y = v1Y - v2Y;
        final double v02Y = v0Y - v2Y;
        final double den = v12Y * (v0X - v2X) + (v2X - v1X) * v02Y;
        final double l3_1 = den - v12Y + v02Y;
        final double invDen = 1.0 / den;
        dest.x = invDen * (v12Y * f0X - v02Y * f1X + l3_1 * f2X) - f2X;
        dest.y = invDen * (v12Y * f0Y - v02Y * f1Y + l3_1 * f2Y) - f2Y;
        return dest;
    }
    
    public static Vector2d dFdyLinear(final double v0X, final double v0Y, final double f0X, final double f0Y, final double v1X, final double v1Y, final double f1X, final double f1Y, final double v2X, final double v2Y, final double f2X, final double f2Y, final Vector2d dest) {
        final double v21X = v2X - v1X;
        final double v02X = v0X - v2X;
        final double den = (v1Y - v2Y) * v02X + v21X * (v0Y - v2Y);
        final double l3_1 = den - v21X - v02X;
        final double invDen = 1.0 / den;
        dest.x = invDen * (v21X * f0X + v02X * f1X + l3_1 * f2X) - f2X;
        dest.y = invDen * (v21X * f0Y + v02X * f1Y + l3_1 * f2Y) - f2Y;
        return dest;
    }
    
    public static Vector3d interpolateTriangle(final double v0X, final double v0Y, final double f0X, final double f0Y, final double f0Z, final double v1X, final double v1Y, final double f1X, final double f1Y, final double f1Z, final double v2X, final double v2Y, final double f2X, final double f2Y, final double f2Z, final double x, final double y, final Vector3d dest) {
        final Vector3d t = dest;
        interpolationFactorsTriangle(v0X, v0Y, v1X, v1Y, v2X, v2Y, x, y, t);
        return dest.set(t.x * f0X + t.y * f1X + t.z * f2X, t.x * f0Y + t.y * f1Y + t.z * f2Y, t.x * f0Z + t.y * f1Z + t.z * f2Z);
    }
    
    public static Vector3d interpolationFactorsTriangle(final double v0X, final double v0Y, final double v1X, final double v1Y, final double v2X, final double v2Y, final double x, final double y, final Vector3d dest) {
        final double v12Y = v1Y - v2Y;
        final double v21X = v2X - v1X;
        final double v02X = v0X - v2X;
        final double yv2Y = y - v2Y;
        final double xv2X = x - v2X;
        final double v02Y = v0Y - v2Y;
        final double invDen = 1.0 / (v12Y * v02X + v21X * v02Y);
        dest.x = (v12Y * xv2X + v21X * yv2Y) * invDen;
        dest.y = (v02X * yv2Y - v02Y * xv2X) * invDen;
        dest.z = 1.0 - dest.x - dest.y;
        return dest;
    }
}
