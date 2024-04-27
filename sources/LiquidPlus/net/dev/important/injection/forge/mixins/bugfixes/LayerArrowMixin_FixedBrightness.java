/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.layers.LayerArrow
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.client.renderer.entity.layers.LayerArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={LayerArrow.class})
public class LayerArrowMixin_FixedBrightness {
    @Redirect(method={"doRenderLayer"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/RenderHelper;disableStandardItemLighting()V"))
    private void patcher$removeDisable() {
    }

    @Redirect(method={"doRenderLayer"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/RenderHelper;enableStandardItemLighting()V"))
    private void patcher$removeEnable() {
    }
}

