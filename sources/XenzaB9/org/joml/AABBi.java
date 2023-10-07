// 
// Decompiled by Procyon v0.6.0
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.Externalizable;

public class AABBi implements Externalizable
{
    public int minX;
    public int minY;
    public int minZ;
    public int maxX;
    public int maxY;
    public int maxZ;
    
    public AABBi() {
        this.minX = Integer.MAX_VALUE;
        this.minY = Integer.MAX_VALUE;
        this.minZ = Integer.MAX_VALUE;
        this.maxX = Integer.MIN_VALUE;
        this.maxY = Integer.MIN_VALUE;
        this.maxZ = Integer.MIN_VALUE;
    }
    
    public AABBi(final AABBi source) {
        this.minX = Integer.MAX_VALUE;
        this.minY = Integer.MAX_VALUE;
        this.minZ = Integer.MAX_VALUE;
        this.maxX = Integer.MIN_VALUE;
        this.maxY = Integer.MIN_VALUE;
        this.maxZ = Integer.MIN_VALUE;
        this.minX = source.minX;
        this.minY = source.minY;
        this.minZ = source.minZ;
        this.maxX = source.maxX;
        this.maxY = source.maxY;
        this.maxZ = source.maxZ;
    }
    
    public AABBi(final Vector3ic min, final Vector3ic max) {
        this.minX = Integer.MAX_VALUE;
        this.minY = Integer.MAX_VALUE;
        this.minZ = Integer.MAX_VALUE;
        this.maxX = Integer.MIN_VALUE;
        this.maxY = Integer.MIN_VALUE;
        this.maxZ = Integer.MIN_VALUE;
        this.minX = min.x();
        this.minY = min.y();
        this.minZ = min.z();
        this.maxX = max.x();
        this.maxY = max.y();
        this.maxZ = max.z();
    }
    
    public AABBi(final int minX, final int minY, final int minZ, final int maxX, final int maxY, final int maxZ) {
        this.minX = Integer.MAX_VALUE;
        this.minY = Integer.MAX_VALUE;
        this.minZ = Integer.MAX_VALUE;
        this.maxX = Integer.MIN_VALUE;
        this.maxY = Integer.MIN_VALUE;
        this.maxZ = Integer.MIN_VALUE;
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }
    
    public AABBi setMin(final int minX, final int minY, final int minZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        return this;
    }
    
    public AABBi setMax(final int maxX, final int maxY, final int maxZ) {
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        return this;
    }
    
    public AABBi set(final AABBi source) {
        this.minX = source.minX;
        this.minY = source.minY;
        this.minZ = source.minZ;
        this.maxX = source.maxX;
        this.maxY = source.maxY;
        this.maxZ = source.maxZ;
        return this;
    }
    
    private AABBi validate() {
        if (!this.isValid()) {
            this.minX = Integer.MAX_VALUE;
            this.minY = Integer.MAX_VALUE;
            this.minZ = Integer.MAX_VALUE;
            this.maxX = Integer.MIN_VALUE;
            this.maxY = Integer.MIN_VALUE;
            this.maxZ = Integer.MIN_VALUE;
        }
        return this;
    }
    
    public boolean isValid() {
        return this.minX < this.maxX && this.minY < this.maxY && this.minZ < this.maxZ;
    }
    
    public AABBi setMin(final Vector3ic min) {
        return this.setMin(min.x(), min.y(), min.z());
    }
    
    public AABBi setMax(final Vector3ic max) {
        return this.setMax(max.x(), max.y(), max.z());
    }
    
    public int getMax(final int component) throws IllegalArgumentException {
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
    
    public int getMin(final int component) throws IllegalArgumentException {
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
    
    public AABBi union(final int x, final int y, final int z) {
        return this.union(x, y, z, this);
    }
    
    public AABBi union(final Vector3ic p) {
        return this.union(p.x(), p.y(), p.z(), this);
    }
    
    public AABBi union(final int x, final int y, final int z, final AABBi dest) {
        dest.minX = ((this.minX < x) ? this.minX : x);
        dest.minY = ((this.minY < y) ? this.minY : y);
        dest.minZ = ((this.minZ < z) ? this.minZ : z);
        dest.maxX = ((this.maxX > x) ? this.maxX : x);
        dest.maxY = ((this.maxY > y) ? this.maxY : y);
        dest.maxZ = ((this.maxZ > z) ? this.maxZ : z);
        return dest;
    }
    
    public AABBi union(final Vector3ic p, final AABBi dest) {
        return this.union(p.x(), p.y(), p.z(), dest);
    }
    
    public AABBi union(final AABBi other) {
        return this.union(other, this);
    }
    
    public AABBi union(final AABBi other, final AABBi dest) {
        dest.minX = ((this.minX < other.minX) ? this.minX : other.minX);
        dest.minY = ((this.minY < other.minY) ? this.minY : other.minY);
        dest.minZ = ((this.minZ < other.minZ) ? this.minZ : other.minZ);
        dest.maxX = ((this.maxX > other.maxX) ? this.maxX : other.maxX);
        dest.maxY = ((this.maxY > other.maxY) ? this.maxY : other.maxY);
        dest.maxZ = ((this.maxZ > other.maxZ) ? this.maxZ : other.maxZ);
        return dest;
    }
    
    public AABBi correctBounds() {
        if (this.minX > this.maxX) {
            final int tmp = this.minX;
            this.minX = this.maxX;
            this.maxX = tmp;
        }
        if (this.minY > this.maxY) {
            final int tmp = this.minY;
            this.minY = this.maxY;
            this.maxY = tmp;
        }
        if (this.minZ > this.maxZ) {
            final int tmp = this.minZ;
            this.minZ = this.maxZ;
            this.maxZ = tmp;
        }
        return this;
    }
    
    public AABBi translate(final Vector3ic xyz) {
        return this.translate(xyz.x(), xyz.y(), xyz.z(), this);
    }
    
    public AABBi translate(final Vector3ic xyz, final AABBi dest) {
        return this.translate(xyz.x(), xyz.y(), xyz.z(), dest);
    }
    
    public AABBi translate(final int x, final int y, final int z) {
        return this.translate(x, y, z, this);
    }
    
    public AABBi translate(final int x, final int y, final int z, final AABBi dest) {
        dest.minX = this.minX + x;
        dest.minY = this.minY + y;
        dest.minZ = this.minZ + z;
        dest.maxX = this.maxX + x;
        dest.maxY = this.maxY + y;
        dest.maxZ = this.maxZ + z;
        return dest;
    }
    
    public AABBi intersection(final AABBi other, final AABBi dest) {
        dest.minX = Math.max(this.minX, other.minX);
        dest.minY = Math.max(this.minY, other.minY);
        dest.minZ = Math.max(this.minZ, other.minZ);
        dest.maxX = Math.min(this.maxX, other.maxX);
        dest.maxY = Math.min(this.maxY, other.maxY);
        dest.maxZ = Math.min(this.maxZ, other.maxZ);
        return dest.validate();
    }
    
    public AABBi intersection(final AABBi other) {
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
    
    public boolean containsPoint(final int x, final int y, final int z) {
        return x >= this.minX && y >= this.minY && z >= this.minZ && x <= this.maxX && y <= this.maxY && z <= this.maxZ;
    }
    
    public boolean containsPoint(final float x, final float y, final float z) {
        return x >= this.minX && y >= this.minY && z >= this.minZ && x <= this.maxX && y <= this.maxY && z <= this.maxZ;
    }
    
    public boolean containsPoint(final Vector3ic point) {
        return this.containsPoint(point.x(), point.y(), point.z());
    }
    
    public boolean containsPoint(final Vector3fc point) {
        return this.containsPoint(point.x(), point.y(), point.z());
    }
    
    public boolean intersectsPlane(final float a, final float b, final float c, final float d) {
        return Intersectionf.testAabPlane((float)this.minX, (float)this.minY, (float)this.minZ, (float)this.maxX, (float)this.maxY, (float)this.maxZ, a, b, c, d);
    }
    
    public boolean intersectsPlane(final Planef plane) {
        return Intersectionf.testAabPlane(this, plane);
    }
    
    public boolean intersectsAABB(final AABBi other) {
        return this.maxX >= other.minX && this.maxY >= other.minY && this.maxZ >= other.minZ && this.minX <= other.maxX && this.minY <= other.maxY && this.minZ <= other.maxZ;
    }
    
    public boolean intersectsAABB(final AABBf other) {
        return this.maxX >= other.minX && this.maxY >= other.minY && this.maxZ >= other.minZ && this.minX <= other.maxX && this.minY <= other.maxY && this.minZ <= other.maxZ;
    }
    
    public boolean intersectsSphere(final float centerX, final float centerY, final float centerZ, final float radiusSquared) {
        return Intersectionf.testAabSphere((float)this.minX, (float)this.minY, (float)this.minZ, (float)this.maxX, (float)this.maxY, (float)this.maxZ, centerX, centerY, centerZ, radiusSquared);
    }
    
    public boolean intersectsSphere(final Spheref sphere) {
        return Intersectionf.testAabSphere(this, sphere);
    }
    
    public boolean intersectsRay(final float originX, final float originY, final float originZ, final float dirX, final float dirY, final float dirZ) {
        return Intersectionf.testRayAab(originX, originY, originZ, dirX, dirY, dirZ, (float)this.minX, (float)this.minY, (float)this.minZ, (float)this.maxX, (float)this.maxY, (float)this.maxZ);
    }
    
    public boolean intersectsRay(final Rayf ray) {
        return Intersectionf.testRayAab(ray, this);
    }
    
    public boolean intersectsRay(final float originX, final float originY, final float originZ, final float dirX, final float dirY, final float dirZ, final Vector2f result) {
        return Intersectionf.intersectRayAab(originX, originY, originZ, dirX, dirY, dirZ, (float)this.minX, (float)this.minY, (float)this.minZ, (float)this.maxX, (float)this.maxY, (float)this.maxZ, result);
    }
    
    public boolean intersectsRay(final Rayf ray, final Vector2f result) {
        return Intersectionf.intersectRayAab(ray, this, result);
    }
    
    public int intersectLineSegment(final float p0X, final float p0Y, final float p0Z, final float p1X, final float p1Y, final float p1Z, final Vector2f result) {
        return Intersectionf.intersectLineSegmentAab(p0X, p0Y, p0Z, p1X, p1Y, p1Z, (float)this.minX, (float)this.minY, (float)this.minZ, (float)this.maxX, (float)this.maxY, (float)this.maxZ, result);
    }
    
    public int intersectLineSegment(final LineSegmentf lineSegment, final Vector2f result) {
        return Intersectionf.intersectLineSegmentAab(lineSegment, this, result);
    }
    
    public AABBi transform(final Matrix4fc m) {
        return this.transform(m, this);
    }
    
    public AABBi transform(final Matrix4fc m, final AABBi dest) {
        final float dx = (float)(this.maxX - this.minX);
        final float dy = (float)(this.maxY - this.minY);
        final float dz = (float)(this.maxZ - this.minZ);
        float minx = Float.POSITIVE_INFINITY;
        float miny = Float.POSITIVE_INFINITY;
        float minz = Float.POSITIVE_INFINITY;
        float maxx = Float.NEGATIVE_INFINITY;
        float maxy = Float.NEGATIVE_INFINITY;
        float maxz = Float.NEGATIVE_INFINITY;
        for (int i = 0; i < 8; ++i) {
            final float x = this.minX + (i & 0x1) * dx;
            final float y = this.minY + (i >> 1 & 0x1) * dy;
            final float z = this.minZ + (i >> 2 & 0x1) * dz;
            final float tx = m.m00() * x + m.m10() * y + m.m20() * z + m.m30();
            final float ty = m.m01() * x + m.m11() * y + m.m21() * z + m.m31();
            final float tz = m.m02() * x + m.m12() * y + m.m22() * z + m.m32();
            minx = Math.min(tx, minx);
            miny = Math.min(ty, miny);
            minz = Math.min(tz, minz);
            maxx = Math.max(tx, maxx);
            maxy = Math.max(ty, maxy);
            maxz = Math.max(tz, maxz);
        }
        dest.minX = Math.roundUsing(minx, 2);
        dest.minY = Math.roundUsing(miny, 2);
        dest.minZ = Math.roundUsing(minz, 2);
        dest.maxX = Math.roundUsing(maxx, 1);
        dest.maxY = Math.roundUsing(maxy, 1);
        dest.maxZ = Math.roundUsing(maxz, 1);
        return dest;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + this.minX;
        result = 31 * result + this.minY;
        result = 31 * result + this.minZ;
        result = 31 * result + this.maxX;
        result = 31 * result + this.maxY;
        result = 31 * result + this.maxZ;
        return result;
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final AABBi aabBi = (AABBi)obj;
        return this.minX == aabBi.minX && this.minY == aabBi.minY && this.minZ == aabBi.minZ && this.maxX == aabBi.maxX && this.maxY == aabBi.maxY && this.maxZ == aabBi.maxZ;
    }
    
    public String toString() {
        return "(" + this.minX + " " + this.minY + " " + this.minZ + ") < (" + this.maxX + " " + this.maxY + " " + this.maxZ + ")";
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeInt(this.minX);
        out.writeInt(this.minY);
        out.writeInt(this.minZ);
        out.writeInt(this.maxX);
        out.writeInt(this.maxY);
        out.writeInt(this.maxZ);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.minX = in.readInt();
        this.minY = in.readInt();
        this.minZ = in.readInt();
        this.maxX = in.readInt();
        this.maxY = in.readInt();
        this.maxZ = in.readInt();
    }
}
