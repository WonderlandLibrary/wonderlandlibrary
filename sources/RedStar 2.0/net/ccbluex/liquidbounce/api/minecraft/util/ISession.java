package net.ccbluex.liquidbounce.api.minecraft.util;

import com.mojang.authlib.GameProfile;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\u0000\n\n\b\n\n\b\t\bf\u000020R0X¦¢\bR0X¦¢\b\b\tR\n0X¦¢\bR\f0X¦¢\b\rR0X¦¢\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/util/ISession;", "", "playerId", "", "getPlayerId", "()Ljava/lang/String;", "profile", "Lcom/mojang/authlib/GameProfile;", "getProfile", "()Lcom/mojang/authlib/GameProfile;", "sessionType", "getSessionType", "token", "getToken", "username", "getUsername", "Pride"})
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
