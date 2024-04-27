/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  kotlin.Metadata
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.api.minecraft.util;

import com.mojang.authlib.GameProfile;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\bf\u0018\u00002\u00020\u0001R\u0012\u0010\u0002\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0007X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0012\u0010\n\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\u0005R\u0012\u0010\f\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u0005R\u0012\u0010\u000e\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0005\u00a8\u0006\u0010"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/util/ISession;", "", "playerId", "", "getPlayerId", "()Ljava/lang/String;", "profile", "Lcom/mojang/authlib/GameProfile;", "getProfile", "()Lcom/mojang/authlib/GameProfile;", "sessionType", "getSessionType", "token", "getToken", "username", "getUsername", "LiKingSense"})
public interface ISession {
    @NotNull
    public GameProfile getProfile();

    @NotNull
    public String getUsername();

    @NotNull
    public String getPlayerId();

    @NotNull
    public String getSessionType();

    @NotNull
    public String getToken();
}

