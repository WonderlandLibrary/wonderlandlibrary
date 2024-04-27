/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.texture.TextureUtil
 *  net.minecraft.client.resources.AbstractResourcePack
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.AbstractResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={AbstractResourcePack.class})
public abstract class AbstractResourcePackMixin_DownscaleImages {
    @Shadow
    protected abstract InputStream func_110591_a(String var1) throws IOException;

    @Inject(method={"getPackImage"}, at={@At(value="HEAD")}, cancellable=true)
    private void patcher$downscalePackImage(CallbackInfoReturnable<BufferedImage> cir) throws IOException {
        BufferedImage image = TextureUtil.func_177053_a((InputStream)this.func_110591_a("pack.png"));
        if (image == null) {
            cir.setReturnValue(null);
            return;
        }
        if (image.getWidth() <= 64 && image.getHeight() <= 64) {
            cir.setReturnValue(image);
            return;
        }
        BufferedImage downscaledIcon = new BufferedImage(64, 64, 2);
        Graphics graphics = downscaledIcon.getGraphics();
        graphics.drawImage(image, 0, 0, 64, 64, null);
        graphics.dispose();
        cir.setReturnValue(downscaledIcon);
    }
}

