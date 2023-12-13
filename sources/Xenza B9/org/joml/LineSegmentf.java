// 
// Decompiled by Procyon v0.6.0
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.text.NumberFormat;
import java.io.Externalizable;

public class LineSegmentf implements Externalizable
{
    public float aX;
    public float aY;
    public float aZ;
    public float bX;
    public float bY;
    public float bZ;
    
    public LineSegmentf() {
    }
    
    public LineSegmentf(final LineSegmentf source) {
        this.aX = source.aX;
        this.aY = source.aY;
        this.aZ = source.aZ;
        this.aX = source.bX;
        this.bY = source.bY;
        this.bZ = source.bZ;
    }
    
    public LineSegmentf(final Vector3fc a, final Vector3fc b) {
        this.aX = a.x();
        this.aY = a.y();
        this.aZ = a.z();
        this.bX = b.x();
        this.bY = b.y();
        this.bZ = b.z();
    }
    
    public LineSegmentf(final float aX, final float aY, final float aZ, final float bX, final float bY, final float bZ) {
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
        result = 31 * result + Float.floatToIntBits(this.aX);
        result = 31 * result + Float.floatToIntBits(this.aY);
        result = 31 * result + Float.floatToIntBits(this.aZ);
        result = 31 * result + Float.floatToIntBits(this.bX);
        result = 31 * result + Float.floatToIntBits(this.bY);
        result = 31 * result + Float.floatToIntBits(this.bZ);
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
        final LineSegmentf other = (LineSegmentf)obj;
        return Float.floatToIntBits(this.aX) == Float.floatToIntBits(other.aX) && Float.floatToIntBits(this.aY) == Float.floatToIntBits(other.aY) && Float.floatToIntBits(this.aZ) == Float.floatToIntBits(other.aZ) && Float.floatToIntBits(this.bX) == Float.floatToIntBits(other.bX) && Float.floatToIntBits(this.bY) == Float.floatToIntBits(other.bY) && Float.floatToIntBits(this.bZ) == Float.floatToIntBits(other.bZ);
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "(" + Runtime.format(this.aX, formatter) + " " + Runtime.format(this.aY, formatter) + " " + Runtime.format(this.aZ, formatter) + ") - (" + Runtime.format(this.bX, formatter) + " " + Runtime.format(this.bY, formatter) + " " + Runtime.format(this.bZ, formatter) + ")";
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeFloat(this.aX);
        out.writeFloat(this.aY);
        out.writeFloat(this.aZ);
        out.writeFloat(this.bX);
        out.writeFloat(this.bY);
        out.writeFloat(this.bZ);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.aX = in.readFloat();
        this.aY = in.readFloat();
        this.aZ = in.readFloat();
        this.bX = in.readFloat();
        this.bY = in.readFloat();
        this.bZ = in.readFloat();
    }
}
