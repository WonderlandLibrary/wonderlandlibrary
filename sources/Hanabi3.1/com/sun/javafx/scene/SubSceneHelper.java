package com.sun.javafx.scene;

import javafx.scene.Camera;
import javafx.scene.SubScene;

public class SubSceneHelper {
   private static SubSceneAccessor subSceneAccessor;

   private SubSceneHelper() {
   }

   public static boolean isDepthBuffer(SubScene var0) {
      return subSceneAccessor.isDepthBuffer(var0);
   }

   public static Camera getEffectiveCamera(SubScene var0) {
      return subSceneAccessor.getEffectiveCamera(var0);
   }

   public static void setSubSceneAccessor(SubSceneAccessor var0) {
      if (subSceneAccessor != null) {
         throw new IllegalStateException();
      } else {
         subSceneAccessor = var0;
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
      forceInit(SubScene.class);
   }

   public interface SubSceneAccessor {
      boolean isDepthBuffer(SubScene var1);

      Camera getEffectiveCamera(SubScene var1);
   }
}
