/*
 * Decompiled with CFR 0.152.
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

public abstract class AbstractClientPlayer
extends EntityPlayer {
    private NetworkPlayerInfo playerInfo;

    public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation resourceLocation, String string) {
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        ITextureObject iTextureObject = textureManager.getTexture(resourceLocation);
        if (iTextureObject == null) {
            iTextureObject = new ThreadDownloadImageData(null, String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", StringUtils.stripControlCodes(string)), DefaultPlayerSkin.getDefaultSkin(AbstractClientPlayer.getOfflineUUID(string)), new ImageBufferDownload());
            textureManager.loadTexture(resourceLocation, iTextureObject);
        }
        return (ThreadDownloadImageData)iTextureObject;
    }

    public AbstractClientPlayer(World world, GameProfile gameProfile) {
        super(world, gameProfile);
    }

    public ResourceLocation getLocationSkin() {
        NetworkPlayerInfo networkPlayerInfo = this.getPlayerInfo();
        return networkPlayerInfo == null ? DefaultPlayerSkin.getDefaultSkin(this.getUniqueID()) : networkPlayerInfo.getLocationSkin();
    }

    @Override
    public boolean isSpectator() {
        NetworkPlayerInfo networkPlayerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(this.getGameProfile().getId());
        return networkPlayerInfo != null && networkPlayerInfo.getGameType() == WorldSettings.GameType.SPECTATOR;
    }

    public static ResourceLocation getLocationSkin(String string) {
        return new ResourceLocation("skins/" + StringUtils.stripControlCodes(string));
    }

    public String getSkinType() {
        NetworkPlayerInfo networkPlayerInfo = this.getPlayerInfo();
        return networkPlayerInfo == null ? DefaultPlayerSkin.getSkinType(this.getUniqueID()) : networkPlayerInfo.getSkinType();
    }

    protected NetworkPlayerInfo getPlayerInfo() {
        if (this.playerInfo == null) {
            this.playerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(this.getUniqueID());
        }
        return this.playerInfo;
    }

    public float getFovModifier() {
        float f = 1.0f;
        if (this.capabilities.isFlying) {
            f *= 1.1f;
        }
        IAttributeInstance iAttributeInstance = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        f = (float)((double)f * ((iAttributeInstance.getAttributeValue() / (double)this.capabilities.getWalkSpeed() + 1.0) / 2.0));
        if (this.capabilities.getWalkSpeed() == 0.0f || Float.isNaN(f) || Float.isInfinite(f)) {
            f = 1.0f;
        }
        if (this.isUsingItem() && this.getItemInUse().getItem() == Items.bow) {
            int n = this.getItemInUseDuration();
            float f2 = (float)n / 20.0f;
            f2 = f2 > 1.0f ? 1.0f : (f2 *= f2);
            f *= 1.0f - f2 * 0.15f;
        }
        return f;
    }

    public boolean hasPlayerInfo() {
        return this.getPlayerInfo() != null;
    }

    public ResourceLocation getLocationCape() {
        NetworkPlayerInfo networkPlayerInfo = this.getPlayerInfo();
        return networkPlayerInfo == null ? null : networkPlayerInfo.getLocationCape();
    }

    public boolean hasSkin() {
        NetworkPlayerInfo networkPlayerInfo = this.getPlayerInfo();
        return networkPlayerInfo != null && networkPlayerInfo.hasLocationSkin();
    }
}

