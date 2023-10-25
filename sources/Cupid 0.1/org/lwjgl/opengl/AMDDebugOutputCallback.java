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
  
  private final Handler handler;
  
  static {
    long pointer = 0L;
    try {
      pointer = ((Long)Class.forName("org.lwjgl.opengl.CallbackUtil").getDeclaredMethod("getDebugOutputCallbackAMD", new Class[0]).invoke(null, new Object[0])).longValue();
    } catch (Exception e) {}
    CALLBACK_POINTER = pointer;
  }
  
  public AMDDebugOutputCallback() {
    this(new Handler() {
          public void handleMessage(int id, int category, int severity, String message) {
            String description;
            System.err.println("[LWJGL] AMD_debug_output message");
            System.err.println("\tID: " + id);
            switch (category) {
              case 37193:
                description = "API ERROR";
                break;
              case 37194:
                description = "WINDOW SYSTEM";
                break;
              case 37195:
                description = "DEPRECATION";
                break;
              case 37196:
                description = "UNDEFINED BEHAVIOR";
                break;
              case 37197:
                description = "PERFORMANCE";
                break;
              case 37198:
                description = "SHADER COMPILER";
                break;
              case 37199:
                description = "APPLICATION";
                break;
              case 37200:
                description = "OTHER";
                break;
              default:
                description = printUnknownToken(category);
                break;
            } 
            System.err.println("\tCategory: " + description);
            switch (severity) {
              case 37190:
                description = "HIGH";
                break;
              case 37191:
                description = "MEDIUM";
                break;
              case 37192:
                description = "LOW";
                break;
              default:
                description = printUnknownToken(severity);
                break;
            } 
            System.err.println("\tSeverity: " + description);
            System.err.println("\tMessage: " + message);
          }
          
          private String printUnknownToken(int token) {
            return "Unknown (0x" + Integer.toHexString(token).toUpperCase() + ")";
          }
        });
  }
  
  public static interface Handler {
    void handleMessage(int param1Int1, int param1Int2, int param1Int3, String param1String);
  }
  
  public AMDDebugOutputCallback(Handler handler) {
    super(CALLBACK_POINTER);
    this.handler = handler;
  }
  
  Handler getHandler() {
    return this.handler;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\AMDDebugOutputCallback.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */