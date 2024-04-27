/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.BlockFluidRenderer
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.client.renderer.BlockFluidRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value={BlockFluidRenderer.class})
public class BlockFluidRendererMixin_FixStitching {
    @ModifyConstant(method={"renderFluid"}, constant={@Constant(floatValue=0.001f)})
    private float patcher$fixFluidStitching(float original) {
        return 0.0f;
    }
}

