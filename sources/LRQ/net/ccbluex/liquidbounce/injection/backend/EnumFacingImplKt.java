/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.EnumFacing
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.injection.backend.EnumFacingImpl;
import net.minecraft.util.EnumFacing;

public final class EnumFacingImplKt {
    public static final EnumFacing unwrap(IEnumFacing $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((EnumFacingImpl)$this$unwrap).getWrapped();
    }

    public static final IEnumFacing wrap(EnumFacing $this$wrap) {
        int $i$f$wrap = 0;
        return new EnumFacingImpl($this$wrap);
    }
}

