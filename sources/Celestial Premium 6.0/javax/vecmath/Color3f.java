/*
 * Decompiled with CFR 0.150.
 */
package javax.vecmath;

import java.awt.Color;
import java.io.Serializable;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;

public class Color3f
extends Tuple3f
implements Serializable {
    static final long serialVersionUID = -1861792981817493659L;

    public Color3f(float x, float y, float z) {
        super(x, y, z);
    }

    public Color3f(float[] v) {
        super(v);
    }

    public Color3f(Color3f v1) {
        super(v1);
    }

    public Color3f(Tuple3f t1) {
        super(t1);
    }

    public Color3f(Tuple3d t1) {
        super(t1);
    }

    public Color3f(Color color) {
        super((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f);
    }

    public Color3f() {
    }

    public final void set(Color color) {
        this.x = (float)color.getRed() / 255.0f;
        this.y = (float)color.getGreen() / 255.0f;
        this.z = (float)color.getBlue() / 255.0f;
    }

    public final Color get() {
        int r = Math.round(this.x * 255.0f);
        int g = Math.round(this.y * 255.0f);
        int b = Math.round(this.z * 255.0f);
        return new Color(r, g, b);
    }
}

