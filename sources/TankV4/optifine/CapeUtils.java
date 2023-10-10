package optifine;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FilenameUtils;

public class CapeUtils {
   public static void downloadCape(AbstractClientPlayer var0) {
      String var1 = var0.getNameClear();
      if (var1 != null && !var1.isEmpty()) {
         String var2 = "http://s.optifine.net/capes/" + var1 + ".png";
         String var3 = FilenameUtils.getBaseName(var2);
         ResourceLocation var4 = new ResourceLocation("capeof/" + var3);
         TextureManager var5 = Minecraft.getMinecraft().getTextureManager();
         ITextureObject var6 = var5.getTexture(var4);
         if (var6 != null && var6 instanceof ThreadDownloadImageData) {
            ThreadDownloadImageData var7 = (ThreadDownloadImageData)var6;
            if (var7.imageFound != null) {
               if (var7.imageFound) {
                  var0.setLocationOfCape(var4);
               }

               return;
            }
         }

         IImageBuffer var9 = new IImageBuffer(var0, var4) {
            ImageBufferDownload ibd;
            private final AbstractClientPlayer val$p_downloadCape_0_;
            private final ResourceLocation val$resourcelocation;

            {
               this.val$p_downloadCape_0_ = var1;
               this.val$resourcelocation = var2;
               this.ibd = new ImageBufferDownload();
            }

            public BufferedImage parseUserSkin(BufferedImage var1) {
               return CapeUtils.parseCape(var1);
            }

            public void skinAvailable() {
               this.val$p_downloadCape_0_.setLocationOfCape(this.val$resourcelocation);
            }
         };
         ThreadDownloadImageData var8 = new ThreadDownloadImageData((File)null, var2, (ResourceLocation)null, var9);
         var8.pipeline = true;
         var5.loadTexture(var4, var8);
      }

   }

   public static BufferedImage parseCape(BufferedImage var0) {
      int var1 = 64;
      int var2 = 32;
      int var3 = var0.getWidth();

      for(int var4 = var0.getHeight(); var1 < var3 || var2 < var4; var2 *= 2) {
         var1 *= 2;
      }

      BufferedImage var6 = new BufferedImage(var1, var2, 2);
      Graphics var5 = var6.getGraphics();
      var5.drawImage(var0, 0, 0, (ImageObserver)null);
      var5.dispose();
      return var6;
   }
}
