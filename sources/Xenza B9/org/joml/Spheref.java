// 
// Decompiled by Procyon v0.6.0
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.text.NumberFormat;
import java.io.Externalizable;

public class Spheref implements Externalizable
{
    public float x;
    public float y;
    public float z;
    public float r;
    
    public Spheref() {
    }
    
    public Spheref(final Spheref source) {
        this.x = source.x;
        this.y = source.y;
        this.z = source.z;
        this.r = source.r;
    }
    
    public Spheref(final Vector3fc c, final float r) {
        this.x = c.x();
        this.y = c.y();
        this.z = c.z();
        this.r = r;
    }
    
    public Spheref(final float x, final float y, final float z, final float r) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.r = r;
    }
    
    public Spheref translate(final Vector3fc xyz) {
        return this.translate(xyz.x(), xyz.y(), xyz.z(), this);
    }
    
    public Spheref translate(final Vector3fc xyz, final Spheref dest) {
        return this.translate(xyz.x(), xyz.y(), xyz.z(), dest);
    }
    
    public Spheref translate(final float x, final float y, final float z) {
        return this.translate(x, y, z, this);
    }
    
    public Spheref translate(final float x, final float y, final float z, final Spheref dest) {
        dest.x = this.x + x;
        dest.y = this.y + y;
        dest.z = this.z + z;
        return dest;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + Float.floatToIntBits(this.r);
        result = 31 * result + Float.floatToIntBits(this.x);
        result = 31 * result + Float.floatToIntBits(this.y);
        result = 31 * result + Float.floatToIntBits(this.z);
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
        final Spheref other = (Spheref)obj;
        return Float.floatToIntBits(this.r) == Float.floatToIntBits(other.r) && Float.floatToIntBits(this.x) == Float.floatToIntBits(other.x) && Float.floatToIntBits(this.y) == Float.floatToIntBits(other.y) && Float.floatToIntBits(this.z) == Float.floatToIntBits(other.z);
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "[" + Runtime.format(this.x, formatter) + " " + Runtime.format(this.y, formatter) + " " + Runtime.format(this.z, formatter) + " " + Runtime.format(this.r, formatter) + "]";
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeFloat(this.x);
        out.writeFloat(this.y);
        out.writeFloat(this.z);
        out.writeFloat(this.r);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.x = in.readFloat();
        this.y = in.readFloat();
        this.z = in.readFloat();
        this.r = in.readFloat();
    }
}
