package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLUtil;

final class LinuxKeyboard {
   private static final int LockMapIndex = 1;
   private static final long NoSymbol = 0L;
   private static final long ShiftMask = 1L;
   private static final long LockMask = 2L;
   private static final int XLookupChars = 2;
   private static final int XLookupBoth = 4;
   private static final int KEYBOARD_BUFFER_SIZE = 50;
   private final long xim;
   private final long xic;
   private final int numlock_mask;
   private final int modeswitch_mask;
   private final int caps_lock_mask;
   private final int shift_lock_mask;
   private final ByteBuffer compose_status;
   private final byte[] key_down_buffer = new byte[256];
   private final EventQueue event_queue = new EventQueue(18);
   private final ByteBuffer tmp_event = ByteBuffer.allocate(18);
   private final int[] temp_translation_buffer = new int[50];
   private final ByteBuffer native_translation_buffer = BufferUtils.createByteBuffer(50);
   private final CharsetDecoder utf8_decoder = Charset.forName("UTF-8").newDecoder();
   private final CharBuffer char_buffer = CharBuffer.allocate(50);
   private boolean has_deferred_event;
   private int deferred_keycode;
   private int deferred_event_keycode;
   private long deferred_nanos;
   private byte deferred_key_state;

   LinuxKeyboard(long var1, long var3) {
      long var5 = getModifierMapping(var1);
      int var7 = 0;
      int var8 = 0;
      int var9 = 0;
      int var10 = 0;
      if (var5 != 0L) {
         int var11 = getMaxKeyPerMod(var5);

         for(int var12 = 0; var12 < 8; ++var12) {
            for(int var13 = 0; var13 < var11; ++var13) {
               int var14 = lookupModifierMap(var5, var12 * var11 + var13);
               int var15 = (int)keycodeToKeySym(var1, var14);
               int var16 = 1 << var12;
               switch(var15) {
               case 65406:
                  var8 |= var16;
                  break;
               case 65407:
                  var7 |= var16;
                  break;
               case 65509:
                  if (var12 == 1) {
                     var9 = var16;
                     var10 = 0;
                  }
                  break;
               case 65510:
                  if (var12 == 1 && var9 == 0) {
                     var10 = var16;
                  }
               }
            }
         }

         freeModifierMapping(var5);
      }

      this.numlock_mask = var7;
      this.modeswitch_mask = var8;
      this.caps_lock_mask = var9;
      this.shift_lock_mask = var10;
      setDetectableKeyRepeat(var1, true);
      this.xim = openIM(var1);
      if (this.xim != 0L) {
         this.xic = createIC(this.xim, var3);
         if (this.xic != 0L) {
            setupIMEventMask(var1, var3, this.xic);
         } else {
            this.destroy(var1);
         }
      } else {
         this.xic = 0L;
      }

      this.compose_status = allocateComposeStatus();
   }

   private static native long getModifierMapping(long var0);

   private static native void freeModifierMapping(long var0);

   private static native int getMaxKeyPerMod(long var0);

   private static native int lookupModifierMap(long var0, int var2);

   private static native long keycodeToKeySym(long var0, int var2);

   private static native long openIM(long var0);

   private static native long createIC(long var0, long var2);

   private static native void setupIMEventMask(long var0, long var2, long var4);

   private static native ByteBuffer allocateComposeStatus();

   private static void setDetectableKeyRepeat(long var0, boolean var2) {
      boolean var3 = nSetDetectableKeyRepeat(var0, var2);
      if (!var3) {
         LWJGLUtil.log("Failed to set detectable key repeat to " + var2);
      }

   }

   private static native boolean nSetDetectableKeyRepeat(long var0, boolean var2);

   public void destroy(long var1) {
      if (this.xic != 0L) {
         destroyIC(this.xic);
      }

      if (this.xim != 0L) {
         closeIM(this.xim);
      }

      setDetectableKeyRepeat(var1, false);
   }

   private static native void destroyIC(long var0);

   private static native void closeIM(long var0);

   public void read(ByteBuffer var1) {
      this.flushDeferredEvent();
      this.event_queue.copyEvents(var1);
   }

   public void poll(ByteBuffer var1) {
      this.flushDeferredEvent();
      int var2 = var1.position();
      var1.put(this.key_down_buffer);
      var1.position(var2);
   }

   private void putKeyboardEvent(int var1, byte var2, int var3, long var4, boolean var6) {
      this.tmp_event.clear();
      this.tmp_event.putInt(var1).put(var2).putInt(var3).putLong(var4).put((byte)(var6 ? 1 : 0));
      this.tmp_event.flip();
      this.event_queue.putEvent(this.tmp_event);
   }

   private int lookupStringISO88591(long var1, int[] var3) {
      int var5 = lookupString(var1, this.native_translation_buffer, this.compose_status);

      for(int var4 = 0; var4 < var5; ++var4) {
         var3[var4] = this.native_translation_buffer.get(var4) & 255;
      }

      return var5;
   }

   private static native int lookupString(long var0, ByteBuffer var2, ByteBuffer var3);

   private int lookupStringUnicode(long var1, int[] var3) {
      int var4 = utf8LookupString(this.xic, var1, this.native_translation_buffer, this.native_translation_buffer.position(), this.native_translation_buffer.remaining());
      if (var4 != 2 && var4 != 4) {
         return 0;
      } else {
         this.native_translation_buffer.flip();
         this.utf8_decoder.decode(this.native_translation_buffer, this.char_buffer, true);
         this.native_translation_buffer.compact();
         this.char_buffer.flip();

         int var5;
         for(var5 = 0; this.char_buffer.hasRemaining() && var5 < var3.length; var3[var5++] = this.char_buffer.get()) {
         }

         this.char_buffer.compact();
         return var5;
      }
   }

   private static native int utf8LookupString(long var0, long var2, ByteBuffer var4, int var5, int var6);

   private int lookupString(long var1, int[] var3) {
      return this.xic != 0L ? this.lookupStringUnicode(var1, var3) : this.lookupStringISO88591(var1, var3);
   }

   private void translateEvent(long var1, int var3, byte var4, long var5, boolean var7) {
      int var8 = this.lookupString(var1, this.temp_translation_buffer);
      if (var8 > 0) {
         int var10 = this.temp_translation_buffer[0];
         this.putKeyboardEvent(var3, var4, var10, var5, var7);

         for(int var9 = 1; var9 < var8; ++var9) {
            var10 = this.temp_translation_buffer[var9];
            this.putKeyboardEvent(0, (byte)0, var10, var5, var7);
         }
      } else {
         this.putKeyboardEvent(var3, var4, 0, var5, var7);
      }

   }

   private static long getKeySym(long var0, int var2, int var3) {
      long var4 = lookupKeysym(var0, var2 * 2 + var3);
      if (var4 != false && var3 == 1) {
         var4 = lookupKeysym(var0, var2 * 2 + 0);
      }

      if (var4 != false && var2 == 1) {
         var4 = getKeySym(var0, 0, var3);
      }

      return var4;
   }

   private static native long lookupKeysym(long var0, int var2);

   private static native long toUpper(long var0);

   private long mapEventToKeySym(long var1, int var3) {
      byte var4;
      if ((var3 & this.modeswitch_mask) != 0) {
         var4 = 1;
      } else {
         var4 = 0;
      }

      long var5;
      if ((var3 & this.numlock_mask) != 0 && (var5 = getKeySym(var1, var4, 1)) <= 0) {
         return ((long)var3 & (1L | (long)this.shift_lock_mask)) != 0L ? getKeySym(var1, var4, 0) : var5;
      } else if (((long)var3 & 3L) == 0L) {
         return getKeySym(var1, var4, 0);
      } else if (((long)var3 & 1L) == 0L) {
         var5 = getKeySym(var1, var4, 0);
         if ((var3 & this.caps_lock_mask) != 0) {
            var5 = toUpper(var5);
         }

         return var5;
      } else {
         var5 = getKeySym(var1, var4, 1);
         if ((var3 & this.caps_lock_mask) != 0) {
            var5 = toUpper(var5);
         }

         return var5;
      }
   }

   private int getKeycode(long var1, int var3) {
      long var4 = this.mapEventToKeySym(var1, var3);
      int var6 = LinuxKeycodes.mapKeySymToLWJGLKeyCode(var4);
      if (var6 == 0) {
         var4 = lookupKeysym(var1, 0);
         var6 = LinuxKeycodes.mapKeySymToLWJGLKeyCode(var4);
      }

      return var6;
   }

   private byte getKeyState(int var1) {
      switch(var1) {
      case 2:
         return 1;
      case 3:
         return 0;
      default:
         throw new IllegalArgumentException("Unknown event_type: " + var1);
      }
   }

   private void handleKeyEvent(long var1, long var3, int var5, int var6, int var7) {
      int var8 = this.getKeycode(var1, var7);
      byte var9 = this.getKeyState(var5);
      boolean var10 = var9 == this.key_down_buffer[var8];
      this.key_down_buffer[var8] = var9;
      long var11 = var3 * 1000000L;
      if (var5 == 2) {
         if (this.has_deferred_event) {
            if (var11 == this.deferred_nanos && var6 == this.deferred_event_keycode) {
               this.has_deferred_event = false;
               var10 = true;
            } else {
               this.flushDeferredEvent();
            }
         }

         this.translateEvent(var1, var8, var9, var11, var10);
      } else {
         this.flushDeferredEvent();
         this.has_deferred_event = true;
         this.deferred_keycode = var8;
         this.deferred_event_keycode = var6;
         this.deferred_nanos = var11;
         this.deferred_key_state = var9;
      }

   }

   private void flushDeferredEvent() {
      if (this.has_deferred_event) {
         this.putKeyboardEvent(this.deferred_keycode, this.deferred_key_state, 0, this.deferred_nanos, false);
         this.has_deferred_event = false;
      }

   }

   public boolean filterEvent(LinuxEvent var1) {
      switch(var1.getType()) {
      case 2:
      case 3:
         this.handleKeyEvent(var1.getKeyAddress(), var1.getKeyTime(), var1.getKeyType(), var1.getKeyKeyCode(), var1.getKeyState());
         return true;
      default:
         return false;
      }
   }
}
