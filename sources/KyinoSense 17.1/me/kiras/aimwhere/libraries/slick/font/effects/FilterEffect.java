/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.font.effects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import me.kiras.aimwhere.libraries.slick.UnicodeFont;
import me.kiras.aimwhere.libraries.slick.font.Glyph;
import me.kiras.aimwhere.libraries.slick.font.effects.Effect;
import me.kiras.aimwhere.libraries.slick.font.effects.EffectUtil;

public class FilterEffect
implements Effect {
    private BufferedImageOp filter;

    public FilterEffect() {
    }

    public FilterEffect(BufferedImageOp filter) {
        this.filter = filter;
    }

    @Override
    public void draw(BufferedImage image2, Graphics2D g, UnicodeFont unicodeFont, Glyph glyph) {
        BufferedImage scratchImage = EffectUtil.getScratchImage();
        this.filter.filter(image2, scratchImage);
        image2.getGraphics().drawImage(scratchImage, 0, 0, null);
    }

    public BufferedImageOp getFilter() {
        return this.filter;
    }

    public void setFilter(BufferedImageOp filter) {
        this.filter = filter;
    }
}

