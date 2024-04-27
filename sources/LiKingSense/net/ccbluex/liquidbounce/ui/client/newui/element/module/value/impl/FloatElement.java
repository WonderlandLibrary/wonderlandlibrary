/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.ranges.RangesKt
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.ui.client.newui.element.module.value.impl;

import java.awt.Color;
import java.math.BigDecimal;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import net.ccbluex.liquidbounce.ui.client.newui.ColorManager;
import net.ccbluex.liquidbounce.ui.client.newui.element.components.Slider;
import net.ccbluex.liquidbounce.ui.client.newui.element.module.value.ValueElement;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MouseUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.FloatValue;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J@\u0010\f\u001a\u00020\u00022\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u00022\u0006\u0010\u0011\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0014H\u0016J0\u0010\u0016\u001a\u00020\u00172\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u00022\u0006\u0010\u0011\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u0002H\u0016J0\u0010\u0018\u001a\u00020\u00172\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u00022\u0006\u0010\u0011\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u0002H\u0016J\u0010\u0010\u0019\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0002H\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/newui/element/module/value/impl/FloatElement;", "Lnet/ccbluex/liquidbounce/ui/client/newui/element/module/value/ValueElement;", "", "savedValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "(Lnet/ccbluex/liquidbounce/value/FloatValue;)V", "dragged", "", "getSavedValue", "()Lnet/ccbluex/liquidbounce/value/FloatValue;", "slider", "Lnet/ccbluex/liquidbounce/ui/client/newui/element/components/Slider;", "drawElement", "mouseX", "", "mouseY", "x", "y", "width", "bgColor", "Ljava/awt/Color;", "accentColor", "onClick", "", "onRelease", "round", "f", "LiKingSense"})
public final class FloatElement
extends ValueElement<Float> {
    private final Slider slider;
    private boolean dragged;
    @NotNull
    private final FloatValue savedValue;

    @Override
    public float drawElement(int mouseX, int mouseY, float x, float y, float width, @NotNull Color bgColor, @NotNull Color accentColor) {
        Intrinsics.checkParameterIsNotNull((Object)bgColor, (String)"bgColor");
        Intrinsics.checkParameterIsNotNull((Object)accentColor, (String)"accentColor");
        float valueDisplay = 30.0f + (float)Fonts.misans40.getStringWidth(String.valueOf((float)((int)this.savedValue.getMaximum()) + 0.01f));
        int maxLength = Fonts.misans40.getStringWidth(String.valueOf(this.savedValue.getMaximum()));
        int minLength = Fonts.misans40.getStringWidth(String.valueOf(this.savedValue.getMinimum()));
        int nameLength = Fonts.misans40.getStringWidth(this.getValue().getName());
        float sliderWidth = width - 50.0f - (float)nameLength - (float)maxLength - (float)minLength - valueDisplay;
        float startPoint = x + width - 20.0f - sliderWidth - (float)maxLength - valueDisplay;
        if (this.dragged) {
            this.savedValue.set(Float.valueOf(RangesKt.coerceIn((float)this.round(this.savedValue.getMinimum() + (this.savedValue.getMaximum() - this.savedValue.getMinimum()) / sliderWidth * ((float)mouseX - startPoint)), (float)this.savedValue.getMinimum(), (float)this.savedValue.getMaximum())));
        }
        int currLength = Fonts.misans40.getStringWidth(String.valueOf(this.round(((Number)this.savedValue.get()).floatValue())));
        Fonts.misans40.drawString(this.getValue().getName(), x + 10.0f, y + 10.0f - (float)Fonts.misans40.getFontHeight() / 2.0f + 2.0f, -1);
        Fonts.misans40.drawString(String.valueOf(this.savedValue.getMaximum()), x + width - 10.0f - (float)maxLength - valueDisplay, y + 10.0f - (float)Fonts.misans40.getFontHeight() / 2.0f + 2.0f, -1);
        Fonts.misans40.drawString(String.valueOf(this.savedValue.getMinimum()), x + width - 30.0f - sliderWidth - (float)maxLength - (float)minLength - valueDisplay, y + 10.0f - (float)Fonts.misans40.getFontHeight() / 2.0f + 2.0f, -1);
        this.slider.setValue(RangesKt.coerceIn((float)((Number)this.savedValue.get()).floatValue(), (float)this.savedValue.getMinimum(), (float)this.savedValue.getMaximum()), this.savedValue.getMinimum(), this.savedValue.getMaximum());
        this.slider.onDraw(x + width - 20.0f - sliderWidth - (float)maxLength - valueDisplay, y + 10.0f, sliderWidth, accentColor);
        RenderUtils.drawRoundedRect(x + width - 5.0f - valueDisplay, y + 2.0f, x + width - 10.0f, y + 18.0f, 4.0f, ColorManager.INSTANCE.getButton().getRGB());
        RenderUtils.customRounded(x + width - 18.0f, y + 2.0f, x + width - 10.0f, y + 18.0f, 0.0f, 4.0f, 4.0f, 0.0f, ColorManager.INSTANCE.getButtonOutline().getRGB());
        RenderUtils.customRounded(x + width - 5.0f - valueDisplay, y + 2.0f, x + width + 3.0f - valueDisplay, y + (float)18, 4.0f, 0.0f, 0.0f, 4.0f, ColorManager.INSTANCE.getButtonOutline().getRGB());
        Fonts.misans40.drawString(String.valueOf(this.round(((Number)this.savedValue.get()).floatValue())), x + width + 6.0f - valueDisplay, y + 10.0f - (float)Fonts.misans40.getFontHeight() / 2.0f + 2.0f, -1);
        Fonts.misans40.drawString("-", x + width - 3.0f - valueDisplay, y + 10.0f - (float)Fonts.misans40.getFontHeight() / 2.0f + 2.0f, -1);
        Fonts.misans40.drawString("+", x + width - 17.0f, y + 10.0f - (float)Fonts.misans40.getFontHeight() / 2.0f + 2.0f, -1);
        return this.getValueHeight();
    }

    @Override
    public void onClick(int mouseX, int mouseY, float x, float y, float width) {
        float endPoint;
        float valueDisplay = 30.0f + (float)Fonts.misans40.getStringWidth(String.valueOf((float)((int)this.savedValue.getMaximum()) + 0.01f));
        int maxLength = Fonts.misans40.getStringWidth(String.valueOf(this.savedValue.getMaximum()));
        int minLength = Fonts.misans40.getStringWidth(String.valueOf(this.savedValue.getMinimum()));
        int nameLength = Fonts.misans40.getStringWidth(this.getValue().getName());
        float sliderWidth = width - 50.0f - (float)nameLength - (float)maxLength - (float)minLength - valueDisplay;
        float startPoint = x + width - 30.0f - sliderWidth - valueDisplay - (float)maxLength;
        if (MouseUtils.mouseWithinBounds(mouseX, mouseY, startPoint, y + 5.0f, endPoint = x + width - 10.0f - valueDisplay - (float)maxLength, y + 15.0f)) {
            this.dragged = true;
        }
        if (MouseUtils.mouseWithinBounds(mouseX, mouseY, x + width - 5.0f - valueDisplay, y + 2.0f, x + width + 3.0f - valueDisplay, y + 18.0f)) {
            this.savedValue.set(Float.valueOf(RangesKt.coerceIn((float)this.round(((Number)this.savedValue.get()).floatValue() - 0.01f), (float)this.savedValue.getMinimum(), (float)this.savedValue.getMaximum())));
        }
        if (MouseUtils.mouseWithinBounds(mouseX, mouseY, x + width - 18.0f, y + 2.0f, x + width - 10.0f, y + 18.0f)) {
            this.savedValue.set(Float.valueOf(RangesKt.coerceIn((float)this.round(((Number)this.savedValue.get()).floatValue() + 0.01f), (float)this.savedValue.getMinimum(), (float)this.savedValue.getMaximum())));
        }
    }

    @Override
    public void onRelease(int mouseX, int mouseY, float x, float y, float width) {
        if (this.dragged) {
            this.dragged = false;
        }
    }

    private final float round(float f) {
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(f));
        return 0.floatValue();
    }

    @NotNull
    public final FloatValue getSavedValue() {
        return this.savedValue;
    }

    public FloatElement(@NotNull FloatValue savedValue) {
        Intrinsics.checkParameterIsNotNull((Object)savedValue, (String)"savedValue");
        super(savedValue);
        this.savedValue = savedValue;
        this.slider = new Slider();
    }
}

