package optifine;

import java.nio.ByteBuffer;
import java.util.Properties;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TextureAnimation {
   private String srcTex = null;
   private String dstTex = null;
   ResourceLocation dstTexLoc = null;
   private int dstTextId = -1;
   private int dstX = 0;
   private int dstY = 0;
   private int frameWidth = 0;
   private int frameHeight = 0;
   private TextureAnimationFrame[] frames = null;
   private int activeFrame = 0;
   byte[] srcData = null;
   private ByteBuffer imageData = null;

   public TextureAnimation(String var1, byte[] var2, String var3, ResourceLocation var4, int var5, int var6, int var7, int var8, Properties var9, int var10) {
      this.srcTex = var1;
      this.dstTex = var3;
      this.dstTexLoc = var4;
      this.dstX = var5;
      this.dstY = var6;
      this.frameWidth = var7;
      this.frameHeight = var8;
      int var11 = var7 * var8 * 4;
      if (var2.length % var11 != 0) {
         Config.warn("Invalid animated texture length: " + var2.length + ", frameWidth: " + var7 + ", frameHeight: " + var8);
      }

      this.srcData = var2;
      int var12 = var2.length / var11;
      if (var9.get("tile.0") != null) {
         for(int var13 = 0; var9.get("tile." + var13) != null; ++var13) {
            var12 = var13 + 1;
         }
      }

      String var21 = (String)var9.get("duration");
      int var14 = Config.parseInt(var21, var10);
      this.frames = new TextureAnimationFrame[var12];

      for(int var15 = 0; var15 < this.frames.length; ++var15) {
         String var16 = (String)var9.get("tile." + var15);
         int var17 = Config.parseInt(var16, var15);
         String var18 = (String)var9.get("duration." + var15);
         int var19 = Config.parseInt(var18, var14);
         TextureAnimationFrame var20 = new TextureAnimationFrame(var17, var19);
         this.frames[var15] = var20;
      }

   }

   public int getActiveFrameIndex() {
      if (this.frames.length <= 0) {
         return 0;
      } else {
         if (this.activeFrame >= this.frames.length) {
            this.activeFrame = 0;
         }

         TextureAnimationFrame var1 = this.frames[this.activeFrame];
         return var1.index;
      }
   }

   public int getFrameCount() {
      return this.frames.length;
   }

   public boolean updateTexture() {
      if (this.dstTextId < 0) {
         ITextureObject var1 = TextureUtils.getTexture(this.dstTexLoc);
         if (var1 == null) {
            return false;
         }

         this.dstTextId = var1.getGlTextureId();
      }

      if (this.imageData == null) {
         this.imageData = GLAllocation.createDirectByteBuffer(this.srcData.length);
         this.imageData.put(this.srcData);
         this.srcData = null;
      }

      if (this <= 0) {
         return false;
      } else {
         int var4 = this.frameWidth * this.frameHeight * 4;
         int var2 = this.getActiveFrameIndex();
         int var3 = var4 * var2;
         if (var3 + var4 > this.imageData.capacity()) {
            return false;
         } else {
            this.imageData.position(var3);
            GlStateManager.bindTexture(this.dstTextId);
            GL11.glTexSubImage2D(3553, 0, this.dstX, this.dstY, this.frameWidth, this.frameHeight, 6408, 5121, (ByteBuffer)this.imageData);
            return true;
         }
      }
   }

   public String getSrcTex() {
      return this.srcTex;
   }

   public String getDstTex() {
      return this.dstTex;
   }

   public ResourceLocation getDstTexLoc() {
      return this.dstTexLoc;
   }
}
