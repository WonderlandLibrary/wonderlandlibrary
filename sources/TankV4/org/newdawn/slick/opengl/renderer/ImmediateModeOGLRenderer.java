package org.newdawn.slick.opengl.renderer;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL11;

public class ImmediateModeOGLRenderer implements SGL {
   private int width;
   private int height;
   private float[] current = new float[]{1.0F, 1.0F, 1.0F, 1.0F};
   protected float alphaScale = 1.0F;

   public void initDisplay(int var1, int var2) {
      this.width = var1;
      this.height = var2;
      String var3 = GL11.glGetString(7939);
      GL11.glEnable(3553);
      GL11.glShadeModel(7425);
      GL11.glDisable(2929);
      GL11.glDisable(2896);
      GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
      GL11.glClearDepth(1.0D);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glViewport(0, 0, var1, var2);
      GL11.glMatrixMode(5888);
   }

   public void enterOrtho(int var1, int var2) {
      GL11.glMatrixMode(5889);
      GL11.glLoadIdentity();
      GL11.glOrtho(0.0D, (double)this.width, (double)this.height, 0.0D, 1.0D, -1.0D);
      GL11.glMatrixMode(5888);
      GL11.glTranslatef((float)((this.width - var1) / 2), (float)((this.height - var2) / 2), 0.0F);
   }

   public void glBegin(int var1) {
      GL11.glBegin(var1);
   }

   public void glBindTexture(int var1, int var2) {
      GL11.glBindTexture(var1, var2);
   }

   public void glBlendFunc(int var1, int var2) {
      GL11.glBlendFunc(var1, var2);
   }

   public void glCallList(int var1) {
      GL11.glCallList(var1);
   }

   public void glClear(int var1) {
      GL11.glClear(var1);
   }

   public void glClearColor(float var1, float var2, float var3, float var4) {
      GL11.glClearColor(var1, var2, var3, var4);
   }

   public void glClipPlane(int var1, DoubleBuffer var2) {
      GL11.glClipPlane(var1, var2);
   }

   public void glColor4f(float var1, float var2, float var3, float var4) {
      var4 *= this.alphaScale;
      this.current = new float[]{var1, var2, var3, var4};
      GL11.glColor4f(var1, var2, var3, var4);
   }

   public void glColorMask(boolean var1, boolean var2, boolean var3, boolean var4) {
      GL11.glColorMask(var1, var2, var3, var4);
   }

   public void glCopyTexImage2D(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      GL11.glCopyTexImage2D(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public void glDeleteTextures(IntBuffer var1) {
      GL11.glDeleteTextures(var1);
   }

   public void glDisable(int var1) {
      GL11.glDisable(var1);
   }

   public void glEnable(int var1) {
      GL11.glEnable(var1);
   }

   public void glEnd() {
      GL11.glEnd();
   }

   public void glEndList() {
      GL11.glEndList();
   }

   public int glGenLists(int var1) {
      return GL11.glGenLists(var1);
   }

   public void glGetFloat(int var1, FloatBuffer var2) {
      GL11.glGetFloat(var1, var2);
   }

   public void glGetInteger(int var1, IntBuffer var2) {
      GL11.glGetInteger(var1, var2);
   }

   public void glGetTexImage(int var1, int var2, int var3, int var4, ByteBuffer var5) {
      GL11.glGetTexImage(var1, var2, var3, var4, var5);
   }

   public void glLineWidth(float var1) {
      GL11.glLineWidth(var1);
   }

   public void glLoadIdentity() {
      GL11.glLoadIdentity();
   }

   public void glNewList(int var1, int var2) {
      GL11.glNewList(var1, var2);
   }

   public void glPointSize(float var1) {
      GL11.glPointSize(var1);
   }

   public void glPopMatrix() {
      GL11.glPopMatrix();
   }

   public void glPushMatrix() {
      GL11.glPushMatrix();
   }

   public void glReadPixels(int var1, int var2, int var3, int var4, int var5, int var6, ByteBuffer var7) {
      GL11.glReadPixels(var1, var2, var3, var4, var5, var6, var7);
   }

   public void glRotatef(float var1, float var2, float var3, float var4) {
      GL11.glRotatef(var1, var2, var3, var4);
   }

   public void glScalef(float var1, float var2, float var3) {
      GL11.glScalef(var1, var2, var3);
   }

   public void glScissor(int var1, int var2, int var3, int var4) {
      GL11.glScissor(var1, var2, var3, var4);
   }

   public void glTexCoord2f(float var1, float var2) {
      GL11.glTexCoord2f(var1, var2);
   }

   public void glTexEnvi(int var1, int var2, int var3) {
      GL11.glTexEnvi(var1, var2, var3);
   }

   public void glTranslatef(float var1, float var2, float var3) {
      GL11.glTranslatef(var1, var2, var3);
   }

   public void glVertex2f(float var1, float var2) {
      GL11.glVertex2f(var1, var2);
   }

   public void glVertex3f(float var1, float var2, float var3) {
      GL11.glVertex3f(var1, var2, var3);
   }

   public void flush() {
   }

   public void glTexParameteri(int var1, int var2, int var3) {
      GL11.glTexParameteri(var1, var2, var3);
   }

   public float[] getCurrentColor() {
      return this.current;
   }

   public void glDeleteLists(int var1, int var2) {
      GL11.glDeleteLists(var1, var2);
   }

   public void glClearDepth(float var1) {
      GL11.glClearDepth((double)var1);
   }

   public void glDepthFunc(int var1) {
      GL11.glDepthFunc(var1);
   }

   public void glDepthMask(boolean var1) {
      GL11.glDepthMask(var1);
   }

   public void setGlobalAlphaScale(float var1) {
      this.alphaScale = var1;
   }

   public void glLoadMatrix(FloatBuffer var1) {
      GL11.glLoadMatrix(var1);
   }
}
