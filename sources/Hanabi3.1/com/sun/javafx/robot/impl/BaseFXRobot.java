package com.sun.javafx.robot.impl;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.robot.FXRobot;
import com.sun.javafx.robot.FXRobotImage;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class BaseFXRobot extends FXRobot {
   private static final boolean debugOut;
   private static Map keyTextMap;
   private Scene target;
   private boolean isShiftDown = false;
   private boolean isControlDown = false;
   private boolean isAltDown = false;
   private boolean isMetaDown = false;
   private boolean isButton1Pressed = false;
   private boolean isButton2Pressed = false;
   private boolean isButton3Pressed = false;
   private MouseButton lastButtonPressed = null;
   private double sceneMouseX;
   private double sceneMouseY;
   private double screenMouseX;
   private double screenMouseY;
   private Object lastImage;
   private FXRobotImage lastConvertedImage;

   private static boolean computeDebugOut() {
      boolean var0 = false;

      try {
         var0 = "true".equals(System.getProperty("fxrobot.verbose", "false"));
      } catch (Throwable var2) {
      }

      return var0;
   }

   private static void out(String var0) {
      if (debugOut) {
         System.out.println("[FXRobot] " + var0);
      }

   }

   private static String getKeyText(KeyCode var0) {
      return var0.getName();
   }

   public BaseFXRobot(Scene var1) {
      this.target = var1;
   }

   public void waitForIdle() {
      CountDownLatch var1 = new CountDownLatch(1);
      PlatformImpl.runLater(() -> {
         var1.countDown();
      });

      while(true) {
         try {
            var1.await();
            return;
         } catch (InterruptedException var3) {
         }
      }
   }

   public void keyPress(KeyCode var1) {
      this.doKeyEvent(KeyEvent.KEY_PRESSED, var1, "");
   }

   public void keyRelease(KeyCode var1) {
      this.doKeyEvent(KeyEvent.KEY_RELEASED, var1, "");
   }

   public void keyType(KeyCode var1, String var2) {
      this.doKeyEvent(KeyEvent.KEY_TYPED, var1, var2);
   }

   public void mouseMove(int var1, int var2) {
      this.doMouseEvent((double)var1, (double)var2, this.lastButtonPressed, 0, MouseEvent.MOUSE_MOVED);
   }

   public void mousePress(MouseButton var1, int var2) {
      this.doMouseEvent(this.sceneMouseX, this.sceneMouseY, var1, var2, MouseEvent.MOUSE_PRESSED);
   }

   public void mouseRelease(MouseButton var1, int var2) {
      this.doMouseEvent(this.sceneMouseX, this.sceneMouseY, var1, var2, MouseEvent.MOUSE_RELEASED);
   }

   public void mouseClick(MouseButton var1, int var2) {
      this.doMouseEvent(this.sceneMouseX, this.sceneMouseY, var1, var2, MouseEvent.MOUSE_CLICKED);
   }

   public void mouseDrag(MouseButton var1) {
      this.doMouseEvent(this.sceneMouseX, this.sceneMouseY, var1, 0, MouseEvent.MOUSE_DRAGGED);
   }

   public void mouseWheel(int var1) {
      this.doScrollEvent(this.sceneMouseX, this.sceneMouseY, (double)var1, ScrollEvent.SCROLL);
   }

   public int getPixelColor(int var1, int var2) {
      FXRobotImage var3 = this.getSceneCapture(0, 0, 100, 100);
      return var3 != null ? var3.getArgb(var1, var2) : 0;
   }

   public FXRobotImage getSceneCapture(int var1, int var2, int var3, int var4) {
      Object var5 = FXRobotHelper.sceneAccessor.renderToImage(this.target, this.lastImage);
      if (var5 != null) {
         this.lastImage = var5;
         this.lastConvertedImage = FXRobotHelper.imageConvertor.convertToFXRobotImage(var5);
      }

      return this.lastConvertedImage;
   }

   private void doKeyEvent(EventType var1, KeyCode var2, String var3) {
      boolean var4 = var1 == KeyEvent.KEY_PRESSED;
      boolean var5 = var1 == KeyEvent.KEY_TYPED;
      if (var2 == KeyCode.SHIFT) {
         this.isShiftDown = var4;
      }

      if (var2 == KeyCode.CONTROL) {
         this.isControlDown = var4;
      }

      if (var2 == KeyCode.ALT) {
         this.isAltDown = var4;
      }

      if (var2 == KeyCode.META) {
         this.isMetaDown = var4;
      }

      String var6 = var5 ? "" : getKeyText(var2);
      String var7 = var5 ? var3 : KeyEvent.CHAR_UNDEFINED;
      KeyEvent var8 = FXRobotHelper.inputAccessor.createKeyEvent(var1, var2, var7, var6, this.isShiftDown, this.isControlDown, this.isAltDown, this.isMetaDown);
      PlatformImpl.runLater(() -> {
         out("doKeyEvent: injecting: {e}");
         FXRobotHelper.sceneAccessor.processKeyEvent(this.target, var8);
      });
      if (this.autoWait) {
         this.waitForIdle();
      }

   }

   private void doMouseEvent(double var1, double var3, MouseButton var5, int var6, EventType var7) {
      this.screenMouseX = this.target.getWindow().getX() + this.target.getX() + var1;
      this.screenMouseY = this.target.getWindow().getY() + this.target.getY() + var3;
      this.sceneMouseX = var1;
      this.sceneMouseY = var3;
      MouseButton var8 = var5;
      EventType var9 = var7;
      boolean var10;
      if (var7 != MouseEvent.MOUSE_PRESSED && var7 != MouseEvent.MOUSE_RELEASED) {
         if (var7 == MouseEvent.MOUSE_MOVED) {
            var10 = this.isButton1Pressed || this.isButton2Pressed || this.isButton3Pressed;
            if (var10) {
               var9 = MouseEvent.MOUSE_DRAGGED;
               var8 = MouseButton.NONE;
            }
         }
      } else {
         var10 = var7 == MouseEvent.MOUSE_PRESSED;
         if (var5 == MouseButton.PRIMARY) {
            this.isButton1Pressed = var10;
         } else if (var5 == MouseButton.MIDDLE) {
            this.isButton2Pressed = var10;
         } else if (var5 == MouseButton.SECONDARY) {
            this.isButton3Pressed = var10;
         }

         if (var10) {
            this.lastButtonPressed = var5;
         } else if (!this.isButton1Pressed && !this.isButton2Pressed && !this.isButton3Pressed) {
            this.lastButtonPressed = MouseButton.NONE;
         }
      }

      MouseEvent var11 = FXRobotHelper.inputAccessor.createMouseEvent(var9, (int)this.sceneMouseX, (int)this.sceneMouseY, (int)this.screenMouseX, (int)this.screenMouseY, var8, var6, this.isShiftDown, this.isControlDown, this.isAltDown, this.isMetaDown, var8 == MouseButton.SECONDARY, this.isButton1Pressed, this.isButton2Pressed, this.isButton3Pressed);
      PlatformImpl.runLater(() -> {
         out("doMouseEvent: injecting: " + var11);
         FXRobotHelper.sceneAccessor.processMouseEvent(this.target, var11);
      });
      if (this.autoWait) {
         this.waitForIdle();
      }

   }

   private void doScrollEvent(double var1, double var3, double var5, EventType var7) {
      this.screenMouseX = this.target.getWindow().getX() + this.target.getX() + var1;
      this.screenMouseY = this.target.getWindow().getY() + this.target.getY() + var3;
      this.sceneMouseX = var1;
      this.sceneMouseY = var3;
      ScrollEvent var8 = FXRobotHelper.inputAccessor.createScrollEvent(var7, 0, (int)var5 * 40, ScrollEvent.HorizontalTextScrollUnits.NONE, 0, ScrollEvent.VerticalTextScrollUnits.NONE, 0, (int)this.sceneMouseX, (int)this.sceneMouseY, (int)this.screenMouseX, (int)this.screenMouseY, this.isShiftDown, this.isControlDown, this.isAltDown, this.isMetaDown);
      PlatformImpl.runLater(() -> {
         out("doScrollEvent: injecting: " + var8);
         FXRobotHelper.sceneAccessor.processScrollEvent(this.target, var8);
      });
      if (this.autoWait) {
         this.waitForIdle();
      }

   }

   static {
      String var0 = KeyEvent.CHAR_UNDEFINED;
      debugOut = computeDebugOut();
   }
}
