package com.sun.javafx.scene.control.skin.resources;

import java.util.ResourceBundle;

public final class ControlResources {
   private static final String BASE_NAME = "com/sun/javafx/scene/control/skin/resources/controls";
   private static final String NT_BASE_NAME = "com/sun/javafx/scene/control/skin/resources/controls-nt";

   private ControlResources() {
   }

   public static String getString(String var0) {
      return ResourceBundle.getBundle("com/sun/javafx/scene/control/skin/resources/controls").getString(var0);
   }

   public static String getNonTranslatableString(String var0) {
      return ResourceBundle.getBundle("com/sun/javafx/scene/control/skin/resources/controls-nt").getString(var0);
   }
}
