package org.lwjgl.opengl;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

final class MacOSXMouseEventQueue extends MouseEventQueue {
   private final IntBuffer delta_buffer = BufferUtils.createIntBuffer(2);
   private boolean skip_event;
   private static boolean is_grabbed;

   MacOSXMouseEventQueue(Component var1) {
      super(var1);
   }

   public void setGrabbed(boolean var1) {
      if (is_grabbed != var1) {
         super.setGrabbed(var1);
         this.warpCursor();
         grabMouse(var1);
      }

   }

   private static synchronized void grabMouse(boolean var0) {
      is_grabbed = var0;
      if (!var0) {
         nGrabMouse(var0);
      }

   }

   protected void resetCursorToCenter() {
      super.resetCursorToCenter();
      getMouseDeltas(this.delta_buffer);
   }

   protected void updateDeltas(long var1) {
      super.updateDeltas(var1);
      synchronized(this){}
      getMouseDeltas(this.delta_buffer);
      int var4 = this.delta_buffer.get(0);
      int var5 = -this.delta_buffer.get(1);
      if (this.skip_event) {
         this.skip_event = false;
         nGrabMouse(this.isGrabbed());
      } else {
         if (var4 != 0 || var5 != 0) {
            this.putMouseEventWithCoords((byte)-1, (byte)0, var4, var5, 0, var1);
            this.addDelta(var4, var5);
         }

      }
   }

   void warpCursor() {
      synchronized(this){}
      this.skip_event = this.isGrabbed();
      if (this.isGrabbed()) {
         Rectangle var1 = this.getComponent().getBounds();
         Point var2 = this.getComponent().getLocationOnScreen();
         int var3 = var2.x + var1.width / 2;
         int var4 = var2.y + var1.height / 2;
         nWarpCursor(var3, var4);
      }

   }

   private static native void getMouseDeltas(IntBuffer var0);

   private static native void nWarpCursor(int var0, int var1);

   static native void nGrabMouse(boolean var0);
}
