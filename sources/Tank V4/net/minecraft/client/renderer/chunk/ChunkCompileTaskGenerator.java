package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;

public class ChunkCompileTaskGenerator {
   private RegionRenderCacheBuilder regionRenderCacheBuilder;
   private final ReentrantLock lock = new ReentrantLock();
   private boolean finished;
   private final List listFinishRunnables = Lists.newArrayList();
   private final RenderChunk renderChunk;
   private ChunkCompileTaskGenerator.Status status;
   private final ChunkCompileTaskGenerator.Type type;
   private CompiledChunk compiledChunk;

   public void addFinishRunnable(Runnable var1) {
      this.lock.lock();
      this.listFinishRunnables.add(var1);
      if (this.finished) {
         var1.run();
      }

      this.lock.unlock();
   }

   public void setRegionRenderCacheBuilder(RegionRenderCacheBuilder var1) {
      this.regionRenderCacheBuilder = var1;
   }

   public ChunkCompileTaskGenerator(RenderChunk var1, ChunkCompileTaskGenerator.Type var2) {
      this.status = ChunkCompileTaskGenerator.Status.PENDING;
      this.renderChunk = var1;
      this.type = var2;
   }

   public ChunkCompileTaskGenerator.Status getStatus() {
      return this.status;
   }

   public CompiledChunk getCompiledChunk() {
      return this.compiledChunk;
   }

   public void setStatus(ChunkCompileTaskGenerator.Status var1) {
      this.lock.lock();
      this.status = var1;
      this.lock.unlock();
   }

   public boolean isFinished() {
      return this.finished;
   }

   public RegionRenderCacheBuilder getRegionRenderCacheBuilder() {
      return this.regionRenderCacheBuilder;
   }

   public ReentrantLock getLock() {
      return this.lock;
   }

   public RenderChunk getRenderChunk() {
      return this.renderChunk;
   }

   public void setCompiledChunk(CompiledChunk var1) {
      this.compiledChunk = var1;
   }

   public ChunkCompileTaskGenerator.Type getType() {
      return this.type;
   }

   public void finish() {
      this.lock.lock();
      if (this.type == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK && this.status != ChunkCompileTaskGenerator.Status.DONE) {
         this.renderChunk.setNeedsUpdate(true);
      }

      this.finished = true;
      this.status = ChunkCompileTaskGenerator.Status.DONE;
      Iterator var2 = this.listFinishRunnables.iterator();

      while(var2.hasNext()) {
         Runnable var1 = (Runnable)var2.next();
         var1.run();
      }

      this.lock.unlock();
   }

   public static enum Status {
      PENDING,
      COMPILING;

      private static final ChunkCompileTaskGenerator.Status[] ENUM$VALUES = new ChunkCompileTaskGenerator.Status[]{PENDING, COMPILING, UPLOADING, DONE};
      DONE,
      UPLOADING;
   }

   public static enum Type {
      RESORT_TRANSPARENCY,
      REBUILD_CHUNK;

      private static final ChunkCompileTaskGenerator.Type[] ENUM$VALUES = new ChunkCompileTaskGenerator.Type[]{REBUILD_CHUNK, RESORT_TRANSPARENCY};
   }
}
