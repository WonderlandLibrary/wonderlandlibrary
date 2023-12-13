package com.sun.javafx.robot.impl;

import com.sun.javafx.robot.FXRobotImage;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

public class FXRobotHelper {
   static FXRobotInputAccessor inputAccessor;
   static FXRobotSceneAccessor sceneAccessor;
   static FXRobotStageAccessor stageAccessor;
   static FXRobotImageConvertor imageConvertor;

   public static ObservableList getChildren(Parent var0) {
      if (sceneAccessor == null) {
      }

      return sceneAccessor.getChildren(var0);
   }

   public static ObservableList getStages() {
      if (stageAccessor == null) {
      }

      return stageAccessor.getStages();
   }

   public static Color argbToColor(int var0) {
      int var1 = var0 >> 24;
      var1 &= 255;
      float var2 = (float)var1 / 255.0F;
      int var3 = var0 >> 16;
      var3 &= 255;
      int var4 = var0 >> 8;
      var4 &= 255;
      int var5 = var0 & 255;
      return Color.rgb(var3, var4, var5, (double)var2);
   }

   public static void setInputAccessor(FXRobotInputAccessor var0) {
      if (inputAccessor != null) {
         System.out.println("Warning: Input accessor is already set: " + inputAccessor);
         Thread.dumpStack();
      }

      inputAccessor = var0;
   }

   public static void setSceneAccessor(FXRobotSceneAccessor var0) {
      if (sceneAccessor != null) {
         System.out.println("Warning: Scene accessor is already set: " + sceneAccessor);
         Thread.dumpStack();
      }

      sceneAccessor = var0;
   }

   public static void setImageConvertor(FXRobotImageConvertor var0) {
      if (imageConvertor != null) {
         System.out.println("Warning: Image convertor is already set: " + imageConvertor);
         Thread.dumpStack();
      }

      imageConvertor = var0;
   }

   public static void setStageAccessor(FXRobotStageAccessor var0) {
      if (stageAccessor != null) {
         System.out.println("Warning: Stage accessor already set: " + stageAccessor);
         Thread.dumpStack();
      }

      stageAccessor = var0;
   }

   public abstract static class FXRobotSceneAccessor {
      public abstract void processKeyEvent(Scene var1, KeyEvent var2);

      public abstract void processMouseEvent(Scene var1, MouseEvent var2);

      public abstract void processScrollEvent(Scene var1, ScrollEvent var2);

      public abstract ObservableList getChildren(Parent var1);

      public abstract Object renderToImage(Scene var1, Object var2);
   }

   public abstract static class FXRobotInputAccessor {
      public abstract int getCodeForKeyCode(KeyCode var1);

      public abstract KeyCode getKeyCodeForCode(int var1);

      public abstract KeyEvent createKeyEvent(EventType var1, KeyCode var2, String var3, String var4, boolean var5, boolean var6, boolean var7, boolean var8);

      public abstract MouseEvent createMouseEvent(EventType var1, int var2, int var3, int var4, int var5, MouseButton var6, int var7, boolean var8, boolean var9, boolean var10, boolean var11, boolean var12, boolean var13, boolean var14, boolean var15);

      public abstract ScrollEvent createScrollEvent(EventType var1, int var2, int var3, ScrollEvent.HorizontalTextScrollUnits var4, int var5, ScrollEvent.VerticalTextScrollUnits var6, int var7, int var8, int var9, int var10, int var11, boolean var12, boolean var13, boolean var14, boolean var15);
   }

   public abstract static class FXRobotImageConvertor {
      public abstract FXRobotImage convertToFXRobotImage(Object var1);
   }

   public abstract static class FXRobotStageAccessor {
      public abstract ObservableList getStages();
   }
}
