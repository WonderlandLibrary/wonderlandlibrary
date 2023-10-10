package org.lwjgl.opengl;

import java.awt.event.KeyEvent;
import java.nio.ByteBuffer;
import java.util.HashMap;

final class MacOSXNativeKeyboard extends EventQueue {
   private final byte[] key_states = new byte[256];
   private final ByteBuffer event = ByteBuffer.allocate(18);
   private ByteBuffer window_handle;
   private boolean has_deferred_event;
   private long deferred_nanos;
   private int deferred_key_code;
   private byte deferred_key_state;
   private int deferred_character;
   private HashMap nativeToLwjglMap = new HashMap();

   MacOSXNativeKeyboard(ByteBuffer var1) {
      super(18);
      this.initKeyboardMappings();
      this.window_handle = var1;
   }

   private native void nRegisterKeyListener(ByteBuffer var1);

   private native void nUnregisterKeyListener(ByteBuffer var1);

   private void initKeyboardMappings() {
      this.nativeToLwjglMap.put(Short.valueOf((short)29), 11);
      this.nativeToLwjglMap.put(Short.valueOf((short)18), 2);
      this.nativeToLwjglMap.put(Short.valueOf((short)19), 3);
      this.nativeToLwjglMap.put(Short.valueOf((short)20), 4);
      this.nativeToLwjglMap.put(Short.valueOf((short)21), 5);
      this.nativeToLwjglMap.put(Short.valueOf((short)23), 6);
      this.nativeToLwjglMap.put(Short.valueOf((short)22), 7);
      this.nativeToLwjglMap.put(Short.valueOf((short)26), 8);
      this.nativeToLwjglMap.put(Short.valueOf((short)28), 9);
      this.nativeToLwjglMap.put(Short.valueOf((short)25), 10);
      this.nativeToLwjglMap.put(Short.valueOf((short)0), 30);
      this.nativeToLwjglMap.put(Short.valueOf((short)11), 48);
      this.nativeToLwjglMap.put(Short.valueOf((short)8), 46);
      this.nativeToLwjglMap.put(Short.valueOf((short)2), 32);
      this.nativeToLwjglMap.put(Short.valueOf((short)14), 18);
      this.nativeToLwjglMap.put(Short.valueOf((short)3), 33);
      this.nativeToLwjglMap.put(Short.valueOf((short)5), 34);
      this.nativeToLwjglMap.put(Short.valueOf((short)4), 35);
      this.nativeToLwjglMap.put(Short.valueOf((short)34), 23);
      this.nativeToLwjglMap.put(Short.valueOf((short)38), 36);
      this.nativeToLwjglMap.put(Short.valueOf((short)40), 37);
      this.nativeToLwjglMap.put(Short.valueOf((short)37), 38);
      this.nativeToLwjglMap.put(Short.valueOf((short)46), 50);
      this.nativeToLwjglMap.put(Short.valueOf((short)45), 49);
      this.nativeToLwjglMap.put(Short.valueOf((short)31), 24);
      this.nativeToLwjglMap.put(Short.valueOf((short)35), 25);
      this.nativeToLwjglMap.put(Short.valueOf((short)12), 16);
      this.nativeToLwjglMap.put(Short.valueOf((short)15), 19);
      this.nativeToLwjglMap.put(Short.valueOf((short)1), 31);
      this.nativeToLwjglMap.put(Short.valueOf((short)17), 20);
      this.nativeToLwjglMap.put(Short.valueOf((short)32), 22);
      this.nativeToLwjglMap.put(Short.valueOf((short)9), 47);
      this.nativeToLwjglMap.put(Short.valueOf((short)13), 17);
      this.nativeToLwjglMap.put(Short.valueOf((short)7), 45);
      this.nativeToLwjglMap.put(Short.valueOf((short)16), 21);
      this.nativeToLwjglMap.put(Short.valueOf((short)6), 44);
      this.nativeToLwjglMap.put(Short.valueOf((short)42), 43);
      this.nativeToLwjglMap.put(Short.valueOf((short)43), 51);
      this.nativeToLwjglMap.put(Short.valueOf((short)24), 13);
      this.nativeToLwjglMap.put(Short.valueOf((short)33), 26);
      this.nativeToLwjglMap.put(Short.valueOf((short)27), 12);
      this.nativeToLwjglMap.put(Short.valueOf((short)39), 40);
      this.nativeToLwjglMap.put(Short.valueOf((short)30), 27);
      this.nativeToLwjglMap.put(Short.valueOf((short)41), 39);
      this.nativeToLwjglMap.put(Short.valueOf((short)44), 53);
      this.nativeToLwjglMap.put(Short.valueOf((short)47), 52);
      this.nativeToLwjglMap.put(Short.valueOf((short)50), 144);
      this.nativeToLwjglMap.put(Short.valueOf((short)65), 83);
      this.nativeToLwjglMap.put(Short.valueOf((short)67), 55);
      this.nativeToLwjglMap.put(Short.valueOf((short)69), 78);
      this.nativeToLwjglMap.put(Short.valueOf((short)71), 218);
      this.nativeToLwjglMap.put(Short.valueOf((short)75), 181);
      this.nativeToLwjglMap.put(Short.valueOf((short)76), 156);
      this.nativeToLwjglMap.put(Short.valueOf((short)78), 74);
      this.nativeToLwjglMap.put(Short.valueOf((short)81), 141);
      this.nativeToLwjglMap.put(Short.valueOf((short)82), 82);
      this.nativeToLwjglMap.put(Short.valueOf((short)83), 79);
      this.nativeToLwjglMap.put(Short.valueOf((short)84), 80);
      this.nativeToLwjglMap.put(Short.valueOf((short)85), 81);
      this.nativeToLwjglMap.put(Short.valueOf((short)86), 75);
      this.nativeToLwjglMap.put(Short.valueOf((short)87), 76);
      this.nativeToLwjglMap.put(Short.valueOf((short)88), 77);
      this.nativeToLwjglMap.put(Short.valueOf((short)89), 71);
      this.nativeToLwjglMap.put(Short.valueOf((short)91), 72);
      this.nativeToLwjglMap.put(Short.valueOf((short)92), 73);
      this.nativeToLwjglMap.put(Short.valueOf((short)36), 28);
      this.nativeToLwjglMap.put(Short.valueOf((short)48), 15);
      this.nativeToLwjglMap.put(Short.valueOf((short)49), 57);
      this.nativeToLwjglMap.put(Short.valueOf((short)51), 14);
      this.nativeToLwjglMap.put(Short.valueOf((short)53), 1);
      this.nativeToLwjglMap.put(Short.valueOf((short)54), 220);
      this.nativeToLwjglMap.put(Short.valueOf((short)55), 219);
      this.nativeToLwjglMap.put(Short.valueOf((short)56), 42);
      this.nativeToLwjglMap.put(Short.valueOf((short)57), 58);
      this.nativeToLwjglMap.put(Short.valueOf((short)58), 56);
      this.nativeToLwjglMap.put(Short.valueOf((short)59), 29);
      this.nativeToLwjglMap.put(Short.valueOf((short)60), 54);
      this.nativeToLwjglMap.put(Short.valueOf((short)61), 184);
      this.nativeToLwjglMap.put(Short.valueOf((short)62), 157);
      this.nativeToLwjglMap.put(Short.valueOf((short)63), 196);
      this.nativeToLwjglMap.put(Short.valueOf((short)119), 207);
      this.nativeToLwjglMap.put(Short.valueOf((short)122), 59);
      this.nativeToLwjglMap.put(Short.valueOf((short)120), 60);
      this.nativeToLwjglMap.put(Short.valueOf((short)99), 61);
      this.nativeToLwjglMap.put(Short.valueOf((short)118), 62);
      this.nativeToLwjglMap.put(Short.valueOf((short)96), 63);
      this.nativeToLwjglMap.put(Short.valueOf((short)97), 64);
      this.nativeToLwjglMap.put(Short.valueOf((short)98), 65);
      this.nativeToLwjglMap.put(Short.valueOf((short)100), 66);
      this.nativeToLwjglMap.put(Short.valueOf((short)101), 67);
      this.nativeToLwjglMap.put(Short.valueOf((short)109), 68);
      this.nativeToLwjglMap.put(Short.valueOf((short)103), 87);
      this.nativeToLwjglMap.put(Short.valueOf((short)111), 88);
      this.nativeToLwjglMap.put(Short.valueOf((short)105), 100);
      this.nativeToLwjglMap.put(Short.valueOf((short)107), 101);
      this.nativeToLwjglMap.put(Short.valueOf((short)113), 102);
      this.nativeToLwjglMap.put(Short.valueOf((short)106), 103);
      this.nativeToLwjglMap.put(Short.valueOf((short)64), 104);
      this.nativeToLwjglMap.put(Short.valueOf((short)79), 105);
      this.nativeToLwjglMap.put(Short.valueOf((short)80), 113);
      this.nativeToLwjglMap.put(Short.valueOf((short)117), 211);
      this.nativeToLwjglMap.put(Short.valueOf((short)114), 210);
      this.nativeToLwjglMap.put(Short.valueOf((short)115), 199);
      this.nativeToLwjglMap.put(Short.valueOf((short)121), 209);
      this.nativeToLwjglMap.put(Short.valueOf((short)116), 201);
      this.nativeToLwjglMap.put(Short.valueOf((short)123), 203);
      this.nativeToLwjglMap.put(Short.valueOf((short)124), 205);
      this.nativeToLwjglMap.put(Short.valueOf((short)125), 208);
      this.nativeToLwjglMap.put(Short.valueOf((short)126), 200);
      this.nativeToLwjglMap.put(Short.valueOf((short)10), 167);
      this.nativeToLwjglMap.put(Short.valueOf((short)110), 221);
      this.nativeToLwjglMap.put((short)297, 146);
   }

   public void register() {
      this.nRegisterKeyListener(this.window_handle);
   }

   public void unregister() {
      this.nUnregisterKeyListener(this.window_handle);
   }

   public void putKeyboardEvent(int var1, byte var2, int var3, long var4, boolean var6) {
      this.event.clear();
      this.event.putInt(var1).put(var2).putInt(var3).putLong(var4).put((byte)(var6 ? 1 : 0));
      this.event.flip();
      this.putEvent(this.event);
   }

   public synchronized void poll(ByteBuffer var1) {
      this.flushDeferredEvent();
      int var2 = var1.position();
      var1.put(this.key_states);
      var1.position(var2);
   }

   public synchronized void copyEvents(ByteBuffer var1) {
      this.flushDeferredEvent();
      super.copyEvents(var1);
   }

   private synchronized void handleKey(int var1, byte var2, int var3, long var4) {
      if (var3 == 65535) {
         var3 = 0;
      }

      if (var2 == 1) {
         boolean var6 = false;
         if (this.has_deferred_event) {
            if (var4 == this.deferred_nanos && this.deferred_key_code == var1) {
               this.has_deferred_event = false;
               var6 = true;
            } else {
               this.flushDeferredEvent();
            }
         }

         this.putKeyEvent(var1, var2, var3, var4, var6);
      } else {
         this.flushDeferredEvent();
         this.has_deferred_event = true;
         this.deferred_nanos = var4;
         this.deferred_key_code = var1;
         this.deferred_key_state = var2;
         this.deferred_character = var3;
      }

   }

   private void flushDeferredEvent() {
      if (this.has_deferred_event) {
         this.putKeyEvent(this.deferred_key_code, this.deferred_key_state, this.deferred_character, this.deferred_nanos, false);
         this.has_deferred_event = false;
      }

   }

   public void putKeyEvent(int var1, byte var2, int var3, long var4, boolean var6) {
      int var7 = this.getMappedKeyCode((short)var1);
      if (var7 < 0) {
         System.out.println("Unrecognized keycode: " + var1);
      } else {
         if (this.key_states[var7] == var2) {
            var6 = true;
         }

         this.key_states[var7] = var2;
         int var8 = var3 & '\uffff';
         this.putKeyboardEvent(var7, var2, var8, var4, var6);
      }
   }

   private int getMappedKeyCode(short var1) {
      return this.nativeToLwjglMap.containsKey(var1) ? (Integer)this.nativeToLwjglMap.get(var1) : -1;
   }

   public void keyPressed(int var1, String var2, long var3) {
      char var5 = var2 != null && var2.length() != 0 ? var2.charAt(0) : 0;
      this.handleKey(var1, (byte)1, var5, var3);
   }

   public void keyReleased(int var1, String var2, long var3) {
      char var5 = var2 != null && var2.length() != 0 ? var2.charAt(0) : 0;
      this.handleKey(var1, (byte)0, var5, var3);
   }

   public void keyTyped(KeyEvent var1) {
   }
}
