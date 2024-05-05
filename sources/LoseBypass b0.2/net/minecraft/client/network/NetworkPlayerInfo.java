/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.minecraft.MinecraftProfileTexture
 *  com.mojang.authlib.minecraft.MinecraftProfileTexture$Type
 */
package net.minecraft.client.network;

import com.google.common.base.Objects;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldSettings;

public class NetworkPlayerInfo {
    public final GameProfile gameProfile;
    private WorldSettings.GameType gameType;
    private int responseTime;
    public boolean playerTexturesLoaded = false;
    private ResourceLocation locationSkin;
    private ResourceLocation locationCape;
    private String skinType;
    private IChatComponent displayName;
    private int field_178873_i = 0;
    private int field_178870_j = 0;
    private long field_178871_k = 0L;
    private long field_178868_l = 0L;
    private long field_178869_m = 0L;

    public NetworkPlayerInfo(GameProfile p_i46294_1_) {
        this.gameProfile = p_i46294_1_;
    }

    public NetworkPlayerInfo(S38PacketPlayerListItem.AddPlayerData p_i46295_1_) {
        this.gameProfile = p_i46295_1_.getProfile();
        this.gameType = p_i46295_1_.getGameMode();
        this.responseTime = p_i46295_1_.getPing();
        this.displayName = p_i46295_1_.getDisplayName();
    }

    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    public WorldSettings.GameType getGameType() {
        return this.gameType;
    }

    public int getResponseTime() {
        return this.responseTime;
    }

    public void setGameType(WorldSettings.GameType p_178839_1_) {
        this.gameType = p_178839_1_;
    }

    protected void setResponseTime(int p_178838_1_) {
        this.responseTime = p_178838_1_;
    }

    public boolean hasLocationSkin() {
        if (this.locationSkin == null) return false;
        return true;
    }

    public String getSkinType() {
        String string;
        if (this.skinType == null) {
            string = DefaultPlayerSkin.getSkinType(this.gameProfile.getId());
            return string;
        }
        string = this.skinType;
        return string;
    }

    public ResourceLocation getLocationSkin() {
        if (this.locationSkin != null) return (ResourceLocation)Objects.firstNonNull((Object)this.locationSkin, (Object)DefaultPlayerSkin.getDefaultSkin(this.gameProfile.getId()));
        this.loadPlayerTextures();
        return (ResourceLocation)Objects.firstNonNull((Object)this.locationSkin, (Object)DefaultPlayerSkin.getDefaultSkin(this.gameProfile.getId()));
    }

    public ResourceLocation getLocationCape() {
        if (this.locationCape != null) return this.locationCape;
        this.loadPlayerTextures();
        return this.locationCape;
    }

    public ScorePlayerTeam getPlayerTeam() {
        return Minecraft.getMinecraft().theWorld.getScoreboard().getPlayersTeam(this.getGameProfile().getName());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void loadPlayerTextures() {
        NetworkPlayerInfo networkPlayerInfo = this;
        synchronized (networkPlayerInfo) {
            if (this.playerTexturesLoaded) return;
            this.playerTexturesLoaded = true;
            Minecraft.getMinecraft().getSkinManager().loadProfileTextures(this.gameProfile, new SkinManager.SkinAvailableCallback(){

                @Override
                public void skinAvailable(MinecraftProfileTexture.Type p_180521_1_, ResourceLocation location, MinecraftProfileTexture profileTexture) {
                    switch (2.$SwitchMap$com$mojang$authlib$minecraft$MinecraftProfileTexture$Type[p_180521_1_.ordinal()]) {
                        case 1: {
                            NetworkPlayerInfo.this.locationSkin = location;
                            NetworkPlayerInfo.this.skinType = profileTexture.getMetadata("model");
                            if (NetworkPlayerInfo.this.skinType != null) return;
                            NetworkPlayerInfo.this.skinType = "default";
                            return;
                        }
                        case 2: {
                            NetworkPlayerInfo.this.locationCape = location;
                            return;
                        }
                    }
                }
            }, true);
            return;
        }
    }

    public void setDisplayName(IChatComponent displayNameIn) {
        this.displayName = displayNameIn;
    }

    public IChatComponent getDisplayName() {
        return this.displayName;
    }

    public int func_178835_l() {
        return this.field_178873_i;
    }

    public void func_178836_b(int p_178836_1_) {
        this.field_178873_i = p_178836_1_;
    }

    public int func_178860_m() {
        return this.field_178870_j;
    }

    public void func_178857_c(int p_178857_1_) {
        this.field_178870_j = p_178857_1_;
    }

    public long func_178847_n() {
        return this.field_178871_k;
    }

    public void func_178846_a(long p_178846_1_) {
        this.field_178871_k = p_178846_1_;
    }

    public long func_178858_o() {
        return this.field_178868_l;
    }

    public void func_178844_b(long p_178844_1_) {
        this.field_178868_l = p_178844_1_;
    }

    public long func_178855_p() {
        return this.field_178869_m;
    }

    public void func_178843_c(long p_178843_1_) {
        this.field_178869_m = p_178843_1_;
    }
}

