package org.lwjgl.opengl;

public final class ARBTextureView {
  public static final int GL_TEXTURE_VIEW_MIN_LEVEL = 33499;
  
  public static final int GL_TEXTURE_VIEW_NUM_LEVELS = 33500;
  
  public static final int GL_TEXTURE_VIEW_MIN_LAYER = 33501;
  
  public static final int GL_TEXTURE_VIEW_NUM_LAYERS = 33502;
  
  public static final int GL_TEXTURE_IMMUTABLE_LEVELS = 33503;
  
  public static void glTextureView(int texture, int target, int origtexture, int internalformat, int minlevel, int numlevels, int minlayer, int numlayers) {
    GL43.glTextureView(texture, target, origtexture, internalformat, minlevel, numlevels, minlayer, numlayers);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\ARBTextureView.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */