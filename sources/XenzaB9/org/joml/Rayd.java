// 
// Decompiled by Procyon v0.6.0
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.text.NumberFormat;
import java.io.Externalizable;

public class Rayd implements Externalizable
{
    public double oX;
    public double oY;
    public double oZ;
    public double dX;
    public double dY;
    public double dZ;
    
    public Rayd() {
    }
    
    public Rayd(final Rayd source) {
        this.oX = source.oX;
        this.oY = source.oY;
        this.oZ = source.oZ;
        this.dX = source.dX;
        this.dY = source.dY;
        this.dZ = source.dZ;
    }
    
    public Rayd(final Vector3dc origin, final Vector3dc direction) {
        this.oX = origin.x();
        this.oY = origin.y();
        this.oZ = origin.z();
        this.dX = direction.x();
        this.dY = direction.y();
        this.dZ = direction.z();
    }
    
    public Rayd(final double oX, final double oY, final double oZ, final double dX, final double dY, final double dZ) {
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
        long temp = Double.doubleToLongBits(this.dX);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.dY);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.dZ);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.oX);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.oY);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.oZ);
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
        final Rayd other = (Rayd)obj;
        return Double.doubleToLongBits(this.dX) == Double.doubleToLongBits(other.dX) && Double.doubleToLongBits(this.dY) == Double.doubleToLongBits(other.dY) && Double.doubleToLongBits(this.dZ) == Double.doubleToLongBits(other.dZ) && Double.doubleToLongBits(this.oX) == Double.doubleToLongBits(other.oX) && Double.doubleToLongBits(this.oY) == Double.doubleToLongBits(other.oY) && Double.doubleToLongBits(this.oZ) == Double.doubleToLongBits(other.oZ);
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "(" + Runtime.format(this.oX, formatter) + " " + Runtime.format(this.oY, formatter) + " " + Runtime.format(this.oZ, formatter) + ") -> (" + Runtime.format(this.dX, formatter) + " " + Runtime.format(this.dY, formatter) + " " + Runtime.format(this.dZ, formatter) + ")";
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeDouble(this.oX);
        out.writeDouble(this.oY);
        out.writeDouble(this.oZ);
        out.writeDouble(this.dX);
        out.writeDouble(this.dY);
        out.writeDouble(this.dZ);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.oX = in.readDouble();
        this.oY = in.readDouble();
        this.oZ = in.readDouble();
        this.dX = in.readDouble();
        this.dY = in.readDouble();
        this.dZ = in.readDouble();
    }
}
