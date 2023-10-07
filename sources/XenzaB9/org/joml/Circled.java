// 
// Decompiled by Procyon v0.6.0
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.text.NumberFormat;
import java.io.Externalizable;

public class Circled implements Externalizable
{
    public double x;
    public double y;
    public double r;
    
    public Circled() {
    }
    
    public Circled(final Circled source) {
        this.x = source.x;
        this.y = source.y;
        this.r = source.r;
    }
    
    public Circled(final double x, final double y, final double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }
    
    public Circled translate(final Vector2dc xy) {
        return this.translate(xy.x(), xy.y(), this);
    }
    
    public Circled translate(final Vector2dc xy, final Circled dest) {
        return this.translate(xy.x(), xy.y(), dest);
    }
    
    public Circled translate(final Vector2fc xy) {
        return this.translate(xy.x(), xy.y(), this);
    }
    
    public Circled translate(final Vector2fc xy, final Circled dest) {
        return this.translate(xy.x(), xy.y(), dest);
    }
    
    public Circled translate(final double x, final double y) {
        return this.translate(x, y, this);
    }
    
    public Circled translate(final double x, final double y, final Circled dest) {
        dest.x = this.x + x;
        dest.y = this.y + y;
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
        final Circled other = (Circled)obj;
        return Double.doubleToLongBits(this.r) == Double.doubleToLongBits(other.r) && Double.doubleToLongBits(this.x) == Double.doubleToLongBits(other.x) && Double.doubleToLongBits(this.y) == Double.doubleToLongBits(other.y);
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "(" + Runtime.format(this.x, formatter) + " " + Runtime.format(this.y, formatter) + " " + Runtime.format(this.r, formatter) + ")";
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.r);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.r = in.readDouble();
    }
}
