/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.util.Session
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import com.mojang.authlib.GameProfile;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.util.ISession;
import net.minecraft.util.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0096\u0002R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\n8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0014\u0010\r\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\bR\u0014\u0010\u000f\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\bR\u0014\u0010\u0011\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006\u0019"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/SessionImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/util/ISession;", "wrapped", "Lnet/minecraft/util/Session;", "(Lnet/minecraft/util/Session;)V", "playerId", "", "getPlayerId", "()Ljava/lang/String;", "profile", "Lcom/mojang/authlib/GameProfile;", "getProfile", "()Lcom/mojang/authlib/GameProfile;", "sessionType", "getSessionType", "token", "getToken", "username", "getUsername", "getWrapped", "()Lnet/minecraft/util/Session;", "equals", "", "other", "", "LiKingSense"})
public final class SessionImpl
implements ISession {
    @NotNull
    private final Session wrapped;

    @Override
    @NotNull
    public GameProfile getProfile() {
        GameProfile gameProfile = this.wrapped.func_148256_e();
        Intrinsics.checkExpressionValueIsNotNull((Object)gameProfile, (String)"wrapped.profile");
        return gameProfile;
    }

    @Override
    @NotNull
    public String getUsername() {
        String string = this.wrapped.func_111285_a();
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"wrapped.username");
        return string;
    }

    @Override
    @NotNull
    public String getPlayerId() {
        String string = this.wrapped.func_148255_b();
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"wrapped.playerID");
        return string;
    }

    @Override
    @NotNull
    public String getSessionType() {
        return this.wrapped.field_152429_d.name();
    }

    @Override
    @NotNull
    public String getToken() {
        String string = this.wrapped.func_148254_d();
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"wrapped.token");
        return string;
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof SessionImpl && Intrinsics.areEqual((Object)((SessionImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final Session getWrapped() {
        return this.wrapped;
    }

    public SessionImpl(@NotNull Session wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

