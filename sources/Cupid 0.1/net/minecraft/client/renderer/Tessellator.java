package net.minecraft.client.renderer;

public class Tessellator {
  private WorldRenderer worldRenderer;
  
  private WorldVertexBufferUploader vboUploader = new WorldVertexBufferUploader();
  
  private static final Tessellator instance = new Tessellator(2097152);
  
  public static Tessellator getInstance() {
    return instance;
  }
  
  public Tessellator(int bufferSize) {
    this.worldRenderer = new WorldRenderer(bufferSize);
  }
  
  public void draw() {
    this.worldRenderer.finishDrawing();
    this.vboUploader.func_181679_a(this.worldRenderer);
  }
  
  public WorldRenderer getWorldRenderer() {
    return this.worldRenderer;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\renderer\Tessellator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */