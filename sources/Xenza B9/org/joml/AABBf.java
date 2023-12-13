// 
// Decompiled by Procyon v0.6.0
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.text.NumberFormat;
import java.io.Externalizable;

public class AABBf implements Externalizable
{
    public float minX;
    public float minY;
    public float minZ;
    public float maxX;
    public float maxY;
    public float maxZ;
    
    public AABBf() {
        this.minX = Float.POSITIVE_INFINITY;
        this.minY = Float.POSITIVE_INFINITY;
        this.minZ = Float.POSITIVE_INFINITY;
        this.maxX = Float.NEGATIVE_INFINITY;
        this.maxY = Float.NEGATIVE_INFINITY;
        this.maxZ = Float.NEGATIVE_INFINITY;
    }
    
    public AABBf(final AABBf source) {
        this.minX = Float.POSITIVE_INFINITY;
        this.minY = Float.POSITIVE_INFINITY;
        this.minZ = Float.POSITIVE_INFINITY;
        this.maxX = Float.NEGATIVE_INFINITY;
        this.maxY = Float.NEGATIVE_INFINITY;
        this.maxZ = Float.NEGATIVE_INFINITY;
        this.minX = source.minX;
        this.minY = source.minY;
        this.minZ = source.minZ;
        this.maxX = source.maxX;
        this.maxY = source.maxY;
        this.maxZ = source.maxZ;
    }
    
    public AABBf(final Vector3fc min, final Vector3fc max) {
        this.minX = Float.POSITIVE_INFINITY;
        this.minY = Float.POSITIVE_INFINITY;
        this.minZ = Float.POSITIVE_INFINITY;
        this.maxX = Float.NEGATIVE_INFINITY;
        this.maxY = Float.NEGATIVE_INFINITY;
        this.maxZ = Float.NEGATIVE_INFINITY;
        this.minX = min.x();
        this.minY = min.y();
        this.minZ = min.z();
        this.maxX = max.x();
        this.maxY = max.y();
        this.maxZ = max.z();
    }
    
    public AABBf(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        this.minX = Float.POSITIVE_INFINITY;
        this.minY = Float.POSITIVE_INFINITY;
        this.minZ = Float.POSITIVE_INFINITY;
        this.maxX = Float.NEGATIVE_INFINITY;
        this.maxY = Float.NEGATIVE_INFINITY;
        this.maxZ = Float.NEGATIVE_INFINITY;
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }
    
    public AABBf set(final AABBf source) {
        this.minX = source.minX;
        this.minY = source.minY;
        this.minZ = source.minZ;
        this.maxX = source.maxX;
        this.maxY = source.maxY;
        this.maxZ = source.maxZ;
        return this;
    }
    
    private AABBf validate() {
        if (!this.isValid()) {
            this.minX = Float.POSITIVE_INFINITY;
            this.minY = Float.POSITIVE_INFINITY;
            this.minZ = Float.POSITIVE_INFINITY;
            this.maxX = Float.NEGATIVE_INFINITY;
            this.maxY = Float.NEGATIVE_INFINITY;
            this.maxZ = Float.NEGATIVE_INFINITY;
        }
        return this;
    }
    
    public boolean isValid() {
        return this.minX < this.maxX && this.minY < this.maxY && this.minZ < this.maxZ;
    }
    
    public AABBf setMin(final float minX, final float minY, final float minZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        return this;
    }
    
    public AABBf setMax(final float maxX, final float maxY, final float maxZ) {
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        return this;
    }
    
    public AABBf setMin(final Vector3fc min) {
        return this.setMin(min.x(), min.y(), min.z());
    }
    
    public AABBf setMax(final Vector3fc max) {
        return this.setMax(max.x(), max.y(), max.z());
    }
    
    public float getMax(final int component) throws IllegalArgumentException {
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
    
    public float getMin(final int component) throws IllegalArgumentException {
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
    
    public AABBf union(final float x, final float y, final float z) {
        return this.union(x, y, z, this);
    }
    
    public AABBf union(final Vector3fc p) {
        return this.union(p.x(), p.y(), p.z(), this);
    }
    
    public AABBf union(final float x, final float y, final float z, final AABBf dest) {
        dest.minX = ((this.minX < x) ? this.minX : x);
        dest.minY = ((this.minY < y) ? this.minY : y);
        dest.minZ = ((this.minZ < z) ? this.minZ : z);
        dest.maxX = ((this.maxX > x) ? this.maxX : x);
        dest.maxY = ((this.maxY > y) ? this.maxY : y);
        dest.maxZ = ((this.maxZ > z) ? this.maxZ : z);
        return dest;
    }
    
    public AABBf union(final Vector3fc p, final AABBf dest) {
        return this.union(p.x(), p.y(), p.z(), dest);
    }
    
    public AABBf union(final AABBf other) {
        return this.union(other, this);
    }
    
    public AABBf union(final AABBf other, final AABBf dest) {
        dest.minX = ((this.minX < other.minX) ? this.minX : other.minX);
        dest.minY = ((this.minY < other.minY) ? this.minY : other.minY);
        dest.minZ = ((this.minZ < other.minZ) ? this.minZ : other.minZ);
        dest.maxX = ((this.maxX > other.maxX) ? this.maxX : other.maxX);
        dest.maxY = ((this.maxY > other.maxY) ? this.maxY : other.maxY);
        dest.maxZ = ((this.maxZ > other.maxZ) ? this.maxZ : other.maxZ);
        return dest;
    }
    
    public AABBf correctBounds() {
        if (this.minX > this.maxX) {
            final float tmp = this.minX;
            this.minX = this.maxX;
            this.maxX = tmp;
        }
        if (this.minY > this.maxY) {
            final float tmp = this.minY;
            this.minY = this.maxY;
            this.maxY = tmp;
        }
        if (this.minZ > this.maxZ) {
            final float tmp = this.minZ;
            this.minZ = this.maxZ;
            this.maxZ = tmp;
        }
        return this;
    }
    
    public AABBf translate(final Vector3fc xyz) {
        return this.translate(xyz.x(), xyz.y(), xyz.z(), this);
    }
    
    public AABBf translate(final Vector3fc xyz, final AABBf dest) {
        return this.translate(xyz.x(), xyz.y(), xyz.z(), dest);
    }
    
    public AABBf translate(final float x, final float y, final float z) {
        return this.translate(x, y, z, this);
    }
    
    public AABBf translate(final float x, final float y, final float z, final AABBf dest) {
        dest.minX = this.minX + x;
        dest.minY = this.minY + y;
        dest.minZ = this.minZ + z;
        dest.maxX = this.maxX + x;
        dest.maxY = this.maxY + y;
        dest.maxZ = this.maxZ + z;
        return dest;
    }
    
    public AABBf intersection(final AABBf other, final AABBf dest) {
        dest.minX = Math.max(this.minX, other.minX);
        dest.minY = Math.max(this.minY, other.minY);
        dest.minZ = Math.max(this.minZ, other.minZ);
        dest.maxX = Math.min(this.maxX, other.maxX);
        dest.maxY = Math.min(this.maxY, other.maxY);
        dest.maxZ = Math.min(this.maxZ, other.maxZ);
        return dest.validate();
    }
    
    public AABBf intersection(final AABBf other) {
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
    
    public boolean containsPoint(final float x, final float y, final float z) {
        return x >= this.minX && y >= this.minY && z >= this.minZ && x <= this.maxX && y <= this.maxY && z <= this.maxZ;
    }
    
    public boolean containsPoint(final Vector3fc point) {
        return this.containsPoint(point.x(), point.y(), point.z());
    }
    
    public boolean intersectsPlane(final float a, final float b, final float c, final float d) {
        return Intersectionf.testAabPlane(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ, a, b, c, d);
    }
    
    public boolean intersectsPlane(final Planef plane) {
        return Intersectionf.testAabPlane(this, plane);
    }
    
    public boolean intersectsAABB(final AABBf other) {
        return this.maxX >= other.minX && this.maxY >= other.minY && this.maxZ >= other.minZ && this.minX <= other.maxX && this.minY <= other.maxY && this.minZ <= other.maxZ;
    }
    
    public boolean intersectsSphere(final float centerX, final float centerY, final float centerZ, final float radiusSquared) {
        return Intersectionf.testAabSphere(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ, centerX, centerY, centerZ, radiusSquared);
    }
    
    public boolean intersectsSphere(final Spheref sphere) {
        return Intersectionf.testAabSphere(this, sphere);
    }
    
    public boolean intersectsRay(final float originX, final float originY, final float originZ, final float dirX, final float dirY, final float dirZ) {
        return Intersectionf.testRayAab(originX, originY, originZ, dirX, dirY, dirZ, this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }
    
    public boolean intersectsRay(final Rayf ray) {
        return Intersectionf.testRayAab(ray, this);
    }
    
    public boolean intersectsRay(final float originX, final float originY, final float originZ, final float dirX, final float dirY, final float dirZ, final Vector2f result) {
        return Intersectionf.intersectRayAab(originX, originY, originZ, dirX, dirY, dirZ, this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ, result);
    }
    
    public boolean intersectsRay(final Rayf ray, final Vector2f result) {
        return Intersectionf.intersectRayAab(ray, this, result);
    }
    
    public int intersectsLineSegment(final float p0X, final float p0Y, final float p0Z, final float p1X, final float p1Y, final float p1Z, final Vector2f result) {
        return Intersectionf.intersectLineSegmentAab(p0X, p0Y, p0Z, p1X, p1Y, p1Z, this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ, result);
    }
    
    public int intersectsLineSegment(final LineSegmentf lineSegment, final Vector2f result) {
        return Intersectionf.intersectLineSegmentAab(lineSegment, this, result);
    }
    
    public AABBf transform(final Matrix4fc m) {
        return this.transform(m, this);
    }
    
    public AABBf transform(final Matrix4fc m, final AABBf dest) {
        final float dx = this.maxX - this.minX;
        final float dy = this.maxY - this.minY;
        final float dz = this.maxZ - this.minZ;
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
        result = 31 * result + Float.floatToIntBits(this.maxX);
        result = 31 * result + Float.floatToIntBits(this.maxY);
        result = 31 * result + Float.floatToIntBits(this.maxZ);
        result = 31 * result + Float.floatToIntBits(this.minX);
        result = 31 * result + Float.floatToIntBits(this.minY);
        result = 31 * result + Float.floatToIntBits(this.minZ);
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
        final AABBf other = (AABBf)obj;
        return Float.floatToIntBits(this.maxX) == Float.floatToIntBits(other.maxX) && Float.floatToIntBits(this.maxY) == Float.floatToIntBits(other.maxY) && Float.floatToIntBits(this.maxZ) == Float.floatToIntBits(other.maxZ) && Float.floatToIntBits(this.minX) == Float.floatToIntBits(other.minX) && Float.floatToIntBits(this.minY) == Float.floatToIntBits(other.minY) && Float.floatToIntBits(this.minZ) == Float.floatToIntBits(other.minZ);
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "(" + Runtime.format(this.minX, formatter) + " " + Runtime.format(this.minY, formatter) + " " + Runtime.format(this.minZ, formatter) + ") < (" + Runtime.format(this.maxX, formatter) + " " + Runtime.format(this.maxY, formatter) + " " + Runtime.format(this.maxZ, formatter) + ")";
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeFloat(this.minX);
        out.writeFloat(this.minY);
        out.writeFloat(this.minZ);
        out.writeFloat(this.maxX);
        out.writeFloat(this.maxY);
        out.writeFloat(this.maxZ);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.minX = in.readFloat();
        this.minY = in.readFloat();
        this.minZ = in.readFloat();
        this.maxX = in.readFloat();
        this.maxY = in.readFloat();
        this.maxZ = in.readFloat();
    }
}
