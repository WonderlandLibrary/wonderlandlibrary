package com.sun.javafx.webkit.theme;

import com.sun.webkit.graphics.WCGraphicsContext;
import javafx.scene.control.Control;

public abstract class Renderer {
   private static Renderer instance;

   public static void setRenderer(Renderer var0) {
      instance = var0;
   }

   public static Renderer getRenderer() {
      return instance;
   }

   protected abstract void render(Control var1, WCGraphicsContext var2);
}
