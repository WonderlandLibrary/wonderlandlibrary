// 
// Decompiled by Procyon v0.6.0
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.text.NumberFormat;
import java.io.Externalizable;

public class Circlef implements Externalizable
{
    public float x;
    public float y;
    public float r;
    
    public Circlef() {
    }
    
    public Circlef(final Circlef source) {
        this.x = source.x;
        this.y = source.y;
        this.r = source.r;
    }
    
    public Circlef(final float x, final float y, final float r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }
    
    public Circlef translate(final Vector2fc xy) {
        return this.translate(xy.x(), xy.y(), this);
    }
    
    public Circlef translate(final Vector2fc xy, final Circlef dest) {
        return this.translate(xy.x(), xy.y(), dest);
    }
    
    public Circlef translate(final float x, final float y) {
        return this.translate(x, y, this);
    }
    
    public Circlef translate(final float x, final float y, final Circlef dest) {
        dest.x = this.x + x;
        dest.y = this.y + y;
        return dest;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + Float.floatToIntBits(this.r);
        result = 31 * result + Float.floatToIntBits(this.x);
        result = 31 * result + Float.floatToIntBits(this.y);
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
        final Circlef other = (Circlef)obj;
        return Float.floatToIntBits(this.r) == Float.floatToIntBits(other.r) && Float.floatToIntBits(this.x) == Float.floatToIntBits(other.x) && Float.floatToIntBits(this.y) == Float.floatToIntBits(other.y);
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "(" + Runtime.format(this.x, formatter) + " " + Runtime.format(this.y, formatter) + " " + Runtime.format(this.r, formatter) + ")";
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeFloat(this.x);
        out.writeFloat(this.y);
        out.writeFloat(this.r);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.x = in.readFloat();
        this.y = in.readFloat();
        this.r = in.readFloat();
    }
}
