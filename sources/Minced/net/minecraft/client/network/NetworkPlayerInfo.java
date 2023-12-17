// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.network;

import net.minecraft.util.text.TextComponentString;
import ru.tuskevich.modules.Module;
import ru.tuskevich.modules.impl.PLAYER.StreamerMode;
import ru.tuskevich.Minced;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScorePlayerTeam;
import javax.annotation.Nullable;
import com.google.common.base.MoreObjects;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import com.google.common.collect.Maps;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameType;
import net.minecraft.util.ResourceLocation;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import java.util.Map;
import com.mojang.authlib.GameProfile;

public class NetworkPlayerInfo
{
    private final GameProfile gameProfile;
    Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures;
    private GameType gameType;
    private int responseTime;
    private boolean playerTexturesLoaded;
    private String skinType;
    private ITextComponent displayName;
    private int lastHealth;
    private int displayHealth;
    private long lastHealthTime;
    private long healthBlinkTime;
    private long renderVisibilityId;
    
    public NetworkPlayerInfo(final GameProfile profile) {
        this.playerTextures = (Map<MinecraftProfileTexture.Type, ResourceLocation>)Maps.newEnumMap((Class)MinecraftProfileTexture.Type.class);
        this.gameProfile = profile;
    }
    
    public NetworkPlayerInfo(final SPacketPlayerListItem.AddPlayerData entry) {
        this.playerTextures = (Map<MinecraftProfileTexture.Type, ResourceLocation>)Maps.newEnumMap((Class)MinecraftProfileTexture.Type.class);
        this.gameProfile = entry.getProfile();
        this.gameType = entry.getGameMode();
        this.responseTime = entry.getPing();
        this.displayName = entry.getDisplayName();
    }
    
    public GameProfile getGameProfile() {
        return this.gameProfile;
    }
    
    public GameType getGameType() {
        return this.gameType;
    }
    
    protected void setGameType(final GameType gameMode) {
        this.gameType = gameMode;
    }
    
    public int getResponseTime() {
        return this.responseTime;
    }
    
    protected void setResponseTime(final int latency) {
        this.responseTime = latency;
    }
    
    public boolean hasLocationSkin() {
        return this.getLocationSkin() != null;
    }
    
    public String getSkinType() {
        return (this.skinType == null) ? DefaultPlayerSkin.getSkinType(this.gameProfile.getId()) : this.skinType;
    }
    
    public ResourceLocation getLocationSkin() {
        this.loadPlayerTextures();
        return (ResourceLocation)MoreObjects.firstNonNull((Object)this.playerTextures.get(MinecraftProfileTexture.Type.SKIN), (Object)DefaultPlayerSkin.getDefaultSkin(this.gameProfile.getId()));
    }
    
    @Nullable
    public ResourceLocation getLocationCape() {
        this.loadPlayerTextures();
        return this.playerTextures.get(MinecraftProfileTexture.Type.CAPE);
    }
    
    @Nullable
    public ResourceLocation getLocationElytra() {
        this.loadPlayerTextures();
        return this.playerTextures.get(MinecraftProfileTexture.Type.ELYTRA);
    }
    
    @Nullable
    public ScorePlayerTeam getPlayerTeam() {
        return Minecraft.getMinecraft().world.getScoreboard().getPlayersTeam(this.getGameProfile().getName());
    }
    
    protected void loadPlayerTextures() {
        synchronized (this) {
            if (!this.playerTexturesLoaded) {
                this.playerTexturesLoaded = true;
                Minecraft.getMinecraft().getSkinManager().loadProfileTextures(this.gameProfile, new SkinManager.SkinAvailableCallback() {
                    @Override
                    public void skinAvailable(final MinecraftProfileTexture.Type typeIn, final ResourceLocation location, final MinecraftProfileTexture profileTexture) {
                        switch (typeIn) {
                            case SKIN: {
                                NetworkPlayerInfo.this.playerTextures.put(MinecraftProfileTexture.Type.SKIN, location);
                                NetworkPlayerInfo.this.skinType = profileTexture.getMetadata("model");
                                if (NetworkPlayerInfo.this.skinType == null) {
                                    NetworkPlayerInfo.this.skinType = "default";
                                    break;
                                }
                                break;
                            }
                            case CAPE: {
                                NetworkPlayerInfo.this.playerTextures.put(MinecraftProfileTexture.Type.CAPE, location);
                                break;
                            }
                            case ELYTRA: {
                                NetworkPlayerInfo.this.playerTextures.put(MinecraftProfileTexture.Type.ELYTRA, location);
                                break;
                            }
                        }
                    }
                }, true);
            }
        }
    }
    
    public void setDisplayName(@Nullable final ITextComponent displayNameIn) {
        this.displayName = displayNameIn;
    }
    
    @Nullable
    public ITextComponent getDisplayName() {
        return (Minced.getInstance().manager.getModule(StreamerMode.class).state && StreamerMode.youtuber.get()) ? new TextComponentString("tuskevichpon") : this.displayName;
    }
    
    public int getLastHealth() {
        return this.lastHealth;
    }
    
    public void setLastHealth(final int p_178836_1_) {
        this.lastHealth = p_178836_1_;
    }
    
    public int getDisplayHealth() {
        return this.displayHealth;
    }
    
    public void setDisplayHealth(final int p_178857_1_) {
        this.displayHealth = p_178857_1_;
    }
    
    public long getLastHealthTime() {
        return this.lastHealthTime;
    }
    
    public void setLastHealthTime(final long p_178846_1_) {
        this.lastHealthTime = p_178846_1_;
    }
    
    public long getHealthBlinkTime() {
        return this.healthBlinkTime;
    }
    
    public void setHealthBlinkTime(final long p_178844_1_) {
        this.healthBlinkTime = p_178844_1_;
    }
    
    public long getRenderVisibilityId() {
        return this.renderVisibilityId;
    }
    
    public void setRenderVisibilityId(final long p_178843_1_) {
        this.renderVisibilityId = p_178843_1_;
    }
}
