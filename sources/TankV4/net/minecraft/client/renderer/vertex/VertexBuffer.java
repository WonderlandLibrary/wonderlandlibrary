package net.minecraft.client.renderer.vertex;

import java.nio.ByteBuffer;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

public class VertexBuffer {
   private int count;
   private int glBufferId;
   private final VertexFormat vertexFormat;

   public void func_181722_a(ByteBuffer var1) {
      this.bindBuffer();
      OpenGlHelper.glBufferData(OpenGlHelper.GL_ARRAY_BUFFER, var1, 35044);
      this.unbindBuffer();
      this.count = var1.limit() / this.vertexFormat.getNextOffset();
   }

   public VertexBuffer(VertexFormat var1) {
      this.vertexFormat = var1;
      this.glBufferId = OpenGlHelper.glGenBuffers();
   }

   public void drawArrays(int var1) {
      GL11.glDrawArrays(var1, 0, this.count);
   }

   public void unbindBuffer() {
      OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, 0);
   }

   public void bindBuffer() {
      OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, this.glBufferId);
   }

   public void deleteGlBuffers() {
      if (this.glBufferId >= 0) {
         OpenGlHelper.glDeleteBuffers(this.glBufferId);
         this.glBufferId = -1;
      }

   }
}
