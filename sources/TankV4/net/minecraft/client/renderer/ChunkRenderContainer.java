package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;

public abstract class ChunkRenderContainer {
   private double viewEntityZ;
   protected boolean initialized;
   private double viewEntityY;
   protected List renderChunks = Lists.newArrayListWithCapacity(17424);
   private double viewEntityX;

   public void preRenderChunk(RenderChunk var1) {
      BlockPos var2 = var1.getPosition();
      GlStateManager.translate((float)((double)var2.getX() - this.viewEntityX), (float)((double)var2.getY() - this.viewEntityY), (float)((double)var2.getZ() - this.viewEntityZ));
   }

   public abstract void renderChunkLayer(EnumWorldBlockLayer var1);

   public void initialize(double var1, double var3, double var5) {
      this.initialized = true;
      this.renderChunks.clear();
      this.viewEntityX = var1;
      this.viewEntityY = var3;
      this.viewEntityZ = var5;
   }

   public void addRenderChunk(RenderChunk var1, EnumWorldBlockLayer var2) {
      this.renderChunks.add(var1);
   }
}
