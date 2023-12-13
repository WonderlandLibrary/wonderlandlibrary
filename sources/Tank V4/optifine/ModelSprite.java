package optifine;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class ModelSprite {
   private ModelRenderer modelRenderer = null;
   private int textureOffsetX = 0;
   private int textureOffsetY = 0;
   private float posX = 0.0F;
   private float posY = 0.0F;
   private float posZ = 0.0F;
   private int sizeX = 0;
   private int sizeY = 0;
   private int sizeZ = 0;
   private float sizeAdd = 0.0F;
   private float minU = 0.0F;
   private float minV = 0.0F;
   private float maxU = 0.0F;
   private float maxV = 0.0F;

   public ModelSprite(ModelRenderer var1, int var2, int var3, float var4, float var5, float var6, int var7, int var8, int var9, float var10) {
      this.modelRenderer = var1;
      this.textureOffsetX = var2;
      this.textureOffsetY = var3;
      this.posX = var4;
      this.posY = var5;
      this.posZ = var6;
      this.sizeX = var7;
      this.sizeY = var8;
      this.sizeZ = var9;
      this.sizeAdd = var10;
      this.minU = (float)var2 / var1.textureWidth;
      this.minV = (float)var3 / var1.textureHeight;
      this.maxU = (float)(var2 + var7) / var1.textureWidth;
      this.maxV = (float)(var3 + var8) / var1.textureHeight;
   }

   public void render(Tessellator var1, float var2) {
      GlStateManager.translate(this.posX * var2, this.posY * var2, this.posZ * var2);
      float var3 = this.minU;
      float var4 = this.maxU;
      float var5 = this.minV;
      float var6 = this.maxV;
      if (this.modelRenderer.mirror) {
         var3 = this.maxU;
         var4 = this.minU;
      }

      if (this.modelRenderer.mirrorV) {
         var5 = this.maxV;
         var6 = this.minV;
      }

      renderItemIn2D(var1, var3, var5, var4, var6, this.sizeX, this.sizeY, var2 * (float)this.sizeZ, this.modelRenderer.textureWidth, this.modelRenderer.textureHeight);
      GlStateManager.translate(-this.posX * var2, -this.posY * var2, -this.posZ * var2);
   }

   public static void renderItemIn2D(Tessellator var0, float var1, float var2, float var3, float var4, int var5, int var6, float var7, float var8, float var9) {
      if (var7 < 6.25E-4F) {
         var7 = 6.25E-4F;
      }

      float var10 = var3 - var1;
      float var11 = var4 - var2;
      double var12 = (double)(MathHelper.abs(var10) * (var8 / 16.0F));
      double var14 = (double)(MathHelper.abs(var11) * (var9 / 16.0F));
      WorldRenderer var16 = var0.getWorldRenderer();
      GL11.glNormal3f(0.0F, 0.0F, -1.0F);
      var16.begin(7, DefaultVertexFormats.POSITION_TEX);
      var16.pos(0.0D, var14, 0.0D).tex((double)var1, (double)var4).endVertex();
      var16.pos(var12, var14, 0.0D).tex((double)var3, (double)var4).endVertex();
      var16.pos(var12, 0.0D, 0.0D).tex((double)var3, (double)var2).endVertex();
      var16.pos(0.0D, 0.0D, 0.0D).tex((double)var1, (double)var2).endVertex();
      var0.draw();
      GL11.glNormal3f(0.0F, 0.0F, 1.0F);
      var16.begin(7, DefaultVertexFormats.POSITION_TEX);
      var16.pos(0.0D, 0.0D, (double)var7).tex((double)var1, (double)var2).endVertex();
      var16.pos(var12, 0.0D, (double)var7).tex((double)var3, (double)var2).endVertex();
      var16.pos(var12, var14, (double)var7).tex((double)var3, (double)var4).endVertex();
      var16.pos(0.0D, var14, (double)var7).tex((double)var1, (double)var4).endVertex();
      var0.draw();
      float var17 = 0.5F * var10 / (float)var5;
      float var18 = 0.5F * var11 / (float)var6;
      GL11.glNormal3f(-1.0F, 0.0F, 0.0F);
      var16.begin(7, DefaultVertexFormats.POSITION_TEX);

      int var19;
      float var20;
      float var21;
      for(var19 = 0; var19 < var5; ++var19) {
         var20 = (float)var19 / (float)var5;
         var21 = var1 + var10 * var20 + var17;
         var16.pos((double)var20 * var12, var14, (double)var7).tex((double)var21, (double)var4).endVertex();
         var16.pos((double)var20 * var12, var14, 0.0D).tex((double)var21, (double)var4).endVertex();
         var16.pos((double)var20 * var12, 0.0D, 0.0D).tex((double)var21, (double)var2).endVertex();
         var16.pos((double)var20 * var12, 0.0D, (double)var7).tex((double)var21, (double)var2).endVertex();
      }

      var0.draw();
      GL11.glNormal3f(1.0F, 0.0F, 0.0F);
      var16.begin(7, DefaultVertexFormats.POSITION_TEX);

      float var22;
      for(var19 = 0; var19 < var5; ++var19) {
         var20 = (float)var19 / (float)var5;
         var21 = var1 + var10 * var20 + var17;
         var22 = var20 + 1.0F / (float)var5;
         var16.pos((double)var22 * var12, 0.0D, (double)var7).tex((double)var21, (double)var2).endVertex();
         var16.pos((double)var22 * var12, 0.0D, 0.0D).tex((double)var21, (double)var2).endVertex();
         var16.pos((double)var22 * var12, var14, 0.0D).tex((double)var21, (double)var4).endVertex();
         var16.pos((double)var22 * var12, var14, (double)var7).tex((double)var21, (double)var4).endVertex();
      }

      var0.draw();
      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
      var16.begin(7, DefaultVertexFormats.POSITION_TEX);

      for(var19 = 0; var19 < var6; ++var19) {
         var20 = (float)var19 / (float)var6;
         var21 = var2 + var11 * var20 + var18;
         var22 = var20 + 1.0F / (float)var6;
         var16.pos(0.0D, (double)var22 * var14, (double)var7).tex((double)var1, (double)var21).endVertex();
         var16.pos(var12, (double)var22 * var14, (double)var7).tex((double)var3, (double)var21).endVertex();
         var16.pos(var12, (double)var22 * var14, 0.0D).tex((double)var3, (double)var21).endVertex();
         var16.pos(0.0D, (double)var22 * var14, 0.0D).tex((double)var1, (double)var21).endVertex();
      }

      var0.draw();
      GL11.glNormal3f(0.0F, -1.0F, 0.0F);
      var16.begin(7, DefaultVertexFormats.POSITION_TEX);

      for(var19 = 0; var19 < var6; ++var19) {
         var20 = (float)var19 / (float)var6;
         var21 = var2 + var11 * var20 + var18;
         var16.pos(var12, (double)var20 * var14, (double)var7).tex((double)var3, (double)var21).endVertex();
         var16.pos(0.0D, (double)var20 * var14, (double)var7).tex((double)var1, (double)var21).endVertex();
         var16.pos(0.0D, (double)var20 * var14, 0.0D).tex((double)var1, (double)var21).endVertex();
         var16.pos(var12, (double)var20 * var14, 0.0D).tex((double)var3, (double)var21).endVertex();
      }

      var0.draw();
   }
}
