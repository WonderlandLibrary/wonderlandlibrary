package net.minecraft.client.resources;

import com.google.common.cache.CacheLoader;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import java.util.Map;
import net.minecraft.client.Minecraft;

class SkinManager.1
extends CacheLoader<GameProfile, Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>> {
    SkinManager.1() {
    }

    public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> load(GameProfile p_load_1_) throws Exception {
        return Minecraft.getMinecraft().getSessionService().getTextures(p_load_1_, false);
    }
}
