package my.NewSnake.utils;

import java.awt.Font;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class FontManager {
   private ResourceLocation darrow = new ResourceLocation("font/SF-UI-Display-Regular.otf");
   public static FontManager instance;
   private TTFFontRenderer defaultFont;
   private HashMap fonts = new HashMap();

   public FontManager getInstance() {
      return instance;
   }

   public TTFFontRenderer getFont(String var1) {
      return (TTFFontRenderer)this.fonts.getOrDefault(var1, this.defaultFont);
   }

   public FontManager() {
      instance = this;
      ThreadPoolExecutor var1 = (ThreadPoolExecutor)Executors.newFixedThreadPool(8);
      ConcurrentLinkedQueue var2 = new ConcurrentLinkedQueue();
      this.defaultFont = new TTFFontRenderer(new Font("Verdana", 0, 18));

      try {
         int[] var6;
         int var5 = (var6 = new int[]{6, 7, 8, 10, 11, 12, 14}).length;

         int var3;
         int var4;
         InputStream var7;
         Font var8;
         for(var4 = 0; var4 < var5; ++var4) {
            var3 = var6[var4];
            var7 = this.getClass().getResourceAsStream("/assets/minecraft/font/SF-UI-Display-Regular.otf");
            var8 = Font.createFont(0, var7);
            var8 = var8.deriveFont(0, (float)var3);
            this.fonts.put("SFR " + var3 + 100, new TTFFontRenderer(var8));
         }

         var5 = (var6 = new int[]{6, 7, 8, 9, 11}).length;

         for(var4 = 0; var4 < var5; ++var4) {
            var3 = var6[var4];
            var7 = this.getClass().getResourceAsStream("/assets/minecraft/font/SF-UI-Display-Bold.otf");
            var8 = Font.createFont(0, var7);
            var8 = var8.deriveFont(0, (float)var3);
            this.fonts.put("SFB " + var3, new TTFFontRenderer(var8));
         }

         var5 = (var6 = new int[]{6, 7, 8, 9, 11, 12}).length;

         for(var4 = 0; var4 < var5; ++var4) {
            var3 = var6[var4];
            var7 = this.getClass().getResourceAsStream("/assets/minecraft/font/SF-UI-Display-Medium.otf");
            var8 = Font.createFont(0, var7);
            var8 = var8.deriveFont(0, (float)var3);
            this.fonts.put("SFM " + var3, new TTFFontRenderer(var8));
         }

         var5 = (var6 = new int[]{17, 10, 9, 8, 7, 6}).length;

         for(var4 = 0; var4 < var5; ++var4) {
            var3 = var6[var4];
            var7 = this.getClass().getResourceAsStream("/assets/minecraft/font/SF-UI-Display-Light.otf");
            var8 = Font.createFont(0, var7);
            var8 = var8.deriveFont(0, (float)var3);
            this.fonts.put("SFL " + var3, new TTFFontRenderer(var8));
         }

         var5 = (var6 = new int[]{19}).length;

         for(var4 = 0; var4 < var5; ++var4) {
            var3 = var6[var4];
            var7 = this.getClass().getResourceAsStream("/assets/minecraft/font/Jigsaw-Regular.otf");
            var8 = Font.createFont(0, var7);
            var8 = var8.deriveFont(0, (float)var3);
            this.fonts.put("JIGR " + var3, new TTFFontRenderer(var8));
         }

         this.fonts.put("Verdana 12", new TTFFontRenderer(new Font("Verdana", 0, 12)));
         this.fonts.put("Verdana Bold 16", new TTFFontRenderer(new Font("Verdana Bold", 0, 16)));
         this.fonts.put("Verdana Bold 20", new TTFFontRenderer(new Font("Verdana Bold", 0, 20)));
      } catch (Exception var10) {
      }

      var1.shutdown();

      while(!var1.isTerminated()) {
         try {
            Thread.sleep(10L);
         } catch (InterruptedException var9) {
            var9.printStackTrace();
         }

         while(!var2.isEmpty()) {
            TextureData var11 = (TextureData)var2.poll();
            GlStateManager.bindTexture(var11.getTextureId());
            GL11.glTexParameteri(3553, 10241, 9728);
            GL11.glTexParameteri(3553, 10240, 9728);
            GL11.glTexImage2D(3553, 0, 6408, var11.getWidth(), var11.getHeight(), 0, 6408, 5121, (ByteBuffer)var11.getBuffer());
         }
      }

   }
}
