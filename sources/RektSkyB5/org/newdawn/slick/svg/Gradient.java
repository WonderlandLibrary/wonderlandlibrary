/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.svg;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.svg.Diagram;

public class Gradient {
    private String name;
    private ArrayList steps = new ArrayList();
    private float x1;
    private float x2;
    private float y1;
    private float y2;
    private float r;
    private Image image;
    private boolean radial;
    private Transform transform;
    private String ref;

    public Gradient(String name, boolean radial) {
        this.name = name;
        this.radial = radial;
    }

    public boolean isRadial() {
        return this.radial;
    }

    public void setTransform(Transform trans) {
        this.transform = trans;
    }

    public Transform getTransform() {
        return this.transform;
    }

    public void reference(String ref) {
        this.ref = ref;
    }

    public void resolve(Diagram diagram) {
        if (this.ref == null) {
            return;
        }
        Gradient other = diagram.getGradient(this.ref);
        for (int i2 = 0; i2 < other.steps.size(); ++i2) {
            this.steps.add(other.steps.get(i2));
        }
    }

    public void genImage() {
        if (this.image == null) {
            ImageBuffer buffer = new ImageBuffer(128, 16);
            for (int i2 = 0; i2 < 128; ++i2) {
                Color col = this.getColorAt((float)i2 / 128.0f);
                for (int j2 = 0; j2 < 16; ++j2) {
                    buffer.setRGBA(i2, j2, col.getRedByte(), col.getGreenByte(), col.getBlueByte(), col.getAlphaByte());
                }
            }
            this.image = buffer.getImage();
        }
    }

    public Image getImage() {
        this.genImage();
        return this.image;
    }

    public void setR(float r2) {
        this.r = r2;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    public void setX2(float x2) {
        this.x2 = x2;
    }

    public void setY1(float y1) {
        this.y1 = y1;
    }

    public void setY2(float y2) {
        this.y2 = y2;
    }

    public float getR() {
        return this.r;
    }

    public float getX1() {
        return this.x1;
    }

    public float getX2() {
        return this.x2;
    }

    public float getY1() {
        return this.y1;
    }

    public float getY2() {
        return this.y2;
    }

    public void addStep(float location, Color c2) {
        this.steps.add(new Step(location, c2));
    }

    public Color getColorAt(float p2) {
        if (p2 <= 0.0f) {
            return ((Step)this.steps.get((int)0)).col;
        }
        if (p2 > 1.0f) {
            return ((Step)this.steps.get((int)(this.steps.size() - 1))).col;
        }
        for (int i2 = 1; i2 < this.steps.size(); ++i2) {
            Step prev = (Step)this.steps.get(i2 - 1);
            Step current = (Step)this.steps.get(i2);
            if (!(p2 <= current.location)) continue;
            float dis = current.location - prev.location;
            float v2 = (p2 -= prev.location) / dis;
            Color c2 = new Color(1, 1, 1, 1);
            c2.a = prev.col.a * (1.0f - v2) + current.col.a * v2;
            c2.r = prev.col.r * (1.0f - v2) + current.col.r * v2;
            c2.g = prev.col.g * (1.0f - v2) + current.col.g * v2;
            c2.b = prev.col.b * (1.0f - v2) + current.col.b * v2;
            return c2;
        }
        return Color.black;
    }

    private class Step {
        float location;
        Color col;

        public Step(float location, Color c2) {
            this.location = location;
            this.col = c2;
        }
    }
}

