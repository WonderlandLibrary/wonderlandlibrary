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
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.EnumWorldBlockLayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class ChunkRenderDispatcher {
   private static final Logger logger = LogManager.getLogger();
   private final VertexBufferUploader vertexUploader = new VertexBufferUploader();
   private final ChunkRenderWorker renderWorker;
   private final WorldVertexBufferUploader worldVertexUploader = new WorldVertexBufferUploader();
   private final List listThreadedWorkers = Lists.newArrayList();
   private static final ThreadFactory threadFactory = (new ThreadFactoryBuilder()).setNameFormat("Chunk Batcher %d").setDaemon(true).build();
   private final Queue queueChunkUploads = Queues.newArrayDeque();
   private final BlockingQueue queueFreeRenderBuilders = Queues.newArrayBlockingQueue(5);
   private final BlockingQueue queueChunkUpdates = Queues.newArrayBlockingQueue(100);

   public ListenableFuture uploadChunk(EnumWorldBlockLayer var1, WorldRenderer var2, RenderChunk var3, CompiledChunk var4) {
      if (Minecraft.getMinecraft().isCallingFromMinecraftThread()) {
         if (OpenGlHelper.useVbo()) {
            this.uploadVertexBuffer(var2, var3.getVertexBufferByLayer(var1.ordinal()));
         } else {
            this.uploadDisplayList(var2, ((ListedRenderChunk)var3).getDisplayList(var1, var4), var3);
         }

         var2.setTranslation(0.0D, 0.0D, 0.0D);
         return Futures.immediateFuture((Object)null);
      } else {
         ListenableFutureTask var5 = ListenableFutureTask.create(new Runnable(this, var1, var2, var3, var4) {
            private final CompiledChunk val$compiledChunkIn;
            private final WorldRenderer val$p_178503_2_;
            private final EnumWorldBlockLayer val$player;
            final ChunkRenderDispatcher this$0;
            private final RenderChunk val$chunkRenderer;

            public void run() {
               this.this$0.uploadChunk(this.val$player, this.val$p_178503_2_, this.val$chunkRenderer, this.val$compiledChunkIn);
            }

            {
               this.this$0 = var1;
               this.val$player = var2;
               this.val$p_178503_2_ = var3;
               this.val$chunkRenderer = var4;
               this.val$compiledChunkIn = var5;
            }
         }, (Object)null);
         Queue var6;
         synchronized(var6 = this.queueChunkUploads){}
         this.queueChunkUploads.add(var5);
         return var5;
      }
   }

   public ChunkCompileTaskGenerator getNextChunkUpdate() throws InterruptedException {
      return (ChunkCompileTaskGenerator)this.queueChunkUpdates.take();
   }

   public ChunkRenderDispatcher() {
      int var1;
      for(var1 = 0; var1 < 2; ++var1) {
         ChunkRenderWorker var2 = new ChunkRenderWorker(this);
         Thread var3 = threadFactory.newThread(var2);
         var3.start();
         this.listThreadedWorkers.add(var2);
      }

      for(var1 = 0; var1 < 5; ++var1) {
         this.queueFreeRenderBuilders.add(new RegionRenderCacheBuilder());
      }

      this.renderWorker = new ChunkRenderWorker(this, new RegionRenderCacheBuilder());
   }

   public String getDebugInfo() {
      return String.format("pC: %03d, pU: %1d, aB: %1d", this.queueChunkUpdates.size(), this.queueChunkUploads.size(), this.queueFreeRenderBuilders.size());
   }

   public boolean updateChunkLater(RenderChunk var1) {
      var1.getLockCompileTask().lock();
      ChunkCompileTaskGenerator var3 = var1.makeCompileTaskChunk();
      var3.addFinishRunnable(new Runnable(this, var3) {
         private final ChunkCompileTaskGenerator val$chunkcompiletaskgenerator;
         final ChunkRenderDispatcher this$0;

         {
            this.this$0 = var1;
            this.val$chunkcompiletaskgenerator = var2;
         }

         public void run() {
            ChunkRenderDispatcher.access$0(this.this$0).remove(this.val$chunkcompiletaskgenerator);
         }
      });
      boolean var4 = this.queueChunkUpdates.offer(var3);
      if (!var4) {
         var3.finish();
      }

      var1.getLockCompileTask().unlock();
      return var4;
   }

   public boolean updateTransparencyLater(RenderChunk var1) {
      var1.getLockCompileTask().lock();
      ChunkCompileTaskGenerator var3 = var1.makeCompileTaskTransparency();
      boolean var2;
      if (var3 == null) {
         var2 = true;
         var1.getLockCompileTask().unlock();
         return var2;
      } else {
         var3.addFinishRunnable(new Runnable(this, var3) {
            private final ChunkCompileTaskGenerator val$chunkcompiletaskgenerator;
            final ChunkRenderDispatcher this$0;

            {
               this.this$0 = var1;
               this.val$chunkcompiletaskgenerator = var2;
            }

            public void run() {
               ChunkRenderDispatcher.access$0(this.this$0).remove(this.val$chunkcompiletaskgenerator);
            }
         });
         var2 = this.queueChunkUpdates.offer(var3);
         var1.getLockCompileTask().unlock();
         return var2;
      }
   }

   public void stopChunkUpdates() {
      this.clearChunkUpdates();

      while(0L != false) {
      }

      ArrayList var1 = Lists.newArrayList();

      while(var1.size() != 5) {
         try {
            var1.add(this.allocateRenderBuilder());
         } catch (InterruptedException var4) {
         }
      }

      this.queueFreeRenderBuilders.addAll(var1);
   }

   public void clearChunkUpdates() {
      while(!this.queueChunkUpdates.isEmpty()) {
         ChunkCompileTaskGenerator var1 = (ChunkCompileTaskGenerator)this.queueChunkUpdates.poll();
         if (var1 != null) {
            var1.finish();
         }
      }

   }

   private void uploadDisplayList(WorldRenderer var1, int var2, RenderChunk var3) {
      GL11.glNewList(var2, 4864);
      GlStateManager.pushMatrix();
      var3.multModelviewMatrix();
      this.worldVertexUploader.func_181679_a(var1);
      GlStateManager.popMatrix();
      GL11.glEndList();
   }

   public void freeRenderBuilder(RegionRenderCacheBuilder var1) {
      this.queueFreeRenderBuilders.add(var1);
   }

   public boolean updateChunkNow(RenderChunk var1) {
      var1.getLockCompileTask().lock();
      ChunkCompileTaskGenerator var3 = var1.makeCompileTaskChunk();

      try {
         this.renderWorker.processTask(var3);
      } catch (InterruptedException var7) {
      }

      boolean var2 = true;
      var1.getLockCompileTask().unlock();
      return var2;
   }

   private void uploadVertexBuffer(WorldRenderer var1, VertexBuffer var2) {
      this.vertexUploader.setVertexBuffer(var2);
      this.vertexUploader.func_181679_a(var1);
   }

   static BlockingQueue access$0(ChunkRenderDispatcher var0) {
      return var0.queueChunkUpdates;
   }

   public RegionRenderCacheBuilder allocateRenderBuilder() throws InterruptedException {
      return (RegionRenderCacheBuilder)this.queueFreeRenderBuilders.take();
   }
}
