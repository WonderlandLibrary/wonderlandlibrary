/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.fills;

import org.newdawn.slick.Color;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class GradientFill
implements ShapeFill {
    private Vector2f none = new Vector2f(0.0f, 0.0f);
    private Vector2f start;
    private Vector2f end;
    private Color startCol;
    private Color endCol;
    private boolean local = false;

    public GradientFill(float sx, float sy, Color startCol, float ex, float ey, Color endCol) {
        this(sx, sy, startCol, ex, ey, endCol, false);
    }

    public GradientFill(float sx, float sy, Color startCol, float ex, float ey, Color endCol, boolean local) {
        this(new Vector2f(sx, sy), startCol, new Vector2f(ex, ey), endCol, local);
    }

    public GradientFill(Vector2f start, Color startCol, Vector2f end, Color endCol, boolean local) {
        this.start = new Vector2f(start);
        this.end = new Vector2f(end);
        this.startCol = new Color(startCol);
        this.endCol = new Color(endCol);
        this.local = local;
    }

    public GradientFill getInvertedCopy() {
        return new GradientFill(this.start, this.endCol, this.end, this.startCol, this.local);
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public Vector2f getStart() {
        return this.start;
    }

    public Vector2f getEnd() {
        return this.end;
    }

    public Color getStartColor() {
        return this.startCol;
    }

    public Color getEndColor() {
        return this.endCol;
    }

    public void setStart(float x2, float y2) {
        this.setStart(new Vector2f(x2, y2));
    }

    public void setStart(Vector2f start) {
        this.start = new Vector2f(start);
    }

    public void setEnd(float x2, float y2) {
        this.setEnd(new Vector2f(x2, y2));
    }

    public void setEnd(Vector2f end) {
        this.end = new Vector2f(end);
    }

    public void setStartColor(Color color) {
        this.startCol = new Color(color);
    }

    public void setEndColor(Color color) {
        this.endCol = new Color(color);
    }

    public Color colorAt(Shape shape, float x2, float y2) {
        if (this.local) {
            return this.colorAt(x2 - shape.getCenterX(), y2 - shape.getCenterY());
        }
        return this.colorAt(x2, y2);
    }

    public Color colorAt(float x2, float y2) {
        float dy1;
        float dx2;
        float dx1 = this.end.getX() - this.start.getX();
        float dy2 = dx1;
        float denom = dy2 * dx1 - (dx2 = -(dy1 = this.end.getY() - this.start.getY())) * dy1;
        if (denom == 0.0f) {
            return Color.black;
        }
        float ua = dx2 * (this.start.getY() - y2) - dy2 * (this.start.getX() - x2);
        ua /= denom;
        float ub = dx1 * (this.start.getY() - y2) - dy1 * (this.start.getX() - x2);
        ub /= denom;
        float u2 = ua;
        if (u2 < 0.0f) {
            u2 = 0.0f;
        }
        if (u2 > 1.0f) {
            u2 = 1.0f;
        }
        float v2 = 1.0f - u2;
        Color col = new Color(1, 1, 1, 1);
        col.r = u2 * this.endCol.r + v2 * this.startCol.r;
        col.b = u2 * this.endCol.b + v2 * this.startCol.b;
        col.g = u2 * this.endCol.g + v2 * this.startCol.g;
        col.a = u2 * this.endCol.a + v2 * this.startCol.a;
        return col;
    }

    public Vector2f getOffsetAt(Shape shape, float x2, float y2) {
        return this.none;
    }
}

