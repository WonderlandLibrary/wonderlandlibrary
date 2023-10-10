/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.util;

import java.io.Serializable;
import org.lwjgl.util.ReadablePoint;
import org.lwjgl.util.WritablePoint;

public final class Point
implements ReadablePoint,
WritablePoint,
Serializable {
    static final long serialVersionUID = 1L;
    private int x;
    private int y;

    public Point() {
    }

    public Point(int x2, int y2) {
        this.setLocation(x2, y2);
    }

    public Point(ReadablePoint p2) {
        this.setLocation(p2);
    }

    public void setLocation(int x2, int y2) {
        this.x = x2;
        this.y = y2;
    }

    public void setLocation(ReadablePoint p2) {
        this.x = p2.getX();
        this.y = p2.getY();
    }

    public void setX(int x2) {
        this.x = x2;
    }

    public void setY(int y2) {
        this.y = y2;
    }

    public void translate(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public void translate(ReadablePoint p2) {
        this.x += p2.getX();
        this.y += p2.getY();
    }

    public void untranslate(ReadablePoint p2) {
        this.x -= p2.getX();
        this.y -= p2.getY();
    }

    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point pt = (Point)obj;
            return this.x == pt.x && this.y == pt.y;
        }
        return super.equals(obj);
    }

    public String toString() {
        return this.getClass().getName() + "[x=" + this.x + ",y=" + this.y + "]";
    }

    public int hashCode() {
        int sum = this.x + this.y;
        return sum * (sum + 1) / 2 + this.x;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void getLocation(WritablePoint dest) {
        dest.setLocation(this.x, this.y);
    }
}

