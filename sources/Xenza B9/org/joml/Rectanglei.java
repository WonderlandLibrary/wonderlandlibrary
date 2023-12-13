// 
// Decompiled by Procyon v0.6.0
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.text.NumberFormat;
import java.io.Externalizable;

public class Rectanglei implements Externalizable
{
    public int minX;
    public int minY;
    public int maxX;
    public int maxY;
    
    public Rectanglei() {
    }
    
    public Rectanglei(final Rectanglei source) {
        this.minX = source.minX;
        this.minY = source.minY;
        this.maxX = source.maxX;
        this.maxY = source.maxY;
    }
    
    public Rectanglei(final Vector2ic min, final Vector2ic max) {
        this.minX = min.x();
        this.minY = min.y();
        this.maxX = max.x();
        this.maxY = max.y();
    }
    
    public Rectanglei(final int minX, final int minY, final int maxX, final int maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }
    
    public int lengthX() {
        return this.maxX - this.minX;
    }
    
    public int lengthY() {
        return this.maxY - this.minY;
    }
    
    public int area() {
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
    
    private Rectanglei validate() {
        if (!this.isValid()) {
            this.minX = Integer.MAX_VALUE;
            this.minY = Integer.MAX_VALUE;
            this.maxX = Integer.MIN_VALUE;
            this.maxY = Integer.MIN_VALUE;
        }
        return this;
    }
    
    public boolean isValid() {
        return this.minX < this.maxX && this.minY < this.maxY;
    }
    
    public Rectanglei intersection(final Rectanglei other) {
        return this.intersection(other, this);
    }
    
    public Rectanglei intersection(final Rectanglei other, final Rectanglei dest) {
        dest.minX = Math.max(this.minX, other.minX);
        dest.minY = Math.max(this.minY, other.minY);
        dest.maxX = Math.min(this.maxX, other.maxX);
        dest.maxY = Math.min(this.maxY, other.maxY);
        return dest.validate();
    }
    
    public Vector2i lengths(final Vector2i dest) {
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
    
    public boolean containsPoint(final Vector2ic point) {
        return this.containsPoint(point.x(), point.y());
    }
    
    public boolean containsPoint(final int x, final int y) {
        return x >= this.minX && y >= this.minY && x < this.maxX && y < this.maxY;
    }
    
    public Rectanglei translate(final Vector2ic xy) {
        return this.translate(xy.x(), xy.y(), this);
    }
    
    public Rectanglei translate(final Vector2ic xy, final Rectanglei dest) {
        return this.translate(xy.x(), xy.y(), dest);
    }
    
    public Rectanglei translate(final int x, final int y) {
        return this.translate(x, y, this);
    }
    
    public Rectanglei translate(final int x, final int y, final Rectanglei dest) {
        dest.minX = this.minX + x;
        dest.minY = this.minY + y;
        dest.maxX = this.maxX + x;
        dest.maxY = this.maxY + y;
        return dest;
    }
    
    public Rectanglei scale(final int sf) {
        return this.scale(sf, sf);
    }
    
    public Rectanglei scale(final int sf, final Rectanglei dest) {
        return this.scale(sf, sf, dest);
    }
    
    public Rectanglei scale(final int sf, final int ax, final int ay) {
        return this.scale(sf, sf, ax, ay);
    }
    
    public Rectanglei scale(final int sf, final int ax, final int ay, final Rectanglei dest) {
        return this.scale(sf, sf, ax, ay, dest);
    }
    
    public Rectanglei scale(final int sf, final Vector2ic anchor) {
        return this.scale(sf, anchor.x(), anchor.y());
    }
    
    public Rectanglei scale(final int sf, final Vector2ic anchor, final Rectanglei dest) {
        return this.scale(sf, anchor.x(), anchor.y(), dest);
    }
    
    public Rectanglei scale(final int sx, final int sy) {
        return this.scale(sx, sy, 0, 0);
    }
    
    public Rectanglei scale(final int sx, final int sy, final Rectanglei dest) {
        return this.scale(sx, sy, 0, 0, dest);
    }
    
    public Rectanglei scale(final int sx, final int sy, final int ax, final int ay) {
        this.minX = (this.minX - ax) * sx + ax;
        this.minY = (this.minY - ay) * sy + ay;
        this.maxX = (this.maxX - ax) * sx + ax;
        this.maxY = (this.maxY - ay) * sy + ay;
        return this;
    }
    
    public Rectanglei scale(final int sx, final int sy, final Vector2ic anchor) {
        return this.scale(sx, sy, anchor.x(), anchor.y());
    }
    
    public Rectanglei scale(final int sx, final int sy, final int ax, final int ay, final Rectanglei dest) {
        dest.minX = (this.minX - ax) * sx + ax;
        dest.minY = (this.minY - ay) * sy + ay;
        dest.maxX = (this.maxX - ax) * sx + ax;
        dest.maxY = (this.maxY - ay) * sy + ay;
        return dest;
    }
    
    public Rectanglei scale(final int sx, final int sy, final Vector2ic anchor, final Rectanglei dest) {
        return this.scale(sx, sy, anchor.x(), anchor.y(), dest);
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + this.maxX;
        result = 31 * result + this.maxY;
        result = 31 * result + this.minX;
        result = 31 * result + this.minY;
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
        final Rectanglei other = (Rectanglei)obj;
        return this.maxX == other.maxX && this.maxY == other.maxY && this.minX == other.minX && this.minY == other.minY;
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "(" + formatter.format(this.minX) + " " + formatter.format(this.minY) + ") < (" + formatter.format(this.maxX) + " " + formatter.format(this.maxY) + ")";
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeInt(this.minX);
        out.writeInt(this.minY);
        out.writeInt(this.maxX);
        out.writeInt(this.maxY);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.minX = in.readInt();
        this.minY = in.readInt();
        this.maxX = in.readInt();
        this.maxY = in.readInt();
    }
}
