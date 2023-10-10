// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

public class Intersectiond
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
    
    public static boolean testPlaneSphere(final double a, final double b, final double c, final double d, final double centerX, final double centerY, final double centerZ, final double radius) {
        final double denom = Math.sqrt(a * a + b * b + c * c);
        final double dist = (a * centerX + b * centerY + c * centerZ + d) / denom;
        return -radius <= dist && dist <= radius;
    }
    
    public static boolean intersectPlaneSphere(final double a, final double b, final double c, final double d, final double centerX, final double centerY, final double centerZ, final double radius, final Vector4d intersectionCenterAndRadius) {
        final double invDenom = Math.invsqrt(a * a + b * b + c * c);
        final double dist = (a * centerX + b * centerY + c * centerZ + d) * invDenom;
        if (-radius <= dist && dist <= radius) {
            intersectionCenterAndRadius.x = centerX + dist * a * invDenom;
            intersectionCenterAndRadius.y = centerY + dist * b * invDenom;
            intersectionCenterAndRadius.z = centerZ + dist * c * invDenom;
            intersectionCenterAndRadius.w = Math.sqrt(radius * radius - dist * dist);
            return true;
        }
        return false;
    }
    
    public static boolean intersectPlaneSweptSphere(final double a, final double b, final double c, final double d, final double cX, final double cY, final double cZ, final double radius, final double vX, final double vY, final double vZ, final Vector4d pointAndTime) {
        final double dist = a * cX + b * cY + c * cZ - d;
        if (Math.abs(dist) <= radius) {
            pointAndTime.set(cX, cY, cZ, 0.0);
            return true;
        }
        final double denom = a * vX + b * vY + c * vZ;
        if (denom * dist >= 0.0) {
            return false;
        }
        final double r = (dist > 0.0) ? radius : (-radius);
        final double t = (r - dist) / denom;
        pointAndTime.set(cX + t * vX - r * a, cY + t * vY - r * b, cZ + t * vZ - r * c, t);
        return true;
    }
    
    public static boolean testPlaneSweptSphere(final double a, final double b, final double c, final double d, final double t0X, final double t0Y, final double t0Z, final double r, final double t1X, final double t1Y, final double t1Z) {
        final double adist = t0X * a + t0Y * b + t0Z * c - d;
        final double bdist = t1X * a + t1Y * b + t1Z * c - d;
        return adist * bdist < 0.0 || (Math.abs(adist) <= r || Math.abs(bdist) <= r);
    }
    
    public static boolean testAabPlane(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ, final double a, final double b, final double c, final double d) {
        double pX;
        double nX;
        if (a > 0.0) {
            pX = maxX;
            nX = minX;
        }
        else {
            pX = minX;
            nX = maxX;
        }
        double pY;
        double nY;
        if (b > 0.0) {
            pY = maxY;
            nY = minY;
        }
        else {
            pY = minY;
            nY = maxY;
        }
        double pZ;
        double nZ;
        if (c > 0.0) {
            pZ = maxZ;
            nZ = minZ;
        }
        else {
            pZ = minZ;
            nZ = maxZ;
        }
        final double distN = d + a * nX + b * nY + c * nZ;
        final double distP = d + a * pX + b * pY + c * pZ;
        return distN <= 0.0 && distP >= 0.0;
    }
    
    public static boolean testAabPlane(final Vector3dc min, final Vector3dc max, final double a, final double b, final double c, final double d) {
        return testAabPlane(min.x(), min.y(), min.z(), max.x(), max.y(), max.z(), a, b, c, d);
    }
    
    public static boolean testAabAab(final double minXA, final double minYA, final double minZA, final double maxXA, final double maxYA, final double maxZA, final double minXB, final double minYB, final double minZB, final double maxXB, final double maxYB, final double maxZB) {
        return maxXA >= minXB && maxYA >= minYB && maxZA >= minZB && minXA <= maxXB && minYA <= maxYB && minZA <= maxZB;
    }
    
    public static boolean testAabAab(final Vector3dc minA, final Vector3dc maxA, final Vector3dc minB, final Vector3dc maxB) {
        return testAabAab(minA.x(), minA.y(), minA.z(), maxA.x(), maxA.y(), maxA.z(), minB.x(), minB.y(), minB.z(), maxB.x(), maxB.y(), maxB.z());
    }
    
    public static boolean testObOb(final Vector3d b0c, final Vector3d b0uX, final Vector3d b0uY, final Vector3d b0uZ, final Vector3d b0hs, final Vector3d b1c, final Vector3d b1uX, final Vector3d b1uY, final Vector3d b1uZ, final Vector3d b1hs) {
        return testObOb(b0c.x, b0c.y, b0c.z, b0uX.x, b0uX.y, b0uX.z, b0uY.x, b0uY.y, b0uY.z, b0uZ.x, b0uZ.y, b0uZ.z, b0hs.x, b0hs.y, b0hs.z, b1c.x, b1c.y, b1c.z, b1uX.x, b1uX.y, b1uX.z, b1uY.x, b1uY.y, b1uY.z, b1uZ.x, b1uZ.y, b1uZ.z, b1hs.x, b1hs.y, b1hs.z);
    }
    
    public static boolean testObOb(final double b0cX, final double b0cY, final double b0cZ, final double b0uXx, final double b0uXy, final double b0uXz, final double b0uYx, final double b0uYy, final double b0uYz, final double b0uZx, final double b0uZy, final double b0uZz, final double b0hsX, final double b0hsY, final double b0hsZ, final double b1cX, final double b1cY, final double b1cZ, final double b1uXx, final double b1uXy, final double b1uXz, final double b1uYx, final double b1uYy, final double b1uYz, final double b1uZx, final double b1uZy, final double b1uZz, final double b1hsX, final double b1hsY, final double b1hsZ) {
        final double rm00 = b0uXx * b1uXx + b0uYx * b1uYx + b0uZx * b1uZx;
        final double rm2 = b0uXx * b1uXy + b0uYx * b1uYy + b0uZx * b1uZy;
        final double rm3 = b0uXx * b1uXz + b0uYx * b1uYz + b0uZx * b1uZz;
        final double rm4 = b0uXy * b1uXx + b0uYy * b1uYx + b0uZy * b1uZx;
        final double rm5 = b0uXy * b1uXy + b0uYy * b1uYy + b0uZy * b1uZy;
        final double rm6 = b0uXy * b1uXz + b0uYy * b1uYz + b0uZy * b1uZz;
        final double rm7 = b0uXz * b1uXx + b0uYz * b1uYx + b0uZz * b1uZx;
        final double rm8 = b0uXz * b1uXy + b0uYz * b1uYy + b0uZz * b1uZy;
        final double rm9 = b0uXz * b1uXz + b0uYz * b1uYz + b0uZz * b1uZz;
        final double EPSILON = 1.0E-8;
        final double arm00 = Math.abs(rm00) + EPSILON;
        final double arm2 = Math.abs(rm4) + EPSILON;
        final double arm3 = Math.abs(rm7) + EPSILON;
        final double arm4 = Math.abs(rm2) + EPSILON;
        final double arm5 = Math.abs(rm5) + EPSILON;
        final double arm6 = Math.abs(rm8) + EPSILON;
        final double arm7 = Math.abs(rm3) + EPSILON;
        final double arm8 = Math.abs(rm6) + EPSILON;
        final double arm9 = Math.abs(rm9) + EPSILON;
        final double tx = b1cX - b0cX;
        final double ty = b1cY - b0cY;
        final double tz = b1cZ - b0cZ;
        final double tax = tx * b0uXx + ty * b0uXy + tz * b0uXz;
        final double tay = tx * b0uYx + ty * b0uYy + tz * b0uYz;
        final double taz = tx * b0uZx + ty * b0uZy + tz * b0uZz;
        double ra = b0hsX;
        double rb = b1hsX * arm00 + b1hsY * arm2 + b1hsZ * arm3;
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
    
    public static boolean intersectSphereSphere(final double aX, final double aY, final double aZ, final double radiusSquaredA, final double bX, final double bY, final double bZ, final double radiusSquaredB, final Vector4d centerAndRadiusOfIntersectionCircle) {
        final double dX = bX - aX;
        final double dY = bY - aY;
        final double dZ = bZ - aZ;
        final double distSquared = dX * dX + dY * dY + dZ * dZ;
        final double h = 0.5 + (radiusSquaredA - radiusSquaredB) / (2.0 * distSquared);
        final double r_i = radiusSquaredA - h * h * distSquared;
        if (r_i >= 0.0) {
            centerAndRadiusOfIntersectionCircle.x = aX + h * dX;
            centerAndRadiusOfIntersectionCircle.y = aY + h * dY;
            centerAndRadiusOfIntersectionCircle.z = aZ + h * dZ;
            centerAndRadiusOfIntersectionCircle.w = Math.sqrt(r_i);
            return true;
        }
        return false;
    }
    
    public static boolean intersectSphereSphere(final Vector3dc centerA, final double radiusSquaredA, final Vector3dc centerB, final double radiusSquaredB, final Vector4d centerAndRadiusOfIntersectionCircle) {
        return intersectSphereSphere(centerA.x(), centerA.y(), centerA.z(), radiusSquaredA, centerB.x(), centerB.y(), centerB.z(), radiusSquaredB, centerAndRadiusOfIntersectionCircle);
    }
    
    public static int intersectSphereTriangle(final double sX, final double sY, final double sZ, final double sR, final double v0X, final double v0Y, final double v0Z, final double v1X, final double v1Y, final double v1Z, final double v2X, final double v2Y, final double v2Z, final Vector3d result) {
        final int closest = findClosestPointOnTriangle(v0X, v0Y, v0Z, v1X, v1Y, v1Z, v2X, v2Y, v2Z, sX, sY, sZ, result);
        final double vX = result.x - sX;
        final double vY = result.y - sY;
        final double vZ = result.z - sZ;
        final double dot = vX * vX + vY * vY + vZ * vZ;
        if (dot <= sR * sR) {
            return closest;
        }
        return 0;
    }
    
    public static boolean testSphereSphere(final double aX, final double aY, final double aZ, final double radiusSquaredA, final double bX, final double bY, final double bZ, final double radiusSquaredB) {
        final double dX = bX - aX;
        final double dY = bY - aY;
        final double dZ = bZ - aZ;
        final double distSquared = dX * dX + dY * dY + dZ * dZ;
        final double h = 0.5 + (radiusSquaredA - radiusSquaredB) / (2.0 * distSquared);
        final double r_i = radiusSquaredA - h * h * distSquared;
        return r_i >= 0.0;
    }
    
    public static boolean testSphereSphere(final Vector3dc centerA, final double radiusSquaredA, final Vector3dc centerB, final double radiusSquaredB) {
        return testSphereSphere(centerA.x(), centerA.y(), centerA.z(), radiusSquaredA, centerB.x(), centerB.y(), centerB.z(), radiusSquaredB);
    }
    
    public static double distancePointPlane(final double pointX, final double pointY, final double pointZ, final double a, final double b, final double c, final double d) {
        final double denom = Math.sqrt(a * a + b * b + c * c);
        return (a * pointX + b * pointY + c * pointZ + d) / denom;
    }
    
    public static double distancePointPlane(final double pointX, final double pointY, final double pointZ, final double v0X, final double v0Y, final double v0Z, final double v1X, final double v1Y, final double v1Z, final double v2X, final double v2Y, final double v2Z) {
        final double v1Y0Y = v1Y - v0Y;
        final double v2Z0Z = v2Z - v0Z;
        final double v2Y0Y = v2Y - v0Y;
        final double v1Z0Z = v1Z - v0Z;
        final double v2X0X = v2X - v0X;
        final double v1X0X = v1X - v0X;
        final double a = v1Y0Y * v2Z0Z - v2Y0Y * v1Z0Z;
        final double b = v1Z0Z * v2X0X - v2Z0Z * v1X0X;
        final double c = v1X0X * v2Y0Y - v2X0X * v1Y0Y;
        final double d = -(a * v0X + b * v0Y + c * v0Z);
        return distancePointPlane(pointX, pointY, pointZ, a, b, c, d);
    }
    
    public static double intersectRayPlane(final double originX, final double originY, final double originZ, final double dirX, final double dirY, final double dirZ, final double pointX, final double pointY, final double pointZ, final double normalX, final double normalY, final double normalZ, final double epsilon) {
        final double denom = normalX * dirX + normalY * dirY + normalZ * dirZ;
        if (denom < epsilon) {
            final double t = ((pointX - originX) * normalX + (pointY - originY) * normalY + (pointZ - originZ) * normalZ) / denom;
            if (t >= 0.0) {
                return t;
            }
        }
        return -1.0;
    }
    
    public static double intersectRayPlane(final Vector3dc origin, final Vector3dc dir, final Vector3dc point, final Vector3dc normal, final double epsilon) {
        return intersectRayPlane(origin.x(), origin.y(), origin.z(), dir.x(), dir.y(), dir.z(), point.x(), point.y(), point.z(), normal.x(), normal.y(), normal.z(), epsilon);
    }
    
    public static double intersectRayPlane(final double originX, final double originY, final double originZ, final double dirX, final double dirY, final double dirZ, final double a, final double b, final double c, final double d, final double epsilon) {
        final double denom = a * dirX + b * dirY + c * dirZ;
        if (denom < 0.0) {
            final double t = -(a * originX + b * originY + c * originZ + d) / denom;
            if (t >= 0.0) {
                return t;
            }
        }
        return -1.0;
    }
    
    public static boolean testAabSphere(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ, final double centerX, final double centerY, final double centerZ, final double radiusSquared) {
        double radius2 = radiusSquared;
        if (centerX < minX) {
            final double d = centerX - minX;
            radius2 -= d * d;
        }
        else if (centerX > maxX) {
            final double d = centerX - maxX;
            radius2 -= d * d;
        }
        if (centerY < minY) {
            final double d = centerY - minY;
            radius2 -= d * d;
        }
        else if (centerY > maxY) {
            final double d = centerY - maxY;
            radius2 -= d * d;
        }
        if (centerZ < minZ) {
            final double d = centerZ - minZ;
            radius2 -= d * d;
        }
        else if (centerZ > maxZ) {
            final double d = centerZ - maxZ;
            radius2 -= d * d;
        }
        return radius2 >= 0.0;
    }
    
    public static boolean testAabSphere(final Vector3dc min, final Vector3dc max, final Vector3dc center, final double radiusSquared) {
        return testAabSphere(min.x(), min.y(), min.z(), max.x(), max.y(), max.z(), center.x(), center.y(), center.z(), radiusSquared);
    }
    
    public static Vector3d findClosestPointOnPlane(final double aX, final double aY, final double aZ, final double nX, final double nY, final double nZ, final double pX, final double pY, final double pZ, final Vector3d result) {
        final double d = -(nX * aX + nY * aY + nZ * aZ);
        final double t = nX * pX + nY * pY + nZ * pZ - d;
        result.x = pX - t * nX;
        result.y = pY - t * nY;
        result.z = pZ - t * nZ;
        return result;
    }
    
    public static Vector3d findClosestPointOnLineSegment(final double aX, final double aY, final double aZ, final double bX, final double bY, final double bZ, final double pX, final double pY, final double pZ, final Vector3d result) {
        final double abX = bX - aX;
        final double abY = bY - aY;
        final double abZ = bZ - aZ;
        double t = ((pX - aX) * abX + (pY - aY) * abY + (pZ - aZ) * abZ) / (abX * abX + abY * abY + abZ * abZ);
        if (t < 0.0) {
            t = 0.0;
        }
        if (t > 1.0) {
            t = 1.0;
        }
        result.x = aX + t * abX;
        result.y = aY + t * abY;
        result.z = aZ + t * abZ;
        return result;
    }
    
    public static double findClosestPointsLineSegments(final double a0X, final double a0Y, final double a0Z, final double a1X, final double a1Y, final double a1Z, final double b0X, final double b0Y, final double b0Z, final double b1X, final double b1Y, final double b1Z, final Vector3d resultA, final Vector3d resultB) {
        final double d1x = a1X - a0X;
        final double d1y = a1Y - a0Y;
        final double d1z = a1Z - a0Z;
        final double d2x = b1X - b0X;
        final double d2y = b1Y - b0Y;
        final double d2z = b1Z - b0Z;
        final double rX = a0X - b0X;
        final double rY = a0Y - b0Y;
        final double rZ = a0Z - b0Z;
        final double a = d1x * d1x + d1y * d1y + d1z * d1z;
        final double e = d2x * d2x + d2y * d2y + d2z * d2z;
        final double f = d2x * rX + d2y * rY + d2z * rZ;
        final double EPSILON = 1.0E-8;
        if (a <= EPSILON && e <= EPSILON) {
            resultA.set(a0X, a0Y, a0Z);
            resultB.set(b0X, b0Y, b0Z);
            return resultA.dot(resultB);
        }
        double s;
        double t;
        if (a <= EPSILON) {
            s = 0.0;
            t = f / e;
            t = Math.min(Math.max(t, 0.0), 1.0);
        }
        else {
            final double c = d1x * rX + d1y * rY + d1z * rZ;
            if (e <= EPSILON) {
                t = 0.0;
                s = Math.min(Math.max(-c / a, 0.0), 1.0);
            }
            else {
                final double b = d1x * d2x + d1y * d2y + d1z * d2z;
                final double denom = a * e - b * b;
                if (denom != 0.0) {
                    s = Math.min(Math.max((b * f - c * e) / denom, 0.0), 1.0);
                }
                else {
                    s = 0.0;
                }
                t = (b * s + f) / e;
                if (t < 0.0) {
                    t = 0.0;
                    s = Math.min(Math.max(-c / a, 0.0), 1.0);
                }
                else if (t > 1.0) {
                    t = 1.0;
                    s = Math.min(Math.max((b - c) / a, 0.0), 1.0);
                }
            }
        }
        resultA.set(a0X + d1x * s, a0Y + d1y * s, a0Z + d1z * s);
        resultB.set(b0X + d2x * t, b0Y + d2y * t, b0Z + d2z * t);
        final double dX = resultA.x - resultB.x;
        final double dY = resultA.y - resultB.y;
        final double dZ = resultA.z - resultB.z;
        return dX * dX + dY * dY + dZ * dZ;
    }
    
    public static double findClosestPointsLineSegmentTriangle(final double aX, final double aY, final double aZ, final double bX, final double bY, final double bZ, final double v0X, final double v0Y, final double v0Z, final double v1X, final double v1Y, final double v1Z, final double v2X, final double v2Y, final double v2Z, final Vector3d lineSegmentResult, final Vector3d triangleResult) {
        double min;
        double d = min = findClosestPointsLineSegments(aX, aY, aZ, bX, bY, bZ, v0X, v0Y, v0Z, v1X, v1Y, v1Z, lineSegmentResult, triangleResult);
        double minlsX = lineSegmentResult.x;
        double minlsY = lineSegmentResult.y;
        double minlsZ = lineSegmentResult.z;
        double mintX = triangleResult.x;
        double mintY = triangleResult.y;
        double mintZ = triangleResult.z;
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
        double a = Double.NaN;
        double b = Double.NaN;
        double c = Double.NaN;
        double nd = Double.NaN;
        if (testPointInTriangle(aX, aY, aZ, v0X, v0Y, v0Z, v1X, v1Y, v1Z, v2X, v2Y, v2Z)) {
            final double v1Y0Y = v1Y - v0Y;
            final double v2Z0Z = v2Z - v0Z;
            final double v2Y0Y = v2Y - v0Y;
            final double v1Z0Z = v1Z - v0Z;
            final double v2X0X = v2X - v0X;
            final double v1X0X = v1X - v0X;
            a = v1Y0Y * v2Z0Z - v2Y0Y * v1Z0Z;
            b = v1Z0Z * v2X0X - v2Z0Z * v1X0X;
            c = v1X0X * v2Y0Y - v2X0X * v1Y0Y;
            computed = true;
            final double invLen = Math.invsqrt(a * a + b * b + c * c);
            a *= invLen;
            b *= invLen;
            c *= invLen;
            nd = -(a * v0X + b * v0Y + c * v0Z);
            final double l;
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
                final double v1Y0Y = v1Y - v0Y;
                final double v2Z0Z = v2Z - v0Z;
                final double v2Y0Y = v2Y - v0Y;
                final double v1Z0Z = v1Z - v0Z;
                final double v2X0X = v2X - v0X;
                final double v1X0X = v1X - v0X;
                a = v1Y0Y * v2Z0Z - v2Y0Y * v1Z0Z;
                b = v1Z0Z * v2X0X - v2Z0Z * v1X0X;
                c = v1X0X * v2Y0Y - v2X0X * v1Y0Y;
                final double invLen = Math.invsqrt(a * a + b * b + c * c);
                a *= invLen;
                b *= invLen;
                c *= invLen;
                nd = -(a * v0X + b * v0Y + c * v0Z);
            }
            final double i;
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
    
    public static int findClosestPointOnTriangle(final double v0X, final double v0Y, final double v0Z, final double v1X, final double v1Y, final double v1Z, final double v2X, final double v2Y, final double v2Z, final double pX, final double pY, final double pZ, final Vector3d result) {
        final double abX = v1X - v0X;
        final double abY = v1Y - v0Y;
        final double abZ = v1Z - v0Z;
        final double acX = v2X - v0X;
        final double acY = v2Y - v0Y;
        final double acZ = v2Z - v0Z;
        final double apX = pX - v0X;
        final double apY = pY - v0Y;
        final double apZ = pZ - v0Z;
        final double d1 = abX * apX + abY * apY + abZ * apZ;
        final double d2 = acX * apX + acY * apY + acZ * apZ;
        if (d1 <= 0.0 && d2 <= 0.0) {
            result.x = v0X;
            result.y = v0Y;
            result.z = v0Z;
            return 1;
        }
        final double bpX = pX - v1X;
        final double bpY = pY - v1Y;
        final double bpZ = pZ - v1Z;
        final double d3 = abX * bpX + abY * bpY + abZ * bpZ;
        final double d4 = acX * bpX + acY * bpY + acZ * bpZ;
        if (d3 >= 0.0 && d4 <= d3) {
            result.x = v1X;
            result.y = v1Y;
            result.z = v1Z;
            return 2;
        }
        final double vc = d1 * d4 - d3 * d2;
        if (vc <= 0.0 && d1 >= 0.0 && d3 <= 0.0) {
            final double v = d1 / (d1 - d3);
            result.x = v0X + v * abX;
            result.y = v0Y + v * abY;
            result.z = v0Z + v * abZ;
            return 4;
        }
        final double cpX = pX - v2X;
        final double cpY = pY - v2Y;
        final double cpZ = pZ - v2Z;
        final double d5 = abX * cpX + abY * cpY + abZ * cpZ;
        final double d6 = acX * cpX + acY * cpY + acZ * cpZ;
        if (d6 >= 0.0 && d5 <= d6) {
            result.x = v2X;
            result.y = v2Y;
            result.z = v2Z;
            return 3;
        }
        final double vb = d5 * d2 - d1 * d6;
        if (vb <= 0.0 && d2 >= 0.0 && d6 <= 0.0) {
            final double w = d2 / (d2 - d6);
            result.x = v0X + w * acX;
            result.y = v0Y + w * acY;
            result.z = v0Z + w * acZ;
            return 6;
        }
        final double va = d3 * d6 - d5 * d4;
        if (va <= 0.0 && d4 - d3 >= 0.0 && d5 - d6 >= 0.0) {
            final double w2 = (d4 - d3) / (d4 - d3 + d5 - d6);
            result.x = v1X + w2 * (v2X - v1X);
            result.y = v1Y + w2 * (v2Y - v1Y);
            result.z = v1Z + w2 * (v2Z - v1Z);
            return 5;
        }
        final double denom = 1.0 / (va + vb + vc);
        final double v2 = vb * denom;
        final double w3 = vc * denom;
        result.x = v0X + abX * v2 + acX * w3;
        result.y = v0Y + abY * v2 + acY * w3;
        result.z = v0Z + abZ * v2 + acZ * w3;
        return 7;
    }
    
    public static int findClosestPointOnTriangle(final Vector3dc v0, final Vector3dc v1, final Vector3dc v2, final Vector3dc p, final Vector3d result) {
        return findClosestPointOnTriangle(v0.x(), v0.y(), v0.z(), v1.x(), v1.y(), v1.z(), v2.x(), v2.y(), v2.z(), p.x(), p.y(), p.z(), result);
    }
    
    public static Vector3d findClosestPointOnRectangle(final double aX, final double aY, final double aZ, final double bX, final double bY, final double bZ, final double cX, final double cY, final double cZ, final double pX, final double pY, final double pZ, final Vector3d res) {
        final double abX = bX - aX;
        final double abY = bY - aY;
        final double abZ = bZ - aZ;
        final double acX = cX - aX;
        final double acY = cY - aY;
        final double acZ = cZ - aZ;
        final double dX = pX - aX;
        final double dY = pY - aY;
        final double dZ = pZ - aZ;
        double qX = aX;
        double qY = aY;
        double qZ = aZ;
        double dist = dX * abX + dY * abY + dZ * abZ;
        double maxdist = abX * abX + abY * abY + abZ * abZ;
        if (dist >= maxdist) {
            qX += abX;
            qY += abY;
            qZ += abZ;
        }
        else if (dist > 0.0) {
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
        else if (dist > 0.0) {
            qX += dist / maxdist * acX;
            qY += dist / maxdist * acY;
            qZ += dist / maxdist * acZ;
        }
        res.x = qX;
        res.y = qY;
        res.z = qZ;
        return res;
    }
    
    public static int intersectSweptSphereTriangle(final double centerX, final double centerY, final double centerZ, final double radius, final double velX, final double velY, final double velZ, final double v0X, final double v0Y, final double v0Z, final double v1X, final double v1Y, final double v1Z, final double v2X, final double v2Y, final double v2Z, final double epsilon, final double maxT, final Vector4d pointAndTime) {
        final double v10X = v1X - v0X;
        final double v10Y = v1Y - v0Y;
        final double v10Z = v1Z - v0Z;
        final double v20X = v2X - v0X;
        final double v20Y = v2Y - v0Y;
        final double v20Z = v2Z - v0Z;
        final double a = v10Y * v20Z - v20Y * v10Z;
        final double b = v10Z * v20X - v20Z * v10X;
        final double c = v10X * v20Y - v20X * v10Y;
        final double d = -(a * v0X + b * v0Y + c * v0Z);
        final double invLen = Math.invsqrt(a * a + b * b + c * c);
        final double signedDist = (a * centerX + b * centerY + c * centerZ + d) * invLen;
        final double dot = (a * velX + b * velY + c * velZ) * invLen;
        if (dot < epsilon && dot > -epsilon) {
            return 0;
        }
        final double pt0 = (radius - signedDist) / dot;
        if (pt0 > maxT) {
            return 0;
        }
        final double pt2 = (-radius - signedDist) / dot;
        final double p0X = centerX - radius * a * invLen + velX * pt0;
        final double p0Y = centerY - radius * b * invLen + velY * pt0;
        final double p0Z = centerZ - radius * c * invLen + velZ * pt0;
        final boolean insideTriangle = testPointInTriangle(p0X, p0Y, p0Z, v0X, v0Y, v0Z, v1X, v1Y, v1Z, v2X, v2Y, v2Z);
        if (insideTriangle) {
            pointAndTime.x = p0X;
            pointAndTime.y = p0Y;
            pointAndTime.z = p0Z;
            pointAndTime.w = pt0;
            return 7;
        }
        int isect = 0;
        double t0 = maxT;
        final double A = velX * velX + velY * velY + velZ * velZ;
        final double radius2 = radius * radius;
        final double centerV0X = centerX - v0X;
        final double centerV0Y = centerY - v0Y;
        final double centerV0Z = centerZ - v0Z;
        final double B0 = 2.0 * (velX * centerV0X + velY * centerV0Y + velZ * centerV0Z);
        final double C0 = centerV0X * centerV0X + centerV0Y * centerV0Y + centerV0Z * centerV0Z - radius2;
        final double root0 = computeLowestRoot(A, B0, C0, t0);
        if (root0 < t0) {
            pointAndTime.x = v0X;
            pointAndTime.y = v0Y;
            pointAndTime.z = v0Z;
            pointAndTime.w = root0;
            t0 = root0;
            isect = 1;
        }
        final double centerV1X = centerX - v1X;
        final double centerV1Y = centerY - v1Y;
        final double centerV1Z = centerZ - v1Z;
        final double centerV1Len = centerV1X * centerV1X + centerV1Y * centerV1Y + centerV1Z * centerV1Z;
        final double B2 = 2.0 * (velX * centerV1X + velY * centerV1Y + velZ * centerV1Z);
        final double C2 = centerV1Len - radius2;
        final double root2 = computeLowestRoot(A, B2, C2, t0);
        if (root2 < t0) {
            pointAndTime.x = v1X;
            pointAndTime.y = v1Y;
            pointAndTime.z = v1Z;
            pointAndTime.w = root2;
            t0 = root2;
            isect = 2;
        }
        final double centerV2X = centerX - v2X;
        final double centerV2Y = centerY - v2Y;
        final double centerV2Z = centerZ - v2Z;
        final double B3 = 2.0 * (velX * centerV2X + velY * centerV2Y + velZ * centerV2Z);
        final double C3 = centerV2X * centerV2X + centerV2Y * centerV2Y + centerV2Z * centerV2Z - radius2;
        final double root3 = computeLowestRoot(A, B3, C3, t0);
        if (root3 < t0) {
            pointAndTime.x = v2X;
            pointAndTime.y = v2Y;
            pointAndTime.z = v2Z;
            pointAndTime.w = root3;
            t0 = root3;
            isect = 3;
        }
        final double velLen = velX * velX + velY * velY + velZ * velZ;
        final double len10 = v10X * v10X + v10Y * v10Y + v10Z * v10Z;
        final double baseTo0Len = centerV0X * centerV0X + centerV0Y * centerV0Y + centerV0Z * centerV0Z;
        final double v10Vel = v10X * velX + v10Y * velY + v10Z * velZ;
        final double A2 = len10 * -velLen + v10Vel * v10Vel;
        final double v10BaseTo0 = v10X * -centerV0X + v10Y * -centerV0Y + v10Z * -centerV0Z;
        final double velBaseTo0 = velX * -centerV0X + velY * -centerV0Y + velZ * -centerV0Z;
        final double B4 = len10 * 2.0 * velBaseTo0 - 2.0 * v10Vel * v10BaseTo0;
        final double C4 = len10 * (radius2 - baseTo0Len) + v10BaseTo0 * v10BaseTo0;
        final double root4 = computeLowestRoot(A2, B4, C4, t0);
        final double f10 = (v10Vel * root4 - v10BaseTo0) / len10;
        if (f10 >= 0.0 && f10 <= 1.0 && root4 < t0) {
            pointAndTime.x = v0X + f10 * v10X;
            pointAndTime.y = v0Y + f10 * v10Y;
            pointAndTime.z = v0Z + f10 * v10Z;
            pointAndTime.w = root4;
            t0 = root4;
            isect = 4;
        }
        final double len11 = v20X * v20X + v20Y * v20Y + v20Z * v20Z;
        final double v20Vel = v20X * velX + v20Y * velY + v20Z * velZ;
        final double A3 = len11 * -velLen + v20Vel * v20Vel;
        final double v20BaseTo0 = v20X * -centerV0X + v20Y * -centerV0Y + v20Z * -centerV0Z;
        final double B5 = len11 * 2.0 * velBaseTo0 - 2.0 * v20Vel * v20BaseTo0;
        final double C5 = len11 * (radius2 - baseTo0Len) + v20BaseTo0 * v20BaseTo0;
        final double root5 = computeLowestRoot(A3, B5, C5, t0);
        final double f11 = (v20Vel * root5 - v20BaseTo0) / len11;
        if (f11 >= 0.0 && f11 <= 1.0 && root5 < pt2) {
            pointAndTime.x = v0X + f11 * v20X;
            pointAndTime.y = v0Y + f11 * v20Y;
            pointAndTime.z = v0Z + f11 * v20Z;
            pointAndTime.w = root5;
            t0 = root5;
            isect = 6;
        }
        final double v21X = v2X - v1X;
        final double v21Y = v2Y - v1Y;
        final double v21Z = v2Z - v1Z;
        final double len12 = v21X * v21X + v21Y * v21Y + v21Z * v21Z;
        final double baseTo1Len = centerV1Len;
        final double v21Vel = v21X * velX + v21Y * velY + v21Z * velZ;
        final double A4 = len12 * -velLen + v21Vel * v21Vel;
        final double v21BaseTo1 = v21X * -centerV1X + v21Y * -centerV1Y + v21Z * -centerV1Z;
        final double velBaseTo2 = velX * -centerV1X + velY * -centerV1Y + velZ * -centerV1Z;
        final double B6 = len12 * 2.0 * velBaseTo2 - 2.0 * v21Vel * v21BaseTo1;
        final double C6 = len12 * (radius2 - baseTo1Len) + v21BaseTo1 * v21BaseTo1;
        final double root6 = computeLowestRoot(A4, B6, C6, t0);
        final double f12 = (v21Vel * root6 - v21BaseTo1) / len12;
        if (f12 >= 0.0 && f12 <= 1.0 && root6 < t0) {
            pointAndTime.x = v1X + f12 * v21X;
            pointAndTime.y = v1Y + f12 * v21Y;
            pointAndTime.z = v1Z + f12 * v21Z;
            pointAndTime.w = root6;
            t0 = root6;
            isect = 5;
        }
        return isect;
    }
    
    private static double computeLowestRoot(final double a, final double b, final double c, final double maxR) {
        final double determinant = b * b - 4.0 * a * c;
        if (determinant < 0.0) {
            return Double.POSITIVE_INFINITY;
        }
        final double sqrtD = Math.sqrt(determinant);
        double r1 = (-b - sqrtD) / (2.0 * a);
        double r2 = (-b + sqrtD) / (2.0 * a);
        if (r1 > r2) {
            final double temp = r2;
            r2 = r1;
            r1 = temp;
        }
        if (r1 > 0.0 && r1 < maxR) {
            return r1;
        }
        if (r2 > 0.0 && r2 < maxR) {
            return r2;
        }
        return Double.POSITIVE_INFINITY;
    }
    
    public static boolean testPointInTriangle(final double pX, final double pY, final double pZ, final double v0X, final double v0Y, final double v0Z, final double v1X, final double v1Y, final double v1Z, final double v2X, final double v2Y, final double v2Z) {
        final double e10X = v1X - v0X;
        final double e10Y = v1Y - v0Y;
        final double e10Z = v1Z - v0Z;
        final double e20X = v2X - v0X;
        final double e20Y = v2Y - v0Y;
        final double e20Z = v2Z - v0Z;
        final double a = e10X * e10X + e10Y * e10Y + e10Z * e10Z;
        final double b = e10X * e20X + e10Y * e20Y + e10Z * e20Z;
        final double c = e20X * e20X + e20Y * e20Y + e20Z * e20Z;
        final double ac_bb = a * c - b * b;
        final double vpX = pX - v0X;
        final double vpY = pY - v0Y;
        final double vpZ = pZ - v0Z;
        final double d = vpX * e10X + vpY * e10Y + vpZ * e10Z;
        final double e = vpX * e20X + vpY * e20Y + vpZ * e20Z;
        final double x = d * c - e * b;
        final double y = e * a - d * b;
        final double z = x + y - ac_bb;
        return (Runtime.doubleToLongBits(z) & ~(Runtime.doubleToLongBits(x) | Runtime.doubleToLongBits(y)) & Long.MIN_VALUE) != 0x0L;
    }
    
    public static boolean intersectRaySphere(final double originX, final double originY, final double originZ, final double dirX, final double dirY, final double dirZ, final double centerX, final double centerY, final double centerZ, final double radiusSquared, final Vector2d result) {
        final double Lx = centerX - originX;
        final double Ly = centerY - originY;
        final double Lz = centerZ - originZ;
        final double tca = Lx * dirX + Ly * dirY + Lz * dirZ;
        final double d2 = Lx * Lx + Ly * Ly + Lz * Lz - tca * tca;
        if (d2 > radiusSquared) {
            return false;
        }
        final double thc = Math.sqrt(radiusSquared - d2);
        final double t0 = tca - thc;
        final double t2 = tca + thc;
        if (t0 < t2 && t2 >= 0.0) {
            result.x = t0;
            result.y = t2;
            return true;
        }
        return false;
    }
    
    public static boolean intersectRaySphere(final Vector3dc origin, final Vector3dc dir, final Vector3dc center, final double radiusSquared, final Vector2d result) {
        return intersectRaySphere(origin.x(), origin.y(), origin.z(), dir.x(), dir.y(), dir.z(), center.x(), center.y(), center.z(), radiusSquared, result);
    }
    
    public static boolean testRaySphere(final double originX, final double originY, final double originZ, final double dirX, final double dirY, final double dirZ, final double centerX, final double centerY, final double centerZ, final double radiusSquared) {
        final double Lx = centerX - originX;
        final double Ly = centerY - originY;
        final double Lz = centerZ - originZ;
        final double tca = Lx * dirX + Ly * dirY + Lz * dirZ;
        final double d2 = Lx * Lx + Ly * Ly + Lz * Lz - tca * tca;
        if (d2 > radiusSquared) {
            return false;
        }
        final double thc = Math.sqrt(radiusSquared - d2);
        final double t0 = tca - thc;
        final double t2 = tca + thc;
        return t0 < t2 && t2 >= 0.0;
    }
    
    public static boolean testRaySphere(final Vector3dc origin, final Vector3dc dir, final Vector3dc center, final double radiusSquared) {
        return testRaySphere(origin.x(), origin.y(), origin.z(), dir.x(), dir.y(), dir.z(), center.x(), center.y(), center.z(), radiusSquared);
    }
    
    public static boolean testLineSegmentSphere(final double p0X, final double p0Y, final double p0Z, final double p1X, final double p1Y, final double p1Z, final double centerX, final double centerY, final double centerZ, final double radiusSquared) {
        double dX = p1X - p0X;
        double dY = p1Y - p0Y;
        double dZ = p1Z - p0Z;
        final double nom = (centerX - p0X) * dX + (centerY - p0Y) * dY + (centerZ - p0Z) * dZ;
        final double den = dX * dX + dY * dY + dZ * dZ;
        final double u = nom / den;
        if (u < 0.0) {
            dX = p0X - centerX;
            dY = p0Y - centerY;
            dZ = p0Z - centerZ;
        }
        else if (u > 1.0) {
            dX = p1X - centerX;
            dY = p1Y - centerY;
            dZ = p1Z - centerZ;
        }
        else {
            final double pX = p0X + u * dX;
            final double pY = p0Y + u * dY;
            final double pZ = p0Z + u * dZ;
            dX = pX - centerX;
            dY = pY - centerY;
            dZ = pZ - centerZ;
        }
        final double dist = dX * dX + dY * dY + dZ * dZ;
        return dist <= radiusSquared;
    }
    
    public static boolean testLineSegmentSphere(final Vector3dc p0, final Vector3dc p1, final Vector3dc center, final double radiusSquared) {
        return testLineSegmentSphere(p0.x(), p0.y(), p0.z(), p1.x(), p1.y(), p1.z(), center.x(), center.y(), center.z(), radiusSquared);
    }
    
    public static boolean intersectRayAab(final double originX, final double originY, final double originZ, final double dirX, final double dirY, final double dirZ, final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ, final Vector2d result) {
        final double invDirX = 1.0 / dirX;
        final double invDirY = 1.0 / dirY;
        final double invDirZ = 1.0 / dirZ;
        double tNear;
        double tFar;
        if (invDirX >= 0.0) {
            tNear = (minX - originX) * invDirX;
            tFar = (maxX - originX) * invDirX;
        }
        else {
            tNear = (maxX - originX) * invDirX;
            tFar = (minX - originX) * invDirX;
        }
        double tymin;
        double tymax;
        if (invDirY >= 0.0) {
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
        double tzmin;
        double tzmax;
        if (invDirZ >= 0.0) {
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
        tNear = ((tymin > tNear || Double.isNaN(tNear)) ? tymin : tNear);
        tFar = ((tymax < tFar || Double.isNaN(tFar)) ? tymax : tFar);
        tNear = ((tzmin > tNear) ? tzmin : tNear);
        tFar = ((tzmax < tFar) ? tzmax : tFar);
        if (tNear < tFar && tFar >= 0.0) {
            result.x = tNear;
            result.y = tFar;
            return true;
        }
        return false;
    }
    
    public static boolean intersectRayAab(final Vector3dc origin, final Vector3dc dir, final Vector3dc min, final Vector3dc max, final Vector2d result) {
        return intersectRayAab(origin.x(), origin.y(), origin.z(), dir.x(), dir.y(), dir.z(), min.x(), min.y(), min.z(), max.x(), max.y(), max.z(), result);
    }
    
    public static int intersectLineSegmentAab(final double p0X, final double p0Y, final double p0Z, final double p1X, final double p1Y, final double p1Z, final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ, final Vector2d result) {
        final double dirX = p1X - p0X;
        final double dirY = p1Y - p0Y;
        final double dirZ = p1Z - p0Z;
        final double invDirX = 1.0 / dirX;
        final double invDirY = 1.0 / dirY;
        final double invDirZ = 1.0 / dirZ;
        double tNear;
        double tFar;
        if (invDirX >= 0.0) {
            tNear = (minX - p0X) * invDirX;
            tFar = (maxX - p0X) * invDirX;
        }
        else {
            tNear = (maxX - p0X) * invDirX;
            tFar = (minX - p0X) * invDirX;
        }
        double tymin;
        double tymax;
        if (invDirY >= 0.0) {
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
        double tzmin;
        double tzmax;
        if (invDirZ >= 0.0) {
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
        tNear = ((tymin > tNear || Double.isNaN(tNear)) ? tymin : tNear);
        tFar = ((tymax < tFar || Double.isNaN(tFar)) ? tymax : tFar);
        tNear = ((tzmin > tNear) ? tzmin : tNear);
        tFar = ((tzmax < tFar) ? tzmax : tFar);
        int type = -1;
        if (tNear <= tFar && tNear <= 1.0 && tFar >= 0.0) {
            if (tNear >= 0.0 && tFar > 1.0) {
                tFar = tNear;
                type = 1;
            }
            else if (tNear < 0.0 && tFar <= 1.0) {
                tNear = tFar;
                type = 1;
            }
            else if (tNear < 0.0 && tFar > 1.0) {
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
    
    public static int intersectLineSegmentAab(final Vector3dc p0, final Vector3dc p1, final Vector3dc min, final Vector3dc max, final Vector2d result) {
        return intersectLineSegmentAab(p0.x(), p0.y(), p0.z(), p1.x(), p1.y(), p1.z(), min.x(), min.y(), min.z(), max.x(), max.y(), max.z(), result);
    }
    
    public static boolean testRayAab(final double originX, final double originY, final double originZ, final double dirX, final double dirY, final double dirZ, final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ) {
        final double invDirX = 1.0 / dirX;
        final double invDirY = 1.0 / dirY;
        final double invDirZ = 1.0 / dirZ;
        double tNear;
        double tFar;
        if (invDirX >= 0.0) {
            tNear = (minX - originX) * invDirX;
            tFar = (maxX - originX) * invDirX;
        }
        else {
            tNear = (maxX - originX) * invDirX;
            tFar = (minX - originX) * invDirX;
        }
        double tymin;
        double tymax;
        if (invDirY >= 0.0) {
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
        double tzmin;
        double tzmax;
        if (invDirZ >= 0.0) {
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
        tNear = ((tymin > tNear || Double.isNaN(tNear)) ? tymin : tNear);
        tFar = ((tymax < tFar || Double.isNaN(tFar)) ? tymax : tFar);
        tNear = ((tzmin > tNear) ? tzmin : tNear);
        tFar = ((tzmax < tFar) ? tzmax : tFar);
        return tNear < tFar && tFar >= 0.0;
    }
    
    public static boolean testRayAab(final Vector3dc origin, final Vector3dc dir, final Vector3dc min, final Vector3dc max) {
        return testRayAab(origin.x(), origin.y(), origin.z(), dir.x(), dir.y(), dir.z(), min.x(), min.y(), min.z(), max.x(), max.y(), max.z());
    }
    
    public static boolean testRayTriangleFront(final double originX, final double originY, final double originZ, final double dirX, final double dirY, final double dirZ, final double v0X, final double v0Y, final double v0Z, final double v1X, final double v1Y, final double v1Z, final double v2X, final double v2Y, final double v2Z, final double epsilon) {
        final double edge1X = v1X - v0X;
        final double edge1Y = v1Y - v0Y;
        final double edge1Z = v1Z - v0Z;
        final double edge2X = v2X - v0X;
        final double edge2Y = v2Y - v0Y;
        final double edge2Z = v2Z - v0Z;
        final double pvecX = dirY * edge2Z - dirZ * edge2Y;
        final double pvecY = dirZ * edge2X - dirX * edge2Z;
        final double pvecZ = dirX * edge2Y - dirY * edge2X;
        final double det = edge1X * pvecX + edge1Y * pvecY + edge1Z * pvecZ;
        if (det < epsilon) {
            return false;
        }
        final double tvecX = originX - v0X;
        final double tvecY = originY - v0Y;
        final double tvecZ = originZ - v0Z;
        final double u = tvecX * pvecX + tvecY * pvecY + tvecZ * pvecZ;
        if (u < 0.0 || u > det) {
            return false;
        }
        final double qvecX = tvecY * edge1Z - tvecZ * edge1Y;
        final double qvecY = tvecZ * edge1X - tvecX * edge1Z;
        final double qvecZ = tvecX * edge1Y - tvecY * edge1X;
        final double v = dirX * qvecX + dirY * qvecY + dirZ * qvecZ;
        if (v < 0.0 || u + v > det) {
            return false;
        }
        final double invDet = 1.0 / det;
        final double t = (edge2X * qvecX + edge2Y * qvecY + edge2Z * qvecZ) * invDet;
        return t >= epsilon;
    }
    
    public static boolean testRayTriangleFront(final Vector3dc origin, final Vector3dc dir, final Vector3dc v0, final Vector3dc v1, final Vector3dc v2, final double epsilon) {
        return testRayTriangleFront(origin.x(), origin.y(), origin.z(), dir.x(), dir.y(), dir.z(), v0.x(), v0.y(), v0.z(), v1.x(), v1.y(), v1.z(), v2.x(), v2.y(), v2.z(), epsilon);
    }
    
    public static boolean testRayTriangle(final double originX, final double originY, final double originZ, final double dirX, final double dirY, final double dirZ, final double v0X, final double v0Y, final double v0Z, final double v1X, final double v1Y, final double v1Z, final double v2X, final double v2Y, final double v2Z, final double epsilon) {
        final double edge1X = v1X - v0X;
        final double edge1Y = v1Y - v0Y;
        final double edge1Z = v1Z - v0Z;
        final double edge2X = v2X - v0X;
        final double edge2Y = v2Y - v0Y;
        final double edge2Z = v2Z - v0Z;
        final double pvecX = dirY * edge2Z - dirZ * edge2Y;
        final double pvecY = dirZ * edge2X - dirX * edge2Z;
        final double pvecZ = dirX * edge2Y - dirY * edge2X;
        final double det = edge1X * pvecX + edge1Y * pvecY + edge1Z * pvecZ;
        if (det > -epsilon && det < epsilon) {
            return false;
        }
        final double tvecX = originX - v0X;
        final double tvecY = originY - v0Y;
        final double tvecZ = originZ - v0Z;
        final double invDet = 1.0 / det;
        final double u = (tvecX * pvecX + tvecY * pvecY + tvecZ * pvecZ) * invDet;
        if (u < 0.0 || u > 1.0) {
            return false;
        }
        final double qvecX = tvecY * edge1Z - tvecZ * edge1Y;
        final double qvecY = tvecZ * edge1X - tvecX * edge1Z;
        final double qvecZ = tvecX * edge1Y - tvecY * edge1X;
        final double v = (dirX * qvecX + dirY * qvecY + dirZ * qvecZ) * invDet;
        if (v < 0.0 || u + v > 1.0) {
            return false;
        }
        final double t = (edge2X * qvecX + edge2Y * qvecY + edge2Z * qvecZ) * invDet;
        return t >= epsilon;
    }
    
    public static boolean testRayTriangle(final Vector3dc origin, final Vector3dc dir, final Vector3dc v0, final Vector3dc v1, final Vector3dc v2, final double epsilon) {
        return testRayTriangle(origin.x(), origin.y(), origin.z(), dir.x(), dir.y(), dir.z(), v0.x(), v0.y(), v0.z(), v1.x(), v1.y(), v1.z(), v2.x(), v2.y(), v2.z(), epsilon);
    }
    
    public static double intersectRayTriangleFront(final double originX, final double originY, final double originZ, final double dirX, final double dirY, final double dirZ, final double v0X, final double v0Y, final double v0Z, final double v1X, final double v1Y, final double v1Z, final double v2X, final double v2Y, final double v2Z, final double epsilon) {
        final double edge1X = v1X - v0X;
        final double edge1Y = v1Y - v0Y;
        final double edge1Z = v1Z - v0Z;
        final double edge2X = v2X - v0X;
        final double edge2Y = v2Y - v0Y;
        final double edge2Z = v2Z - v0Z;
        final double pvecX = dirY * edge2Z - dirZ * edge2Y;
        final double pvecY = dirZ * edge2X - dirX * edge2Z;
        final double pvecZ = dirX * edge2Y - dirY * edge2X;
        final double det = edge1X * pvecX + edge1Y * pvecY + edge1Z * pvecZ;
        if (det <= epsilon) {
            return -1.0;
        }
        final double tvecX = originX - v0X;
        final double tvecY = originY - v0Y;
        final double tvecZ = originZ - v0Z;
        final double u = tvecX * pvecX + tvecY * pvecY + tvecZ * pvecZ;
        if (u < 0.0 || u > det) {
            return -1.0;
        }
        final double qvecX = tvecY * edge1Z - tvecZ * edge1Y;
        final double qvecY = tvecZ * edge1X - tvecX * edge1Z;
        final double qvecZ = tvecX * edge1Y - tvecY * edge1X;
        final double v = dirX * qvecX + dirY * qvecY + dirZ * qvecZ;
        if (v < 0.0 || u + v > det) {
            return -1.0;
        }
        final double invDet = 1.0 / det;
        final double t = (edge2X * qvecX + edge2Y * qvecY + edge2Z * qvecZ) * invDet;
        return t;
    }
    
    public static double intersectRayTriangleFront(final Vector3dc origin, final Vector3dc dir, final Vector3dc v0, final Vector3dc v1, final Vector3dc v2, final double epsilon) {
        return intersectRayTriangleFront(origin.x(), origin.y(), origin.z(), dir.x(), dir.y(), dir.z(), v0.x(), v0.y(), v0.z(), v1.x(), v1.y(), v1.z(), v2.x(), v2.y(), v2.z(), epsilon);
    }
    
    public static double intersectRayTriangle(final double originX, final double originY, final double originZ, final double dirX, final double dirY, final double dirZ, final double v0X, final double v0Y, final double v0Z, final double v1X, final double v1Y, final double v1Z, final double v2X, final double v2Y, final double v2Z, final double epsilon) {
        final double edge1X = v1X - v0X;
        final double edge1Y = v1Y - v0Y;
        final double edge1Z = v1Z - v0Z;
        final double edge2X = v2X - v0X;
        final double edge2Y = v2Y - v0Y;
        final double edge2Z = v2Z - v0Z;
        final double pvecX = dirY * edge2Z - dirZ * edge2Y;
        final double pvecY = dirZ * edge2X - dirX * edge2Z;
        final double pvecZ = dirX * edge2Y - dirY * edge2X;
        final double det = edge1X * pvecX + edge1Y * pvecY + edge1Z * pvecZ;
        if (det > -epsilon && det < epsilon) {
            return -1.0;
        }
        final double tvecX = originX - v0X;
        final double tvecY = originY - v0Y;
        final double tvecZ = originZ - v0Z;
        final double invDet = 1.0 / det;
        final double u = (tvecX * pvecX + tvecY * pvecY + tvecZ * pvecZ) * invDet;
        if (u < 0.0 || u > 1.0) {
            return -1.0;
        }
        final double qvecX = tvecY * edge1Z - tvecZ * edge1Y;
        final double qvecY = tvecZ * edge1X - tvecX * edge1Z;
        final double qvecZ = tvecX * edge1Y - tvecY * edge1X;
        final double v = (dirX * qvecX + dirY * qvecY + dirZ * qvecZ) * invDet;
        if (v < 0.0 || u + v > 1.0) {
            return -1.0;
        }
        final double t = (edge2X * qvecX + edge2Y * qvecY + edge2Z * qvecZ) * invDet;
        return t;
    }
    
    public static double intersectRayTriangle(final Vector3dc origin, final Vector3dc dir, final Vector3dc v0, final Vector3dc v1, final Vector3dc v2, final double epsilon) {
        return intersectRayTriangle(origin.x(), origin.y(), origin.z(), dir.x(), dir.y(), dir.z(), v0.x(), v0.y(), v0.z(), v1.x(), v1.y(), v1.z(), v2.x(), v2.y(), v2.z(), epsilon);
    }
    
    public static boolean testLineSegmentTriangle(final double p0X, final double p0Y, final double p0Z, final double p1X, final double p1Y, final double p1Z, final double v0X, final double v0Y, final double v0Z, final double v1X, final double v1Y, final double v1Z, final double v2X, final double v2Y, final double v2Z, final double epsilon) {
        final double dirX = p1X - p0X;
        final double dirY = p1Y - p0Y;
        final double dirZ = p1Z - p0Z;
        final double t = intersectRayTriangle(p0X, p0Y, p0Z, dirX, dirY, dirZ, v0X, v0Y, v0Z, v1X, v1Y, v1Z, v2X, v2Y, v2Z, epsilon);
        return t >= 0.0 && t <= 1.0;
    }
    
    public static boolean testLineSegmentTriangle(final Vector3dc p0, final Vector3dc p1, final Vector3dc v0, final Vector3dc v1, final Vector3dc v2, final double epsilon) {
        return testLineSegmentTriangle(p0.x(), p0.y(), p0.z(), p1.x(), p1.y(), p1.z(), v0.x(), v0.y(), v0.z(), v1.x(), v1.y(), v1.z(), v2.x(), v2.y(), v2.z(), epsilon);
    }
    
    public static boolean intersectLineSegmentTriangle(final double p0X, final double p0Y, final double p0Z, final double p1X, final double p1Y, final double p1Z, final double v0X, final double v0Y, final double v0Z, final double v1X, final double v1Y, final double v1Z, final double v2X, final double v2Y, final double v2Z, final double epsilon, final Vector3d intersectionPoint) {
        final double dirX = p1X - p0X;
        final double dirY = p1Y - p0Y;
        final double dirZ = p1Z - p0Z;
        final double t = intersectRayTriangle(p0X, p0Y, p0Z, dirX, dirY, dirZ, v0X, v0Y, v0Z, v1X, v1Y, v1Z, v2X, v2Y, v2Z, epsilon);
        if (t >= 0.0 && t <= 1.0) {
            intersectionPoint.x = p0X + dirX * t;
            intersectionPoint.y = p0Y + dirY * t;
            intersectionPoint.z = p0Z + dirZ * t;
            return true;
        }
        return false;
    }
    
    public static boolean intersectLineSegmentTriangle(final Vector3dc p0, final Vector3dc p1, final Vector3dc v0, final Vector3dc v1, final Vector3dc v2, final double epsilon, final Vector3d intersectionPoint) {
        return intersectLineSegmentTriangle(p0.x(), p0.y(), p0.z(), p1.x(), p1.y(), p1.z(), v0.x(), v0.y(), v0.z(), v1.x(), v1.y(), v1.z(), v2.x(), v2.y(), v2.z(), epsilon, intersectionPoint);
    }
    
    public static boolean intersectLineSegmentPlane(final double p0X, final double p0Y, final double p0Z, final double p1X, final double p1Y, final double p1Z, final double a, final double b, final double c, final double d, final Vector3d intersectionPoint) {
        final double dirX = p1X - p0X;
        final double dirY = p1Y - p0Y;
        final double dirZ = p1Z - p0Z;
        final double denom = a * dirX + b * dirY + c * dirZ;
        final double t = -(a * p0X + b * p0Y + c * p0Z + d) / denom;
        if (t >= 0.0 && t <= 1.0) {
            intersectionPoint.x = p0X + t * dirX;
            intersectionPoint.y = p0Y + t * dirY;
            intersectionPoint.z = p0Z + t * dirZ;
            return true;
        }
        return false;
    }
    
    public static boolean testLineCircle(final double a, final double b, final double c, final double centerX, final double centerY, final double radius) {
        final double denom = Math.sqrt(a * a + b * b);
        final double dist = (a * centerX + b * centerY + c) / denom;
        return -radius <= dist && dist <= radius;
    }
    
    public static boolean intersectLineCircle(final double a, final double b, final double c, final double centerX, final double centerY, final double radius, final Vector3d intersectionCenterAndHL) {
        final double invDenom = Math.invsqrt(a * a + b * b);
        final double dist = (a * centerX + b * centerY + c) * invDenom;
        if (-radius <= dist && dist <= radius) {
            intersectionCenterAndHL.x = centerX + dist * a * invDenom;
            intersectionCenterAndHL.y = centerY + dist * b * invDenom;
            intersectionCenterAndHL.z = Math.sqrt(radius * radius - dist * dist);
            return true;
        }
        return false;
    }
    
    public static boolean intersectLineCircle(final double x0, final double y0, final double x1, final double y1, final double centerX, final double centerY, final double radius, final Vector3d intersectionCenterAndHL) {
        return intersectLineCircle(y0 - y1, x1 - x0, (x0 - x1) * y0 + (y1 - y0) * x0, centerX, centerY, radius, intersectionCenterAndHL);
    }
    
    public static boolean testAarLine(final double minX, final double minY, final double maxX, final double maxY, final double a, final double b, final double c) {
        double pX;
        double nX;
        if (a > 0.0) {
            pX = maxX;
            nX = minX;
        }
        else {
            pX = minX;
            nX = maxX;
        }
        double pY;
        double nY;
        if (b > 0.0) {
            pY = maxY;
            nY = minY;
        }
        else {
            pY = minY;
            nY = maxY;
        }
        final double distN = c + a * nX + b * nY;
        final double distP = c + a * pX + b * pY;
        return distN <= 0.0 && distP >= 0.0;
    }
    
    public static boolean testAarLine(final Vector2dc min, final Vector2dc max, final double a, final double b, final double c) {
        return testAarLine(min.x(), min.y(), max.x(), max.y(), a, b, c);
    }
    
    public static boolean testAarLine(final double minX, final double minY, final double maxX, final double maxY, final double x0, final double y0, final double x1, final double y1) {
        final double a = y0 - y1;
        final double b = x1 - x0;
        final double c = -b * y0 - a * x0;
        return testAarLine(minX, minY, maxX, maxY, a, b, c);
    }
    
    public static boolean testAarAar(final double minXA, final double minYA, final double maxXA, final double maxYA, final double minXB, final double minYB, final double maxXB, final double maxYB) {
        return maxXA >= minXB && maxYA >= minYB && minXA <= maxXB && minYA <= maxYB;
    }
    
    public static boolean testAarAar(final Vector2dc minA, final Vector2dc maxA, final Vector2dc minB, final Vector2dc maxB) {
        return testAarAar(minA.x(), minA.y(), maxA.x(), maxA.y(), minB.x(), minB.y(), maxB.x(), maxB.y());
    }
    
    public static boolean testMovingCircleCircle(final double aX, final double aY, final double maX, final double maY, final double aR, final double bX, final double bY, final double bR) {
        final double aRbR = aR + bR;
        final double dist = Math.sqrt((aX - bX) * (aX - bX) + (aY - bY) * (aY - bY)) - aRbR;
        final double mLen = Math.sqrt(maX * maX + maY * maY);
        if (mLen < dist) {
            return false;
        }
        final double invMLen = 1.0 / mLen;
        final double nX = maX * invMLen;
        final double nY = maY * invMLen;
        final double cX = bX - aX;
        final double cY = bY - aY;
        final double nDotC = nX * cX + nY * cY;
        if (nDotC <= 0.0) {
            return false;
        }
        final double cLen = Math.sqrt(cX * cX + cY * cY);
        final double cLenNdotC = cLen * cLen - nDotC * nDotC;
        final double aRbR2 = aRbR * aRbR;
        if (cLenNdotC >= aRbR2) {
            return false;
        }
        final double t = aRbR2 - cLenNdotC;
        if (t < 0.0) {
            return false;
        }
        final double distance = nDotC - Math.sqrt(t);
        final double mag = mLen;
        return mag >= distance;
    }
    
    public static boolean testMovingCircleCircle(final Vector2d centerA, final Vector2d moveA, final double aR, final Vector2d centerB, final double bR) {
        return testMovingCircleCircle(centerA.x, centerA.y, moveA.x, moveA.y, aR, centerB.x, centerB.y, bR);
    }
    
    public static boolean intersectCircleCircle(final double aX, final double aY, final double radiusSquaredA, final double bX, final double bY, final double radiusSquaredB, final Vector3d intersectionCenterAndHL) {
        final double dX = bX - aX;
        final double dY = bY - aY;
        final double distSquared = dX * dX + dY * dY;
        final double h = 0.5 + (radiusSquaredA - radiusSquaredB) / distSquared;
        final double r_i = Math.sqrt(radiusSquaredA - h * h * distSquared);
        if (r_i >= 0.0) {
            intersectionCenterAndHL.x = aX + h * dX;
            intersectionCenterAndHL.y = aY + h * dY;
            intersectionCenterAndHL.z = r_i;
            return true;
        }
        return false;
    }
    
    public static boolean intersectCircleCircle(final Vector2dc centerA, final double radiusSquaredA, final Vector2dc centerB, final double radiusSquaredB, final Vector3d intersectionCenterAndHL) {
        return intersectCircleCircle(centerA.x(), centerA.y(), radiusSquaredA, centerB.x(), centerB.y(), radiusSquaredB, intersectionCenterAndHL);
    }
    
    public static boolean testCircleCircle(final double aX, final double aY, final double rA, final double bX, final double bY, final double rB) {
        final double d = (aX - bX) * (aX - bX) + (aY - bY) * (aY - bY);
        return d <= (rA + rB) * (rA + rB);
    }
    
    public static boolean testCircleCircle(final Vector2dc centerA, final double radiusSquaredA, final Vector2dc centerB, final double radiusSquaredB) {
        return testCircleCircle(centerA.x(), centerA.y(), radiusSquaredA, centerB.x(), centerB.y(), radiusSquaredB);
    }
    
    public static double distancePointLine(final double pointX, final double pointY, final double a, final double b, final double c) {
        final double denom = Math.sqrt(a * a + b * b);
        return (a * pointX + b * pointY + c) / denom;
    }
    
    public static double distancePointLine(final double pointX, final double pointY, final double x0, final double y0, final double x1, final double y1) {
        final double dx = x1 - x0;
        final double dy = y1 - y0;
        final double denom = Math.sqrt(dx * dx + dy * dy);
        return (dx * (y0 - pointY) - (x0 - pointX) * dy) / denom;
    }
    
    public static double distancePointLine(final double pX, final double pY, final double pZ, final double x0, final double y0, final double z0, final double x1, final double y1, final double z1) {
        final double d21x = x1 - x0;
        final double d21y = y1 - y0;
        final double d21z = z1 - z0;
        final double d10x = x0 - pX;
        final double d10y = y0 - pY;
        final double d10z = z0 - pZ;
        final double cx = d21y * d10z - d21z * d10y;
        final double cy = d21z * d10x - d21x * d10z;
        final double cz = d21x * d10y - d21y * d10x;
        return Math.sqrt((cx * cx + cy * cy + cz * cz) / (d21x * d21x + d21y * d21y + d21z * d21z));
    }
    
    public static double intersectRayLine(final double originX, final double originY, final double dirX, final double dirY, final double pointX, final double pointY, final double normalX, final double normalY, final double epsilon) {
        final double denom = normalX * dirX + normalY * dirY;
        if (denom < epsilon) {
            final double t = ((pointX - originX) * normalX + (pointY - originY) * normalY) / denom;
            if (t >= 0.0) {
                return t;
            }
        }
        return -1.0;
    }
    
    public static double intersectRayLine(final Vector2dc origin, final Vector2dc dir, final Vector2dc point, final Vector2dc normal, final double epsilon) {
        return intersectRayLine(origin.x(), origin.y(), dir.x(), dir.y(), point.x(), point.y(), normal.x(), normal.y(), epsilon);
    }
    
    public static double intersectRayLineSegment(final double originX, final double originY, final double dirX, final double dirY, final double aX, final double aY, final double bX, final double bY) {
        final double v1X = originX - aX;
        final double v1Y = originY - aY;
        final double v2X = bX - aX;
        final double v2Y = bY - aY;
        final double invV23 = 1.0 / (v2Y * dirX - v2X * dirY);
        final double t1 = (v2X * v1Y - v2Y * v1X) * invV23;
        final double t2 = (v1Y * dirX - v1X * dirY) * invV23;
        if (t1 >= 0.0 && t2 >= 0.0 && t2 <= 1.0) {
            return t1;
        }
        return -1.0;
    }
    
    public static double intersectRayLineSegment(final Vector2dc origin, final Vector2dc dir, final Vector2dc a, final Vector2dc b) {
        return intersectRayLineSegment(origin.x(), origin.y(), dir.x(), dir.y(), a.x(), a.y(), b.x(), b.y());
    }
    
    public static boolean testAarCircle(final double minX, final double minY, final double maxX, final double maxY, final double centerX, final double centerY, final double radiusSquared) {
        double radius2 = radiusSquared;
        if (centerX < minX) {
            final double d = centerX - minX;
            radius2 -= d * d;
        }
        else if (centerX > maxX) {
            final double d = centerX - maxX;
            radius2 -= d * d;
        }
        if (centerY < minY) {
            final double d = centerY - minY;
            radius2 -= d * d;
        }
        else if (centerY > maxY) {
            final double d = centerY - maxY;
            radius2 -= d * d;
        }
        return radius2 >= 0.0;
    }
    
    public static boolean testAarCircle(final Vector2dc min, final Vector2dc max, final Vector2dc center, final double radiusSquared) {
        return testAarCircle(min.x(), min.y(), max.x(), max.y(), center.x(), center.y(), radiusSquared);
    }
    
    public static int findClosestPointOnTriangle(final double v0X, final double v0Y, final double v1X, final double v1Y, final double v2X, final double v2Y, final double pX, final double pY, final Vector2d result) {
        final double abX = v1X - v0X;
        final double abY = v1Y - v0Y;
        final double acX = v2X - v0X;
        final double acY = v2Y - v0Y;
        final double apX = pX - v0X;
        final double apY = pY - v0Y;
        final double d1 = abX * apX + abY * apY;
        final double d2 = acX * apX + acY * apY;
        if (d1 <= 0.0 && d2 <= 0.0) {
            result.x = v0X;
            result.y = v0Y;
            return 1;
        }
        final double bpX = pX - v1X;
        final double bpY = pY - v1Y;
        final double d3 = abX * bpX + abY * bpY;
        final double d4 = acX * bpX + acY * bpY;
        if (d3 >= 0.0 && d4 <= d3) {
            result.x = v1X;
            result.y = v1Y;
            return 2;
        }
        final double vc = d1 * d4 - d3 * d2;
        if (vc <= 0.0 && d1 >= 0.0 && d3 <= 0.0) {
            final double v = d1 / (d1 - d3);
            result.x = v0X + v * abX;
            result.y = v0Y + v * abY;
            return 4;
        }
        final double cpX = pX - v2X;
        final double cpY = pY - v2Y;
        final double d5 = abX * cpX + abY * cpY;
        final double d6 = acX * cpX + acY * cpY;
        if (d6 >= 0.0 && d5 <= d6) {
            result.x = v2X;
            result.y = v2Y;
            return 3;
        }
        final double vb = d5 * d2 - d1 * d6;
        if (vb <= 0.0 && d2 >= 0.0 && d6 <= 0.0) {
            final double w = d2 / (d2 - d6);
            result.x = v0X + w * acX;
            result.y = v0Y + w * acY;
            return 6;
        }
        final double va = d3 * d6 - d5 * d4;
        if (va <= 0.0 && d4 - d3 >= 0.0 && d5 - d6 >= 0.0) {
            final double w2 = (d4 - d3) / (d4 - d3 + d5 - d6);
            result.x = v1X + w2 * (v2X - v1X);
            result.y = v1Y + w2 * (v2Y - v1Y);
            return 5;
        }
        final double denom = 1.0 / (va + vb + vc);
        final double v2 = vb * denom;
        final double w3 = vc * denom;
        result.x = v0X + abX * v2 + acX * w3;
        result.y = v0Y + abY * v2 + acY * w3;
        return 7;
    }
    
    public static int findClosestPointOnTriangle(final Vector2dc v0, final Vector2dc v1, final Vector2dc v2, final Vector2dc p, final Vector2d result) {
        return findClosestPointOnTriangle(v0.x(), v0.y(), v1.x(), v1.y(), v2.x(), v2.y(), p.x(), p.y(), result);
    }
    
    public static boolean intersectRayCircle(final double originX, final double originY, final double dirX, final double dirY, final double centerX, final double centerY, final double radiusSquared, final Vector2d result) {
        final double Lx = centerX - originX;
        final double Ly = centerY - originY;
        final double tca = Lx * dirX + Ly * dirY;
        final double d2 = Lx * Lx + Ly * Ly - tca * tca;
        if (d2 > radiusSquared) {
            return false;
        }
        final double thc = Math.sqrt(radiusSquared - d2);
        final double t0 = tca - thc;
        final double t2 = tca + thc;
        if (t0 < t2 && t2 >= 0.0) {
            result.x = t0;
            result.y = t2;
            return true;
        }
        return false;
    }
    
    public static boolean intersectRayCircle(final Vector2dc origin, final Vector2dc dir, final Vector2dc center, final double radiusSquared, final Vector2d result) {
        return intersectRayCircle(origin.x(), origin.y(), dir.x(), dir.y(), center.x(), center.y(), radiusSquared, result);
    }
    
    public static boolean testRayCircle(final double originX, final double originY, final double dirX, final double dirY, final double centerX, final double centerY, final double radiusSquared) {
        final double Lx = centerX - originX;
        final double Ly = centerY - originY;
        final double tca = Lx * dirX + Ly * dirY;
        final double d2 = Lx * Lx + Ly * Ly - tca * tca;
        if (d2 > radiusSquared) {
            return false;
        }
        final double thc = Math.sqrt(radiusSquared - d2);
        final double t0 = tca - thc;
        final double t2 = tca + thc;
        return t0 < t2 && t2 >= 0.0;
    }
    
    public static boolean testRayCircle(final Vector2dc origin, final Vector2dc dir, final Vector2dc center, final double radiusSquared) {
        return testRayCircle(origin.x(), origin.y(), dir.x(), dir.y(), center.x(), center.y(), radiusSquared);
    }
    
    public static int intersectRayAar(final double originX, final double originY, final double dirX, final double dirY, final double minX, final double minY, final double maxX, final double maxY, final Vector2d result) {
        final double invDirX = 1.0 / dirX;
        final double invDirY = 1.0 / dirY;
        double tNear;
        double tFar;
        if (invDirX >= 0.0) {
            tNear = (minX - originX) * invDirX;
            tFar = (maxX - originX) * invDirX;
        }
        else {
            tNear = (maxX - originX) * invDirX;
            tFar = (minX - originX) * invDirX;
        }
        double tymin;
        double tymax;
        if (invDirY >= 0.0) {
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
        tNear = ((tymin > tNear || Double.isNaN(tNear)) ? tymin : tNear);
        tFar = ((tymax < tFar || Double.isNaN(tFar)) ? tymax : tFar);
        int side = -1;
        if (tNear < tFar && tFar >= 0.0) {
            final double px = originX + tNear * dirX;
            final double py = originY + tNear * dirY;
            result.x = tNear;
            result.y = tFar;
            final double daX = Math.abs(px - minX);
            final double daY = Math.abs(py - minY);
            final double dbX = Math.abs(px - maxX);
            final double dbY = Math.abs(py - maxY);
            side = 0;
            double min = daX;
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
    
    public static int intersectRayAar(final Vector2dc origin, final Vector2dc dir, final Vector2dc min, final Vector2dc max, final Vector2d result) {
        return intersectRayAar(origin.x(), origin.y(), dir.x(), dir.y(), min.x(), min.y(), max.x(), max.y(), result);
    }
    
    public static int intersectLineSegmentAar(final double p0X, final double p0Y, final double p1X, final double p1Y, final double minX, final double minY, final double maxX, final double maxY, final Vector2d result) {
        final double dirX = p1X - p0X;
        final double dirY = p1Y - p0Y;
        final double invDirX = 1.0 / dirX;
        final double invDirY = 1.0 / dirY;
        double tNear;
        double tFar;
        if (invDirX >= 0.0) {
            tNear = (minX - p0X) * invDirX;
            tFar = (maxX - p0X) * invDirX;
        }
        else {
            tNear = (maxX - p0X) * invDirX;
            tFar = (minX - p0X) * invDirX;
        }
        double tymin;
        double tymax;
        if (invDirY >= 0.0) {
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
        tNear = ((tymin > tNear || Double.isNaN(tNear)) ? tymin : tNear);
        tFar = ((tymax < tFar || Double.isNaN(tFar)) ? tymax : tFar);
        int type = -1;
        if (tNear <= tFar && tNear <= 1.0 && tFar >= 0.0) {
            if (tNear >= 0.0 && tFar > 1.0) {
                tFar = tNear;
                type = 1;
            }
            else if (tNear < 0.0 && tFar <= 1.0) {
                tNear = tFar;
                type = 1;
            }
            else if (tNear < 0.0 && tFar > 1.0) {
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
    
    public static int intersectLineSegmentAar(final Vector2dc p0, final Vector2dc p1, final Vector2dc min, final Vector2dc max, final Vector2d result) {
        return intersectLineSegmentAar(p0.x(), p0.y(), p1.x(), p1.y(), min.x(), min.y(), max.x(), max.y(), result);
    }
    
    public static boolean testRayAar(final double originX, final double originY, final double dirX, final double dirY, final double minX, final double minY, final double maxX, final double maxY) {
        final double invDirX = 1.0 / dirX;
        final double invDirY = 1.0 / dirY;
        double tNear;
        double tFar;
        if (invDirX >= 0.0) {
            tNear = (minX - originX) * invDirX;
            tFar = (maxX - originX) * invDirX;
        }
        else {
            tNear = (maxX - originX) * invDirX;
            tFar = (minX - originX) * invDirX;
        }
        double tymin;
        double tymax;
        if (invDirY >= 0.0) {
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
        tNear = ((tymin > tNear || Double.isNaN(tNear)) ? tymin : tNear);
        tFar = ((tymax < tFar || Double.isNaN(tFar)) ? tymax : tFar);
        return tNear < tFar && tFar >= 0.0;
    }
    
    public static boolean testRayAar(final Vector2dc origin, final Vector2dc dir, final Vector2dc min, final Vector2dc max) {
        return testRayAar(origin.x(), origin.y(), dir.x(), dir.y(), min.x(), min.y(), max.x(), max.y());
    }
    
    public static boolean testPointTriangle(final double pX, final double pY, final double v0X, final double v0Y, final double v1X, final double v1Y, final double v2X, final double v2Y) {
        final boolean b1 = (pX - v1X) * (v0Y - v1Y) - (v0X - v1X) * (pY - v1Y) < 0.0;
        final boolean b2 = (pX - v2X) * (v1Y - v2Y) - (v1X - v2X) * (pY - v2Y) < 0.0;
        if (b1 != b2) {
            return false;
        }
        final boolean b3 = (pX - v0X) * (v2Y - v0Y) - (v2X - v0X) * (pY - v0Y) < 0.0;
        return b2 == b3;
    }
    
    public static boolean testPointTriangle(final Vector2dc point, final Vector2dc v0, final Vector2dc v1, final Vector2dc v2) {
        return testPointTriangle(point.x(), point.y(), v0.x(), v0.y(), v1.x(), v1.y(), v2.x(), v2.y());
    }
    
    public static boolean testPointAar(final double pX, final double pY, final double minX, final double minY, final double maxX, final double maxY) {
        return pX >= minX && pY >= minY && pX <= maxX && pY <= maxY;
    }
    
    public static boolean testPointCircle(final double pX, final double pY, final double centerX, final double centerY, final double radiusSquared) {
        final double dx = pX - centerX;
        final double dy = pY - centerY;
        final double dx2 = dx * dx;
        final double dy2 = dy * dy;
        return dx2 + dy2 <= radiusSquared;
    }
    
    public static boolean testCircleTriangle(final double centerX, final double centerY, final double radiusSquared, final double v0X, final double v0Y, final double v1X, final double v1Y, final double v2X, final double v2Y) {
        final double c1x = centerX - v0X;
        final double c1y = centerY - v0Y;
        final double c1sqr = c1x * c1x + c1y * c1y - radiusSquared;
        if (c1sqr <= 0.0) {
            return true;
        }
        final double c2x = centerX - v1X;
        final double c2y = centerY - v1Y;
        final double c2sqr = c2x * c2x + c2y * c2y - radiusSquared;
        if (c2sqr <= 0.0) {
            return true;
        }
        final double c3x = centerX - v2X;
        final double c3y = centerY - v2Y;
        final double c3sqr = c3x * c3x + c3y * c3y - radiusSquared;
        if (c3sqr <= 0.0) {
            return true;
        }
        final double e1x = v1X - v0X;
        final double e1y = v1Y - v0Y;
        final double e2x = v2X - v1X;
        final double e2y = v2Y - v1Y;
        final double e3x = v0X - v2X;
        final double e3y = v0Y - v2Y;
        if (e1x * c1y - e1y * c1x >= 0.0 && e2x * c2y - e2y * c2x >= 0.0 && e3x * c3y - e3y * c3x >= 0.0) {
            return true;
        }
        double k = c1x * e1x + c1y * e1y;
        if (k >= 0.0) {
            final double len = e1x * e1x + e1y * e1y;
            if (k <= len && c1sqr * len <= k * k) {
                return true;
            }
        }
        k = c2x * e2x + c2y * e2y;
        if (k > 0.0) {
            final double len = e2x * e2x + e2y * e2y;
            if (k <= len && c2sqr * len <= k * k) {
                return true;
            }
        }
        k = c3x * e3x + c3y * e3y;
        if (k >= 0.0) {
            final double len = e3x * e3x + e3y * e3y;
            if (k < len && c3sqr * len <= k * k) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean testCircleTriangle(final Vector2dc center, final double radiusSquared, final Vector2dc v0, final Vector2dc v1, final Vector2dc v2) {
        return testCircleTriangle(center.x(), center.y(), radiusSquared, v0.x(), v0.y(), v1.x(), v1.y(), v2.x(), v2.y());
    }
    
    public static int intersectPolygonRay(final double[] verticesXY, final double originX, final double originY, final double dirX, final double dirY, final Vector2d p) {
        double nearestT = Double.POSITIVE_INFINITY;
        final int count = verticesXY.length >> 1;
        int edgeIndex = -1;
        double aX = verticesXY[count - 1 << 1];
        double aY = verticesXY[(count - 1 << 1) + 1];
        for (int i = 0; i < count; ++i) {
            final double bX = verticesXY[i << 1];
            final double bY = verticesXY[(i << 1) + 1];
            final double doaX = originX - aX;
            final double doaY = originY - aY;
            final double dbaX = bX - aX;
            final double dbaY = bY - aY;
            final double invDbaDir = 1.0 / (dbaY * dirX - dbaX * dirY);
            final double t = (dbaX * doaY - dbaY * doaX) * invDbaDir;
            if (t >= 0.0 && t < nearestT) {
                final double t2 = (doaY * dirX - doaX * dirY) * invDbaDir;
                if (t2 >= 0.0 && t2 <= 1.0) {
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
    
    public static int intersectPolygonRay(final Vector2dc[] vertices, final double originX, final double originY, final double dirX, final double dirY, final Vector2d p) {
        double nearestT = Double.POSITIVE_INFINITY;
        final int count = vertices.length;
        int edgeIndex = -1;
        double aX = vertices[count - 1].x();
        double aY = vertices[count - 1].y();
        for (int i = 0; i < count; ++i) {
            final Vector2dc b = vertices[i];
            final double bX = b.x();
            final double bY = b.y();
            final double doaX = originX - aX;
            final double doaY = originY - aY;
            final double dbaX = bX - aX;
            final double dbaY = bY - aY;
            final double invDbaDir = 1.0 / (dbaY * dirX - dbaX * dirY);
            final double t = (dbaX * doaY - dbaY * doaX) * invDbaDir;
            if (t >= 0.0 && t < nearestT) {
                final double t2 = (doaY * dirX - doaX * dirY) * invDbaDir;
                if (t2 >= 0.0 && t2 <= 1.0) {
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
    
    public static boolean intersectLineLine(final double ps1x, final double ps1y, final double pe1x, final double pe1y, final double ps2x, final double ps2y, final double pe2x, final double pe2y, final Vector2d p) {
        final double d1x = ps1x - pe1x;
        final double d1y = pe1y - ps1y;
        final double d1ps1 = d1y * ps1x + d1x * ps1y;
        final double d2x = ps2x - pe2x;
        final double d2y = pe2y - ps2y;
        final double d2ps2 = d2y * ps2x + d2x * ps2y;
        final double det = d1y * d2x - d2y * d1x;
        if (det == 0.0) {
            return false;
        }
        p.x = (d2x * d1ps1 - d1x * d2ps2) / det;
        p.y = (d1y * d2ps2 - d2y * d1ps1) / det;
        return true;
    }
    
    private static boolean separatingAxis(final Vector2d[] v1s, final Vector2d[] v2s, final double aX, final double aY) {
        double minA = Double.POSITIVE_INFINITY;
        double maxA = Double.NEGATIVE_INFINITY;
        double minB = Double.POSITIVE_INFINITY;
        double maxB = Double.NEGATIVE_INFINITY;
        for (int maxLen = Math.max(v1s.length, v2s.length), k = 0; k < maxLen; ++k) {
            if (k < v1s.length) {
                final Vector2d v1 = v1s[k];
                final double d = v1.x * aX + v1.y * aY;
                if (d < minA) {
                    minA = d;
                }
                if (d > maxA) {
                    maxA = d;
                }
            }
            if (k < v2s.length) {
                final Vector2d v2 = v2s[k];
                final double d = v2.x * aX + v2.y * aY;
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
    
    public static boolean testPolygonPolygon(final Vector2d[] v1s, final Vector2d[] v2s) {
        int i = 0;
        int j = v1s.length - 1;
        while (i < v1s.length) {
            final Vector2d s = v1s[i];
            final Vector2d t = v1s[j];
            if (separatingAxis(v1s, v2s, s.y - t.y, t.x - s.x)) {
                return false;
            }
            j = i;
            ++i;
        }
        i = 0;
        j = v2s.length - 1;
        while (i < v2s.length) {
            final Vector2d s = v2s[i];
            final Vector2d t = v2s[j];
            if (separatingAxis(v1s, v2s, s.y - t.y, t.x - s.x)) {
                return false;
            }
            j = i;
            ++i;
        }
        return true;
    }
}
