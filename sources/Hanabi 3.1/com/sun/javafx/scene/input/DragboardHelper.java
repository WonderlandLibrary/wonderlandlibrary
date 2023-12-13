package com.sun.javafx.scene.input;

import javafx.scene.input.Dragboard;

public class DragboardHelper {
   private static DragboardAccessor dragboardAccessor;

   private DragboardHelper() {
   }

   public static void setDataAccessRestriction(Dragboard var0, boolean var1) {
      dragboardAccessor.setDataAccessRestriction(var0, var1);
   }

   public static void setDragboardAccessor(DragboardAccessor var0) {
      if (dragboardAccessor != null) {
         throw new IllegalStateException();
      } else {
         dragboardAccessor = var0;
      }
   }

   private static void forceInit(Class var0) {
      try {
         Class.forName(var0.getName(), true, var0.getClassLoader());
      } catch (ClassNotFoundException var2) {
         throw new AssertionError(var2);
      }
   }

   static {
      forceInit(Dragboard.class);
   }

   public interface DragboardAccessor {
      void setDataAccessRestriction(Dragboard var1, boolean var2);
   }
}
