// 
// Decompiled by Procyon v0.6.0
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.text.NumberFormat;
import java.io.Externalizable;

public class Planef implements Externalizable
{
    public float a;
    public float b;
    public float c;
    public float d;
    
    public Planef() {
    }
    
    public Planef(final Planef source) {
        this.a = source.a;
        this.b = source.b;
        this.c = source.c;
        this.d = source.d;
    }
    
    public Planef(final Vector3fc point, final Vector3fc normal) {
        this.a = normal.x();
        this.b = normal.y();
        this.c = normal.z();
        this.d = -this.a * point.x() - this.b * point.y() - this.c * point.z();
    }
    
    public Planef(final Vector3fc pointA, final Vector3fc pointB, final Vector3fc pointC) {
        final float abX = pointB.x() - pointA.x();
        final float abY = pointB.y() - pointA.y();
        final float abZ = pointB.z() - pointA.z();
        final float acX = pointC.x() - pointA.x();
        final float acY = pointC.y() - pointA.y();
        final float acZ = pointC.z() - pointA.z();
        this.a = abY * acZ - abZ * acY;
        this.b = abZ * acX - abX * acZ;
        this.c = abX * acY - abY * acX;
        this.d = -this.a * pointA.x() - this.b * pointA.y() - this.c * pointA.z();
    }
    
    public Planef(final float a, final float b, final float c, final float d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
    
    public Planef set(final float a, final float b, final float c, final float d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        return this;
    }
    
    public Planef normalize() {
        return this.normalize(this);
    }
    
    public Planef normalize(final Planef dest) {
        final float invLength = Math.invsqrt(this.a * this.a + this.b * this.b + this.c * this.c);
        dest.a = this.a * invLength;
        dest.b = this.b * invLength;
        dest.c = this.c * invLength;
        dest.d = this.d * invLength;
        return dest;
    }
    
    public float distance(final float x, final float y, final float z) {
        return Intersectionf.distancePointPlane(x, y, z, this.a, this.b, this.c, this.d);
    }
    
    public static Vector4f equationFromPoints(final Vector3f v0, final Vector3f v1, final Vector3f v2, final Vector4f dest) {
        return equationFromPoints(v0.x, v0.y, v0.z, v1.x, v1.y, v1.z, v2.x, v2.y, v2.z, dest);
    }
    
    public static Vector4f equationFromPoints(final float v0X, final float v0Y, final float v0Z, final float v1X, final float v1Y, final float v1Z, final float v2X, final float v2Y, final float v2Z, final Vector4f dest) {
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
        dest.x = a;
        dest.y = b;
        dest.z = c;
        dest.w = d;
        return dest;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + Float.floatToIntBits(this.a);
        result = 31 * result + Float.floatToIntBits(this.b);
        result = 31 * result + Float.floatToIntBits(this.c);
        result = 31 * result + Float.floatToIntBits(this.d);
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
        final Planef other = (Planef)obj;
        return Float.floatToIntBits(this.a) == Float.floatToIntBits(other.a) && Float.floatToIntBits(this.b) == Float.floatToIntBits(other.b) && Float.floatToIntBits(this.c) == Float.floatToIntBits(other.c) && Float.floatToIntBits(this.d) == Float.floatToIntBits(other.d);
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "[" + Runtime.format(this.a, formatter) + " " + Runtime.format(this.b, formatter) + " " + Runtime.format(this.c, formatter) + " " + Runtime.format(this.d, formatter) + "]";
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeFloat(this.a);
        out.writeFloat(this.b);
        out.writeFloat(this.c);
        out.writeFloat(this.d);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.a = in.readFloat();
        this.b = in.readFloat();
        this.c = in.readFloat();
        this.d = in.readFloat();
    }
}
