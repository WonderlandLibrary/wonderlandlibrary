package me.theresa.fontRenderer.font.opengl.renderer;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public interface SGL {
   int GL_TEXTURE_2D = 3553;
   int GL_RGBA = 6408;
   int GL_RGB = 6407;
   int GL_UNSIGNED_BYTE = 5121;
   int GL_LINEAR = 9729;
   int GL_NEAREST = 9728;
   int GL_TEXTURE_MIN_FILTER = 10241;
   int GL_TEXTURE_MAG_FILTER = 10240;
   int GL_POINT_SMOOTH = 2832;
   int GL_POLYGON_SMOOTH = 2881;
   int GL_LINE_SMOOTH = 2848;
   int GL_SCISSOR_TEST = 3089;
   int GL_MODULATE = 8448;
   int GL_TEXTURE_ENV = 8960;
   int GL_TEXTURE_ENV_MODE = 8704;
   int GL_QUADS = 7;
   int GL_POINTS = 0;
   int GL_LINES = 1;
   int GL_LINE_STRIP = 3;
   int GL_TRIANGLES = 4;
   int GL_TRIANGLE_FAN = 6;
   int GL_SRC_ALPHA = 770;
   int GL_ONE = 1;
   int GL_ONE_MINUS_DST_ALPHA = 773;
   int GL_DST_ALPHA = 772;
   int GL_ONE_MINUS_SRC_ALPHA = 771;
   int GL_COMPILE = 4864;
   int GL_MAX_TEXTURE_SIZE = 3379;
   int GL_COLOR_BUFFER_BIT = 16384;
   int GL_DEPTH_BUFFER_BIT = 256;
   int GL_BLEND = 3042;
   int GL_COLOR_CLEAR_VALUE = 3106;
   int GL_LINE_WIDTH = 2849;
   int GL_CLIP_PLANE0 = 12288;
   int GL_CLIP_PLANE1 = 12289;
   int GL_CLIP_PLANE2 = 12290;
   int GL_CLIP_PLANE3 = 12291;
   int GL_COMPILE_AND_EXECUTE = 4865;
   int GL_RGBA8 = 6408;
   int GL_RGBA16 = 32859;
   int GL_BGRA = 32993;
   int GL_MIRROR_CLAMP_TO_EDGE_EXT = 34627;
   int GL_TEXTURE_WRAP_S = 10242;
   int GL_TEXTURE_WRAP_T = 10243;
   int GL_CLAMP = 10496;
   int GL_COLOR_SUM_EXT = 33880;
   int GL_ALWAYS = 519;
   int GL_DEPTH_TEST = 2929;
   int GL_NOTEQUAL = 517;
   int GL_EQUAL = 514;
   int GL_SRC_COLOR = 768;
   int GL_ONE_MINUS_SRC_COLOR = 769;
   int GL_MODELVIEW_MATRIX = 2982;

   void flush();

   void initDisplay(int var1, int var2);

   void enterOrtho(int var1, int var2);

   void glClearColor(float var1, float var2, float var3, float var4);

   void glClipPlane(int var1, DoubleBuffer var2);

   void glScissor(int var1, int var2, int var3, int var4);

   void glLineWidth(float var1);

   void glClear(int var1);

   void glColorMask(boolean var1, boolean var2, boolean var3, boolean var4);

   void glLoadIdentity();

   void glGetInteger(int var1, IntBuffer var2);

   void glGetFloat(int var1, FloatBuffer var2);

   void glEnable(int var1);

   void glDisable(int var1);

   void glBindTexture(int var1, int var2);

   void glGetTexImage(int var1, int var2, int var3, int var4, ByteBuffer var5);

   void glDeleteTextures(IntBuffer var1);

   void glColor4f(float var1, float var2, float var3, float var4);

   void glTexCoord2f(float var1, float var2);

   void glVertex3f(float var1, float var2, float var3);

   void glVertex2f(float var1, float var2);

   void glRotatef(float var1, float var2, float var3, float var4);

   void glTranslatef(float var1, float var2, float var3);

   void glBegin(int var1);

   void glEnd();

   void glTexEnvi(int var1, int var2, int var3);

   void glPointSize(float var1);

   void glScalef(float var1, float var2, float var3);

   void glPushMatrix();

   void glPopMatrix();

   void glBlendFunc(int var1, int var2);

   int glGenLists(int var1);

   void glNewList(int var1, int var2);

   void glEndList();

   void glCallList(int var1);

   void glCopyTexImage2D(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8);

   void glReadPixels(int var1, int var2, int var3, int var4, int var5, int var6, ByteBuffer var7);

   void glTexParameteri(int var1, int var2, int var3);

   float[] getCurrentColor();

   void glDeleteLists(int var1, int var2);

   void glDepthMask(boolean var1);

   void glClearDepth(float var1);

   void glDepthFunc(int var1);

   void setGlobalAlphaScale(float var1);

   void glLoadMatrix(FloatBuffer var1);

   void glGenTextures(IntBuffer var1);

   void glGetError();

   void glTexImage2D(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, ByteBuffer var9);

   void glTexSubImage2D(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, ByteBuffer var9);

   boolean canTextureMirrorClamp();

   boolean canSecondaryColor();

   void glSecondaryColor3ubEXT(byte var1, byte var2, byte var3);
}
