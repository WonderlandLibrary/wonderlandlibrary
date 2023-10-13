/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package net.minecraft.client.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import optifine.CapeUtils;
import optifine.Config;
import optifine.PlayerConfigurations;
import optifine.Reflector;

public abstract class AbstractClientPlayer
extends EntityPlayer {
    private NetworkPlayerInfo field_175157_a;
    private ResourceLocation locationOfCape = null;
    private String nameClear = null;
    private static final String __OBFID = "CL_00000935";

    public AbstractClientPlayer(World worldIn, GameProfile p_i45074_2_) {
        super(worldIn, p_i45074_2_);
        this.nameClear = p_i45074_2_.getName();
        if (this.nameClear != null && !this.nameClear.isEmpty()) {
            this.nameClear = StringUtils.stripControlCodes(this.nameClear);
        }
        CapeUtils.downloadCape(this);
        PlayerConfigurations.getPlayerConfiguration(this);
    }

    @Override
    public boolean func_175149_v() {
        NetworkPlayerInfo var1 = Minecraft.getMinecraft().getNetHandler().func_175102_a(this.getGameProfile().getId());
        return var1 != null && var1.getGameType() == WorldSettings.GameType.SPECTATOR;
    }

    public boolean hasCape() {
        return this.func_175155_b() != null;
    }

    protected NetworkPlayerInfo func_175155_b() {
        if (this.field_175157_a == null) {
            this.field_175157_a = Minecraft.getMinecraft().getNetHandler().func_175102_a(this.getUniqueID());
        }
        return this.field_175157_a;
    }

    public boolean hasSkin() {
        NetworkPlayerInfo var1 = this.func_175155_b();
        return var1 != null && var1.func_178856_e();
    }

    public ResourceLocation getLocationSkin() {
        NetworkPlayerInfo var1 = this.func_175155_b();
        return var1 == null ? DefaultPlayerSkin.func_177334_a(this.getUniqueID()) : var1.func_178837_g();
    }

    public ResourceLocation getLocationCape() {
        if (!Config.isShowCapes()) {
            return null;
        }
        if (this.locationOfCape != null) {
            return this.locationOfCape;
        }
        NetworkPlayerInfo var1 = this.func_175155_b();
        return var1 == null ? null : var1.func_178861_h();
    }

    public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation resourceLocationIn, String username) {
        TextureManager var2 = Minecraft.getMinecraft().getTextureManager();
        ITextureObject var3 = var2.getTexture(resourceLocationIn);
        if (var3 == null) {
            var3 = new ThreadDownloadImageData(null, String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", StringUtils.stripControlCodes(username)), DefaultPlayerSkin.func_177334_a(AbstractClientPlayer.func_175147_b(username)), new ImageBufferDownload());
            var2.loadTexture(resourceLocationIn, var3);
        }
        return (ThreadDownloadImageData)var3;
    }

    public static ResourceLocation getLocationSkin(String username) {
        return new ResourceLocation("skins/" + StringUtils.stripControlCodes(username));
    }

    public String func_175154_l() {
        NetworkPlayerInfo var1 = this.func_175155_b();
        return var1 == null ? DefaultPlayerSkin.func_177332_b(this.getUniqueID()) : var1.func_178851_f();
    }

    public float func_175156_o() {
        float var1 = 1.0f;
        if (this.capabilities.isFlying) {
            var1 *= 1.1f;
        }
        IAttributeInstance var2 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        var1 = (float)((double)var1 * ((var2.getAttributeValue() / (double)this.capabilities.getWalkSpeed() + 1.0) / 2.0));
        if (this.capabilities.getWalkSpeed() == 0.0f || Float.isNaN(var1) || Float.isInfinite(var1)) {
            var1 = 1.0f;
        }
        if (this.isUsingItem() && this.getItemInUse().getItem() == Items.bow) {
            int var3 = this.getItemInUseDuration();
            float var4 = (float)var3 / 20.0f;
            var4 = var4 > 1.0f ? 1.0f : (var4 *= var4);
            var1 *= 1.0f - var4 * 0.15f;
        }
        return Reflector.ForgeHooksClient_getOffsetFOV.exists() ? Reflector.callFloat(Reflector.ForgeHooksClient_getOffsetFOV, this, Float.valueOf(var1)) : var1;
    }

    public String getNameClear() {
        return this.nameClear;
    }

    public ResourceLocation getLocationOfCape() {
        return this.locationOfCape;
    }

    public void setLocationOfCape(ResourceLocation locationOfCape) {
        this.locationOfCape = locationOfCape;
    }
}

