package com.sun.javafx.cursor;

import java.util.HashMap;
import java.util.Map;

public abstract class CursorFrame {
   private Class firstPlatformCursorClass;
   private Object firstPlatformCursor;
   private Map otherPlatformCursors;

   public abstract CursorType getCursorType();

   public Object getPlatformCursor(Class var1) {
      if (this.firstPlatformCursorClass == var1) {
         return this.firstPlatformCursor;
      } else {
         return this.otherPlatformCursors != null ? this.otherPlatformCursors.get(var1) : null;
      }
   }

   public void setPlatforCursor(Class var1, Object var2) {
      if (this.firstPlatformCursorClass != null && this.firstPlatformCursorClass != var1) {
         if (this.otherPlatformCursors == null) {
            this.otherPlatformCursors = new HashMap();
         }

         this.otherPlatformCursors.put(var1, var2);
      } else {
         this.firstPlatformCursorClass = var1;
         this.firstPlatformCursor = var2;
      }
   }
}
