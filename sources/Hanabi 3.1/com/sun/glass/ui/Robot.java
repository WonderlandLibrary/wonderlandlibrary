package com.sun.glass.ui;

import java.security.AllPermission;
import java.security.Permission;

public abstract class Robot {
   private static final Permission allPermission = new AllPermission();
   public static final int MOUSE_LEFT_BTN = 1;
   public static final int MOUSE_RIGHT_BTN = 2;
   public static final int MOUSE_MIDDLE_BTN = 4;

   protected abstract void _create();

   protected Robot() {
      SecurityManager var1 = System.getSecurityManager();
      if (var1 != null) {
         var1.checkPermission(allPermission);
      }

      Application.checkEventThread();
      this._create();
   }

   protected abstract void _destroy();

   public void destroy() {
      Application.checkEventThread();
      this._destroy();
   }

   protected abstract void _keyPress(int var1);

   public void keyPress(int var1) {
      Application.checkEventThread();
      this._keyPress(var1);
   }

   protected abstract void _keyRelease(int var1);

   public void keyRelease(int var1) {
      Application.checkEventThread();
      this._keyRelease(var1);
   }

   protected abstract void _mouseMove(int var1, int var2);

   public void mouseMove(int var1, int var2) {
      Application.checkEventThread();
      this._mouseMove(var1, var2);
   }

   protected abstract void _mousePress(int var1);

   public void mousePress(int var1) {
      Application.checkEventThread();
      this._mousePress(var1);
   }

   protected abstract void _mouseRelease(int var1);

   public void mouseRelease(int var1) {
      Application.checkEventThread();
      this._mouseRelease(var1);
   }

   protected abstract void _mouseWheel(int var1);

   public void mouseWheel(int var1) {
      Application.checkEventThread();
      this._mouseWheel(var1);
   }

   protected abstract int _getMouseX();

   public int getMouseX() {
      Application.checkEventThread();
      return this._getMouseX();
   }

   protected abstract int _getMouseY();

   public int getMouseY() {
      Application.checkEventThread();
      return this._getMouseY();
   }

   protected abstract int _getPixelColor(int var1, int var2);

   public int getPixelColor(int var1, int var2) {
      Application.checkEventThread();
      return this._getPixelColor(var1, var2);
   }

   protected abstract Pixels _getScreenCapture(int var1, int var2, int var3, int var4, boolean var5);

   public Pixels getScreenCapture(int var1, int var2, int var3, int var4, boolean var5) {
      Application.checkEventThread();
      return this._getScreenCapture(var1, var2, var3, var4, var5);
   }

   public Pixels getScreenCapture(int var1, int var2, int var3, int var4) {
      return this.getScreenCapture(var1, var2, var3, var4, false);
   }
}
