package com.sun.javafx.webkit;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.input.KeyCode;

public final class KeyCodeMap {
   private static final Map MAP;

   private static void put(Map var0, KeyCode var1, int var2, String var3) {
      var0.put(var1, new Entry(var2, var3));
   }

   private static void put(Map var0, KeyCode var1, int var2) {
      put(var0, var1, var2, (String)null);
   }

   public static Entry lookup(KeyCode var0) {
      Entry var1 = (Entry)MAP.get(var0);
      if (var1 == null || var1.getKeyIdentifier() == null) {
         int var2 = var1 != null ? var1.getWindowsVirtualKeyCode() : 0;
         String var3 = String.format("U+%04X", var2);
         var1 = new Entry(var2, var3);
      }

      return var1;
   }

   static {
      HashMap var0 = new HashMap();
      put(var0, KeyCode.ENTER, 13, "Enter");
      put(var0, KeyCode.BACK_SPACE, 8);
      put(var0, KeyCode.TAB, 9);
      put(var0, KeyCode.CANCEL, 3);
      put(var0, KeyCode.CLEAR, 12, "Clear");
      put(var0, KeyCode.SHIFT, 16, "Shift");
      put(var0, KeyCode.CONTROL, 17, "Control");
      put(var0, KeyCode.ALT, 18, "Alt");
      put(var0, KeyCode.PAUSE, 19, "Pause");
      put(var0, KeyCode.CAPS, 20, "CapsLock");
      put(var0, KeyCode.ESCAPE, 27);
      put(var0, KeyCode.SPACE, 32);
      put(var0, KeyCode.PAGE_UP, 33, "PageUp");
      put(var0, KeyCode.PAGE_DOWN, 34, "PageDown");
      put(var0, KeyCode.END, 35, "End");
      put(var0, KeyCode.HOME, 36, "Home");
      put(var0, KeyCode.LEFT, 37, "Left");
      put(var0, KeyCode.UP, 38, "Up");
      put(var0, KeyCode.RIGHT, 39, "Right");
      put(var0, KeyCode.DOWN, 40, "Down");
      put(var0, KeyCode.COMMA, 188);
      put(var0, KeyCode.MINUS, 189);
      put(var0, KeyCode.PERIOD, 190);
      put(var0, KeyCode.SLASH, 191);
      put(var0, KeyCode.DIGIT0, 48);
      put(var0, KeyCode.DIGIT1, 49);
      put(var0, KeyCode.DIGIT2, 50);
      put(var0, KeyCode.DIGIT3, 51);
      put(var0, KeyCode.DIGIT4, 52);
      put(var0, KeyCode.DIGIT5, 53);
      put(var0, KeyCode.DIGIT6, 54);
      put(var0, KeyCode.DIGIT7, 55);
      put(var0, KeyCode.DIGIT8, 56);
      put(var0, KeyCode.DIGIT9, 57);
      put(var0, KeyCode.SEMICOLON, 186);
      put(var0, KeyCode.EQUALS, 187);
      put(var0, KeyCode.A, 65);
      put(var0, KeyCode.B, 66);
      put(var0, KeyCode.C, 67);
      put(var0, KeyCode.D, 68);
      put(var0, KeyCode.E, 69);
      put(var0, KeyCode.F, 70);
      put(var0, KeyCode.G, 71);
      put(var0, KeyCode.H, 72);
      put(var0, KeyCode.I, 73);
      put(var0, KeyCode.J, 74);
      put(var0, KeyCode.K, 75);
      put(var0, KeyCode.L, 76);
      put(var0, KeyCode.M, 77);
      put(var0, KeyCode.N, 78);
      put(var0, KeyCode.O, 79);
      put(var0, KeyCode.P, 80);
      put(var0, KeyCode.Q, 81);
      put(var0, KeyCode.R, 82);
      put(var0, KeyCode.S, 83);
      put(var0, KeyCode.T, 84);
      put(var0, KeyCode.U, 85);
      put(var0, KeyCode.V, 86);
      put(var0, KeyCode.W, 87);
      put(var0, KeyCode.X, 88);
      put(var0, KeyCode.Y, 89);
      put(var0, KeyCode.Z, 90);
      put(var0, KeyCode.OPEN_BRACKET, 219);
      put(var0, KeyCode.BACK_SLASH, 220);
      put(var0, KeyCode.CLOSE_BRACKET, 221);
      put(var0, KeyCode.NUMPAD0, 96);
      put(var0, KeyCode.NUMPAD1, 97);
      put(var0, KeyCode.NUMPAD2, 98);
      put(var0, KeyCode.NUMPAD3, 99);
      put(var0, KeyCode.NUMPAD4, 100);
      put(var0, KeyCode.NUMPAD5, 101);
      put(var0, KeyCode.NUMPAD6, 102);
      put(var0, KeyCode.NUMPAD7, 103);
      put(var0, KeyCode.NUMPAD8, 104);
      put(var0, KeyCode.NUMPAD9, 105);
      put(var0, KeyCode.MULTIPLY, 106);
      put(var0, KeyCode.ADD, 107);
      put(var0, KeyCode.SEPARATOR, 108);
      put(var0, KeyCode.SUBTRACT, 109);
      put(var0, KeyCode.DECIMAL, 110);
      put(var0, KeyCode.DIVIDE, 111);
      put(var0, KeyCode.DELETE, 46, "U+007F");
      put(var0, KeyCode.NUM_LOCK, 144);
      put(var0, KeyCode.SCROLL_LOCK, 145, "Scroll");
      put(var0, KeyCode.F1, 112, "F1");
      put(var0, KeyCode.F2, 113, "F2");
      put(var0, KeyCode.F3, 114, "F3");
      put(var0, KeyCode.F4, 115, "F4");
      put(var0, KeyCode.F5, 116, "F5");
      put(var0, KeyCode.F6, 117, "F6");
      put(var0, KeyCode.F7, 118, "F7");
      put(var0, KeyCode.F8, 119, "F8");
      put(var0, KeyCode.F9, 120, "F9");
      put(var0, KeyCode.F10, 121, "F10");
      put(var0, KeyCode.F11, 122, "F11");
      put(var0, KeyCode.F12, 123, "F12");
      put(var0, KeyCode.F13, 124, "F13");
      put(var0, KeyCode.F14, 125, "F14");
      put(var0, KeyCode.F15, 126, "F15");
      put(var0, KeyCode.F16, 127, "F16");
      put(var0, KeyCode.F17, 128, "F17");
      put(var0, KeyCode.F18, 129, "F18");
      put(var0, KeyCode.F19, 130, "F19");
      put(var0, KeyCode.F20, 131, "F20");
      put(var0, KeyCode.F21, 132, "F21");
      put(var0, KeyCode.F22, 133, "F22");
      put(var0, KeyCode.F23, 134, "F23");
      put(var0, KeyCode.F24, 135, "F24");
      put(var0, KeyCode.PRINTSCREEN, 44, "PrintScreen");
      put(var0, KeyCode.INSERT, 45, "Insert");
      put(var0, KeyCode.HELP, 47, "Help");
      put(var0, KeyCode.META, 0, "Meta");
      put(var0, KeyCode.BACK_QUOTE, 192);
      put(var0, KeyCode.QUOTE, 222);
      put(var0, KeyCode.KP_UP, 38, "Up");
      put(var0, KeyCode.KP_DOWN, 40, "Down");
      put(var0, KeyCode.KP_LEFT, 37, "Left");
      put(var0, KeyCode.KP_RIGHT, 39, "Right");
      put(var0, KeyCode.AMPERSAND, 55);
      put(var0, KeyCode.ASTERISK, 56);
      put(var0, KeyCode.QUOTEDBL, 222);
      put(var0, KeyCode.LESS, 188);
      put(var0, KeyCode.GREATER, 190);
      put(var0, KeyCode.BRACELEFT, 219);
      put(var0, KeyCode.BRACERIGHT, 221);
      put(var0, KeyCode.AT, 50);
      put(var0, KeyCode.COLON, 186);
      put(var0, KeyCode.CIRCUMFLEX, 54);
      put(var0, KeyCode.DOLLAR, 52);
      put(var0, KeyCode.EXCLAMATION_MARK, 49);
      put(var0, KeyCode.LEFT_PARENTHESIS, 57);
      put(var0, KeyCode.NUMBER_SIGN, 51);
      put(var0, KeyCode.PLUS, 187);
      put(var0, KeyCode.RIGHT_PARENTHESIS, 48);
      put(var0, KeyCode.UNDERSCORE, 189);
      put(var0, KeyCode.WINDOWS, 91, "Win");
      put(var0, KeyCode.CONTEXT_MENU, 93);
      put(var0, KeyCode.FINAL, 24);
      put(var0, KeyCode.CONVERT, 28);
      put(var0, KeyCode.NONCONVERT, 29);
      put(var0, KeyCode.ACCEPT, 30);
      put(var0, KeyCode.MODECHANGE, 31);
      put(var0, KeyCode.KANA, 21);
      put(var0, KeyCode.KANJI, 25);
      put(var0, KeyCode.ALT_GRAPH, 165);
      put(var0, KeyCode.PLAY, 250);
      put(var0, KeyCode.TRACK_PREV, 177);
      put(var0, KeyCode.TRACK_NEXT, 176);
      put(var0, KeyCode.VOLUME_UP, 175);
      put(var0, KeyCode.VOLUME_DOWN, 174);
      put(var0, KeyCode.MUTE, 173);
      MAP = Collections.unmodifiableMap(var0);
   }

   public static final class Entry {
      private final int windowsVirtualKeyCode;
      private final String keyIdentifier;

      private Entry(int var1, String var2) {
         this.windowsVirtualKeyCode = var1;
         this.keyIdentifier = var2;
      }

      public int getWindowsVirtualKeyCode() {
         return this.windowsVirtualKeyCode;
      }

      public String getKeyIdentifier() {
         return this.keyIdentifier;
      }

      // $FF: synthetic method
      Entry(int var1, String var2, Object var3) {
         this(var1, var2);
      }
   }
}
