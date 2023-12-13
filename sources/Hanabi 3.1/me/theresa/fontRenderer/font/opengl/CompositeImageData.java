package me.theresa.fontRenderer.font.opengl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import me.theresa.fontRenderer.font.log.Log;

public class CompositeImageData implements LoadableImageData {
   private final ArrayList sources = new ArrayList();
   private LoadableImageData picked;

   public void add(LoadableImageData data) {
      this.sources.add(data);
   }

   public ByteBuffer loadImage(InputStream fis) throws IOException {
      return this.loadImage(fis, false, (int[])null);
   }

   public ByteBuffer loadImage(InputStream fis, boolean flipped, int[] transparent) throws IOException {
      return this.loadImage(fis, flipped, false, transparent);
   }

   public ByteBuffer loadImage(InputStream is, boolean flipped, boolean forceAlpha, int[] transparent) throws IOException {
      CompositeIOException exception = new CompositeIOException();
      ByteBuffer buffer = null;
      BufferedInputStream in = new BufferedInputStream(is, is.available());
      in.mark(is.available());
      Iterator var8 = this.sources.iterator();

      while(var8.hasNext()) {
         Object source = var8.next();
         in.reset();

         try {
            LoadableImageData data = (LoadableImageData)source;
            buffer = data.loadImage(in, flipped, forceAlpha, transparent);
            this.picked = data;
            break;
         } catch (Exception var11) {
            Log.warn(source.getClass() + " failed to read the data", var11);
            exception.addException(var11);
         }
      }

      if (this.picked == null) {
         throw exception;
      } else {
         return buffer;
      }
   }

   private void checkPicked() {
      if (this.picked == null) {
         throw new RuntimeException("Attempt to make use of uninitialised or invalid composite image data");
      }
   }

   public int getDepth() {
      this.checkPicked();
      return this.picked.getDepth();
   }

   public int getHeight() {
      this.checkPicked();
      return this.picked.getHeight();
   }

   public ByteBuffer getImageBufferData() {
      this.checkPicked();
      return this.picked.getImageBufferData();
   }

   public int getTexHeight() {
      this.checkPicked();
      return this.picked.getTexHeight();
   }

   public int getTexWidth() {
      this.checkPicked();
      return this.picked.getTexWidth();
   }

   public int getWidth() {
      this.checkPicked();
      return this.picked.getWidth();
   }

   public void configureEdging(boolean edging) {
      Iterator var2 = this.sources.iterator();

      while(var2.hasNext()) {
         Object source = var2.next();
         ((LoadableImageData)source).configureEdging(edging);
      }

   }
}
