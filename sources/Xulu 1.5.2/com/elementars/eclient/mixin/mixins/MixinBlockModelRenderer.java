// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import com.elementars.eclient.event.events.EventRenderBlock;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.world.IBlockAccess;
import net.minecraft.client.renderer.BlockModelRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ BlockModelRenderer.class })
public class MixinBlockModelRenderer
{
    @Inject(method = { "renderModel(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/renderer/BufferBuilder;ZJ)Z" }, at = { @At("HEAD") }, cancellable = true)
    public void renderModel(final IBlockAccess worldIn, final IBakedModel modelIn, final IBlockState stateIn, final BlockPos posIn, final BufferBuilder buffer, final boolean checkSides, final long rand, final CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        final EventRenderBlock eventRenderBlock = new EventRenderBlock(worldIn, modelIn, stateIn, posIn, buffer, checkSides, rand);
        eventRenderBlock.call();
        if (eventRenderBlock.isCancelled()) {
            callbackInfoReturnable.setReturnValue(eventRenderBlock.isRenderable());
        }
    }
}
