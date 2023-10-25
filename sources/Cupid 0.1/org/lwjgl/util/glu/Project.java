package org.lwjgl.util.glu;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class Project extends Util {
  private static final float[] IDENTITY_MATRIX = new float[] { 
      1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 
      1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F };
  
  private static final FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
  
  private static final FloatBuffer finalMatrix = BufferUtils.createFloatBuffer(16);
  
  private static final FloatBuffer tempMatrix = BufferUtils.createFloatBuffer(16);
  
  private static final float[] in = new float[4];
  
  private static final float[] out = new float[4];
  
  private static final float[] forward = new float[3];
  
  private static final float[] side = new float[3];
  
  private static final float[] up = new float[3];
  
  private static void __gluMakeIdentityf(FloatBuffer m) {
    int oldPos = m.position();
    m.put(IDENTITY_MATRIX);
    m.position(oldPos);
  }
  
  private static void __gluMultMatrixVecf(FloatBuffer m, float[] in, float[] out) {
    for (int i = 0; i < 4; i++)
      out[i] = in[0] * m.get(m.position() + 0 + i) + in[1] * m.get(m.position() + 4 + i) + in[2] * m.get(m.position() + 8 + i) + in[3] * m.get(m.position() + 12 + i); 
  }
  
  private static boolean __gluInvertMatrixf(FloatBuffer src, FloatBuffer inverse) {
    FloatBuffer temp = tempMatrix;
    int i;
    for (i = 0; i < 16; i++)
      temp.put(i, src.get(i + src.position())); 
    __gluMakeIdentityf(inverse);
    for (i = 0; i < 4; i++) {
      int swap = i;
      int j;
      for (j = i + 1; j < 4; j++) {
        if (Math.abs(temp.get(j * 4 + i)) > Math.abs(temp.get(i * 4 + i)))
          swap = j; 
      } 
      if (swap != i)
        for (int m = 0; m < 4; m++) {
          float f = temp.get(i * 4 + m);
          temp.put(i * 4 + m, temp.get(swap * 4 + m));
          temp.put(swap * 4 + m, f);
          f = inverse.get(i * 4 + m);
          inverse.put(i * 4 + m, inverse.get(swap * 4 + m));
          inverse.put(swap * 4 + m, f);
        }  
      if (temp.get(i * 4 + i) == 0.0F)
        return false; 
      float t = temp.get(i * 4 + i);
      int k;
      for (k = 0; k < 4; k++) {
        temp.put(i * 4 + k, temp.get(i * 4 + k) / t);
        inverse.put(i * 4 + k, inverse.get(i * 4 + k) / t);
      } 
      for (j = 0; j < 4; j++) {
        if (j != i) {
          t = temp.get(j * 4 + i);
          for (k = 0; k < 4; k++) {
            temp.put(j * 4 + k, temp.get(j * 4 + k) - temp.get(i * 4 + k) * t);
            inverse.put(j * 4 + k, inverse.get(j * 4 + k) - inverse.get(i * 4 + k) * t);
          } 
        } 
      } 
    } 
    return true;
  }
  
  private static void __gluMultMatricesf(FloatBuffer a, FloatBuffer b, FloatBuffer r) {
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++)
        r.put(r.position() + i * 4 + j, a.get(a.position() + i * 4 + 0) * b.get(b.position() + 0 + j) + a.get(a.position() + i * 4 + 1) * b.get(b.position() + 4 + j) + a.get(a.position() + i * 4 + 2) * b.get(b.position() + 8 + j) + a.get(a.position() + i * 4 + 3) * b.get(b.position() + 12 + j)); 
    } 
  }
  
  public static void gluPerspective(float fovy, float aspect, float zNear, float zFar) {
    float radians = fovy / 2.0F * 3.1415927F / 180.0F;
    float deltaZ = zFar - zNear;
    float sine = (float)Math.sin(radians);
    if (deltaZ == 0.0F || sine == 0.0F || aspect == 0.0F)
      return; 
    float cotangent = (float)Math.cos(radians) / sine;
    __gluMakeIdentityf(matrix);
    matrix.put(0, cotangent / aspect);
    matrix.put(5, cotangent);
    matrix.put(10, -(zFar + zNear) / deltaZ);
    matrix.put(11, -1.0F);
    matrix.put(14, -2.0F * zNear * zFar / deltaZ);
    matrix.put(15, 0.0F);
    GL11.glMultMatrix(matrix);
  }
  
  public static void gluLookAt(float eyex, float eyey, float eyez, float centerx, float centery, float centerz, float upx, float upy, float upz) {
    float[] forward = Project.forward;
    float[] side = Project.side;
    float[] up = Project.up;
    forward[0] = centerx - eyex;
    forward[1] = centery - eyey;
    forward[2] = centerz - eyez;
    up[0] = upx;
    up[1] = upy;
    up[2] = upz;
    normalize(forward);
    cross(forward, up, side);
    normalize(side);
    cross(side, forward, up);
    __gluMakeIdentityf(matrix);
    matrix.put(0, side[0]);
    matrix.put(4, side[1]);
    matrix.put(8, side[2]);
    matrix.put(1, up[0]);
    matrix.put(5, up[1]);
    matrix.put(9, up[2]);
    matrix.put(2, -forward[0]);
    matrix.put(6, -forward[1]);
    matrix.put(10, -forward[2]);
    GL11.glMultMatrix(matrix);
    GL11.glTranslatef(-eyex, -eyey, -eyez);
  }
  
  public static boolean gluProject(float objx, float objy, float objz, FloatBuffer modelMatrix, FloatBuffer projMatrix, IntBuffer viewport, FloatBuffer win_pos) {
    float[] in = Project.in;
    float[] out = Project.out;
    in[0] = objx;
    in[1] = objy;
    in[2] = objz;
    in[3] = 1.0F;
    __gluMultMatrixVecf(modelMatrix, in, out);
    __gluMultMatrixVecf(projMatrix, out, in);
    if (in[3] == 0.0D)
      return false; 
    in[3] = 1.0F / in[3] * 0.5F;
    in[0] = in[0] * in[3] + 0.5F;
    in[1] = in[1] * in[3] + 0.5F;
    in[2] = in[2] * in[3] + 0.5F;
    win_pos.put(0, in[0] * viewport.get(viewport.position() + 2) + viewport.get(viewport.position() + 0));
    win_pos.put(1, in[1] * viewport.get(viewport.position() + 3) + viewport.get(viewport.position() + 1));
    win_pos.put(2, in[2]);
    return true;
  }
  
  public static boolean gluUnProject(float winx, float winy, float winz, FloatBuffer modelMatrix, FloatBuffer projMatrix, IntBuffer viewport, FloatBuffer obj_pos) {
    float[] in = Project.in;
    float[] out = Project.out;
    __gluMultMatricesf(modelMatrix, projMatrix, finalMatrix);
    if (!__gluInvertMatrixf(finalMatrix, finalMatrix))
      return false; 
    in[0] = winx;
    in[1] = winy;
    in[2] = winz;
    in[3] = 1.0F;
    in[0] = (in[0] - viewport.get(viewport.position() + 0)) / viewport.get(viewport.position() + 2);
    in[1] = (in[1] - viewport.get(viewport.position() + 1)) / viewport.get(viewport.position() + 3);
    in[0] = in[0] * 2.0F - 1.0F;
    in[1] = in[1] * 2.0F - 1.0F;
    in[2] = in[2] * 2.0F - 1.0F;
    __gluMultMatrixVecf(finalMatrix, in, out);
    if (out[3] == 0.0D)
      return false; 
    out[3] = 1.0F / out[3];
    obj_pos.put(obj_pos.position() + 0, out[0] * out[3]);
    obj_pos.put(obj_pos.position() + 1, out[1] * out[3]);
    obj_pos.put(obj_pos.position() + 2, out[2] * out[3]);
    return true;
  }
  
  public static void gluPickMatrix(float x, float y, float deltaX, float deltaY, IntBuffer viewport) {
    if (deltaX <= 0.0F || deltaY <= 0.0F)
      return; 
    GL11.glTranslatef((viewport.get(viewport.position() + 2) - 2.0F * (x - viewport.get(viewport.position() + 0))) / deltaX, (viewport.get(viewport.position() + 3) - 2.0F * (y - viewport.get(viewport.position() + 1))) / deltaY, 0.0F);
    GL11.glScalef(viewport.get(viewport.position() + 2) / deltaX, viewport.get(viewport.position() + 3) / deltaY, 1.0F);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjg\\util\glu\Project.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */