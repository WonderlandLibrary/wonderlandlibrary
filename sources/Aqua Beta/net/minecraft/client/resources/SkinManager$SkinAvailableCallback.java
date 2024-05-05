package net.minecraft.client.resources;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.util.ResourceLocation;

public static interface SkinManager.SkinAvailableCallback {
    public void skinAvailable(MinecraftProfileTexture.Type var1, ResourceLocation var2, MinecraftProfileTexture var3);
}
