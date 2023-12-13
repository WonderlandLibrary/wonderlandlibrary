/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.font.effects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.GeneralPath;
import java.util.List;
import org.newdawn.slick.font.effects.ConfigurableEffect;
import org.newdawn.slick.font.effects.EffectUtil;
import org.newdawn.slick.font.effects.OutlineEffect;

public class OutlineZigzagEffect
extends OutlineEffect {
    private float amplitude = 1.0f;
    private float wavelength = 3.0f;

    public OutlineZigzagEffect() {
        this.setStroke(new ZigzagStroke());
    }

    public float getWavelength() {
        return this.wavelength;
    }

    public void setWavelength(float wavelength) {
        this.wavelength = wavelength;
    }

    public float getAmplitude() {
        return this.amplitude;
    }

    public void setAmplitude(float amplitude) {
        this.amplitude = amplitude;
    }

    public OutlineZigzagEffect(int width, Color color) {
        super(width, color);
    }

    public String toString() {
        return "Outline (Zigzag)";
    }

    public List getValues() {
        List values = super.getValues();
        values.add(EffectUtil.floatValue("Wavelength", this.wavelength, 1.0f, 100.0f, "This setting controls the wavelength of the outline. The smaller the value, the more segments will be used to draw the outline."));
        values.add(EffectUtil.floatValue("Amplitude", this.amplitude, 0.5f, 50.0f, "This setting controls the amplitude of the outline. The bigger the value, the more the zigzags will vary."));
        return values;
    }

    public void setValues(List values) {
        super.setValues(values);
        for (ConfigurableEffect.Value value : values) {
            if (value.getName().equals("Wavelength")) {
                this.wavelength = ((Float)value.getObject()).floatValue();
                continue;
            }
            if (!value.getName().equals("Amplitude")) continue;
            this.amplitude = ((Float)value.getObject()).floatValue();
        }
    }

    private class ZigzagStroke
    implements Stroke {
        private static final float FLATNESS = 1.0f;

        private ZigzagStroke() {
        }

        public Shape createStrokedShape(Shape shape) {
            GeneralPath result = new GeneralPath();
            FlatteningPathIterator it = new FlatteningPathIterator(shape.getPathIterator(null), 1.0);
            float[] points = new float[6];
            float moveX = 0.0f;
            float moveY = 0.0f;
            float lastX = 0.0f;
            float lastY = 0.0f;
            float thisX = 0.0f;
            float thisY = 0.0f;
            int type = 0;
            float next = 0.0f;
            int phase = 0;
            while (!it.isDone()) {
                type = it.currentSegment(points);
                switch (type) {
                    case 0: {
                        moveX = lastX = points[0];
                        moveY = lastY = points[1];
                        result.moveTo(moveX, moveY);
                        next = OutlineZigzagEffect.this.wavelength / 2.0f;
                        break;
                    }
                    case 4: {
                        points[0] = moveX;
                        points[1] = moveY;
                    }
                    case 1: {
                        thisX = points[0];
                        thisY = points[1];
                        float dx = thisX - lastX;
                        float dy = thisY - lastY;
                        float distance = (float)Math.sqrt(dx * dx + dy * dy);
                        if (distance >= next) {
                            float r2 = 1.0f / distance;
                            while (distance >= next) {
                                float x2 = lastX + next * dx * r2;
                                float y2 = lastY + next * dy * r2;
                                if (!(phase & true)) {
                                    result.lineTo(x2 + OutlineZigzagEffect.this.amplitude * dy * r2, y2 - OutlineZigzagEffect.this.amplitude * dx * r2);
                                } else {
                                    result.lineTo(x2 - OutlineZigzagEffect.this.amplitude * dy * r2, y2 + OutlineZigzagEffect.this.amplitude * dx * r2);
                                }
                                next += OutlineZigzagEffect.this.wavelength;
                                ++phase;
                            }
                        }
                        next -= distance;
                        lastX = thisX;
                        lastY = thisY;
                        if (type != 4) break;
                        result.closePath();
                    }
                }
                it.next();
            }
            return new BasicStroke(OutlineZigzagEffect.this.getWidth(), 2, OutlineZigzagEffect.this.getJoin()).createStrokedShape(result);
        }
    }
}

