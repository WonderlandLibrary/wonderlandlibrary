/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.ranges.RangesKt
 */
package cc.paimon.skid.lbp.newVer.element.module.value.impl;

import cc.paimon.skid.lbp.newVer.ColorManager;
import cc.paimon.skid.lbp.newVer.MouseUtils;
import cc.paimon.skid.lbp.newVer.element.components.Slider;
import cc.paimon.skid.lbp.newVer.element.module.value.ValueElement;
import java.awt.Color;
import java.math.BigDecimal;
import kotlin.ranges.RangesKt;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.FloatValue;

public final class FloatElement
extends ValueElement {
    private final Slider slider;
    private boolean dragged;
    private final FloatValue savedValue;

    private final float round(float f) {
        return new BigDecimal(String.valueOf(f)).setScale(2, 4).floatValue();
    }

    public final FloatValue getSavedValue() {
        return this.savedValue;
    }

    @Override
    public void onRelease(int n, int n2, float f, float f2, float f3) {
        if (this.dragged) {
            this.dragged = false;
        }
    }

    @Override
    public float drawElement(int n, int n2, float f, float f2, float f3, Color color, Color color2) {
        float f4 = 30.0f + (float)Fonts.posterama40.getStringWidth(String.valueOf((float)((int)this.savedValue.getMaximum()) + 0.01f));
        int n3 = Fonts.posterama40.getStringWidth(String.valueOf(this.savedValue.getMaximum()));
        int n4 = Fonts.posterama40.getStringWidth(String.valueOf(this.savedValue.getMinimum()));
        int n5 = Fonts.posterama40.getStringWidth(this.getValue().getName());
        float f5 = f3 - 50.0f - (float)n5 - (float)n3 - (float)n4 - f4;
        float f6 = f + f3 - 20.0f - f5 - (float)n3 - f4;
        if (this.dragged) {
            this.savedValue.set((Object)Float.valueOf(RangesKt.coerceIn((float)this.round(this.savedValue.getMinimum() + (this.savedValue.getMaximum() - this.savedValue.getMinimum()) / f5 * ((float)n - f6)), (float)this.savedValue.getMinimum(), (float)this.savedValue.getMaximum())));
        }
        int n6 = Fonts.posterama40.getStringWidth(String.valueOf(this.round(((Number)this.savedValue.get()).floatValue())));
        Fonts.posterama40.drawString(this.getValue().getName(), f + 10.0f, f2 + 10.0f - (float)Fonts.posterama40.getFontHeight() / 2.0f + 2.0f, new Color(26, 26, 26).getRGB());
        Fonts.posterama40.drawString(String.valueOf(this.savedValue.getMaximum()), f + f3 - 10.0f - (float)n3 - f4, f2 + 10.0f - (float)Fonts.posterama40.getFontHeight() / 2.0f + 2.0f, new Color(26, 26, 26).getRGB());
        Fonts.posterama40.drawString(String.valueOf(this.savedValue.getMinimum()), f + f3 - 30.0f - f5 - (float)n3 - (float)n4 - f4, f2 + 10.0f - (float)Fonts.posterama40.getFontHeight() / 2.0f + 2.0f, new Color(26, 26, 26).getRGB());
        this.slider.setValue(RangesKt.coerceIn((float)((Number)this.savedValue.get()).floatValue(), (float)this.savedValue.getMinimum(), (float)this.savedValue.getMaximum()), this.savedValue.getMinimum(), this.savedValue.getMaximum());
        this.slider.onDraw(f + f3 - 20.0f - f5 - (float)n3 - f4, f2 + 10.0f, f5, color2);
        RenderUtils.drawRoundedRect(f + f3 - 5.0f - f4, f2 + 2.0f, f + f3 - 10.0f, f2 + 18.0f, 4.0f, ColorManager.INSTANCE.getButton().getRGB());
        RenderUtils.customRounded(f + f3 - 18.0f, f2 + 2.0f, f + f3 - 10.0f, f2 + 18.0f, 0.0f, 4.0f, 4.0f, 0.0f, new Color(241, 243, 247).getRGB());
        RenderUtils.customRounded(f + f3 - 5.0f - f4, f2 + 2.0f, f + f3 + 3.0f - f4, f2 + (float)18, 4.0f, 0.0f, 0.0f, 4.0f, new Color(241, 243, 247).getRGB());
        Fonts.posterama40.drawString(String.valueOf(this.round(((Number)this.savedValue.get()).floatValue())), f + f3 + 6.0f - f4, f2 + 10.0f - (float)Fonts.posterama40.getFontHeight() / 2.0f + 2.0f, new Color(26, 26, 26).getRGB());
        Fonts.posterama40.drawString("-", f + f3 - 3.0f - f4, f2 + 10.0f - (float)Fonts.posterama40.getFontHeight() / 2.0f + 2.0f, new Color(26, 26, 26).getRGB());
        Fonts.posterama40.drawString("+", f + f3 - 17.0f, f2 + 10.0f - (float)Fonts.posterama40.getFontHeight() / 2.0f + 2.0f, new Color(26, 26, 26).getRGB());
        return this.getValueHeight();
    }

    @Override
    public void onClick(int n, int n2, float f, float f2, float f3) {
        float f4;
        float f5 = 30.0f + (float)Fonts.posterama40.getStringWidth(String.valueOf((float)((int)this.savedValue.getMaximum()) + 0.01f));
        int n3 = Fonts.posterama40.getStringWidth(String.valueOf(this.savedValue.getMaximum()));
        int n4 = Fonts.posterama40.getStringWidth(String.valueOf(this.savedValue.getMinimum()));
        int n5 = Fonts.posterama40.getStringWidth(this.getValue().getName());
        float f6 = f3 - 50.0f - (float)n5 - (float)n3 - (float)n4 - f5;
        float f7 = f + f3 - 30.0f - f6 - f5 - (float)n3;
        if (MouseUtils.mouseWithinBounds(n, n2, f7, f2 + 5.0f, f4 = f + f3 - 10.0f - f5 - (float)n3, f2 + 15.0f)) {
            this.dragged = true;
        }
        if (MouseUtils.mouseWithinBounds(n, n2, f + f3 - 5.0f - f5, f2 + 2.0f, f + f3 + 3.0f - f5, f2 + 18.0f)) {
            this.savedValue.set((Object)Float.valueOf(RangesKt.coerceIn((float)this.round(((Number)this.savedValue.get()).floatValue() - 0.01f), (float)this.savedValue.getMinimum(), (float)this.savedValue.getMaximum())));
        }
        if (MouseUtils.mouseWithinBounds(n, n2, f + f3 - 18.0f, f2 + 2.0f, f + f3 - 10.0f, f2 + 18.0f)) {
            this.savedValue.set((Object)Float.valueOf(RangesKt.coerceIn((float)this.round(((Number)this.savedValue.get()).floatValue() + 0.01f), (float)this.savedValue.getMinimum(), (float)this.savedValue.getMaximum())));
        }
    }

    public FloatElement(FloatValue floatValue) {
        super(floatValue);
        this.savedValue = floatValue;
        this.slider = new Slider();
    }
}

