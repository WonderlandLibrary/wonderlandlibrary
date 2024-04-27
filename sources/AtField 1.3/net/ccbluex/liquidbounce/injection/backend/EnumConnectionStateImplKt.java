/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.EnumConnectionState
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.IEnumConnectionState;
import net.ccbluex.liquidbounce.injection.backend.EnumConnectionStateImpl;
import net.minecraft.network.EnumConnectionState;

public final class EnumConnectionStateImplKt {
    public static final IEnumConnectionState wrap(EnumConnectionState enumConnectionState) {
        boolean bl = false;
        return new EnumConnectionStateImpl(enumConnectionState);
    }

    public static final EnumConnectionState unwrap(IEnumConnectionState iEnumConnectionState) {
        boolean bl = false;
        return ((EnumConnectionStateImpl)iEnumConnectionState).getWrapped();
    }
}

