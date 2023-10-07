package net.minecraft.client.network;

import com.google.common.base.Objects;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldSettings;

public class NetworkPlayerInfo {

    /**
     * The GameProfile for the player represented by this NetworkPlayerInfo instance
     */
    private final GameProfile gameProfile;
    private WorldSettings.GameType gameType;

    /**
     * Player response time to server in milliseconds
     */
    private int responseTime;
    private boolean playerTexturesLoaded = false;
    private ResourceLocation locationSkin;
    private ResourceLocation locationCape;
    private String skinType;

    /**
     * When this is non-null, it is displayed instead of the player's real name
     */
    private IChatComponent displayName;
    private int field_178873_i = 0;
    private int field_178870_j = 0;
    private long field_178871_k = 0L;
    private long field_178868_l = 0L;
    private long field_178869_m = 0L;

    public NetworkPlayerInfo(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }

    public NetworkPlayerInfo(S38PacketPlayerListItem.AddPlayerData addPlayerData) {
        this.gameProfile = addPlayerData.getProfile();
        this.gameType = addPlayerData.getGameMode();
        this.responseTime = addPlayerData.getPing();
        this.displayName = addPlayerData.getDisplayName();
    }

    /**
     * Returns the GameProfile for the player represented by this NetworkPlayerInfo instance
     */
    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    public WorldSettings.GameType getGameType() {
        return this.gameType;
    }

    protected void setGameType(WorldSettings.GameType p_178839_1_) {
        this.gameType = p_178839_1_;
    }

    public int getResponseTime() {
        return this.responseTime;
    }

    protected void setResponseTime(int p_178838_1_) {
        this.responseTime = p_178838_1_;
    }

    public boolean hasLocationSkin() {
        return this.locationSkin != null;
    }

    public String getSkinType() {
        return this.skinType == null ? DefaultPlayerSkin.getSkinType(this.gameProfile.getId()) : this.skinType;
    }

    public ResourceLocation getRawLocationSkin() {
        if (this.locationSkin == null) loadPlayerTextures();
        return this.locationSkin != null ? this.locationSkin : DefaultPlayerSkin.getDefaultSkin(this.gameProfile.getId());
    }

    public ResourceLocation getLocationSkin() {
        if (this.locationSkin == null) loadPlayerTextures();
        return Objects.firstNonNull(this.locationSkin, DefaultPlayerSkin.getDefaultSkin(this.gameProfile.getId()));
    }

    public ResourceLocation getLocationCape() {
        if (this.locationCape == null) this.loadPlayerTextures();
        return this.locationCape;
    }

    public ScorePlayerTeam getPlayerTeam() {
        return Minecraft.getInstance().world.getScoreboard().getPlayersTeam(this.getGameProfile().getName());
    }

    protected void loadPlayerTextures() {
        synchronized (this) {
            if (!this.playerTexturesLoaded) {
                this.playerTexturesLoaded = true;

                Minecraft.getInstance().getSkinManager().loadProfileTextures(this.gameProfile, (type, location, profileTexture) -> {
                    switch (type) {
                        case SKIN:
                            NetworkPlayerInfo.this.locationSkin = location;
                            NetworkPlayerInfo.this.skinType = profileTexture.getMetadata("model");

                            if (NetworkPlayerInfo.this.skinType == null) {
                                NetworkPlayerInfo.this.skinType = "default";
                            }

                            break;

                        case CAPE:
                            NetworkPlayerInfo.this.locationCape = location;
                    }
                }, true);
            }
        }
    }

    public IChatComponent getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(IChatComponent displayName) {
        this.displayName = displayName;
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
