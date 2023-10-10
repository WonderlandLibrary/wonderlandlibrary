/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
    private static final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("Chunk Batcher %d").setDaemon(true).build();
    private final List<ChunkRenderWorker> listThreadedWorkers = Lists.newArrayList();
    private final BlockingQueue<ChunkCompileTaskGenerator> queueChunkUpdates = Queues.newArrayBlockingQueue(100);
    private final BlockingQueue<RegionRenderCacheBuilder> queueFreeRenderBuilders = Queues.newArrayBlockingQueue(5);
    private final WorldVertexBufferUploader worldVertexUploader = new WorldVertexBufferUploader();
    private final VertexBufferUploader vertexUploader = new VertexBufferUploader();
    private final Queue<ListenableFutureTask<?>> queueChunkUploads = Queues.newArrayDeque();
    private final ChunkRenderWorker renderWorker;

    public ChunkRenderDispatcher() {
        for (int i = 0; i < 2; ++i) {
            ChunkRenderWorker chunkrenderworker = new ChunkRenderWorker(this);
            Thread thread = threadFactory.newThread(chunkrenderworker);
            thread.start();
            this.listThreadedWorkers.add(chunkrenderworker);
        }
        int j = 0;
        while (true) {
            if (j >= 5) {
                this.renderWorker = new ChunkRenderWorker(this, new RegionRenderCacheBuilder());
                return;
            }
            this.queueFreeRenderBuilders.add(new RegionRenderCacheBuilder());
            ++j;
        }
    }

    public String getDebugInfo() {
        return String.format("pC: %03d, pU: %1d, aB: %1d", this.queueChunkUpdates.size(), this.queueChunkUploads.size(), this.queueFreeRenderBuilders.size());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean runChunkUploads(long p_178516_1_) {
        long i;
        boolean flag = false;
        do {
            boolean flag1 = false;
            Queue<ListenableFutureTask<?>> queue = this.queueChunkUploads;
            synchronized (queue) {
                if (!this.queueChunkUploads.isEmpty()) {
                    this.queueChunkUploads.poll().run();
                    flag1 = true;
                    flag = true;
                }
            }
            if (p_178516_1_ == 0L) return flag;
            if (flag1) continue;
            return flag;
        } while ((i = p_178516_1_ - System.nanoTime()) >= 0L);
        return flag;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean updateChunkLater(RenderChunk chunkRenderer) {
        chunkRenderer.getLockCompileTask().lock();
        try {
            final ChunkCompileTaskGenerator chunkcompiletaskgenerator = chunkRenderer.makeCompileTaskChunk();
            chunkcompiletaskgenerator.addFinishRunnable(new Runnable(){

                @Override
                public void run() {
                    ChunkRenderDispatcher.this.queueChunkUpdates.remove(chunkcompiletaskgenerator);
                }
            });
            boolean flag = this.queueChunkUpdates.offer(chunkcompiletaskgenerator);
            if (!flag) {
                chunkcompiletaskgenerator.finish();
            }
            boolean flag1 = flag;
            return flag1;
        }
        finally {
            chunkRenderer.getLockCompileTask().unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean updateChunkNow(RenderChunk chunkRenderer) {
        chunkRenderer.getLockCompileTask().lock();
        try {
            ChunkCompileTaskGenerator chunkcompiletaskgenerator = chunkRenderer.makeCompileTaskChunk();
            try {
                this.renderWorker.processTask(chunkcompiletaskgenerator);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
            boolean flag = true;
            return flag;
        }
        finally {
            chunkRenderer.getLockCompileTask().unlock();
        }
    }

    public void stopChunkUpdates() {
        this.clearChunkUpdates();
        while (this.runChunkUploads(0L)) {
        }
        ArrayList<RegionRenderCacheBuilder> list = Lists.newArrayList();
        while (true) {
            if (list.size() == 5) {
                this.queueFreeRenderBuilders.addAll(list);
                return;
            }
            try {
                list.add(this.allocateRenderBuilder());
            }
            catch (InterruptedException interruptedException) {
            }
        }
    }

    public void freeRenderBuilder(RegionRenderCacheBuilder p_178512_1_) {
        this.queueFreeRenderBuilders.add(p_178512_1_);
    }

    public RegionRenderCacheBuilder allocateRenderBuilder() throws InterruptedException {
        return this.queueFreeRenderBuilders.take();
    }

    public ChunkCompileTaskGenerator getNextChunkUpdate() throws InterruptedException {
        return this.queueChunkUpdates.take();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean updateTransparencyLater(RenderChunk chunkRenderer) {
        chunkRenderer.getLockCompileTask().lock();
        try {
            final ChunkCompileTaskGenerator chunkcompiletaskgenerator = chunkRenderer.makeCompileTaskTransparency();
            if (chunkcompiletaskgenerator == null) {
                boolean flag;
                boolean bl = flag = true;
                return bl;
            }
            chunkcompiletaskgenerator.addFinishRunnable(new Runnable(){

                @Override
                public void run() {
                    ChunkRenderDispatcher.this.queueChunkUpdates.remove(chunkcompiletaskgenerator);
                }
            });
            boolean flag = this.queueChunkUpdates.offer(chunkcompiletaskgenerator);
            return flag;
        }
        finally {
            chunkRenderer.getLockCompileTask().unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public ListenableFuture<Object> uploadChunk(final EnumWorldBlockLayer player, final WorldRenderer p_178503_2_, final RenderChunk chunkRenderer, final CompiledChunk compiledChunkIn) {
        if (Minecraft.getMinecraft().isCallingFromMinecraftThread()) {
            if (OpenGlHelper.useVbo()) {
                this.uploadVertexBuffer(p_178503_2_, chunkRenderer.getVertexBufferByLayer(player.ordinal()));
            } else {
                this.uploadDisplayList(p_178503_2_, ((ListedRenderChunk)chunkRenderer).getDisplayList(player, compiledChunkIn), chunkRenderer);
            }
            p_178503_2_.setTranslation(0.0, 0.0, 0.0);
            return Futures.immediateFuture(null);
        }
        ListenableFutureTask<Object> listenablefuturetask = ListenableFutureTask.create(new Runnable(){

            @Override
            public void run() {
                ChunkRenderDispatcher.this.uploadChunk(player, p_178503_2_, chunkRenderer, compiledChunkIn);
            }
        }, null);
        Queue<ListenableFutureTask<?>> queue = this.queueChunkUploads;
        synchronized (queue) {
            this.queueChunkUploads.add(listenablefuturetask);
            return listenablefuturetask;
        }
    }

    private void uploadDisplayList(WorldRenderer p_178510_1_, int p_178510_2_, RenderChunk chunkRenderer) {
        GL11.glNewList((int)p_178510_2_, (int)4864);
        GlStateManager.pushMatrix();
        chunkRenderer.multModelviewMatrix();
        this.worldVertexUploader.func_181679_a(p_178510_1_);
        GlStateManager.popMatrix();
        GL11.glEndList();
    }

    private void uploadVertexBuffer(WorldRenderer p_178506_1_, VertexBuffer vertexBufferIn) {
        this.vertexUploader.setVertexBuffer(vertexBufferIn);
        this.vertexUploader.func_181679_a(p_178506_1_);
    }

    public void clearChunkUpdates() {
        while (!this.queueChunkUpdates.isEmpty()) {
            ChunkCompileTaskGenerator chunkcompiletaskgenerator = (ChunkCompileTaskGenerator)this.queueChunkUpdates.poll();
            if (chunkcompiletaskgenerator == null) continue;
            chunkcompiletaskgenerator.finish();
        }
    }
}

