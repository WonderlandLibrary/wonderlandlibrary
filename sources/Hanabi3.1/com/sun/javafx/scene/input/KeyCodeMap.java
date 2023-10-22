package com.sun.javafx.scene.input;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.input.KeyCode;

public final class KeyCodeMap {
   private static final Map charMap = new HashMap(KeyCode.values().length);

   KeyCodeMap() {
   }

   public static KeyCode valueOf(int var0) {
      return (KeyCode)charMap.get(var0);
   }

   static {
      KeyCode[] var0 = KeyCode.values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         KeyCode var3 = var0[var2];
         charMap.put(var3.impl_getCode(), var3);
      }

   }
}
