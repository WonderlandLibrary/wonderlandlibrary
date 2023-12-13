package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import net.minecraft.client.resources.IResourceManager;
import optifine.Config;
import shadersmod.client.ShadersTex;

public class DynamicTexture extends AbstractTexture {
   private final int height;
   private boolean shadersInitialized;
   private static final String __OBFID = "CL_00001048";
   private final int width;
   private final int[] dynamicTextureData;

   public void updateDynamicTexture() {
      if (Config.isShaders()) {
         if (!this.shadersInitialized) {
            ShadersTex.initDynamicTexture(this.getGlTextureId(), this.width, this.height, this);
            this.shadersInitialized = true;
         }

         ShadersTex.updateDynamicTexture(this.getGlTextureId(), this.dynamicTextureData, this.width, this.height, this);
      } else {
         TextureUtil.uploadTexture(this.getGlTextureId(), this.dynamicTextureData, this.width, this.height);
      }

   }

   public int[] getTextureData() {
      return this.dynamicTextureData;
   }

   public DynamicTexture(int var1, int var2) {
      this.shadersInitialized = false;
      this.width = var1;
      this.height = var2;
      this.dynamicTextureData = new int[var1 * var2 * 3];
      if (Config.isShaders()) {
         ShadersTex.initDynamicTexture(this.getGlTextureId(), var1, var2, this);
         this.shadersInitialized = true;
      } else {
         TextureUtil.allocateTexture(this.getGlTextureId(), var1, var2);
      }

   }

   public DynamicTexture(BufferedImage var1) {
      this(var1.getWidth(), var1.getHeight());
      var1.getRGB(0, 0, var1.getWidth(), var1.getHeight(), this.dynamicTextureData, 0, var1.getWidth());
      this.updateDynamicTexture();
   }

   public void loadTexture(IResourceManager var1) throws IOException {
   }
}
