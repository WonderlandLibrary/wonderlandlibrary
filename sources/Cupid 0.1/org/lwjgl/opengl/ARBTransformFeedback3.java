package org.lwjgl.opengl;

import java.nio.IntBuffer;

public final class ARBTransformFeedback3 {
  public static final int GL_MAX_TRANSFORM_FEEDBACK_BUFFERS = 36464;
  
  public static final int GL_MAX_VERTEX_STREAMS = 36465;
  
  public static void glDrawTransformFeedbackStream(int mode, int id, int stream) {
    GL40.glDrawTransformFeedbackStream(mode, id, stream);
  }
  
  public static void glBeginQueryIndexed(int target, int index, int id) {
    GL40.glBeginQueryIndexed(target, index, id);
  }
  
  public static void glEndQueryIndexed(int target, int index) {
    GL40.glEndQueryIndexed(target, index);
  }
  
  public static void glGetQueryIndexed(int target, int index, int pname, IntBuffer params) {
    GL40.glGetQueryIndexed(target, index, pname, params);
  }
  
  public static int glGetQueryIndexedi(int target, int index, int pname) {
    return GL40.glGetQueryIndexedi(target, index, pname);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\ARBTransformFeedback3.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */