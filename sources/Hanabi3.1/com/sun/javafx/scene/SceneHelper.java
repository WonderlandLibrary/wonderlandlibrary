package com.sun.javafx.scene;

import com.sun.glass.ui.Accessible;
import javafx.scene.Camera;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

public final class SceneHelper {
   private static SceneAccessor sceneAccessor;

   private SceneHelper() {
   }

   public static void setPaused(boolean var0) {
      sceneAccessor.setPaused(var0);
   }

   public static void parentEffectiveOrientationInvalidated(Scene var0) {
      sceneAccessor.parentEffectiveOrientationInvalidated(var0);
   }

   public static Camera getEffectiveCamera(Scene var0) {
      return sceneAccessor.getEffectiveCamera(var0);
   }

   public static Scene createPopupScene(Parent var0) {
      return sceneAccessor.createPopupScene(var0);
   }

   public static Accessible getAccessible(Scene var0) {
      return sceneAccessor.getAccessible(var0);
   }

   public static void setSceneAccessor(SceneAccessor var0) {
      if (sceneAccessor != null) {
         throw new IllegalStateException();
      } else {
         sceneAccessor = var0;
      }
   }

   public static SceneAccessor getSceneAccessor() {
      if (sceneAccessor == null) {
         throw new IllegalStateException();
      } else {
         return sceneAccessor;
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
      forceInit(Scene.class);
   }

   public interface SceneAccessor {
      void setPaused(boolean var1);

      void parentEffectiveOrientationInvalidated(Scene var1);

      Camera getEffectiveCamera(Scene var1);

      Scene createPopupScene(Parent var1);

      void setTransientFocusContainer(Scene var1, Node var2);

      Accessible getAccessible(Scene var1);
   }
}
