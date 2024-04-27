/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.minecraft.client.gui.ScaledResolution;
import org.jetbrains.annotations.Nullable;

public final class ScaledResolutionImpl
implements IScaledResolution {
    private final ScaledResolution wrapped;

    @Override
    public int getScaleFactor() {
        return this.wrapped.func_78325_e();
    }

    public ScaledResolutionImpl(ScaledResolution scaledResolution) {
        this.wrapped = scaledResolution;
    }

    public final ScaledResolution getWrapped() {
        return this.wrapped;
    }

    @Override
    public int getScaledHeight() {
        return this.wrapped.func_78328_b();
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof ScaledResolutionImpl && ((ScaledResolutionImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public int getScaledWidth() {
        return this.wrapped.func_78326_a();
    }
}

