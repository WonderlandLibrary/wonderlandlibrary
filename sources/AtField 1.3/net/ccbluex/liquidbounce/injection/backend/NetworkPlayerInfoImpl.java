/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.scoreboard.ScorePlayerTeam
 *  net.minecraft.scoreboard.Team
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.text.ITextComponent
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import com.mojang.authlib.GameProfile;
import net.ccbluex.liquidbounce.api.minecraft.client.network.INetworkPlayerInfo;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.ITeam;
import net.ccbluex.liquidbounce.api.minecraft.util.IIChatComponent;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.injection.backend.IChatComponentImpl;
import net.ccbluex.liquidbounce.injection.backend.ResourceLocationImpl;
import net.ccbluex.liquidbounce.injection.backend.TeamImpl;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.Nullable;

public final class NetworkPlayerInfoImpl
implements INetworkPlayerInfo {
    private final NetworkPlayerInfo wrapped;

    @Override
    public IIChatComponent getDisplayName() {
        IIChatComponent iIChatComponent;
        ITextComponent iTextComponent = this.wrapped.func_178854_k();
        if (iTextComponent != null) {
            ITextComponent iTextComponent2 = iTextComponent;
            boolean bl = false;
            iIChatComponent = new IChatComponentImpl(iTextComponent2);
        } else {
            iIChatComponent = null;
        }
        return iIChatComponent;
    }

    @Override
    public GameProfile getGameProfile() {
        return this.wrapped.func_178845_a();
    }

    @Override
    public ITeam getPlayerTeam() {
        ITeam iTeam;
        ScorePlayerTeam scorePlayerTeam = this.wrapped.func_178850_i();
        if (scorePlayerTeam != null) {
            Team team = (Team)scorePlayerTeam;
            boolean bl = false;
            iTeam = new TeamImpl(team);
        } else {
            iTeam = null;
        }
        return iTeam;
    }

    public NetworkPlayerInfoImpl(NetworkPlayerInfo networkPlayerInfo) {
        this.wrapped = networkPlayerInfo;
    }

    @Override
    public int getResponseTime() {
        return this.wrapped.func_178853_c();
    }

    @Override
    public IResourceLocation getLocationSkin() {
        ResourceLocation resourceLocation = this.wrapped.func_178837_g();
        boolean bl = false;
        return new ResourceLocationImpl(resourceLocation);
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof NetworkPlayerInfoImpl && ((NetworkPlayerInfoImpl)object).wrapped.equals(this.wrapped);
    }

    public final NetworkPlayerInfo getWrapped() {
        return this.wrapped;
    }
}

