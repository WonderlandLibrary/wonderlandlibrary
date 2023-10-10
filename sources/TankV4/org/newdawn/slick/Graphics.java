package org.newdawn.slick;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.ShapeRenderer;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.renderer.LineStripRenderer;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.util.FastTrig;
import org.newdawn.slick.util.Log;

public class Graphics {
   protected static SGL GL = Renderer.get();
   private static LineStripRenderer LSR = Renderer.getLineStripRenderer();
   public static int MODE_NORMAL = 1;
   public static int MODE_ALPHA_MAP = 2;
   public static int MODE_ALPHA_BLEND = 3;
   public static int MODE_COLOR_MULTIPLY = 4;
   public static int MODE_ADD = 5;
   public static int MODE_SCREEN = 6;
   private static final int DEFAULT_SEGMENTS = 50;
   protected static Graphics currentGraphics = null;
   protected static Font DEFAULT_FONT;
   private float sx = 1.0F;
   private float sy = 1.0F;
   private Font font;
   private Color currentColor;
   protected int screenWidth;
   protected int screenHeight;
   private boolean pushed;
   private Rectangle clip;
   private DoubleBuffer worldClip;
   private ByteBuffer readBuffer;
   private boolean antialias;
   private Rectangle worldClipRecord;
   private int currentDrawingMode;
   private float lineWidth;
   private ArrayList stack;
   private int stackIndex;

   public static void setCurrent(Graphics var0) {
      if (currentGraphics != var0) {
         if (currentGraphics != null) {
            currentGraphics.disable();
         }

         currentGraphics = var0;
         currentGraphics.enable();
      }

   }

   public Graphics() {
      this.currentColor = Color.white;
      this.worldClip = BufferUtils.createDoubleBuffer(4);
      this.readBuffer = BufferUtils.createByteBuffer(4);
      this.currentDrawingMode = MODE_NORMAL;
      this.lineWidth = 1.0F;
      this.stack = new ArrayList();
   }

   public Graphics(int var1, int var2) {
      this.currentColor = Color.white;
      this.worldClip = BufferUtils.createDoubleBuffer(4);
      this.readBuffer = BufferUtils.createByteBuffer(4);
      this.currentDrawingMode = MODE_NORMAL;
      this.lineWidth = 1.0F;
      this.stack = new ArrayList();
      if (DEFAULT_FONT == null) {
         AccessController.doPrivileged(new PrivilegedAction(this) {
            private final Graphics this$0;

            {
               this.this$0 = var1;
            }

            public Object run() {
               try {
                  Graphics.DEFAULT_FONT = new AngelCodeFont("org/newdawn/slick/data/defaultfont.fnt", "org/newdawn/slick/data/defaultfont.png");
               } catch (SlickException var2) {
                  Log.error((Throwable)var2);
               }

               return null;
            }
         });
      }

      this.font = DEFAULT_FONT;
      this.screenWidth = var1;
      this.screenHeight = var2;
   }

   void setDimensions(int var1, int var2) {
      this.screenWidth = var1;
      this.screenHeight = var2;
   }

   public void setDrawMode(int var1) {
      this.predraw();
      this.currentDrawingMode = var1;
      if (this.currentDrawingMode == MODE_NORMAL) {
         GL.glEnable(3042);
         GL.glColorMask(true, true, true, true);
         GL.glBlendFunc(770, 771);
      }

      if (this.currentDrawingMode == MODE_ALPHA_MAP) {
         GL.glDisable(3042);
         GL.glColorMask(false, false, false, true);
      }

      if (this.currentDrawingMode == MODE_ALPHA_BLEND) {
         GL.glEnable(3042);
         GL.glColorMask(true, true, true, false);
         GL.glBlendFunc(772, 773);
      }

      if (this.currentDrawingMode == MODE_COLOR_MULTIPLY) {
         GL.glEnable(3042);
         GL.glColorMask(true, true, true, true);
         GL.glBlendFunc(769, 768);
      }

      if (this.currentDrawingMode == MODE_ADD) {
         GL.glEnable(3042);
         GL.glColorMask(true, true, true, true);
         GL.glBlendFunc(1, 1);
      }

      if (this.currentDrawingMode == MODE_SCREEN) {
         GL.glEnable(3042);
         GL.glColorMask(true, true, true, true);
         GL.glBlendFunc(1, 769);
      }

      this.postdraw();
   }

   public void clearAlphaMap() {
      this.pushTransform();
      GL.glLoadIdentity();
      int var1 = this.currentDrawingMode;
      this.setDrawMode(MODE_ALPHA_MAP);
      this.setColor(new Color(0, 0, 0, 0));
      this.fillRect(0.0F, 0.0F, (float)this.screenWidth, (float)this.screenHeight);
      this.setColor(this.currentColor);
      this.setDrawMode(var1);
      this.popTransform();
   }

   private void predraw() {
      setCurrent(this);
   }

   private void postdraw() {
   }

   protected void enable() {
   }

   public void flush() {
      if (currentGraphics == this) {
         currentGraphics.disable();
         currentGraphics = null;
      }

   }

   protected void disable() {
   }

   public Font getFont() {
      return this.font;
   }

   public void setBackground(Color var1) {
      this.predraw();
      GL.glClearColor(var1.r, var1.g, var1.b, var1.a);
      this.postdraw();
   }

   public Color getBackground() {
      this.predraw();
      FloatBuffer var1 = BufferUtils.createFloatBuffer(16);
      GL.glGetFloat(3106, var1);
      this.postdraw();
      return new Color(var1);
   }

   public void clear() {
      this.predraw();
      GL.glClear(16384);
      this.postdraw();
   }

   public void resetTransform() {
      this.sx = 1.0F;
      this.sy = 1.0F;
      if (this.pushed) {
         this.predraw();
         GL.glPopMatrix();
         this.pushed = false;
         this.postdraw();
      }

   }

   private void checkPush() {
      if (!this.pushed) {
         this.predraw();
         GL.glPushMatrix();
         this.pushed = true;
         this.postdraw();
      }

   }

   public void scale(float var1, float var2) {
      this.sx *= var1;
      this.sy *= var2;
      this.checkPush();
      this.predraw();
      GL.glScalef(var1, var2, 1.0F);
      this.postdraw();
   }

   public void rotate(float var1, float var2, float var3) {
      this.checkPush();
      this.predraw();
      this.translate(var1, var2);
      GL.glRotatef(var3, 0.0F, 0.0F, 1.0F);
      this.translate(-var1, -var2);
      this.postdraw();
   }

   public void translate(float var1, float var2) {
      this.checkPush();
      this.predraw();
      GL.glTranslatef(var1, var2, 0.0F);
      this.postdraw();
   }

   public void setFont(Font var1) {
      this.font = var1;
   }

   public void resetFont() {
      this.font = DEFAULT_FONT;
   }

   public void setColor(Color var1) {
      if (var1 != null) {
         this.currentColor = new Color(var1);
         this.predraw();
         this.currentColor.bind();
         this.postdraw();
      }
   }

   public Color getColor() {
      return new Color(this.currentColor);
   }

   public void drawLine(float var1, float var2, float var3, float var4) {
      float var5 = this.lineWidth - 1.0F;
      if (LSR.applyGLLineFixes()) {
         float var6;
         if (var1 == var3) {
            if (var2 > var4) {
               var6 = var4;
               var4 = var2;
               var2 = var6;
            }

            var6 = 1.0F / this.sy;
            var5 /= this.sy;
            this.fillRect(var1 - var5 / 2.0F, var2 - var5 / 2.0F, var5 + var6, var4 - var2 + var5 + var6);
            return;
         }

         if (var2 == var4) {
            if (var1 > var3) {
               var6 = var3;
               var3 = var1;
               var1 = var6;
            }

            var6 = 1.0F / this.sx;
            var5 /= this.sx;
            this.fillRect(var1 - var5 / 2.0F, var2 - var5 / 2.0F, var3 - var1 + var5 + var6, var5 + var6);
            return;
         }
      }

      this.predraw();
      this.currentColor.bind();
      TextureImpl.bindNone();
      LSR.start();
      LSR.vertex(var1, var2);
      LSR.vertex(var3, var4);
      LSR.end();
      this.postdraw();
   }

   public void draw(Shape var1, ShapeFill var2) {
      this.predraw();
      TextureImpl.bindNone();
      ShapeRenderer.draw(var1, var2);
      this.currentColor.bind();
      this.postdraw();
   }

   public void fill(Shape var1, ShapeFill var2) {
      this.predraw();
      TextureImpl.bindNone();
      ShapeRenderer.fill(var1, var2);
      this.currentColor.bind();
      this.postdraw();
   }

   public void draw(Shape var1) {
      this.predraw();
      TextureImpl.bindNone();
      this.currentColor.bind();
      ShapeRenderer.draw(var1);
      this.postdraw();
   }

   public void fill(Shape var1) {
      this.predraw();
      TextureImpl.bindNone();
      this.currentColor.bind();
      ShapeRenderer.fill(var1);
      this.postdraw();
   }

   public void texture(Shape var1, Image var2) {
      this.texture(var1, var2, 0.01F, 0.01F, false);
   }

   public void texture(Shape var1, Image var2, ShapeFill var3) {
      this.texture(var1, var2, 0.01F, 0.01F, var3);
   }

   public void texture(Shape var1, Image var2, boolean var3) {
      if (var3) {
         this.texture(var1, var2, 1.0F, 1.0F, true);
      } else {
         this.texture(var1, var2, 0.01F, 0.01F, false);
      }

   }

   public void texture(Shape var1, Image var2, float var3, float var4) {
      this.texture(var1, var2, var3, var4, false);
   }

   public void texture(Shape var1, Image var2, float var3, float var4, boolean var5) {
      this.predraw();
      TextureImpl.bindNone();
      this.currentColor.bind();
      if (var5) {
         ShapeRenderer.textureFit(var1, var2, var3, var4);
      } else {
         ShapeRenderer.texture(var1, var2, var3, var4);
      }

      this.postdraw();
   }

   public void texture(Shape var1, Image var2, float var3, float var4, ShapeFill var5) {
      this.predraw();
      TextureImpl.bindNone();
      this.currentColor.bind();
      ShapeRenderer.texture(var1, var2, var3, var4, var5);
      this.postdraw();
   }

   public void drawRect(float var1, float var2, float var3, float var4) {
      float var5 = this.getLineWidth();
      this.drawLine(var1, var2, var1 + var3, var2);
      this.drawLine(var1 + var3, var2, var1 + var3, var2 + var4);
      this.drawLine(var1 + var3, var2 + var4, var1, var2 + var4);
      this.drawLine(var1, var2 + var4, var1, var2);
   }

   public void clearClip() {
      this.clip = null;
      this.predraw();
      GL.glDisable(3089);
      this.postdraw();
   }

   public void setWorldClip(float var1, float var2, float var3, float var4) {
      this.predraw();
      this.worldClipRecord = new Rectangle(var1, var2, var3, var4);
      GL.glEnable(12288);
      this.worldClip.put(1.0D).put(0.0D).put(0.0D).put((double)(-var1)).flip();
      GL.glClipPlane(12288, this.worldClip);
      GL.glEnable(12289);
      this.worldClip.put(-1.0D).put(0.0D).put(0.0D).put((double)(var1 + var3)).flip();
      GL.glClipPlane(12289, this.worldClip);
      GL.glEnable(12290);
      this.worldClip.put(0.0D).put(1.0D).put(0.0D).put((double)(-var2)).flip();
      GL.glClipPlane(12290, this.worldClip);
      GL.glEnable(12291);
      this.worldClip.put(0.0D).put(-1.0D).put(0.0D).put((double)(var2 + var4)).flip();
      GL.glClipPlane(12291, this.worldClip);
      this.postdraw();
   }

   public void clearWorldClip() {
      this.predraw();
      this.worldClipRecord = null;
      GL.glDisable(12288);
      GL.glDisable(12289);
      GL.glDisable(12290);
      GL.glDisable(12291);
      this.postdraw();
   }

   public void setWorldClip(Rectangle var1) {
      if (var1 == null) {
         this.clearWorldClip();
      } else {
         this.setWorldClip(var1.getX(), var1.getY(), var1.getWidth(), var1.getHeight());
      }

   }

   public Rectangle getWorldClip() {
      return this.worldClipRecord;
   }

   public void setClip(int var1, int var2, int var3, int var4) {
      this.predraw();
      if (this.clip == null) {
         GL.glEnable(3089);
         this.clip = new Rectangle((float)var1, (float)var2, (float)var3, (float)var4);
      } else {
         this.clip.setBounds((float)var1, (float)var2, (float)var3, (float)var4);
      }

      GL.glScissor(var1, this.screenHeight - var2 - var4, var3, var4);
      this.postdraw();
   }

   public void setClip(Rectangle var1) {
      if (var1 == null) {
         this.clearClip();
      } else {
         this.setClip((int)var1.getX(), (int)var1.getY(), (int)var1.getWidth(), (int)var1.getHeight());
      }
   }

   public Rectangle getClip() {
      return this.clip;
   }

   public void fillRect(float var1, float var2, float var3, float var4, Image var5, float var6, float var7) {
      int var8 = (int)Math.ceil((double)(var3 / (float)var5.getWidth())) + 2;
      int var9 = (int)Math.ceil((double)(var4 / (float)var5.getHeight())) + 2;
      Rectangle var10 = this.getWorldClip();
      this.setWorldClip(var1, var2, var3, var4);
      this.predraw();

      for(int var11 = 0; var11 < var8; ++var11) {
         for(int var12 = 0; var12 < var9; ++var12) {
            var5.draw((float)(var11 * var5.getWidth()) + var1 - var6, (float)(var12 * var5.getHeight()) + var2 - var7);
         }
      }

      this.postdraw();
      this.setWorldClip(var10);
   }

   public void fillRect(float var1, float var2, float var3, float var4) {
      this.predraw();
      TextureImpl.bindNone();
      this.currentColor.bind();
      GL.glBegin(7);
      GL.glVertex2f(var1, var2);
      GL.glVertex2f(var1 + var3, var2);
      GL.glVertex2f(var1 + var3, var2 + var4);
      GL.glVertex2f(var1, var2 + var4);
      GL.glEnd();
      this.postdraw();
   }

   public void drawOval(float var1, float var2, float var3, float var4) {
      this.drawOval(var1, var2, var3, var4, 50);
   }

   public void drawOval(float var1, float var2, float var3, float var4, int var5) {
      this.drawArc(var1, var2, var3, var4, var5, 0.0F, 360.0F);
   }

   public void drawArc(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.drawArc(var1, var2, var3, var4, 50, var5, var6);
   }

   public void drawArc(float var1, float var2, float var3, float var4, int var5, float var6, float var7) {
      this.predraw();
      TextureImpl.bindNone();
      this.currentColor.bind();

      while(var7 < var6) {
         var7 += 360.0F;
      }

      float var8 = var1 + var3 / 2.0F;
      float var9 = var2 + var4 / 2.0F;
      LSR.start();
      int var10 = 360 / var5;

      for(int var11 = (int)var6; var11 < (int)(var7 + (float)var10); var11 += var10) {
         float var12 = (float)var11;
         if (var12 > var7) {
            var12 = var7;
         }

         float var13 = (float)((double)var8 + FastTrig.cos(Math.toRadians((double)var12)) * (double)var3 / 2.0D);
         float var14 = (float)((double)var9 + FastTrig.sin(Math.toRadians((double)var12)) * (double)var4 / 2.0D);
         LSR.vertex(var13, var14);
      }

      LSR.end();
      this.postdraw();
   }

   public void fillOval(float var1, float var2, float var3, float var4) {
      this.fillOval(var1, var2, var3, var4, 50);
   }

   public void fillOval(float var1, float var2, float var3, float var4, int var5) {
      this.fillArc(var1, var2, var3, var4, var5, 0.0F, 360.0F);
   }

   public void fillArc(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.fillArc(var1, var2, var3, var4, 50, var5, var6);
   }

   public void fillArc(float var1, float var2, float var3, float var4, int var5, float var6, float var7) {
      this.predraw();
      TextureImpl.bindNone();
      this.currentColor.bind();

      while(var7 < var6) {
         var7 += 360.0F;
      }

      float var8 = var1 + var3 / 2.0F;
      float var9 = var2 + var4 / 2.0F;
      GL.glBegin(6);
      int var10 = 360 / var5;
      GL.glVertex2f(var8, var9);

      int var11;
      float var12;
      float var13;
      float var14;
      for(var11 = (int)var6; var11 < (int)(var7 + (float)var10); var11 += var10) {
         var12 = (float)var11;
         if (var12 > var7) {
            var12 = var7;
         }

         var13 = (float)((double)var8 + FastTrig.cos(Math.toRadians((double)var12)) * (double)var3 / 2.0D);
         var14 = (float)((double)var9 + FastTrig.sin(Math.toRadians((double)var12)) * (double)var4 / 2.0D);
         GL.glVertex2f(var13, var14);
      }

      GL.glEnd();
      if (this.antialias) {
         GL.glBegin(6);
         GL.glVertex2f(var8, var9);
         if (var7 != 360.0F) {
            var7 -= 10.0F;
         }

         for(var11 = (int)var6; var11 < (int)(var7 + (float)var10); var11 += var10) {
            var12 = (float)var11;
            if (var12 > var7) {
               var12 = var7;
            }

            var13 = (float)((double)var8 + FastTrig.cos(Math.toRadians((double)(var12 + 10.0F))) * (double)var3 / 2.0D);
            var14 = (float)((double)var9 + FastTrig.sin(Math.toRadians((double)(var12 + 10.0F))) * (double)var4 / 2.0D);
            GL.glVertex2f(var13, var14);
         }

         GL.glEnd();
      }

      this.postdraw();
   }

   public void drawRoundRect(float var1, float var2, float var3, float var4, int var5) {
      this.drawRoundRect(var1, var2, var3, var4, var5, 50);
   }

   public void drawRoundRect(float var1, float var2, float var3, float var4, int var5, int var6) {
      if (var5 < 0) {
         throw new IllegalArgumentException("corner radius must be > 0");
      } else if (var5 == 0) {
         this.drawRect(var1, var2, var3, var4);
      } else {
         int var7 = (int)Math.min(var3, var4) / 2;
         if (var5 > var7) {
            var5 = var7;
         }

         this.drawLine(var1 + (float)var5, var2, var1 + var3 - (float)var5, var2);
         this.drawLine(var1, var2 + (float)var5, var1, var2 + var4 - (float)var5);
         this.drawLine(var1 + var3, var2 + (float)var5, var1 + var3, var2 + var4 - (float)var5);
         this.drawLine(var1 + (float)var5, var2 + var4, var1 + var3 - (float)var5, var2 + var4);
         float var8 = (float)(var5 * 2);
         this.drawArc(var1 + var3 - var8, var2 + var4 - var8, var8, var8, var6, 0.0F, 90.0F);
         this.drawArc(var1, var2 + var4 - var8, var8, var8, var6, 90.0F, 180.0F);
         this.drawArc(var1 + var3 - var8, var2, var8, var8, var6, 270.0F, 360.0F);
         this.drawArc(var1, var2, var8, var8, var6, 180.0F, 270.0F);
      }
   }

   public void fillRoundRect(float var1, float var2, float var3, float var4, int var5) {
      this.fillRoundRect(var1, var2, var3, var4, var5, 50);
   }

   public void fillRoundRect(float var1, float var2, float var3, float var4, int var5, int var6) {
      if (var5 < 0) {
         throw new IllegalArgumentException("corner radius must be > 0");
      } else if (var5 == 0) {
         this.fillRect(var1, var2, var3, var4);
      } else {
         int var7 = (int)Math.min(var3, var4) / 2;
         if (var5 > var7) {
            var5 = var7;
         }

         float var8 = (float)(var5 * 2);
         this.fillRect(var1 + (float)var5, var2, var3 - var8, (float)var5);
         this.fillRect(var1, var2 + (float)var5, (float)var5, var4 - var8);
         this.fillRect(var1 + var3 - (float)var5, var2 + (float)var5, (float)var5, var4 - var8);
         this.fillRect(var1 + (float)var5, var2 + var4 - (float)var5, var3 - var8, (float)var5);
         this.fillRect(var1 + (float)var5, var2 + (float)var5, var3 - var8, var4 - var8);
         this.fillArc(var1 + var3 - var8, var2 + var4 - var8, var8, var8, var6, 0.0F, 90.0F);
         this.fillArc(var1, var2 + var4 - var8, var8, var8, var6, 90.0F, 180.0F);
         this.fillArc(var1 + var3 - var8, var2, var8, var8, var6, 270.0F, 360.0F);
         this.fillArc(var1, var2, var8, var8, var6, 180.0F, 270.0F);
      }
   }

   public void setLineWidth(float var1) {
      this.predraw();
      this.lineWidth = var1;
      LSR.setWidth(var1);
      GL.glPointSize(var1);
      this.postdraw();
   }

   public float getLineWidth() {
      return this.lineWidth;
   }

   public void resetLineWidth() {
      this.predraw();
      Renderer.getLineStripRenderer().setWidth(1.0F);
      GL.glLineWidth(1.0F);
      GL.glPointSize(1.0F);
      this.postdraw();
   }

   public void setAntiAlias(boolean var1) {
      this.predraw();
      this.antialias = var1;
      LSR.setAntiAlias(var1);
      if (var1) {
         GL.glEnable(2881);
      } else {
         GL.glDisable(2881);
      }

      this.postdraw();
   }

   public boolean isAntiAlias() {
      return this.antialias;
   }

   public void drawString(String var1, float var2, float var3) {
      this.predraw();
      this.font.drawString(var2, var3, var1, this.currentColor);
      this.postdraw();
   }

   public void drawImage(Image var1, float var2, float var3, Color var4) {
      this.predraw();
      var1.draw(var2, var3, var4);
      this.currentColor.bind();
      this.postdraw();
   }

   public void drawAnimation(Animation var1, float var2, float var3) {
      this.drawAnimation(var1, var2, var3, Color.white);
   }

   public void drawAnimation(Animation var1, float var2, float var3, Color var4) {
      this.predraw();
      var1.draw(var2, var3, var4);
      this.currentColor.bind();
      this.postdraw();
   }

   public void drawImage(Image var1, float var2, float var3) {
      this.drawImage(var1, var2, var3, Color.white);
   }

   public void drawImage(Image var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      this.predraw();
      var1.draw(var2, var3, var4, var5, var6, var7, var8, var9);
      this.currentColor.bind();
      this.postdraw();
   }

   public void drawImage(Image var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.drawImage(var1, var2, var3, var2 + (float)var1.getWidth(), var3 + (float)var1.getHeight(), var4, var5, var6, var7);
   }

   public void copyArea(Image var1, int var2, int var3) {
      int var4 = var1.getTexture().hasAlpha() ? 6408 : 6407;
      var1.bind();
      GL.glCopyTexImage2D(3553, 0, var4, var2, this.screenHeight - (var3 + var1.getHeight()), var1.getTexture().getTextureWidth(), var1.getTexture().getTextureHeight(), 0);
      var1.ensureInverted();
   }

   private int translate(byte var1) {
      return var1 < 0 ? 256 + var1 : var1;
   }

   public Color getPixel(int var1, int var2) {
      this.predraw();
      GL.glReadPixels(var1, this.screenHeight - var2, 1, 1, 6408, 5121, this.readBuffer);
      this.postdraw();
      return new Color(this.translate(this.readBuffer.get(0)), this.translate(this.readBuffer.get(1)), this.translate(this.readBuffer.get(2)), this.translate(this.readBuffer.get(3)));
   }

   public void getArea(int var1, int var2, int var3, int var4, ByteBuffer var5) {
      if (var5.capacity() < var3 * var4 * 4) {
         throw new IllegalArgumentException("Byte buffer provided to get area is not big enough");
      } else {
         this.predraw();
         GL.glReadPixels(var1, this.screenHeight - var2 - var4, var3, var4, 6408, 5121, var5);
         this.postdraw();
      }
   }

   public void drawImage(Image var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Color var10) {
      this.predraw();
      var1.draw(var2, var3, var4, var5, var6, var7, var8, var9, var10);
      this.currentColor.bind();
      this.postdraw();
   }

   public void drawImage(Image var1, float var2, float var3, float var4, float var5, float var6, float var7, Color var8) {
      this.drawImage(var1, var2, var3, var2 + (float)var1.getWidth(), var3 + (float)var1.getHeight(), var4, var5, var6, var7, var8);
   }

   public void drawGradientLine(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12) {
      this.predraw();
      TextureImpl.bindNone();
      GL.glBegin(1);
      GL.glColor4f(var3, var4, var5, var6);
      GL.glVertex2f(var1, var2);
      GL.glColor4f(var9, var10, var11, var12);
      GL.glVertex2f(var7, var8);
      GL.glEnd();
      this.postdraw();
   }

   public void drawGradientLine(float var1, float var2, Color var3, float var4, float var5, Color var6) {
      this.predraw();
      TextureImpl.bindNone();
      GL.glBegin(1);
      var3.bind();
      GL.glVertex2f(var1, var2);
      var6.bind();
      GL.glVertex2f(var4, var5);
      GL.glEnd();
      this.postdraw();
   }

   public void pushTransform() {
      this.predraw();
      FloatBuffer var1;
      if (this.stackIndex >= this.stack.size()) {
         var1 = BufferUtils.createFloatBuffer(18);
         this.stack.add(var1);
      } else {
         var1 = (FloatBuffer)this.stack.get(this.stackIndex);
      }

      GL.glGetFloat(2982, var1);
      var1.put(16, this.sx);
      var1.put(17, this.sy);
      ++this.stackIndex;
      this.postdraw();
   }

   public void popTransform() {
      if (this.stackIndex == 0) {
         throw new RuntimeException("Attempt to pop a transform that hasn't be pushed");
      } else {
         this.predraw();
         --this.stackIndex;
         FloatBuffer var1 = (FloatBuffer)this.stack.get(this.stackIndex);
         GL.glLoadMatrix(var1);
         this.sx = var1.get(16);
         this.sy = var1.get(17);
         this.postdraw();
      }
   }

   public void destroy() {
   }
}
