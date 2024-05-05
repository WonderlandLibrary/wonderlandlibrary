/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.AxisAlignedBB
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.injection.backend.AxisAlignedBBImpl;
import net.minecraft.util.math.AxisAlignedBB;

public final class AxisAlignedBBImplKt {
    public static final AxisAlignedBB unwrap(IAxisAlignedBB $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((AxisAlignedBBImpl)$this$unwrap).getWrapped();
    }

    public static final IAxisAlignedBB wrap(AxisAlignedBB $this$wrap) {
        int $i$f$wrap = 0;
        return new AxisAlignedBBImpl($this$wrap);
    }
}

