/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.renderer.BlockModelRenderer
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.block.model.IBakedModel
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.IBlockAccess
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.block;

import java.util.Objects;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.XRay;
import net.ccbluex.liquidbounce.injection.backend.BlockImplKt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={BlockModelRenderer.class})
public class MixinBlockModelRenderer {
    @Inject(method={"renderModelSmooth"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderModelSmooth(IBlockAccess iBlockAccess, IBakedModel iBakedModel, IBlockState iBlockState, BlockPos blockPos, BufferBuilder bufferBuilder, boolean bl, long l, CallbackInfoReturnable callbackInfoReturnable) {
        XRay xRay = (XRay)LiquidBounce.moduleManager.getModule(XRay.class);
        if (Objects.requireNonNull(xRay).getState() && !xRay.getXrayBlocks().contains(BlockImplKt.wrap(iBlockState.func_177230_c()))) {
            callbackInfoReturnable.setReturnValue((Object)false);
        }
    }

    @Inject(method={"renderModelFlat"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderModelStandard(IBlockAccess iBlockAccess, IBakedModel iBakedModel, IBlockState iBlockState, BlockPos blockPos, BufferBuilder bufferBuilder, boolean bl, long l, CallbackInfoReturnable callbackInfoReturnable) {
        XRay xRay = (XRay)LiquidBounce.moduleManager.getModule(XRay.class);
        if (Objects.requireNonNull(xRay).getState() && !xRay.getXrayBlocks().contains(BlockImplKt.wrap(iBlockState.func_177230_c()))) {
            callbackInfoReturnable.setReturnValue((Object)false);
        }
    }
}

