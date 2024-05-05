/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.font.effects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import me.kiras.aimwhere.libraries.slick.UnicodeFont;
import me.kiras.aimwhere.libraries.slick.font.Glyph;
import me.kiras.aimwhere.libraries.slick.font.effects.ConfigurableEffect;
import me.kiras.aimwhere.libraries.slick.font.effects.EffectUtil;

public class ColorEffect
implements ConfigurableEffect {
    private Color color = Color.white;

    public ColorEffect() {
    }

    public ColorEffect(Color color) {
        this.color = color;
    }

    @Override
    public void draw(BufferedImage image2, Graphics2D g, UnicodeFont unicodeFont, Glyph glyph) {
        g.setColor(this.color);
        g.fill(glyph.getShape());
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

    @Override
    public List getValues() {
        ArrayList<ConfigurableEffect.Value> values2 = new ArrayList<ConfigurableEffect.Value>();
        values2.add(EffectUtil.colorValue("Color", this.color));
        return values2;
    }

    @Override
    public void setValues(List values2) {
        for (ConfigurableEffect.Value value : values2) {
            if (!value.getName().equals("Color")) continue;
            this.setColor((Color)value.getObject());
        }
    }
}

