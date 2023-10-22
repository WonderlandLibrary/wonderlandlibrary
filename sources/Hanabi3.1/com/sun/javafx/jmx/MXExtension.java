package com.sun.javafx.jmx;

import com.sun.javafx.util.Logging;

public abstract class MXExtension {
   private static final String EXTENSION_CLASS_NAME = System.getProperty("javafx.debug.jmx.class", "com.oracle.javafx.jmx.MXExtensionImpl");

   public abstract void intialize() throws Exception;

   public static void initializeIfAvailable() {
      try {
         ClassLoader var0 = MXExtension.class.getClassLoader();
         Class var1 = Class.forName(EXTENSION_CLASS_NAME, false, var0);
         if (!MXExtension.class.isAssignableFrom(var1)) {
            throw new IllegalArgumentException("Unrecognized MXExtension class: " + EXTENSION_CLASS_NAME);
         }

         MXExtension var2 = (MXExtension)var1.newInstance();
         var2.intialize();
      } catch (Exception var3) {
         Logging.getJavaFXLogger().info("Failed to initialize management extension", var3);
      }

   }
}
