package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;

/*
 * Exception performing whole class analysis ignored.
 */
class ChunkRenderDispatcher.1
implements Runnable {
    final /* synthetic */ ChunkCompileTaskGenerator val$chunkcompiletaskgenerator;

    ChunkRenderDispatcher.1(ChunkCompileTaskGenerator chunkCompileTaskGenerator) {
        this.val$chunkcompiletaskgenerator = chunkCompileTaskGenerator;
    }

    public void run() {
        ChunkRenderDispatcher.access$000((ChunkRenderDispatcher)ChunkRenderDispatcher.this).remove((Object)this.val$chunkcompiletaskgenerator);
    }
}
