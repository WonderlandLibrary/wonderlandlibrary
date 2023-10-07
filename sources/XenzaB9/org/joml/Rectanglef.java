// 
// Decompiled by Procyon v0.6.0
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.text.NumberFormat;
import java.io.Externalizable;

public class Rectanglef implements Externalizable
{
    public float minX;
    public float minY;
    public float maxX;
    public float maxY;
    
    public Rectanglef() {
    }
    
    public Rectanglef(final Rectanglef source) {
        this.minX = source.minX;
        this.minY = source.minY;
        this.maxX = source.maxX;
        this.maxY = source.maxY;
    }
    
    public Rectanglef(final Vector2fc min, final Vector2fc max) {
        this.minX = min.x();
        this.minY = min.y();
        this.maxX = max.x();
        this.maxY = max.y();
    }
    
    public Rectanglef(final float minX, final float minY, final float maxX, final float maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }
    
    public float lengthX() {
        return this.maxX - this.minX;
    }
    
    public float lengthY() {
        return this.maxY - this.minY;
    }
    
    public float area() {
        return this.lengthX() * this.lengthY();
    }
    
    public boolean intersectsRectangle(final Rectangled other) {
        return this.minX < other.maxX && this.maxX >= other.minX && this.maxY >= other.minY && this.minY < other.maxY;
    }
    
    public boolean intersectsRectangle(final Rectanglef other) {
        return this.minX < other.maxX && this.maxX >= other.minX && this.maxY >= other.minY && this.minY < other.maxY;
    }
    
    public boolean intersectsRectangle(final Rectanglei other) {
        return this.minX < other.maxX && this.maxX >= other.minX && this.maxY >= other.minY && this.minY < other.maxY;
    }
    
    private Rectanglef validate() {
        if (!this.isValid()) {
            this.minX = Float.NaN;
            this.minY = Float.NaN;
            this.maxX = Float.NaN;
            this.maxY = Float.NaN;
        }
        return this;
    }
    
    public boolean isValid() {
        return this.minX < this.maxX && this.minY < this.maxY;
    }
    
    public Rectanglef intersection(final Rectanglef other) {
        return this.intersection(other, this);
    }
    
    public Rectanglef intersection(final Rectanglei other) {
        return this.intersection(other, this);
    }
    
    public Rectanglef intersection(final Rectanglef other, final Rectanglef dest) {
        dest.minX = Math.max(this.minX, other.minX);
        dest.minY = Math.max(this.minY, other.minY);
        dest.maxX = Math.min(this.maxX, other.maxX);
        dest.maxY = Math.min(this.maxY, other.maxY);
        return dest.validate();
    }
    
    public Rectanglef intersection(final Rectanglei other, final Rectanglef dest) {
        dest.minX = Math.max(this.minX, (float)other.minX);
        dest.minY = Math.max(this.minY, (float)other.minY);
        dest.maxX = Math.min(this.maxX, (float)other.maxX);
        dest.maxY = Math.min(this.maxY, (float)other.maxY);
        return dest.validate();
    }
    
    public Vector2f lengths(final Vector2f dest) {
        return dest.set(this.lengthX(), this.lengthY());
    }
    
    public boolean containsRectangle(final Rectangled rectangle) {
        return rectangle.minX >= this.minX && rectangle.maxX <= this.maxX && rectangle.minY >= this.minY && rectangle.maxY <= this.maxY;
    }
    
    public boolean containsRectangle(final Rectanglef rectangle) {
        return rectangle.minX >= this.minX && rectangle.maxX <= this.maxX && rectangle.minY >= this.minY && rectangle.maxY <= this.maxY;
    }
    
    public boolean containsRectangle(final Rectanglei rectangle) {
        return rectangle.minX >= this.minX && rectangle.maxX <= this.maxX && rectangle.minY >= this.minY && rectangle.maxY <= this.maxY;
    }
    
    public boolean containsPoint(final Vector2fc point) {
        return this.containsPoint(point.x(), point.y());
    }
    
    public boolean containsPoint(final float x, final float y) {
        return x >= this.minX && y >= this.minY && x < this.maxX && y < this.maxY;
    }
    
    public Rectanglef translate(final Vector2fc xy) {
        return this.translate(xy.x(), xy.y(), this);
    }
    
    public Rectanglef translate(final Vector2fc xy, final Rectanglef dest) {
        return this.translate(xy.x(), xy.y(), dest);
    }
    
    public Rectanglef translate(final float x, final float y) {
        return this.translate(x, y, this);
    }
    
    public Rectanglef translate(final float x, final float y, final Rectanglef dest) {
        dest.minX = this.minX + x;
        dest.minY = this.minY + y;
        dest.maxX = this.maxX + x;
        dest.maxY = this.maxY + y;
        return dest;
    }
    
    public Rectanglef scale(final float sf) {
        return this.scale(sf, sf);
    }
    
    public Rectanglef scale(final float sf, final Rectanglef dest) {
        return this.scale(sf, sf, dest);
    }
    
    public Rectanglef scale(final float sf, final float ax, final float ay) {
        return this.scale(sf, sf, ax, ay);
    }
    
    public Rectanglef scale(final float sf, final float ax, final float ay, final Rectanglef dest) {
        return this.scale(sf, sf, ax, ay, dest);
    }
    
    public Rectanglef scale(final float sf, final Vector2fc anchor) {
        return this.scale(sf, anchor.x(), anchor.y());
    }
    
    public Rectanglef scale(final float sf, final Vector2fc anchor, final Rectanglef dest) {
        return this.scale(sf, anchor.x(), anchor.y(), dest);
    }
    
    public Rectanglef scale(final float sx, final float sy) {
        return this.scale(sx, sy, 0.0f, 0.0f);
    }
    
    public Rectanglef scale(final float sx, final float sy, final Rectanglef dest) {
        return this.scale(sx, sy, 0.0f, 0.0f, dest);
    }
    
    public Rectanglef scale(final float sx, final float sy, final float ax, final float ay) {
        this.minX = (this.minX - ax) * sx + ax;
        this.minY = (this.minY - ay) * sy + ay;
        this.maxX = (this.maxX - ax) * sx + ax;
        this.maxY = (this.maxY - ay) * sy + ay;
        return this;
    }
    
    public Rectanglef scale(final float sx, final float sy, final Vector2fc anchor) {
        return this.scale(sx, sy, anchor.x(), anchor.y());
    }
    
    public Rectanglef scale(final float sx, final float sy, final float ax, final float ay, final Rectanglef dest) {
        dest.minX = (this.minX - ax) * sx + ax;
        dest.minY = (this.minY - ay) * sy + ay;
        dest.maxX = (this.maxX - ax) * sx + ax;
        dest.maxY = (this.maxY - ay) * sy + ay;
        return dest;
    }
    
    public Rectanglef scale(final float sx, final float sy, final Vector2fc anchor, final Rectanglef dest) {
        return this.scale(sx, sy, anchor.x(), anchor.y(), dest);
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + Float.floatToIntBits(this.maxX);
        result = 31 * result + Float.floatToIntBits(this.maxY);
        result = 31 * result + Float.floatToIntBits(this.minX);
        result = 31 * result + Float.floatToIntBits(this.minY);
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
        final Rectanglef other = (Rectanglef)obj;
        return Float.floatToIntBits(this.maxX) == Float.floatToIntBits(other.maxX) && Float.floatToIntBits(this.maxY) == Float.floatToIntBits(other.maxY) && Float.floatToIntBits(this.minX) == Float.floatToIntBits(other.minX) && Float.floatToIntBits(this.minY) == Float.floatToIntBits(other.minY);
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "(" + Runtime.format(this.minX, formatter) + " " + Runtime.format(this.minY, formatter) + ") < (" + Runtime.format(this.maxX, formatter) + " " + Runtime.format(this.maxY, formatter) + ")";
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeFloat(this.minX);
        out.writeFloat(this.minY);
        out.writeFloat(this.maxX);
        out.writeFloat(this.maxY);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.minX = in.readFloat();
        this.minY = in.readFloat();
        this.maxX = in.readFloat();
        this.maxY = in.readFloat();
    }
}
