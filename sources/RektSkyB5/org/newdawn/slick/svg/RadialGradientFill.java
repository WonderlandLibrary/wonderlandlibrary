/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.svg;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.TexCoordGenerator;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.svg.Gradient;

public class RadialGradientFill
implements TexCoordGenerator {
    private Vector2f centre;
    private float radius;
    private Gradient gradient;
    private Shape shape;

    public RadialGradientFill(Shape shape, Transform trans, Gradient gradient) {
        this.gradient = gradient;
        this.radius = gradient.getR();
        float x2 = gradient.getX1();
        float y2 = gradient.getY1();
        float[] c2 = new float[]{x2, y2};
        gradient.getTransform().transform(c2, 0, c2, 0, 1);
        trans.transform(c2, 0, c2, 0, 1);
        float[] rt = new float[]{x2, y2 - this.radius};
        gradient.getTransform().transform(rt, 0, rt, 0, 1);
        trans.transform(rt, 0, rt, 0, 1);
        this.centre = new Vector2f(c2[0], c2[1]);
        Vector2f dis = new Vector2f(rt[0], rt[1]);
        this.radius = dis.distance(this.centre);
    }

    public Vector2f getCoordFor(float x2, float y2) {
        float u2 = this.centre.distance(new Vector2f(x2, y2));
        if ((u2 /= this.radius) > 0.99f) {
            u2 = 0.99f;
        }
        return new Vector2f(u2, 0.0f);
    }
}

