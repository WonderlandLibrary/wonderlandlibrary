package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;

public abstract class ChunkRenderContainer {
  private double viewEntityX;
  
  private double viewEntityY;
  
  private double viewEntityZ;
  
  protected List<RenderChunk> renderChunks = Lists.newArrayListWithCapacity(17424);
  
  protected boolean initialized;
  
  public void initialize(double viewEntityXIn, double viewEntityYIn, double viewEntityZIn) {
    this.initialized = true;
    this.renderChunks.clear();
    this.viewEntityX = viewEntityXIn;
    this.viewEntityY = viewEntityYIn;
    this.viewEntityZ = viewEntityZIn;
  }
  
  public void preRenderChunk(RenderChunk renderChunkIn) {
    BlockPos blockpos = renderChunkIn.getPosition();
    GlStateManager.translate((float)(blockpos.getX() - this.viewEntityX), (float)(blockpos.getY() - this.viewEntityY), (float)(blockpos.getZ() - this.viewEntityZ));
  }
  
  public void addRenderChunk(RenderChunk renderChunkIn, EnumWorldBlockLayer layer) {
    this.renderChunks.add(renderChunkIn);
  }
  
  public abstract void renderChunkLayer(EnumWorldBlockLayer paramEnumWorldBlockLayer);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\renderer\ChunkRenderContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */