package me.offeex.bloomware.mixin.world;

import me.offeex.bloomware.event.events.EventUpdate;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.chunk.BlockBufferBuilderStorage;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.render.chunk.ChunkOcclusionDataBuilder;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.Set;

@Mixin(ChunkBuilder.BuiltChunk.class)
abstract class MixinChunkBuilderBuiltChunk {

    @Mixin(ChunkBuilder.BuiltChunk.RebuildTask.class)
    static class RebuildTask {
        @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getRenderType()Lnet/minecraft/block/BlockRenderType;"), locals = LocalCapture.CAPTURE_FAILHARD)
        void update(float cameraX, float cameraY, float cameraZ, BlockBufferBuilderStorage blockBufferBuilderStorage, CallbackInfoReturnable<ChunkBuilder.BuiltChunk.RebuildTask.RenderData> cir, ChunkBuilder.BuiltChunk.RebuildTask.RenderData renderData, int i, BlockPos chunkOrigin, BlockPos chunkOriginEnd, ChunkOcclusionDataBuilder chunkOcclusionDataBuilder, ChunkRendererRegion chunkRendererRegion, MatrixStack matrixStack, Set<RenderLayer> set, Random random, BlockRenderManager blockRenderManager, Iterator<BlockPos> var15, BlockPos blockPos) {
            new EventUpdate.BlocksInChunk(chunkOrigin, blockPos).post();
        }
    }
}
