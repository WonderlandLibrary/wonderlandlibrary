package org.lwjgl.opengl;

import org.lwjgl.PointerWrapperAbstract;

public final class ARBDebugOutputCallback extends PointerWrapperAbstract {
   private static final int GL_DEBUG_SEVERITY_HIGH_ARB = 37190;
   private static final int GL_DEBUG_SEVERITY_MEDIUM_ARB = 37191;
   private static final int GL_DEBUG_SEVERITY_LOW_ARB = 37192;
   private static final int GL_DEBUG_SOURCE_API_ARB = 33350;
   private static final int GL_DEBUG_SOURCE_WINDOW_SYSTEM_ARB = 33351;
   private static final int GL_DEBUG_SOURCE_SHADER_COMPILER_ARB = 33352;
   private static final int GL_DEBUG_SOURCE_THIRD_PARTY_ARB = 33353;
   private static final int GL_DEBUG_SOURCE_APPLICATION_ARB = 33354;
   private static final int GL_DEBUG_SOURCE_OTHER_ARB = 33355;
   private static final int GL_DEBUG_TYPE_ERROR_ARB = 33356;
   private static final int GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR_ARB = 33357;
   private static final int GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR_ARB = 33358;
   private static final int GL_DEBUG_TYPE_PORTABILITY_ARB = 33359;
   private static final int GL_DEBUG_TYPE_PERFORMANCE_ARB = 33360;
   private static final int GL_DEBUG_TYPE_OTHER_ARB = 33361;
   private static final long CALLBACK_POINTER;
   private final ARBDebugOutputCallback.Handler handler;

   public ARBDebugOutputCallback() {
      this(new ARBDebugOutputCallback.Handler() {
         public void handleMessage(int var1, int var2, int var3, int var4, String var5) {
            System.err.println("[LWJGL] ARB_debug_output message");
            System.err.println("\tID: " + var3);
            String var6;
            switch(var1) {
            case 33350:
               var6 = "API";
               break;
            case 33351:
               var6 = "WINDOW SYSTEM";
               break;
            case 33352:
               var6 = "SHADER COMPILER";
               break;
            case 33353:
               var6 = "THIRD PARTY";
               break;
            case 33354:
               var6 = "APPLICATION";
               break;
            case 33355:
               var6 = "OTHER";
               break;
            default:
               var6 = this.printUnknownToken(var1);
            }

            System.err.println("\tSource: " + var6);
            switch(var2) {
            case 33356:
               var6 = "ERROR";
               break;
            case 33357:
               var6 = "DEPRECATED BEHAVIOR";
               break;
            case 33358:
               var6 = "UNDEFINED BEHAVIOR";
               break;
            case 33359:
               var6 = "PORTABILITY";
               break;
            case 33360:
               var6 = "PERFORMANCE";
               break;
            case 33361:
               var6 = "OTHER";
               break;
            default:
               var6 = this.printUnknownToken(var2);
            }

            System.err.println("\tType: " + var6);
            switch(var4) {
            case 37190:
               var6 = "HIGH";
               break;
            case 37191:
               var6 = "MEDIUM";
               break;
            case 37192:
               var6 = "LOW";
               break;
            default:
               var6 = this.printUnknownToken(var4);
            }

            System.err.println("\tSeverity: " + var6);
            System.err.println("\tMessage: " + var5);
         }

         private String printUnknownToken(int var1) {
            return "Unknown (0x" + Integer.toHexString(var1).toUpperCase() + ")";
         }
      });
   }

   public ARBDebugOutputCallback(ARBDebugOutputCallback.Handler var1) {
      super(CALLBACK_POINTER);
      this.handler = var1;
   }

   ARBDebugOutputCallback.Handler getHandler() {
      return this.handler;
   }

   static {
      long var0 = 0L;

      try {
         var0 = (Long)Class.forName("org.lwjgl.opengl.CallbackUtil").getDeclaredMethod("getDebugOutputCallbackARB").invoke((Object)null);
      } catch (Exception var3) {
      }

      CALLBACK_POINTER = var0;
   }

   public interface Handler {
      void handleMessage(int var1, int var2, int var3, int var4, String var5);
   }
}
