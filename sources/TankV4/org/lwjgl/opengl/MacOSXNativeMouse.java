package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;

final class MacOSXNativeMouse extends EventQueue {
   private static final int WHEEL_SCALE = 120;
   private static final int NUM_BUTTONS = 3;
   private ByteBuffer window_handle;
   private MacOSXDisplay display;
   private boolean grabbed;
   private float accum_dx;
   private float accum_dy;
   private int accum_dz;
   private float last_x;
   private float last_y;
   private boolean saved_control_state;
   private final ByteBuffer event = ByteBuffer.allocate(22);
   private IntBuffer delta_buffer = BufferUtils.createIntBuffer(2);
   private int skip_event;
   private final byte[] buttons = new byte[3];

   MacOSXNativeMouse(MacOSXDisplay var1, ByteBuffer var2) {
      super(22);
      this.display = var1;
      this.window_handle = var2;
   }

   private native void nSetCursorPosition(ByteBuffer var1, int var2, int var3);

   public static native void nGrabMouse(boolean var0);

   private native void nRegisterMouseListener(ByteBuffer var1);

   private native void nUnregisterMouseListener(ByteBuffer var1);

   private static native long nCreateCursor(int var0, int var1, int var2, int var3, int var4, IntBuffer var5, int var6, IntBuffer var7, int var8) throws LWJGLException;

   private static native void nDestroyCursor(long var0);

   private static native void nSetCursor(long var0) throws LWJGLException;

   public synchronized void register() {
      this.nRegisterMouseListener(this.window_handle);
   }

   public static long createCursor(int var0, int var1, int var2, int var3, int var4, IntBuffer var5, IntBuffer var6) throws LWJGLException {
      try {
         return nCreateCursor(var0, var1, var2, var3, var4, var5, var5.position(), var6, var6 != null ? var6.position() : -1);
      } catch (LWJGLException var8) {
         throw var8;
      }
   }

   public static void destroyCursor(long var0) {
      nDestroyCursor(var0);
   }

   public static void setCursor(long var0) throws LWJGLException {
      try {
         nSetCursor(var0);
      } catch (LWJGLException var3) {
         throw var3;
      }
   }

   public synchronized void setCursorPosition(int var1, int var2) {
      this.nSetCursorPosition(this.window_handle, var1, var2);
   }

   public synchronized void unregister() {
      this.nUnregisterMouseListener(this.window_handle);
   }

   public synchronized void setGrabbed(boolean var1) {
      this.grabbed = var1;
      nGrabMouse(var1);
      this.skip_event = 1;
      this.accum_dx = this.accum_dy = 0.0F;
   }

   public synchronized boolean isGrabbed() {
      return this.grabbed;
   }

   protected void resetCursorToCenter() {
      this.clearEvents();
      this.accum_dx = this.accum_dy = 0.0F;
      if (this.display != null) {
         this.last_x = (float)(this.display.getWidth() / 2);
         this.last_y = (float)(this.display.getHeight() / 2);
      }

   }

   private void putMouseEvent(byte var1, byte var2, int var3, long var4) {
      if (this.grabbed) {
         this.putMouseEventWithCoords(var1, var2, 0, 0, var3, var4);
      } else {
         this.putMouseEventWithCoords(var1, var2, (int)this.last_x, (int)this.last_y, var3, var4);
      }

   }

   protected void putMouseEventWithCoords(byte var1, byte var2, int var3, int var4, int var5, long var6) {
      this.event.clear();
      this.event.put(var1).put(var2).putInt(var3).putInt(var4).putInt(var5).putLong(var6);
      this.event.flip();
      this.putEvent(this.event);
   }

   public synchronized void poll(IntBuffer var1, ByteBuffer var2) {
      if (this.grabbed) {
         var1.put(0, (int)this.accum_dx);
         var1.put(1, (int)this.accum_dy);
      } else {
         var1.put(0, (int)this.last_x);
         var1.put(1, (int)this.last_y);
      }

      var1.put(2, this.accum_dz);
      this.accum_dx = this.accum_dy = (float)(this.accum_dz = 0);
      int var3 = var2.position();
      var2.put(this.buttons, 0, this.buttons.length);
      var2.position(var3);
   }

   private void setCursorPos(float var1, float var2, long var3) {
      if (!this.grabbed) {
         float var5 = var1 - this.last_x;
         float var6 = var2 - this.last_y;
         this.addDelta(var5, var6);
         this.last_x = var1;
         this.last_y = var2;
         this.putMouseEventWithCoords((byte)-1, (byte)0, (int)var1, (int)var2, 0, var3);
      }
   }

   protected void addDelta(float var1, float var2) {
      this.accum_dx += var1;
      this.accum_dy += -var2;
   }

   public synchronized void setButton(int var1, int var2, long var3) {
      this.buttons[var1] = (byte)var2;
      this.putMouseEvent((byte)var1, (byte)var2, 0, var3);
   }

   public synchronized void mouseMoved(float var1, float var2, float var3, float var4, float var5, long var6) {
      if (this.skip_event > 0) {
         --this.skip_event;
         if (this.skip_event == 0) {
            this.last_x = var1;
            this.last_y = var2;
         }

      } else {
         if (var5 != 0.0F) {
            if (var4 == 0.0F) {
               var4 = var3;
            }

            int var8 = (int)(var4 * 120.0F);
            this.accum_dz += var8;
            this.putMouseEvent((byte)-1, (byte)0, var8, var6);
         } else if (this.grabbed) {
            if (var3 != 0.0F || var4 != 0.0F) {
               this.putMouseEventWithCoords((byte)-1, (byte)0, (int)var3, (int)(-var4), 0, var6);
               this.addDelta(var3, var4);
            }
         } else {
            this.setCursorPos(var1, var2, var6);
         }

      }
   }
}
