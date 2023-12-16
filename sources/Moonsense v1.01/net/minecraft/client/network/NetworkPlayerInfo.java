// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.network;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScorePlayerTeam;
import com.google.common.base.Objects;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldSettings;
import com.mojang.authlib.GameProfile;

public class NetworkPlayerInfo
{
    private final GameProfile field_178867_a;
    private WorldSettings.GameType gameType;
    private int responseTime;
    private boolean field_178864_d;
    private ResourceLocation field_178865_e;
    private ResourceLocation field_178862_f;
    private String field_178863_g;
    private IChatComponent field_178872_h;
    private int field_178873_i;
    private int field_178870_j;
    private long field_178871_k;
    private long field_178868_l;
    private long field_178869_m;
    private static final String __OBFID = "CL_00000888";
    
    public NetworkPlayerInfo(final GameProfile p_i46294_1_) {
        this.field_178864_d = false;
        this.field_178873_i = 0;
        this.field_178870_j = 0;
        this.field_178871_k = 0L;
        this.field_178868_l = 0L;
        this.field_178869_m = 0L;
        this.field_178867_a = p_i46294_1_;
    }
    
    public NetworkPlayerInfo(final S38PacketPlayerListItem.AddPlayerData p_i46295_1_) {
        this.field_178864_d = false;
        this.field_178873_i = 0;
        this.field_178870_j = 0;
        this.field_178871_k = 0L;
        this.field_178868_l = 0L;
        this.field_178869_m = 0L;
        this.field_178867_a = p_i46295_1_.func_179962_a();
        this.gameType = p_i46295_1_.func_179960_c();
        this.responseTime = p_i46295_1_.func_179963_b();
    }
    
    public GameProfile func_178845_a() {
        return this.field_178867_a;
    }
    
    public WorldSettings.GameType getGameType() {
        return this.gameType;
    }
    
    public int getResponseTime() {
        return this.responseTime;
    }
    
    protected void func_178839_a(final WorldSettings.GameType p_178839_1_) {
        this.gameType = p_178839_1_;
    }
    
    protected void func_178838_a(final int p_178838_1_) {
        this.responseTime = p_178838_1_;
    }
    
    public boolean func_178856_e() {
        return this.field_178865_e != null;
    }
    
    public String func_178851_f() {
        return (this.field_178863_g == null) ? DefaultPlayerSkin.func_177332_b(this.field_178867_a.getId()) : this.field_178863_g;
    }
    
    public ResourceLocation func_178837_g() {
        if (this.field_178865_e == null) {
            this.func_178841_j();
        }
        return (ResourceLocation)Objects.firstNonNull((Object)this.field_178865_e, (Object)DefaultPlayerSkin.func_177334_a(this.field_178867_a.getId()));
    }
    
    public ResourceLocation func_178861_h() {
        if (this.field_178862_f == null) {
            this.func_178841_j();
        }
        return this.field_178862_f;
    }
    
    public ScorePlayerTeam func_178850_i() {
        return Minecraft.getMinecraft().theWorld.getScoreboard().getPlayersTeam(this.func_178845_a().getName());
    }
    
    protected void func_178841_j() {
        synchronized (this) {
            if (!this.field_178864_d) {
                this.field_178864_d = true;
                Minecraft.getMinecraft().getSkinManager().func_152790_a(this.field_178867_a, new SkinManager.SkinAvailableCallback() {
                    private static final String __OBFID = "CL_00002619";
                    
                    @Override
                    public void func_180521_a(final MinecraftProfileTexture.Type p_180521_1_, final ResourceLocation p_180521_2_, final MinecraftProfileTexture p_180521_3_) {
                        switch (SwitchType.field_178875_a[p_180521_1_.ordinal()]) {
                            case 1: {
                                NetworkPlayerInfo.access$0(NetworkPlayerInfo.this, p_180521_2_);
                                NetworkPlayerInfo.access$1(NetworkPlayerInfo.this, p_180521_3_.getMetadata("model"));
                                if (NetworkPlayerInfo.this.field_178863_g == null) {
                                    NetworkPlayerInfo.access$1(NetworkPlayerInfo.this, "default");
                                    break;
                                }
                                break;
                            }
                            case 2: {
                                NetworkPlayerInfo.access$3(NetworkPlayerInfo.this, p_180521_2_);
                                break;
                            }
                        }
                    }
                }, true);
            }
        }
    }
    
    public void func_178859_a(final IChatComponent p_178859_1_) {
        this.field_178872_h = p_178859_1_;
    }
    
    public IChatComponent func_178854_k() {
        return this.field_178872_h;
    }
    
    public int func_178835_l() {
        return this.field_178873_i;
    }
    
    public void func_178836_b(final int p_178836_1_) {
        this.field_178873_i = p_178836_1_;
    }
    
    public int func_178860_m() {
        return this.field_178870_j;
    }
    
    public void func_178857_c(final int p_178857_1_) {
        this.field_178870_j = p_178857_1_;
    }
    
    public long func_178847_n() {
        return this.field_178871_k;
    }
    
    public void func_178846_a(final long p_178846_1_) {
        this.field_178871_k = p_178846_1_;
    }
    
    public long func_178858_o() {
        return this.field_178868_l;
    }
    
    public void func_178844_b(final long p_178844_1_) {
        this.field_178868_l = p_178844_1_;
    }
    
    public long func_178855_p() {
        return this.field_178869_m;
    }
    
    public void func_178843_c(final long p_178843_1_) {
        this.field_178869_m = p_178843_1_;
    }
    
    static /* synthetic */ void access$0(final NetworkPlayerInfo networkPlayerInfo, final ResourceLocation field_178865_e) {
        networkPlayerInfo.field_178865_e = field_178865_e;
    }
    
    static /* synthetic */ void access$1(final NetworkPlayerInfo networkPlayerInfo, final String field_178863_g) {
        networkPlayerInfo.field_178863_g = field_178863_g;
    }
    
    static /* synthetic */ void access$3(final NetworkPlayerInfo networkPlayerInfo, final ResourceLocation field_178862_f) {
        networkPlayerInfo.field_178862_f = field_178862_f;
    }
    
    static final class SwitchType
    {
        static final int[] field_178875_a;
        private static final String __OBFID = "CL_00002618";
        
        static {
            field_178875_a = new int[MinecraftProfileTexture.Type.values().length];
            try {
                SwitchType.field_178875_a[MinecraftProfileTexture.Type.SKIN.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchType.field_178875_a[MinecraftProfileTexture.Type.CAPE.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
        }
    }
}
