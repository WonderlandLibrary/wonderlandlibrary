/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.entity.player.PlayerCapabilities
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.entity.player.IPlayerCapabilities;
import net.minecraft.entity.player.PlayerCapabilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0010\u0000\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010\u0010\u001a\u00020\u00062\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0096\u0002R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\bR$\u0010\u000b\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u00068V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u000b\u0010\b\"\u0004\b\f\u0010\rR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0013"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/PlayerCapabilitiesImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/entity/player/IPlayerCapabilities;", "wrapped", "Lnet/minecraft/entity/player/PlayerCapabilities;", "(Lnet/minecraft/entity/player/PlayerCapabilities;)V", "allowFlying", "", "getAllowFlying", "()Z", "isCreativeMode", "value", "isFlying", "setFlying", "(Z)V", "getWrapped", "()Lnet/minecraft/entity/player/PlayerCapabilities;", "equals", "other", "", "LiKingSense"})
public final class PlayerCapabilitiesImpl
implements IPlayerCapabilities {
    @NotNull
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
        return other instanceof PlayerCapabilitiesImpl && Intrinsics.areEqual((Object)((PlayerCapabilitiesImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final PlayerCapabilities getWrapped() {
        return this.wrapped;
    }

    public PlayerCapabilitiesImpl(@NotNull PlayerCapabilities wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

