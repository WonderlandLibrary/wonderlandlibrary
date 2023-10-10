package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator.Status;
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator.Type;
import net.minecraft.client.renderer.chunk.ChunkRenderWorker.1;
import net.minecraft.client.renderer.chunk.ChunkRenderWorker.2;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumWorldBlockLayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChunkRenderWorker implements Runnable {
   private static final Logger LOGGER = LogManager.getLogger();
   private final ChunkRenderDispatcher chunkRenderDispatcher;
   private final RegionRenderCacheBuilder regionRenderCacheBuilder;

   public ChunkRenderWorker(ChunkRenderDispatcher p_i46201_1_) {
      this(p_i46201_1_, (RegionRenderCacheBuilder)null);
   }

   public ChunkRenderWorker(ChunkRenderDispatcher chunkRenderDispatcherIn, RegionRenderCacheBuilder regionRenderCacheBuilderIn) {
      this.chunkRenderDispatcher = chunkRenderDispatcherIn;
      this.regionRenderCacheBuilder = regionRenderCacheBuilderIn;
   }

   public void run() {
      while(true) {
         try {
            this.processTask(this.chunkRenderDispatcher.getNextChunkUpdate());
         } catch (InterruptedException var3) {
            LOGGER.debug("Stopping due to interrupt");
            return;
         } catch (Throwable var4) {
            CrashReport crashreport = CrashReport.makeCrashReport(var4, "Batching chunks");
            Minecraft.getMinecraft().crashed(Minecraft.getMinecraft().addGraphicsAndWorldToCrashReport(crashreport));
            return;
         }
      }
   }

   // $FF: synthetic method
   static Logger access$100() {
      return LOGGER;
   }

   // $FF: synthetic method
   static void access$000(ChunkRenderWorker x0, ChunkCompileTaskGenerator x1) {
      x0.freeRenderBuilder(x1);
   }

   private RegionRenderCacheBuilder getRegionRenderCacheBuilder() throws InterruptedException {
      return this.regionRenderCacheBuilder != null?this.regionRenderCacheBuilder:this.chunkRenderDispatcher.allocateRenderBuilder();
   }

   private void freeRenderBuilder(ChunkCompileTaskGenerator taskGenerator) {
      if(this.regionRenderCacheBuilder == null) {
         this.chunkRenderDispatcher.freeRenderBuilder(taskGenerator.getRegionRenderCacheBuilder());
      }

   }

   protected void processTask(ChunkCompileTaskGenerator generator) throws InterruptedException {
      generator.getLock().lock();

      try {
         if(generator.getStatus() != Status.PENDING) {
            if(!generator.isFinished()) {
               LOGGER.warn("Chunk render task was " + generator.getStatus() + " when I expected it to be pending; ignoring task");
            }

            return;
         }

         generator.setStatus(Status.COMPILING);
      } finally {
         generator.getLock().unlock();
      }

      Entity lvt_2_1_ = Minecraft.getMinecraft().getRenderViewEntity();
      if(lvt_2_1_ == null) {
         generator.finish();
      } else {
         generator.setRegionRenderCacheBuilder(this.getRegionRenderCacheBuilder());
         float f = (float)lvt_2_1_.posX;
         float f1 = (float)lvt_2_1_.posY + lvt_2_1_.getEyeHeight();
         float f2 = (float)lvt_2_1_.posZ;
         Type chunkcompiletaskgenerator$type = generator.getType();
         if(chunkcompiletaskgenerator$type == Type.REBUILD_CHUNK) {
            generator.getRenderChunk().rebuildChunk(f, f1, f2, generator);
         } else if(chunkcompiletaskgenerator$type == Type.RESORT_TRANSPARENCY) {
            generator.getRenderChunk().resortTransparency(f, f1, f2, generator);
         }

         generator.getLock().lock();

         try {
            if(generator.getStatus() != Status.COMPILING) {
               if(!generator.isFinished()) {
                  LOGGER.warn("Chunk render task was " + generator.getStatus() + " when I expected it to be compiling; aborting task");
               }

               this.freeRenderBuilder(generator);
               return;
            }

            generator.setStatus(Status.UPLOADING);
         } finally {
            generator.getLock().unlock();
         }

         CompiledChunk lvt_7_1_ = generator.getCompiledChunk();
         ArrayList lvt_8_1_ = Lists.newArrayList();
         if(chunkcompiletaskgenerator$type == Type.REBUILD_CHUNK) {
            for(EnumWorldBlockLayer enumworldblocklayer : EnumWorldBlockLayer.values()) {
               if(lvt_7_1_.isLayerStarted(enumworldblocklayer)) {
                  lvt_8_1_.add(this.chunkRenderDispatcher.uploadChunk(enumworldblocklayer, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(enumworldblocklayer), generator.getRenderChunk(), lvt_7_1_));
               }
            }
         } else if(chunkcompiletaskgenerator$type == Type.RESORT_TRANSPARENCY) {
            lvt_8_1_.add(this.chunkRenderDispatcher.uploadChunk(EnumWorldBlockLayer.TRANSLUCENT, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT), generator.getRenderChunk(), lvt_7_1_));
         }

         ListenableFuture<List<Object>> listenablefuture = Futures.allAsList(lvt_8_1_);
         generator.addFinishRunnable(new 1(this, listenablefuture));
         Futures.addCallback(listenablefuture, new 2(this, generator, lvt_7_1_));
      }
   }
}
