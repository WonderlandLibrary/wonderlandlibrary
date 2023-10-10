package net.minecraft.client.renderer;

import net.minecraft.client.renderer.vertex.VertexBuffer;

public class VertexBufferUploader extends WorldVertexBufferUploader {
   private VertexBuffer vertexBuffer = null;

   public void func_181679_a(WorldRenderer var1) {
      var1.reset();
      this.vertexBuffer.func_181722_a(var1.getByteBuffer());
   }

   public void setVertexBuffer(VertexBuffer var1) {
      this.vertexBuffer = var1;
   }
}
