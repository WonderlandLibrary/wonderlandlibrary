package org.lwjgl.opengl;

import org.lwjgl.PointerWrapperAbstract;

public final class KHRDebugCallback extends PointerWrapperAbstract {
  private static final int GL_DEBUG_SEVERITY_HIGH = 37190;
  
  private static final int GL_DEBUG_SEVERITY_MEDIUM = 37191;
  
  private static final int GL_DEBUG_SEVERITY_LOW = 37192;
  
  private static final int GL_DEBUG_SEVERITY_NOTIFICATION = 33387;
  
  private static final int GL_DEBUG_SOURCE_API = 33350;
  
  private static final int GL_DEBUG_SOURCE_WINDOW_SYSTEM = 33351;
  
  private static final int GL_DEBUG_SOURCE_SHADER_COMPILER = 33352;
  
  private static final int GL_DEBUG_SOURCE_THIRD_PARTY = 33353;
  
  private static final int GL_DEBUG_SOURCE_APPLICATION = 33354;
  
  private static final int GL_DEBUG_SOURCE_OTHER = 33355;
  
  private static final int GL_DEBUG_TYPE_ERROR = 33356;
  
  private static final int GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR = 33357;
  
  private static final int GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR = 33358;
  
  private static final int GL_DEBUG_TYPE_PORTABILITY = 33359;
  
  private static final int GL_DEBUG_TYPE_PERFORMANCE = 33360;
  
  private static final int GL_DEBUG_TYPE_OTHER = 33361;
  
  private static final int GL_DEBUG_TYPE_MARKER = 33384;
  
  private static final long CALLBACK_POINTER;
  
  private final Handler handler;
  
  static {
    long pointer = 0L;
    try {
      pointer = ((Long)Class.forName("org.lwjgl.opengl.CallbackUtil").getDeclaredMethod("getDebugCallbackKHR", new Class[0]).invoke(null, new Object[0])).longValue();
    } catch (Exception e) {}
    CALLBACK_POINTER = pointer;
  }
  
  public KHRDebugCallback() {
    this(new Handler() {
          public void handleMessage(int source, int type, int id, int severity, String message) {
            String description;
            System.err.println("[LWJGL] KHR_debug message");
            System.err.println("\tID: " + id);
            switch (source) {
              case 33350:
                description = "API";
                break;
              case 33351:
                description = "WINDOW SYSTEM";
                break;
              case 33352:
                description = "SHADER COMPILER";
                break;
              case 33353:
                description = "THIRD PARTY";
                break;
              case 33354:
                description = "APPLICATION";
                break;
              case 33355:
                description = "OTHER";
                break;
              default:
                description = printUnknownToken(source);
                break;
            } 
            System.err.println("\tSource: " + description);
            switch (type) {
              case 33356:
                description = "ERROR";
                break;
              case 33357:
                description = "DEPRECATED BEHAVIOR";
                break;
              case 33358:
                description = "UNDEFINED BEHAVIOR";
                break;
              case 33359:
                description = "PORTABILITY";
                break;
              case 33360:
                description = "PERFORMANCE";
                break;
              case 33361:
                description = "OTHER";
                break;
              case 33384:
                description = "MARKER";
                break;
              default:
                description = printUnknownToken(type);
                break;
            } 
            System.err.println("\tType: " + description);
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
              case 33387:
                description = "NOTIFICATION";
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
    void handleMessage(int param1Int1, int param1Int2, int param1Int3, int param1Int4, String param1String);
  }
  
  public KHRDebugCallback(Handler handler) {
    super(CALLBACK_POINTER);
    this.handler = handler;
  }
  
  Handler getHandler() {
    return this.handler;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\KHRDebugCallback.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */