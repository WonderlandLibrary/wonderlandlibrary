// 
// Decompiled by Procyon v0.6.0
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.text.NumberFormat;
import java.io.Externalizable;

public class LineSegmentd implements Externalizable
{
    public double aX;
    public double aY;
    public double aZ;
    public double bX;
    public double bY;
    public double bZ;
    
    public LineSegmentd() {
    }
    
    public LineSegmentd(final LineSegmentd source) {
        this.aX = source.aX;
        this.aY = source.aY;
        this.aZ = source.aZ;
        this.aX = source.bX;
        this.bY = source.bY;
        this.bZ = source.bZ;
    }
    
    public LineSegmentd(final Vector3dc a, final Vector3dc b) {
        this.aX = a.x();
        this.aY = a.y();
        this.aZ = a.z();
        this.bX = b.x();
        this.bY = b.y();
        this.bZ = b.z();
    }
    
    public LineSegmentd(final double aX, final double aY, final double aZ, final double bX, final double bY, final double bZ) {
        this.aX = aX;
        this.aY = aY;
        this.aZ = aZ;
        this.bX = bX;
        this.bY = bY;
        this.bZ = bZ;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp = Double.doubleToLongBits(this.aX);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.aY);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.aZ);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.bX);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.bY);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.bZ);
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
        final LineSegmentd other = (LineSegmentd)obj;
        return Double.doubleToLongBits(this.aX) == Double.doubleToLongBits(other.aX) && Double.doubleToLongBits(this.aY) == Double.doubleToLongBits(other.aY) && Double.doubleToLongBits(this.aZ) == Double.doubleToLongBits(other.aZ) && Double.doubleToLongBits(this.bX) == Double.doubleToLongBits(other.bX) && Double.doubleToLongBits(this.bY) == Double.doubleToLongBits(other.bY) && Double.doubleToLongBits(this.bZ) == Double.doubleToLongBits(other.bZ);
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "(" + Runtime.format(this.aX, formatter) + " " + Runtime.format(this.aY, formatter) + " " + Runtime.format(this.aZ, formatter) + ") - (" + Runtime.format(this.bX, formatter) + " " + Runtime.format(this.bY, formatter) + " " + Runtime.format(this.bZ, formatter) + ")";
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeDouble(this.aX);
        out.writeDouble(this.aY);
        out.writeDouble(this.aZ);
        out.writeDouble(this.bX);
        out.writeDouble(this.bY);
        out.writeDouble(this.bZ);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.aX = in.readDouble();
        this.aY = in.readDouble();
        this.aZ = in.readDouble();
        this.bX = in.readDouble();
        this.bY = in.readDouble();
        this.bZ = in.readDouble();
    }
}
