package net.minecraft.client.network;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;

static class NetworkPlayerInfo.2 {
    static final /* synthetic */ int[] $SwitchMap$com$mojang$authlib$minecraft$MinecraftProfileTexture$Type;

    static {
        $SwitchMap$com$mojang$authlib$minecraft$MinecraftProfileTexture$Type = new int[MinecraftProfileTexture.Type.values().length];
        try {
            NetworkPlayerInfo.2.$SwitchMap$com$mojang$authlib$minecraft$MinecraftProfileTexture$Type[MinecraftProfileTexture.Type.SKIN.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            NetworkPlayerInfo.2.$SwitchMap$com$mojang$authlib$minecraft$MinecraftProfileTexture$Type[MinecraftProfileTexture.Type.CAPE.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
