package me.theresa.fontRenderer.font.opengl.renderer;

public class QuadBasedLineStripRenderer implements LineStripRenderer {
   private final SGL GL = Renderer.get();
   public static int MAX_POINTS = 10000;
   private boolean antialias;
   private float width = 1.0F;
   private final float[] points;
   private final float[] colours;
   private int pts;
   private int cpt;
   private final DefaultLineStripRenderer def = new DefaultLineStripRenderer();
   private boolean renderHalf;
   private boolean lineCaps = false;

   public QuadBasedLineStripRenderer() {
      this.points = new float[MAX_POINTS * 2];
      this.colours = new float[MAX_POINTS * 4];
   }

   public void setLineCaps(boolean caps) {
      this.lineCaps = caps;
   }

   public void start() {
      if (this.width == 1.0F) {
         this.def.start();
      } else {
         this.pts = 0;
         this.cpt = 0;
         this.GL.flush();
         float[] col = this.GL.getCurrentColor();
         this.color(col[0], col[1], col[2], col[3]);
      }
   }

   public void end() {
      if (this.width == 1.0F) {
         this.def.end();
      } else {
         this.renderLines(this.points, this.pts);
      }
   }

   public void vertex(float x, float y) {
      if (this.width == 1.0F) {
         this.def.vertex(x, y);
      } else {
         this.points[this.pts * 2] = x;
         this.points[this.pts * 2 + 1] = y;
         ++this.pts;
         int index = this.pts - 1;
         this.color(this.colours[index * 4], this.colours[index * 4 + 1], this.colours[index * 4 + 2], this.colours[index * 4 + 3]);
      }
   }

   public void setWidth(float width) {
      this.width = width;
   }

   public void setAntiAlias(boolean antialias) {
      this.def.setAntiAlias(antialias);
      this.antialias = antialias;
   }

   public void renderLines(float[] points, int count) {
      if (this.antialias) {
         this.GL.glEnable(2881);
         this.renderLinesImpl(points, count, this.width + 1.0F);
      }

      this.GL.glDisable(2881);
      this.renderLinesImpl(points, count, this.width);
      if (this.antialias) {
         this.GL.glEnable(2881);
      }

   }

   public void renderLinesImpl(float[] points, int count, float w) {
      float width = w / 2.0F;
      float lastx1 = 0.0F;
      float lasty1 = 0.0F;
      float lastx2 = 0.0F;
      float lasty2 = 0.0F;
      this.GL.glBegin(7);

      float y1;
      for(int i = 0; i < count + 1; ++i) {
         int current = i;
         int next = i + 1;
         int prev = i - 1;
         if (prev < 0) {
            prev += count;
         }

         if (next >= count) {
            next -= count;
         }

         if (i >= count) {
            current = i - count;
         }

         float x1 = points[current * 2];
         y1 = points[current * 2 + 1];
         float x2 = points[next * 2];
         float y2 = points[next * 2 + 1];
         float dx = x2 - x1;
         float dy = y2 - y1;
         if (dx != 0.0F || dy != 0.0F) {
            float d2 = dx * dx + dy * dy;
            float d = (float)Math.sqrt((double)d2);
            dx *= width;
            dy *= width;
            dx /= d;
            dy /= d;
            float ty = -dx;
            if (i != 0) {
               this.bindColor(prev);
               this.GL.glVertex3f(lastx1, lasty1, 0.0F);
               this.GL.glVertex3f(lastx2, lasty2, 0.0F);
               this.bindColor(current);
               this.GL.glVertex3f(x1 + dy, y1 + ty, 0.0F);
               this.GL.glVertex3f(x1 - dy, y1 - ty, 0.0F);
            }

            lastx1 = x2 - dy;
            lasty1 = y2 - ty;
            lastx2 = x2 + dy;
            lasty2 = y2 + ty;
            if (i < count - 1) {
               this.bindColor(current);
               this.GL.glVertex3f(x1 + dy, y1 + ty, 0.0F);
               this.GL.glVertex3f(x1 - dy, y1 - ty, 0.0F);
               this.bindColor(next);
               this.GL.glVertex3f(x2 - dy, y2 - ty, 0.0F);
               this.GL.glVertex3f(x2 + dy, y2 + ty, 0.0F);
            }
         }
      }

      this.GL.glEnd();
      float step = width <= 12.5F ? 5.0F : 180.0F / (float)Math.ceil((double)width / 2.5);
      float dx;
      float dy;
      float fang;
      int i;
      if (this.lineCaps) {
         dx = points[2] - points[0];
         dy = points[3] - points[1];
         fang = (float)Math.toDegrees(Math.atan2((double)dy, (double)dx)) + 90.0F;
         if (dx != 0.0F || dy != 0.0F) {
            this.GL.glBegin(6);
            this.bindColor(0);
            this.GL.glVertex2f(points[0], points[1]);

            for(i = 0; (float)i < 180.0F + step; i = (int)((float)i + step)) {
               y1 = (float)Math.toRadians((double)(fang + (float)i));
               this.GL.glVertex2f(points[0] + (float)(Math.cos((double)y1) * (double)width), points[1] + (float)(Math.sin((double)y1) * (double)width));
            }

            this.GL.glEnd();
         }
      }

      if (this.lineCaps) {
         dx = points[count * 2 - 2] - points[count * 2 - 4];
         dy = points[count * 2 - 1] - points[count * 2 - 3];
         fang = (float)Math.toDegrees(Math.atan2((double)dy, (double)dx)) - 90.0F;
         if (dx != 0.0F || dy != 0.0F) {
            this.GL.glBegin(6);
            this.bindColor(count - 1);
            this.GL.glVertex2f(points[count * 2 - 2], points[count * 2 - 1]);

            for(i = 0; (float)i < 180.0F + step; i = (int)((float)i + step)) {
               y1 = (float)Math.toRadians((double)(fang + (float)i));
               this.GL.glVertex2f(points[count * 2 - 2] + (float)(Math.cos((double)y1) * (double)width), points[count * 2 - 1] + (float)(Math.sin((double)y1) * (double)width));
            }

            this.GL.glEnd();
         }
      }

   }

   private void bindColor(int index) {
      if (index < this.cpt) {
         if (this.renderHalf) {
            this.GL.glColor4f(this.colours[index * 4] * 0.5F, this.colours[index * 4 + 1] * 0.5F, this.colours[index * 4 + 2] * 0.5F, this.colours[index * 4 + 3] * 0.5F);
         } else {
            this.GL.glColor4f(this.colours[index * 4], this.colours[index * 4 + 1], this.colours[index * 4 + 2], this.colours[index * 4 + 3]);
         }
      }

   }

   public void color(float r, float g, float b, float a) {
      if (this.width == 1.0F) {
         this.def.color(r, g, b, a);
      } else {
         this.colours[this.pts * 4] = r;
         this.colours[this.pts * 4 + 1] = g;
         this.colours[this.pts * 4 + 2] = b;
         this.colours[this.pts * 4 + 3] = a;
         ++this.cpt;
      }
   }

   public boolean applyGLLineFixes() {
      return this.width == 1.0F ? this.def.applyGLLineFixes() : this.def.applyGLLineFixes();
   }
}
