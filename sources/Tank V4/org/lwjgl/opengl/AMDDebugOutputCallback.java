package org.lwjgl.opengl;

import org.lwjgl.PointerWrapperAbstract;

public final class AMDDebugOutputCallback extends PointerWrapperAbstract {
   private static final int GL_DEBUG_SEVERITY_HIGH_AMD = 37190;
   private static final int GL_DEBUG_SEVERITY_MEDIUM_AMD = 37191;
   private static final int GL_DEBUG_SEVERITY_LOW_AMD = 37192;
   private static final int GL_DEBUG_CATEGORY_API_ERROR_AMD = 37193;
   private static final int GL_DEBUG_CATEGORY_WINDOW_SYSTEM_AMD = 37194;
   private static final int GL_DEBUG_CATEGORY_DEPRECATION_AMD = 37195;
   private static final int GL_DEBUG_CATEGORY_UNDEFINED_BEHAVIOR_AMD = 37196;
   private static final int GL_DEBUG_CATEGORY_PERFORMANCE_AMD = 37197;
   private static final int GL_DEBUG_CATEGORY_SHADER_COMPILER_AMD = 37198;
   private static final int GL_DEBUG_CATEGORY_APPLICATION_AMD = 37199;
   private static final int GL_DEBUG_CATEGORY_OTHER_AMD = 37200;
   private static final long CALLBACK_POINTER;
   private final AMDDebugOutputCallback.Handler handler;

   public AMDDebugOutputCallback() {
      this(new AMDDebugOutputCallback.Handler() {
         public void handleMessage(int var1, int var2, int var3, String var4) {
            System.err.println("[LWJGL] AMD_debug_output message");
            System.err.println("\tID: " + var1);
            String var5;
            switch(var2) {
            case 37193:
               var5 = "API ERROR";
               break;
            case 37194:
               var5 = "WINDOW SYSTEM";
               break;
            case 37195:
               var5 = "DEPRECATION";
               break;
            case 37196:
               var5 = "UNDEFINED BEHAVIOR";
               break;
            case 37197:
               var5 = "PERFORMANCE";
               break;
            case 37198:
               var5 = "SHADER COMPILER";
               break;
            case 37199:
               var5 = "APPLICATION";
               break;
            case 37200:
               var5 = "OTHER";
               break;
            default:
               var5 = this.printUnknownToken(var2);
            }

            System.err.println("\tCategory: " + var5);
            switch(var3) {
            case 37190:
               var5 = "HIGH";
               break;
            case 37191:
               var5 = "MEDIUM";
               break;
            case 37192:
               var5 = "LOW";
               break;
            default:
               var5 = this.printUnknownToken(var3);
            }

            System.err.println("\tSeverity: " + var5);
            System.err.println("\tMessage: " + var4);
         }

         private String printUnknownToken(int var1) {
            return "Unknown (0x" + Integer.toHexString(var1).toUpperCase() + ")";
         }
      });
   }

   public AMDDebugOutputCallback(AMDDebugOutputCallback.Handler var1) {
      super(CALLBACK_POINTER);
      this.handler = var1;
   }

   AMDDebugOutputCallback.Handler getHandler() {
      return this.handler;
   }

   static {
      long var0 = 0L;

      try {
         var0 = (Long)Class.forName("org.lwjgl.opengl.CallbackUtil").getDeclaredMethod("getDebugOutputCallbackAMD").invoke((Object)null);
      } catch (Exception var3) {
      }

      CALLBACK_POINTER = var0;
   }

   public interface Handler {
      void handleMessage(int var1, int var2, int var3, String var4);
   }
}
