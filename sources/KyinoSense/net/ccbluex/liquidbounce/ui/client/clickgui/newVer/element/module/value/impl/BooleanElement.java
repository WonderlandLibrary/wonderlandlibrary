/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.newVer.element.module.value.impl;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.ui.client.clickgui.newVer.element.components.Checkbox;
import net.ccbluex.liquidbounce.ui.client.clickgui.newVer.element.module.value.ValueElement;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MouseUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J@\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0016J0\u0010\u0013\u001a\u00020\u00142\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\tH\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/clickgui/newVer/element/module/value/impl/BooleanElement;", "Lnet/ccbluex/liquidbounce/ui/client/clickgui/newVer/element/module/value/ValueElement;", "", "value", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "(Lnet/ccbluex/liquidbounce/value/BoolValue;)V", "checkbox", "Lnet/ccbluex/liquidbounce/ui/client/clickgui/newVer/element/components/Checkbox;", "drawElement", "", "mouseX", "", "mouseY", "x", "y", "width", "bgColor", "Ljava/awt/Color;", "accentColor", "onClick", "", "KyinoClient"})
public final class BooleanElement
extends ValueElement<Boolean> {
    private final Checkbox checkbox;

    @Override
    public float drawElement(int mouseX, int mouseY, float x, float y, float width, @NotNull Color bgColor, @NotNull Color accentColor) {
        Intrinsics.checkParameterIsNotNull(bgColor, "bgColor");
        Intrinsics.checkParameterIsNotNull(accentColor, "accentColor");
        this.checkbox.setState((Boolean)this.getValue().get());
        this.checkbox.onDraw(x + 10.0f, y + 5.0f, 10.0f, 10.0f, bgColor, accentColor);
        Fonts.font40.drawString(this.getValue().getName(), x + 25.0f, y + 10.0f - (float)Fonts.font40.field_78288_b / 2.0f + 2.0f, -1);
        return this.getValueHeight();
    }

    @Override
    public void onClick(int mouseX, int mouseY, float x, float y, float width) {
        if (this.isDisplayable() && MouseUtils.mouseWithinBounds(mouseX, mouseY, x, y, x + width, y + 20.0f)) {
            this.getValue().set((Boolean)this.getValue().get() == false);
        }
    }

    public BooleanElement(@NotNull BoolValue value) {
        Intrinsics.checkParameterIsNotNull(value, "value");
        super(value);
        this.checkbox = new Checkbox();
    }
}

