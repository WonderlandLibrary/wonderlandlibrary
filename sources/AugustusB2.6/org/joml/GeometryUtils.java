// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

public class GeometryUtils
{
    public static void perpendicular(final float x, final float y, final float z, final Vector3f dest1, final Vector3f dest2) {
        final float magX = z * z + y * y;
        final float magY = z * z + x * x;
        final float magZ = y * y + x * x;
        float mag;
        if (magX > magY && magX > magZ) {
            dest1.x = 0.0f;
            dest1.y = z;
            dest1.z = -y;
            mag = magX;
        }
        else if (magY > magZ) {
            dest1.x = -z;
            dest1.y = 0.0f;
            dest1.z = x;
            mag = magY;
        }
        else {
            dest1.x = y;
            dest1.y = -x;
            dest1.z = 0.0f;
            mag = magZ;
        }
        final float len = Math.invsqrt(mag);
        dest1.x *= len;
        dest1.y *= len;
        dest1.z *= len;
        dest2.x = y * dest1.z - z * dest1.y;
        dest2.y = z * dest1.x - x * dest1.z;
        dest2.z = x * dest1.y - y * dest1.x;
    }
    
    public static void perpendicular(final Vector3fc v, final Vector3f dest1, final Vector3f dest2) {
        perpendicular(v.x(), v.y(), v.z(), dest1, dest2);
    }
    
    public static void normal(final Vector3fc v0, final Vector3fc v1, final Vector3fc v2, final Vector3f dest) {
        normal(v0.x(), v0.y(), v0.z(), v1.x(), v1.y(), v1.z(), v2.x(), v2.y(), v2.z(), dest);
    }
    
    public static void normal(final float v0X, final float v0Y, final float v0Z, final float v1X, final float v1Y, final float v1Z, final float v2X, final float v2Y, final float v2Z, final Vector3f dest) {
        dest.x = (v1Y - v0Y) * (v2Z - v0Z) - (v1Z - v0Z) * (v2Y - v0Y);
        dest.y = (v1Z - v0Z) * (v2X - v0X) - (v1X - v0X) * (v2Z - v0Z);
        dest.z = (v1X - v0X) * (v2Y - v0Y) - (v1Y - v0Y) * (v2X - v0X);
        dest.normalize();
    }
    
    public static void tangent(final Vector3fc v1, final Vector2fc uv1, final Vector3fc v2, final Vector2fc uv2, final Vector3fc v3, final Vector2fc uv3, final Vector3f dest) {
        final float DeltaV1 = uv2.y() - uv1.y();
        final float DeltaV2 = uv3.y() - uv1.y();
        final float f = 1.0f / ((uv2.x() - uv1.x()) * DeltaV2 - (uv3.x() - uv1.x()) * DeltaV1);
        dest.x = f * (DeltaV2 * (v2.x() - v1.x()) - DeltaV1 * (v3.x() - v1.x()));
        dest.y = f * (DeltaV2 * (v2.y() - v1.y()) - DeltaV1 * (v3.y() - v1.y()));
        dest.z = f * (DeltaV2 * (v2.z() - v1.z()) - DeltaV1 * (v3.z() - v1.z()));
        dest.normalize();
    }
    
    public static void bitangent(final Vector3fc v1, final Vector2fc uv1, final Vector3fc v2, final Vector2fc uv2, final Vector3fc v3, final Vector2fc uv3, final Vector3f dest) {
        final float DeltaU1 = uv2.x() - uv1.x();
        final float DeltaU2 = uv3.x() - uv1.x();
        final float f = 1.0f / (DeltaU1 * (uv3.y() - uv1.y()) - DeltaU2 * (uv2.y() - uv1.y()));
        dest.x = f * (-DeltaU2 * (v2.x() - v1.x()) + DeltaU1 * (v3.x() - v1.x()));
        dest.y = f * (-DeltaU2 * (v2.y() - v1.y()) + DeltaU1 * (v3.y() - v1.y()));
        dest.z = f * (-DeltaU2 * (v2.z() - v1.z()) + DeltaU1 * (v3.z() - v1.z()));
        dest.normalize();
    }
    
    public static void tangentBitangent(final Vector3fc v1, final Vector2fc uv1, final Vector3fc v2, final Vector2fc uv2, final Vector3fc v3, final Vector2fc uv3, final Vector3f destTangent, final Vector3f destBitangent) {
        final float DeltaV1 = uv2.y() - uv1.y();
        final float DeltaV2 = uv3.y() - uv1.y();
        final float DeltaU1 = uv2.x() - uv1.x();
        final float DeltaU2 = uv3.x() - uv1.x();
        final float f = 1.0f / (DeltaU1 * DeltaV2 - DeltaU2 * DeltaV1);
        destTangent.x = f * (DeltaV2 * (v2.x() - v1.x()) - DeltaV1 * (v3.x() - v1.x()));
        destTangent.y = f * (DeltaV2 * (v2.y() - v1.y()) - DeltaV1 * (v3.y() - v1.y()));
        destTangent.z = f * (DeltaV2 * (v2.z() - v1.z()) - DeltaV1 * (v3.z() - v1.z()));
        destTangent.normalize();
        destBitangent.x = f * (-DeltaU2 * (v2.x() - v1.x()) + DeltaU1 * (v3.x() - v1.x()));
        destBitangent.y = f * (-DeltaU2 * (v2.y() - v1.y()) + DeltaU1 * (v3.y() - v1.y()));
        destBitangent.z = f * (-DeltaU2 * (v2.z() - v1.z()) + DeltaU1 * (v3.z() - v1.z()));
        destBitangent.normalize();
    }
}
