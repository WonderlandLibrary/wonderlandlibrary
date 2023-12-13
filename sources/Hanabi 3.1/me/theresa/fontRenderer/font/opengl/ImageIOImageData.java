package me.theresa.fontRenderer.font.opengl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;
import javax.imageio.ImageIO;

public class ImageIOImageData implements LoadableImageData {
   private static final ColorModel glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(1000), new int[]{8, 8, 8, 8}, true, false, 3, 0);
   private static final ColorModel glColorModel = new ComponentColorModel(ColorSpace.getInstance(1000), new int[]{8, 8, 8, 0}, false, false, 1, 0);
   private int depth;
   private int height;
   private int width;
   private int texWidth;
   private int texHeight;
   private boolean edging = true;

   public int getDepth() {
      return this.depth;
   }

   public int getHeight() {
      return this.height;
   }

   public int getTexHeight() {
      return this.texHeight;
   }

   public int getTexWidth() {
      return this.texWidth;
   }

   public int getWidth() {
      return this.width;
   }

   public ByteBuffer loadImage(InputStream fis) throws IOException {
      return this.loadImage(fis, true, (int[])null);
   }

   public ByteBuffer loadImage(InputStream fis, boolean flipped, int[] transparent) throws IOException {
      return this.loadImage(fis, flipped, false, transparent);
   }

   public ByteBuffer loadImage(InputStream fis, boolean flipped, boolean forceAlpha, int[] transparent) throws IOException {
      if (transparent != null) {
         forceAlpha = true;
      }

      BufferedImage bufferedImage = ImageIO.read(fis);
      return this.imageToByteBuffer(bufferedImage, flipped, forceAlpha, transparent);
   }

   public ByteBuffer imageToByteBuffer(BufferedImage image, boolean flipped, boolean forceAlpha, int[] transparent) {
      ByteBuffer imageBuffer = null;
      int texWidth = 2;

      int texHeight;
      for(texHeight = 2; texWidth < image.getWidth(); texWidth *= 2) {
      }

      while(texHeight < image.getHeight()) {
         texHeight *= 2;
      }

      this.width = image.getWidth();
      this.height = image.getHeight();
      this.texHeight = texHeight;
      this.texWidth = texWidth;
      boolean useAlpha = image.getColorModel().hasAlpha() || forceAlpha;
      WritableRaster raster;
      BufferedImage texImage;
      if (useAlpha) {
         this.depth = 32;
         raster = Raster.createInterleavedRaster(0, texWidth, texHeight, 4, (Point)null);
         texImage = new BufferedImage(glAlphaColorModel, raster, false, new Hashtable());
      } else {
         this.depth = 24;
         raster = Raster.createInterleavedRaster(0, texWidth, texHeight, 3, (Point)null);
         texImage = new BufferedImage(glColorModel, raster, false, new Hashtable());
      }

      Graphics2D g = (Graphics2D)texImage.getGraphics();
      if (useAlpha) {
         g.setColor(new Color(0.0F, 0.0F, 0.0F, 0.0F));
         g.fillRect(0, 0, texWidth, texHeight);
      }

      if (flipped) {
         g.scale(1.0, -1.0);
         g.drawImage(image, 0, -this.height, (ImageObserver)null);
      } else {
         g.drawImage(image, 0, 0, (ImageObserver)null);
      }

      if (this.edging) {
         if (this.height < texHeight - 1) {
            this.copyArea(texImage, 0, 0, this.width, 1, 0, texHeight - 1);
            this.copyArea(texImage, 0, this.height - 1, this.width, 1, 0, 1);
         }

         if (this.width < texWidth - 1) {
            this.copyArea(texImage, 0, 0, 1, this.height, texWidth - 1, 0);
            this.copyArea(texImage, this.width - 1, 0, 1, this.height, 1, 0);
         }
      }

      byte[] data = ((DataBufferByte)texImage.getRaster().getDataBuffer()).getData();
      if (transparent != null) {
         for(int i = 0; i < data.length; i += 4) {
            boolean match = true;

            for(int c = 0; c < 3; ++c) {
               int value = data[i + c] < 0 ? 256 + data[i + c] : data[i + c];
               if (value != transparent[c]) {
                  match = false;
                  break;
               }
            }

            if (match) {
               data[i + 3] = 0;
            }
         }
      }

      imageBuffer = ByteBuffer.allocateDirect(data.length);
      imageBuffer.order(ByteOrder.nativeOrder());
      imageBuffer.put(data, 0, data.length);
      imageBuffer.flip();
      g.dispose();
      return imageBuffer;
   }

   public ByteBuffer getImageBufferData() {
      throw new RuntimeException("ImageIOImageData doesn't store it's image.");
   }

   private void copyArea(BufferedImage image, int x, int y, int width, int height, int dx, int dy) {
      Graphics2D g = (Graphics2D)image.getGraphics();
      g.drawImage(image.getSubimage(x, y, width, height), x + dx, y + dy, (ImageObserver)null);
   }

   public void configureEdging(boolean edging) {
      this.edging = edging;
   }
}
