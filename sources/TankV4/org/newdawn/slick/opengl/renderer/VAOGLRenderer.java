package org.newdawn.slick.opengl.renderer;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class VAOGLRenderer extends ImmediateModeOGLRenderer {
   private static final int TOLERANCE = 20;
   public static final int NONE = -1;
   public static final int MAX_VERTS = 5000;
   private int currentType = -1;
   private float[] color = new float[]{1.0F, 1.0F, 1.0F, 1.0F};
   private float[] tex = new float[]{0.0F, 0.0F};
   private int vertIndex;
   private float[] verts = new float[15000];
   private float[] cols = new float[20000];
   private float[] texs = new float[15000];
   private FloatBuffer vertices = BufferUtils.createFloatBuffer(15000);
   private FloatBuffer colors = BufferUtils.createFloatBuffer(20000);
   private FloatBuffer textures = BufferUtils.createFloatBuffer(10000);
   private int listMode = 0;

   public void initDisplay(int var1, int var2) {
      super.initDisplay(var1, var2);
      this.startBuffer();
      GL11.glEnableClientState(32884);
      GL11.glEnableClientState(32888);
      GL11.glEnableClientState(32886);
   }

   private void startBuffer() {
      this.vertIndex = 0;
   }

   private void flushBuffer() {
      if (this.vertIndex != 0) {
         if (this.currentType != -1) {
            if (this.vertIndex >= 20) {
               this.vertices.clear();
               this.colors.clear();
               this.textures.clear();
               this.vertices.put(this.verts, 0, this.vertIndex * 3);
               this.colors.put(this.cols, 0, this.vertIndex * 4);
               this.textures.put(this.texs, 0, this.vertIndex * 2);
               this.vertices.flip();
               this.colors.flip();
               this.textures.flip();
               GL11.glVertexPointer(3, 0, (FloatBuffer)this.vertices);
               GL11.glColorPointer(4, 0, (FloatBuffer)this.colors);
               GL11.glTexCoordPointer(2, 0, (FloatBuffer)this.textures);
               GL11.glDrawArrays(this.currentType, 0, this.vertIndex);
               this.currentType = -1;
            } else {
               GL11.glBegin(this.currentType);

               for(int var1 = 0; var1 < this.vertIndex; ++var1) {
                  GL11.glColor4f(this.cols[var1 * 4 + 0], this.cols[var1 * 4 + 1], this.cols[var1 * 4 + 2], this.cols[var1 * 4 + 3]);
                  GL11.glTexCoord2f(this.texs[var1 * 2 + 0], this.texs[var1 * 2 + 1]);
                  GL11.glVertex3f(this.verts[var1 * 3 + 0], this.verts[var1 * 3 + 1], this.verts[var1 * 3 + 2]);
               }

               GL11.glEnd();
               this.currentType = -1;
            }
         }
      }
   }

   private void applyBuffer() {
      if (this.listMode <= 0) {
         if (this.vertIndex != 0) {
            this.flushBuffer();
            this.startBuffer();
         }

         super.glColor4f(this.color[0], this.color[1], this.color[2], this.color[3]);
      }
   }

   public void flush() {
      super.flush();
      this.applyBuffer();
   }

   public void glBegin(int var1) {
      if (this.listMode > 0) {
         super.glBegin(var1);
      } else {
         if (this.currentType != var1) {
            this.applyBuffer();
            this.currentType = var1;
         }

      }
   }

   public void glColor4f(float var1, float var2, float var3, float var4) {
      var4 *= this.alphaScale;
      this.color[0] = var1;
      this.color[1] = var2;
      this.color[2] = var3;
      this.color[3] = var4;
      if (this.listMode > 0) {
         super.glColor4f(var1, var2, var3, var4);
      }
   }

   public void glEnd() {
      if (this.listMode > 0) {
         super.glEnd();
      }
   }

   public void glTexCoord2f(float var1, float var2) {
      if (this.listMode > 0) {
         super.glTexCoord2f(var1, var2);
      } else {
         this.tex[0] = var1;
         this.tex[1] = var2;
      }
   }

   public void glVertex2f(float var1, float var2) {
      if (this.listMode > 0) {
         super.glVertex2f(var1, var2);
      } else {
         this.glVertex3f(var1, var2, 0.0F);
      }
   }

   public void glVertex3f(float var1, float var2, float var3) {
      if (this.listMode > 0) {
         super.glVertex3f(var1, var2, var3);
      } else {
         this.verts[this.vertIndex * 3 + 0] = var1;
         this.verts[this.vertIndex * 3 + 1] = var2;
         this.verts[this.vertIndex * 3 + 2] = var3;
         this.cols[this.vertIndex * 4 + 0] = this.color[0];
         this.cols[this.vertIndex * 4 + 1] = this.color[1];
         this.cols[this.vertIndex * 4 + 2] = this.color[2];
         this.cols[this.vertIndex * 4 + 3] = this.color[3];
         this.texs[this.vertIndex * 2 + 0] = this.tex[0];
         this.texs[this.vertIndex * 2 + 1] = this.tex[1];
         ++this.vertIndex;
         if (this.vertIndex > 4950) {
            int var10001 = this.vertIndex;
            if (this.currentType == 0) {
               int var4 = this.currentType;
               this.applyBuffer();
               this.currentType = var4;
            }
         }

      }
   }

   public void glBindTexture(int var1, int var2) {
      this.applyBuffer();
      super.glBindTexture(var1, var2);
   }

   public void glBlendFunc(int var1, int var2) {
      this.applyBuffer();
      super.glBlendFunc(var1, var2);
   }

   public void glCallList(int var1) {
      this.applyBuffer();
      super.glCallList(var1);
   }

   public void glClear(int var1) {
      this.applyBuffer();
      super.glClear(var1);
   }

   public void glClipPlane(int var1, DoubleBuffer var2) {
      this.applyBuffer();
      super.glClipPlane(var1, var2);
   }

   public void glColorMask(boolean var1, boolean var2, boolean var3, boolean var4) {
      this.applyBuffer();
      super.glColorMask(var1, var2, var3, var4);
   }

   public void glDisable(int var1) {
      this.applyBuffer();
      super.glDisable(var1);
   }

   public void glEnable(int var1) {
      this.applyBuffer();
      super.glEnable(var1);
   }

   public void glLineWidth(float var1) {
      this.applyBuffer();
      super.glLineWidth(var1);
   }

   public void glPointSize(float var1) {
      this.applyBuffer();
      super.glPointSize(var1);
   }

   public void glPopMatrix() {
      this.applyBuffer();
      super.glPopMatrix();
   }

   public void glPushMatrix() {
      this.applyBuffer();
      super.glPushMatrix();
   }

   public void glRotatef(float var1, float var2, float var3, float var4) {
      this.applyBuffer();
      super.glRotatef(var1, var2, var3, var4);
   }

   public void glScalef(float var1, float var2, float var3) {
      this.applyBuffer();
      super.glScalef(var1, var2, var3);
   }

   public void glScissor(int var1, int var2, int var3, int var4) {
      this.applyBuffer();
      super.glScissor(var1, var2, var3, var4);
   }

   public void glTexEnvi(int var1, int var2, int var3) {
      this.applyBuffer();
      super.glTexEnvi(var1, var2, var3);
   }

   public void glTranslatef(float var1, float var2, float var3) {
      this.applyBuffer();
      super.glTranslatef(var1, var2, var3);
   }

   public void glEndList() {
      --this.listMode;
      super.glEndList();
   }

   public void glNewList(int var1, int var2) {
      ++this.listMode;
      super.glNewList(var1, var2);
   }

   public float[] getCurrentColor() {
      return this.color;
   }

   public void glLoadMatrix(FloatBuffer var1) {
      this.flushBuffer();
      super.glLoadMatrix(var1);
   }
}
