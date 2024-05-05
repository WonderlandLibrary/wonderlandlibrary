package net.minecraft.client.renderer.chunk;

import com.google.common.util.concurrent.FutureCallback;
import java.util.List;
import java.util.concurrent.CancellationException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator;
import net.minecraft.client.renderer.chunk.ChunkRenderWorker;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.crash.CrashReport;

/*
 * Exception performing whole class analysis ignored.
 */
class ChunkRenderWorker.2
implements FutureCallback<List<Object>> {
    final /* synthetic */ ChunkCompileTaskGenerator val$generator;
    final /* synthetic */ CompiledChunk val$lvt_7_1_;

    ChunkRenderWorker.2(ChunkCompileTaskGenerator chunkCompileTaskGenerator, CompiledChunk compiledChunk) {
        this.val$generator = chunkCompileTaskGenerator;
        this.val$lvt_7_1_ = compiledChunk;
    }

    public void onSuccess(List<Object> p_onSuccess_1_) {
        block6: {
            ChunkRenderWorker.access$000((ChunkRenderWorker)ChunkRenderWorker.this, (ChunkCompileTaskGenerator)this.val$generator);
            this.val$generator.getLock().lock();
            try {
                if (this.val$generator.getStatus() == ChunkCompileTaskGenerator.Status.UPLOADING) {
                    this.val$generator.setStatus(ChunkCompileTaskGenerator.Status.DONE);
                    break block6;
                }
                if (!this.val$generator.isFinished()) {
                    ChunkRenderWorker.access$100().warn("Chunk render task was " + this.val$generator.getStatus() + " when I expected it to be uploading; aborting task");
                }
            }
            finally {
                this.val$generator.getLock().unlock();
            }
            return;
        }
        this.val$generator.getRenderChunk().setCompiledChunk(this.val$lvt_7_1_);
    }

    public void onFailure(Throwable p_onFailure_1_) {
        ChunkRenderWorker.access$000((ChunkRenderWorker)ChunkRenderWorker.this, (ChunkCompileTaskGenerator)this.val$generator);
        if (!(p_onFailure_1_ instanceof CancellationException) && !(p_onFailure_1_ instanceof InterruptedException)) {
            Minecraft.getMinecraft().crashed(CrashReport.makeCrashReport((Throwable)p_onFailure_1_, (String)"Rendering chunk"));
        }
    }
}
