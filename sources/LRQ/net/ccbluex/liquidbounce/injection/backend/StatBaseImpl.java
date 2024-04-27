/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.stats.StatBase
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.stats.IStatBase;
import net.minecraft.stats.StatBase;
import org.jetbrains.annotations.Nullable;

public final class StatBaseImpl
implements IStatBase {
    private final StatBase wrapped;

    public boolean equals(@Nullable Object other) {
        return other instanceof StatBaseImpl && ((StatBaseImpl)other).wrapped.equals(this.wrapped);
    }

    public final StatBase getWrapped() {
        return this.wrapped;
    }

    public StatBaseImpl(StatBase wrapped) {
        this.wrapped = wrapped;
    }
}

