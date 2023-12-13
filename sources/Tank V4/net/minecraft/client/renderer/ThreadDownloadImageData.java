package net.minecraft.client.renderer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.Proxy.Type;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.HttpPipeline;
import optifine.HttpRequest;
import optifine.HttpResponse;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadDownloadImageData extends SimpleTexture {
   private final IImageBuffer imageBuffer;
   private boolean textureUploaded;
   private static final String __OBFID = "CL_00001049";
   private BufferedImage bufferedImage;
   private static final Logger logger = LogManager.getLogger();
   private Thread imageThread;
   public Boolean imageFound = null;
   public boolean pipeline = false;
   private final String imageUrl;
   private final File cacheFile;
   private static final AtomicInteger threadDownloadCounter = new AtomicInteger(0);

   static IImageBuffer access$6(ThreadDownloadImageData var0) {
      return var0.imageBuffer;
   }

   static void access$4(ThreadDownloadImageData var0) {
      var0.loadPipelined();
   }

   private void loadPipelined() {
      label51: {
         try {
            HttpRequest var1 = HttpPipeline.makeRequest(this.imageUrl, Minecraft.getMinecraft().getProxy());
            HttpResponse var2 = HttpPipeline.executeRequest(var1);
            if (var2.getStatus() / 100 != 2) {
               break label51;
            }

            byte[] var3 = var2.getBody();
            ByteArrayInputStream var4 = new ByteArrayInputStream(var3);
            BufferedImage var5;
            if (this.cacheFile != null) {
               FileUtils.copyInputStreamToFile(var4, this.cacheFile);
               var5 = ImageIO.read(this.cacheFile);
            } else {
               var5 = TextureUtil.readBufferedImage(var4);
            }

            if (this.imageBuffer != null) {
               var5 = this.imageBuffer.parseUserSkin(var5);
            }

            this.setBufferedImage(var5);
         } catch (Exception var7) {
            logger.error("Couldn't download http texture: " + var7.getClass().getName() + ": " + var7.getMessage());
            this.imageFound = this.bufferedImage != null;
            return;
         }

         this.imageFound = this.bufferedImage != null;
         return;
      }

      this.imageFound = this.bufferedImage != null;
   }

   static File access$2(ThreadDownloadImageData var0) {
      return var0.cacheFile;
   }

   private boolean shouldPipeline() {
      if (!this.pipeline) {
         return false;
      } else {
         Proxy var1 = Minecraft.getMinecraft().getProxy();
         return var1.type() != Type.DIRECT && var1.type() != Type.SOCKS ? false : this.imageUrl.startsWith("http://");
      }
   }

   protected void loadTextureFromServer() {
      this.imageThread = new Thread(this, "Texture Downloader #" + threadDownloadCounter.incrementAndGet()) {
         private static final String __OBFID = "CL_00001050";
         final ThreadDownloadImageData this$0;

         public void run() {
            HttpURLConnection var1;
            label88: {
               var1 = null;
               ThreadDownloadImageData.access$0().debug("Downloading http texture from {} to {}", ThreadDownloadImageData.access$1(this.this$0), ThreadDownloadImageData.access$2(this.this$0));
               if (ThreadDownloadImageData.access$3(this.this$0)) {
                  ThreadDownloadImageData.access$4(this.this$0);
               } else {
                  try {
                     var1 = (HttpURLConnection)(new URL(ThreadDownloadImageData.access$1(this.this$0))).openConnection(Minecraft.getMinecraft().getProxy());
                     var1.setDoInput(true);
                     var1.setDoOutput(false);
                     var1.connect();
                     if (var1.getResponseCode() / 100 != 2) {
                        if (var1.getErrorStream() != null) {
                           Config.readAll(var1.getErrorStream());
                        }
                        break label88;
                     }

                     BufferedImage var2;
                     if (ThreadDownloadImageData.access$2(this.this$0) != null) {
                        FileUtils.copyInputStreamToFile(var1.getInputStream(), ThreadDownloadImageData.access$2(this.this$0));
                        var2 = ImageIO.read(ThreadDownloadImageData.access$2(this.this$0));
                     } else {
                        var2 = TextureUtil.readBufferedImage(var1.getInputStream());
                     }

                     if (ThreadDownloadImageData.access$6(this.this$0) != null) {
                        var2 = ThreadDownloadImageData.access$6(this.this$0).parseUserSkin(var2);
                     }

                     this.this$0.setBufferedImage(var2);
                  } catch (Exception var5) {
                     ThreadDownloadImageData.access$0().error("Couldn't download http texture: " + var5.getClass().getName() + ": " + var5.getMessage());
                     if (var1 != null) {
                        var1.disconnect();
                     }

                     this.this$0.imageFound = ThreadDownloadImageData.access$5(this.this$0) != null;
                     return;
                  }

                  if (var1 != null) {
                     var1.disconnect();
                  }

                  this.this$0.imageFound = ThreadDownloadImageData.access$5(this.this$0) != null;
               }

               return;
            }

            if (var1 != null) {
               var1.disconnect();
            }

            this.this$0.imageFound = ThreadDownloadImageData.access$5(this.this$0) != null;
         }

         {
            this.this$0 = var1;
         }
      };
      this.imageThread.setDaemon(true);
      this.imageThread.start();
   }

   public int getGlTextureId() {
      this.checkTextureUploaded();
      return super.getGlTextureId();
   }

   static Logger access$0() {
      return logger;
   }

   static String access$1(ThreadDownloadImageData var0) {
      return var0.imageUrl;
   }

   static BufferedImage access$5(ThreadDownloadImageData var0) {
      return var0.bufferedImage;
   }

   public void setBufferedImage(BufferedImage var1) {
      this.bufferedImage = var1;
      if (this.imageBuffer != null) {
         this.imageBuffer.skinAvailable();
      }

      this.imageFound = this.bufferedImage != null;
   }

   static boolean access$3(ThreadDownloadImageData var0) {
      return var0.shouldPipeline();
   }

   public void loadTexture(IResourceManager var1) throws IOException {
      if (this.bufferedImage == null && this.textureLocation != null) {
         super.loadTexture(var1);
      }

      if (this.imageThread == null) {
         if (this.cacheFile != null && this.cacheFile.isFile()) {
            logger.debug("Loading http texture from local cache ({})", this.cacheFile);

            try {
               this.bufferedImage = ImageIO.read(this.cacheFile);
               if (this.imageBuffer != null) {
                  this.setBufferedImage(this.imageBuffer.parseUserSkin(this.bufferedImage));
               }

               this.imageFound = this.bufferedImage != null;
            } catch (IOException var3) {
               logger.error((String)("Couldn't load skin " + this.cacheFile), (Throwable)var3);
               this.loadTextureFromServer();
            }
         } else {
            this.loadTextureFromServer();
         }
      }

   }

   public ThreadDownloadImageData(File var1, String var2, ResourceLocation var3, IImageBuffer var4) {
      super(var3);
      this.cacheFile = var1;
      this.imageUrl = var2;
      this.imageBuffer = var4;
   }

   private void checkTextureUploaded() {
      if (!this.textureUploaded && this.bufferedImage != null) {
         this.textureUploaded = true;
         if (this.textureLocation != null) {
            this.deleteGlTexture();
         }

         TextureUtil.uploadTextureImage(super.getGlTextureId(), this.bufferedImage);
      }

   }
}
