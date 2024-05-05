package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumWorldBlockLayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChunkRenderWorker
implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger();
    private final ChunkRenderDispatcher chunkRenderDispatcher;
    private final RegionRenderCacheBuilder regionRenderCacheBuilder;

    public ChunkRenderWorker(ChunkRenderDispatcher p_i46201_1_) {
        this(p_i46201_1_, null);
    }

    public ChunkRenderWorker(ChunkRenderDispatcher chunkRenderDispatcherIn, RegionRenderCacheBuilder regionRenderCacheBuilderIn) {
        this.chunkRenderDispatcher = chunkRenderDispatcherIn;
        this.regionRenderCacheBuilder = regionRenderCacheBuilderIn;
    }

    public void run() {
        try {
            while (true) {
                this.processTask(this.chunkRenderDispatcher.getNextChunkUpdate());
            }
        }
        catch (InterruptedException var3) {
            LOGGER.debug("Stopping due to interrupt");
            return;
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport((Throwable)throwable, (String)"Batching chunks");
            Minecraft.getMinecraft().crashed(Minecraft.getMinecraft().addGraphicsAndWorldToCrashReport(crashreport));
            return;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void processTask(ChunkCompileTaskGenerator generator) throws InterruptedException {
        generator.getLock().lock();
        try {
            if (generator.getStatus() != ChunkCompileTaskGenerator.Status.PENDING) {
                if (!generator.isFinished()) {
                    LOGGER.warn("Chunk render task was " + generator.getStatus() + " when I expected it to be pending; ignoring task");
                }
                return;
            }
            generator.setStatus(ChunkCompileTaskGenerator.Status.COMPILING);
        }
        finally {
            generator.getLock().unlock();
        }
        Entity lvt_2_1_ = Minecraft.getMinecraft().getRenderViewEntity();
        if (lvt_2_1_ == null) {
            generator.finish();
        } else {
            generator.setRegionRenderCacheBuilder(this.getRegionRenderCacheBuilder());
            float f = (float)lvt_2_1_.posX;
            float f1 = (float)lvt_2_1_.posY + lvt_2_1_.getEyeHeight();
            float f2 = (float)lvt_2_1_.posZ;
            ChunkCompileTaskGenerator.Type chunkcompiletaskgenerator$type = generator.getType();
            if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK) {
                generator.getRenderChunk().rebuildChunk(f, f1, f2, generator);
            } else if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY) {
                generator.getRenderChunk().resortTransparency(f, f1, f2, generator);
            }
            generator.getLock().lock();
            try {
                if (generator.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
                    if (!generator.isFinished()) {
                        LOGGER.warn("Chunk render task was " + generator.getStatus() + " when I expected it to be compiling; aborting task");
                    }
                    this.freeRenderBuilder(generator);
                    return;
                }
                generator.setStatus(ChunkCompileTaskGenerator.Status.UPLOADING);
            }
            finally {
                generator.getLock().unlock();
            }
            CompiledChunk lvt_7_1_ = generator.getCompiledChunk();
            ArrayList lvt_8_1_ = Lists.newArrayList();
            if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK) {
                for (EnumWorldBlockLayer enumworldblocklayer : EnumWorldBlockLayer.values()) {
                    if (!lvt_7_1_.isLayerStarted(enumworldblocklayer)) continue;
                    lvt_8_1_.add((Object)this.chunkRenderDispatcher.uploadChunk(enumworldblocklayer, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(enumworldblocklayer), generator.getRenderChunk(), lvt_7_1_));
                }
            } else if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY) {
                lvt_8_1_.add((Object)this.chunkRenderDispatcher.uploadChunk(EnumWorldBlockLayer.TRANSLUCENT, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT), generator.getRenderChunk(), lvt_7_1_));
            }
            ListenableFuture listenablefuture = Futures.allAsList((Iterable)lvt_8_1_);
            generator.addFinishRunnable((Runnable)new /* Unavailable Anonymous Inner Class!! */);
            Futures.addCallback((ListenableFuture)listenablefuture, (FutureCallback)new /* Unavailable Anonymous Inner Class!! */);
        }
    }

    private RegionRenderCacheBuilder getRegionRenderCacheBuilder() throws InterruptedException {
        return this.regionRenderCacheBuilder != null ? this.regionRenderCacheBuilder : this.chunkRenderDispatcher.allocateRenderBuilder();
    }

    private void freeRenderBuilder(ChunkCompileTaskGenerator taskGenerator) {
        if (this.regionRenderCacheBuilder == null) {
            this.chunkRenderDispatcher.freeRenderBuilder(taskGenerator.getRegionRenderCacheBuilder());
        }
    }

    static /* synthetic */ void access$000(ChunkRenderWorker x0, ChunkCompileTaskGenerator x1) {
        x0.freeRenderBuilder(x1);
    }

    static /* synthetic */ Logger access$100() {
        return LOGGER;
    }
}
