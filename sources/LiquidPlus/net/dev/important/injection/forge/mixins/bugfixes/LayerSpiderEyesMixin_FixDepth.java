/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.entity.layers.LayerSpiderEyes
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerSpiderEyes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={LayerSpiderEyes.class})
public class LayerSpiderEyesMixin_FixDepth {
    @Inject(method={"doRenderLayer(Lnet/minecraft/entity/monster/EntitySpider;FFFFFFF)V"}, at={@At(value="TAIL")})
    private void patcher$fixDepth(CallbackInfo ci) {
        GlStateManager.func_179132_a((boolean)true);
    }
}

