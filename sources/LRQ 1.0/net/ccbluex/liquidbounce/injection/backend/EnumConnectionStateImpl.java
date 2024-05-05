/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.EnumConnectionState
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.IEnumConnectionState;
import net.minecraft.network.EnumConnectionState;
import org.jetbrains.annotations.Nullable;

public final class EnumConnectionStateImpl
implements IEnumConnectionState {
    private final EnumConnectionState wrapped;

    @Override
    public boolean isHandshake() {
        return this.wrapped == EnumConnectionState.HANDSHAKING;
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof EnumConnectionStateImpl && ((EnumConnectionStateImpl)other).wrapped == this.wrapped;
    }

    public final EnumConnectionState getWrapped() {
        return this.wrapped;
    }

    public EnumConnectionStateImpl(EnumConnectionState wrapped) {
        this.wrapped = wrapped;
    }
}

