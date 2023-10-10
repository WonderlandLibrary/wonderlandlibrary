package org.lwjgl.opengles;

import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

public final class ContextAttribs {
   private int version;

   public ContextAttribs() {
      this(2);
   }

   public ContextAttribs(int var1) {
      if (3 < var1) {
         throw new IllegalArgumentException("Invalid OpenGL ES version specified: " + var1);
      } else {
         this.version = var1;
      }
   }

   private ContextAttribs(ContextAttribs var1) {
      this.version = var1.version;
   }

   public int getVersion() {
      return this.version;
   }

   public IntBuffer getAttribList() {
      byte var1 = 1;
      IntBuffer var2 = BufferUtils.createIntBuffer(var1 * 2 + 1);
      var2.put(12440).put(this.version);
      var2.put(12344);
      var2.rewind();
      return var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(32);
      var1.append("ContextAttribs:");
      var1.append(" Version=").append(this.version);
      return var1.toString();
   }
}
