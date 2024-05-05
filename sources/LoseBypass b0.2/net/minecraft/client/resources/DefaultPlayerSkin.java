/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.resources;

import java.util.UUID;
import net.minecraft.util.ResourceLocation;

public class DefaultPlayerSkin {
    private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");
    private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");

    public static ResourceLocation getDefaultSkinLegacy() {
        return TEXTURE_STEVE;
    }

    public static ResourceLocation getDefaultSkin(UUID playerUUID) {
        ResourceLocation resourceLocation;
        if (DefaultPlayerSkin.isSlimSkin(playerUUID)) {
            resourceLocation = TEXTURE_ALEX;
            return resourceLocation;
        }
        resourceLocation = TEXTURE_STEVE;
        return resourceLocation;
    }

    public static String getSkinType(UUID playerUUID) {
        if (!DefaultPlayerSkin.isSlimSkin(playerUUID)) return "default";
        return "slim";
    }

    private static boolean isSlimSkin(UUID playerUUID) {
        if (playerUUID == null) {
            return false;
        }
        if ((playerUUID.hashCode() & 1) != 1) return false;
        return true;
    }
}

