// 
// Decompiled by Procyon v0.6.0
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.text.NumberFormat;
import java.io.Externalizable;

public class Rectangled implements Externalizable
{
    public double minX;
    public double minY;
    public double maxX;
    public double maxY;
    
    public Rectangled() {
    }
    
    public Rectangled(final Rectangled source) {
        this.minX = source.minX;
        this.minY = source.minY;
        this.maxX = source.maxX;
        this.maxY = source.maxY;
    }
    
    public Rectangled(final Vector2dc min, final Vector2dc max) {
        this.minX = min.x();
        this.minY = min.y();
        this.maxX = max.x();
        this.maxY = max.y();
    }
    
    public Rectangled(final double minX, final double minY, final double maxX, final double maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }
    
    public double lengthX() {
        return this.maxX - this.minX;
    }
    
    public double lengthY() {
        return this.maxY - this.minY;
    }
    
    public double area() {
        return this.lengthX() * this.lengthY();
    }
    
    private Rectangled validate() {
        if (!this.isValid()) {
            this.minX = Double.NaN;
            this.minY = Double.NaN;
            this.maxX = Double.NaN;
            this.maxY = Double.NaN;
        }
        return this;
    }
    
    public boolean isValid() {
        return this.minX < this.maxX && this.minY < this.maxY;
    }
    
    public Rectangled intersection(final Rectangled other) {
        return this.intersection(other, this);
    }
    
    public Rectangled intersection(final Rectangled other, final Rectangled dest) {
        dest.minX = Math.max(this.minX, other.minX);
        dest.minY = Math.max(this.minY, other.minY);
        dest.maxX = Math.min(this.maxX, other.maxX);
        dest.maxY = Math.min(this.maxY, other.maxY);
        return dest.validate();
    }
    
    public Rectangled intersection(final Rectanglef other, final Rectangled dest) {
        dest.minX = Math.max(this.minX, other.minX);
        dest.minY = Math.max(this.minY, other.minY);
        dest.maxX = Math.min(this.maxX, other.maxX);
        dest.maxY = Math.min(this.maxY, other.maxY);
        return dest.validate();
    }
    
    public Rectangled intersection(final Rectanglei other, final Rectangled dest) {
        dest.minX = Math.max(this.minX, other.minX);
        dest.minY = Math.max(this.minY, other.minY);
        dest.maxX = Math.min(this.maxX, other.maxX);
        dest.maxY = Math.min(this.maxY, other.maxY);
        return dest.validate();
    }
    
    public Vector2d lengths(final Vector2d dest) {
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
    
    public boolean intersectsRectangle(final Rectangled other) {
        return this.minX < other.maxX && this.maxX >= other.minX && this.maxY >= other.minY && this.minY < other.maxY;
    }
    
    public boolean intersectsRectangle(final Rectanglef other) {
        return this.minX < other.maxX && this.maxX >= other.minX && this.maxY >= other.minY && this.minY < other.maxY;
    }
    
    public boolean intersectsRectangle(final Rectanglei other) {
        return this.minX < other.maxX && this.maxX >= other.minX && this.maxY >= other.minY && this.minY < other.maxY;
    }
    
    public boolean containsPoint(final Vector2dc point) {
        return this.containsPoint(point.x(), point.y());
    }
    
    public boolean containsPoint(final double x, final double y) {
        return x >= this.minX && y >= this.minY && x < this.maxX && y < this.maxY;
    }
    
    public Rectangled translate(final Vector2dc xy) {
        return this.translate(xy.x(), xy.y(), this);
    }
    
    public Rectangled translate(final Vector2dc xy, final Rectangled dest) {
        return this.translate(xy.x(), xy.y(), dest);
    }
    
    public Rectangled translate(final Vector2fc xy) {
        return this.translate(xy.x(), xy.y(), this);
    }
    
    public Rectangled translate(final Vector2fc xy, final Rectangled dest) {
        return this.translate(xy.x(), xy.y(), dest);
    }
    
    public Rectangled translate(final double x, final double y) {
        return this.translate(x, y, this);
    }
    
    public Rectangled translate(final double x, final double y, final Rectangled dest) {
        dest.minX = this.minX + x;
        dest.minY = this.minY + y;
        dest.maxX = this.maxX + x;
        dest.maxY = this.maxY + y;
        return dest;
    }
    
    public Rectangled scale(final double sf) {
        return this.scale(sf, sf);
    }
    
    public Rectangled scale(final double sf, final Rectangled dest) {
        return this.scale(sf, sf, dest);
    }
    
    public Rectangled scale(final double sf, final double ax, final double ay) {
        return this.scale(sf, sf, ax, ay);
    }
    
    public Rectangled scale(final double sf, final double ax, final double ay, final Rectangled dest) {
        return this.scale(sf, sf, ax, ay, dest);
    }
    
    public Rectangled scale(final double sf, final Vector2dc anchor) {
        return this.scale(sf, anchor.x(), anchor.y());
    }
    
    public Rectangled scale(final double sf, final Vector2dc anchor, final Rectangled dest) {
        return this.scale(sf, anchor.x(), anchor.y(), dest);
    }
    
    public Rectangled scale(final double sx, final double sy) {
        return this.scale(sx, sy, 0.0, 0.0);
    }
    
    public Rectangled scale(final double sx, final double sy, final Rectangled dest) {
        return this.scale(sx, sy, 0.0, 0.0, dest);
    }
    
    public Rectangled scale(final double sx, final double sy, final double ax, final double ay) {
        this.minX = (this.minX - ax) * sx + ax;
        this.minY = (this.minY - ay) * sy + ay;
        this.maxX = (this.maxX - ax) * sx + ax;
        this.maxY = (this.maxY - ay) * sy + ay;
        return this;
    }
    
    public Rectangled scale(final double sx, final double sy, final Vector2dc anchor) {
        return this.scale(sx, sy, anchor.x(), anchor.y());
    }
    
    public Rectangled scale(final double sx, final double sy, final double ax, final double ay, final Rectangled dest) {
        dest.minX = (this.minX - ax) * sx + ax;
        dest.minY = (this.minY - ay) * sy + ay;
        dest.maxX = (this.maxX - ax) * sx + ax;
        dest.maxY = (this.maxY - ay) * sy + ay;
        return dest;
    }
    
    public Rectangled scale(final double sx, final double sy, final Vector2dc anchor, final Rectangled dest) {
        return this.scale(sx, sy, anchor.x(), anchor.y(), dest);
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp = Double.doubleToLongBits(this.maxX);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.maxY);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.minX);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.minY);
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
        final Rectangled other = (Rectangled)obj;
        return Double.doubleToLongBits(this.maxX) == Double.doubleToLongBits(other.maxX) && Double.doubleToLongBits(this.maxY) == Double.doubleToLongBits(other.maxY) && Double.doubleToLongBits(this.minX) == Double.doubleToLongBits(other.minX) && Double.doubleToLongBits(this.minY) == Double.doubleToLongBits(other.minY);
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "(" + Runtime.format(this.minX, formatter) + " " + Runtime.format(this.minY, formatter) + ") < (" + Runtime.format(this.maxX, formatter) + " " + Runtime.format(this.maxY, formatter) + ")";
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeDouble(this.minX);
        out.writeDouble(this.minY);
        out.writeDouble(this.maxX);
        out.writeDouble(this.maxY);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.minX = in.readDouble();
        this.minY = in.readDouble();
        this.maxX = in.readDouble();
        this.maxY = in.readDouble();
    }
}
