package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.PointerWrapper;

public final class KHRDebug {
   public static final int GL_DEBUG_OUTPUT = 37600;
   public static final int GL_DEBUG_OUTPUT_SYNCHRONOUS = 33346;
   public static final int GL_CONTEXT_FLAG_DEBUG_BIT = 2;
   public static final int GL_MAX_DEBUG_MESSAGE_LENGTH = 37187;
   public static final int GL_MAX_DEBUG_LOGGED_MESSAGES = 37188;
   public static final int GL_DEBUG_LOGGED_MESSAGES = 37189;
   public static final int GL_DEBUG_NEXT_LOGGED_MESSAGE_LENGTH = 33347;
   public static final int GL_MAX_DEBUG_GROUP_STACK_DEPTH = 33388;
   public static final int GL_DEBUG_GROUP_STACK_DEPTH = 33389;
   public static final int GL_MAX_LABEL_LENGTH = 33512;
   public static final int GL_DEBUG_CALLBACK_FUNCTION = 33348;
   public static final int GL_DEBUG_CALLBACK_USER_PARAM = 33349;
   public static final int GL_DEBUG_SOURCE_API = 33350;
   public static final int GL_DEBUG_SOURCE_WINDOW_SYSTEM = 33351;
   public static final int GL_DEBUG_SOURCE_SHADER_COMPILER = 33352;
   public static final int GL_DEBUG_SOURCE_THIRD_PARTY = 33353;
   public static final int GL_DEBUG_SOURCE_APPLICATION = 33354;
   public static final int GL_DEBUG_SOURCE_OTHER = 33355;
   public static final int GL_DEBUG_TYPE_ERROR = 33356;
   public static final int GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR = 33357;
   public static final int GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR = 33358;
   public static final int GL_DEBUG_TYPE_PORTABILITY = 33359;
   public static final int GL_DEBUG_TYPE_PERFORMANCE = 33360;
   public static final int GL_DEBUG_TYPE_OTHER = 33361;
   public static final int GL_DEBUG_TYPE_MARKER = 33384;
   public static final int GL_DEBUG_TYPE_PUSH_GROUP = 33385;
   public static final int GL_DEBUG_TYPE_POP_GROUP = 33386;
   public static final int GL_DEBUG_SEVERITY_HIGH = 37190;
   public static final int GL_DEBUG_SEVERITY_MEDIUM = 37191;
   public static final int GL_DEBUG_SEVERITY_LOW = 37192;
   public static final int GL_DEBUG_SEVERITY_NOTIFICATION = 33387;
   public static final int GL_STACK_UNDERFLOW = 1284;
   public static final int GL_STACK_OVERFLOW = 1283;
   public static final int GL_BUFFER = 33504;
   public static final int GL_SHADER = 33505;
   public static final int GL_PROGRAM = 33506;
   public static final int GL_QUERY = 33507;
   public static final int GL_PROGRAM_PIPELINE = 33508;
   public static final int GL_SAMPLER = 33510;
   public static final int GL_DISPLAY_LIST = 33511;

   private KHRDebug() {
   }

   public static void glDebugMessageControl(int var0, int var1, int var2, IntBuffer var3, boolean var4) {
      GL43.glDebugMessageControl(var0, var1, var2, var3, var4);
   }

   public static void glDebugMessageInsert(int var0, int var1, int var2, int var3, ByteBuffer var4) {
      GL43.glDebugMessageInsert(var0, var1, var2, var3, var4);
   }

   public static void glDebugMessageInsert(int var0, int var1, int var2, int var3, CharSequence var4) {
      GL43.glDebugMessageInsert(var0, var1, var2, var3, var4);
   }

   public static void glDebugMessageCallback(KHRDebugCallback var0) {
      GL43.glDebugMessageCallback(var0);
   }

   public static int glGetDebugMessageLog(int var0, IntBuffer var1, IntBuffer var2, IntBuffer var3, IntBuffer var4, IntBuffer var5, ByteBuffer var6) {
      return GL43.glGetDebugMessageLog(var0, var1, var2, var3, var4, var5, var6);
   }

   public static void glPushDebugGroup(int var0, int var1, ByteBuffer var2) {
      GL43.glPushDebugGroup(var0, var1, var2);
   }

   public static void glPushDebugGroup(int var0, int var1, CharSequence var2) {
      GL43.glPushDebugGroup(var0, var1, var2);
   }

   public static void glPopDebugGroup() {
      GL43.glPopDebugGroup();
   }

   public static void glObjectLabel(int var0, int var1, ByteBuffer var2) {
      GL43.glObjectLabel(var0, var1, var2);
   }

   public static void glObjectLabel(int var0, int var1, CharSequence var2) {
      GL43.glObjectLabel(var0, var1, var2);
   }

   public static void glGetObjectLabel(int var0, int var1, IntBuffer var2, ByteBuffer var3) {
      GL43.glGetObjectLabel(var0, var1, var2, var3);
   }

   public static String glGetObjectLabel(int var0, int var1, int var2) {
      return GL43.glGetObjectLabel(var0, var1, var2);
   }

   public static void glObjectPtrLabel(PointerWrapper var0, ByteBuffer var1) {
      GL43.glObjectPtrLabel(var0, var1);
   }

   public static void glObjectPtrLabel(PointerWrapper var0, CharSequence var1) {
      GL43.glObjectPtrLabel(var0, var1);
   }

   public static void glGetObjectPtrLabel(PointerWrapper var0, IntBuffer var1, ByteBuffer var2) {
      GL43.glGetObjectPtrLabel(var0, var1, var2);
   }

   public static String glGetObjectPtrLabel(PointerWrapper var0, int var1) {
      return GL43.glGetObjectPtrLabel(var0, var1);
   }
}
