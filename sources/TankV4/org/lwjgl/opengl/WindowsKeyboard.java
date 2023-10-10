package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import org.lwjgl.LWJGLException;

final class WindowsKeyboard {
   private final byte[] key_down_buffer = new byte[256];
   private final byte[] virt_key_down_buffer = new byte[256];
   private final EventQueue event_queue = new EventQueue(18);
   private final ByteBuffer tmp_event = ByteBuffer.allocate(18);
   private boolean has_retained_event;
   private int retained_key_code;
   private byte retained_state;
   private int retained_char;
   private long retained_millis;
   private boolean retained_repeat;

   WindowsKeyboard() throws LWJGLException {
   }

   private static native boolean isWindowsNT();

   void poll(ByteBuffer var1) {
      if (this == true && this == true) {
      }

      int var2 = var1.position();
      var1.put(this.key_down_buffer);
      var1.position(var2);
   }

   private static native int MapVirtualKey(int var0, int var1);

   private static native int ToUnicode(int var0, int var1, ByteBuffer var2, CharBuffer var3, int var4, int var5);

   private static native int ToAscii(int var0, int var1, ByteBuffer var2, ByteBuffer var3, int var4);

   private static native int GetKeyboardState(ByteBuffer var0);

   private static native short GetKeyState(int var0);

   private static native short GetAsyncKeyState(int var0);

   private void putEvent(int var1, byte var2, int var3, long var4, boolean var6) {
      this.tmp_event.clear();
      this.tmp_event.putInt(var1).put(var2).putInt(var3).putLong(var4 * 1000000L).put((byte)(var6 ? 1 : 0));
      this.tmp_event.flip();
      this.event_queue.putEvent(this.tmp_event);
   }

   private static int translateExtended(int var0, int var1, boolean var2) {
      switch(var0) {
      case 16:
         return var1 == 54 ? 161 : 160;
      case 17:
         return var2 ? 163 : 162;
      case 18:
         return var2 ? 165 : 164;
      default:
         return var0;
      }
   }

   private void flushRetained() {
      if (this.has_retained_event) {
         this.has_retained_event = false;
         this.putEvent(this.retained_key_code, this.retained_state, this.retained_char, this.retained_millis, this.retained_repeat);
      }

   }

   void handleKey(int var1, int var2, boolean var3, byte var4, long var5, boolean var7) {
      var1 = translateExtended(var1, var2, var3);
      if (var7 || isKeyPressed(var4) != this.virt_key_down_buffer[var1]) {
         this.flushRetained();
         this.has_retained_event = true;
         int var8 = WindowsKeycodes.mapVirtualKeyToLWJGLCode(var1);
         if (var8 < this.key_down_buffer.length) {
            this.key_down_buffer[var8] = var4;
            this.virt_key_down_buffer[var1] = var4;
         }

         this.retained_key_code = var8;
         this.retained_state = var4;
         this.retained_millis = var5;
         this.retained_char = 0;
         this.retained_repeat = var7;
      }
   }

   void fireLostKeyEvents() {
      // $FF: Couldn't be decompiled
   }

   void handleChar(int var1, long var2, boolean var4) {
      if (this.has_retained_event && this.retained_char != 0) {
         this.flushRetained();
      }

      if (!this.has_retained_event) {
         this.putEvent(0, (byte)0, var1, var2, var4);
      } else {
         this.retained_char = var1;
      }

   }

   void read(ByteBuffer var1) {
      this.flushRetained();
      this.event_queue.copyEvents(var1);
   }
}
