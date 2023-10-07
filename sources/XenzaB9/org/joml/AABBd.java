// 
// Decompiled by Procyon v0.6.0
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.text.NumberFormat;
import java.io.Externalizable;

public class AABBd implements Externalizable
{
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;
    
    public AABBd() {
        this.minX = Double.POSITIVE_INFINITY;
        this.minY = Double.POSITIVE_INFINITY;
        this.minZ = Double.POSITIVE_INFINITY;
        this.maxX = Double.NEGATIVE_INFINITY;
        this.maxY = Double.NEGATIVE_INFINITY;
        this.maxZ = Double.NEGATIVE_INFINITY;
    }
    
    public AABBd(final AABBd source) {
        this.minX = Double.POSITIVE_INFINITY;
        this.minY = Double.POSITIVE_INFINITY;
        this.minZ = Double.POSITIVE_INFINITY;
        this.maxX = Double.NEGATIVE_INFINITY;
        this.maxY = Double.NEGATIVE_INFINITY;
        this.maxZ = Double.NEGATIVE_INFINITY;
        this.minX = source.minX;
        this.minY = source.minY;
        this.minZ = source.minZ;
        this.maxX = source.maxX;
        this.maxY = source.maxY;
        this.maxZ = source.maxZ;
    }
    
    public AABBd(final Vector3fc min, final Vector3fc max) {
        this.minX = Double.POSITIVE_INFINITY;
        this.minY = Double.POSITIVE_INFINITY;
        this.minZ = Double.POSITIVE_INFINITY;
        this.maxX = Double.NEGATIVE_INFINITY;
        this.maxY = Double.NEGATIVE_INFINITY;
        this.maxZ = Double.NEGATIVE_INFINITY;
        this.minX = min.x();
        this.minY = min.y();
        this.minZ = min.z();
        this.maxX = max.x();
        this.maxY = max.y();
        this.maxZ = max.z();
    }
    
    public AABBd(final Vector3dc min, final Vector3dc max) {
        this.minX = Double.POSITIVE_INFINITY;
        this.minY = Double.POSITIVE_INFINITY;
        this.minZ = Double.POSITIVE_INFINITY;
        this.maxX = Double.NEGATIVE_INFINITY;
        this.maxY = Double.NEGATIVE_INFINITY;
        this.maxZ = Double.NEGATIVE_INFINITY;
        this.minX = min.x();
        this.minY = min.y();
        this.minZ = min.z();
        this.maxX = max.x();
        this.maxY = max.y();
        this.maxZ = max.z();
    }
    
    public AABBd(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ) {
        this.minX = Double.POSITIVE_INFINITY;
        this.minY = Double.POSITIVE_INFINITY;
        this.minZ = Double.POSITIVE_INFINITY;
        this.maxX = Double.NEGATIVE_INFINITY;
        this.maxY = Double.NEGATIVE_INFINITY;
        this.maxZ = Double.NEGATIVE_INFINITY;
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }
    
    public AABBd set(final AABBd source) {
        this.minX = source.minX;
        this.minY = source.minY;
        this.minZ = source.minZ;
        this.maxX = source.maxX;
        this.maxY = source.maxY;
        this.maxZ = source.maxZ;
        return this;
    }
    
    private AABBd validate() {
        if (!this.isValid()) {
            this.minX = Double.POSITIVE_INFINITY;
            this.minY = Double.POSITIVE_INFINITY;
            this.minZ = Double.POSITIVE_INFINITY;
            this.maxX = Double.NEGATIVE_INFINITY;
            this.maxY = Double.NEGATIVE_INFINITY;
            this.maxZ = Double.NEGATIVE_INFINITY;
        }
        return this;
    }
    
    public boolean isValid() {
        return this.minX < this.maxX && this.minY < this.maxY && this.minZ < this.maxZ;
    }
    
    public AABBd setMin(final double minX, final double minY, final double minZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        return this;
    }
    
    public AABBd setMax(final double maxX, final double maxY, final double maxZ) {
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        return this;
    }
    
    public AABBd setMin(final Vector3dc min) {
        return this.setMin(min.x(), min.y(), min.z());
    }
    
    public AABBd setMax(final Vector3dc max) {
        return this.setMax(max.x(), max.y(), max.z());
    }
    
    public double getMax(final int component) throws IllegalArgumentException {
        switch (component) {
            case 0: {
                return this.maxX;
            }
            case 1: {
                return this.maxY;
            }
            case 2: {
                return this.maxZ;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public double getMin(final int component) throws IllegalArgumentException {
        switch (component) {
            case 0: {
                return this.minX;
            }
            case 1: {
                return this.minY;
            }
            case 2: {
                return this.minZ;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public AABBd union(final double x, final double y, final double z) {
        return this.union(x, y, z, this);
    }
    
    public AABBd union(final Vector3dc p) {
        return this.union(p.x(), p.y(), p.z(), this);
    }
    
    public AABBd union(final double x, final double y, final double z, final AABBd dest) {
        dest.minX = ((this.minX < x) ? this.minX : x);
        dest.minY = ((this.minY < y) ? this.minY : y);
        dest.minZ = ((this.minZ < z) ? this.minZ : z);
        dest.maxX = ((this.maxX > x) ? this.maxX : x);
        dest.maxY = ((this.maxY > y) ? this.maxY : y);
        dest.maxZ = ((this.maxZ > z) ? this.maxZ : z);
        return dest;
    }
    
    public AABBd union(final Vector3dc p, final AABBd dest) {
        return this.union(p.x(), p.y(), p.z(), dest);
    }
    
    public AABBd union(final AABBd other) {
        return this.union(other, this);
    }
    
    public AABBd union(final AABBd other, final AABBd dest) {
        dest.minX = ((this.minX < other.minX) ? this.minX : other.minX);
        dest.minY = ((this.minY < other.minY) ? this.minY : other.minY);
        dest.minZ = ((this.minZ < other.minZ) ? this.minZ : other.minZ);
        dest.maxX = ((this.maxX > other.maxX) ? this.maxX : other.maxX);
        dest.maxY = ((this.maxY > other.maxY) ? this.maxY : other.maxY);
        dest.maxZ = ((this.maxZ > other.maxZ) ? this.maxZ : other.maxZ);
        return dest;
    }
    
    public AABBd correctBounds() {
        if (this.minX > this.maxX) {
            final double tmp = this.minX;
            this.minX = this.maxX;
            this.maxX = tmp;
        }
        if (this.minY > this.maxY) {
            final double tmp = this.minY;
            this.minY = this.maxY;
            this.maxY = tmp;
        }
        if (this.minZ > this.maxZ) {
            final double tmp = this.minZ;
            this.minZ = this.maxZ;
            this.maxZ = tmp;
        }
        return this;
    }
    
    public AABBd translate(final Vector3dc xyz) {
        return this.translate(xyz.x(), xyz.y(), xyz.z(), this);
    }
    
    public AABBd translate(final Vector3dc xyz, final AABBd dest) {
        return this.translate(xyz.x(), xyz.y(), xyz.z(), dest);
    }
    
    public AABBd translate(final Vector3fc xyz) {
        return this.translate(xyz.x(), xyz.y(), xyz.z(), this);
    }
    
    public AABBd translate(final Vector3fc xyz, final AABBd dest) {
        return this.translate(xyz.x(), xyz.y(), xyz.z(), dest);
    }
    
    public AABBd translate(final double x, final double y, final double z) {
        return this.translate(x, y, z, this);
    }
    
    public AABBd translate(final double x, final double y, final double z, final AABBd dest) {
        dest.minX = this.minX + x;
        dest.minY = this.minY + y;
        dest.minZ = this.minZ + z;
        dest.maxX = this.maxX + x;
        dest.maxY = this.maxY + y;
        dest.maxZ = this.maxZ + z;
        return dest;
    }
    
    public AABBd intersection(final AABBd other, final AABBd dest) {
        dest.minX = Math.max(this.minX, other.minX);
        dest.minY = Math.max(this.minY, other.minY);
        dest.minZ = Math.max(this.minZ, other.minZ);
        dest.maxX = Math.min(this.maxX, other.maxX);
        dest.maxY = Math.min(this.maxY, other.maxY);
        dest.maxZ = Math.min(this.maxZ, other.maxZ);
        return dest.validate();
    }
    
    public AABBd intersection(final AABBd other) {
        return this.intersection(other, this);
    }
    
    public boolean containsAABB(final AABBd aabb) {
        return aabb.minX >= this.minX && aabb.maxX <= this.maxX && aabb.minY >= this.minY && aabb.maxY <= this.maxY && aabb.minZ >= this.minZ && aabb.maxZ <= this.maxZ;
    }
    
    public boolean containsAABB(final AABBf aabb) {
        return aabb.minX >= this.minX && aabb.maxX <= this.maxX && aabb.minY >= this.minY && aabb.maxY <= this.maxY && aabb.minZ >= this.minZ && aabb.maxZ <= this.maxZ;
    }
    
    public boolean containsAABB(final AABBi aabb) {
        return aabb.minX >= this.minX && aabb.maxX <= this.maxX && aabb.minY >= this.minY && aabb.maxY <= this.maxY && aabb.minZ >= this.minZ && aabb.maxZ <= this.maxZ;
    }
    
    public boolean containsPoint(final double x, final double y, final double z) {
        return x >= this.minX && y >= this.minY && z >= this.minZ && x <= this.maxX && y <= this.maxY && z <= this.maxZ;
    }
    
    public boolean containsPoint(final Vector3dc point) {
        return this.containsPoint(point.x(), point.y(), point.z());
    }
    
    public boolean intersectsPlane(final double a, final double b, final double c, final double d) {
        return Intersectiond.testAabPlane(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ, a, b, c, d);
    }
    
    public boolean intersectsPlane(final Planed plane) {
        return Intersectiond.testAabPlane(this, plane);
    }
    
    public boolean intersectsAABB(final AABBd other) {
        return this.maxX >= other.minX && this.maxY >= other.minY && this.maxZ >= other.minZ && this.minX <= other.maxX && this.minY <= other.maxY && this.minZ <= other.maxZ;
    }
    
    public boolean intersectsSphere(final double centerX, final double centerY, final double centerZ, final double radiusSquared) {
        return Intersectiond.testAabSphere(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ, centerX, centerY, centerZ, radiusSquared);
    }
    
    public boolean intersectsSphere(final Spheref sphere) {
        return Intersectiond.testAabSphere(this, sphere);
    }
    
    public boolean intersectsRay(final double originX, final double originY, final double originZ, final double dirX, final double dirY, final double dirZ) {
        return Intersectiond.testRayAab(originX, originY, originZ, dirX, dirY, dirZ, this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }
    
    public boolean intersectsRay(final Rayd ray) {
        return Intersectiond.testRayAab(ray, this);
    }
    
    public boolean intersectsRay(final double originX, final double originY, final double originZ, final double dirX, final double dirY, final double dirZ, final Vector2d result) {
        return Intersectiond.intersectRayAab(originX, originY, originZ, dirX, dirY, dirZ, this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ, result);
    }
    
    public boolean intersectsRay(final Rayd ray, final Vector2d result) {
        return Intersectiond.intersectRayAab(ray, this, result);
    }
    
    public int intersectsLineSegment(final double p0X, final double p0Y, final double p0Z, final double p1X, final double p1Y, final double p1Z, final Vector2d result) {
        return Intersectiond.intersectLineSegmentAab(p0X, p0Y, p0Z, p1X, p1Y, p1Z, this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ, result);
    }
    
    public int intersectsLineSegment(final LineSegmentf lineSegment, final Vector2d result) {
        return Intersectiond.intersectLineSegmentAab(lineSegment, this, result);
    }
    
    public AABBd transform(final Matrix4dc m) {
        return this.transform(m, this);
    }
    
    public AABBd transform(final Matrix4dc m, final AABBd dest) {
        final double dx = this.maxX - this.minX;
        final double dy = this.maxY - this.minY;
        final double dz = this.maxZ - this.minZ;
        double minx = Double.POSITIVE_INFINITY;
        double miny = Double.POSITIVE_INFINITY;
        double minz = Double.POSITIVE_INFINITY;
        double maxx = Double.NEGATIVE_INFINITY;
        double maxy = Double.NEGATIVE_INFINITY;
        double maxz = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < 8; ++i) {
            final double x = this.minX + (i & 0x1) * dx;
            final double y = this.minY + (i >> 1 & 0x1) * dy;
            final double z = this.minZ + (i >> 2 & 0x1) * dz;
            final double tx = m.m00() * x + m.m10() * y + m.m20() * z + m.m30();
            final double ty = m.m01() * x + m.m11() * y + m.m21() * z + m.m31();
            final double tz = m.m02() * x + m.m12() * y + m.m22() * z + m.m32();
            minx = Math.min(tx, minx);
            miny = Math.min(ty, miny);
            minz = Math.min(tz, minz);
            maxx = Math.max(tx, maxx);
            maxy = Math.max(ty, maxy);
            maxz = Math.max(tz, maxz);
        }
        dest.minX = minx;
        dest.minY = miny;
        dest.minZ = minz;
        dest.maxX = maxx;
        dest.maxY = maxy;
        dest.maxZ = maxz;
        return dest;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp = Double.doubleToLongBits(this.maxX);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.maxY);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.maxZ);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.minX);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.minY);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.minZ);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        return result;
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final AABBd other = (AABBd)obj;
        return Double.doubleToLongBits(this.maxX) == Double.doubleToLongBits(other.maxX) && Double.doubleToLongBits(this.maxY) == Double.doubleToLongBits(other.maxY) && Double.doubleToLongBits(this.maxZ) == Double.doubleToLongBits(other.maxZ) && Double.doubleToLongBits(this.minX) == Double.doubleToLongBits(other.minX) && Double.doubleToLongBits(this.minY) == Double.doubleToLongBits(other.minY) && Double.doubleToLongBits(this.minZ) == Double.doubleToLongBits(other.minZ);
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "(" + Runtime.format(this.minX, formatter) + " " + Runtime.format(this.minY, formatter) + " " + Runtime.format(this.minZ, formatter) + ") < (" + Runtime.format(this.maxX, formatter) + " " + Runtime.format(this.maxY, formatter) + " " + Runtime.format(this.maxZ, formatter) + ")";
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeDouble(this.minX);
        out.writeDouble(this.minY);
        out.writeDouble(this.minZ);
        out.writeDouble(this.maxX);
        out.writeDouble(this.maxY);
        out.writeDouble(this.maxZ);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.minX = in.readDouble();
        this.minY = in.readDouble();
        this.minZ = in.readDouble();
        this.maxX = in.readDouble();
        this.maxY = in.readDouble();
        this.maxZ = in.readDouble();
    }
}
