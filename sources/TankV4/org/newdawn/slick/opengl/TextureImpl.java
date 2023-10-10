package org.newdawn.slick.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;

public class TextureImpl implements Texture {
   protected static SGL GL = Renderer.get();
   static Texture lastBind;
   private int target;
   private int textureID;
   private int height;
   private int width;
   private int texWidth;
   private int texHeight;
   private float widthRatio;
   private float heightRatio;
   private boolean alpha;
   private String ref;
   private String cacheName;

   public static Texture getLastBind() {
      return lastBind;
   }

   protected TextureImpl() {
   }

   public TextureImpl(String var1, int var2, int var3) {
      this.target = var2;
      this.ref = var1;
      this.textureID = var3;
      lastBind = this;
   }

   public void setCacheName(String var1) {
      this.cacheName = var1;
   }

   public boolean hasAlpha() {
      return this.alpha;
   }

   public String getTextureRef() {
      return this.ref;
   }

   public void setAlpha(boolean var1) {
      this.alpha = var1;
   }

   public static void bindNone() {
      lastBind = null;
      GL.glDisable(3553);
   }

   public static void unbind() {
      lastBind = null;
   }

   public void bind() {
      if (lastBind != this) {
         lastBind = this;
         GL.glEnable(3553);
         GL.glBindTexture(this.target, this.textureID);
      }

   }

   public void setHeight(int var1) {
      this.height = var1;
      this.setHeight();
   }

   public void setWidth(int var1) {
      this.width = var1;
      this.setWidth();
   }

   public int getImageHeight() {
      return this.height;
   }

   public int getImageWidth() {
      return this.width;
   }

   public float getHeight() {
      return this.heightRatio;
   }

   public float getWidth() {
      return this.widthRatio;
   }

   public int getTextureHeight() {
      return this.texHeight;
   }

   public int getTextureWidth() {
      return this.texWidth;
   }

   public void setTextureHeight(int var1) {
      this.texHeight = var1;
      this.setHeight();
   }

   public void setTextureWidth(int var1) {
      this.texWidth = var1;
      this.setWidth();
   }

   private void setHeight() {
      if (this.texHeight != 0) {
         this.heightRatio = (float)this.height / (float)this.texHeight;
      }

   }

   private void setWidth() {
      if (this.texWidth != 0) {
         this.widthRatio = (float)this.width / (float)this.texWidth;
      }

   }

   public void release() {
      IntBuffer var1 = this.createIntBuffer(1);
      var1.put(this.textureID);
      var1.flip();
      GL.glDeleteTextures(var1);
      if (lastBind == this) {
         bindNone();
      }

      if (this.cacheName != null) {
         InternalTextureLoader.get().clear(this.cacheName);
      } else {
         InternalTextureLoader.get().clear(this.ref);
      }

   }

   public int getTextureID() {
      return this.textureID;
   }

   public void setTextureID(int var1) {
      this.textureID = var1;
   }

   protected IntBuffer createIntBuffer(int var1) {
      ByteBuffer var2 = ByteBuffer.allocateDirect(4 * var1);
      var2.order(ByteOrder.nativeOrder());
      return var2.asIntBuffer();
   }

   public byte[] getTextureData() {
      ByteBuffer var1 = BufferUtils.createByteBuffer((this.hasAlpha() ? 4 : 3) * this.texWidth * this.texHeight);
      this.bind();
      GL.glGetTexImage(3553, 0, this.hasAlpha() ? 6408 : 6407, 5121, var1);
      byte[] var2 = new byte[var1.limit()];
      var1.get(var2);
      var1.clear();
      return var2;
   }
}
