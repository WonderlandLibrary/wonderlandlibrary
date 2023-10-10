package net.minecraft.client.renderer;

public class Tessellator {
   private static final Tessellator instance = new Tessellator(2097152);
   private WorldVertexBufferUploader vboUploader = new WorldVertexBufferUploader();
   private WorldRenderer worldRenderer;

   public void draw() {
      this.worldRenderer.finishDrawing();
      this.vboUploader.func_181679_a(this.worldRenderer);
   }

   public WorldRenderer getWorldRenderer() {
      return this.worldRenderer;
   }

   public static Tessellator getInstance() {
      return instance;
   }

   public Tessellator(int var1) {
      this.worldRenderer = new WorldRenderer(var1);
   }
}
