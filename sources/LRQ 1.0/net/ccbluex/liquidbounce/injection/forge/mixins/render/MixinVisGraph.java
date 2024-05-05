/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.chunk.VisGraph
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.XRay;
import net.minecraft.client.renderer.chunk.VisGraph;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={VisGraph.class})
public class MixinVisGraph {
    @Inject(method={"setOpaqueCube"}, at={@At(value="HEAD")}, cancellable=true)
    private void func_178606_a(CallbackInfo callbackInfo) {
        if (LiquidBounce.moduleManager.getModule(XRay.class).getState()) {
            callbackInfo.cancel();
        }
    }
}

