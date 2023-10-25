/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.util.concurrent.FutureCallback
 *  com.google.common.util.concurrent.Futures
 *  com.google.common.util.concurrent.ListenableFuture
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
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

    @Override
    public void run() {
        try {
            while (true) {
                this.processTask(this.chunkRenderDispatcher.getNextChunkUpdate());
            }
        }
        catch (InterruptedException interruptedException) {
            LOGGER.debug("Stopping due to interrupt");
            return;
        }
        catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Batching chunks");
            Minecraft.getMinecraft().crashed(Minecraft.getMinecraft().addGraphicsAndWorldToCrashReport(crashReport));
            return;
        }
    }

    private RegionRenderCacheBuilder getRegionRenderCacheBuilder() throws InterruptedException {
        return this.regionRenderCacheBuilder != null ? this.regionRenderCacheBuilder : this.chunkRenderDispatcher.allocateRenderBuilder();
    }

    protected void processTask(final ChunkCompileTaskGenerator chunkCompileTaskGenerator) throws InterruptedException {
        chunkCompileTaskGenerator.getLock().lock();
        if (chunkCompileTaskGenerator.getStatus() != ChunkCompileTaskGenerator.Status.PENDING) {
            if (!chunkCompileTaskGenerator.isFinished()) {
                LOGGER.warn("Chunk render task was " + (Object)((Object)chunkCompileTaskGenerator.getStatus()) + " when I expected it to be pending; ignoring task");
            }
            chunkCompileTaskGenerator.getLock().unlock();
            return;
        }
        chunkCompileTaskGenerator.setStatus(ChunkCompileTaskGenerator.Status.COMPILING);
        chunkCompileTaskGenerator.getLock().unlock();
        Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
        if (entity == null) {
            chunkCompileTaskGenerator.finish();
        } else {
            Object object;
            chunkCompileTaskGenerator.setRegionRenderCacheBuilder(this.getRegionRenderCacheBuilder());
            float f = (float)entity.posX;
            float f2 = (float)entity.posY + entity.getEyeHeight();
            float f3 = (float)entity.posZ;
            ChunkCompileTaskGenerator.Type type = chunkCompileTaskGenerator.getType();
            if (type == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK) {
                chunkCompileTaskGenerator.getRenderChunk().rebuildChunk(f, f2, f3, chunkCompileTaskGenerator);
            } else if (type == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY) {
                chunkCompileTaskGenerator.getRenderChunk().resortTransparency(f, f2, f3, chunkCompileTaskGenerator);
            }
            chunkCompileTaskGenerator.getLock().lock();
            if (chunkCompileTaskGenerator.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
                if (!chunkCompileTaskGenerator.isFinished()) {
                    LOGGER.warn("Chunk render task was " + (Object)((Object)chunkCompileTaskGenerator.getStatus()) + " when I expected it to be compiling; aborting task");
                }
                this.freeRenderBuilder(chunkCompileTaskGenerator);
                chunkCompileTaskGenerator.getLock().unlock();
                return;
            }
            chunkCompileTaskGenerator.setStatus(ChunkCompileTaskGenerator.Status.UPLOADING);
            chunkCompileTaskGenerator.getLock().unlock();
            final CompiledChunk compiledChunk = chunkCompileTaskGenerator.getCompiledChunk();
            ArrayList arrayList = Lists.newArrayList();
            if (type == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK) {
                EnumWorldBlockLayer[] enumWorldBlockLayerArray = EnumWorldBlockLayer.values();
                int n = enumWorldBlockLayerArray.length;
                int n2 = 0;
                while (n2 < n) {
                    object = enumWorldBlockLayerArray[n2];
                    if (compiledChunk.isLayerStarted((EnumWorldBlockLayer)((Object)object))) {
                        arrayList.add(this.chunkRenderDispatcher.uploadChunk((EnumWorldBlockLayer)((Object)object), chunkCompileTaskGenerator.getRegionRenderCacheBuilder().getWorldRendererByLayer((EnumWorldBlockLayer)((Object)object)), chunkCompileTaskGenerator.getRenderChunk(), compiledChunk));
                    }
                    ++n2;
                }
            } else if (type == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY) {
                arrayList.add(this.chunkRenderDispatcher.uploadChunk(EnumWorldBlockLayer.TRANSLUCENT, chunkCompileTaskGenerator.getRegionRenderCacheBuilder().getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT), chunkCompileTaskGenerator.getRenderChunk(), compiledChunk));
            }
            object = Futures.allAsList((Iterable)arrayList);
            chunkCompileTaskGenerator.addFinishRunnable(new Runnable((ListenableFuture)object){
                private final /* synthetic */ ListenableFuture val$listenablefuture;
                {
                    this.val$listenablefuture = listenableFuture;
                }

                @Override
                public void run() {
                    this.val$listenablefuture.cancel(false);
                }
            });
            Futures.addCallback((ListenableFuture)object, (FutureCallback)new FutureCallback<List<Object>>(){

                public void onFailure(Throwable throwable) {
                    ChunkRenderWorker.this.freeRenderBuilder(chunkCompileTaskGenerator);
                    if (!(throwable instanceof CancellationException) && !(throwable instanceof InterruptedException)) {
                        Minecraft.getMinecraft().crashed(CrashReport.makeCrashReport(throwable, "Rendering chunk"));
                    }
                }

                public void onSuccess(List<Object> list) {
                    ChunkRenderWorker.this.freeRenderBuilder(chunkCompileTaskGenerator);
                    chunkCompileTaskGenerator.getLock().lock();
                    if (chunkCompileTaskGenerator.getStatus() != ChunkCompileTaskGenerator.Status.UPLOADING) {
                        if (!chunkCompileTaskGenerator.isFinished()) {
                            LOGGER.warn("Chunk render task was " + (Object)((Object)chunkCompileTaskGenerator.getStatus()) + " when I expected it to be uploading; aborting task");
                        }
                        chunkCompileTaskGenerator.getLock().unlock();
                        return;
                    }
                    chunkCompileTaskGenerator.setStatus(ChunkCompileTaskGenerator.Status.DONE);
                    chunkCompileTaskGenerator.getLock().unlock();
                    chunkCompileTaskGenerator.getRenderChunk().setCompiledChunk(compiledChunk);
                }
            });
        }
    }

    public ChunkRenderWorker(ChunkRenderDispatcher chunkRenderDispatcher) {
        this(chunkRenderDispatcher, null);
    }

    public ChunkRenderWorker(ChunkRenderDispatcher chunkRenderDispatcher, RegionRenderCacheBuilder regionRenderCacheBuilder) {
        this.chunkRenderDispatcher = chunkRenderDispatcher;
        this.regionRenderCacheBuilder = regionRenderCacheBuilder;
    }

    private void freeRenderBuilder(ChunkCompileTaskGenerator chunkCompileTaskGenerator) {
        if (this.regionRenderCacheBuilder == null) {
            this.chunkRenderDispatcher.freeRenderBuilder(chunkCompileTaskGenerator.getRegionRenderCacheBuilder());
        }
    }
}

