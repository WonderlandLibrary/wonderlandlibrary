package org.lwjgl.opengl;

import java.nio.Buffer;
import java.util.Arrays;

class BaseReferences {
   int elementArrayBuffer;
   int arrayBuffer;
   final Buffer[] glVertexAttribPointer_buffer;
   final Buffer[] glTexCoordPointer_buffer;
   int glClientActiveTexture;
   int vertexArrayObject;
   int pixelPackBuffer;
   int pixelUnpackBuffer;
   int indirectBuffer;

   BaseReferences(ContextCapabilities var1) {
      int var2;
      if (!var1.OpenGL20 && !var1.GL_ARB_vertex_shader) {
         var2 = 0;
      } else {
         var2 = GL11.glGetInteger(34921);
      }

      this.glVertexAttribPointer_buffer = new Buffer[var2];
      int var3;
      if (var1.OpenGL20) {
         var3 = GL11.glGetInteger(34930);
      } else if (!var1.OpenGL13 && !var1.GL_ARB_multitexture) {
         var3 = 1;
      } else {
         var3 = GL11.glGetInteger(34018);
      }

      this.glTexCoordPointer_buffer = new Buffer[var3];
   }

   void clear() {
      this.elementArrayBuffer = 0;
      this.arrayBuffer = 0;
      this.glClientActiveTexture = 0;
      Arrays.fill(this.glVertexAttribPointer_buffer, (Object)null);
      Arrays.fill(this.glTexCoordPointer_buffer, (Object)null);
      this.vertexArrayObject = 0;
      this.pixelPackBuffer = 0;
      this.pixelUnpackBuffer = 0;
      this.indirectBuffer = 0;
   }

   void copy(BaseReferences var1, int var2) {
      if ((var2 & 2) != 0) {
         this.elementArrayBuffer = var1.elementArrayBuffer;
         this.arrayBuffer = var1.arrayBuffer;
         this.glClientActiveTexture = var1.glClientActiveTexture;
         System.arraycopy(var1.glVertexAttribPointer_buffer, 0, this.glVertexAttribPointer_buffer, 0, this.glVertexAttribPointer_buffer.length);
         System.arraycopy(var1.glTexCoordPointer_buffer, 0, this.glTexCoordPointer_buffer, 0, this.glTexCoordPointer_buffer.length);
         this.vertexArrayObject = var1.vertexArrayObject;
         this.indirectBuffer = var1.indirectBuffer;
      }

      if ((var2 & 1) != 0) {
         this.pixelPackBuffer = var1.pixelPackBuffer;
         this.pixelUnpackBuffer = var1.pixelUnpackBuffer;
      }

   }
}
