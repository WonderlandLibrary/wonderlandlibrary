/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.util;

import java.io.Serializable;
import org.lwjgl.util.ReadableDimension;
import org.lwjgl.util.ReadablePoint;
import org.lwjgl.util.ReadableRectangle;
import org.lwjgl.util.WritableDimension;
import org.lwjgl.util.WritablePoint;
import org.lwjgl.util.WritableRectangle;

public final class Rectangle
implements ReadableRectangle,
WritableRectangle,
Serializable {
    static final long serialVersionUID = 1L;
    private int x;
    private int y;
    private int width;
    private int height;

    public Rectangle() {
    }

    public Rectangle(int x2, int y2, int w2, int h2) {
        this.x = x2;
        this.y = y2;
        this.width = w2;
        this.height = h2;
    }

    public Rectangle(ReadablePoint p2, ReadableDimension d2) {
        this.x = p2.getX();
        this.y = p2.getY();
        this.width = d2.getWidth();
        this.height = d2.getHeight();
    }

    public Rectangle(ReadableRectangle r2) {
        this.x = r2.getX();
        this.y = r2.getY();
        this.width = r2.getWidth();
        this.height = r2.getHeight();
    }

    public void setLocation(int x2, int y2) {
        this.x = x2;
        this.y = y2;
    }

    public void setLocation(ReadablePoint p2) {
        this.x = p2.getX();
        this.y = p2.getY();
    }

    public void setSize(int w2, int h2) {
        this.width = w2;
        this.height = h2;
    }

    public void setSize(ReadableDimension d2) {
        this.width = d2.getWidth();
        this.height = d2.getHeight();
    }

    public void setBounds(int x2, int y2, int w2, int h2) {
        this.x = x2;
        this.y = y2;
        this.width = w2;
        this.height = h2;
    }

    public void setBounds(ReadablePoint p2, ReadableDimension d2) {
        this.x = p2.getX();
        this.y = p2.getY();
        this.width = d2.getWidth();
        this.height = d2.getHeight();
    }

    public void setBounds(ReadableRectangle r2) {
        this.x = r2.getX();
        this.y = r2.getY();
        this.width = r2.getWidth();
        this.height = r2.getHeight();
    }

    public void getBounds(WritableRectangle dest) {
        dest.setBounds(this.x, this.y, this.width, this.height);
    }

    public void getLocation(WritablePoint dest) {
        dest.setLocation(this.x, this.y);
    }

    public void getSize(WritableDimension dest) {
        dest.setSize(this.width, this.height);
    }

    public void translate(int x2, int y2) {
        this.x += x2;
        this.y += y2;
    }

    public void translate(ReadablePoint point) {
        this.x += point.getX();
        this.y += point.getY();
    }

    public void untranslate(ReadablePoint point) {
        this.x -= point.getX();
        this.y -= point.getY();
    }

    public boolean contains(ReadablePoint p2) {
        return this.contains(p2.getX(), p2.getY());
    }

    public boolean contains(int X2, int Y) {
        int w2 = this.width;
        int h2 = this.height;
        if ((w2 | h2) < 0) {
            return false;
        }
        int x2 = this.x;
        int y2 = this.y;
        if (X2 < x2 || Y < y2) {
            return false;
        }
        h2 += y2;
        return !((w2 += x2) >= x2 && w2 <= X2 || h2 >= y2 && h2 <= Y);
    }

    public boolean contains(ReadableRectangle r2) {
        return this.contains(r2.getX(), r2.getY(), r2.getWidth(), r2.getHeight());
    }

    public boolean contains(int X2, int Y, int W, int H) {
        int w2 = this.width;
        int h2 = this.height;
        if ((w2 | h2 | W | H) < 0) {
            return false;
        }
        int x2 = this.x;
        int y2 = this.y;
        if (X2 < x2 || Y < y2) {
            return false;
        }
        w2 += x2;
        if ((W += X2) <= X2 ? w2 >= x2 || W > w2 : w2 >= x2 && W > w2) {
            return false;
        }
        h2 += y2;
        return !((H += Y) <= Y ? h2 >= y2 || H > h2 : h2 >= y2 && H > h2);
    }

    public boolean intersects(ReadableRectangle r2) {
        int tw = this.width;
        int th = this.height;
        int rw = r2.getWidth();
        int rh = r2.getHeight();
        if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
            return false;
        }
        int tx = this.x;
        int ty = this.y;
        int rx = r2.getX();
        int ry = r2.getY();
        rh += ry;
        tw += tx;
        th += ty;
        return !((rw += rx) >= rx && rw <= tx || rh >= ry && rh <= ty || tw >= tx && tw <= rx || th >= ty && th <= ry);
    }

    public Rectangle intersection(ReadableRectangle r2, Rectangle dest) {
        int tx1 = this.x;
        int ty1 = this.y;
        int rx1 = r2.getX();
        int ry1 = r2.getY();
        long tx2 = tx1;
        tx2 += (long)this.width;
        long ty2 = ty1;
        ty2 += (long)this.height;
        long rx2 = rx1;
        rx2 += (long)r2.getWidth();
        long ry2 = ry1;
        ry2 += (long)r2.getHeight();
        if (tx1 < rx1) {
            tx1 = rx1;
        }
        if (ty1 < ry1) {
            ty1 = ry1;
        }
        if (tx2 > rx2) {
            tx2 = rx2;
        }
        if (ty2 > ry2) {
            ty2 = ry2;
        }
        ty2 -= (long)ty1;
        if ((tx2 -= (long)tx1) < Integer.MIN_VALUE) {
            tx2 = Integer.MIN_VALUE;
        }
        if (ty2 < Integer.MIN_VALUE) {
            ty2 = Integer.MIN_VALUE;
        }
        if (dest == null) {
            dest = new Rectangle(tx1, ty1, (int)tx2, (int)ty2);
        } else {
            dest.setBounds(tx1, ty1, (int)tx2, (int)ty2);
        }
        return dest;
    }

    public WritableRectangle union(ReadableRectangle r2, WritableRectangle dest) {
        int x1 = Math.min(this.x, r2.getX());
        int x2 = Math.max(this.x + this.width, r2.getX() + r2.getWidth());
        int y1 = Math.min(this.y, r2.getY());
        int y2 = Math.max(this.y + this.height, r2.getY() + r2.getHeight());
        dest.setBounds(x1, y1, x2 - x1, y2 - y1);
        return dest;
    }

    public void add(int newx, int newy) {
        int x1 = Math.min(this.x, newx);
        int x2 = Math.max(this.x + this.width, newx);
        int y1 = Math.min(this.y, newy);
        int y2 = Math.max(this.y + this.height, newy);
        this.x = x1;
        this.y = y1;
        this.width = x2 - x1;
        this.height = y2 - y1;
    }

    public void add(ReadablePoint pt) {
        this.add(pt.getX(), pt.getY());
    }

    public void add(ReadableRectangle r2) {
        int x1 = Math.min(this.x, r2.getX());
        int x2 = Math.max(this.x + this.width, r2.getX() + r2.getWidth());
        int y1 = Math.min(this.y, r2.getY());
        int y2 = Math.max(this.y + this.height, r2.getY() + r2.getHeight());
        this.x = x1;
        this.y = y1;
        this.width = x2 - x1;
        this.height = y2 - y1;
    }

    public void grow(int h2, int v2) {
        this.x -= h2;
        this.y -= v2;
        this.width += h2 * 2;
        this.height += v2 * 2;
    }

    public boolean isEmpty() {
        return this.width <= 0 || this.height <= 0;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Rectangle) {
            Rectangle r2 = (Rectangle)obj;
            return this.x == r2.x && this.y == r2.y && this.width == r2.width && this.height == r2.height;
        }
        return super.equals(obj);
    }

    public String toString() {
        return this.getClass().getName() + "[x=" + this.x + ",y=" + this.y + ",width=" + this.width + ",height=" + this.height + "]";
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x2) {
        this.x = x2;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y2) {
        this.y = y2;
    }
}

