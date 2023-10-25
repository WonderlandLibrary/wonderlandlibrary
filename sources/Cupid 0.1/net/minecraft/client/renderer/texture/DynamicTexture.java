package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import net.minecraft.client.resources.IResourceManager;
import optifine.Config;
import shadersmod.client.ShadersTex;

public class DynamicTexture extends AbstractTexture {
  private final int[] dynamicTextureData;
  
  private final int width;
  
  private final int height;
  
  private static final String __OBFID = "CL_00001048";
  
  private boolean shadersInitialized;
  
  public DynamicTexture(BufferedImage bufferedImage) {
    this(bufferedImage.getWidth(), bufferedImage.getHeight());
    bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), this.dynamicTextureData, 0, bufferedImage.getWidth());
    updateDynamicTexture();
  }
  
  public DynamicTexture(int textureWidth, int textureHeight) {
    this.shadersInitialized = false;
    this.width = textureWidth;
    this.height = textureHeight;
    this.dynamicTextureData = new int[textureWidth * textureHeight * 3];
    if (Config.isShaders()) {
      ShadersTex.initDynamicTexture(getGlTextureId(), textureWidth, textureHeight, this);
      this.shadersInitialized = true;
    } else {
      TextureUtil.allocateTexture(getGlTextureId(), textureWidth, textureHeight);
    } 
  }
  
  public void loadTexture(IResourceManager resourceManager) throws IOException {}
  
  public void updateDynamicTexture() {
    if (Config.isShaders()) {
      if (!this.shadersInitialized) {
        ShadersTex.initDynamicTexture(getGlTextureId(), this.width, this.height, this);
        this.shadersInitialized = true;
      } 
      ShadersTex.updateDynamicTexture(getGlTextureId(), this.dynamicTextureData, this.width, this.height, this);
    } else {
      TextureUtil.uploadTexture(getGlTextureId(), this.dynamicTextureData, this.width, this.height);
    } 
  }
  
  public int[] getTextureData() {
    return this.dynamicTextureData;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\renderer\texture\DynamicTexture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */