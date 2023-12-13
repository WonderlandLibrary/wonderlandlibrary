package org.lwjgl.opengl;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.ByteBuffer;

final class KeyboardEventQueue extends EventQueue implements KeyListener {
   private static final int[] KEY_MAP = new int['\uffff'];
   private final byte[] key_states = new byte[256];
   private final ByteBuffer event = ByteBuffer.allocate(18);
   private final Component component;
   private boolean has_deferred_event;
   private long deferred_nanos;
   private int deferred_key_code;
   private int deferred_key_location;
   private byte deferred_key_state;
   private int deferred_character;

   KeyboardEventQueue(Component var1) {
      super(18);
      this.component = var1;
   }

   public void register() {
      this.component.addKeyListener(this);
   }

   public void unregister() {
   }

   private void putKeyboardEvent(int var1, byte var2, int var3, long var4, boolean var6) {
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

   private synchronized void handleKey(int var1, int var2, byte var3, int var4, long var5) {
      if (var4 == 65535) {
         var4 = 0;
      }

      if (var3 == 1) {
         boolean var7 = false;
         if (this.has_deferred_event) {
            if (var5 == this.deferred_nanos && this.deferred_key_code == var1 && this.deferred_key_location == var2) {
               this.has_deferred_event = false;
               var7 = true;
            } else {
               this.flushDeferredEvent();
            }
         }

         this.putKeyEvent(var1, var2, var3, var4, var5, var7);
      } else {
         this.flushDeferredEvent();
         this.has_deferred_event = true;
         this.deferred_nanos = var5;
         this.deferred_key_code = var1;
         this.deferred_key_location = var2;
         this.deferred_key_state = var3;
         this.deferred_character = var4;
      }

   }

   private void flushDeferredEvent() {
      if (this.has_deferred_event) {
         this.putKeyEvent(this.deferred_key_code, this.deferred_key_location, this.deferred_key_state, this.deferred_character, this.deferred_nanos, false);
         this.has_deferred_event = false;
      }

   }

   private void putKeyEvent(int var1, int var2, byte var3, int var4, long var5, boolean var7) {
      int var8 = this.getMappedKeyCode(var1, var2);
      if (this.key_states[var8] == var3) {
         var7 = true;
      }

      this.key_states[var8] = var3;
      int var9 = var4 & '\uffff';
      this.putKeyboardEvent(var8, var3, var9, var5, var7);
   }

   private int getMappedKeyCode(int var1, int var2) {
      switch(var1) {
      case 16:
         if (var2 == 3) {
            return 54;
         }

         return 42;
      case 17:
         if (var2 == 3) {
            return 157;
         }

         return 29;
      case 18:
         if (var2 == 3) {
            return 184;
         }

         return 56;
      case 157:
         if (var2 == 3) {
            return 220;
         }

         return 219;
      default:
         return KEY_MAP[var1];
      }
   }

   public void keyPressed(KeyEvent var1) {
      this.handleKey(var1.getKeyCode(), var1.getKeyLocation(), (byte)1, var1.getKeyChar(), var1.getWhen() * 1000000L);
   }

   public void keyReleased(KeyEvent var1) {
      this.handleKey(var1.getKeyCode(), var1.getKeyLocation(), (byte)0, 0, var1.getWhen() * 1000000L);
   }

   public void keyTyped(KeyEvent var1) {
   }

   static {
      KEY_MAP[48] = 11;
      KEY_MAP[49] = 2;
      KEY_MAP[50] = 3;
      KEY_MAP[51] = 4;
      KEY_MAP[52] = 5;
      KEY_MAP[53] = 6;
      KEY_MAP[54] = 7;
      KEY_MAP[55] = 8;
      KEY_MAP[56] = 9;
      KEY_MAP[57] = 10;
      KEY_MAP[65] = 30;
      KEY_MAP[107] = 78;
      KEY_MAP['ｾ'] = 184;
      KEY_MAP[512] = 145;
      KEY_MAP[66] = 48;
      KEY_MAP[92] = 43;
      KEY_MAP[8] = 14;
      KEY_MAP[67] = 46;
      KEY_MAP[20] = 58;
      KEY_MAP[514] = 144;
      KEY_MAP[93] = 27;
      KEY_MAP[513] = 146;
      KEY_MAP[44] = 51;
      KEY_MAP[28] = 121;
      KEY_MAP[68] = 32;
      KEY_MAP[110] = 83;
      KEY_MAP[127] = 211;
      KEY_MAP[111] = 181;
      KEY_MAP[40] = 208;
      KEY_MAP[69] = 18;
      KEY_MAP[35] = 207;
      KEY_MAP[10] = 28;
      KEY_MAP[61] = 13;
      KEY_MAP[27] = 1;
      KEY_MAP[70] = 33;
      KEY_MAP[112] = 59;
      KEY_MAP[121] = 68;
      KEY_MAP[122] = 87;
      KEY_MAP[123] = 88;
      KEY_MAP['\uf000'] = 100;
      KEY_MAP['\uf001'] = 101;
      KEY_MAP['\uf002'] = 102;
      KEY_MAP[113] = 60;
      KEY_MAP[114] = 61;
      KEY_MAP[115] = 62;
      KEY_MAP[116] = 63;
      KEY_MAP[117] = 64;
      KEY_MAP[118] = 65;
      KEY_MAP[119] = 66;
      KEY_MAP[120] = 67;
      KEY_MAP[71] = 34;
      KEY_MAP[72] = 35;
      KEY_MAP[36] = 199;
      KEY_MAP[73] = 23;
      KEY_MAP[155] = 210;
      KEY_MAP[74] = 36;
      KEY_MAP[75] = 37;
      KEY_MAP[21] = 112;
      KEY_MAP[25] = 148;
      KEY_MAP[76] = 38;
      KEY_MAP[37] = 203;
      KEY_MAP[77] = 50;
      KEY_MAP[45] = 12;
      KEY_MAP[106] = 55;
      KEY_MAP[78] = 49;
      KEY_MAP[144] = 69;
      KEY_MAP[96] = 82;
      KEY_MAP[97] = 79;
      KEY_MAP[98] = 80;
      KEY_MAP[99] = 81;
      KEY_MAP[100] = 75;
      KEY_MAP[101] = 76;
      KEY_MAP[102] = 77;
      KEY_MAP[103] = 71;
      KEY_MAP[104] = 72;
      KEY_MAP[105] = 73;
      KEY_MAP[79] = 24;
      KEY_MAP[91] = 26;
      KEY_MAP[80] = 25;
      KEY_MAP[34] = 209;
      KEY_MAP[33] = 201;
      KEY_MAP[19] = 197;
      KEY_MAP[46] = 52;
      KEY_MAP[81] = 16;
      KEY_MAP[82] = 19;
      KEY_MAP[39] = 205;
      KEY_MAP[83] = 31;
      KEY_MAP[145] = 70;
      KEY_MAP[59] = 39;
      KEY_MAP[108] = 83;
      KEY_MAP[47] = 53;
      KEY_MAP[32] = 57;
      KEY_MAP['\uffc8'] = 149;
      KEY_MAP[109] = 74;
      KEY_MAP[84] = 20;
      KEY_MAP[9] = 15;
      KEY_MAP[85] = 22;
      KEY_MAP[38] = 200;
      KEY_MAP[86] = 47;
      KEY_MAP[87] = 17;
      KEY_MAP[88] = 45;
      KEY_MAP[89] = 21;
      KEY_MAP[90] = 44;
   }
}
