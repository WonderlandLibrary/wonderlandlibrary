/*
 * Decompiled with CFR 0.152.
 */
package cc.paimon.skid.lbp.newVer.element.module.value.impl;

import cc.paimon.skid.lbp.newVer.MouseUtils;
import cc.paimon.skid.lbp.newVer.element.components.Checkbox;
import cc.paimon.skid.lbp.newVer.element.module.value.ValueElement;
import java.awt.Color;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.value.BoolValue;

public final class BooleanElement
extends ValueElement {
    private final Checkbox checkbox = new Checkbox();

    @Override
    public void onClick(int n, int n2, float f, float f2, float f3) {
        if (this.isDisplayable() && MouseUtils.mouseWithinBounds(n, n2, f, f2, f + f3, f2 + 20.0f)) {
            this.getValue().set((Boolean)this.getValue().get() == false);
        }
    }

    public BooleanElement(BoolValue boolValue) {
        super(boolValue);
    }

    @Override
    public float drawElement(int n, int n2, float f, float f2, float f3, Color color, Color color2) {
        this.checkbox.setState((Boolean)this.getValue().get());
        this.checkbox.onDraw(f + 10.0f, f2 + 5.0f, 10.0f, 10.0f, color, color2);
        Fonts.posterama40.drawString(this.getValue().getName(), f + 25.0f, f2 + 10.0f - (float)Fonts.posterama40.getFontHeight() / 2.0f + 2.0f, new Color(26, 26, 26).getRGB());
        return this.getValueHeight();
    }
}

