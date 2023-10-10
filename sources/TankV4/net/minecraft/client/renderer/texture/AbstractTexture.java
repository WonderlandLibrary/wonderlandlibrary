package net.minecraft.client.renderer.texture;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import shadersmod.client.MultiTexID;
import shadersmod.client.ShadersTex;

public abstract class AbstractTexture implements ITextureObject {
   public MultiTexID multiTex;
   protected boolean blurLast;
   protected boolean mipmap;
   protected boolean mipmapLast;
   private static final String __OBFID = "CL_00001047";
   protected boolean blur;
   protected int glTextureId = -1;

   public int getGlTextureId() {
      if (this.glTextureId == -1) {
         this.glTextureId = TextureUtil.glGenTextures();
      }

      return this.glTextureId;
   }

   public void setBlurMipmapDirect(boolean var1, boolean var2) {
      this.blur = var1;
      this.mipmap = var2;
      boolean var3 = true;
      boolean var4 = true;
      int var5;
      short var6;
      if (var1) {
         var5 = var2 ? 9987 : 9729;
         var6 = 9729;
      } else {
         var5 = var2 ? 9986 : 9728;
         var6 = 9728;
      }

      GlStateManager.bindTexture(this.getGlTextureId());
      GL11.glTexParameteri(3553, 10241, var5);
      GL11.glTexParameteri(3553, 10240, var6);
   }

   public void deleteGlTexture() {
      ShadersTex.deleteTextures(this, this.glTextureId);
      if (this.glTextureId != -1) {
         TextureUtil.deleteTexture(this.glTextureId);
         this.glTextureId = -1;
      }

   }

   public void restoreLastBlurMipmap() {
      this.setBlurMipmapDirect(this.blurLast, this.mipmapLast);
   }

   public MultiTexID getMultiTexID() {
      return ShadersTex.getMultiTexID(this);
   }

   public void setBlurMipmap(boolean var1, boolean var2) {
      this.blurLast = this.blur;
      this.mipmapLast = this.mipmap;
      this.setBlurMipmapDirect(var1, var2);
   }
}
