package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;

final class WindowsMouse {
   private final long hwnd;
   private final int mouse_button_count;
   private final boolean has_wheel;
   private final EventQueue event_queue = new EventQueue(22);
   private final ByteBuffer mouse_event = ByteBuffer.allocate(22);
   private final Object blank_cursor;
   private boolean mouse_grabbed;
   private byte[] button_states;
   private int accum_dx;
   private int accum_dy;
   private int accum_dwheel;
   private int last_x;
   private int last_y;

   WindowsMouse(long var1) throws LWJGLException {
      this.hwnd = var1;
      this.mouse_button_count = Math.min(5, WindowsDisplay.getSystemMetrics(43));
      this.has_wheel = WindowsDisplay.getSystemMetrics(75) != 0;
      this.blank_cursor = this.createBlankCursor();
      this.button_states = new byte[this.mouse_button_count];
   }

   private Object createBlankCursor() throws LWJGLException {
      int var1 = WindowsDisplay.getSystemMetrics(13);
      int var2 = WindowsDisplay.getSystemMetrics(14);
      IntBuffer var3 = BufferUtils.createIntBuffer(var1 * var2);
      return WindowsDisplay.doCreateCursor(var1, var2, 0, 0, 1, var3, (IntBuffer)null);
   }

   public boolean isGrabbed() {
      return this.mouse_grabbed;
   }

   public boolean hasWheel() {
      return this.has_wheel;
   }

   public int getButtonCount() {
      return this.mouse_button_count;
   }

   public void poll(IntBuffer var1, ByteBuffer var2) {
      int var3;
      for(var3 = 0; var3 < var1.remaining(); ++var3) {
         var1.put(var1.position() + var3, 0);
      }

      var3 = this.mouse_button_count;
      var1.put(var1.position() + 2, this.accum_dwheel);
      if (var3 > this.button_states.length) {
         var3 = this.button_states.length;
      }

      for(int var4 = 0; var4 < var3; ++var4) {
         var2.put(var2.position() + var4, this.button_states[var4]);
      }

      if (this.isGrabbed()) {
         var1.put(var1.position() + 0, this.accum_dx);
         var1.put(var1.position() + 1, this.accum_dy);
      } else {
         var1.put(var1.position() + 0, this.last_x);
         var1.put(var1.position() + 1, this.last_y);
      }

      this.accum_dx = this.accum_dy = this.accum_dwheel = 0;
   }

   private void putMouseEventWithCoords(byte var1, byte var2, int var3, int var4, int var5, long var6) {
      this.mouse_event.clear();
      this.mouse_event.put(var1).put(var2).putInt(var3).putInt(var4).putInt(var5).putLong(var6);
      this.mouse_event.flip();
      this.event_queue.putEvent(this.mouse_event);
   }

   private void putMouseEvent(byte var1, byte var2, int var3, long var4) {
      if (this.mouse_grabbed) {
         this.putMouseEventWithCoords(var1, var2, 0, 0, var3, var4);
      } else {
         this.putMouseEventWithCoords(var1, var2, this.last_x, this.last_y, var3, var4);
      }

   }

   public void read(ByteBuffer var1) {
      this.event_queue.copyEvents(var1);
   }

   public Object getBlankCursor() {
      return this.blank_cursor;
   }

   public void grab(boolean var1, boolean var2) {
      if (var1) {
         if (!this.mouse_grabbed) {
            this.mouse_grabbed = true;
            if (var2) {
               try {
                  WindowsDisplay.setupCursorClipping(this.hwnd);
               } catch (LWJGLException var4) {
                  LWJGLUtil.log("Failed to setup cursor clipping: " + var4);
               }

               this.centerCursor();
            }
         }
      } else if (this.mouse_grabbed) {
         this.mouse_grabbed = false;
         WindowsDisplay.resetCursorClipping();
      }

      this.event_queue.clearEvents();
   }

   public void handleMouseScrolled(int var1, long var2) {
      this.accum_dwheel += var1;
      this.putMouseEvent((byte)-1, (byte)0, var1, var2 * 1000000L);
   }

   private void centerCursor() {
      WindowsDisplay.centerCursor(this.hwnd);
   }

   public void setPosition(int var1, int var2) {
      this.last_x = var1;
      this.last_y = var2;
   }

   public void destroy() {
      WindowsDisplay.doDestroyCursor(this.blank_cursor);
   }

   public void handleMouseMoved(int var1, int var2, long var3, boolean var5) {
      int var6 = var1 - this.last_x;
      int var7 = var2 - this.last_y;
      if (var6 != 0 || var7 != 0) {
         this.accum_dx += var6;
         this.accum_dy += var7;
         this.last_x = var1;
         this.last_y = var2;
         long var8 = var3 * 1000000L;
         if (this.mouse_grabbed) {
            this.putMouseEventWithCoords((byte)-1, (byte)0, var6, var7, 0, var8);
            if (var5) {
               this.centerCursor();
            }
         } else {
            this.putMouseEventWithCoords((byte)-1, (byte)0, var1, var2, 0, var8);
         }
      }

   }

   public void handleMouseButton(byte var1, byte var2, long var3) {
      this.putMouseEvent(var1, var2, 0, var3 * 1000000L);
      if (var1 < this.button_states.length) {
         this.button_states[var1] = (byte)(var2 != 0 ? 1 : 0);
      }

   }
}
