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
    public static final IAxisAlignedBB wrap(AxisAlignedBB axisAlignedBB) {
        boolean bl = false;
        return new AxisAlignedBBImpl(axisAlignedBB);
    }

    public static final AxisAlignedBB unwrap(IAxisAlignedBB iAxisAlignedBB) {
        boolean bl = false;
        return ((AxisAlignedBBImpl)iAxisAlignedBB).getWrapped();
    }
}

