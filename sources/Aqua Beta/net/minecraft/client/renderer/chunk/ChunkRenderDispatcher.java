package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
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
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class ChunkRenderDispatcher {
    private static final Logger logger = LogManager.getLogger();
    private static final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("Chunk Batcher %d").setDaemon(true).build();
    private final List<ChunkRenderWorker> listThreadedWorkers = Lists.newArrayList();
    private final BlockingQueue<ChunkCompileTaskGenerator> queueChunkUpdates = Queues.newArrayBlockingQueue((int)100);
    private final BlockingQueue<RegionRenderCacheBuilder> queueFreeRenderBuilders;
    private final WorldVertexBufferUploader worldVertexUploader = new WorldVertexBufferUploader();
    private final VertexBufferUploader vertexUploader = new VertexBufferUploader();
    private final Queue<ListenableFutureTask<?>> queueChunkUploads = Queues.newArrayDeque();
    private final ChunkRenderWorker renderWorker;
    private final int countRenderBuilders;
    private List<RegionRenderCacheBuilder> listPausedBuilders = new ArrayList();

    public ChunkRenderDispatcher() {
        this(-1);
    }

    public ChunkRenderDispatcher(int p_i4_1_) {
        int i = Math.max((int)1, (int)((int)((double)Runtime.getRuntime().maxMemory() * 0.3) / 0xA00000));
        int j = Math.max((int)1, (int)MathHelper.clamp_int((int)(Runtime.getRuntime().availableProcessors() - 2), (int)1, (int)(i / 5)));
        this.countRenderBuilders = p_i4_1_ < 0 ? MathHelper.clamp_int((int)(j * 8), (int)1, (int)i) : p_i4_1_;
        for (int k = 0; k < j; ++k) {
            ChunkRenderWorker chunkrenderworker = new ChunkRenderWorker(this);
            Thread thread = threadFactory.newThread((Runnable)chunkrenderworker);
            thread.start();
            this.listThreadedWorkers.add((Object)chunkrenderworker);
        }
        this.queueFreeRenderBuilders = Queues.newArrayBlockingQueue((int)this.countRenderBuilders);
        for (int l = 0; l < this.countRenderBuilders; ++l) {
            this.queueFreeRenderBuilders.add((Object)new RegionRenderCacheBuilder());
        }
        this.renderWorker = new ChunkRenderWorker(this, new RegionRenderCacheBuilder());
    }

    public String getDebugInfo() {
        return String.format((String)"pC: %03d, pU: %1d, aB: %1d", (Object[])new Object[]{this.queueChunkUpdates.size(), this.queueChunkUploads.size(), this.queueFreeRenderBuilders.size()});
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean runChunkUploads(long p_178516_1_) {
        long i;
        boolean flag1;
        boolean flag = false;
        do {
            flag1 = false;
            ListenableFutureTask listenablefuturetask = null;
            Queue<ListenableFutureTask<?>> queue = this.queueChunkUploads;
            synchronized (queue) {
                listenablefuturetask = (ListenableFutureTask)this.queueChunkUploads.poll();
            }
            if (listenablefuturetask == null) continue;
            listenablefuturetask.run();
            flag1 = true;
            flag = true;
        } while (p_178516_1_ != 0L && flag1 && (i = p_178516_1_ - System.nanoTime()) >= 0L);
        return flag;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean updateChunkLater(RenderChunk chunkRenderer) {
        boolean flag;
        chunkRenderer.getLockCompileTask().lock();
        try {
            ChunkCompileTaskGenerator chunkcompiletaskgenerator = chunkRenderer.makeCompileTaskChunk();
            chunkcompiletaskgenerator.addFinishRunnable((Runnable)new /* Unavailable Anonymous Inner Class!! */);
            boolean flag1 = this.queueChunkUpdates.offer((Object)chunkcompiletaskgenerator);
            if (!flag1) {
                chunkcompiletaskgenerator.finish();
            }
            flag = flag1;
        }
        finally {
            chunkRenderer.getLockCompileTask().unlock();
        }
        return flag;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean updateChunkNow(RenderChunk chunkRenderer) {
        boolean flag;
        chunkRenderer.getLockCompileTask().lock();
        try {
            ChunkCompileTaskGenerator chunkcompiletaskgenerator = chunkRenderer.makeCompileTaskChunk();
            try {
                this.renderWorker.processTask(chunkcompiletaskgenerator);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
            flag = true;
        }
        finally {
            chunkRenderer.getLockCompileTask().unlock();
        }
        return flag;
    }

    public void stopChunkUpdates() {
        this.clearChunkUpdates();
        while (this.runChunkUploads(0L)) {
        }
        ArrayList list = Lists.newArrayList();
        while (list.size() != this.countRenderBuilders) {
            try {
                list.add((Object)this.allocateRenderBuilder());
            }
            catch (InterruptedException interruptedException) {}
        }
        this.queueFreeRenderBuilders.addAll((Collection)list);
    }

    public void freeRenderBuilder(RegionRenderCacheBuilder p_178512_1_) {
        this.queueFreeRenderBuilders.add((Object)p_178512_1_);
    }

    public RegionRenderCacheBuilder allocateRenderBuilder() throws InterruptedException {
        return (RegionRenderCacheBuilder)this.queueFreeRenderBuilders.take();
    }

    public ChunkCompileTaskGenerator getNextChunkUpdate() throws InterruptedException {
        return (ChunkCompileTaskGenerator)this.queueChunkUpdates.take();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean updateTransparencyLater(RenderChunk chunkRenderer) {
        boolean flag1;
        chunkRenderer.getLockCompileTask().lock();
        try {
            boolean flag;
            ChunkCompileTaskGenerator chunkcompiletaskgenerator = chunkRenderer.makeCompileTaskTransparency();
            if (chunkcompiletaskgenerator != null) {
                boolean flag2;
                chunkcompiletaskgenerator.addFinishRunnable((Runnable)new /* Unavailable Anonymous Inner Class!! */);
                boolean bl = flag2 = this.queueChunkUpdates.offer((Object)chunkcompiletaskgenerator);
                return bl;
            }
            flag1 = flag = true;
        }
        finally {
            chunkRenderer.getLockCompileTask().unlock();
        }
        return flag1;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public ListenableFuture<Object> uploadChunk(EnumWorldBlockLayer player, WorldRenderer p_178503_2_, RenderChunk chunkRenderer, CompiledChunk compiledChunkIn) {
        if (Minecraft.getMinecraft().isCallingFromMinecraftThread()) {
            if (OpenGlHelper.useVbo()) {
                this.uploadVertexBuffer(p_178503_2_, chunkRenderer.getVertexBufferByLayer(player.ordinal()));
            } else {
                this.uploadDisplayList(p_178503_2_, ((ListedRenderChunk)chunkRenderer).getDisplayList(player, compiledChunkIn), chunkRenderer);
            }
            p_178503_2_.setTranslation(0.0, 0.0, 0.0);
            return Futures.immediateFuture(null);
        }
        ListenableFutureTask listenablefuturetask = ListenableFutureTask.create((Runnable)new /* Unavailable Anonymous Inner Class!! */, null);
        Queue<ListenableFutureTask<?>> queue = this.queueChunkUploads;
        synchronized (queue) {
            this.queueChunkUploads.add((Object)listenablefuturetask);
            return listenablefuturetask;
        }
    }

    private void uploadDisplayList(WorldRenderer p_178510_1_, int p_178510_2_, RenderChunk chunkRenderer) {
        GL11.glNewList((int)p_178510_2_, (int)4864);
        GlStateManager.pushMatrix();
        chunkRenderer.multModelviewMatrix();
        this.worldVertexUploader.draw(p_178510_1_);
        GlStateManager.popMatrix();
        GL11.glEndList();
    }

    private void uploadVertexBuffer(WorldRenderer p_178506_1_, VertexBuffer vertexBufferIn) {
        this.vertexUploader.setVertexBuffer(vertexBufferIn);
        this.vertexUploader.draw(p_178506_1_);
    }

    public void clearChunkUpdates() {
        while (!this.queueChunkUpdates.isEmpty()) {
            ChunkCompileTaskGenerator chunkcompiletaskgenerator = (ChunkCompileTaskGenerator)this.queueChunkUpdates.poll();
            if (chunkcompiletaskgenerator == null) continue;
            chunkcompiletaskgenerator.finish();
        }
    }

    public boolean hasChunkUpdates() {
        return this.queueChunkUpdates.isEmpty() && this.queueChunkUploads.isEmpty();
    }

    public void pauseChunkUpdates() {
        while (this.listPausedBuilders.size() != this.countRenderBuilders) {
            try {
                this.runChunkUploads(Long.MAX_VALUE);
                RegionRenderCacheBuilder regionrendercachebuilder = (RegionRenderCacheBuilder)this.queueFreeRenderBuilders.poll(100L, TimeUnit.MILLISECONDS);
                if (regionrendercachebuilder == null) continue;
                this.listPausedBuilders.add((Object)regionrendercachebuilder);
            }
            catch (InterruptedException interruptedException) {}
        }
    }

    public void resumeChunkUpdates() {
        this.queueFreeRenderBuilders.addAll(this.listPausedBuilders);
        this.listPausedBuilders.clear();
    }

    static /* synthetic */ BlockingQueue access$000(ChunkRenderDispatcher x0) {
        return x0.queueChunkUpdates;
    }
}
