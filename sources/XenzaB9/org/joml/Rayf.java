// 
// Decompiled by Procyon v0.6.0
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.text.NumberFormat;
import java.io.Externalizable;

public class Rayf implements Externalizable
{
    public float oX;
    public float oY;
    public float oZ;
    public float dX;
    public float dY;
    public float dZ;
    
    public Rayf() {
    }
    
    public Rayf(final Rayf source) {
        this.oX = source.oX;
        this.oY = source.oY;
        this.oZ = source.oZ;
        this.dX = source.dX;
        this.dY = source.dY;
        this.dZ = source.dZ;
    }
    
    public Rayf(final Vector3fc origin, final Vector3fc direction) {
        this.oX = origin.x();
        this.oY = origin.y();
        this.oZ = origin.z();
        this.dX = direction.x();
        this.dY = direction.y();
        this.dZ = direction.z();
    }
    
    public Rayf(final float oX, final float oY, final float oZ, final float dX, final float dY, final float dZ) {
        this.oX = oX;
        this.oY = oY;
        this.oZ = oZ;
        this.dX = dX;
        this.dY = dY;
        this.dZ = dZ;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + Float.floatToIntBits(this.dX);
        result = 31 * result + Float.floatToIntBits(this.dY);
        result = 31 * result + Float.floatToIntBits(this.dZ);
        result = 31 * result + Float.floatToIntBits(this.oX);
        result = 31 * result + Float.floatToIntBits(this.oY);
        result = 31 * result + Float.floatToIntBits(this.oZ);
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
        final Rayf other = (Rayf)obj;
        return Float.floatToIntBits(this.dX) == Float.floatToIntBits(other.dX) && Float.floatToIntBits(this.dY) == Float.floatToIntBits(other.dY) && Float.floatToIntBits(this.dZ) == Float.floatToIntBits(other.dZ) && Float.floatToIntBits(this.oX) == Float.floatToIntBits(other.oX) && Float.floatToIntBits(this.oY) == Float.floatToIntBits(other.oY) && Float.floatToIntBits(this.oZ) == Float.floatToIntBits(other.oZ);
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "(" + Runtime.format(this.oX, formatter) + " " + Runtime.format(this.oY, formatter) + " " + Runtime.format(this.oZ, formatter) + ") -> (" + Runtime.format(this.dX, formatter) + " " + Runtime.format(this.dY, formatter) + " " + Runtime.format(this.dZ, formatter) + ")";
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeFloat(this.oX);
        out.writeFloat(this.oY);
        out.writeFloat(this.oZ);
        out.writeFloat(this.dX);
        out.writeFloat(this.dY);
        out.writeFloat(this.dZ);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.oX = in.readFloat();
        this.oY = in.readFloat();
        this.oZ = in.readFloat();
        this.dX = in.readFloat();
        this.dY = in.readFloat();
        this.dZ = in.readFloat();
    }
}
