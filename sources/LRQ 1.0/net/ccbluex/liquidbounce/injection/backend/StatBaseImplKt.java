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
    public static final StatBase unwrap(IStatBase $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((StatBaseImpl)$this$unwrap).getWrapped();
    }

    public static final IStatBase wrap(StatBase $this$wrap) {
        int $i$f$wrap = 0;
        return new StatBaseImpl($this$wrap);
    }
}

