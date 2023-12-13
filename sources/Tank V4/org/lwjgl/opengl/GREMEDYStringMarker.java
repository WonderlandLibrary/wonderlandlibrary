package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class GREMEDYStringMarker {
   private GREMEDYStringMarker() {
   }

   public static void glStringMarkerGREMEDY(ByteBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glStringMarkerGREMEDY;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglStringMarkerGREMEDY(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglStringMarkerGREMEDY(int var0, long var1, long var3);

   public static void glStringMarkerGREMEDY(CharSequence var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glStringMarkerGREMEDY;
      BufferChecks.checkFunctionAddress(var2);
      nglStringMarkerGREMEDY(var0.length(), APIUtil.getBuffer(var1, var0), var2);
   }
}
