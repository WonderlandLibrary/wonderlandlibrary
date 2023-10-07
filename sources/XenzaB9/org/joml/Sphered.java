// 
// Decompiled by Procyon v0.6.0
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.text.NumberFormat;
import java.io.Externalizable;

public class Sphered implements Externalizable
{
    public double x;
    public double y;
    public double z;
    public double r;
    
    public Sphered() {
    }
    
    public Sphered(final Sphered source) {
        this.x = source.x;
        this.y = source.y;
        this.z = source.z;
        this.r = source.r;
    }
    
    public Sphered(final Vector3fc c, final double r) {
        this.x = c.x();
        this.y = c.y();
        this.z = c.z();
        this.r = r;
    }
    
    public Sphered(final Vector3dc c, final double r) {
        this.x = c.x();
        this.y = c.y();
        this.z = c.z();
        this.r = r;
    }
    
    public Sphered(final double x, final double y, final double z, final double r) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.r = r;
    }
    
    public Sphered translate(final Vector3dc xyz) {
        return this.translate(xyz.x(), xyz.y(), xyz.z(), this);
    }
    
    public Sphered translate(final Vector3dc xyz, final Sphered dest) {
        return this.translate(xyz.x(), xyz.y(), xyz.z(), dest);
    }
    
    public Sphered translate(final Vector3fc xyz) {
        return this.translate(xyz.x(), xyz.y(), xyz.z(), this);
    }
    
    public Sphered translate(final Vector3fc xyz, final Sphered dest) {
        return this.translate(xyz.x(), xyz.y(), xyz.z(), dest);
    }
    
    public Sphered translate(final double x, final double y, final double z) {
        return this.translate(x, y, z, this);
    }
    
    public Sphered translate(final double x, final double y, final double z, final Sphered dest) {
        dest.x = this.x + x;
        dest.y = this.y + y;
        dest.z = this.z + z;
        return dest;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp = Double.doubleToLongBits(this.r);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.x);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.y);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.z);
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
        final Sphered other = (Sphered)obj;
        return Double.doubleToLongBits(this.r) == Double.doubleToLongBits(other.r) && Double.doubleToLongBits(this.x) == Double.doubleToLongBits(other.x) && Double.doubleToLongBits(this.y) == Double.doubleToLongBits(other.y) && Double.doubleToLongBits(this.z) == Double.doubleToLongBits(other.z);
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "[" + Runtime.format(this.x, formatter) + " " + Runtime.format(this.y, formatter) + " " + Runtime.format(this.z, formatter) + " " + Runtime.format(this.r, formatter) + "]";
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
        out.writeDouble(this.r);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.r = in.readDouble();
    }
}
