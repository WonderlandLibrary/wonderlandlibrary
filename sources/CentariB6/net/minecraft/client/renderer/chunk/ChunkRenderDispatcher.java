package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
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
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher.1;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher.2;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher.3;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.EnumWorldBlockLayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class ChunkRenderDispatcher {
   private static final Logger logger = LogManager.getLogger();
   private static final ThreadFactory threadFactory = (new ThreadFactoryBuilder()).setNameFormat("Chunk Batcher %d").setDaemon(true).build();
   private final List listThreadedWorkers = Lists.newArrayList();
   private final BlockingQueue queueChunkUpdates = Queues.newArrayBlockingQueue(100);
   private final BlockingQueue queueFreeRenderBuilders = Queues.newArrayBlockingQueue(5);
   private final WorldVertexBufferUploader worldVertexUploader = new WorldVertexBufferUploader();
   private final VertexBufferUploader vertexUploader = new VertexBufferUploader();
   private final Queue queueChunkUploads = Queues.newArrayDeque();
   private final ChunkRenderWorker renderWorker;

   public ChunkRenderDispatcher() {
      for(int i = 0; i < 2; ++i) {
         ChunkRenderWorker chunkrenderworker = new ChunkRenderWorker(this);
         Thread thread = threadFactory.newThread(chunkrenderworker);
         thread.start();
         this.listThreadedWorkers.add(chunkrenderworker);
      }

      for(int j = 0; j < 5; ++j) {
         this.queueFreeRenderBuilders.add(new RegionRenderCacheBuilder());
      }

      this.renderWorker = new ChunkRenderWorker(this, new RegionRenderCacheBuilder());
   }

   // $FF: synthetic method
   static BlockingQueue access$000(ChunkRenderDispatcher x0) {
      return x0.queueChunkUpdates;
   }

   public void stopChunkUpdates() {
      this.clearChunkUpdates();

      while(this.runChunkUploads(0L)) {
         ;
      }

      List<RegionRenderCacheBuilder> list = Lists.newArrayList();

      while(((List)list).size() != 5) {
         try {
            list.add(this.allocateRenderBuilder());
         } catch (InterruptedException var3) {
            ;
         }
      }

      this.queueFreeRenderBuilders.addAll(list);
   }

   public String getDebugInfo() {
      return String.format("pC: %03d, pU: %1d, aB: %1d", new Object[]{Integer.valueOf(this.queueChunkUpdates.size()), Integer.valueOf(this.queueChunkUploads.size()), Integer.valueOf(this.queueFreeRenderBuilders.size())});
   }

   public boolean updateChunkNow(RenderChunk chunkRenderer) {
      chunkRenderer.getLockCompileTask().lock();

      boolean flag;
      try {
         ChunkCompileTaskGenerator chunkcompiletaskgenerator = chunkRenderer.makeCompileTaskChunk();

         try {
            this.renderWorker.processTask(chunkcompiletaskgenerator);
         } catch (InterruptedException var8) {
            ;
         }

         flag = true;
      } finally {
         chunkRenderer.getLockCompileTask().unlock();
      }

      return flag;
   }

   public boolean runChunkUploads(long p_178516_1_) {
      boolean flag = false;

      while(true) {
         boolean flag1 = false;
         synchronized(this.queueChunkUploads) {
            if(!this.queueChunkUploads.isEmpty()) {
               ((ListenableFutureTask)this.queueChunkUploads.poll()).run();
               flag1 = true;
               flag = true;
            }
         }

         if(p_178516_1_ == 0L || !flag1) {
            break;
         }

         long i = p_178516_1_ - System.nanoTime();
         if(i < 0L) {
            break;
         }
      }

      return flag;
   }

   public boolean updateChunkLater(RenderChunk chunkRenderer) {
      chunkRenderer.getLockCompileTask().lock();

      boolean flag1;
      try {
         ChunkCompileTaskGenerator chunkcompiletaskgenerator = chunkRenderer.makeCompileTaskChunk();
         chunkcompiletaskgenerator.addFinishRunnable(new 1(this, chunkcompiletaskgenerator));
         boolean flag = this.queueChunkUpdates.offer(chunkcompiletaskgenerator);
         if(!flag) {
            chunkcompiletaskgenerator.finish();
         }

         flag1 = flag;
      } finally {
         chunkRenderer.getLockCompileTask().unlock();
      }

      return flag1;
   }

   public void clearChunkUpdates() {
      while(!this.queueChunkUpdates.isEmpty()) {
         ChunkCompileTaskGenerator chunkcompiletaskgenerator = (ChunkCompileTaskGenerator)this.queueChunkUpdates.poll();
         if(chunkcompiletaskgenerator != null) {
            chunkcompiletaskgenerator.finish();
         }
      }

   }

   public boolean updateTransparencyLater(RenderChunk chunkRenderer) {
      chunkRenderer.getLockCompileTask().lock();

      boolean var4;
      try {
         ChunkCompileTaskGenerator chunkcompiletaskgenerator = chunkRenderer.makeCompileTaskTransparency();
         if(chunkcompiletaskgenerator != null) {
            chunkcompiletaskgenerator.addFinishRunnable(new 2(this, chunkcompiletaskgenerator));
            boolean flag = this.queueChunkUpdates.offer(chunkcompiletaskgenerator);
            return flag;
         }

         boolean flag = true;
         var4 = flag;
      } finally {
         chunkRenderer.getLockCompileTask().unlock();
      }

      return var4;
   }

   private void uploadDisplayList(WorldRenderer p_178510_1_, int p_178510_2_, RenderChunk chunkRenderer) {
      GL11.glNewList(p_178510_2_, 4864);
      GlStateManager.pushMatrix();
      chunkRenderer.multModelviewMatrix();
      this.worldVertexUploader.func_181679_a(p_178510_1_);
      GlStateManager.popMatrix();
      GL11.glEndList();
   }

   public void freeRenderBuilder(RegionRenderCacheBuilder p_178512_1_) {
      this.queueFreeRenderBuilders.add(p_178512_1_);
   }

   public ChunkCompileTaskGenerator getNextChunkUpdate() throws InterruptedException {
      return (ChunkCompileTaskGenerator)this.queueChunkUpdates.take();
   }

   public RegionRenderCacheBuilder allocateRenderBuilder() throws InterruptedException {
      return (RegionRenderCacheBuilder)this.queueFreeRenderBuilders.take();
   }

   private void uploadVertexBuffer(WorldRenderer p_178506_1_, VertexBuffer vertexBufferIn) {
      this.vertexUploader.setVertexBuffer(vertexBufferIn);
      this.vertexUploader.func_181679_a(p_178506_1_);
   }

   public ListenableFuture uploadChunk(EnumWorldBlockLayer player, WorldRenderer p_178503_2_, RenderChunk chunkRenderer, CompiledChunk compiledChunkIn) {
      if(Minecraft.getMinecraft().isCallingFromMinecraftThread()) {
         if(OpenGlHelper.useVbo()) {
            this.uploadVertexBuffer(p_178503_2_, chunkRenderer.getVertexBufferByLayer(player.ordinal()));
         } else {
            this.uploadDisplayList(p_178503_2_, ((ListedRenderChunk)chunkRenderer).getDisplayList(player, compiledChunkIn), chunkRenderer);
         }

         p_178503_2_.setTranslation(0.0D, 0.0D, 0.0D);
         return Futures.immediateFuture((Object)null);
      } else {
         ListenableFutureTask<Object> listenablefuturetask = ListenableFutureTask.create(new 3(this, player, p_178503_2_, chunkRenderer, compiledChunkIn), (Object)null);
         synchronized(this.queueChunkUploads) {
            this.queueChunkUploads.add(listenablefuturetask);
            return listenablefuturetask;
         }
      }
   }
}
