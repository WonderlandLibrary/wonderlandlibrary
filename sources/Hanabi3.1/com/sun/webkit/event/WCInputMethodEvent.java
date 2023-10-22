package com.sun.webkit.event;

import java.util.Arrays;

public final class WCInputMethodEvent {
   public static final int INPUT_METHOD_TEXT_CHANGED = 0;
   public static final int CARET_POSITION_CHANGED = 1;
   private final int id;
   private final String composed;
   private final String committed;
   private final int[] attributes;
   private final int caretPosition;

   public WCInputMethodEvent(String var1, String var2, int[] var3, int var4) {
      this.id = 0;
      this.composed = var1;
      this.committed = var2;
      this.attributes = Arrays.copyOf(var3, var3.length);
      this.caretPosition = var4;
   }

   public WCInputMethodEvent(int var1) {
      this.id = 1;
      this.composed = null;
      this.committed = null;
      this.attributes = null;
      this.caretPosition = var1;
   }

   public int getID() {
      return this.id;
   }

   public String getComposed() {
      return this.composed;
   }

   public String getCommitted() {
      return this.committed;
   }

   public int[] getAttributes() {
      return Arrays.copyOf(this.attributes, this.attributes.length);
   }

   public int getCaretPosition() {
      return this.caretPosition;
   }
}
