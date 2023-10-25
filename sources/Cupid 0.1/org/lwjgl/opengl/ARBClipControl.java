package org.lwjgl.opengl;

public final class ARBClipControl {
  public static final int GL_LOWER_LEFT = 36001;
  
  public static final int GL_UPPER_LEFT = 36002;
  
  public static final int GL_NEGATIVE_ONE_TO_ONE = 37726;
  
  public static final int GL_ZERO_TO_ONE = 37727;
  
  public static final int GL_CLIP_ORIGIN = 37724;
  
  public static final int GL_CLIP_DEPTH_MODE = 37725;
  
  public static void glClipControl(int origin, int depth) {
    GL45.glClipControl(origin, depth);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\ARBClipControl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */