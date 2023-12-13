package me.yarukon;

import cn.hanabi.Hanabi;
import cn.hanabi.utils.ChatUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

public class Yarukon {
   public static Yarukon INSTANCE;
   public final char[] ascii_Chars = new char[256];
   public final char[] all_Chars = new char['\uffff'];
   public File skinPath;
   public final HashMap skinCache = new HashMap();

   public Yarukon() {
      INSTANCE = this;
      this.skinPath = new File(Hanabi.INSTANCE.fileManager.fileDir + File.separator + "CustomSkins");
      if (!this.skinPath.exists()) {
         this.skinPath.mkdir();
      }

      int i;
      for(i = 0; i < this.ascii_Chars.length; ++i) {
         this.ascii_Chars[i] = (char)i;
      }

      for(i = 0; i < this.all_Chars.length; ++i) {
         this.all_Chars[i] = (char)i;
      }

   }

   public ResourceLocation getSkin(String fileName) {
      return this.skinCache.containsKey(fileName) ? (ResourceLocation)this.skinCache.get(fileName) : DefaultPlayerSkin.getDefaultSkin(Minecraft.getMinecraft().getSession().getProfile().getId());
   }

   public void loadSkinFromLocal(String fileName) {
      if (!this.skinCache.containsKey(fileName)) {
         File path = new File(this.skinPath.getAbsolutePath() + File.separator + fileName + ".png");
         if (!path.exists()) {
            ChatUtils.info("File " + fileName + ".png not exist!");
         } else {
            (new Thread(() -> {
               this.skinCache.put(fileName, (Object)null);
               final ResourceLocation rl = new ResourceLocation("CustomSkins/" + fileName);
               IImageBuffer iib = new IImageBuffer() {
                  ImageBufferDownload ibd = new ImageBufferDownload();

                  public BufferedImage parseUserSkin(BufferedImage image) {
                     return image;
                  }

                  public void skinAvailable() {
                     Yarukon.this.skinCache.put(fileName, rl);
                  }
               };
               ThreadDownloadImageData textureArt = new ThreadDownloadImageData(path, (String)null, (ResourceLocation)null, iib);
               Minecraft.getMinecraft().getTextureManager().loadTexture(rl, textureArt);
            })).start();
         }
      }
   }
}
