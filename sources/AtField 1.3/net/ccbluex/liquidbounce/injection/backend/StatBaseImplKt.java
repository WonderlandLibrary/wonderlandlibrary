/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.stats.StatBase
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.stats.IStatBase;
import net.ccbluex.liquidbounce.injection.backend.StatBaseImpl;
import net.minecraft.stats.StatBase;

public final class StatBaseImplKt {
    public static final IStatBase wrap(StatBase statBase) {
        boolean bl = false;
        return new StatBaseImpl(statBase);
    }

    public static final StatBase unwrap(IStatBase iStatBase) {
        boolean bl = false;
        return ((StatBaseImpl)iStatBase).getWrapped();
    }
}

