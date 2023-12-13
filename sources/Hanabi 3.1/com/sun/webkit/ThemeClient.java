package com.sun.webkit;

import com.sun.webkit.graphics.RenderTheme;
import com.sun.webkit.graphics.ScrollBarTheme;

public abstract class ThemeClient {
   private static RenderTheme defaultRenderTheme;

   public static void setDefaultRenderTheme(RenderTheme var0) {
      defaultRenderTheme = var0;
   }

   public static RenderTheme getDefaultRenderTheme() {
      return defaultRenderTheme;
   }

   protected abstract RenderTheme createRenderTheme();

   protected abstract ScrollBarTheme createScrollBarTheme();
}
