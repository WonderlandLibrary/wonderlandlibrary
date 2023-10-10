/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.svg;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.TexCoordGenerator;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.svg.Gradient;

public class LinearGradientFill
implements TexCoordGenerator {
    private Vector2f start;
    private Vector2f end;
    private Gradient gradient;
    private Line line;
    private Shape shape;

    public LinearGradientFill(Shape shape, Transform trans, Gradient gradient) {
        this.gradient = gradient;
        float x2 = gradient.getX1();
        float y2 = gradient.getY1();
        float mx = gradient.getX2();
        float my = gradient.getY2();
        float h2 = my - y2;
        float w2 = mx - x2;
        float[] s2 = new float[]{x2, y2 + h2 / 2.0f};
        gradient.getTransform().transform(s2, 0, s2, 0, 1);
        trans.transform(s2, 0, s2, 0, 1);
        float[] e2 = new float[]{x2 + w2, y2 + h2 / 2.0f};
        gradient.getTransform().transform(e2, 0, e2, 0, 1);
        trans.transform(e2, 0, e2, 0, 1);
        this.start = new Vector2f(s2[0], s2[1]);
        this.end = new Vector2f(e2[0], e2[1]);
        this.line = new Line(this.start, this.end);
    }

    public Vector2f getCoordFor(float x2, float y2) {
        Vector2f result = new Vector2f();
        this.line.getClosestPoint(new Vector2f(x2, y2), result);
        float u2 = result.distance(this.start);
        return new Vector2f(u2 /= this.line.length(), 0.0f);
    }
}

