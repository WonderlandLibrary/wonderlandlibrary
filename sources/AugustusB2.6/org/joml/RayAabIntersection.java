// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

public class RayAabIntersection
{
    private float originX;
    private float originY;
    private float originZ;
    private float dirX;
    private float dirY;
    private float dirZ;
    private float c_xy;
    private float c_yx;
    private float c_zy;
    private float c_yz;
    private float c_xz;
    private float c_zx;
    private float s_xy;
    private float s_yx;
    private float s_zy;
    private float s_yz;
    private float s_xz;
    private float s_zx;
    private byte classification;
    
    public RayAabIntersection() {
    }
    
    public RayAabIntersection(final float originX, final float originY, final float originZ, final float dirX, final float dirY, final float dirZ) {
        this.set(originX, originY, originZ, dirX, dirY, dirZ);
    }
    
    public void set(final float originX, final float originY, final float originZ, final float dirX, final float dirY, final float dirZ) {
        this.originX = originX;
        this.originY = originY;
        this.originZ = originZ;
        this.dirX = dirX;
        this.dirY = dirY;
        this.dirZ = dirZ;
        this.precomputeSlope();
    }
    
    private static int signum(final float f) {
        return (f == 0.0f || Float.isNaN(f)) ? 0 : ((1 - Float.floatToIntBits(f) >>> 31 << 1) - 1);
    }
    
    private void precomputeSlope() {
        final float invDirX = 1.0f / this.dirX;
        final float invDirY = 1.0f / this.dirY;
        final float invDirZ = 1.0f / this.dirZ;
        this.s_yx = this.dirX * invDirY;
        this.s_xy = this.dirY * invDirX;
        this.s_zy = this.dirY * invDirZ;
        this.s_yz = this.dirZ * invDirY;
        this.s_xz = this.dirZ * invDirX;
        this.s_zx = this.dirX * invDirZ;
        this.c_xy = this.originY - this.s_xy * this.originX;
        this.c_yx = this.originX - this.s_yx * this.originY;
        this.c_zy = this.originY - this.s_zy * this.originZ;
        this.c_yz = this.originZ - this.s_yz * this.originY;
        this.c_xz = this.originZ - this.s_xz * this.originX;
        this.c_zx = this.originX - this.s_zx * this.originZ;
        final int sgnX = signum(this.dirX);
        final int sgnY = signum(this.dirY);
        final int sgnZ = signum(this.dirZ);
        this.classification = (byte)(sgnZ + 1 << 4 | sgnY + 1 << 2 | sgnX + 1);
    }
    
    public boolean test(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        switch (this.classification) {
            case 0: {
                return this.MMM(minX, minY, minZ, maxX, maxY, maxZ);
            }
            case 1: {
                return this.OMM(minX, minY, minZ, maxX, maxY, maxZ);
            }
            case 2: {
                return this.PMM(minX, minY, minZ, maxX, maxY, maxZ);
            }
            case 3: {
                return false;
            }
            case 4: {
                return this.MOM(minX, minY, minZ, maxX, maxY, maxZ);
            }
            case 5: {
                return this.OOM(minX, minY, minZ, maxX, maxY);
            }
            case 6: {
                return this.POM(minX, minY, minZ, maxX, maxY, maxZ);
            }
            case 7: {
                return false;
            }
            case 8: {
                return this.MPM(minX, minY, minZ, maxX, maxY, maxZ);
            }
            case 9: {
                return this.OPM(minX, minY, minZ, maxX, maxY, maxZ);
            }
            case 10: {
                return this.PPM(minX, minY, minZ, maxX, maxY, maxZ);
            }
            case 11:
            case 12:
            case 13:
            case 14:
            case 15: {
                return false;
            }
            case 16: {
                return this.MMO(minX, minY, minZ, maxX, maxY, maxZ);
            }
            case 17: {
                return this.OMO(minX, minY, minZ, maxX, maxZ);
            }
            case 18: {
                return this.PMO(minX, minY, minZ, maxX, maxY, maxZ);
            }
            case 19: {
                return false;
            }
            case 20: {
                return this.MOO(minX, minY, minZ, maxY, maxZ);
            }
            case 21: {
                return false;
            }
            case 22: {
                return this.POO(minY, minZ, maxX, maxY, maxZ);
            }
            case 23: {
                return false;
            }
            case 24: {
                return this.MPO(minX, minY, minZ, maxX, maxY, maxZ);
            }
            case 25: {
                return this.OPO(minX, minZ, maxX, maxY, maxZ);
            }
            case 26: {
                return this.PPO(minX, minY, minZ, maxX, maxY, maxZ);
            }
            case 27:
            case 28:
            case 29:
            case 30:
            case 31: {
                return false;
            }
            case 32: {
                return this.MMP(minX, minY, minZ, maxX, maxY, maxZ);
            }
            case 33: {
                return this.OMP(minX, minY, minZ, maxX, maxY, maxZ);
            }
            case 34: {
                return this.PMP(minX, minY, minZ, maxX, maxY, maxZ);
            }
            case 35: {
                return false;
            }
            case 36: {
                return this.MOP(minX, minY, minZ, maxX, maxY, maxZ);
            }
            case 37: {
                return this.OOP(minX, minY, maxX, maxY, maxZ);
            }
            case 38: {
                return this.POP(minX, minY, minZ, maxX, maxY, maxZ);
            }
            case 39: {
                return false;
            }
            case 40: {
                return this.MPP(minX, minY, minZ, maxX, maxY, maxZ);
            }
            case 41: {
                return this.OPP(minX, minY, minZ, maxX, maxY, maxZ);
            }
            case 42: {
                return this.PPP(minX, minY, minZ, maxX, maxY, maxZ);
            }
            default: {
                return false;
            }
        }
    }
    
    private boolean MMM(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return this.originX >= minX && this.originY >= minY && this.originZ >= minZ && this.s_xy * minX - maxY + this.c_xy <= 0.0f && this.s_yx * minY - maxX + this.c_yx <= 0.0f && this.s_zy * minZ - maxY + this.c_zy <= 0.0f && this.s_yz * minY - maxZ + this.c_yz <= 0.0f && this.s_xz * minX - maxZ + this.c_xz <= 0.0f && this.s_zx * minZ - maxX + this.c_zx <= 0.0f;
    }
    
    private boolean OMM(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return this.originX >= minX && this.originX <= maxX && this.originY >= minY && this.originZ >= minZ && this.s_zy * minZ - maxY + this.c_zy <= 0.0f && this.s_yz * minY - maxZ + this.c_yz <= 0.0f;
    }
    
    private boolean PMM(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return this.originX <= maxX && this.originY >= minY && this.originZ >= minZ && this.s_xy * maxX - maxY + this.c_xy <= 0.0f && this.s_yx * minY - minX + this.c_yx >= 0.0f && this.s_zy * minZ - maxY + this.c_zy <= 0.0f && this.s_yz * minY - maxZ + this.c_yz <= 0.0f && this.s_xz * maxX - maxZ + this.c_xz <= 0.0f && this.s_zx * minZ - minX + this.c_zx >= 0.0f;
    }
    
    private boolean MOM(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return this.originY >= minY && this.originY <= maxY && this.originX >= minX && this.originZ >= minZ && this.s_xz * minX - maxZ + this.c_xz <= 0.0f && this.s_zx * minZ - maxX + this.c_zx <= 0.0f;
    }
    
    private boolean OOM(final float minX, final float minY, final float minZ, final float maxX, final float maxY) {
        return this.originZ >= minZ && this.originX >= minX && this.originX <= maxX && this.originY >= minY && this.originY <= maxY;
    }
    
    private boolean POM(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return this.originY >= minY && this.originY <= maxY && this.originX <= maxX && this.originZ >= minZ && this.s_xz * maxX - maxZ + this.c_xz <= 0.0f && this.s_zx * minZ - minX + this.c_zx >= 0.0f;
    }
    
    private boolean MPM(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return this.originX >= minX && this.originY <= maxY && this.originZ >= minZ && this.s_xy * minX - minY + this.c_xy >= 0.0f && this.s_yx * maxY - maxX + this.c_yx <= 0.0f && this.s_zy * minZ - minY + this.c_zy >= 0.0f && this.s_yz * maxY - maxZ + this.c_yz <= 0.0f && this.s_xz * minX - maxZ + this.c_xz <= 0.0f && this.s_zx * minZ - maxX + this.c_zx <= 0.0f;
    }
    
    private boolean OPM(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return this.originX >= minX && this.originX <= maxX && this.originY <= maxY && this.originZ >= minZ && this.s_zy * minZ - minY + this.c_zy >= 0.0f && this.s_yz * maxY - maxZ + this.c_yz <= 0.0f;
    }
    
    private boolean PPM(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return this.originX <= maxX && this.originY <= maxY && this.originZ >= minZ && this.s_xy * maxX - minY + this.c_xy >= 0.0f && this.s_yx * maxY - minX + this.c_yx >= 0.0f && this.s_zy * minZ - minY + this.c_zy >= 0.0f && this.s_yz * maxY - maxZ + this.c_yz <= 0.0f && this.s_xz * maxX - maxZ + this.c_xz <= 0.0f && this.s_zx * minZ - minX + this.c_zx >= 0.0f;
    }
    
    private boolean MMO(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return this.originZ >= minZ && this.originZ <= maxZ && this.originX >= minX && this.originY >= minY && this.s_xy * minX - maxY + this.c_xy <= 0.0f && this.s_yx * minY - maxX + this.c_yx <= 0.0f;
    }
    
    private boolean OMO(final float minX, final float minY, final float minZ, final float maxX, final float maxZ) {
        return this.originY >= minY && this.originX >= minX && this.originX <= maxX && this.originZ >= minZ && this.originZ <= maxZ;
    }
    
    private boolean PMO(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return this.originZ >= minZ && this.originZ <= maxZ && this.originX <= maxX && this.originY >= minY && this.s_xy * maxX - maxY + this.c_xy <= 0.0f && this.s_yx * minY - minX + this.c_yx >= 0.0f;
    }
    
    private boolean MOO(final float minX, final float minY, final float minZ, final float maxY, final float maxZ) {
        return this.originX >= minX && this.originY >= minY && this.originY <= maxY && this.originZ >= minZ && this.originZ <= maxZ;
    }
    
    private boolean POO(final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return this.originX <= maxX && this.originY >= minY && this.originY <= maxY && this.originZ >= minZ && this.originZ <= maxZ;
    }
    
    private boolean MPO(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return this.originZ >= minZ && this.originZ <= maxZ && this.originX >= minX && this.originY <= maxY && this.s_xy * minX - minY + this.c_xy >= 0.0f && this.s_yx * maxY - maxX + this.c_yx <= 0.0f;
    }
    
    private boolean OPO(final float minX, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return this.originY <= maxY && this.originX >= minX && this.originX <= maxX && this.originZ >= minZ && this.originZ <= maxZ;
    }
    
    private boolean PPO(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return this.originZ >= minZ && this.originZ <= maxZ && this.originX <= maxX && this.originY <= maxY && this.s_xy * maxX - minY + this.c_xy >= 0.0f && this.s_yx * maxY - minX + this.c_yx >= 0.0f;
    }
    
    private boolean MMP(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return this.originX >= minX && this.originY >= minY && this.originZ <= maxZ && this.s_xy * minX - maxY + this.c_xy <= 0.0f && this.s_yx * minY - maxX + this.c_yx <= 0.0f && this.s_zy * maxZ - maxY + this.c_zy <= 0.0f && this.s_yz * minY - minZ + this.c_yz >= 0.0f && this.s_xz * minX - minZ + this.c_xz >= 0.0f && this.s_zx * maxZ - maxX + this.c_zx <= 0.0f;
    }
    
    private boolean OMP(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return this.originX >= minX && this.originX <= maxX && this.originY >= minY && this.originZ <= maxZ && this.s_zy * maxZ - maxY + this.c_zy <= 0.0f && this.s_yz * minY - minZ + this.c_yz >= 0.0f;
    }
    
    private boolean PMP(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return this.originX <= maxX && this.originY >= minY && this.originZ <= maxZ && this.s_xy * maxX - maxY + this.c_xy <= 0.0f && this.s_yx * minY - minX + this.c_yx >= 0.0f && this.s_zy * maxZ - maxY + this.c_zy <= 0.0f && this.s_yz * minY - minZ + this.c_yz >= 0.0f && this.s_xz * maxX - minZ + this.c_xz >= 0.0f && this.s_zx * maxZ - minX + this.c_zx >= 0.0f;
    }
    
    private boolean MOP(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return this.originY >= minY && this.originY <= maxY && this.originX >= minX && this.originZ <= maxZ && this.s_xz * minX - minZ + this.c_xz >= 0.0f && this.s_zx * maxZ - maxX + this.c_zx <= 0.0f;
    }
    
    private boolean OOP(final float minX, final float minY, final float maxX, final float maxY, final float maxZ) {
        return this.originZ <= maxZ && this.originX >= minX && this.originX <= maxX && this.originY >= minY && this.originY <= maxY;
    }
    
    private boolean POP(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return this.originY >= minY && this.originY <= maxY && this.originX <= maxX && this.originZ <= maxZ && this.s_xz * maxX - minZ + this.c_xz >= 0.0f && this.s_zx * maxZ - minX + this.c_zx <= 0.0f;
    }
    
    private boolean MPP(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return this.originX >= minX && this.originY <= maxY && this.originZ <= maxZ && this.s_xy * minX - minY + this.c_xy >= 0.0f && this.s_yx * maxY - maxX + this.c_yx <= 0.0f && this.s_zy * maxZ - minY + this.c_zy >= 0.0f && this.s_yz * maxY - minZ + this.c_yz >= 0.0f && this.s_xz * minX - minZ + this.c_xz >= 0.0f && this.s_zx * maxZ - maxX + this.c_zx <= 0.0f;
    }
    
    private boolean OPP(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return this.originX >= minX && this.originX <= maxX && this.originY <= maxY && this.originZ <= maxZ && this.s_zy * maxZ - minY + this.c_zy <= 0.0f && this.s_yz * maxY - minZ + this.c_yz <= 0.0f;
    }
    
    private boolean PPP(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return this.originX <= maxX && this.originY <= maxY && this.originZ <= maxZ && this.s_xy * maxX - minY + this.c_xy >= 0.0f && this.s_yx * maxY - minX + this.c_yx >= 0.0f && this.s_zy * maxZ - minY + this.c_zy >= 0.0f && this.s_yz * maxY - minZ + this.c_yz >= 0.0f && this.s_xz * maxX - minZ + this.c_xz >= 0.0f && this.s_zx * maxZ - minX + this.c_zx >= 0.0f;
    }
}
