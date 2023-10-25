/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Objects
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
    private String skinType;
    private WorldSettings.GameType gameType;
    private int field_178873_i = 0;
    private int field_178870_j = 0;
    private final GameProfile gameProfile;
    private ResourceLocation locationCape;
    private long field_178869_m = 0L;
    private long field_178868_l = 0L;
    private int responseTime;
    private IChatComponent displayName;
    private ResourceLocation locationSkin;
    private boolean playerTexturesLoaded = false;
    private long field_178871_k = 0L;

    public void func_178843_c(long l) {
        this.field_178869_m = l;
    }

    public IChatComponent getDisplayName() {
        return this.displayName;
    }

    public void func_178836_b(int n) {
        this.field_178873_i = n;
    }

    public ScorePlayerTeam getPlayerTeam() {
        Minecraft.getMinecraft();
        return Minecraft.theWorld.getScoreboard().getPlayersTeam(this.getGameProfile().getName());
    }

    public void func_178844_b(long l) {
        this.field_178868_l = l;
    }

    public void func_178846_a(long l) {
        this.field_178871_k = l;
    }

    public boolean hasLocationSkin() {
        return this.locationSkin != null;
    }

    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    public void setDisplayName(IChatComponent iChatComponent) {
        this.displayName = iChatComponent;
    }

    public long func_178855_p() {
        return this.field_178869_m;
    }

    protected void loadPlayerTextures() {
        NetworkPlayerInfo networkPlayerInfo = this;
        synchronized (networkPlayerInfo) {
            if (!this.playerTexturesLoaded) {
                this.playerTexturesLoaded = true;
                Minecraft.getMinecraft().getSkinManager().loadProfileTextures(this.gameProfile, new SkinManager.SkinAvailableCallback(){

                    @Override
                    public void skinAvailable(MinecraftProfileTexture.Type type, ResourceLocation resourceLocation, MinecraftProfileTexture minecraftProfileTexture) {
                        switch (type) {
                            case SKIN: {
                                NetworkPlayerInfo.this.locationSkin = resourceLocation;
                                NetworkPlayerInfo.this.skinType = minecraftProfileTexture.getMetadata("model");
                                if (NetworkPlayerInfo.this.skinType != null) break;
                                NetworkPlayerInfo.this.skinType = "default";
                                break;
                            }
                            case CAPE: {
                                NetworkPlayerInfo.this.locationCape = resourceLocation;
                            }
                        }
                    }
                }, true);
            }
        }
    }

    public WorldSettings.GameType getGameType() {
        return this.gameType;
    }

    public String getSkinType() {
        return this.skinType == null ? DefaultPlayerSkin.getSkinType(this.gameProfile.getId()) : this.skinType;
    }

    public int func_178835_l() {
        return this.field_178873_i;
    }

    public NetworkPlayerInfo(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }

    public NetworkPlayerInfo(S38PacketPlayerListItem.AddPlayerData addPlayerData) {
        this.gameProfile = addPlayerData.getProfile();
        this.gameType = addPlayerData.getGameMode();
        this.responseTime = addPlayerData.getPing();
        this.displayName = addPlayerData.getDisplayName();
    }

    public ResourceLocation getLocationSkin() {
        if (this.locationSkin == null) {
            this.loadPlayerTextures();
        }
        return (ResourceLocation)Objects.firstNonNull((Object)this.locationSkin, (Object)DefaultPlayerSkin.getDefaultSkin(this.gameProfile.getId()));
    }

    public void func_178857_c(int n) {
        this.field_178870_j = n;
    }

    public long func_178847_n() {
        return this.field_178871_k;
    }

    public int getResponseTime() {
        return this.responseTime;
    }

    protected void setGameType(WorldSettings.GameType gameType) {
        this.gameType = gameType;
    }

    public long func_178858_o() {
        return this.field_178868_l;
    }

    public ResourceLocation getLocationCape() {
        if (this.locationCape == null) {
            this.loadPlayerTextures();
        }
        return this.locationCape;
    }

    protected void setResponseTime(int n) {
        this.responseTime = n;
    }

    public int func_178860_m() {
        return this.field_178870_j;
    }
}

