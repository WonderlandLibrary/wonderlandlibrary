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
    public boolean isCreativeMode() {
        return this.wrapped.field_75098_d;
    }

    @Override
    public boolean isFlying() {
        return this.wrapped.field_75100_b;
    }

    @Override
    public void setFlying(boolean bl) {
        this.wrapped.field_75100_b = bl;
    }

    public PlayerCapabilitiesImpl(PlayerCapabilities playerCapabilities) {
        this.wrapped = playerCapabilities;
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof PlayerCapabilitiesImpl && ((PlayerCapabilitiesImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public boolean getAllowFlying() {
        return this.wrapped.field_75101_c;
    }

    public final PlayerCapabilities getWrapped() {
        return this.wrapped;
    }
}

