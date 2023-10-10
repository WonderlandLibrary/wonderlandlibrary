// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

public class FrustumIntersection
{
    public static final int PLANE_NX = 0;
    public static final int PLANE_PX = 1;
    public static final int PLANE_NY = 2;
    public static final int PLANE_PY = 3;
    public static final int PLANE_NZ = 4;
    public static final int PLANE_PZ = 5;
    public static final int INTERSECT = -1;
    public static final int INSIDE = -2;
    public static final int OUTSIDE = -3;
    public static final int PLANE_MASK_NX = 1;
    public static final int PLANE_MASK_PX = 2;
    public static final int PLANE_MASK_NY = 4;
    public static final int PLANE_MASK_PY = 8;
    public static final int PLANE_MASK_NZ = 16;
    public static final int PLANE_MASK_PZ = 32;
    private float nxX;
    private float nxY;
    private float nxZ;
    private float nxW;
    private float pxX;
    private float pxY;
    private float pxZ;
    private float pxW;
    private float nyX;
    private float nyY;
    private float nyZ;
    private float nyW;
    private float pyX;
    private float pyY;
    private float pyZ;
    private float pyW;
    private float nzX;
    private float nzY;
    private float nzZ;
    private float nzW;
    private float pzX;
    private float pzY;
    private float pzZ;
    private float pzW;
    private final Vector4f[] planes;
    
    public FrustumIntersection() {
        this.planes = new Vector4f[6];
        for (int i = 0; i < 6; ++i) {
            this.planes[i] = new Vector4f();
        }
    }
    
    public FrustumIntersection(final Matrix4fc m) {
        this.planes = new Vector4f[6];
        for (int i = 0; i < 6; ++i) {
            this.planes[i] = new Vector4f();
        }
        this.set(m, true);
    }
    
    public FrustumIntersection(final Matrix4fc m, final boolean allowTestSpheres) {
        this.planes = new Vector4f[6];
        for (int i = 0; i < 6; ++i) {
            this.planes[i] = new Vector4f();
        }
        this.set(m, allowTestSpheres);
    }
    
    public FrustumIntersection set(final Matrix4fc m) {
        return this.set(m, true);
    }
    
    public FrustumIntersection set(final Matrix4fc m, final boolean allowTestSpheres) {
        this.nxX = m.m03() + m.m00();
        this.nxY = m.m13() + m.m10();
        this.nxZ = m.m23() + m.m20();
        this.nxW = m.m33() + m.m30();
        if (allowTestSpheres) {
            final float invl = Math.invsqrt(this.nxX * this.nxX + this.nxY * this.nxY + this.nxZ * this.nxZ);
            this.nxX *= invl;
            this.nxY *= invl;
            this.nxZ *= invl;
            this.nxW *= invl;
        }
        this.planes[0].set(this.nxX, this.nxY, this.nxZ, this.nxW);
        this.pxX = m.m03() - m.m00();
        this.pxY = m.m13() - m.m10();
        this.pxZ = m.m23() - m.m20();
        this.pxW = m.m33() - m.m30();
        if (allowTestSpheres) {
            final float invl = Math.invsqrt(this.pxX * this.pxX + this.pxY * this.pxY + this.pxZ * this.pxZ);
            this.pxX *= invl;
            this.pxY *= invl;
            this.pxZ *= invl;
            this.pxW *= invl;
        }
        this.planes[1].set(this.pxX, this.pxY, this.pxZ, this.pxW);
        this.nyX = m.m03() + m.m01();
        this.nyY = m.m13() + m.m11();
        this.nyZ = m.m23() + m.m21();
        this.nyW = m.m33() + m.m31();
        if (allowTestSpheres) {
            final float invl = Math.invsqrt(this.nyX * this.nyX + this.nyY * this.nyY + this.nyZ * this.nyZ);
            this.nyX *= invl;
            this.nyY *= invl;
            this.nyZ *= invl;
            this.nyW *= invl;
        }
        this.planes[2].set(this.nyX, this.nyY, this.nyZ, this.nyW);
        this.pyX = m.m03() - m.m01();
        this.pyY = m.m13() - m.m11();
        this.pyZ = m.m23() - m.m21();
        this.pyW = m.m33() - m.m31();
        if (allowTestSpheres) {
            final float invl = Math.invsqrt(this.pyX * this.pyX + this.pyY * this.pyY + this.pyZ * this.pyZ);
            this.pyX *= invl;
            this.pyY *= invl;
            this.pyZ *= invl;
            this.pyW *= invl;
        }
        this.planes[3].set(this.pyX, this.pyY, this.pyZ, this.pyW);
        this.nzX = m.m03() + m.m02();
        this.nzY = m.m13() + m.m12();
        this.nzZ = m.m23() + m.m22();
        this.nzW = m.m33() + m.m32();
        if (allowTestSpheres) {
            final float invl = Math.invsqrt(this.nzX * this.nzX + this.nzY * this.nzY + this.nzZ * this.nzZ);
            this.nzX *= invl;
            this.nzY *= invl;
            this.nzZ *= invl;
            this.nzW *= invl;
        }
        this.planes[4].set(this.nzX, this.nzY, this.nzZ, this.nzW);
        this.pzX = m.m03() - m.m02();
        this.pzY = m.m13() - m.m12();
        this.pzZ = m.m23() - m.m22();
        this.pzW = m.m33() - m.m32();
        if (allowTestSpheres) {
            final float invl = Math.invsqrt(this.pzX * this.pzX + this.pzY * this.pzY + this.pzZ * this.pzZ);
            this.pzX *= invl;
            this.pzY *= invl;
            this.pzZ *= invl;
            this.pzW *= invl;
        }
        this.planes[5].set(this.pzX, this.pzY, this.pzZ, this.pzW);
        return this;
    }
    
    public boolean testPoint(final Vector3fc point) {
        return this.testPoint(point.x(), point.y(), point.z());
    }
    
    public boolean testPoint(final float x, final float y, final float z) {
        return this.nxX * x + this.nxY * y + this.nxZ * z + this.nxW >= 0.0f && this.pxX * x + this.pxY * y + this.pxZ * z + this.pxW >= 0.0f && this.nyX * x + this.nyY * y + this.nyZ * z + this.nyW >= 0.0f && this.pyX * x + this.pyY * y + this.pyZ * z + this.pyW >= 0.0f && this.nzX * x + this.nzY * y + this.nzZ * z + this.nzW >= 0.0f && this.pzX * x + this.pzY * y + this.pzZ * z + this.pzW >= 0.0f;
    }
    
    public boolean testSphere(final Vector3fc center, final float radius) {
        return this.testSphere(center.x(), center.y(), center.z(), radius);
    }
    
    public boolean testSphere(final float x, final float y, final float z, final float r) {
        return this.nxX * x + this.nxY * y + this.nxZ * z + this.nxW >= -r && this.pxX * x + this.pxY * y + this.pxZ * z + this.pxW >= -r && this.nyX * x + this.nyY * y + this.nyZ * z + this.nyW >= -r && this.pyX * x + this.pyY * y + this.pyZ * z + this.pyW >= -r && this.nzX * x + this.nzY * y + this.nzZ * z + this.nzW >= -r && this.pzX * x + this.pzY * y + this.pzZ * z + this.pzW >= -r;
    }
    
    public int intersectSphere(final Vector3fc center, final float radius) {
        return this.intersectSphere(center.x(), center.y(), center.z(), radius);
    }
    
    public int intersectSphere(final float x, final float y, final float z, final float r) {
        boolean inside = true;
        float dist = this.nxX * x + this.nxY * y + this.nxZ * z + this.nxW;
        if (dist >= -r) {
            inside &= (dist >= r);
            dist = this.pxX * x + this.pxY * y + this.pxZ * z + this.pxW;
            if (dist >= -r) {
                inside &= (dist >= r);
                dist = this.nyX * x + this.nyY * y + this.nyZ * z + this.nyW;
                if (dist >= -r) {
                    inside &= (dist >= r);
                    dist = this.pyX * x + this.pyY * y + this.pyZ * z + this.pyW;
                    if (dist >= -r) {
                        inside &= (dist >= r);
                        dist = this.nzX * x + this.nzY * y + this.nzZ * z + this.nzW;
                        if (dist >= -r) {
                            inside &= (dist >= r);
                            dist = this.pzX * x + this.pzY * y + this.pzZ * z + this.pzW;
                            if (dist >= -r) {
                                inside &= (dist >= r);
                                return inside ? -2 : -1;
                            }
                        }
                    }
                }
            }
        }
        return -3;
    }
    
    public boolean testAab(final Vector3fc min, final Vector3fc max) {
        return this.testAab(min.x(), min.y(), min.z(), max.x(), max.y(), max.z());
    }
    
    public boolean testAab(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return this.nxX * ((this.nxX < 0.0f) ? minX : maxX) + this.nxY * ((this.nxY < 0.0f) ? minY : maxY) + this.nxZ * ((this.nxZ < 0.0f) ? minZ : maxZ) >= -this.nxW && this.pxX * ((this.pxX < 0.0f) ? minX : maxX) + this.pxY * ((this.pxY < 0.0f) ? minY : maxY) + this.pxZ * ((this.pxZ < 0.0f) ? minZ : maxZ) >= -this.pxW && this.nyX * ((this.nyX < 0.0f) ? minX : maxX) + this.nyY * ((this.nyY < 0.0f) ? minY : maxY) + this.nyZ * ((this.nyZ < 0.0f) ? minZ : maxZ) >= -this.nyW && this.pyX * ((this.pyX < 0.0f) ? minX : maxX) + this.pyY * ((this.pyY < 0.0f) ? minY : maxY) + this.pyZ * ((this.pyZ < 0.0f) ? minZ : maxZ) >= -this.pyW && this.nzX * ((this.nzX < 0.0f) ? minX : maxX) + this.nzY * ((this.nzY < 0.0f) ? minY : maxY) + this.nzZ * ((this.nzZ < 0.0f) ? minZ : maxZ) >= -this.nzW && this.pzX * ((this.pzX < 0.0f) ? minX : maxX) + this.pzY * ((this.pzY < 0.0f) ? minY : maxY) + this.pzZ * ((this.pzZ < 0.0f) ? minZ : maxZ) >= -this.pzW;
    }
    
    public boolean testPlaneXY(final Vector2fc min, final Vector2fc max) {
        return this.testPlaneXY(min.x(), min.y(), max.x(), max.y());
    }
    
    public boolean testPlaneXY(final float minX, final float minY, final float maxX, final float maxY) {
        return this.nxX * ((this.nxX < 0.0f) ? minX : maxX) + this.nxY * ((this.nxY < 0.0f) ? minY : maxY) >= -this.nxW && this.pxX * ((this.pxX < 0.0f) ? minX : maxX) + this.pxY * ((this.pxY < 0.0f) ? minY : maxY) >= -this.pxW && this.nyX * ((this.nyX < 0.0f) ? minX : maxX) + this.nyY * ((this.nyY < 0.0f) ? minY : maxY) >= -this.nyW && this.pyX * ((this.pyX < 0.0f) ? minX : maxX) + this.pyY * ((this.pyY < 0.0f) ? minY : maxY) >= -this.pyW && this.nzX * ((this.nzX < 0.0f) ? minX : maxX) + this.nzY * ((this.nzY < 0.0f) ? minY : maxY) >= -this.nzW && this.pzX * ((this.pzX < 0.0f) ? minX : maxX) + this.pzY * ((this.pzY < 0.0f) ? minY : maxY) >= -this.pzW;
    }
    
    public boolean testPlaneXZ(final float minX, final float minZ, final float maxX, final float maxZ) {
        return this.nxX * ((this.nxX < 0.0f) ? minX : maxX) + this.nxZ * ((this.nxZ < 0.0f) ? minZ : maxZ) >= -this.nxW && this.pxX * ((this.pxX < 0.0f) ? minX : maxX) + this.pxZ * ((this.pxZ < 0.0f) ? minZ : maxZ) >= -this.pxW && this.nyX * ((this.nyX < 0.0f) ? minX : maxX) + this.nyZ * ((this.nyZ < 0.0f) ? minZ : maxZ) >= -this.nyW && this.pyX * ((this.pyX < 0.0f) ? minX : maxX) + this.pyZ * ((this.pyZ < 0.0f) ? minZ : maxZ) >= -this.pyW && this.nzX * ((this.nzX < 0.0f) ? minX : maxX) + this.nzZ * ((this.nzZ < 0.0f) ? minZ : maxZ) >= -this.nzW && this.pzX * ((this.pzX < 0.0f) ? minX : maxX) + this.pzZ * ((this.pzZ < 0.0f) ? minZ : maxZ) >= -this.pzW;
    }
    
    public int intersectAab(final Vector3fc min, final Vector3fc max) {
        return this.intersectAab(min.x(), min.y(), min.z(), max.x(), max.y(), max.z());
    }
    
    public int intersectAab(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        int plane = 0;
        boolean inside = true;
        if (this.nxX * ((this.nxX < 0.0f) ? minX : maxX) + this.nxY * ((this.nxY < 0.0f) ? minY : maxY) + this.nxZ * ((this.nxZ < 0.0f) ? minZ : maxZ) >= -this.nxW) {
            plane = 1;
            inside &= (this.nxX * ((this.nxX < 0.0f) ? maxX : minX) + this.nxY * ((this.nxY < 0.0f) ? maxY : minY) + this.nxZ * ((this.nxZ < 0.0f) ? maxZ : minZ) >= -this.nxW);
            if (this.pxX * ((this.pxX < 0.0f) ? minX : maxX) + this.pxY * ((this.pxY < 0.0f) ? minY : maxY) + this.pxZ * ((this.pxZ < 0.0f) ? minZ : maxZ) >= -this.pxW) {
                plane = 2;
                inside &= (this.pxX * ((this.pxX < 0.0f) ? maxX : minX) + this.pxY * ((this.pxY < 0.0f) ? maxY : minY) + this.pxZ * ((this.pxZ < 0.0f) ? maxZ : minZ) >= -this.pxW);
                if (this.nyX * ((this.nyX < 0.0f) ? minX : maxX) + this.nyY * ((this.nyY < 0.0f) ? minY : maxY) + this.nyZ * ((this.nyZ < 0.0f) ? minZ : maxZ) >= -this.nyW) {
                    plane = 3;
                    inside &= (this.nyX * ((this.nyX < 0.0f) ? maxX : minX) + this.nyY * ((this.nyY < 0.0f) ? maxY : minY) + this.nyZ * ((this.nyZ < 0.0f) ? maxZ : minZ) >= -this.nyW);
                    if (this.pyX * ((this.pyX < 0.0f) ? minX : maxX) + this.pyY * ((this.pyY < 0.0f) ? minY : maxY) + this.pyZ * ((this.pyZ < 0.0f) ? minZ : maxZ) >= -this.pyW) {
                        plane = 4;
                        inside &= (this.pyX * ((this.pyX < 0.0f) ? maxX : minX) + this.pyY * ((this.pyY < 0.0f) ? maxY : minY) + this.pyZ * ((this.pyZ < 0.0f) ? maxZ : minZ) >= -this.pyW);
                        if (this.nzX * ((this.nzX < 0.0f) ? minX : maxX) + this.nzY * ((this.nzY < 0.0f) ? minY : maxY) + this.nzZ * ((this.nzZ < 0.0f) ? minZ : maxZ) >= -this.nzW) {
                            plane = 5;
                            inside &= (this.nzX * ((this.nzX < 0.0f) ? maxX : minX) + this.nzY * ((this.nzY < 0.0f) ? maxY : minY) + this.nzZ * ((this.nzZ < 0.0f) ? maxZ : minZ) >= -this.nzW);
                            if (this.pzX * ((this.pzX < 0.0f) ? minX : maxX) + this.pzY * ((this.pzY < 0.0f) ? minY : maxY) + this.pzZ * ((this.pzZ < 0.0f) ? minZ : maxZ) >= -this.pzW) {
                                inside &= (this.pzX * ((this.pzX < 0.0f) ? maxX : minX) + this.pzY * ((this.pzY < 0.0f) ? maxY : minY) + this.pzZ * ((this.pzZ < 0.0f) ? maxZ : minZ) >= -this.pzW);
                                return inside ? -2 : -1;
                            }
                        }
                    }
                }
            }
        }
        return plane;
    }
    
    public float distanceToPlane(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ, final int plane) {
        return this.planes[plane].x * ((this.planes[plane].x < 0.0f) ? maxX : minX) + this.planes[plane].y * ((this.planes[plane].y < 0.0f) ? maxY : minY) + this.planes[plane].z * ((this.planes[plane].z < 0.0f) ? maxZ : minZ) + this.planes[plane].w;
    }
    
    public int intersectAab(final Vector3fc min, final Vector3fc max, final int mask) {
        return this.intersectAab(min.x(), min.y(), min.z(), max.x(), max.y(), max.z(), mask);
    }
    
    public int intersectAab(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ, final int mask) {
        int plane = 0;
        boolean inside = true;
        if ((mask & 0x1) == 0x0 || this.nxX * ((this.nxX < 0.0f) ? minX : maxX) + this.nxY * ((this.nxY < 0.0f) ? minY : maxY) + this.nxZ * ((this.nxZ < 0.0f) ? minZ : maxZ) >= -this.nxW) {
            plane = 1;
            inside &= (this.nxX * ((this.nxX < 0.0f) ? maxX : minX) + this.nxY * ((this.nxY < 0.0f) ? maxY : minY) + this.nxZ * ((this.nxZ < 0.0f) ? maxZ : minZ) >= -this.nxW);
            if ((mask & 0x2) == 0x0 || this.pxX * ((this.pxX < 0.0f) ? minX : maxX) + this.pxY * ((this.pxY < 0.0f) ? minY : maxY) + this.pxZ * ((this.pxZ < 0.0f) ? minZ : maxZ) >= -this.pxW) {
                plane = 2;
                inside &= (this.pxX * ((this.pxX < 0.0f) ? maxX : minX) + this.pxY * ((this.pxY < 0.0f) ? maxY : minY) + this.pxZ * ((this.pxZ < 0.0f) ? maxZ : minZ) >= -this.pxW);
                if ((mask & 0x4) == 0x0 || this.nyX * ((this.nyX < 0.0f) ? minX : maxX) + this.nyY * ((this.nyY < 0.0f) ? minY : maxY) + this.nyZ * ((this.nyZ < 0.0f) ? minZ : maxZ) >= -this.nyW) {
                    plane = 3;
                    inside &= (this.nyX * ((this.nyX < 0.0f) ? maxX : minX) + this.nyY * ((this.nyY < 0.0f) ? maxY : minY) + this.nyZ * ((this.nyZ < 0.0f) ? maxZ : minZ) >= -this.nyW);
                    if ((mask & 0x8) == 0x0 || this.pyX * ((this.pyX < 0.0f) ? minX : maxX) + this.pyY * ((this.pyY < 0.0f) ? minY : maxY) + this.pyZ * ((this.pyZ < 0.0f) ? minZ : maxZ) >= -this.pyW) {
                        plane = 4;
                        inside &= (this.pyX * ((this.pyX < 0.0f) ? maxX : minX) + this.pyY * ((this.pyY < 0.0f) ? maxY : minY) + this.pyZ * ((this.pyZ < 0.0f) ? maxZ : minZ) >= -this.pyW);
                        if ((mask & 0x10) == 0x0 || this.nzX * ((this.nzX < 0.0f) ? minX : maxX) + this.nzY * ((this.nzY < 0.0f) ? minY : maxY) + this.nzZ * ((this.nzZ < 0.0f) ? minZ : maxZ) >= -this.nzW) {
                            plane = 5;
                            inside &= (this.nzX * ((this.nzX < 0.0f) ? maxX : minX) + this.nzY * ((this.nzY < 0.0f) ? maxY : minY) + this.nzZ * ((this.nzZ < 0.0f) ? maxZ : minZ) >= -this.nzW);
                            if ((mask & 0x20) == 0x0 || this.pzX * ((this.pzX < 0.0f) ? minX : maxX) + this.pzY * ((this.pzY < 0.0f) ? minY : maxY) + this.pzZ * ((this.pzZ < 0.0f) ? minZ : maxZ) >= -this.pzW) {
                                inside &= (this.pzX * ((this.pzX < 0.0f) ? maxX : minX) + this.pzY * ((this.pzY < 0.0f) ? maxY : minY) + this.pzZ * ((this.pzZ < 0.0f) ? maxZ : minZ) >= -this.pzW);
                                return inside ? -2 : -1;
                            }
                        }
                    }
                }
            }
        }
        return plane;
    }
    
    public int intersectAab(final Vector3fc min, final Vector3fc max, final int mask, final int startPlane) {
        return this.intersectAab(min.x(), min.y(), min.z(), max.x(), max.y(), max.z(), mask, startPlane);
    }
    
    public int intersectAab(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ, final int mask, final int startPlane) {
        int plane = startPlane;
        boolean inside = true;
        final Vector4f p = this.planes[startPlane];
        if ((mask & 1 << startPlane) != 0x0 && p.x * ((p.x < 0.0f) ? minX : maxX) + p.y * ((p.y < 0.0f) ? minY : maxY) + p.z * ((p.z < 0.0f) ? minZ : maxZ) < -p.w) {
            return plane;
        }
        if ((mask & 0x1) == 0x0 || this.nxX * ((this.nxX < 0.0f) ? minX : maxX) + this.nxY * ((this.nxY < 0.0f) ? minY : maxY) + this.nxZ * ((this.nxZ < 0.0f) ? minZ : maxZ) >= -this.nxW) {
            plane = 1;
            inside &= (this.nxX * ((this.nxX < 0.0f) ? maxX : minX) + this.nxY * ((this.nxY < 0.0f) ? maxY : minY) + this.nxZ * ((this.nxZ < 0.0f) ? maxZ : minZ) >= -this.nxW);
            if ((mask & 0x2) == 0x0 || this.pxX * ((this.pxX < 0.0f) ? minX : maxX) + this.pxY * ((this.pxY < 0.0f) ? minY : maxY) + this.pxZ * ((this.pxZ < 0.0f) ? minZ : maxZ) >= -this.pxW) {
                plane = 2;
                inside &= (this.pxX * ((this.pxX < 0.0f) ? maxX : minX) + this.pxY * ((this.pxY < 0.0f) ? maxY : minY) + this.pxZ * ((this.pxZ < 0.0f) ? maxZ : minZ) >= -this.pxW);
                if ((mask & 0x4) == 0x0 || this.nyX * ((this.nyX < 0.0f) ? minX : maxX) + this.nyY * ((this.nyY < 0.0f) ? minY : maxY) + this.nyZ * ((this.nyZ < 0.0f) ? minZ : maxZ) >= -this.nyW) {
                    plane = 3;
                    inside &= (this.nyX * ((this.nyX < 0.0f) ? maxX : minX) + this.nyY * ((this.nyY < 0.0f) ? maxY : minY) + this.nyZ * ((this.nyZ < 0.0f) ? maxZ : minZ) >= -this.nyW);
                    if ((mask & 0x8) == 0x0 || this.pyX * ((this.pyX < 0.0f) ? minX : maxX) + this.pyY * ((this.pyY < 0.0f) ? minY : maxY) + this.pyZ * ((this.pyZ < 0.0f) ? minZ : maxZ) >= -this.pyW) {
                        plane = 4;
                        inside &= (this.pyX * ((this.pyX < 0.0f) ? maxX : minX) + this.pyY * ((this.pyY < 0.0f) ? maxY : minY) + this.pyZ * ((this.pyZ < 0.0f) ? maxZ : minZ) >= -this.pyW);
                        if ((mask & 0x10) == 0x0 || this.nzX * ((this.nzX < 0.0f) ? minX : maxX) + this.nzY * ((this.nzY < 0.0f) ? minY : maxY) + this.nzZ * ((this.nzZ < 0.0f) ? minZ : maxZ) >= -this.nzW) {
                            plane = 5;
                            inside &= (this.nzX * ((this.nzX < 0.0f) ? maxX : minX) + this.nzY * ((this.nzY < 0.0f) ? maxY : minY) + this.nzZ * ((this.nzZ < 0.0f) ? maxZ : minZ) >= -this.nzW);
                            if ((mask & 0x20) == 0x0 || this.pzX * ((this.pzX < 0.0f) ? minX : maxX) + this.pzY * ((this.pzY < 0.0f) ? minY : maxY) + this.pzZ * ((this.pzZ < 0.0f) ? minZ : maxZ) >= -this.pzW) {
                                inside &= (this.pzX * ((this.pzX < 0.0f) ? maxX : minX) + this.pzY * ((this.pzY < 0.0f) ? maxY : minY) + this.pzZ * ((this.pzZ < 0.0f) ? maxZ : minZ) >= -this.pzW);
                                return inside ? -2 : -1;
                            }
                        }
                    }
                }
            }
        }
        return plane;
    }
    
    public boolean testLineSegment(final Vector3fc a, final Vector3fc b) {
        return this.testLineSegment(a.x(), a.y(), a.z(), b.x(), b.y(), b.z());
    }
    
    public boolean testLineSegment(float aX, float aY, float aZ, float bX, float bY, float bZ) {
        float da = Math.fma(this.nxX, aX, Math.fma(this.nxY, aY, Math.fma(this.nxZ, aZ, this.nxW)));
        float db = Math.fma(this.nxX, bX, Math.fma(this.nxY, bY, Math.fma(this.nxZ, bZ, this.nxW)));
        if (da < 0.0f && db < 0.0f) {
            return false;
        }
        if (da * db < 0.0f) {
            final float p = Math.abs(da) / Math.abs(db - da);
            final float dx = Math.fma(bX - aX, p, aX);
            final float dy = Math.fma(bY - aY, p, aY);
            final float dz = Math.fma(bZ - aZ, p, aZ);
            if (da < 0.0f) {
                aX = dx;
                aY = dy;
                aZ = dz;
            }
            else {
                bX = dx;
                bY = dy;
                bZ = dz;
            }
        }
        da = Math.fma(this.pxX, aX, Math.fma(this.pxY, aY, Math.fma(this.pxZ, aZ, this.pxW)));
        db = Math.fma(this.pxX, bX, Math.fma(this.pxY, bY, Math.fma(this.pxZ, bZ, this.pxW)));
        if (da < 0.0f && db < 0.0f) {
            return false;
        }
        if (da * db < 0.0f) {
            final float p = Math.abs(da) / Math.abs(db - da);
            final float dx = Math.fma(bX - aX, p, aX);
            final float dy = Math.fma(bY - aY, p, aY);
            final float dz = Math.fma(bZ - aZ, p, aZ);
            if (da < 0.0f) {
                aX = dx;
                aY = dy;
                aZ = dz;
            }
            else {
                bX = dx;
                bY = dy;
                bZ = dz;
            }
        }
        da = Math.fma(this.nyX, aX, Math.fma(this.nyY, aY, Math.fma(this.nyZ, aZ, this.nyW)));
        db = Math.fma(this.nyX, bX, Math.fma(this.nyY, bY, Math.fma(this.nyZ, bZ, this.nyW)));
        if (da < 0.0f && db < 0.0f) {
            return false;
        }
        if (da * db < 0.0f) {
            final float p = Math.abs(da) / Math.abs(db - da);
            final float dx = Math.fma(bX - aX, p, aX);
            final float dy = Math.fma(bY - aY, p, aY);
            final float dz = Math.fma(bZ - aZ, p, aZ);
            if (da < 0.0f) {
                aX = dx;
                aY = dy;
                aZ = dz;
            }
            else {
                bX = dx;
                bY = dy;
                bZ = dz;
            }
        }
        da = Math.fma(this.pyX, aX, Math.fma(this.pyY, aY, Math.fma(this.pyZ, aZ, this.pyW)));
        db = Math.fma(this.pyX, bX, Math.fma(this.pyY, bY, Math.fma(this.pyZ, bZ, this.pyW)));
        if (da < 0.0f && db < 0.0f) {
            return false;
        }
        if (da * db < 0.0f) {
            final float p = Math.abs(da) / Math.abs(db - da);
            final float dx = Math.fma(bX - aX, p, aX);
            final float dy = Math.fma(bY - aY, p, aY);
            final float dz = Math.fma(bZ - aZ, p, aZ);
            if (da < 0.0f) {
                aX = dx;
                aY = dy;
                aZ = dz;
            }
            else {
                bX = dx;
                bY = dy;
                bZ = dz;
            }
        }
        da = Math.fma(this.nzX, aX, Math.fma(this.nzY, aY, Math.fma(this.nzZ, aZ, this.nzW)));
        db = Math.fma(this.nzX, bX, Math.fma(this.nzY, bY, Math.fma(this.nzZ, bZ, this.nzW)));
        if (da < 0.0f && db < 0.0f) {
            return false;
        }
        if (da * db < 0.0f) {
            final float p = Math.abs(da) / Math.abs(db - da);
            final float dx = Math.fma(bX - aX, p, aX);
            final float dy = Math.fma(bY - aY, p, aY);
            final float dz = Math.fma(bZ - aZ, p, aZ);
            if (da < 0.0f) {
                aX = dx;
                aY = dy;
                aZ = dz;
            }
            else {
                bX = dx;
                bY = dy;
                bZ = dz;
            }
        }
        da = Math.fma(this.pzX, aX, Math.fma(this.pzY, aY, Math.fma(this.pzZ, aZ, this.pzW)));
        db = Math.fma(this.pzX, bX, Math.fma(this.pzY, bY, Math.fma(this.pzZ, bZ, this.pzW)));
        return da >= 0.0f || db >= 0.0f;
    }
}
