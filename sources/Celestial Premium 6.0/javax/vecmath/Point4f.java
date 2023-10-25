/*
 * Decompiled with CFR 0.150.
 */
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.Point4d;
import javax.vecmath.Tuple3f;
import javax.vecmath.Tuple4d;
import javax.vecmath.Tuple4f;

public class Point4f
extends Tuple4f
implements Serializable {
    static final long serialVersionUID = 4643134103185764459L;

    public Point4f(float x, float y, float z, float w) {
        super(x, y, z, w);
    }

    public Point4f(float[] p) {
        super(p);
    }

    public Point4f(Point4f p1) {
        super(p1);
    }

    public Point4f(Point4d p1) {
        super(p1);
    }

    public Point4f(Tuple4f t1) {
        super(t1);
    }

    public Point4f(Tuple4d t1) {
        super(t1);
    }

    public Point4f(Tuple3f t1) {
        super(t1.x, t1.y, t1.z, 1.0f);
    }

    public Point4f() {
    }

    public final void set(Tuple3f t1) {
        this.x = t1.x;
        this.y = t1.y;
        this.z = t1.z;
        this.w = 1.0f;
    }

    public final float distanceSquared(Point4f p1) {
        float dx = this.x - p1.x;
        float dy = this.y - p1.y;
        float dz = this.z - p1.z;
        float dw = this.w - p1.w;
        return dx * dx + dy * dy + dz * dz + dw * dw;
    }

    public final float distance(Point4f p1) {
        float dx = this.x - p1.x;
        float dy = this.y - p1.y;
        float dz = this.z - p1.z;
        float dw = this.w - p1.w;
        return (float)Math.sqrt(dx * dx + dy * dy + dz * dz + dw * dw);
    }

    public final float distanceL1(Point4f p1) {
        return Math.abs(this.x - p1.x) + Math.abs(this.y - p1.y) + Math.abs(this.z - p1.z) + Math.abs(this.w - p1.w);
    }

    public final float distanceLinf(Point4f p1) {
        float t1 = Math.max(Math.abs(this.x - p1.x), Math.abs(this.y - p1.y));
        float t2 = Math.max(Math.abs(this.z - p1.z), Math.abs(this.w - p1.w));
        return Math.max(t1, t2);
    }

    public final void project(Point4f p1) {
        float oneOw = 1.0f / p1.w;
        this.x = p1.x * oneOw;
        this.y = p1.y * oneOw;
        this.z = p1.z * oneOw;
        this.w = 1.0f;
    }
}

