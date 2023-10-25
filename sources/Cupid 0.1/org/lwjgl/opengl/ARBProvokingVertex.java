package org.lwjgl.opengl;

public final class ARBProvokingVertex {
  public static final int GL_FIRST_VERTEX_CONVENTION = 36429;
  
  public static final int GL_LAST_VERTEX_CONVENTION = 36430;
  
  public static final int GL_PROVOKING_VERTEX = 36431;
  
  public static final int GL_QUADS_FOLLOW_PROVOKING_VERTEX_CONVENTION = 36428;
  
  public static void glProvokingVertex(int mode) {
    GL32.glProvokingVertex(mode);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\ARBProvokingVertex.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */