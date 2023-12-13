package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shadersmod.client.ShadersTex;

public class SimpleTexture extends AbstractTexture {
   private static final Logger logger = LogManager.getLogger();
   private static final String __OBFID = "CL_00001052";
   protected final ResourceLocation textureLocation;

   public SimpleTexture(ResourceLocation var1) {
      this.textureLocation = var1;
   }

   public void loadTexture(IResourceManager var1) throws IOException {
      this.deleteGlTexture();
      InputStream var2 = null;
      IResource var3 = var1.getResource(this.textureLocation);
      var2 = var3.getInputStream();
      BufferedImage var4 = TextureUtil.readBufferedImage(var2);
      boolean var5 = false;
      boolean var6 = false;
      if (var3.hasMetadata()) {
         try {
            TextureMetadataSection var7 = (TextureMetadataSection)var3.getMetadata("texture");
            if (var7 != null) {
               var5 = var7.getTextureBlur();
               var6 = var7.getTextureClamp();
            }
         } catch (RuntimeException var10) {
            logger.warn((String)("Failed reading metadata of: " + this.textureLocation), (Throwable)var10);
         }
      }

      if (Config.isShaders()) {
         ShadersTex.loadSimpleTexture(this.getGlTextureId(), var4, var5, var6, var1, this.textureLocation, this.getMultiTexID());
      } else {
         TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), var4, var5, var6);
      }

      if (var2 != null) {
         var2.close();
      }

   }
}
