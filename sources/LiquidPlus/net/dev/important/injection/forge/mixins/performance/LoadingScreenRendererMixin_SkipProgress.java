/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.LoadingScreenRenderer
 */
package net.dev.important.injection.forge.mixins.performance;

import net.dev.important.modules.module.modules.misc.Patcher;
import net.minecraft.client.LoadingScreenRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={LoadingScreenRenderer.class})
public class LoadingScreenRendererMixin_SkipProgress {
    @Inject(method={"setLoadingProgress"}, at={@At(value="HEAD")}, cancellable=true)
    private void patcher$skipProgress(int progress, CallbackInfo ci) {
        if (progress < 0 || ((Boolean)Patcher.optimizedWorldSwapping.get()).booleanValue()) {
            ci.cancel();
        }
    }
}

