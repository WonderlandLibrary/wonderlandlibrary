package com.sun.javafx.cursor;

public final class StandardCursorFrame extends CursorFrame {
   private CursorType cursorType;

   public StandardCursorFrame(CursorType var1) {
      this.cursorType = var1;
   }

   public CursorType getCursorType() {
      return this.cursorType;
   }
}
