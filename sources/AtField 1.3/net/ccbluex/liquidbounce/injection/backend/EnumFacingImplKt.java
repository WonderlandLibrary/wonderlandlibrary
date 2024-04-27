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
    public static final IEnumFacing wrap(EnumFacing enumFacing) {
        boolean bl = false;
        return new EnumFacingImpl(enumFacing);
    }

    public static final EnumFacing unwrap(IEnumFacing iEnumFacing) {
        boolean bl = false;
        return ((EnumFacingImpl)iEnumFacing).getWrapped();
    }
}

