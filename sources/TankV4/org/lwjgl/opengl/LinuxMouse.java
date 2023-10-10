package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;

final class LinuxMouse {
   private static final int POINTER_WARP_BORDER = 10;
   private static final int WHEEL_SCALE = 120;
   private int button_count;
   private static final int Button1 = 1;
   private static final int Button2 = 2;
   private static final int Button3 = 3;
   private static final int Button4 = 4;
   private static final int Button5 = 5;
   private static final int Button6 = 6;
   private static final int Button7 = 7;
   private static final int Button8 = 8;
   private static final int Button9 = 9;
   private static final int ButtonPress = 4;
   private static final int ButtonRelease = 5;
   private final long display;
   private final long window;
   private final long input_window;
   private final long warp_atom;
   private final IntBuffer query_pointer_buffer = BufferUtils.createIntBuffer(4);
   private final ByteBuffer event_buffer = ByteBuffer.allocate(22);
   private int last_x;
   private int last_y;
   private int accum_dx;
   private int accum_dy;
   private int accum_dz;
   private byte[] buttons;
   private EventQueue event_queue;
   private long last_event_nanos;

   LinuxMouse(long var1, long var3, long var5) throws LWJGLException {
      this.display = var1;
      this.window = var3;
      this.input_window = var5;
      this.warp_atom = LinuxDisplay.nInternAtom(var1, "_LWJGL", false);
      this.button_count = nGetButtonCount(var1);
      this.buttons = new byte[this.button_count];
      this.reset(false, false);
   }

   private void reset(boolean var1, boolean var2) {
      this.event_queue = new EventQueue(this.event_buffer.capacity());
      this.accum_dx = this.accum_dy = 0;
      long var3 = nQueryPointer(this.display, this.window, this.query_pointer_buffer);
      int var5 = this.query_pointer_buffer.get(0);
      int var6 = this.query_pointer_buffer.get(1);
      int var7 = this.query_pointer_buffer.get(2);
      int var8 = this.query_pointer_buffer.get(3);
      this.last_x = var7;
      this.last_y = this.transformY(var8);
      this.doHandlePointerMotion(var1, var2, var3, var5, var6, var7, var8, this.last_event_nanos);
   }

   public void read(ByteBuffer var1) {
      this.event_queue.copyEvents(var1);
   }

   public void poll(boolean var1, IntBuffer var2, ByteBuffer var3) {
      if (var1) {
         var2.put(0, this.accum_dx);
         var2.put(1, this.accum_dy);
      } else {
         var2.put(0, this.last_x);
         var2.put(1, this.last_y);
      }

      var2.put(2, this.accum_dz);
      this.accum_dx = this.accum_dy = this.accum_dz = 0;

      for(int var4 = 0; var4 < this.buttons.length; ++var4) {
         var3.put(var4, this.buttons[var4]);
      }

   }

   private void putMouseEventWithCoords(byte var1, byte var2, int var3, int var4, int var5, long var6) {
      this.event_buffer.clear();
      this.event_buffer.put(var1).put(var2).putInt(var3).putInt(var4).putInt(var5).putLong(var6);
      this.event_buffer.flip();
      this.event_queue.putEvent(this.event_buffer);
      this.last_event_nanos = var6;
   }

   private void setCursorPos(boolean var1, int var2, int var3, long var4) {
      var3 = this.transformY(var3);
      int var6 = var2 - this.last_x;
      int var7 = var3 - this.last_y;
      if (var6 != 0 || var7 != 0) {
         this.accum_dx += var6;
         this.accum_dy += var7;
         this.last_x = var2;
         this.last_y = var3;
         if (var1) {
            this.putMouseEventWithCoords((byte)-1, (byte)0, var6, var7, 0, var4);
         } else {
            this.putMouseEventWithCoords((byte)-1, (byte)0, var2, var3, 0, var4);
         }
      }

   }

   private void doWarpPointer(int var1, int var2) {
      nSendWarpEvent(this.display, this.input_window, this.warp_atom, var1, var2);
      nWarpCursor(this.display, this.window, var1, var2);
   }

   private static native void nSendWarpEvent(long var0, long var2, long var4, int var6, int var7);

   private void doHandlePointerMotion(boolean var1, boolean var2, long var3, int var5, int var6, int var7, int var8, long var9) {
      this.setCursorPos(var1, var7, var8, var9);
      if (var2) {
         int var11 = nGetWindowHeight(this.display, var3);
         int var12 = nGetWindowWidth(this.display, var3);
         int var13 = nGetWindowHeight(this.display, this.window);
         int var14 = nGetWindowWidth(this.display, this.window);
         int var15 = var5 - var7;
         int var16 = var6 - var8;
         int var17 = var15 + var14;
         int var18 = var16 + var13;
         int var19 = Math.max(0, var15);
         int var20 = Math.max(0, var16);
         int var21 = Math.min(var12, var17);
         int var22 = Math.min(var11, var18);
         boolean var23 = var5 < var19 + 10 || var6 < var20 + 10 || var5 > var21 - 10 || var6 > var22 - 10;
         if (var23) {
            int var24 = (var21 - var19) / 2;
            int var25 = (var22 - var20) / 2;
            this.doWarpPointer(var24, var25);
         }

      }
   }

   public void changeGrabbed(boolean var1, boolean var2) {
      this.reset(var1, var2);
   }

   public int getButtonCount() {
      return this.buttons.length;
   }

   private int transformY(int var1) {
      return nGetWindowHeight(this.display, this.window) - 1 - var1;
   }

   private static native int nGetWindowHeight(long var0, long var2);

   private static native int nGetWindowWidth(long var0, long var2);

   private static native int nGetButtonCount(long var0);

   private static native long nQueryPointer(long var0, long var2, IntBuffer var4);

   public void setCursorPosition(int var1, int var2) {
      nWarpCursor(this.display, this.window, var1, this.transformY(var2));
   }

   private static native void nWarpCursor(long var0, long var2, int var4, int var5);

   private void handlePointerMotion(boolean var1, boolean var2, long var3, long var5, int var7, int var8, int var9, int var10) {
      this.doHandlePointerMotion(var1, var2, var5, var7, var8, var9, var10, var3 * 1000000L);
   }

   private void handleButton(boolean var1, int var2, byte var3, long var4) {
      byte var6;
      switch(var2) {
      case 1:
         var6 = 0;
         break;
      case 2:
         var6 = 2;
         break;
      case 3:
         var6 = 1;
         break;
      case 4:
      case 5:
      default:
         if (var2 <= 9 || var2 > this.button_count) {
            return;
         }

         var6 = (byte)(var2 - 1);
         break;
      case 6:
         var6 = 5;
         break;
      case 7:
         var6 = 6;
         break;
      case 8:
         var6 = 3;
         break;
      case 9:
         var6 = 4;
      }

      this.buttons[var6] = var3;
      this.putMouseEvent(var1, var6, var3, 0, var4);
   }

   private void putMouseEvent(boolean var1, byte var2, byte var3, int var4, long var5) {
      if (var1) {
         this.putMouseEventWithCoords(var2, var3, 0, 0, var4, var5);
      } else {
         this.putMouseEventWithCoords(var2, var3, this.last_x, this.last_y, var4, var5);
      }

   }

   private void handleButtonPress(boolean var1, byte var2, long var3) {
      boolean var5 = false;
      switch(var2) {
      case 4:
         byte var7 = 120;
         this.putMouseEvent(var1, (byte)-1, (byte)0, var7, var3);
         this.accum_dz += var7;
         break;
      case 5:
         byte var6 = -120;
         this.putMouseEvent(var1, (byte)-1, (byte)0, var6, var3);
         this.accum_dz += var6;
         break;
      default:
         this.handleButton(var1, var2, (byte)1, var3);
      }

   }

   private void handleButtonEvent(boolean var1, long var2, int var4, byte var5) {
      long var6 = var2 * 1000000L;
      switch(var4) {
      case 4:
         this.handleButtonPress(var1, var5, var6);
         break;
      case 5:
         this.handleButton(var1, var5, (byte)0, var6);
      }

   }

   private void resetCursor(int var1, int var2) {
      this.last_x = var1;
      this.last_y = this.transformY(var2);
   }

   private void handleWarpEvent(int var1, int var2) {
      this.resetCursor(var1, var2);
   }

   public boolean filterEvent(boolean var1, boolean var2, LinuxEvent var3) {
      switch(var3.getType()) {
      case 4:
      case 5:
         this.handleButtonEvent(var1, var3.getButtonTime(), var3.getButtonType(), (byte)var3.getButtonButton());
         return true;
      case 6:
         this.handlePointerMotion(var1, var2, var3.getButtonTime(), var3.getButtonRoot(), var3.getButtonXRoot(), var3.getButtonYRoot(), var3.getButtonX(), var3.getButtonY());
         return true;
      case 33:
         if (var3.getClientMessageType() == this.warp_atom) {
            this.handleWarpEvent(var3.getClientData(0), var3.getClientData(1));
            return true;
         }
      default:
         return false;
      }
   }
}
