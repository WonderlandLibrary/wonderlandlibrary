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
    public NetworkPlayerInfo playerInfo;

    public AbstractClientPlayer(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Override
    public boolean isSpectator() {
        if (Minecraft.getMinecraft().theWorld == null) {
            return false;
        }
        NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(this.getGameProfile().getId());
        if (networkplayerinfo == null) return false;
        if (networkplayerinfo.getGameType() != WorldSettings.GameType.SPECTATOR) return false;
        return true;
    }

    public boolean hasPlayerInfo() {
        if (this.getPlayerInfo() == null) return false;
        return true;
    }

    protected NetworkPlayerInfo getPlayerInfo() {
        if (this.playerInfo != null) return this.playerInfo;
        this.playerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(this.getUniqueID());
        return this.playerInfo;
    }

    public boolean hasSkin() {
        NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
        if (networkplayerinfo == null) return false;
        if (!networkplayerinfo.hasLocationSkin()) return false;
        return true;
    }

    public ResourceLocation getLocationSkin() {
        ResourceLocation resourceLocation;
        NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
        if (networkplayerinfo == null) {
            resourceLocation = DefaultPlayerSkin.getDefaultSkin(this.getUniqueID());
            return resourceLocation;
        }
        resourceLocation = networkplayerinfo.getLocationSkin();
        return resourceLocation;
    }

    public ResourceLocation getLocationCape() {
        NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
        if (networkplayerinfo == null) {
            return null;
        }
        ResourceLocation resourceLocation = networkplayerinfo.getLocationCape();
        return resourceLocation;
    }

    public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation resourceLocationIn, String username) {
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        ITextureObject itextureobject = texturemanager.getTexture(resourceLocationIn);
        if (itextureobject != null) return (ThreadDownloadImageData)itextureobject;
        itextureobject = new ThreadDownloadImageData(null, String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", StringUtils.stripControlCodes(username)), DefaultPlayerSkin.getDefaultSkin(AbstractClientPlayer.getOfflineUUID(username)), new ImageBufferDownload());
        texturemanager.loadTexture(resourceLocationIn, itextureobject);
        return (ThreadDownloadImageData)itextureobject;
    }

    public static ResourceLocation getLocationSkin(String username) {
        return new ResourceLocation("skins/" + StringUtils.stripControlCodes(username));
    }

    public String getSkinType() {
        String string;
        NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
        if (networkplayerinfo == null) {
            string = DefaultPlayerSkin.getSkinType(this.getUniqueID());
            return string;
        }
        string = networkplayerinfo.getSkinType();
        return string;
    }

    public float getFovModifier() {
        float f = 1.0f;
        if (this.capabilities.isFlying) {
            f *= 1.1f;
        }
        IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        f = (float)((double)f * ((iattributeinstance.getAttributeValue() / (double)this.capabilities.getWalkSpeed() + 1.0) / 2.0));
        if (this.capabilities.getWalkSpeed() == 0.0f || Float.isNaN(f) || Float.isInfinite(f)) {
            f = 1.0f;
        }
        if (!this.isUsingItem()) return f;
        if (this.getItemInUse().getItem() != Items.bow) return f;
        int i = this.getItemInUseDuration();
        float f1 = (float)i / 20.0f;
        f1 = f1 > 1.0f ? 1.0f : (f1 *= f1);
        f *= 1.0f - f1 * 0.15f;
        return f;
    }
}

