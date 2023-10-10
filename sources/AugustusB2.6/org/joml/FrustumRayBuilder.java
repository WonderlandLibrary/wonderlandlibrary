// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

public class FrustumRayBuilder
{
    private float nxnyX;
    private float nxnyY;
    private float nxnyZ;
    private float pxnyX;
    private float pxnyY;
    private float pxnyZ;
    private float pxpyX;
    private float pxpyY;
    private float pxpyZ;
    private float nxpyX;
    private float nxpyY;
    private float nxpyZ;
    private float cx;
    private float cy;
    private float cz;
    
    public FrustumRayBuilder() {
    }
    
    public FrustumRayBuilder(final Matrix4fc m) {
        this.set(m);
    }
    
    public FrustumRayBuilder set(final Matrix4fc m) {
        final float nxX = m.m03() + m.m00();
        final float nxY = m.m13() + m.m10();
        final float nxZ = m.m23() + m.m20();
        final float d1 = m.m33() + m.m30();
        final float pxX = m.m03() - m.m00();
        final float pxY = m.m13() - m.m10();
        final float pxZ = m.m23() - m.m20();
        final float d2 = m.m33() - m.m30();
        final float nyX = m.m03() + m.m01();
        final float nyY = m.m13() + m.m11();
        final float nyZ = m.m23() + m.m21();
        final float pyX = m.m03() - m.m01();
        final float pyY = m.m13() - m.m11();
        final float pyZ = m.m23() - m.m21();
        final float d3 = m.m33() - m.m31();
        this.nxnyX = nyY * nxZ - nyZ * nxY;
        this.nxnyY = nyZ * nxX - nyX * nxZ;
        this.nxnyZ = nyX * nxY - nyY * nxX;
        this.pxnyX = pxY * nyZ - pxZ * nyY;
        this.pxnyY = pxZ * nyX - pxX * nyZ;
        this.pxnyZ = pxX * nyY - pxY * nyX;
        this.nxpyX = nxY * pyZ - nxZ * pyY;
        this.nxpyY = nxZ * pyX - nxX * pyZ;
        this.nxpyZ = nxX * pyY - nxY * pyX;
        this.pxpyX = pyY * pxZ - pyZ * pxY;
        this.pxpyY = pyZ * pxX - pyX * pxZ;
        this.pxpyZ = pyX * pxY - pyY * pxX;
        final float pxnxX = pxY * nxZ - pxZ * nxY;
        final float pxnxY = pxZ * nxX - pxX * nxZ;
        final float pxnxZ = pxX * nxY - pxY * nxX;
        final float invDot = 1.0f / (nxX * this.pxpyX + nxY * this.pxpyY + nxZ * this.pxpyZ);
        this.cx = (-this.pxpyX * d1 - this.nxpyX * d2 - pxnxX * d3) * invDot;
        this.cy = (-this.pxpyY * d1 - this.nxpyY * d2 - pxnxY * d3) * invDot;
        this.cz = (-this.pxpyZ * d1 - this.nxpyZ * d2 - pxnxZ * d3) * invDot;
        return this;
    }
    
    public Vector3fc origin(final Vector3f origin) {
        origin.x = this.cx;
        origin.y = this.cy;
        origin.z = this.cz;
        return origin;
    }
    
    public Vector3fc dir(final float x, final float y, final Vector3f dir) {
        final float y1x = this.nxnyX + (this.nxpyX - this.nxnyX) * y;
        final float y1y = this.nxnyY + (this.nxpyY - this.nxnyY) * y;
        final float y1z = this.nxnyZ + (this.nxpyZ - this.nxnyZ) * y;
        final float y2x = this.pxnyX + (this.pxpyX - this.pxnyX) * y;
        final float y2y = this.pxnyY + (this.pxpyY - this.pxnyY) * y;
        final float y2z = this.pxnyZ + (this.pxpyZ - this.pxnyZ) * y;
        final float dx = y1x + (y2x - y1x) * x;
        final float dy = y1y + (y2y - y1y) * x;
        final float dz = y1z + (y2z - y1z) * x;
        final float invLen = Math.invsqrt(dx * dx + dy * dy + dz * dz);
        dir.x = dx * invLen;
        dir.y = dy * invLen;
        dir.z = dz * invLen;
        return dir;
    }
}
