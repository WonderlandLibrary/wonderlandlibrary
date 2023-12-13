package org.newdawn.slick;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.ImageData;
import org.newdawn.slick.opengl.ImageDataFactory;
import org.newdawn.slick.opengl.LoadableImageData;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.util.OperationNotSupportedException;
import org.newdawn.slick.util.ResourceLoader;

public class BigImage extends Image {
   protected static SGL GL = Renderer.get();
   private static Image lastBind;
   private Image[][] images;
   private int xcount;
   private int ycount;
   private int realWidth;
   private int realHeight;

   public static final int getMaxSingleImageSize() {
      IntBuffer var0 = BufferUtils.createIntBuffer(16);
      GL.glGetInteger(3379, var0);
      return var0.get(0);
   }

   private BigImage() {
      this.inited = true;
   }

   public BigImage(String var1) throws SlickException {
      this(var1, 2);
   }

   public BigImage(String var1, int var2) throws SlickException {
      this.build(var1, var2, getMaxSingleImageSize());
   }

   public BigImage(String var1, int var2, int var3) throws SlickException {
      this.build(var1, var2, var3);
   }

   public BigImage(LoadableImageData var1, ByteBuffer var2, int var3) {
      this.build(var1, var2, var3, getMaxSingleImageSize());
   }

   public BigImage(LoadableImageData var1, ByteBuffer var2, int var3, int var4) {
      this.build(var1, var2, var3, var4);
   }

   public Image getTile(int var1, int var2) {
      return this.images[var1][var2];
   }

   private void build(String var1, int var2, int var3) throws SlickException {
      try {
         LoadableImageData var4 = ImageDataFactory.getImageDataFor(var1);
         ByteBuffer var5 = var4.loadImage(ResourceLoader.getResourceAsStream(var1), false, (int[])null);
         this.build(var4, var5, var2, var3);
      } catch (IOException var6) {
         throw new SlickException("Failed to load: " + var1, var6);
      }
   }

   private void build(LoadableImageData var1, ByteBuffer var2, int var3, int var4) {
      int var5 = var1.getTexWidth();
      int var6 = var1.getTexHeight();
      this.realWidth = this.width = var1.getWidth();
      this.realHeight = this.height = var1.getHeight();
      if (var5 <= var4 && var6 <= var4) {
         this.images = new Image[1][1];
         ImageData var21 = new ImageData(this, var1, var6, var2, var5) {
            private final LoadableImageData val$data;
            private final int val$dataHeight;
            private final ByteBuffer val$imageBuffer;
            private final int val$dataWidth;
            private final BigImage this$0;

            {
               this.this$0 = var1;
               this.val$data = var2;
               this.val$dataHeight = var3;
               this.val$imageBuffer = var4;
               this.val$dataWidth = var5;
            }

            public int getDepth() {
               return this.val$data.getDepth();
            }

            public int getHeight() {
               return this.val$dataHeight;
            }

            public ByteBuffer getImageBufferData() {
               return this.val$imageBuffer;
            }

            public int getTexHeight() {
               return this.val$dataHeight;
            }

            public int getTexWidth() {
               return this.val$dataWidth;
            }

            public int getWidth() {
               return this.val$dataWidth;
            }
         };
         this.images[0][0] = new Image(var21, var3);
         this.xcount = 1;
         this.ycount = 1;
         this.inited = true;
      } else {
         this.xcount = (this.realWidth - 1) / var4 + 1;
         this.ycount = (this.realHeight - 1) / var4 + 1;
         this.images = new Image[this.xcount][this.ycount];
         int var7 = var1.getDepth() / 8;

         for(int var8 = 0; var8 < this.xcount; ++var8) {
            for(int var9 = 0; var9 < this.ycount; ++var9) {
               int var10 = (var8 + 1) * var4;
               int var11 = (var9 + 1) * var4;
               int var14 = Math.min(var5, var4);
               int var15 = Math.min(var6, var4);
               ByteBuffer var16 = BufferUtils.createByteBuffer(var4 * var4 * var7);
               int var17 = var8 * var4 * var7;
               byte[] var18 = new byte[var14 * var7];

               for(int var19 = 0; var19 < var15; ++var19) {
                  int var20 = (var9 * var4 + var19) * var5 * var7;
                  var2.position(var20 + var17);
                  var2.get(var18, 0, var14 * var7);
                  var16.put(var18);
               }

               var16.flip();
               ImageData var22 = new ImageData(this, var1, var4, var4, var16, var15, var14) {
                  private final LoadableImageData val$data;
                  private final int val$imageHeight;
                  private final int val$imageWidth;
                  private final ByteBuffer val$subBuffer;
                  private final int val$ySize;
                  private final int val$xSize;
                  private final BigImage this$0;

                  {
                     this.this$0 = var1;
                     this.val$data = var2;
                     this.val$imageHeight = var3;
                     this.val$imageWidth = var4;
                     this.val$subBuffer = var5;
                     this.val$ySize = var6;
                     this.val$xSize = var7;
                  }

                  public int getDepth() {
                     return this.val$data.getDepth();
                  }

                  public int getHeight() {
                     return this.val$imageHeight;
                  }

                  public int getWidth() {
                     return this.val$imageWidth;
                  }

                  public ByteBuffer getImageBufferData() {
                     return this.val$subBuffer;
                  }

                  public int getTexHeight() {
                     return this.val$ySize;
                  }

                  public int getTexWidth() {
                     return this.val$xSize;
                  }
               };
               this.images[var8][var9] = new Image(var22, var3);
            }
         }

         this.inited = true;
      }
   }

   public void bind() {
      throw new OperationNotSupportedException("Can't bind big images yet");
   }

   public Image copy() {
      throw new OperationNotSupportedException("Can't copy big images yet");
   }

   public void draw() {
      this.draw(0.0F, 0.0F);
   }

   public void draw(float var1, float var2, Color var3) {
      this.draw(var1, var2, (float)this.width, (float)this.height, var3);
   }

   public void draw(float var1, float var2, float var3, Color var4) {
      this.draw(var1, var2, (float)this.width * var3, (float)this.height * var3, var4);
   }

   public void draw(float var1, float var2, float var3, float var4, Color var5) {
      float var6 = var3 / (float)this.realWidth;
      float var7 = var4 / (float)this.realHeight;
      GL.glTranslatef(var1, var2, 0.0F);
      GL.glScalef(var6, var7, 1.0F);
      float var8 = 0.0F;
      float var9 = 0.0F;

      for(int var10 = 0; var10 < this.xcount; ++var10) {
         var9 = 0.0F;

         for(int var11 = 0; var11 < this.ycount; ++var11) {
            Image var12 = this.images[var10][var11];
            var12.draw(var8, var9, (float)var12.getWidth(), (float)var12.getHeight(), var5);
            var9 += (float)var12.getHeight();
            if (var11 == this.ycount - 1) {
               var8 += (float)var12.getWidth();
            }
         }
      }

      GL.glScalef(1.0F / var6, 1.0F / var7, 1.0F);
      GL.glTranslatef(-var1, -var2, 0.0F);
   }

   public void draw(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      int var9 = (int)(var7 - var5);
      int var10 = (int)(var8 - var6);
      Image var11 = this.getSubImage((int)var5, (int)var6, var9, var10);
      int var12 = (int)(var3 - var1);
      int var13 = (int)(var4 - var2);
      var11.draw(var1, var2, (float)var12, (float)var13);
   }

   public void draw(float var1, float var2, float var3, float var4, float var5, float var6) {
      int var7 = (int)(var5 - var3);
      int var8 = (int)(var6 - var4);
      this.draw(var1, var2, (float)var7, (float)var8, var3, var4, var5, var6);
   }

   public void draw(float var1, float var2, float var3, float var4) {
      this.draw(var1, var2, var3, var4, Color.white);
   }

   public void draw(float var1, float var2, float var3) {
      this.draw(var1, var2, var3, Color.white);
   }

   public void draw(float var1, float var2) {
      this.draw(var1, var2, Color.white);
   }

   public void drawEmbedded(float var1, float var2, float var3, float var4) {
      float var10000 = var3 / (float)this.realWidth;
      var10000 = var4 / (float)this.realHeight;
      float var7 = 0.0F;
      float var8 = 0.0F;

      for(int var9 = 0; var9 < this.xcount; ++var9) {
         var8 = 0.0F;

         for(int var10 = 0; var10 < this.ycount; ++var10) {
            Image var11 = this.images[var9][var10];
            if (lastBind == null || var11.getTexture() != lastBind.getTexture()) {
               if (lastBind != null) {
                  lastBind.endUse();
               }

               lastBind = var11;
               lastBind.startUse();
            }

            var11.drawEmbedded(var7 + var1, var8 + var2, (float)var11.getWidth(), (float)var11.getHeight());
            var8 += (float)var11.getHeight();
            if (var10 == this.ycount - 1) {
               var7 += (float)var11.getWidth();
            }
         }
      }

   }

   public void drawFlash(float var1, float var2, float var3, float var4) {
      float var5 = var3 / (float)this.realWidth;
      float var6 = var4 / (float)this.realHeight;
      GL.glTranslatef(var1, var2, 0.0F);
      GL.glScalef(var5, var6, 1.0F);
      float var7 = 0.0F;
      float var8 = 0.0F;

      for(int var9 = 0; var9 < this.xcount; ++var9) {
         var8 = 0.0F;

         for(int var10 = 0; var10 < this.ycount; ++var10) {
            Image var11 = this.images[var9][var10];
            var11.drawFlash(var7, var8, (float)var11.getWidth(), (float)var11.getHeight());
            var8 += (float)var11.getHeight();
            if (var10 == this.ycount - 1) {
               var7 += (float)var11.getWidth();
            }
         }
      }

      GL.glScalef(1.0F / var5, 1.0F / var6, 1.0F);
      GL.glTranslatef(-var1, -var2, 0.0F);
   }

   public void drawFlash(float var1, float var2) {
      this.drawFlash(var1, var2, (float)this.width, (float)this.height);
   }

   public void endUse() {
      if (lastBind != null) {
         lastBind.endUse();
      }

      lastBind = null;
   }

   public void startUse() {
   }

   public void ensureInverted() {
      throw new OperationNotSupportedException("Doesn't make sense for tiled operations");
   }

   public Color getColor(int var1, int var2) {
      throw new OperationNotSupportedException("Can't use big images as buffers");
   }

   public Image getFlippedCopy(boolean var1, boolean var2) {
      BigImage var3 = new BigImage();
      var3.images = this.images;
      var3.xcount = this.xcount;
      var3.ycount = this.ycount;
      var3.width = this.width;
      var3.height = this.height;
      var3.realWidth = this.realWidth;
      var3.realHeight = this.realHeight;
      Image[][] var4;
      int var5;
      int var6;
      if (var1) {
         var4 = var3.images;
         var3.images = new Image[this.xcount][this.ycount];

         for(var5 = 0; var5 < this.xcount; ++var5) {
            for(var6 = 0; var6 < this.ycount; ++var6) {
               var3.images[var5][var6] = var4[this.xcount - 1 - var5][var6].getFlippedCopy(true, false);
            }
         }
      }

      if (var2) {
         var4 = var3.images;
         var3.images = new Image[this.xcount][this.ycount];

         for(var5 = 0; var5 < this.xcount; ++var5) {
            for(var6 = 0; var6 < this.ycount; ++var6) {
               var3.images[var5][var6] = var4[var5][this.ycount - 1 - var6].getFlippedCopy(false, true);
            }
         }
      }

      return var3;
   }

   public Graphics getGraphics() throws SlickException {
      throw new OperationNotSupportedException("Can't use big images as offscreen buffers");
   }

   public Image getScaledCopy(float var1) {
      return this.getScaledCopy((int)(var1 * (float)this.width), (int)(var1 * (float)this.height));
   }

   public Image getScaledCopy(int var1, int var2) {
      BigImage var3 = new BigImage();
      var3.images = this.images;
      var3.xcount = this.xcount;
      var3.ycount = this.ycount;
      var3.width = var1;
      var3.height = var2;
      var3.realWidth = this.realWidth;
      var3.realHeight = this.realHeight;
      return var3;
   }

   public Image getSubImage(int var1, int var2, int var3, int var4) {
      BigImage var5 = new BigImage();
      var5.width = var3;
      var5.height = var4;
      var5.realWidth = var3;
      var5.realHeight = var4;
      var5.images = new Image[this.xcount][this.ycount];
      float var6 = 0.0F;
      float var7 = 0.0F;
      int var8 = var1 + var3;
      int var9 = var2 + var4;
      int var10 = 0;
      boolean var11 = false;
      boolean var12 = false;

      for(int var13 = 0; var13 < this.xcount; ++var13) {
         var7 = 0.0F;
         int var25 = 0;
         var12 = false;

         for(int var14 = 0; var14 < this.ycount; ++var14) {
            Image var15 = this.images[var13][var14];
            int var16 = (int)(var6 + (float)var15.getWidth());
            int var17 = (int)(var7 + (float)var15.getHeight());
            int var18 = (int)Math.max((float)var1, var6);
            int var19 = (int)Math.max((float)var2, var7);
            int var20 = Math.min(var8, var16);
            int var21 = Math.min(var9, var17);
            int var22 = var20 - var18;
            int var23 = var21 - var19;
            if (var22 > 0 && var23 > 0) {
               Image var24 = var15.getSubImage((int)((float)var18 - var6), (int)((float)var19 - var7), var20 - var18, var21 - var19);
               var12 = true;
               var5.images[var10][var25] = var24;
               ++var25;
               var5.ycount = Math.max(var5.ycount, var25);
            }

            var7 += (float)var15.getHeight();
            if (var14 == this.ycount - 1) {
               var6 += (float)var15.getWidth();
            }
         }

         if (var12) {
            ++var10;
            ++var5.xcount;
         }
      }

      return var5;
   }

   public Texture getTexture() {
      throw new OperationNotSupportedException("Can't use big images as offscreen buffers");
   }

   protected void initImpl() {
      throw new OperationNotSupportedException("Can't use big images as offscreen buffers");
   }

   protected void reinit() {
      throw new OperationNotSupportedException("Can't use big images as offscreen buffers");
   }

   public void setTexture(Texture var1) {
      throw new OperationNotSupportedException("Can't use big images as offscreen buffers");
   }

   public Image getSubImage(int var1, int var2) {
      return this.images[var1][var2];
   }

   public int getHorizontalImageCount() {
      return this.xcount;
   }

   public int getVerticalImageCount() {
      return this.ycount;
   }

   public String toString() {
      return "[BIG IMAGE]";
   }

   public void destroy() throws SlickException {
      for(int var1 = 0; var1 < this.xcount; ++var1) {
         for(int var2 = 0; var2 < this.ycount; ++var2) {
            Image var3 = this.images[var1][var2];
            var3.destroy();
         }
      }

   }

   public void draw(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, Color var9) {
      int var10 = (int)(var7 - var5);
      int var11 = (int)(var8 - var6);
      Image var12 = this.getSubImage((int)var5, (int)var6, var10, var11);
      int var13 = (int)(var3 - var1);
      int var14 = (int)(var4 - var2);
      var12.draw(var1, var2, (float)var13, (float)var14, var9);
   }

   public void drawCentered(float var1, float var2) {
      throw new UnsupportedOperationException();
   }

   public void drawEmbedded(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, Color var9) {
      throw new UnsupportedOperationException();
   }

   public void drawEmbedded(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      throw new UnsupportedOperationException();
   }

   public void drawFlash(float var1, float var2, float var3, float var4, Color var5) {
      throw new UnsupportedOperationException();
   }

   public void drawSheared(float var1, float var2, float var3, float var4) {
      throw new UnsupportedOperationException();
   }
}
