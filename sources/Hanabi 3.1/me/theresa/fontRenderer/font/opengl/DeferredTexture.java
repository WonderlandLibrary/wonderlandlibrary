package me.theresa.fontRenderer.font.opengl;

import java.io.IOException;
import java.io.InputStream;
import me.theresa.fontRenderer.font.util.DeferredResource;
import me.theresa.fontRenderer.font.util.LoadingList;

public class DeferredTexture extends TextureImpl implements DeferredResource {
   private final InputStream in;
   private final String resourceName;
   private final boolean flipped;
   private final int filter;
   private TextureImpl target;
   private final int[] trans;

   public DeferredTexture(InputStream in, String resourceName, boolean flipped, int filter, int[] trans) {
      this.in = in;
      this.resourceName = resourceName;
      this.flipped = flipped;
      this.filter = filter;
      this.trans = trans;
      LoadingList.get().add(this);
   }

   public void load() throws IOException {
      boolean before = InternalTextureLoader.get().isDeferredLoading();
      InternalTextureLoader.get().setDeferredLoading(false);
      this.target = InternalTextureLoader.get().getTexture(this.in, this.resourceName, this.flipped, this.filter, this.trans);
      InternalTextureLoader.get().setDeferredLoading(before);
   }

   private void checkTarget() {
      if (this.target == null) {
         try {
            this.load();
            LoadingList.get().remove(this);
         } catch (IOException var2) {
            throw new RuntimeException("Attempt to use deferred texture before loading and resource not found: " + this.resourceName);
         }
      }

   }

   public void bind() {
      this.checkTarget();
      this.target.bind();
   }

   public float getHeight() {
      this.checkTarget();
      return this.target.getHeight();
   }

   public int getImageHeight() {
      this.checkTarget();
      return this.target.getImageHeight();
   }

   public int getImageWidth() {
      this.checkTarget();
      return this.target.getImageWidth();
   }

   public int getTextureHeight() {
      this.checkTarget();
      return this.target.getTextureHeight();
   }

   public int getTextureID() {
      this.checkTarget();
      return this.target.getTextureID();
   }

   public String getTextureRef() {
      this.checkTarget();
      return this.target.getTextureRef();
   }

   public int getTextureWidth() {
      this.checkTarget();
      return this.target.getTextureWidth();
   }

   public float getWidth() {
      this.checkTarget();
      return this.target.getWidth();
   }

   public void release() {
      this.checkTarget();
      this.target.release();
   }

   public void setAlpha(boolean alpha) {
      this.checkTarget();
      this.target.setAlpha(alpha);
   }

   public void setHeight(int height) {
      this.checkTarget();
      this.target.setHeight(height);
   }

   public void setTextureHeight(int texHeight) {
      this.checkTarget();
      this.target.setTextureHeight(texHeight);
   }

   public void setTextureID(int textureID) {
      this.checkTarget();
      this.target.setTextureID(textureID);
   }

   public void setTextureWidth(int texWidth) {
      this.checkTarget();
      this.target.setTextureWidth(texWidth);
   }

   public void setWidth(int width) {
      this.checkTarget();
      this.target.setWidth(width);
   }

   public byte[] getTextureData() {
      this.checkTarget();
      return this.target.getTextureData();
   }

   public String getDescription() {
      return this.resourceName;
   }

   public boolean hasAlpha() {
      this.checkTarget();
      return this.target.hasAlpha();
   }

   public void setTextureFilter(int textureFilter) {
      this.checkTarget();
      this.target.setTextureFilter(textureFilter);
   }
}
