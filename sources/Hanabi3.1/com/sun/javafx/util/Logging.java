package com.sun.javafx.util;

import sun.util.logging.PlatformLogger;

public class Logging {
   private static PlatformLogger layoutLogger = null;
   private static PlatformLogger focusLogger = null;
   private static PlatformLogger inputLogger = null;
   private static PlatformLogger cssLogger = null;
   private static PlatformLogger javafxLogger = null;
   private static PlatformLogger accessibilityLogger = null;

   public static final PlatformLogger getLayoutLogger() {
      if (layoutLogger == null) {
         layoutLogger = PlatformLogger.getLogger("javafx.scene.layout");
      }

      return layoutLogger;
   }

   public static final PlatformLogger getFocusLogger() {
      if (focusLogger == null) {
         focusLogger = PlatformLogger.getLogger("javafx.scene.focus");
      }

      return focusLogger;
   }

   public static final PlatformLogger getInputLogger() {
      if (inputLogger == null) {
         inputLogger = PlatformLogger.getLogger("javafx.scene.input");
      }

      return inputLogger;
   }

   public static final PlatformLogger getCSSLogger() {
      if (cssLogger == null) {
         cssLogger = PlatformLogger.getLogger("javafx.css");
      }

      return cssLogger;
   }

   public static final PlatformLogger getJavaFXLogger() {
      if (javafxLogger == null) {
         javafxLogger = PlatformLogger.getLogger("javafx");
      }

      return javafxLogger;
   }

   public static final PlatformLogger getAccessibilityLogger() {
      if (accessibilityLogger == null) {
         accessibilityLogger = PlatformLogger.getLogger("javafx.accessibility");
      }

      return accessibilityLogger;
   }
}
