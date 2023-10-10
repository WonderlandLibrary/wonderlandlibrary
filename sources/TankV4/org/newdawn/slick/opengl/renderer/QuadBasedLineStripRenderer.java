package org.newdawn.slick.opengl.renderer;

public class QuadBasedLineStripRenderer implements LineStripRenderer {
   private static SGL GL = Renderer.get();
   public static int MAX_POINTS = 10000;
   private boolean antialias;
   private float width = 1.0F;
   private float[] points;
   private float[] colours;
   private int pts;
   private int cpt;
   private DefaultLineStripRenderer def = new DefaultLineStripRenderer();
   private boolean renderHalf;
   private boolean lineCaps = false;

   public QuadBasedLineStripRenderer() {
      this.points = new float[MAX_POINTS * 2];
      this.colours = new float[MAX_POINTS * 4];
   }

   public void setLineCaps(boolean var1) {
      this.lineCaps = var1;
   }

   public void start() {
      if (this.width == 1.0F) {
         this.def.start();
      } else {
         this.pts = 0;
         this.cpt = 0;
         GL.flush();
         float[] var1 = GL.getCurrentColor();
         this.color(var1[0], var1[1], var1[2], var1[3]);
      }
   }

   public void end() {
      if (this.width == 1.0F) {
         this.def.end();
      } else {
         this.renderLines(this.points, this.pts);
      }
   }

   public void vertex(float var1, float var2) {
      if (this.width == 1.0F) {
         this.def.vertex(var1, var2);
      } else {
         this.points[this.pts * 2] = var1;
         this.points[this.pts * 2 + 1] = var2;
         ++this.pts;
         int var3 = this.pts - 1;
         this.color(this.colours[var3 * 4], this.colours[var3 * 4 + 1], this.colours[var3 * 4 + 2], this.colours[var3 * 4 + 3]);
      }
   }

   public void setWidth(float var1) {
      this.width = var1;
   }

   public void setAntiAlias(boolean var1) {
      this.def.setAntiAlias(var1);
      this.antialias = var1;
   }

   public void renderLines(float[] var1, int var2) {
      if (this.antialias) {
         GL.glEnable(2881);
         this.renderLinesImpl(var1, var2, this.width + 1.0F);
      }

      GL.glDisable(2881);
      this.renderLinesImpl(var1, var2, this.width);
      if (this.antialias) {
         GL.glEnable(2881);
      }

   }

   public void renderLinesImpl(float[] var1, int var2, float var3) {
      float var4 = var3 / 2.0F;
      float var5 = 0.0F;
      float var6 = 0.0F;
      float var7 = 0.0F;
      float var8 = 0.0F;
      GL.glBegin(7);

      float var14;
      for(int var9 = 0; var9 < var2 + 1; ++var9) {
         int var10 = var9;
         int var11 = var9 + 1;
         int var12 = var9 - 1;
         if (var12 < 0) {
            var12 += var2;
         }

         if (var11 >= var2) {
            var11 -= var2;
         }

         if (var9 >= var2) {
            var10 = var9 - var2;
         }

         float var13 = var1[var10 * 2];
         var14 = var1[var10 * 2 + 1];
         float var15 = var1[var11 * 2];
         float var16 = var1[var11 * 2 + 1];
         float var17 = var15 - var13;
         float var18 = var16 - var14;
         if (var17 != 0.0F || var18 != 0.0F) {
            float var19 = var17 * var17 + var18 * var18;
            float var20 = (float)Math.sqrt((double)var19);
            var17 *= var4;
            var18 *= var4;
            var17 /= var20;
            var18 /= var20;
            float var22 = -var17;
            if (var9 != 0) {
               this.bindColor(var12);
               GL.glVertex3f(var5, var6, 0.0F);
               GL.glVertex3f(var7, var8, 0.0F);
               this.bindColor(var10);
               GL.glVertex3f(var13 + var18, var14 + var22, 0.0F);
               GL.glVertex3f(var13 - var18, var14 - var22, 0.0F);
            }

            var5 = var15 - var18;
            var6 = var16 - var22;
            var7 = var15 + var18;
            var8 = var16 + var22;
            if (var9 < var2 - 1) {
               this.bindColor(var10);
               GL.glVertex3f(var13 + var18, var14 + var22, 0.0F);
               GL.glVertex3f(var13 - var18, var14 - var22, 0.0F);
               this.bindColor(var11);
               GL.glVertex3f(var15 - var18, var16 - var22, 0.0F);
               GL.glVertex3f(var15 + var18, var16 + var22, 0.0F);
            }
         }
      }

      GL.glEnd();
      float var23 = var4 <= 12.5F ? 5.0F : 180.0F / (float)Math.ceil((double)var4 / 2.5D);
      float var24;
      float var25;
      float var26;
      int var27;
      if (this.lineCaps) {
         var24 = var1[2] - var1[0];
         var25 = var1[3] - var1[1];
         var26 = (float)Math.toDegrees(Math.atan2((double)var25, (double)var24)) + 90.0F;
         if (var24 != 0.0F || var25 != 0.0F) {
            GL.glBegin(6);
            this.bindColor(0);
            GL.glVertex2f(var1[0], var1[1]);

            for(var27 = 0; (float)var27 < 180.0F + var23; var27 = (int)((float)var27 + var23)) {
               var14 = (float)Math.toRadians((double)(var26 + (float)var27));
               GL.glVertex2f(var1[0] + (float)(Math.cos((double)var14) * (double)var4), var1[1] + (float)(Math.sin((double)var14) * (double)var4));
            }

            GL.glEnd();
         }
      }

      if (this.lineCaps) {
         var24 = var1[var2 * 2 - 2] - var1[var2 * 2 - 4];
         var25 = var1[var2 * 2 - 1] - var1[var2 * 2 - 3];
         var26 = (float)Math.toDegrees(Math.atan2((double)var25, (double)var24)) - 90.0F;
         if (var24 != 0.0F || var25 != 0.0F) {
            GL.glBegin(6);
            this.bindColor(var2 - 1);
            GL.glVertex2f(var1[var2 * 2 - 2], var1[var2 * 2 - 1]);

            for(var27 = 0; (float)var27 < 180.0F + var23; var27 = (int)((float)var27 + var23)) {
               var14 = (float)Math.toRadians((double)(var26 + (float)var27));
               GL.glVertex2f(var1[var2 * 2 - 2] + (float)(Math.cos((double)var14) * (double)var4), var1[var2 * 2 - 1] + (float)(Math.sin((double)var14) * (double)var4));
            }

            GL.glEnd();
         }
      }

   }

   private void bindColor(int var1) {
      if (var1 < this.cpt) {
         if (this.renderHalf) {
            GL.glColor4f(this.colours[var1 * 4] * 0.5F, this.colours[var1 * 4 + 1] * 0.5F, this.colours[var1 * 4 + 2] * 0.5F, this.colours[var1 * 4 + 3] * 0.5F);
         } else {
            GL.glColor4f(this.colours[var1 * 4], this.colours[var1 * 4 + 1], this.colours[var1 * 4 + 2], this.colours[var1 * 4 + 3]);
         }
      }

   }

   public void color(float var1, float var2, float var3, float var4) {
      if (this.width == 1.0F) {
         this.def.color(var1, var2, var3, var4);
      } else {
         this.colours[this.pts * 4] = var1;
         this.colours[this.pts * 4 + 1] = var2;
         this.colours[this.pts * 4 + 2] = var3;
         this.colours[this.pts * 4 + 3] = var4;
         ++this.cpt;
      }
   }

   public boolean applyGLLineFixes() {
      return this.width == 1.0F ? this.def.applyGLLineFixes() : this.def.applyGLLineFixes();
   }
}
