package net.minecraft.client.network;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.util.ResourceLocation;

/*
 * Exception performing whole class analysis ignored.
 */
class NetworkPlayerInfo.1
implements SkinManager.SkinAvailableCallback {
    NetworkPlayerInfo.1() {
    }

    public void skinAvailable(MinecraftProfileTexture.Type p_180521_1_, ResourceLocation location, MinecraftProfileTexture profileTexture) {
        switch (NetworkPlayerInfo.2.$SwitchMap$com$mojang$authlib$minecraft$MinecraftProfileTexture$Type[p_180521_1_.ordinal()]) {
            case 1: {
                NetworkPlayerInfo.access$002((NetworkPlayerInfo)NetworkPlayerInfo.this, (ResourceLocation)location);
                NetworkPlayerInfo.access$102((NetworkPlayerInfo)NetworkPlayerInfo.this, (String)profileTexture.getMetadata("model"));
                if (NetworkPlayerInfo.access$100((NetworkPlayerInfo)NetworkPlayerInfo.this) != null) break;
                NetworkPlayerInfo.access$102((NetworkPlayerInfo)NetworkPlayerInfo.this, (String)"default");
                break;
            }
            case 2: {
                NetworkPlayerInfo.access$202((NetworkPlayerInfo)NetworkPlayerInfo.this, (ResourceLocation)location);
            }
        }
    }
}
