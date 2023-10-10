/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.font.effects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.Glyph;
import org.newdawn.slick.font.effects.ConfigurableEffect;
import org.newdawn.slick.font.effects.EffectUtil;

public class ColorEffect
implements ConfigurableEffect {
    private Color color = Color.white;

    public ColorEffect() {
    }

    public ColorEffect(Color color) {
        this.color = color;
    }

    public void draw(BufferedImage image, Graphics2D g2, UnicodeFont unicodeFont, Glyph glyph) {
        g2.setColor(this.color);
        g2.fill(glyph.getShape());
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        if (color == null) {
            throw new IllegalArgumentException("color cannot be null.");
        }
        this.color = color;
    }

    public String toString() {
        return "Color";
    }

    public List getValues() {
        ArrayList<ConfigurableEffect.Value> values = new ArrayList<ConfigurableEffect.Value>();
        values.add(EffectUtil.colorValue("Color", this.color));
        return values;
    }

    public void setValues(List values) {
        for (ConfigurableEffect.Value value : values) {
            if (!value.getName().equals("Color")) continue;
            this.setColor((Color)value.getObject());
        }
    }
}

