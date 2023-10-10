/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package com.wallhacks.losebypass.utils.skinlayers;

import com.mojang.authlib.GameProfile;
import com.wallhacks.losebypass.utils.skinlayers.NativeImage;
import com.wallhacks.losebypass.utils.skinlayers.render.CustomizableModelPart;
import com.wallhacks.losebypass.utils.skinlayers.render.SolidPixelWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

public class SkinUtil {
    public static boolean hasCustomSkin(AbstractClientPlayer player) {
        if (DefaultPlayerSkin.getDefaultSkin(player.getUniqueID()).equals(player.getLocationSkin())) return false;
        return true;
    }

    private static NativeImage getSkinTexture(AbstractClientPlayer player) {
        return SkinUtil.getTexture(player.getLocationSkin());
    }

    private static NativeImage getTexture(ResourceLocation resource) {
        NativeImage skin = new NativeImage(64, 64, false);
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        ITextureObject abstractTexture = textureManager.getTexture(resource);
        if (abstractTexture == null) {
            return null;
        }
        GlStateManager.bindTexture(abstractTexture.getGlTextureId());
        skin.downloadTexture(0, false);
        return skin;
    }

    public static boolean setup3dLayers(AbstractClientPlayer abstractClientPlayerEntity, boolean thinArms, ModelPlayer model) {
        if (!SkinUtil.hasCustomSkin(abstractClientPlayerEntity)) {
            return false;
        }
        NativeImage skin = SkinUtil.getSkinTexture(abstractClientPlayerEntity);
        if (skin == null) {
            return false;
        }
        CustomizableModelPart[] layers = new CustomizableModelPart[5];
        layers[0] = SolidPixelWrapper.wrapBox(skin, 4, 12, 4, 0, 48, true, 0.0f);
        layers[1] = SolidPixelWrapper.wrapBox(skin, 4, 12, 4, 0, 32, true, 0.0f);
        if (thinArms) {
            layers[2] = SolidPixelWrapper.wrapBox(skin, 3, 12, 4, 48, 48, true, -2.5f);
            layers[3] = SolidPixelWrapper.wrapBox(skin, 3, 12, 4, 40, 32, true, -2.5f);
        } else {
            layers[2] = SolidPixelWrapper.wrapBox(skin, 4, 12, 4, 48, 48, true, -2.5f);
            layers[3] = SolidPixelWrapper.wrapBox(skin, 4, 12, 4, 40, 32, true, -2.5f);
        }
        layers[4] = SolidPixelWrapper.wrapBox(skin, 8, 12, 4, 16, 32, true, -0.8f);
        abstractClientPlayerEntity.setupSkinLayers(layers);
        abstractClientPlayerEntity.setupHeadLayers(SolidPixelWrapper.wrapBox(skin, 8, 8, 8, 32, 0, false, 0.6f));
        skin.close();
        return true;
    }

    public static boolean setup3dLayers(GameProfile gameprofile) {
        if (gameprofile != null) return false;
        return false;
    }
}

