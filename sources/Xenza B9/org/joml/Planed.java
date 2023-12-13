// 
// Decompiled by Procyon v0.6.0
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.text.NumberFormat;
import java.io.Externalizable;

public class Planed implements Externalizable
{
    public double a;
    public double b;
    public double c;
    public double d;
    
    public Planed() {
    }
    
    public Planed(final Planed source) {
        this.a = source.a;
        this.b = source.b;
        this.c = source.c;
        this.d = source.d;
    }
    
    public Planed(final Vector3dc point, final Vector3dc normal) {
        this.a = normal.x();
        this.b = normal.y();
        this.c = normal.z();
        this.d = -this.a * point.x() - this.b * point.y() - this.c * point.z();
    }
    
    public Planed(final double a, final double b, final double c, final double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
    
    public Planed(final Vector3dc pointA, final Vector3dc pointB, final Vector3dc pointC) {
        final double abX = pointB.x() - pointA.x();
        final double abY = pointB.y() - pointA.y();
        final double abZ = pointB.z() - pointA.z();
        final double acX = pointC.x() - pointA.x();
        final double acY = pointC.y() - pointA.y();
        final double acZ = pointC.z() - pointA.z();
        this.a = abY * acZ - abZ * acY;
        this.b = abZ * acX - abX * acZ;
        this.c = abX * acY - abY * acX;
        this.d = -this.a * pointA.x() - this.b * pointA.y() - this.c * pointA.z();
    }
    
    public Planed(final Vector3fc pointA, final Vector3fc pointB, final Vector3fc pointC) {
        final double abX = pointB.x() - pointA.x();
        final double abY = pointB.y() - pointA.y();
        final double abZ = pointB.z() - pointA.z();
        final double acX = pointC.x() - pointA.x();
        final double acY = pointC.y() - pointA.y();
        final double acZ = pointC.z() - pointA.z();
        this.a = abY * acZ - abZ * acY;
        this.b = abZ * acX - abX * acZ;
        this.c = abX * acY - abY * acX;
        this.d = -this.a * pointA.x() - this.b * pointA.y() - this.c * pointA.z();
    }
    
    public Planed set(final double a, final double b, final double c, final double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        return this;
    }
    
    public Planed normalize() {
        return this.normalize(this);
    }
    
    public Planed normalize(final Planed dest) {
        final double invLength = Math.invsqrt(this.a * this.a + this.b * this.b + this.c * this.c);
        dest.a = this.a * invLength;
        dest.b = this.b * invLength;
        dest.c = this.c * invLength;
        dest.d = this.d * invLength;
        return dest;
    }
    
    public double distance(final double x, final double y, final double z) {
        return Intersectiond.distancePointPlane(x, y, z, this.a, this.b, this.c, this.d);
    }
    
    public static Vector4d equationFromPoints(final Vector3d v0, final Vector3d v1, final Vector3d v2, final Vector4d dest) {
        return equationFromPoints(v0.x, v0.y, v0.z, v1.x, v1.y, v1.z, v2.x, v2.y, v2.z, dest);
    }
    
    public static Vector4d equationFromPoints(final double v0X, final double v0Y, final double v0Z, final double v1X, final double v1Y, final double v1Z, final double v2X, final double v2Y, final double v2Z, final Vector4d dest) {
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
        dest.x = a;
        dest.y = b;
        dest.z = c;
        dest.w = d;
        return dest;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp = Double.doubleToLongBits(this.a);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.b);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.c);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.d);
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
        final Planed other = (Planed)obj;
        return Double.doubleToLongBits(this.a) == Double.doubleToLongBits(other.a) && Double.doubleToLongBits(this.b) == Double.doubleToLongBits(other.b) && Double.doubleToLongBits(this.c) == Double.doubleToLongBits(other.c) && Double.doubleToLongBits(this.d) == Double.doubleToLongBits(other.d);
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "[" + Runtime.format(this.a, formatter) + " " + Runtime.format(this.b, formatter) + " " + Runtime.format(this.c, formatter) + " " + Runtime.format(this.d, formatter) + "]";
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeDouble(this.a);
        out.writeDouble(this.b);
        out.writeDouble(this.c);
        out.writeDouble(this.d);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.a = in.readDouble();
        this.b = in.readDouble();
        this.c = in.readDouble();
        this.d = in.readDouble();
    }
}
