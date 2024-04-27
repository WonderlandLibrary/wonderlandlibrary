/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.PlayerCapabilities
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.entity.player.IPlayerCapabilities;
import net.minecraft.entity.player.PlayerCapabilities;
import org.jetbrains.annotations.Nullable;

public final class PlayerCapabilitiesImpl
implements IPlayerCapabilities {
    private final PlayerCapabilities wrapped;

    @Override
    public boolean getAllowFlying() {
        return this.wrapped.field_75101_c;
    }

    @Override
    public boolean isFlying() {
        return this.wrapped.field_75100_b;
    }

    @Override
    public void setFlying(boolean value) {
        this.wrapped.field_75100_b = value;
    }

    @Override
    public boolean isCreativeMode() {
        return this.wrapped.field_75098_d;
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof PlayerCapabilitiesImpl && ((PlayerCapabilitiesImpl)other).wrapped.equals(this.wrapped);
    }

    public final PlayerCapabilities getWrapped() {
        return this.wrapped;
    }

    public PlayerCapabilitiesImpl(PlayerCapabilities wrapped) {
        this.wrapped = wrapped;
    }
}

