package net.minecraft.client.resources;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.util.ResourceLocation;

class SkinManager.2
implements IImageBuffer {
    final /* synthetic */ IImageBuffer val$iimagebuffer;
    final /* synthetic */ SkinManager.SkinAvailableCallback val$skinAvailableCallback;
    final /* synthetic */ MinecraftProfileTexture.Type val$p_152789_2_;
    final /* synthetic */ ResourceLocation val$resourcelocation;
    final /* synthetic */ MinecraftProfileTexture val$profileTexture;

    SkinManager.2(IImageBuffer iImageBuffer, SkinManager.SkinAvailableCallback skinAvailableCallback, MinecraftProfileTexture.Type type, ResourceLocation resourceLocation, MinecraftProfileTexture minecraftProfileTexture) {
        this.val$iimagebuffer = iImageBuffer;
        this.val$skinAvailableCallback = skinAvailableCallback;
        this.val$p_152789_2_ = type;
        this.val$resourcelocation = resourceLocation;
        this.val$profileTexture = minecraftProfileTexture;
    }

    public BufferedImage parseUserSkin(BufferedImage image) {
        if (this.val$iimagebuffer != null) {
            image = this.val$iimagebuffer.parseUserSkin(image);
        }
        return image;
    }

    public void skinAvailable() {
        if (this.val$iimagebuffer != null) {
            this.val$iimagebuffer.skinAvailable();
        }
        if (this.val$skinAvailableCallback != null) {
            this.val$skinAvailableCallback.skinAvailable(this.val$p_152789_2_, this.val$resourcelocation, this.val$profileTexture);
        }
    }
}
