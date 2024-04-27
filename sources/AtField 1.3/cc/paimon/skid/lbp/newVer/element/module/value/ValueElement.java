/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package cc.paimon.skid.lbp.newVer.element.module.value;

import java.awt.Color;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.Value;
import org.jetbrains.annotations.NotNull;

public abstract class ValueElement
extends MinecraftInstance {
    private float valueHeight;
    private final Value value;

    public abstract void onClick(int var1, int var2, float var3, float var4, float var5);

    public abstract float drawElement(int var1, int var2, float var3, float var4, float var5, @NotNull Color var6, @NotNull Color var7);

    public final void setValueHeight(float f) {
        this.valueHeight = f;
    }

    public final boolean isDisplayable() {
        return true;
    }

    public final Value getValue() {
        return this.value;
    }

    public boolean onKeyPress(char c, int n) {
        return false;
    }

    public ValueElement(Value value) {
        this.value = value;
        this.valueHeight = 20.0f;
    }

    public void onRelease(int n, int n2, float f, float f2, float f3) {
    }

    public final float getValueHeight() {
        return this.valueHeight;
    }
}

