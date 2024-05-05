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
    public int getScaledWidth() {
        return this.wrapped.func_78326_a();
    }

    @Override
    public int getScaledHeight() {
        return this.wrapped.func_78328_b();
    }

    @Override
    public int getScaleFactor() {
        return this.wrapped.func_78325_e();
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof ScaledResolutionImpl && ((ScaledResolutionImpl)other).wrapped.equals(this.wrapped);
    }

    public final ScaledResolution getWrapped() {
        return this.wrapped;
    }

    public ScaledResolutionImpl(ScaledResolution wrapped) {
        this.wrapped = wrapped;
    }
}

