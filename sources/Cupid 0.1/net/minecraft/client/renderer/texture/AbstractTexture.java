package net.minecraft.client.renderer.texture;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import shadersmod.client.MultiTexID;
import shadersmod.client.ShadersTex;

public abstract class AbstractTexture implements ITextureObject {
  protected int glTextureId = -1;
  
  protected boolean blur;
  
  protected boolean mipmap;
  
  protected boolean blurLast;
  
  protected boolean mipmapLast;
  
  private static final String __OBFID = "CL_00001047";
  
  public MultiTexID multiTex;
  
  public void setBlurMipmapDirect(boolean p_174937_1_, boolean p_174937_2_) {
    int i;
    short short1;
    this.blur = p_174937_1_;
    this.mipmap = p_174937_2_;
    boolean flag = true;
    boolean flag1 = true;
    if (p_174937_1_) {
      i = p_174937_2_ ? 9987 : 9729;
      short1 = 9729;
    } else {
      i = p_174937_2_ ? 9986 : 9728;
      short1 = 9728;
    } 
    GlStateManager.bindTexture(getGlTextureId());
    GL11.glTexParameteri(3553, 10241, i);
    GL11.glTexParameteri(3553, 10240, short1);
  }
  
  public void setBlurMipmap(boolean p_174936_1_, boolean p_174936_2_) {
    this.blurLast = this.blur;
    this.mipmapLast = this.mipmap;
    setBlurMipmapDirect(p_174936_1_, p_174936_2_);
  }
  
  public void restoreLastBlurMipmap() {
    setBlurMipmapDirect(this.blurLast, this.mipmapLast);
  }
  
  public int getGlTextureId() {
    if (this.glTextureId == -1)
      this.glTextureId = TextureUtil.glGenTextures(); 
    return this.glTextureId;
  }
  
  public void deleteGlTexture() {
    ShadersTex.deleteTextures(this, this.glTextureId);
    if (this.glTextureId != -1) {
      TextureUtil.deleteTexture(this.glTextureId);
      this.glTextureId = -1;
    } 
  }
  
  public MultiTexID getMultiTexID() {
    return ShadersTex.getMultiTexID(this);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\renderer\texture\AbstractTexture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */