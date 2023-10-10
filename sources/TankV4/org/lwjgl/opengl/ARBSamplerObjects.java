package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class ARBSamplerObjects {
   public static final int GL_SAMPLER_BINDING = 35097;

   private ARBSamplerObjects() {
   }

   public static void glGenSamplers(IntBuffer var0) {
      GL33.glGenSamplers(var0);
   }

   public static int glGenSamplers() {
      return GL33.glGenSamplers();
   }

   public static void glDeleteSamplers(IntBuffer var0) {
      GL33.glDeleteSamplers(var0);
   }

   public static void glDeleteSamplers(int var0) {
      GL33.glDeleteSamplers(var0);
   }

   public static boolean glIsSampler(int var0) {
      return GL33.glIsSampler(var0);
   }

   public static void glBindSampler(int var0, int var1) {
      GL33.glBindSampler(var0, var1);
   }

   public static void glSamplerParameteri(int var0, int var1, int var2) {
      GL33.glSamplerParameteri(var0, var1, var2);
   }

   public static void glSamplerParameterf(int var0, int var1, float var2) {
      GL33.glSamplerParameterf(var0, var1, var2);
   }

   public static void glSamplerParameter(int var0, int var1, IntBuffer var2) {
      GL33.glSamplerParameter(var0, var1, var2);
   }

   public static void glSamplerParameter(int var0, int var1, FloatBuffer var2) {
      GL33.glSamplerParameter(var0, var1, var2);
   }

   public static void glSamplerParameterI(int var0, int var1, IntBuffer var2) {
      GL33.glSamplerParameterI(var0, var1, var2);
   }

   public static void glSamplerParameterIu(int var0, int var1, IntBuffer var2) {
      GL33.glSamplerParameterIu(var0, var1, var2);
   }

   public static void glGetSamplerParameter(int var0, int var1, IntBuffer var2) {
      GL33.glGetSamplerParameter(var0, var1, var2);
   }

   public static int glGetSamplerParameteri(int var0, int var1) {
      return GL33.glGetSamplerParameteri(var0, var1);
   }

   public static void glGetSamplerParameter(int var0, int var1, FloatBuffer var2) {
      GL33.glGetSamplerParameter(var0, var1, var2);
   }

   public static float glGetSamplerParameterf(int var0, int var1) {
      return GL33.glGetSamplerParameterf(var0, var1);
   }

   public static void glGetSamplerParameterI(int var0, int var1, IntBuffer var2) {
      GL33.glGetSamplerParameterI(var0, var1, var2);
   }

   public static int glGetSamplerParameterIi(int var0, int var1) {
      return GL33.glGetSamplerParameterIi(var0, var1);
   }

   public static void glGetSamplerParameterIu(int var0, int var1, IntBuffer var2) {
      GL33.glGetSamplerParameterIu(var0, var1, var2);
   }

   public static int glGetSamplerParameterIui(int var0, int var1) {
      return GL33.glGetSamplerParameterIui(var0, var1);
   }
}
