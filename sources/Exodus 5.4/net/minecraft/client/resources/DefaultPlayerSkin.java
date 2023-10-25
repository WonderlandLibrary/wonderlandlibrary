/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.resources;

import java.util.UUID;
import net.minecraft.util.ResourceLocation;

public class DefaultPlayerSkin {
    private static final ResourceLocation TEXTURE_ALEX;
    private static final ResourceLocation TEXTURE_STEVE;

    public static ResourceLocation getDefaultSkinLegacy() {
        return TEXTURE_STEVE;
    }

    private static boolean isSlimSkin(UUID uUID) {
        return (uUID.hashCode() & 1) == 1;
    }

    public static String getSkinType(UUID uUID) {
        return DefaultPlayerSkin.isSlimSkin(uUID) ? "slim" : "default";
    }

    static {
        TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");
        TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");
    }

    public static ResourceLocation getDefaultSkin(UUID uUID) {
        return DefaultPlayerSkin.isSlimSkin(uUID) ? TEXTURE_ALEX : TEXTURE_STEVE;
    }
}

