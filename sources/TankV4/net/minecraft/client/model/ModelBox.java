package net.minecraft.client.model;

import net.minecraft.client.renderer.WorldRenderer;

public class ModelBox {
   private TexturedQuad[] quadList;
   public final float posY2;
   public String boxName;
   public final float posX1;
   private PositionTextureVertex[] vertexPositions;
   public final float posZ2;
   public final float posX2;
   public final float posZ1;
   public final float posY1;

   public ModelBox(ModelRenderer var1, int var2, int var3, float var4, float var5, float var6, int var7, int var8, int var9, float var10, boolean var11) {
      this.posX1 = var4;
      this.posY1 = var5;
      this.posZ1 = var6;
      this.posX2 = var4 + (float)var7;
      this.posY2 = var5 + (float)var8;
      this.posZ2 = var6 + (float)var9;
      this.vertexPositions = new PositionTextureVertex[8];
      this.quadList = new TexturedQuad[6];
      float var12 = var4 + (float)var7;
      float var13 = var5 + (float)var8;
      float var14 = var6 + (float)var9;
      var4 -= var10;
      var5 -= var10;
      var6 -= var10;
      var12 += var10;
      var13 += var10;
      var14 += var10;
      if (var11) {
         float var15 = var12;
         var12 = var4;
         var4 = var15;
      }

      PositionTextureVertex var24 = new PositionTextureVertex(var4, var5, var6, 0.0F, 0.0F);
      PositionTextureVertex var16 = new PositionTextureVertex(var12, var5, var6, 0.0F, 8.0F);
      PositionTextureVertex var17 = new PositionTextureVertex(var12, var13, var6, 8.0F, 8.0F);
      PositionTextureVertex var18 = new PositionTextureVertex(var4, var13, var6, 8.0F, 0.0F);
      PositionTextureVertex var19 = new PositionTextureVertex(var4, var5, var14, 0.0F, 0.0F);
      PositionTextureVertex var20 = new PositionTextureVertex(var12, var5, var14, 0.0F, 8.0F);
      PositionTextureVertex var21 = new PositionTextureVertex(var12, var13, var14, 8.0F, 8.0F);
      PositionTextureVertex var22 = new PositionTextureVertex(var4, var13, var14, 8.0F, 0.0F);
      this.vertexPositions[0] = var24;
      this.vertexPositions[1] = var16;
      this.vertexPositions[2] = var17;
      this.vertexPositions[3] = var18;
      this.vertexPositions[4] = var19;
      this.vertexPositions[5] = var20;
      this.vertexPositions[6] = var21;
      this.vertexPositions[7] = var22;
      this.quadList[0] = new TexturedQuad(new PositionTextureVertex[]{var20, var16, var17, var21}, var2 + var9 + var7, var3 + var9, var2 + var9 + var7 + var9, var3 + var9 + var8, var1.textureWidth, var1.textureHeight);
      this.quadList[1] = new TexturedQuad(new PositionTextureVertex[]{var24, var19, var22, var18}, var2, var3 + var9, var2 + var9, var3 + var9 + var8, var1.textureWidth, var1.textureHeight);
      this.quadList[2] = new TexturedQuad(new PositionTextureVertex[]{var20, var19, var24, var16}, var2 + var9, var3, var2 + var9 + var7, var3 + var9, var1.textureWidth, var1.textureHeight);
      this.quadList[3] = new TexturedQuad(new PositionTextureVertex[]{var17, var18, var22, var21}, var2 + var9 + var7, var3 + var9, var2 + var9 + var7 + var7, var3, var1.textureWidth, var1.textureHeight);
      this.quadList[4] = new TexturedQuad(new PositionTextureVertex[]{var16, var24, var18, var17}, var2 + var9, var3 + var9, var2 + var9 + var7, var3 + var9 + var8, var1.textureWidth, var1.textureHeight);
      this.quadList[5] = new TexturedQuad(new PositionTextureVertex[]{var19, var20, var21, var22}, var2 + var9 + var7 + var9, var3 + var9, var2 + var9 + var7 + var9 + var7, var3 + var9 + var8, var1.textureWidth, var1.textureHeight);
      if (var11) {
         for(int var23 = 0; var23 < this.quadList.length; ++var23) {
            this.quadList[var23].flipFace();
         }
      }

   }

   public void render(WorldRenderer var1, float var2) {
      for(int var3 = 0; var3 < this.quadList.length; ++var3) {
         this.quadList[var3].draw(var1, var2);
      }

   }

   public ModelBox setBoxName(String var1) {
      this.boxName = var1;
      return this;
   }

   public ModelBox(ModelRenderer var1, int var2, int var3, float var4, float var5, float var6, int var7, int var8, int var9, float var10) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var1.mirror);
   }
}
