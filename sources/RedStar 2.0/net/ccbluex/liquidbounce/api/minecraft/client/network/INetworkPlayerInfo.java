package net.ccbluex.liquidbounce.api.minecraft.client.network;

import com.mojang.authlib.GameProfile;
import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.ITeam;
import net.ccbluex.liquidbounce.api.minecraft.util.IIChatComponent;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00002\n\n\u0000\n\u0000\n\n\b\n\n\b\n\n\b\n\n\b\n\b\n\b\bf\u000020R0X¦¢\bR0X¦¢\b\b\tR\n0X¦¢\b\f\rR0X¦¢\bR0X¦¢\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/network/INetworkPlayerInfo;", "", "displayName", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IIChatComponent;", "getDisplayName", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IIChatComponent;", "gameProfile", "Lcom/mojang/authlib/GameProfile;", "getGameProfile", "()Lcom/mojang/authlib/GameProfile;", "locationSkin", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "getLocationSkin", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "playerTeam", "Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/ITeam;", "getPlayerTeam", "()Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/ITeam;", "responseTime", "", "getResponseTime", "()I", "Pride"})
public interface INetworkPlayerInfo {
    @NotNull
    public IResourceLocation getLocationSkin();

    public int getResponseTime();

    @NotNull
    public GameProfile getGameProfile();

    @Nullable
    public ITeam getPlayerTeam();

    @Nullable
    public IIChatComponent getDisplayName();
}
