/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.ui.client.newui.element.module.value;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.Value;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000B\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\f\n\u0002\b\u0003\b&\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\u00a2\u0006\u0002\u0010\u0005J@\u0010\u000e\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\t2\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0016H&J\u0006\u0010\u0018\u001a\u00020\u0019J0\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\t2\u0006\u0010\u0014\u001a\u00020\tH&J\u0018\u0010\u001c\u001a\u00020\u00192\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u0010H\u0016J0\u0010 \u001a\u00020\u001b2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\t2\u0006\u0010\u0014\u001a\u00020\tH\u0016R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\u00020\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\r\u00a8\u0006!"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/newui/element/module/value/ValueElement;", "T", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "value", "Lnet/ccbluex/liquidbounce/value/Value;", "(Lnet/ccbluex/liquidbounce/value/Value;)V", "getValue", "()Lnet/ccbluex/liquidbounce/value/Value;", "valueHeight", "", "getValueHeight", "()F", "setValueHeight", "(F)V", "drawElement", "mouseX", "", "mouseY", "x", "y", "width", "bgColor", "Ljava/awt/Color;", "accentColor", "isDisplayable", "", "onClick", "", "onKeyPress", "typed", "", "keyCode", "onRelease", "LiKingSense"})
public abstract class ValueElement<T>
extends MinecraftInstance {
    private float valueHeight;
    @NotNull
    private final Value<T> value;

    public final float getValueHeight() {
        return this.valueHeight;
    }

    public final void setValueHeight(float f) {
        this.valueHeight = f;
    }

    public abstract float drawElement(int var1, int var2, float var3, float var4, float var5, @NotNull Color var6, @NotNull Color var7);

    public abstract void onClick(int var1, int var2, float var3, float var4, float var5);

    public void onRelease(int mouseX, int mouseY, float x, float y, float width) {
    }

    public boolean onKeyPress(char typed, int keyCode) {
        return false;
    }

    public final boolean isDisplayable() {
        return true;
    }

    @NotNull
    public final Value<T> getValue() {
        return this.value;
    }

    public ValueElement(@NotNull Value<T> value) {
        Intrinsics.checkParameterIsNotNull(value, (String)"value");
        this.value = value;
        this.valueHeight = 20.0f;
    }
}

