// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

public class Intersectionf
{
    public static final int POINT_ON_TRIANGLE_VERTEX_0 = 1;
    public static final int POINT_ON_TRIANGLE_VERTEX_1 = 2;
    public static final int POINT_ON_TRIANGLE_VERTEX_2 = 3;
    public static final int POINT_ON_TRIANGLE_EDGE_01 = 4;
    public static final int POINT_ON_TRIANGLE_EDGE_12 = 5;
    public static final int POINT_ON_TRIANGLE_EDGE_20 = 6;
    public static final int POINT_ON_TRIANGLE_FACE = 7;
    public static final int AAR_SIDE_MINX = 0;
    public static final int AAR_SIDE_MINY = 1;
    public static final int AAR_SIDE_MAXX = 2;
    public static final int AAR_SIDE_MAXY = 3;
    public static final int OUTSIDE = -1;
    public static final int ONE_INTERSECTION = 1;
    public static final int TWO_INTERSECTION = 2;
    public static final int INSIDE = 3;
    
    public static boolean testPlaneSphere(final float a, final float b, final float c, final float d, final float centerX, final float centerY, final float centerZ, final float radius) {
        final float denom = Math.sqrt(a * a + b * b + c * c);
        final float dist = (a * centerX + b * centerY + c * centerZ + d) / denom;
        return -radius <= dist && dist <= radius;
    }
    
    public static boolean intersectPlaneSphere(final float a, final float b, final float c, final float d, final float centerX, final float centerY, final float centerZ, final float radius, final Vector4f intersectionCenterAndRadius) {
        final float invDenom = Math.invsqrt(a * a + b * b + c * c);
        final float dist = (a * centerX + b * centerY + c * centerZ + d) * invDenom;
        if (-radius <= dist && dist <= radius) {
            intersectionCenterAndRadius.x = centerX + dist * a * invDenom;
            intersectionCenterAndRadius.y = centerY + dist * b * invDenom;
            intersectionCenterAndRadius.z = centerZ + dist * c * invDenom;
            intersectionCenterAndRadius.w = Math.sqrt(radius * radius - dist * dist);
            return true;
        }
        return false;
    }
    
    public static boolean intersectPlaneSweptSphere(final float a, final float b, final float c, final float d, final float cX, final float cY, final float cZ, final float radius, final float vX, final float vY, final float vZ, final Vector4f pointAndTime) {
        final float dist = a * cX + b * cY + c * cZ - d;
        if (Math.abs(dist) <= radius) {
            pointAndTime.set(cX, cY, cZ, 0.0f);
            return true;
        }
        final float denom = a * vX + b * vY + c * vZ;
        if (denom * dist >= 0.0f) {
            return false;
        }
        final float r = (dist > 0.0f) ? radius : (-radius);
        final float t = (r - dist) / denom;
        pointAndTime.set(cX + t * vX - r * a, cY + t * vY - r * b, cZ + t * vZ - r * c, t);
        return true;
    }
    
    public static boolean testPlaneSweptSphere(final float a, final float b, final float c, final float d, final float t0X, final float t0Y, final float t0Z, final float r, final float t1X, final float t1Y, final float t1Z) {
        final float adist = t0X * a + t0Y * b + t0Z * c - d;
        final float bdist = t1X * a + t1Y * b + t1Z * c - d;
        return adist * bdist < 0.0f || (Math.abs(adist) <= r || Math.abs(bdist) <= r);
    }
    
    public static boolean testAabPlane(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ, final float a, final float b, final float c, final float d) {
        float pX;
        float nX;
        if (a > 0.0f) {
            pX = maxX;
            nX = minX;
        }
        else {
            pX = minX;
            nX = maxX;
        }
        float pY;
        float nY;
        if (b > 0.0f) {
            pY = maxY;
            nY = minY;
        }
        else {
            pY = minY;
            nY = maxY;
        }
        float pZ;
        float nZ;
        if (c > 0.0f) {
            pZ = maxZ;
            nZ = minZ;
        }
        else {
            pZ = minZ;
            nZ = maxZ;
        }
        final float distN = d + a * nX + b * nY + c * nZ;
        final float distP = d + a * pX + b * pY + c * pZ;
        return distN <= 0.0f && distP >= 0.0f;
    }
    
    public static boolean testAabPlane(final Vector3fc min, final Vector3fc max, final float a, final float b, final float c, final float d) {
        return testAabPlane(min.x(), min.y(), min.z(), max.x(), max.y(), max.z(), a, b, c, d);
    }
    
    public static boolean testAabAab(final float minXA, final float minYA, final float minZA, final float maxXA, final float maxYA, final float maxZA, final float minXB, final float minYB, final float minZB, final float maxXB, final float maxYB, final float maxZB) {
        return maxXA >= minXB && maxYA >= minYB && maxZA >= minZB && minXA <= maxXB && minYA <= maxYB && minZA <= maxZB;
    }
    
    public static boolean testAabAab(final Vector3fc minA, final Vector3fc maxA, final Vector3fc minB, final Vector3fc maxB) {
        return testAabAab(minA.x(), minA.y(), minA.z(), maxA.x(), maxA.y(), maxA.z(), minB.x(), minB.y(), minB.z(), maxB.x(), maxB.y(), maxB.z());
    }
    
    public static boolean testObOb(final Vector3f b0c, final Vector3f b0uX, final Vector3f b0uY, final Vector3f b0uZ, final Vector3f b0hs, final Vector3f b1c, final Vector3f b1uX, final Vector3f b1uY, final Vector3f b1uZ, final Vector3f b1hs) {
        return testObOb(b0c.x, b0c.y, b0c.z, b0uX.x, b0uX.y, b0uX.z, b0uY.x, b0uY.y, b0uY.z, b0uZ.x, b0uZ.y, b0uZ.z, b0hs.x, b0hs.y, b0hs.z, b1c.x, b1c.y, b1c.z, b1uX.x, b1uX.y, b1uX.z, b1uY.x, b1uY.y, b1uY.z, b1uZ.x, b1uZ.y, b1uZ.z, b1hs.x, b1hs.y, b1hs.z);
    }
    
    public static boolean testObOb(final float b0cX, final float b0cY, final float b0cZ, final float b0uXx, final float b0uXy, final float b0uXz, final float b0uYx, final float b0uYy, final float b0uYz, final float b0uZx, final float b0uZy, final float b0uZz, final float b0hsX, final float b0hsY, final float b0hsZ, final float b1cX, final float b1cY, final float b1cZ, final float b1uXx, final float b1uXy, final float b1uXz, final float b1uYx, final float b1uYy, final float b1uYz, final float b1uZx, final float b1uZy, final float b1uZz, final float b1hsX, final float b1hsY, final float b1hsZ) {
        final float rm00 = b0uXx * b1uXx + b0uYx * b1uYx + b0uZx * b1uZx;
        final float rm2 = b0uXx * b1uXy + b0uYx * b1uYy + b0uZx * b1uZy;
        final float rm3 = b0uXx * b1uXz + b0uYx * b1uYz + b0uZx * b1uZz;
        final float rm4 = b0uXy * b1uXx + b0uYy * b1uYx + b0uZy * b1uZx;
        final float rm5 = b0uXy * b1uXy + b0uYy * b1uYy + b0uZy * b1uZy;
        final float rm6 = b0uXy * b1uXz + b0uYy * b1uYz + b0uZy * b1uZz;
        final float rm7 = b0uXz * b1uXx + b0uYz * b1uYx + b0uZz * b1uZx;
        final float rm8 = b0uXz * b1uXy + b0uYz * b1uYy + b0uZz * b1uZy;
        final float rm9 = b0uXz * b1uXz + b0uYz * b1uYz + b0uZz * b1uZz;
        final float EPSILON = 1.0E-5f;
        final float arm00 = Math.abs(rm00) + EPSILON;
        final float arm2 = Math.abs(rm4) + EPSILON;
        final float arm3 = Math.abs(rm7) + EPSILON;
        final float arm4 = Math.abs(rm2) + EPSILON;
        final float arm5 = Math.abs(rm5) + EPSILON;
        final float arm6 = Math.abs(rm8) + EPSILON;
        final float arm7 = Math.abs(rm3) + EPSILON;
        final float arm8 = Math.abs(rm6) + EPSILON;
        final float arm9 = Math.abs(rm9) + EPSILON;
        final float tx = b1cX - b0cX;
        final float ty = b1cY - b0cY;
        final float tz = b1cZ - b0cZ;
        final float tax = tx * b0uXx + ty * b0uXy + tz * b0uXz;
        final float tay = tx * b0uYx + ty * b0uYy + tz * b0uYz;
        final float taz = tx * b0uZx + ty * b0uZy + tz * b0uZz;
        float ra = b0hsX;
        float rb = b1hsX * arm00 + b1hsY * arm2 + b1hsZ * arm3;
        if (Math.abs(tax) > ra + rb) {
            return false;
        }
        ra = b0hsY;
        rb = b1hsX * arm4 + b1hsY * arm5 + b1hsZ * arm6;
        if (Math.abs(tay) > ra + rb) {
            return false;
        }
        ra = b0hsZ;
        rb = b1hsX * arm7 + b1hsY * arm8 + b1hsZ * arm9;
        if (Math.abs(taz) > ra + rb) {
            return false;
        }
        ra = b0hsX * arm00 + b0hsY * arm4 + b0hsZ * arm7;
        rb = b1hsX;
        if (Math.abs(tax * rm00 + tay * rm2 + taz * rm3) > ra + rb) {
            return false;
        }
        ra = b0hsX * arm2 + b0hsY * arm5 + b0hsZ * arm8;
        rb = b1hsY;
        if (Math.abs(tax * rm4 + tay * rm5 + taz * rm6) > ra + rb) {
            return false;
        }
        ra = b0hsX * arm3 + b0hsY * arm6 + b0hsZ * arm9;
        rb = b1hsZ;
        if (Math.abs(tax * rm7 + tay * rm8 + taz * rm9) > ra + rb) {
            return false;
        }
        ra = b0hsY * arm7 + b0hsZ * arm4;
        rb = b1hsY * arm3 + b1hsZ * arm2;
        if (Math.abs(taz * rm2 - tay * rm3) > ra + rb) {
            return false;
        }
        ra = b0hsY * arm8 + b0hsZ * arm5;
        rb = b1hsX * arm3 + b1hsZ * arm00;
        if (Math.abs(taz * rm5 - tay * rm6) > ra + rb) {
            return false;
        }
        ra = b0hsY * arm9 + b0hsZ * arm6;
        rb = b1hsX * arm2 + b1hsY * arm00;
        if (Math.abs(taz * rm8 - tay * rm9) > ra + rb) {
            return false;
        }
        ra = b0hsX * arm7 + b0hsZ * arm00;
        rb = b1hsY * arm6 + b1hsZ * arm5;
        if (Math.abs(tax * rm3 - taz * rm00) > ra + rb) {
            return false;
        }
        ra = b0hsX * arm8 + b0hsZ * arm2;
        rb = b1hsX * arm6 + b1hsZ * arm4;
        if (Math.abs(tax * rm6 - taz * rm4) > ra + rb) {
            return false;
        }
        ra = b0hsX * arm9 + b0hsZ * arm3;
        rb = b1hsX * arm5 + b1hsY * arm4;
        if (Math.abs(tax * rm9 - taz * rm7) > ra + rb) {
            return false;
        }
        ra = b0hsX * arm4 + b0hsY * arm00;
        rb = b1hsY * arm9 + b1hsZ * arm8;
        if (Math.abs(tay * rm00 - tax * rm2) > ra + rb) {
            return false;
        }
        ra = b0hsX * arm5 + b0hsY * arm2;
        rb = b1hsX * arm9 + b1hsZ * arm7;
        if (Math.abs(tay * rm4 - tax * rm5) > ra + rb) {
            return false;
        }
        ra = b0hsX * arm6 + b0hsY * arm3;
        rb = b1hsX * arm8 + b1hsY * arm7;
        return Math.abs(tay * rm7 - tax * rm8) <= ra + rb;
    }
    
    public static boolean intersectSphereSphere(final float aX, final float aY, final float aZ, final float radiusSquaredA, final float bX, final float bY, final float bZ, final float radiusSquaredB, final Vector4f centerAndRadiusOfIntersectionCircle) {
        final float dX = bX - aX;
        final float dY = bY - aY;
        final float dZ = bZ - aZ;
        final float distSquared = dX * dX + dY * dY + dZ * dZ;
        final float h = 0.5f + (radiusSquaredA - radiusSquaredB) / (2.0f * distSquared);
        final float r_i = radiusSquaredA - h * h * distSquared;
        if (r_i >= 0.0f) {
            centerAndRadiusOfIntersectionCircle.x = aX + h * dX;
            centerAndRadiusOfIntersectionCircle.y = aY + h * dY;
            centerAndRadiusOfIntersectionCircle.z = aZ + h * dZ;
            centerAndRadiusOfIntersectionCircle.w = Math.sqrt(r_i);
            return true;
        }
        return false;
    }
    
    public static boolean intersectSphereSphere(final Vector3fc centerA, final float radiusSquaredA, final Vector3fc centerB, final float radiusSquaredB, final Vector4f centerAndRadiusOfIntersectionCircle) {
        return intersectSphereSphere(centerA.x(), centerA.y(), centerA.z(), radiusSquaredA, centerB.x(), centerB.y(), centerB.z(), radiusSquaredB, centerAndRadiusOfIntersectionCircle);
    }
    
    public static int intersectSphereTriangle(final float sX, final float sY, final float sZ, final float sR, final float v0X, final float v0Y, final float v0Z, final float v1X, final float v1Y, final float v1Z, final float v2X, final float v2Y, final float v2Z, final Vector3f result) {
        final int closest = findClosestPointOnTriangle(v0X, v0Y, v0Z, v1X, v1Y, v1Z, v2X, v2Y, v2Z, sX, sY, sZ, result);
        final float vX = result.x - sX;
        final float vY = result.y - sY;
        final float vZ = result.z - sZ;
        final float dot = vX * vX + vY * vY + vZ * vZ;
        if (dot <= sR * sR) {
            return closest;
        }
        return 0;
    }
    
    public static boolean testSphereSphere(final float aX, final float aY, final float aZ, final float radiusSquaredA, final float bX, final float bY, final float bZ, final float radiusSquaredB) {
        final float dX = bX - aX;
        final float dY = bY - aY;
        final float dZ = bZ - aZ;
        final float distSquared = dX * dX + dY * dY + dZ * dZ;
        final float h = 0.5f + (radiusSquaredA - radiusSquaredB) / (2.0f * distSquared);
        final float r_i = radiusSquaredA - h * h * distSquared;
        return r_i >= 0.0f;
    }
    
    public static boolean testSphereSphere(final Vector3fc centerA, final float radiusSquaredA, final Vector3fc centerB, final float radiusSquaredB) {
        return testSphereSphere(centerA.x(), centerA.y(), centerA.z(), radiusSquaredA, centerB.x(), centerB.y(), centerB.z(), radiusSquaredB);
    }
    
    public static float distancePointPlane(final float pointX, final float pointY, final float pointZ, final float a, final float b, final float c, final float d) {
        final float denom = Math.sqrt(a * a + b * b + c * c);
        return (a * pointX + b * pointY + c * pointZ + d) / denom;
    }
    
    public static float distancePointPlane(final float pointX, final float pointY, final float pointZ, final float v0X, final float v0Y, final float v0Z, final float v1X, final float v1Y, final float v1Z, final float v2X, final float v2Y, final float v2Z) {
        final float v1Y0Y = v1Y - v0Y;
        final float v2Z0Z = v2Z - v0Z;
        final float v2Y0Y = v2Y - v0Y;
        final float v1Z0Z = v1Z - v0Z;
        final float v2X0X = v2X - v0X;
        final float v1X0X = v1X - v0X;
        final float a = v1Y0Y * v2Z0Z - v2Y0Y * v1Z0Z;
        final float b = v1Z0Z * v2X0X - v2Z0Z * v1X0X;
        final float c = v1X0X * v2Y0Y - v2X0X * v1Y0Y;
        final float d = -(a * v0X + b * v0Y + c * v0Z);
        return distancePointPlane(pointX, pointY, pointZ, a, b, c, d);
    }
    
    public static float intersectRayPlane(final float originX, final float originY, final float originZ, final float dirX, final float dirY, final float dirZ, final float pointX, final float pointY, final float pointZ, final float normalX, final float normalY, final float normalZ, final float epsilon) {
        final float denom = normalX * dirX + normalY * dirY + normalZ * dirZ;
        if (denom < epsilon) {
            final float t = ((pointX - originX) * normalX + (pointY - originY) * normalY + (pointZ - originZ) * normalZ) / denom;
            if (t >= 0.0f) {
                return t;
            }
        }
        return -1.0f;
    }
    
    public static float intersectRayPlane(final Vector3fc origin, final Vector3fc dir, final Vector3fc point, final Vector3fc normal, final float epsilon) {
        return intersectRayPlane(origin.x(), origin.y(), origin.z(), dir.x(), dir.y(), dir.z(), point.x(), point.y(), point.z(), normal.x(), normal.y(), normal.z(), epsilon);
    }
    
    public static float intersectRayPlane(final float originX, final float originY, final float originZ, final float dirX, final float dirY, final float dirZ, final float a, final float b, final float c, final float d, final float epsilon) {
        final float denom = a * dirX + b * dirY + c * dirZ;
        if (denom < 0.0f) {
            final float t = -(a * originX + b * originY + c * originZ + d) / denom;
            if (t >= 0.0f) {
                return t;
            }
        }
        return -1.0f;
    }
    
    public static boolean testAabSphere(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ, final float centerX, final float centerY, final float centerZ, final float radiusSquared) {
        float radius2 = radiusSquared;
        if (centerX < minX) {
            final float d = centerX - minX;
            radius2 -= d * d;
        }
        else if (centerX > maxX) {
            final float d = centerX - maxX;
            radius2 -= d * d;
        }
        if (centerY < minY) {
            final float d = centerY - minY;
            radius2 -= d * d;
        }
        else if (centerY > maxY) {
            final float d = centerY - maxY;
            radius2 -= d * d;
        }
        if (centerZ < minZ) {
            final float d = centerZ - minZ;
            radius2 -= d * d;
        }
        else if (centerZ > maxZ) {
            final float d = centerZ - maxZ;
            radius2 -= d * d;
        }
        return radius2 >= 0.0f;
    }
    
    public static boolean testAabSphere(final Vector3fc min, final Vector3fc max, final Vector3fc center, final float radiusSquared) {
        return testAabSphere(min.x(), min.y(), min.z(), max.x(), max.y(), max.z(), center.x(), center.y(), center.z(), radiusSquared);
    }
    
    public static Vector3f findClosestPointOnPlane(final float aX, final float aY, final float aZ, final float nX, final float nY, final float nZ, final float pX, final float pY, final float pZ, final Vector3f result) {
        final float d = -(nX * aX + nY * aY + nZ * aZ);
        final float t = nX * pX + nY * pY + nZ * pZ - d;
        result.x = pX - t * nX;
        result.y = pY - t * nY;
        result.z = pZ - t * nZ;
        return result;
    }
    
    public static Vector3f findClosestPointOnLineSegment(final float aX, final float aY, final float aZ, final float bX, final float bY, final float bZ, final float pX, final float pY, final float pZ, final Vector3f result) {
        final float abX = bX - aX;
        final float abY = bY - aY;
        final float abZ = bZ - aZ;
        float t = ((pX - aX) * abX + (pY - aY) * abY + (pZ - aZ) * abZ) / (abX * abX + abY * abY + abZ * abZ);
        if (t < 0.0f) {
            t = 0.0f;
        }
        if (t > 1.0f) {
            t = 1.0f;
        }
        result.x = aX + t * abX;
        result.y = aY + t * abY;
        result.z = aZ + t * abZ;
        return result;
    }
    
    public static float findClosestPointsLineSegments(final float a0X, final float a0Y, final float a0Z, final float a1X, final float a1Y, final float a1Z, final float b0X, final float b0Y, final float b0Z, final float b1X, final float b1Y, final float b1Z, final Vector3f resultA, final Vector3f resultB) {
        final float d1x = a1X - a0X;
        final float d1y = a1Y - a0Y;
        final float d1z = a1Z - a0Z;
        final float d2x = b1X - b0X;
        final float d2y = b1Y - b0Y;
        final float d2z = b1Z - b0Z;
        final float rX = a0X - b0X;
        final float rY = a0Y - b0Y;
        final float rZ = a0Z - b0Z;
        final float a = d1x * d1x + d1y * d1y + d1z * d1z;
        final float e = d2x * d2x + d2y * d2y + d2z * d2z;
        final float f = d2x * rX + d2y * rY + d2z * rZ;
        final float EPSILON = 1.0E-5f;
        if (a <= EPSILON && e <= EPSILON) {
            resultA.set(a0X, a0Y, a0Z);
            resultB.set(b0X, b0Y, b0Z);
            return resultA.dot(resultB);
        }
        float s;
        float t;
        if (a <= EPSILON) {
            s = 0.0f;
            t = f / e;
            t = Math.min(Math.max(t, 0.0f), 1.0f);
        }
        else {
            final float c = d1x * rX + d1y * rY + d1z * rZ;
            if (e <= EPSILON) {
                t = 0.0f;
                s = Math.min(Math.max(-c / a, 0.0f), 1.0f);
            }
            else {
                final float b = d1x * d2x + d1y * d2y + d1z * d2z;
                final float denom = a * e - b * b;
                if (denom != 0.0) {
                    s = Math.min(Math.max((b * f - c * e) / denom, 0.0f), 1.0f);
                }
                else {
                    s = 0.0f;
                }
                t = (b * s + f) / e;
                if (t < 0.0) {
                    t = 0.0f;
                    s = Math.min(Math.max(-c / a, 0.0f), 1.0f);
                }
                else if (t > 1.0) {
                    t = 1.0f;
                    s = Math.min(Math.max((b - c) / a, 0.0f), 1.0f);
                }
            }
        }
        resultA.set(a0X + d1x * s, a0Y + d1y * s, a0Z + d1z * s);
        resultB.set(b0X + d2x * t, b0Y + d2y * t, b0Z + d2z * t);
        final float dX = resultA.x - resultB.x;
        final float dY = resultA.y - resultB.y;
        final float dZ = resultA.z - resultB.z;
        return dX * dX + dY * dY + dZ * dZ;
    }
    
    public static float findClosestPointsLineSegmentTriangle(final float aX, final float aY, final float aZ, final float bX, final float bY, final float bZ, final float v0X, final float v0Y, final float v0Z, final float v1X, final float v1Y, final float v1Z, final float v2X, final float v2Y, final float v2Z, final Vector3f lineSegmentResult, final Vector3f triangleResult) {
        float min;
        float d = min = findClosestPointsLineSegments(aX, aY, aZ, bX, bY, bZ, v0X, v0Y, v0Z, v1X, v1Y, v1Z, lineSegmentResult, triangleResult);
        float minlsX = lineSegmentResult.x;
        float minlsY = lineSegmentResult.y;
        float minlsZ = lineSegmentResult.z;
        float mintX = triangleResult.x;
        float mintY = triangleResult.y;
        float mintZ = triangleResult.z;
        d = findClosestPointsLineSegments(aX, aY, aZ, bX, bY, bZ, v1X, v1Y, v1Z, v2X, v2Y, v2Z, lineSegmentResult, triangleResult);
        if (d < min) {
            min = d;
            minlsX = lineSegmentResult.x;
            minlsY = lineSegmentResult.y;
            minlsZ = lineSegmentResult.z;
            mintX = triangleResult.x;
            mintY = triangleResult.y;
            mintZ = triangleResult.z;
        }
        d = findClosestPointsLineSegments(aX, aY, aZ, bX, bY, bZ, v2X, v2Y, v2Z, v0X, v0Y, v0Z, lineSegmentResult, triangleResult);
        if (d < min) {
            min = d;
            minlsX = lineSegmentResult.x;
            minlsY = lineSegmentResult.y;
            minlsZ = lineSegmentResult.z;
            mintX = triangleResult.x;
            mintY = triangleResult.y;
            mintZ = triangleResult.z;
        }
        boolean computed = false;
        float a = Float.NaN;
        float b = Float.NaN;
        float c = Float.NaN;
        float nd = Float.NaN;
        if (testPointInTriangle(aX, aY, aZ, v0X, v0Y, v0Z, v1X, v1Y, v1Z, v2X, v2Y, v2Z)) {
            final float v1Y0Y = v1Y - v0Y;
            final float v2Z0Z = v2Z - v0Z;
            final float v2Y0Y = v2Y - v0Y;
            final float v1Z0Z = v1Z - v0Z;
            final float v2X0X = v2X - v0X;
            final float v1X0X = v1X - v0X;
            a = v1Y0Y * v2Z0Z - v2Y0Y * v1Z0Z;
            b = v1Z0Z * v2X0X - v2Z0Z * v1X0X;
            c = v1X0X * v2Y0Y - v2X0X * v1Y0Y;
            computed = true;
            final float invLen = Math.invsqrt(a * a + b * b + c * c);
            a *= invLen;
            b *= invLen;
            c *= invLen;
            nd = -(a * v0X + b * v0Y + c * v0Z);
            final float l;
            d = (l = a * aX + b * aY + c * aZ + nd);
            d *= d;
            if (d < min) {
                min = d;
                minlsX = aX;
                minlsY = aY;
                minlsZ = aZ;
                mintX = aX - a * l;
                mintY = aY - b * l;
                mintZ = aZ - c * l;
            }
        }
        if (testPointInTriangle(bX, bY, bZ, v0X, v0Y, v0Z, v1X, v1Y, v1Z, v2X, v2Y, v2Z)) {
            if (!computed) {
                final float v1Y0Y = v1Y - v0Y;
                final float v2Z0Z = v2Z - v0Z;
                final float v2Y0Y = v2Y - v0Y;
                final float v1Z0Z = v1Z - v0Z;
                final float v2X0X = v2X - v0X;
                final float v1X0X = v1X - v0X;
                a = v1Y0Y * v2Z0Z - v2Y0Y * v1Z0Z;
                b = v1Z0Z * v2X0X - v2Z0Z * v1X0X;
                c = v1X0X * v2Y0Y - v2X0X * v1Y0Y;
                final float invLen = Math.invsqrt(a * a + b * b + c * c);
                a *= invLen;
                b *= invLen;
                c *= invLen;
                nd = -(a * v0X + b * v0Y + c * v0Z);
            }
            final float i;
            d = (i = a * bX + b * bY + c * bZ + nd);
            d *= d;
            if (d < min) {
                min = d;
                minlsX = bX;
                minlsY = bY;
                minlsZ = bZ;
                mintX = bX - a * i;
                mintY = bY - b * i;
                mintZ = bZ - c * i;
            }
        }
        lineSegmentResult.set(minlsX, minlsY, minlsZ);
        triangleResult.set(mintX, mintY, mintZ);
        return min;
    }
    
    public static int findClosestPointOnTriangle(final float v0X, final float v0Y, final float v0Z, final float v1X, final float v1Y, final float v1Z, final float v2X, final float v2Y, final float v2Z, final float pX, final float pY, final float pZ, final Vector3f result) {
        final float abX = v1X - v0X;
        final float abY = v1Y - v0Y;
        final float abZ = v1Z - v0Z;
        final float acX = v2X - v0X;
        final float acY = v2Y - v0Y;
        final float acZ = v2Z - v0Z;
        final float apX = pX - v0X;
        final float apY = pY - v0Y;
        final float apZ = pZ - v0Z;
        final float d1 = abX * apX + abY * apY + abZ * apZ;
        final float d2 = acX * apX + acY * apY + acZ * apZ;
        if (d1 <= 0.0f && d2 <= 0.0f) {
            result.x = v0X;
            result.y = v0Y;
            result.z = v0Z;
            return 1;
        }
        final float bpX = pX - v1X;
        final float bpY = pY - v1Y;
        final float bpZ = pZ - v1Z;
        final float d3 = abX * bpX + abY * bpY + abZ * bpZ;
        final float d4 = acX * bpX + acY * bpY + acZ * bpZ;
        if (d3 >= 0.0f && d4 <= d3) {
            result.x = v1X;
            result.y = v1Y;
            result.z = v1Z;
            return 2;
        }
        final float vc = d1 * d4 - d3 * d2;
        if (vc <= 0.0f && d1 >= 0.0f && d3 <= 0.0f) {
            final float v = d1 / (d1 - d3);
            result.x = v0X + v * abX;
            result.y = v0Y + v * abY;
            result.z = v0Z + v * abZ;
            return 4;
        }
        final float cpX = pX - v2X;
        final float cpY = pY - v2Y;
        final float cpZ = pZ - v2Z;
        final float d5 = abX * cpX + abY * cpY + abZ * cpZ;
        final float d6 = acX * cpX + acY * cpY + acZ * cpZ;
        if (d6 >= 0.0f && d5 <= d6) {
            result.x = v2X;
            result.y = v2Y;
            result.z = v2Z;
            return 3;
        }
        final float vb = d5 * d2 - d1 * d6;
        if (vb <= 0.0f && d2 >= 0.0f && d6 <= 0.0f) {
            final float w = d2 / (d2 - d6);
            result.x = v0X + w * acX;
            result.y = v0Y + w * acY;
            result.z = v0Z + w * acZ;
            return 6;
        }
        final float va = d3 * d6 - d5 * d4;
        if (va <= 0.0f && d4 - d3 >= 0.0f && d5 - d6 >= 0.0f) {
            final float w2 = (d4 - d3) / (d4 - d3 + d5 - d6);
            result.x = v1X + w2 * (v2X - v1X);
            result.y = v1Y + w2 * (v2Y - v1Y);
            result.z = v1Z + w2 * (v2Z - v1Z);
            return 5;
        }
        final float denom = 1.0f / (va + vb + vc);
        final float v2 = vb * denom;
        final float w3 = vc * denom;
        result.x = v0X + abX * v2 + acX * w3;
        result.y = v0Y + abY * v2 + acY * w3;
        result.z = v0Z + abZ * v2 + acZ * w3;
        return 7;
    }
    
    public static int findClosestPointOnTriangle(final Vector3fc v0, final Vector3fc v1, final Vector3fc v2, final Vector3fc p, final Vector3f result) {
        return findClosestPointOnTriangle(v0.x(), v0.y(), v0.z(), v1.x(), v1.y(), v1.z(), v2.x(), v2.y(), v2.z(), p.x(), p.y(), p.z(), result);
    }
    
    public static Vector3f findClosestPointOnRectangle(final float aX, final float aY, final float aZ, final float bX, final float bY, final float bZ, final float cX, final float cY, final float cZ, final float pX, final float pY, final float pZ, final Vector3f res) {
        final float abX = bX - aX;
        final float abY = bY - aY;
        final float abZ = bZ - aZ;
        final float acX = cX - aX;
        final float acY = cY - aY;
        final float acZ = cZ - aZ;
        final float dX = pX - aX;
        final float dY = pY - aY;
        final float dZ = pZ - aZ;
        float qX = aX;
        float qY = aY;
        float qZ = aZ;
        float dist = dX * abX + dY * abY + dZ * abZ;
        float maxdist = abX * abX + abY * abY + abZ * abZ;
        if (dist >= maxdist) {
            qX += abX;
            qY += abY;
            qZ += abZ;
        }
        else if (dist > 0.0f) {
            qX += dist / maxdist * abX;
            qY += dist / maxdist * abY;
            qZ += dist / maxdist * abZ;
        }
        dist = dX * acX + dY * acY + dZ * acZ;
        maxdist = acX * acX + acY * acY + acZ * acZ;
        if (dist >= maxdist) {
            qX += acX;
            qY += acY;
            qZ += acZ;
        }
        else if (dist > 0.0f) {
            qX += dist / maxdist * acX;
            qY += dist / maxdist * acY;
            qZ += dist / maxdist * acZ;
        }
        res.x = qX;
        res.y = qY;
        res.z = qZ;
        return res;
    }
    
    public static int intersectSweptSphereTriangle(final float centerX, final float centerY, final float centerZ, final float radius, final float velX, final float velY, final float velZ, final float v0X, final float v0Y, final float v0Z, final float v1X, final float v1Y, final float v1Z, final float v2X, final float v2Y, final float v2Z, final float epsilon, final float maxT, final Vector4f pointAndTime) {
        final float v10X = v1X - v0X;
        final float v10Y = v1Y - v0Y;
        final float v10Z = v1Z - v0Z;
        final float v20X = v2X - v0X;
        final float v20Y = v2Y - v0Y;
        final float v20Z = v2Z - v0Z;
        final float a = v10Y * v20Z - v20Y * v10Z;
        final float b = v10Z * v20X - v20Z * v10X;
        final float c = v10X * v20Y - v20X * v10Y;
        final float d = -(a * v0X + b * v0Y + c * v0Z);
        final float invLen = Math.invsqrt(a * a + b * b + c * c);
        final float signedDist = (a * centerX + b * centerY + c * centerZ + d) * invLen;
        final float dot = (a * velX + b * velY + c * velZ) * invLen;
        if (dot < epsilon && dot > -epsilon) {
            return 0;
        }
        final float pt0 = (radius - signedDist) / dot;
        if (pt0 > maxT) {
            return 0;
        }
        final float pt2 = (-radius - signedDist) / dot;
        final float p0X = centerX - radius * a * invLen + velX * pt0;
        final float p0Y = centerY - radius * b * invLen + velY * pt0;
        final float p0Z = centerZ - radius * c * invLen + velZ * pt0;
        final boolean insideTriangle = testPointInTriangle(p0X, p0Y, p0Z, v0X, v0Y, v0Z, v1X, v1Y, v1Z, v2X, v2Y, v2Z);
        if (insideTriangle) {
            pointAndTime.x = p0X;
            pointAndTime.y = p0Y;
            pointAndTime.z = p0Z;
            pointAndTime.w = pt0;
            return 7;
        }
        int isect = 0;
        float t0 = maxT;
        final float A = velX * velX + velY * velY + velZ * velZ;
        final float radius2 = radius * radius;
        final float centerV0X = centerX - v0X;
        final float centerV0Y = centerY - v0Y;
        final float centerV0Z = centerZ - v0Z;
        final float B0 = 2.0f * (velX * centerV0X + velY * centerV0Y + velZ * centerV0Z);
        final float C0 = centerV0X * centerV0X + centerV0Y * centerV0Y + centerV0Z * centerV0Z - radius2;
        final float root0 = computeLowestRoot(A, B0, C0, t0);
        if (root0 < t0) {
            pointAndTime.x = v0X;
            pointAndTime.y = v0Y;
            pointAndTime.z = v0Z;
            pointAndTime.w = root0;
            t0 = root0;
            isect = 1;
        }
        final float centerV1X = centerX - v1X;
        final float centerV1Y = centerY - v1Y;
        final float centerV1Z = centerZ - v1Z;
        final float centerV1Len = centerV1X * centerV1X + centerV1Y * centerV1Y + centerV1Z * centerV1Z;
        final float B2 = 2.0f * (velX * centerV1X + velY * centerV1Y + velZ * centerV1Z);
        final float C2 = centerV1Len - radius2;
        final float root2 = computeLowestRoot(A, B2, C2, t0);
        if (root2 < t0) {
            pointAndTime.x = v1X;
            pointAndTime.y = v1Y;
            pointAndTime.z = v1Z;
            pointAndTime.w = root2;
            t0 = root2;
            isect = 2;
        }
        final float centerV2X = centerX - v2X;
        final float centerV2Y = centerY - v2Y;
        final float centerV2Z = centerZ - v2Z;
        final float B3 = 2.0f * (velX * centerV2X + velY * centerV2Y + velZ * centerV2Z);
        final float C3 = centerV2X * centerV2X + centerV2Y * centerV2Y + centerV2Z * centerV2Z - radius2;
        final float root3 = computeLowestRoot(A, B3, C3, t0);
        if (root3 < t0) {
            pointAndTime.x = v2X;
            pointAndTime.y = v2Y;
            pointAndTime.z = v2Z;
            pointAndTime.w = root3;
            t0 = root3;
            isect = 3;
        }
        final float velLen = velX * velX + velY * velY + velZ * velZ;
        final float len10 = v10X * v10X + v10Y * v10Y + v10Z * v10Z;
        final float baseTo0Len = centerV0X * centerV0X + centerV0Y * centerV0Y + centerV0Z * centerV0Z;
        final float v10Vel = v10X * velX + v10Y * velY + v10Z * velZ;
        final float A2 = len10 * -velLen + v10Vel * v10Vel;
        final float v10BaseTo0 = v10X * -centerV0X + v10Y * -centerV0Y + v10Z * -centerV0Z;
        final float velBaseTo0 = velX * -centerV0X + velY * -centerV0Y + velZ * -centerV0Z;
        final float B4 = len10 * 2.0f * velBaseTo0 - 2.0f * v10Vel * v10BaseTo0;
        final float C4 = len10 * (radius2 - baseTo0Len) + v10BaseTo0 * v10BaseTo0;
        final float root4 = computeLowestRoot(A2, B4, C4, t0);
        final float f10 = (v10Vel * root4 - v10BaseTo0) / len10;
        if (f10 >= 0.0f && f10 <= 1.0f && root4 < t0) {
            pointAndTime.x = v0X + f10 * v10X;
            pointAndTime.y = v0Y + f10 * v10Y;
            pointAndTime.z = v0Z + f10 * v10Z;
            pointAndTime.w = root4;
            t0 = root4;
            isect = 4;
        }
        final float len11 = v20X * v20X + v20Y * v20Y + v20Z * v20Z;
        final float v20Vel = v20X * velX + v20Y * velY + v20Z * velZ;
        final float A3 = len11 * -velLen + v20Vel * v20Vel;
        final float v20BaseTo0 = v20X * -centerV0X + v20Y * -centerV0Y + v20Z * -centerV0Z;
        final float B5 = len11 * 2.0f * velBaseTo0 - 2.0f * v20Vel * v20BaseTo0;
        final float C5 = len11 * (radius2 - baseTo0Len) + v20BaseTo0 * v20BaseTo0;
        final float root5 = computeLowestRoot(A3, B5, C5, t0);
        final float f11 = (v20Vel * root5 - v20BaseTo0) / len11;
        if (f11 >= 0.0f && f11 <= 1.0f && root5 < pt2) {
            pointAndTime.x = v0X + f11 * v20X;
            pointAndTime.y = v0Y + f11 * v20Y;
            pointAndTime.z = v0Z + f11 * v20Z;
            pointAndTime.w = root5;
            t0 = root5;
            isect = 6;
        }
        final float v21X = v2X - v1X;
        final float v21Y = v2Y - v1Y;
        final float v21Z = v2Z - v1Z;
        final float len12 = v21X * v21X + v21Y * v21Y + v21Z * v21Z;
        final float baseTo1Len = centerV1Len;
        final float v21Vel = v21X * velX + v21Y * velY + v21Z * velZ;
        final float A4 = len12 * -velLen + v21Vel * v21Vel;
        final float v21BaseTo1 = v21X * -centerV1X + v21Y * -centerV1Y + v21Z * -centerV1Z;
        final float velBaseTo2 = velX * -centerV1X + velY * -centerV1Y + velZ * -centerV1Z;
        final float B6 = len12 * 2.0f * velBaseTo2 - 2.0f * v21Vel * v21BaseTo1;
        final float C6 = len12 * (radius2 - baseTo1Len) + v21BaseTo1 * v21BaseTo1;
        final float root6 = computeLowestRoot(A4, B6, C6, t0);
        final float f12 = (v21Vel * root6 - v21BaseTo1) / len12;
        if (f12 >= 0.0f && f12 <= 1.0f && root6 < t0) {
            pointAndTime.x = v1X + f12 * v21X;
            pointAndTime.y = v1Y + f12 * v21Y;
            pointAndTime.z = v1Z + f12 * v21Z;
            pointAndTime.w = root6;
            t0 = root6;
            isect = 5;
        }
        return isect;
    }
    
    private static float computeLowestRoot(final float a, final float b, final float c, final float maxR) {
        final float determinant = b * b - 4.0f * a * c;
        if (determinant < 0.0f) {
            return Float.POSITIVE_INFINITY;
        }
        final float sqrtD = Math.sqrt(determinant);
        float r1 = (-b - sqrtD) / (2.0f * a);
        float r2 = (-b + sqrtD) / (2.0f * a);
        if (r1 > r2) {
            final float temp = r2;
            r2 = r1;
            r1 = temp;
        }
        if (r1 > 0.0f && r1 < maxR) {
            return r1;
        }
        if (r2 > 0.0f && r2 < maxR) {
            return r2;
        }
        return Float.POSITIVE_INFINITY;
    }
    
    public static boolean testPointInTriangle(final float pX, final float pY, final float pZ, final float v0X, final float v0Y, final float v0Z, final float v1X, final float v1Y, final float v1Z, final float v2X, final float v2Y, final float v2Z) {
        final float e10X = v1X - v0X;
        final float e10Y = v1Y - v0Y;
        final float e10Z = v1Z - v0Z;
        final float e20X = v2X - v0X;
        final float e20Y = v2Y - v0Y;
        final float e20Z = v2Z - v0Z;
        final float a = e10X * e10X + e10Y * e10Y + e10Z * e10Z;
        final float b = e10X * e20X + e10Y * e20Y + e10Z * e20Z;
        final float c = e20X * e20X + e20Y * e20Y + e20Z * e20Z;
        final float ac_bb = a * c - b * b;
        final float vpX = pX - v0X;
        final float vpY = pY - v0Y;
        final float vpZ = pZ - v0Z;
        final float d = vpX * e10X + vpY * e10Y + vpZ * e10Z;
        final float e = vpX * e20X + vpY * e20Y + vpZ * e20Z;
        final float x = d * c - e * b;
        final float y = e * a - d * b;
        final float z = x + y - ac_bb;
        return ((long)(Runtime.floatToIntBits(z) & ~(Runtime.floatToIntBits(x) | Runtime.floatToIntBits(y))) & Long.MIN_VALUE) != 0x0L;
    }
    
    public static boolean intersectRaySphere(final float originX, final float originY, final float originZ, final float dirX, final float dirY, final float dirZ, final float centerX, final float centerY, final float centerZ, final float radiusSquared, final Vector2f result) {
        final float Lx = centerX - originX;
        final float Ly = centerY - originY;
        final float Lz = centerZ - originZ;
        final float tca = Lx * dirX + Ly * dirY + Lz * dirZ;
        final float d2 = Lx * Lx + Ly * Ly + Lz * Lz - tca * tca;
        if (d2 > radiusSquared) {
            return false;
        }
        final float thc = Math.sqrt(radiusSquared - d2);
        final float t0 = tca - thc;
        final float t2 = tca + thc;
        if (t0 < t2 && t2 >= 0.0f) {
            result.x = t0;
            result.y = t2;
            return true;
        }
        return false;
    }
    
    public static boolean intersectRaySphere(final Vector3fc origin, final Vector3fc dir, final Vector3fc center, final float radiusSquared, final Vector2f result) {
        return intersectRaySphere(origin.x(), origin.y(), origin.z(), dir.x(), dir.y(), dir.z(), center.x(), center.y(), center.z(), radiusSquared, result);
    }
    
    public static boolean testRaySphere(final float originX, final float originY, final float originZ, final float dirX, final float dirY, final float dirZ, final float centerX, final float centerY, final float centerZ, final float radiusSquared) {
        final float Lx = centerX - originX;
        final float Ly = centerY - originY;
        final float Lz = centerZ - originZ;
        final float tca = Lx * dirX + Ly * dirY + Lz * dirZ;
        final float d2 = Lx * Lx + Ly * Ly + Lz * Lz - tca * tca;
        if (d2 > radiusSquared) {
            return false;
        }
        final float thc = Math.sqrt(radiusSquared - d2);
        final float t0 = tca - thc;
        final float t2 = tca + thc;
        return t0 < t2 && t2 >= 0.0f;
    }
    
    public static boolean testRaySphere(final Vector3fc origin, final Vector3fc dir, final Vector3fc center, final float radiusSquared) {
        return testRaySphere(origin.x(), origin.y(), origin.z(), dir.x(), dir.y(), dir.z(), center.x(), center.y(), center.z(), radiusSquared);
    }
    
    public static boolean testLineSegmentSphere(final float p0X, final float p0Y, final float p0Z, final float p1X, final float p1Y, final float p1Z, final float centerX, final float centerY, final float centerZ, final float radiusSquared) {
        float dX = p1X - p0X;
        float dY = p1Y - p0Y;
        float dZ = p1Z - p0Z;
        final float nom = (centerX - p0X) * dX + (centerY - p0Y) * dY + (centerZ - p0Z) * dZ;
        final float den = dX * dX + dY * dY + dZ * dZ;
        final float u = nom / den;
        if (u < 0.0f) {
            dX = p0X - centerX;
            dY = p0Y - centerY;
            dZ = p0Z - centerZ;
        }
        else if (u > 1.0f) {
            dX = p1X - centerX;
            dY = p1Y - centerY;
            dZ = p1Z - centerZ;
        }
        else {
            final float pX = p0X + u * dX;
            final float pY = p0Y + u * dY;
            final float pZ = p0Z + u * dZ;
            dX = pX - centerX;
            dY = pY - centerY;
            dZ = pZ - centerZ;
        }
        final float dist = dX * dX + dY * dY + dZ * dZ;
        return dist <= radiusSquared;
    }
    
    public static boolean testLineSegmentSphere(final Vector3fc p0, final Vector3fc p1, final Vector3fc center, final float radiusSquared) {
        return testLineSegmentSphere(p0.x(), p0.y(), p0.z(), p1.x(), p1.y(), p1.z(), center.x(), center.y(), center.z(), radiusSquared);
    }
    
    public static boolean intersectRayAab(final float originX, final float originY, final float originZ, final float dirX, final float dirY, final float dirZ, final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ, final Vector2f result) {
        final float invDirX = 1.0f / dirX;
        final float invDirY = 1.0f / dirY;
        final float invDirZ = 1.0f / dirZ;
        float tNear;
        float tFar;
        if (invDirX >= 0.0f) {
            tNear = (minX - originX) * invDirX;
            tFar = (maxX - originX) * invDirX;
        }
        else {
            tNear = (maxX - originX) * invDirX;
            tFar = (minX - originX) * invDirX;
        }
        float tymin;
        float tymax;
        if (invDirY >= 0.0f) {
            tymin = (minY - originY) * invDirY;
            tymax = (maxY - originY) * invDirY;
        }
        else {
            tymin = (maxY - originY) * invDirY;
            tymax = (minY - originY) * invDirY;
        }
        if (tNear > tymax || tymin > tFar) {
            return false;
        }
        float tzmin;
        float tzmax;
        if (invDirZ >= 0.0f) {
            tzmin = (minZ - originZ) * invDirZ;
            tzmax = (maxZ - originZ) * invDirZ;
        }
        else {
            tzmin = (maxZ - originZ) * invDirZ;
            tzmax = (minZ - originZ) * invDirZ;
        }
        if (tNear > tzmax || tzmin > tFar) {
            return false;
        }
        tNear = ((tymin > tNear || Float.isNaN(tNear)) ? tymin : tNear);
        tFar = ((tymax < tFar || Float.isNaN(tFar)) ? tymax : tFar);
        tNear = ((tzmin > tNear) ? tzmin : tNear);
        tFar = ((tzmax < tFar) ? tzmax : tFar);
        if (tNear < tFar && tFar >= 0.0f) {
            result.x = tNear;
            result.y = tFar;
            return true;
        }
        return false;
    }
    
    public static boolean intersectRayAab(final Vector3fc origin, final Vector3fc dir, final Vector3fc min, final Vector3fc max, final Vector2f result) {
        return intersectRayAab(origin.x(), origin.y(), origin.z(), dir.x(), dir.y(), dir.z(), min.x(), min.y(), min.z(), max.x(), max.y(), max.z(), result);
    }
    
    public static int intersectLineSegmentAab(final float p0X, final float p0Y, final float p0Z, final float p1X, final float p1Y, final float p1Z, final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ, final Vector2f result) {
        final float dirX = p1X - p0X;
        final float dirY = p1Y - p0Y;
        final float dirZ = p1Z - p0Z;
        final float invDirX = 1.0f / dirX;
        final float invDirY = 1.0f / dirY;
        final float invDirZ = 1.0f / dirZ;
        float tNear;
        float tFar;
        if (invDirX >= 0.0f) {
            tNear = (minX - p0X) * invDirX;
            tFar = (maxX - p0X) * invDirX;
        }
        else {
            tNear = (maxX - p0X) * invDirX;
            tFar = (minX - p0X) * invDirX;
        }
        float tymin;
        float tymax;
        if (invDirY >= 0.0f) {
            tymin = (minY - p0Y) * invDirY;
            tymax = (maxY - p0Y) * invDirY;
        }
        else {
            tymin = (maxY - p0Y) * invDirY;
            tymax = (minY - p0Y) * invDirY;
        }
        if (tNear > tymax || tymin > tFar) {
            return -1;
        }
        float tzmin;
        float tzmax;
        if (invDirZ >= 0.0f) {
            tzmin = (minZ - p0Z) * invDirZ;
            tzmax = (maxZ - p0Z) * invDirZ;
        }
        else {
            tzmin = (maxZ - p0Z) * invDirZ;
            tzmax = (minZ - p0Z) * invDirZ;
        }
        if (tNear > tzmax || tzmin > tFar) {
            return -1;
        }
        tNear = ((tymin > tNear || Float.isNaN(tNear)) ? tymin : tNear);
        tFar = ((tymax < tFar || Float.isNaN(tFar)) ? tymax : tFar);
        tNear = ((tzmin > tNear) ? tzmin : tNear);
        tFar = ((tzmax < tFar) ? tzmax : tFar);
        int type = -1;
        if (tNear <= tFar && tNear <= 1.0f && tFar >= 0.0f) {
            if (tNear >= 0.0f && tFar > 1.0f) {
                tFar = tNear;
                type = 1;
            }
            else if (tNear < 0.0f && tFar <= 1.0f) {
                tNear = tFar;
                type = 1;
            }
            else if (tNear < 0.0f && tFar > 1.0f) {
                type = 3;
            }
            else {
                type = 2;
            }
            result.x = tNear;
            result.y = tFar;
        }
        return type;
    }
    
    public static int intersectLineSegmentAab(final Vector3fc p0, final Vector3fc p1, final Vector3fc min, final Vector3fc max, final Vector2f result) {
        return intersectLineSegmentAab(p0.x(), p0.y(), p0.z(), p1.x(), p1.y(), p1.z(), min.x(), min.y(), min.z(), max.x(), max.y(), max.z(), result);
    }
    
    public static boolean testRayAab(final float originX, final float originY, final float originZ, final float dirX, final float dirY, final float dirZ, final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        final float invDirX = 1.0f / dirX;
        final float invDirY = 1.0f / dirY;
        final float invDirZ = 1.0f / dirZ;
        float tNear;
        float tFar;
        if (invDirX >= 0.0f) {
            tNear = (minX - originX) * invDirX;
            tFar = (maxX - originX) * invDirX;
        }
        else {
            tNear = (maxX - originX) * invDirX;
            tFar = (minX - originX) * invDirX;
        }
        float tymin;
        float tymax;
        if (invDirY >= 0.0f) {
            tymin = (minY - originY) * invDirY;
            tymax = (maxY - originY) * invDirY;
        }
        else {
            tymin = (maxY - originY) * invDirY;
            tymax = (minY - originY) * invDirY;
        }
        if (tNear > tymax || tymin > tFar) {
            return false;
        }
        float tzmin;
        float tzmax;
        if (invDirZ >= 0.0f) {
            tzmin = (minZ - originZ) * invDirZ;
            tzmax = (maxZ - originZ) * invDirZ;
        }
        else {
            tzmin = (maxZ - originZ) * invDirZ;
            tzmax = (minZ - originZ) * invDirZ;
        }
        if (tNear > tzmax || tzmin > tFar) {
            return false;
        }
        tNear = ((tymin > tNear || Float.isNaN(tNear)) ? tymin : tNear);
        tFar = ((tymax < tFar || Float.isNaN(tFar)) ? tymax : tFar);
        tNear = ((tzmin > tNear) ? tzmin : tNear);
        tFar = ((tzmax < tFar) ? tzmax : tFar);
        return tNear < tFar && tFar >= 0.0f;
    }
    
    public static boolean testRayAab(final Vector3fc origin, final Vector3fc dir, final Vector3fc min, final Vector3fc max) {
        return testRayAab(origin.x(), origin.y(), origin.z(), dir.x(), dir.y(), dir.z(), min.x(), min.y(), min.z(), max.x(), max.y(), max.z());
    }
    
    public static boolean testRayTriangleFront(final float originX, final float originY, final float originZ, final float dirX, final float dirY, final float dirZ, final float v0X, final float v0Y, final float v0Z, final float v1X, final float v1Y, final float v1Z, final float v2X, final float v2Y, final float v2Z, final float epsilon) {
        final float edge1X = v1X - v0X;
        final float edge1Y = v1Y - v0Y;
        final float edge1Z = v1Z - v0Z;
        final float edge2X = v2X - v0X;
        final float edge2Y = v2Y - v0Y;
        final float edge2Z = v2Z - v0Z;
        final float pvecX = dirY * edge2Z - dirZ * edge2Y;
        final float pvecY = dirZ * edge2X - dirX * edge2Z;
        final float pvecZ = dirX * edge2Y - dirY * edge2X;
        final float det = edge1X * pvecX + edge1Y * pvecY + edge1Z * pvecZ;
        if (det < epsilon) {
            return false;
        }
        final float tvecX = originX - v0X;
        final float tvecY = originY - v0Y;
        final float tvecZ = originZ - v0Z;
        final float u = tvecX * pvecX + tvecY * pvecY + tvecZ * pvecZ;
        if (u < 0.0f || u > det) {
            return false;
        }
        final float qvecX = tvecY * edge1Z - tvecZ * edge1Y;
        final float qvecY = tvecZ * edge1X - tvecX * edge1Z;
        final float qvecZ = tvecX * edge1Y - tvecY * edge1X;
        final float v = dirX * qvecX + dirY * qvecY + dirZ * qvecZ;
        if (v < 0.0f || u + v > det) {
            return false;
        }
        final float invDet = 1.0f / det;
        final float t = (edge2X * qvecX + edge2Y * qvecY + edge2Z * qvecZ) * invDet;
        return t >= epsilon;
    }
    
    public static boolean testRayTriangleFront(final Vector3fc origin, final Vector3fc dir, final Vector3fc v0, final Vector3fc v1, final Vector3fc v2, final float epsilon) {
        return testRayTriangleFront(origin.x(), origin.y(), origin.z(), dir.x(), dir.y(), dir.z(), v0.x(), v0.y(), v0.z(), v1.x(), v1.y(), v1.z(), v2.x(), v2.y(), v2.z(), epsilon);
    }
    
    public static boolean testRayTriangle(final float originX, final float originY, final float originZ, final float dirX, final float dirY, final float dirZ, final float v0X, final float v0Y, final float v0Z, final float v1X, final float v1Y, final float v1Z, final float v2X, final float v2Y, final float v2Z, final float epsilon) {
        final float edge1X = v1X - v0X;
        final float edge1Y = v1Y - v0Y;
        final float edge1Z = v1Z - v0Z;
        final float edge2X = v2X - v0X;
        final float edge2Y = v2Y - v0Y;
        final float edge2Z = v2Z - v0Z;
        final float pvecX = dirY * edge2Z - dirZ * edge2Y;
        final float pvecY = dirZ * edge2X - dirX * edge2Z;
        final float pvecZ = dirX * edge2Y - dirY * edge2X;
        final float det = edge1X * pvecX + edge1Y * pvecY + edge1Z * pvecZ;
        if (det > -epsilon && det < epsilon) {
            return false;
        }
        final float tvecX = originX - v0X;
        final float tvecY = originY - v0Y;
        final float tvecZ = originZ - v0Z;
        final float invDet = 1.0f / det;
        final float u = (tvecX * pvecX + tvecY * pvecY + tvecZ * pvecZ) * invDet;
        if (u < 0.0f || u > 1.0f) {
            return false;
        }
        final float qvecX = tvecY * edge1Z - tvecZ * edge1Y;
        final float qvecY = tvecZ * edge1X - tvecX * edge1Z;
        final float qvecZ = tvecX * edge1Y - tvecY * edge1X;
        final float v = (dirX * qvecX + dirY * qvecY + dirZ * qvecZ) * invDet;
        if (v < 0.0f || u + v > 1.0f) {
            return false;
        }
        final float t = (edge2X * qvecX + edge2Y * qvecY + edge2Z * qvecZ) * invDet;
        return t >= epsilon;
    }
    
    public static boolean testRayTriangle(final Vector3fc origin, final Vector3fc dir, final Vector3fc v0, final Vector3fc v1, final Vector3fc v2, final float epsilon) {
        return testRayTriangle(origin.x(), origin.y(), origin.z(), dir.x(), dir.y(), dir.z(), v0.x(), v0.y(), v0.z(), v1.x(), v1.y(), v1.z(), v2.x(), v2.y(), v2.z(), epsilon);
    }
    
    public static float intersectRayTriangleFront(final float originX, final float originY, final float originZ, final float dirX, final float dirY, final float dirZ, final float v0X, final float v0Y, final float v0Z, final float v1X, final float v1Y, final float v1Z, final float v2X, final float v2Y, final float v2Z, final float epsilon) {
        final float edge1X = v1X - v0X;
        final float edge1Y = v1Y - v0Y;
        final float edge1Z = v1Z - v0Z;
        final float edge2X = v2X - v0X;
        final float edge2Y = v2Y - v0Y;
        final float edge2Z = v2Z - v0Z;
        final float pvecX = dirY * edge2Z - dirZ * edge2Y;
        final float pvecY = dirZ * edge2X - dirX * edge2Z;
        final float pvecZ = dirX * edge2Y - dirY * edge2X;
        final float det = edge1X * pvecX + edge1Y * pvecY + edge1Z * pvecZ;
        if (det <= epsilon) {
            return -1.0f;
        }
        final float tvecX = originX - v0X;
        final float tvecY = originY - v0Y;
        final float tvecZ = originZ - v0Z;
        final float u = tvecX * pvecX + tvecY * pvecY + tvecZ * pvecZ;
        if (u < 0.0f || u > det) {
            return -1.0f;
        }
        final float qvecX = tvecY * edge1Z - tvecZ * edge1Y;
        final float qvecY = tvecZ * edge1X - tvecX * edge1Z;
        final float qvecZ = tvecX * edge1Y - tvecY * edge1X;
        final float v = dirX * qvecX + dirY * qvecY + dirZ * qvecZ;
        if (v < 0.0f || u + v > det) {
            return -1.0f;
        }
        final float invDet = 1.0f / det;
        final float t = (edge2X * qvecX + edge2Y * qvecY + edge2Z * qvecZ) * invDet;
        return t;
    }
    
    public static float intersectRayTriangleFront(final Vector3fc origin, final Vector3fc dir, final Vector3fc v0, final Vector3fc v1, final Vector3fc v2, final float epsilon) {
        return intersectRayTriangleFront(origin.x(), origin.y(), origin.z(), dir.x(), dir.y(), dir.z(), v0.x(), v0.y(), v0.z(), v1.x(), v1.y(), v1.z(), v2.x(), v2.y(), v2.z(), epsilon);
    }
    
    public static float intersectRayTriangle(final float originX, final float originY, final float originZ, final float dirX, final float dirY, final float dirZ, final float v0X, final float v0Y, final float v0Z, final float v1X, final float v1Y, final float v1Z, final float v2X, final float v2Y, final float v2Z, final float epsilon) {
        final float edge1X = v1X - v0X;
        final float edge1Y = v1Y - v0Y;
        final float edge1Z = v1Z - v0Z;
        final float edge2X = v2X - v0X;
        final float edge2Y = v2Y - v0Y;
        final float edge2Z = v2Z - v0Z;
        final float pvecX = dirY * edge2Z - dirZ * edge2Y;
        final float pvecY = dirZ * edge2X - dirX * edge2Z;
        final float pvecZ = dirX * edge2Y - dirY * edge2X;
        final float det = edge1X * pvecX + edge1Y * pvecY + edge1Z * pvecZ;
        if (det > -epsilon && det < epsilon) {
            return -1.0f;
        }
        final float tvecX = originX - v0X;
        final float tvecY = originY - v0Y;
        final float tvecZ = originZ - v0Z;
        final float invDet = 1.0f / det;
        final float u = (tvecX * pvecX + tvecY * pvecY + tvecZ * pvecZ) * invDet;
        if (u < 0.0f || u > 1.0f) {
            return -1.0f;
        }
        final float qvecX = tvecY * edge1Z - tvecZ * edge1Y;
        final float qvecY = tvecZ * edge1X - tvecX * edge1Z;
        final float qvecZ = tvecX * edge1Y - tvecY * edge1X;
        final float v = (dirX * qvecX + dirY * qvecY + dirZ * qvecZ) * invDet;
        if (v < 0.0f || u + v > 1.0f) {
            return -1.0f;
        }
        final float t = (edge2X * qvecX + edge2Y * qvecY + edge2Z * qvecZ) * invDet;
        return t;
    }
    
    public static float intersectRayTriangle(final Vector3fc origin, final Vector3fc dir, final Vector3fc v0, final Vector3fc v1, final Vector3fc v2, final float epsilon) {
        return intersectRayTriangle(origin.x(), origin.y(), origin.z(), dir.x(), dir.y(), dir.z(), v0.x(), v0.y(), v0.z(), v1.x(), v1.y(), v1.z(), v2.x(), v2.y(), v2.z(), epsilon);
    }
    
    public static boolean testLineSegmentTriangle(final float p0X, final float p0Y, final float p0Z, final float p1X, final float p1Y, final float p1Z, final float v0X, final float v0Y, final float v0Z, final float v1X, final float v1Y, final float v1Z, final float v2X, final float v2Y, final float v2Z, final float epsilon) {
        final float dirX = p1X - p0X;
        final float dirY = p1Y - p0Y;
        final float dirZ = p1Z - p0Z;
        final float t = intersectRayTriangle(p0X, p0Y, p0Z, dirX, dirY, dirZ, v0X, v0Y, v0Z, v1X, v1Y, v1Z, v2X, v2Y, v2Z, epsilon);
        return t >= 0.0f && t <= 1.0f;
    }
    
    public static boolean testLineSegmentTriangle(final Vector3fc p0, final Vector3fc p1, final Vector3fc v0, final Vector3fc v1, final Vector3fc v2, final float epsilon) {
        return testLineSegmentTriangle(p0.x(), p0.y(), p0.z(), p1.x(), p1.y(), p1.z(), v0.x(), v0.y(), v0.z(), v1.x(), v1.y(), v1.z(), v2.x(), v2.y(), v2.z(), epsilon);
    }
    
    public static boolean intersectLineSegmentTriangle(final float p0X, final float p0Y, final float p0Z, final float p1X, final float p1Y, final float p1Z, final float v0X, final float v0Y, final float v0Z, final float v1X, final float v1Y, final float v1Z, final float v2X, final float v2Y, final float v2Z, final float epsilon, final Vector3f intersectionPoint) {
        final float dirX = p1X - p0X;
        final float dirY = p1Y - p0Y;
        final float dirZ = p1Z - p0Z;
        final float t = intersectRayTriangle(p0X, p0Y, p0Z, dirX, dirY, dirZ, v0X, v0Y, v0Z, v1X, v1Y, v1Z, v2X, v2Y, v2Z, epsilon);
        if (t >= 0.0f && t <= 1.0f) {
            intersectionPoint.x = p0X + dirX * t;
            intersectionPoint.y = p0Y + dirY * t;
            intersectionPoint.z = p0Z + dirZ * t;
            return true;
        }
        return false;
    }
    
    public static boolean intersectLineSegmentTriangle(final Vector3fc p0, final Vector3fc p1, final Vector3fc v0, final Vector3fc v1, final Vector3fc v2, final float epsilon, final Vector3f intersectionPoint) {
        return intersectLineSegmentTriangle(p0.x(), p0.y(), p0.z(), p1.x(), p1.y(), p1.z(), v0.x(), v0.y(), v0.z(), v1.x(), v1.y(), v1.z(), v2.x(), v2.y(), v2.z(), epsilon, intersectionPoint);
    }
    
    public static boolean intersectLineSegmentPlane(final float p0X, final float p0Y, final float p0Z, final float p1X, final float p1Y, final float p1Z, final float a, final float b, final float c, final float d, final Vector3f intersectionPoint) {
        final float dirX = p1X - p0X;
        final float dirY = p1Y - p0Y;
        final float dirZ = p1Z - p0Z;
        final float denom = a * dirX + b * dirY + c * dirZ;
        final float t = -(a * p0X + b * p0Y + c * p0Z + d) / denom;
        if (t >= 0.0f && t <= 1.0f) {
            intersectionPoint.x = p0X + t * dirX;
            intersectionPoint.y = p0Y + t * dirY;
            intersectionPoint.z = p0Z + t * dirZ;
            return true;
        }
        return false;
    }
    
    public static boolean testLineCircle(final float a, final float b, final float c, final float centerX, final float centerY, final float radius) {
        final float denom = Math.sqrt(a * a + b * b);
        final float dist = (a * centerX + b * centerY + c) / denom;
        return -radius <= dist && dist <= radius;
    }
    
    public static boolean intersectLineCircle(final float a, final float b, final float c, final float centerX, final float centerY, final float radius, final Vector3f intersectionCenterAndHL) {
        final float invDenom = Math.invsqrt(a * a + b * b);
        final float dist = (a * centerX + b * centerY + c) * invDenom;
        if (-radius <= dist && dist <= radius) {
            intersectionCenterAndHL.x = centerX + dist * a * invDenom;
            intersectionCenterAndHL.y = centerY + dist * b * invDenom;
            intersectionCenterAndHL.z = Math.sqrt(radius * radius - dist * dist);
            return true;
        }
        return false;
    }
    
    public static boolean intersectLineCircle(final float x0, final float y0, final float x1, final float y1, final float centerX, final float centerY, final float radius, final Vector3f intersectionCenterAndHL) {
        return intersectLineCircle(y0 - y1, x1 - x0, (x0 - x1) * y0 + (y1 - y0) * x0, centerX, centerY, radius, intersectionCenterAndHL);
    }
    
    public static boolean testAarLine(final float minX, final float minY, final float maxX, final float maxY, final float a, final float b, final float c) {
        float pX;
        float nX;
        if (a > 0.0f) {
            pX = maxX;
            nX = minX;
        }
        else {
            pX = minX;
            nX = maxX;
        }
        float pY;
        float nY;
        if (b > 0.0f) {
            pY = maxY;
            nY = minY;
        }
        else {
            pY = minY;
            nY = maxY;
        }
        final float distN = c + a * nX + b * nY;
        final float distP = c + a * pX + b * pY;
        return distN <= 0.0f && distP >= 0.0f;
    }
    
    public static boolean testAarLine(final Vector2fc min, final Vector2fc max, final float a, final float b, final float c) {
        return testAarLine(min.x(), min.y(), max.x(), max.y(), a, b, c);
    }
    
    public static boolean testAarLine(final float minX, final float minY, final float maxX, final float maxY, final float x0, final float y0, final float x1, final float y1) {
        final float a = y0 - y1;
        final float b = x1 - x0;
        final float c = -b * y0 - a * x0;
        return testAarLine(minX, minY, maxX, maxY, a, b, c);
    }
    
    public static boolean testAarAar(final float minXA, final float minYA, final float maxXA, final float maxYA, final float minXB, final float minYB, final float maxXB, final float maxYB) {
        return maxXA >= minXB && maxYA >= minYB && minXA <= maxXB && minYA <= maxYB;
    }
    
    public static boolean testAarAar(final Vector2fc minA, final Vector2fc maxA, final Vector2fc minB, final Vector2fc maxB) {
        return testAarAar(minA.x(), minA.y(), maxA.x(), maxA.y(), minB.x(), minB.y(), maxB.x(), maxB.y());
    }
    
    public static boolean testMovingCircleCircle(final float aX, final float aY, final float maX, final float maY, final float aR, final float bX, final float bY, final float bR) {
        final float aRbR = aR + bR;
        final float dist = Math.sqrt((aX - bX) * (aX - bX) + (aY - bY) * (aY - bY)) - aRbR;
        final float mLen = Math.sqrt(maX * maX + maY * maY);
        if (mLen < dist) {
            return false;
        }
        final float invMLen = 1.0f / mLen;
        final float nX = maX * invMLen;
        final float nY = maY * invMLen;
        final float cX = bX - aX;
        final float cY = bY - aY;
        final float nDotC = nX * cX + nY * cY;
        if (nDotC <= 0.0f) {
            return false;
        }
        final float cLen = Math.sqrt(cX * cX + cY * cY);
        final float cLenNdotC = cLen * cLen - nDotC * nDotC;
        final float aRbR2 = aRbR * aRbR;
        if (cLenNdotC >= aRbR2) {
            return false;
        }
        final float t = aRbR2 - cLenNdotC;
        if (t < 0.0f) {
            return false;
        }
        final float distance = nDotC - Math.sqrt(t);
        final float mag = mLen;
        return mag >= distance;
    }
    
    public static boolean testMovingCircleCircle(final Vector2f centerA, final Vector2f moveA, final float aR, final Vector2f centerB, final float bR) {
        return testMovingCircleCircle(centerA.x, centerA.y, moveA.x, moveA.y, aR, centerB.x, centerB.y, bR);
    }
    
    public static boolean intersectCircleCircle(final float aX, final float aY, final float radiusSquaredA, final float bX, final float bY, final float radiusSquaredB, final Vector3f intersectionCenterAndHL) {
        final float dX = bX - aX;
        final float dY = bY - aY;
        final float distSquared = dX * dX + dY * dY;
        final float h = 0.5f + (radiusSquaredA - radiusSquaredB) / distSquared;
        final float r_i = Math.sqrt(radiusSquaredA - h * h * distSquared);
        if (r_i >= 0.0f) {
            intersectionCenterAndHL.x = aX + h * dX;
            intersectionCenterAndHL.y = aY + h * dY;
            intersectionCenterAndHL.z = r_i;
            return true;
        }
        return false;
    }
    
    public static boolean intersectCircleCircle(final Vector2fc centerA, final float radiusSquaredA, final Vector2fc centerB, final float radiusSquaredB, final Vector3f intersectionCenterAndHL) {
        return intersectCircleCircle(centerA.x(), centerA.y(), radiusSquaredA, centerB.x(), centerB.y(), radiusSquaredB, intersectionCenterAndHL);
    }
    
    public static boolean testCircleCircle(final float aX, final float aY, final float rA, final float bX, final float bY, final float rB) {
        final float d = (aX - bX) * (aX - bX) + (aY - bY) * (aY - bY);
        return d <= (rA + rB) * (rA + rB);
    }
    
    public static boolean testCircleCircle(final Vector2fc centerA, final float radiusSquaredA, final Vector2fc centerB, final float radiusSquaredB) {
        return testCircleCircle(centerA.x(), centerA.y(), radiusSquaredA, centerB.x(), centerB.y(), radiusSquaredB);
    }
    
    public static float distancePointLine(final float pointX, final float pointY, final float a, final float b, final float c) {
        final float denom = Math.sqrt(a * a + b * b);
        return (a * pointX + b * pointY + c) / denom;
    }
    
    public static float distancePointLine(final float pointX, final float pointY, final float x0, final float y0, final float x1, final float y1) {
        final float dx = x1 - x0;
        final float dy = y1 - y0;
        final float denom = Math.sqrt(dx * dx + dy * dy);
        return (dx * (y0 - pointY) - (x0 - pointX) * dy) / denom;
    }
    
    public static float distancePointLine(final float pX, final float pY, final float pZ, final float x0, final float y0, final float z0, final float x1, final float y1, final float z1) {
        final float d21x = x1 - x0;
        final float d21y = y1 - y0;
        final float d21z = z1 - z0;
        final float d10x = x0 - pX;
        final float d10y = y0 - pY;
        final float d10z = z0 - pZ;
        final float cx = d21y * d10z - d21z * d10y;
        final float cy = d21z * d10x - d21x * d10z;
        final float cz = d21x * d10y - d21y * d10x;
        return Math.sqrt((cx * cx + cy * cy + cz * cz) / (d21x * d21x + d21y * d21y + d21z * d21z));
    }
    
    public static float intersectRayLine(final float originX, final float originY, final float dirX, final float dirY, final float pointX, final float pointY, final float normalX, final float normalY, final float epsilon) {
        final float denom = normalX * dirX + normalY * dirY;
        if (denom < epsilon) {
            final float t = ((pointX - originX) * normalX + (pointY - originY) * normalY) / denom;
            if (t >= 0.0f) {
                return t;
            }
        }
        return -1.0f;
    }
    
    public static float intersectRayLine(final Vector2fc origin, final Vector2fc dir, final Vector2fc point, final Vector2fc normal, final float epsilon) {
        return intersectRayLine(origin.x(), origin.y(), dir.x(), dir.y(), point.x(), point.y(), normal.x(), normal.y(), epsilon);
    }
    
    public static float intersectRayLineSegment(final float originX, final float originY, final float dirX, final float dirY, final float aX, final float aY, final float bX, final float bY) {
        final float v1X = originX - aX;
        final float v1Y = originY - aY;
        final float v2X = bX - aX;
        final float v2Y = bY - aY;
        final float invV23 = 1.0f / (v2Y * dirX - v2X * dirY);
        final float t1 = (v2X * v1Y - v2Y * v1X) * invV23;
        final float t2 = (v1Y * dirX - v1X * dirY) * invV23;
        if (t1 >= 0.0f && t2 >= 0.0f && t2 <= 1.0f) {
            return t1;
        }
        return -1.0f;
    }
    
    public static float intersectRayLineSegment(final Vector2fc origin, final Vector2fc dir, final Vector2fc a, final Vector2fc b) {
        return intersectRayLineSegment(origin.x(), origin.y(), dir.x(), dir.y(), a.x(), a.y(), b.x(), b.y());
    }
    
    public static boolean testAarCircle(final float minX, final float minY, final float maxX, final float maxY, final float centerX, final float centerY, final float radiusSquared) {
        float radius2 = radiusSquared;
        if (centerX < minX) {
            final float d = centerX - minX;
            radius2 -= d * d;
        }
        else if (centerX > maxX) {
            final float d = centerX - maxX;
            radius2 -= d * d;
        }
        if (centerY < minY) {
            final float d = centerY - minY;
            radius2 -= d * d;
        }
        else if (centerY > maxY) {
            final float d = centerY - maxY;
            radius2 -= d * d;
        }
        return radius2 >= 0.0f;
    }
    
    public static boolean testAarCircle(final Vector2fc min, final Vector2fc max, final Vector2fc center, final float radiusSquared) {
        return testAarCircle(min.x(), min.y(), max.x(), max.y(), center.x(), center.y(), radiusSquared);
    }
    
    public static int findClosestPointOnTriangle(final float v0X, final float v0Y, final float v1X, final float v1Y, final float v2X, final float v2Y, final float pX, final float pY, final Vector2f result) {
        final float abX = v1X - v0X;
        final float abY = v1Y - v0Y;
        final float acX = v2X - v0X;
        final float acY = v2Y - v0Y;
        final float apX = pX - v0X;
        final float apY = pY - v0Y;
        final float d1 = abX * apX + abY * apY;
        final float d2 = acX * apX + acY * apY;
        if (d1 <= 0.0f && d2 <= 0.0f) {
            result.x = v0X;
            result.y = v0Y;
            return 1;
        }
        final float bpX = pX - v1X;
        final float bpY = pY - v1Y;
        final float d3 = abX * bpX + abY * bpY;
        final float d4 = acX * bpX + acY * bpY;
        if (d3 >= 0.0f && d4 <= d3) {
            result.x = v1X;
            result.y = v1Y;
            return 2;
        }
        final float vc = d1 * d4 - d3 * d2;
        if (vc <= 0.0f && d1 >= 0.0f && d3 <= 0.0f) {
            final float v = d1 / (d1 - d3);
            result.x = v0X + v * abX;
            result.y = v0Y + v * abY;
            return 4;
        }
        final float cpX = pX - v2X;
        final float cpY = pY - v2Y;
        final float d5 = abX * cpX + abY * cpY;
        final float d6 = acX * cpX + acY * cpY;
        if (d6 >= 0.0f && d5 <= d6) {
            result.x = v2X;
            result.y = v2Y;
            return 3;
        }
        final float vb = d5 * d2 - d1 * d6;
        if (vb <= 0.0f && d2 >= 0.0f && d6 <= 0.0f) {
            final float w = d2 / (d2 - d6);
            result.x = v0X + w * acX;
            result.y = v0Y + w * acY;
            return 6;
        }
        final float va = d3 * d6 - d5 * d4;
        if (va <= 0.0f && d4 - d3 >= 0.0f && d5 - d6 >= 0.0f) {
            final float w2 = (d4 - d3) / (d4 - d3 + d5 - d6);
            result.x = v1X + w2 * (v2X - v1X);
            result.y = v1Y + w2 * (v2Y - v1Y);
            return 5;
        }
        final float denom = 1.0f / (va + vb + vc);
        final float v2 = vb * denom;
        final float w3 = vc * denom;
        result.x = v0X + abX * v2 + acX * w3;
        result.y = v0Y + abY * v2 + acY * w3;
        return 7;
    }
    
    public static int findClosestPointOnTriangle(final Vector2fc v0, final Vector2fc v1, final Vector2fc v2, final Vector2fc p, final Vector2f result) {
        return findClosestPointOnTriangle(v0.x(), v0.y(), v1.x(), v1.y(), v2.x(), v2.y(), p.x(), p.y(), result);
    }
    
    public static boolean intersectRayCircle(final float originX, final float originY, final float dirX, final float dirY, final float centerX, final float centerY, final float radiusSquared, final Vector2f result) {
        final float Lx = centerX - originX;
        final float Ly = centerY - originY;
        final float tca = Lx * dirX + Ly * dirY;
        final float d2 = Lx * Lx + Ly * Ly - tca * tca;
        if (d2 > radiusSquared) {
            return false;
        }
        final float thc = Math.sqrt(radiusSquared - d2);
        final float t0 = tca - thc;
        final float t2 = tca + thc;
        if (t0 < t2 && t2 >= 0.0f) {
            result.x = t0;
            result.y = t2;
            return true;
        }
        return false;
    }
    
    public static boolean intersectRayCircle(final Vector2fc origin, final Vector2fc dir, final Vector2fc center, final float radiusSquared, final Vector2f result) {
        return intersectRayCircle(origin.x(), origin.y(), dir.x(), dir.y(), center.x(), center.y(), radiusSquared, result);
    }
    
    public static boolean testRayCircle(final float originX, final float originY, final float dirX, final float dirY, final float centerX, final float centerY, final float radiusSquared) {
        final float Lx = centerX - originX;
        final float Ly = centerY - originY;
        final float tca = Lx * dirX + Ly * dirY;
        final float d2 = Lx * Lx + Ly * Ly - tca * tca;
        if (d2 > radiusSquared) {
            return false;
        }
        final float thc = Math.sqrt(radiusSquared - d2);
        final float t0 = tca - thc;
        final float t2 = tca + thc;
        return t0 < t2 && t2 >= 0.0f;
    }
    
    public static boolean testRayCircle(final Vector2fc origin, final Vector2fc dir, final Vector2fc center, final float radiusSquared) {
        return testRayCircle(origin.x(), origin.y(), dir.x(), dir.y(), center.x(), center.y(), radiusSquared);
    }
    
    public static int intersectRayAar(final float originX, final float originY, final float dirX, final float dirY, final float minX, final float minY, final float maxX, final float maxY, final Vector2f result) {
        final float invDirX = 1.0f / dirX;
        final float invDirY = 1.0f / dirY;
        float tNear;
        float tFar;
        if (invDirX >= 0.0f) {
            tNear = (minX - originX) * invDirX;
            tFar = (maxX - originX) * invDirX;
        }
        else {
            tNear = (maxX - originX) * invDirX;
            tFar = (minX - originX) * invDirX;
        }
        float tymin;
        float tymax;
        if (invDirY >= 0.0f) {
            tymin = (minY - originY) * invDirY;
            tymax = (maxY - originY) * invDirY;
        }
        else {
            tymin = (maxY - originY) * invDirY;
            tymax = (minY - originY) * invDirY;
        }
        if (tNear > tymax || tymin > tFar) {
            return -1;
        }
        tNear = ((tymin > tNear || Float.isNaN(tNear)) ? tymin : tNear);
        tFar = ((tymax < tFar || Float.isNaN(tFar)) ? tymax : tFar);
        int side = -1;
        if (tNear <= tFar && tFar >= 0.0f) {
            final float px = originX + tNear * dirX;
            final float py = originY + tNear * dirY;
            result.x = tNear;
            result.y = tFar;
            final float daX = Math.abs(px - minX);
            final float daY = Math.abs(py - minY);
            final float dbX = Math.abs(px - maxX);
            final float dbY = Math.abs(py - maxY);
            side = 0;
            float min = daX;
            if (daY < min) {
                min = daY;
                side = 1;
            }
            if (dbX < min) {
                min = dbX;
                side = 2;
            }
            if (dbY < min) {
                side = 3;
            }
        }
        return side;
    }
    
    public static int intersectRayAar(final Vector2fc origin, final Vector2fc dir, final Vector2fc min, final Vector2fc max, final Vector2f result) {
        return intersectRayAar(origin.x(), origin.y(), dir.x(), dir.y(), min.x(), min.y(), max.x(), max.y(), result);
    }
    
    public static int intersectLineSegmentAar(final float p0X, final float p0Y, final float p1X, final float p1Y, final float minX, final float minY, final float maxX, final float maxY, final Vector2f result) {
        final float dirX = p1X - p0X;
        final float dirY = p1Y - p0Y;
        final float invDirX = 1.0f / dirX;
        final float invDirY = 1.0f / dirY;
        float tNear;
        float tFar;
        if (invDirX >= 0.0f) {
            tNear = (minX - p0X) * invDirX;
            tFar = (maxX - p0X) * invDirX;
        }
        else {
            tNear = (maxX - p0X) * invDirX;
            tFar = (minX - p0X) * invDirX;
        }
        float tymin;
        float tymax;
        if (invDirY >= 0.0f) {
            tymin = (minY - p0Y) * invDirY;
            tymax = (maxY - p0Y) * invDirY;
        }
        else {
            tymin = (maxY - p0Y) * invDirY;
            tymax = (minY - p0Y) * invDirY;
        }
        if (tNear > tymax || tymin > tFar) {
            return -1;
        }
        tNear = ((tymin > tNear || Float.isNaN(tNear)) ? tymin : tNear);
        tFar = ((tymax < tFar || Float.isNaN(tFar)) ? tymax : tFar);
        int type = -1;
        if (tNear <= tFar && tNear <= 1.0f && tFar >= 0.0f) {
            if (tNear >= 0.0f && tFar > 1.0f) {
                tFar = tNear;
                type = 1;
            }
            else if (tNear < 0.0f && tFar <= 1.0f) {
                tNear = tFar;
                type = 1;
            }
            else if (tNear < 0.0f && tFar > 1.0f) {
                type = 3;
            }
            else {
                type = 2;
            }
            result.x = tNear;
            result.y = tFar;
        }
        return type;
    }
    
    public static int intersectLineSegmentAar(final Vector2fc p0, final Vector2fc p1, final Vector2fc min, final Vector2fc max, final Vector2f result) {
        return intersectLineSegmentAar(p0.x(), p0.y(), p1.x(), p1.y(), min.x(), min.y(), max.x(), max.y(), result);
    }
    
    public static boolean testRayAar(final float originX, final float originY, final float dirX, final float dirY, final float minX, final float minY, final float maxX, final float maxY) {
        final float invDirX = 1.0f / dirX;
        final float invDirY = 1.0f / dirY;
        float tNear;
        float tFar;
        if (invDirX >= 0.0f) {
            tNear = (minX - originX) * invDirX;
            tFar = (maxX - originX) * invDirX;
        }
        else {
            tNear = (maxX - originX) * invDirX;
            tFar = (minX - originX) * invDirX;
        }
        float tymin;
        float tymax;
        if (invDirY >= 0.0f) {
            tymin = (minY - originY) * invDirY;
            tymax = (maxY - originY) * invDirY;
        }
        else {
            tymin = (maxY - originY) * invDirY;
            tymax = (minY - originY) * invDirY;
        }
        if (tNear > tymax || tymin > tFar) {
            return false;
        }
        tNear = ((tymin > tNear || Float.isNaN(tNear)) ? tymin : tNear);
        tFar = ((tymax < tFar || Float.isNaN(tFar)) ? tymax : tFar);
        return tNear < tFar && tFar >= 0.0f;
    }
    
    public static boolean testRayAar(final Vector2fc origin, final Vector2fc dir, final Vector2fc min, final Vector2fc max) {
        return testRayAar(origin.x(), origin.y(), dir.x(), dir.y(), min.x(), min.y(), max.x(), max.y());
    }
    
    public static boolean testPointTriangle(final float pX, final float pY, final float v0X, final float v0Y, final float v1X, final float v1Y, final float v2X, final float v2Y) {
        final boolean b1 = (pX - v1X) * (v0Y - v1Y) - (v0X - v1X) * (pY - v1Y) < 0.0f;
        final boolean b2 = (pX - v2X) * (v1Y - v2Y) - (v1X - v2X) * (pY - v2Y) < 0.0f;
        if (b1 != b2) {
            return false;
        }
        final boolean b3 = (pX - v0X) * (v2Y - v0Y) - (v2X - v0X) * (pY - v0Y) < 0.0f;
        return b2 == b3;
    }
    
    public static boolean testPointTriangle(final Vector2fc point, final Vector2fc v0, final Vector2fc v1, final Vector2fc v2) {
        return testPointTriangle(point.x(), point.y(), v0.x(), v0.y(), v1.x(), v1.y(), v2.x(), v2.y());
    }
    
    public static boolean testPointAar(final float pX, final float pY, final float minX, final float minY, final float maxX, final float maxY) {
        return pX >= minX && pY >= minY && pX <= maxX && pY <= maxY;
    }
    
    public static boolean testPointCircle(final float pX, final float pY, final float centerX, final float centerY, final float radiusSquared) {
        final float dx = pX - centerX;
        final float dy = pY - centerY;
        final float dx2 = dx * dx;
        final float dy2 = dy * dy;
        return dx2 + dy2 <= radiusSquared;
    }
    
    public static boolean testCircleTriangle(final float centerX, final float centerY, final float radiusSquared, final float v0X, final float v0Y, final float v1X, final float v1Y, final float v2X, final float v2Y) {
        final float c1x = centerX - v0X;
        final float c1y = centerY - v0Y;
        final float c1sqr = c1x * c1x + c1y * c1y - radiusSquared;
        if (c1sqr <= 0.0f) {
            return true;
        }
        final float c2x = centerX - v1X;
        final float c2y = centerY - v1Y;
        final float c2sqr = c2x * c2x + c2y * c2y - radiusSquared;
        if (c2sqr <= 0.0f) {
            return true;
        }
        final float c3x = centerX - v2X;
        final float c3y = centerY - v2Y;
        final float c3sqr = c3x * c3x + c3y * c3y - radiusSquared;
        if (c3sqr <= 0.0f) {
            return true;
        }
        final float e1x = v1X - v0X;
        final float e1y = v1Y - v0Y;
        final float e2x = v2X - v1X;
        final float e2y = v2Y - v1Y;
        final float e3x = v0X - v2X;
        final float e3y = v0Y - v2Y;
        if (e1x * c1y - e1y * c1x >= 0.0f && e2x * c2y - e2y * c2x >= 0.0f && e3x * c3y - e3y * c3x >= 0.0f) {
            return true;
        }
        float k = c1x * e1x + c1y * e1y;
        if (k >= 0.0f) {
            final float len = e1x * e1x + e1y * e1y;
            if (k <= len && c1sqr * len <= k * k) {
                return true;
            }
        }
        k = c2x * e2x + c2y * e2y;
        if (k > 0.0f) {
            final float len = e2x * e2x + e2y * e2y;
            if (k <= len && c2sqr * len <= k * k) {
                return true;
            }
        }
        k = c3x * e3x + c3y * e3y;
        if (k >= 0.0f) {
            final float len = e3x * e3x + e3y * e3y;
            if (k < len && c3sqr * len <= k * k) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean testCircleTriangle(final Vector2fc center, final float radiusSquared, final Vector2fc v0, final Vector2fc v1, final Vector2fc v2) {
        return testCircleTriangle(center.x(), center.y(), radiusSquared, v0.x(), v0.y(), v1.x(), v1.y(), v2.x(), v2.y());
    }
    
    public static int intersectPolygonRay(final float[] verticesXY, final float originX, final float originY, final float dirX, final float dirY, final Vector2f p) {
        float nearestT = Float.POSITIVE_INFINITY;
        final int count = verticesXY.length >> 1;
        int edgeIndex = -1;
        float aX = verticesXY[count - 1 << 1];
        float aY = verticesXY[(count - 1 << 1) + 1];
        for (int i = 0; i < count; ++i) {
            final float bX = verticesXY[i << 1];
            final float bY = verticesXY[(i << 1) + 1];
            final float doaX = originX - aX;
            final float doaY = originY - aY;
            final float dbaX = bX - aX;
            final float dbaY = bY - aY;
            final float invDbaDir = 1.0f / (dbaY * dirX - dbaX * dirY);
            final float t = (dbaX * doaY - dbaY * doaX) * invDbaDir;
            if (t >= 0.0f && t < nearestT) {
                final float t2 = (doaY * dirX - doaX * dirY) * invDbaDir;
                if (t2 >= 0.0f && t2 <= 1.0f) {
                    edgeIndex = (i - 1 + count) % count;
                    nearestT = t;
                    p.x = originX + t * dirX;
                    p.y = originY + t * dirY;
                }
            }
            aX = bX;
            aY = bY;
        }
        return edgeIndex;
    }
    
    public static int intersectPolygonRay(final Vector2fc[] vertices, final float originX, final float originY, final float dirX, final float dirY, final Vector2f p) {
        float nearestT = Float.POSITIVE_INFINITY;
        final int count = vertices.length;
        int edgeIndex = -1;
        float aX = vertices[count - 1].x();
        float aY = vertices[count - 1].y();
        for (int i = 0; i < count; ++i) {
            final Vector2fc b = vertices[i];
            final float bX = b.x();
            final float bY = b.y();
            final float doaX = originX - aX;
            final float doaY = originY - aY;
            final float dbaX = bX - aX;
            final float dbaY = bY - aY;
            final float invDbaDir = 1.0f / (dbaY * dirX - dbaX * dirY);
            final float t = (dbaX * doaY - dbaY * doaX) * invDbaDir;
            if (t >= 0.0f && t < nearestT) {
                final float t2 = (doaY * dirX - doaX * dirY) * invDbaDir;
                if (t2 >= 0.0f && t2 <= 1.0f) {
                    edgeIndex = (i - 1 + count) % count;
                    nearestT = t;
                    p.x = originX + t * dirX;
                    p.y = originY + t * dirY;
                }
            }
            aX = bX;
            aY = bY;
        }
        return edgeIndex;
    }
    
    public static boolean intersectLineLine(final float ps1x, final float ps1y, final float pe1x, final float pe1y, final float ps2x, final float ps2y, final float pe2x, final float pe2y, final Vector2f p) {
        final float d1x = ps1x - pe1x;
        final float d1y = pe1y - ps1y;
        final float d1ps1 = d1y * ps1x + d1x * ps1y;
        final float d2x = ps2x - pe2x;
        final float d2y = pe2y - ps2y;
        final float d2ps2 = d2y * ps2x + d2x * ps2y;
        final float det = d1y * d2x - d2y * d1x;
        if (det == 0.0f) {
            return false;
        }
        p.x = (d2x * d1ps1 - d1x * d2ps2) / det;
        p.y = (d1y * d2ps2 - d2y * d1ps1) / det;
        return true;
    }
    
    private static boolean separatingAxis(final Vector2f[] v1s, final Vector2f[] v2s, final float aX, final float aY) {
        float minA = Float.POSITIVE_INFINITY;
        float maxA = Float.NEGATIVE_INFINITY;
        float minB = Float.POSITIVE_INFINITY;
        float maxB = Float.NEGATIVE_INFINITY;
        for (int maxLen = Math.max(v1s.length, v2s.length), k = 0; k < maxLen; ++k) {
            if (k < v1s.length) {
                final Vector2f v1 = v1s[k];
                final float d = v1.x * aX + v1.y * aY;
                if (d < minA) {
                    minA = d;
                }
                if (d > maxA) {
                    maxA = d;
                }
            }
            if (k < v2s.length) {
                final Vector2f v2 = v2s[k];
                final float d = v2.x * aX + v2.y * aY;
                if (d < minB) {
                    minB = d;
                }
                if (d > maxB) {
                    maxB = d;
                }
            }
            if (minA <= maxB && minB <= maxA) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean testPolygonPolygon(final Vector2f[] v1s, final Vector2f[] v2s) {
        int i = 0;
        int j = v1s.length - 1;
        while (i < v1s.length) {
            final Vector2f s = v1s[i];
            final Vector2f t = v1s[j];
            if (separatingAxis(v1s, v2s, s.y - t.y, t.x - s.x)) {
                return false;
            }
            j = i;
            ++i;
        }
        i = 0;
        j = v2s.length - 1;
        while (i < v2s.length) {
            final Vector2f s = v2s[i];
            final Vector2f t = v2s[j];
            if (separatingAxis(v1s, v2s, s.y - t.y, t.x - s.x)) {
                return false;
            }
            j = i;
            ++i;
        }
        return true;
    }
}
