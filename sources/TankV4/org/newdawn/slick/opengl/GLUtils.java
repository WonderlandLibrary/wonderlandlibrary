package org.newdawn.slick.opengl;

import org.lwjgl.opengl.GL11;

public final class GLUtils {
   public static void checkGLContext() {
      try {
         GL11.glGetError();
      } catch (NullPointerException var1) {
         throw new RuntimeException("OpenGL based resources (images, fonts, sprites etc) must be loaded as part of init() or the game loop. They cannot be loaded before initialisation.");
      }
   }
}
