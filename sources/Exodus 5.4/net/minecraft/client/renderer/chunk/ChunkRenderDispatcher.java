/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Queues
 *  com.google.common.util.concurrent.Futures
 *  com.google.common.util.concurrent.ListenableFuture
 *  com.google.common.util.concurrent.ListenableFutureTask
 *  com.google.common.util.concurrent.ThreadFactoryBuilder
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.client.renderer.VertexBufferUploader;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator;
import net.minecraft.client.renderer.chunk.ChunkRenderWorker;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.EnumWorldBlockLayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class ChunkRenderDispatcher {
    private static final Logger logger = LogManager.getLogger();
    private final BlockingQueue<ChunkCompileTaskGenerator> queueChunkUpdates;
    private final ChunkRenderWorker renderWorker;
    private final List<ChunkRenderWorker> listThreadedWorkers = Lists.newArrayList();
    private final VertexBufferUploader vertexUploader;
    private final WorldVertexBufferUploader worldVertexUploader;
    private static final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("Chunk Batcher %d").setDaemon(true).build();
    private final Queue<ListenableFutureTask<?>> queueChunkUploads;
    private final BlockingQueue<RegionRenderCacheBuilder> queueFreeRenderBuilders;

    public void stopChunkUpdates() {
        this.clearChunkUpdates();
        while (this.runChunkUploads(0L)) {
        }
        ArrayList arrayList = Lists.newArrayList();
        while (arrayList.size() != 5) {
            try {
                arrayList.add(this.allocateRenderBuilder());
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
        this.queueFreeRenderBuilders.addAll(arrayList);
    }

    public RegionRenderCacheBuilder allocateRenderBuilder() throws InterruptedException {
        return this.queueFreeRenderBuilders.take();
    }

    public boolean updateChunkNow(RenderChunk renderChunk) {
        renderChunk.getLockCompileTask().lock();
        ChunkCompileTaskGenerator chunkCompileTaskGenerator = renderChunk.makeCompileTaskChunk();
        try {
            this.renderWorker.processTask(chunkCompileTaskGenerator);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
        boolean bl = true;
        renderChunk.getLockCompileTask().unlock();
        return bl;
    }

    public ChunkRenderDispatcher() {
        this.queueChunkUpdates = Queues.newArrayBlockingQueue((int)100);
        this.queueFreeRenderBuilders = Queues.newArrayBlockingQueue((int)5);
        this.worldVertexUploader = new WorldVertexBufferUploader();
        this.vertexUploader = new VertexBufferUploader();
        this.queueChunkUploads = Queues.newArrayDeque();
        int n = 0;
        while (n < 2) {
            ChunkRenderWorker chunkRenderWorker = new ChunkRenderWorker(this);
            Thread thread = threadFactory.newThread(chunkRenderWorker);
            thread.start();
            this.listThreadedWorkers.add(chunkRenderWorker);
            ++n;
        }
        n = 0;
        while (n < 5) {
            this.queueFreeRenderBuilders.add(new RegionRenderCacheBuilder());
            ++n;
        }
        this.renderWorker = new ChunkRenderWorker(this, new RegionRenderCacheBuilder());
    }

    public void clearChunkUpdates() {
        while (!this.queueChunkUpdates.isEmpty()) {
            ChunkCompileTaskGenerator chunkCompileTaskGenerator = (ChunkCompileTaskGenerator)this.queueChunkUpdates.poll();
            if (chunkCompileTaskGenerator == null) continue;
            chunkCompileTaskGenerator.finish();
        }
    }

    public boolean updateTransparencyLater(RenderChunk renderChunk) {
        renderChunk.getLockCompileTask().lock();
        final ChunkCompileTaskGenerator chunkCompileTaskGenerator = renderChunk.makeCompileTaskTransparency();
        if (chunkCompileTaskGenerator == null) {
            boolean bl;
            boolean bl2 = bl = true;
            renderChunk.getLockCompileTask().unlock();
            return bl2;
        }
        chunkCompileTaskGenerator.addFinishRunnable(new Runnable(){

            @Override
            public void run() {
                ChunkRenderDispatcher.this.queueChunkUpdates.remove(chunkCompileTaskGenerator);
            }
        });
        boolean bl = this.queueChunkUpdates.offer(chunkCompileTaskGenerator);
        renderChunk.getLockCompileTask().unlock();
        return bl;
    }

    public ChunkCompileTaskGenerator getNextChunkUpdate() throws InterruptedException {
        return this.queueChunkUpdates.take();
    }

    private void uploadDisplayList(WorldRenderer worldRenderer, int n, RenderChunk renderChunk) {
        GL11.glNewList((int)n, (int)4864);
        GlStateManager.pushMatrix();
        renderChunk.multModelviewMatrix();
        this.worldVertexUploader.func_181679_a(worldRenderer);
        GlStateManager.popMatrix();
        GL11.glEndList();
    }

    public boolean updateChunkLater(RenderChunk renderChunk) {
        renderChunk.getLockCompileTask().lock();
        final ChunkCompileTaskGenerator chunkCompileTaskGenerator = renderChunk.makeCompileTaskChunk();
        chunkCompileTaskGenerator.addFinishRunnable(new Runnable(){

            @Override
            public void run() {
                ChunkRenderDispatcher.this.queueChunkUpdates.remove(chunkCompileTaskGenerator);
            }
        });
        boolean bl = this.queueChunkUpdates.offer(chunkCompileTaskGenerator);
        if (!bl) {
            chunkCompileTaskGenerator.finish();
        }
        boolean bl2 = bl;
        renderChunk.getLockCompileTask().unlock();
        return bl2;
    }

    public String getDebugInfo() {
        return String.format("pC: %03d, pU: %1d, aB: %1d", this.queueChunkUpdates.size(), this.queueChunkUploads.size(), this.queueFreeRenderBuilders.size());
    }

    public void freeRenderBuilder(RegionRenderCacheBuilder regionRenderCacheBuilder) {
        this.queueFreeRenderBuilders.add(regionRenderCacheBuilder);
    }

    public boolean runChunkUploads(long l) {
        long l2;
        boolean bl;
        boolean bl2 = false;
        do {
            bl = false;
            Queue<ListenableFutureTask<?>> queue = this.queueChunkUploads;
            synchronized (queue) {
                if (!this.queueChunkUploads.isEmpty()) {
                    this.queueChunkUploads.poll().run();
                    bl = true;
                    bl2 = true;
                }
            }
        } while (l != 0L && bl && (l2 = l - System.nanoTime()) >= 0L);
        return bl2;
    }

    public ListenableFuture<Object> uploadChunk(final EnumWorldBlockLayer enumWorldBlockLayer, final WorldRenderer worldRenderer, final RenderChunk renderChunk, final CompiledChunk compiledChunk) {
        if (Minecraft.getMinecraft().isCallingFromMinecraftThread()) {
            if (OpenGlHelper.useVbo()) {
                this.uploadVertexBuffer(worldRenderer, renderChunk.getVertexBufferByLayer(enumWorldBlockLayer.ordinal()));
            } else {
                this.uploadDisplayList(worldRenderer, ((ListedRenderChunk)renderChunk).getDisplayList(enumWorldBlockLayer, compiledChunk), renderChunk);
            }
            worldRenderer.setTranslation(0.0, 0.0, 0.0);
            return Futures.immediateFuture(null);
        }
        ListenableFutureTask listenableFutureTask = ListenableFutureTask.create((Runnable)new Runnable(){

            @Override
            public void run() {
                ChunkRenderDispatcher.this.uploadChunk(enumWorldBlockLayer, worldRenderer, renderChunk, compiledChunk);
            }
        }, null);
        Queue<ListenableFutureTask<?>> queue = this.queueChunkUploads;
        synchronized (queue) {
            this.queueChunkUploads.add(listenableFutureTask);
            return listenableFutureTask;
        }
    }

    private void uploadVertexBuffer(WorldRenderer worldRenderer, VertexBuffer vertexBuffer) {
        this.vertexUploader.setVertexBuffer(vertexBuffer);
        this.vertexUploader.func_181679_a(worldRenderer);
    }
}

