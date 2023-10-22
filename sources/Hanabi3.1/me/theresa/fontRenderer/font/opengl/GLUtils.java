package me.theresa.fontRenderer.font.opengl;

import me.theresa.fontRenderer.font.opengl.renderer.Renderer;

public final class GLUtils {
   public static void checkGLContext() {
      try {
         Renderer.get().glGetError();
      } catch (NullPointerException var1) {
         throw new RuntimeException("OpenGL based resources (images, fonts, sprites etc) must be loaded as part of init() or the game loop. They cannot be loaded before initialisation.");
      }
   }
}
