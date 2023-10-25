package org.lwjgl.opengl;

import java.nio.IntBuffer;

public final class ARBInternalformatQuery {
  public static final int GL_NUM_SAMPLE_COUNTS = 37760;
  
  public static void glGetInternalformat(int target, int internalformat, int pname, IntBuffer params) {
    GL42.glGetInternalformat(target, internalformat, pname, params);
  }
  
  public static int glGetInternalformat(int target, int internalformat, int pname) {
    return GL42.glGetInternalformat(target, internalformat, pname);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\ARBInternalformatQuery.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */