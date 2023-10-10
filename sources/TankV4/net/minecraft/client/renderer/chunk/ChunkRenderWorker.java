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
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumWorldBlockLayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChunkRenderWorker implements Runnable {
   private static final Logger LOGGER = LogManager.getLogger();
   private final RegionRenderCacheBuilder regionRenderCacheBuilder;
   private final ChunkRenderDispatcher chunkRenderDispatcher;

   public ChunkRenderWorker(ChunkRenderDispatcher var1) {
      this(var1, (RegionRenderCacheBuilder)null);
   }

   private RegionRenderCacheBuilder getRegionRenderCacheBuilder() throws InterruptedException {
      return this.regionRenderCacheBuilder != null ? this.regionRenderCacheBuilder : this.chunkRenderDispatcher.allocateRenderBuilder();
   }

   public void run() {
      try {
         this.processTask(this.chunkRenderDispatcher.getNextChunkUpdate());
      } catch (InterruptedException var4) {
         LOGGER.debug("Stopping due to interrupt");
      } catch (Throwable var5) {
         CrashReport var2 = CrashReport.makeCrashReport(var5, "Batching chunks");
         Minecraft.getMinecraft().crashed(Minecraft.getMinecraft().addGraphicsAndWorldToCrashReport(var2));
      }
   }

   static void access$0(ChunkRenderWorker var0, ChunkCompileTaskGenerator var1) {
      var0.freeRenderBuilder(var1);
   }

   static Logger access$1() {
      return LOGGER;
   }

   public ChunkRenderWorker(ChunkRenderDispatcher var1, RegionRenderCacheBuilder var2) {
      this.chunkRenderDispatcher = var1;
      this.regionRenderCacheBuilder = var2;
   }

   private void freeRenderBuilder(ChunkCompileTaskGenerator var1) {
      if (this.regionRenderCacheBuilder == null) {
         this.chunkRenderDispatcher.freeRenderBuilder(var1.getRegionRenderCacheBuilder());
      }

   }

   protected void processTask(ChunkCompileTaskGenerator var1) throws InterruptedException {
      var1.getLock().lock();
      if (var1.getStatus() != ChunkCompileTaskGenerator.Status.PENDING) {
         if (!var1.isFinished()) {
            LOGGER.warn("Chunk render task was " + var1.getStatus() + " when I expected it to be pending; ignoring task");
         }

         var1.getLock().unlock();
      } else {
         var1.setStatus(ChunkCompileTaskGenerator.Status.COMPILING);
         var1.getLock().unlock();
         Entity var2 = Minecraft.getMinecraft().getRenderViewEntity();
         if (var2 == null) {
            var1.finish();
         } else {
            var1.setRegionRenderCacheBuilder(this.getRegionRenderCacheBuilder());
            float var3 = (float)var2.posX;
            float var4 = (float)var2.posY + var2.getEyeHeight();
            float var5 = (float)var2.posZ;
            ChunkCompileTaskGenerator.Type var6 = var1.getType();
            if (var6 == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK) {
               var1.getRenderChunk().rebuildChunk(var3, var4, var5, var1);
            } else if (var6 == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY) {
               var1.getRenderChunk().resortTransparency(var3, var4, var5, var1);
            }

            var1.getLock().lock();
            if (var1.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
               if (!var1.isFinished()) {
                  LOGGER.warn("Chunk render task was " + var1.getStatus() + " when I expected it to be compiling; aborting task");
               }

               this.freeRenderBuilder(var1);
               var1.getLock().unlock();
               return;
            }

            var1.setStatus(ChunkCompileTaskGenerator.Status.UPLOADING);
            var1.getLock().unlock();
            CompiledChunk var7 = var1.getCompiledChunk();
            ArrayList var8 = Lists.newArrayList();
            if (var6 == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK) {
               EnumWorldBlockLayer[] var12;
               int var11 = (var12 = EnumWorldBlockLayer.values()).length;

               for(int var10 = 0; var10 < var11; ++var10) {
                  EnumWorldBlockLayer var9 = var12[var10];
                  if (var7.isLayerStarted(var9)) {
                     var8.add(this.chunkRenderDispatcher.uploadChunk(var9, var1.getRegionRenderCacheBuilder().getWorldRendererByLayer(var9), var1.getRenderChunk(), var7));
                  }
               }
            } else if (var6 == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY) {
               var8.add(this.chunkRenderDispatcher.uploadChunk(EnumWorldBlockLayer.TRANSLUCENT, var1.getRegionRenderCacheBuilder().getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT), var1.getRenderChunk(), var7));
            }

            ListenableFuture var13 = Futures.allAsList((Iterable)var8);
            var1.addFinishRunnable(new Runnable(this, var13) {
               final ChunkRenderWorker this$0;
               private final ListenableFuture val$listenablefuture;

               {
                  this.this$0 = var1;
                  this.val$listenablefuture = var2;
               }

               public void run() {
                  this.val$listenablefuture.cancel(false);
               }
            });
            Futures.addCallback(var13, new FutureCallback(this, var1, var7) {
               private final ChunkCompileTaskGenerator val$generator;
               final ChunkRenderWorker this$0;
               private final CompiledChunk val$lvt_7_1_;

               public void onSuccess(Object var1) {
                  this.onSuccess((List)var1);
               }

               public void onFailure(Throwable var1) {
                  ChunkRenderWorker.access$0(this.this$0, this.val$generator);
                  if (!(var1 instanceof CancellationException) && !(var1 instanceof InterruptedException)) {
                     Minecraft.getMinecraft().crashed(CrashReport.makeCrashReport(var1, "Rendering chunk"));
                  }

               }

               {
                  this.this$0 = var1;
                  this.val$generator = var2;
                  this.val$lvt_7_1_ = var3;
               }

               public void onSuccess(List var1) {
                  ChunkRenderWorker.access$0(this.this$0, this.val$generator);
                  this.val$generator.getLock().lock();
                  if (this.val$generator.getStatus() == ChunkCompileTaskGenerator.Status.UPLOADING) {
                     this.val$generator.setStatus(ChunkCompileTaskGenerator.Status.DONE);
                     this.val$generator.getLock().unlock();
                     this.val$generator.getRenderChunk().setCompiledChunk(this.val$lvt_7_1_);
                  } else {
                     if (!this.val$generator.isFinished()) {
                        ChunkRenderWorker.access$1().warn("Chunk render task was " + this.val$generator.getStatus() + " when I expected it to be uploading; aborting task");
                     }

                     this.val$generator.getLock().unlock();
                  }
               }
            });
         }

      }
   }
}
