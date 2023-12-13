package net.minecraft.client.model;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Vec3;
import optifine.Config;
import shadersmod.client.SVertexFormat;

public class TexturedQuad {
   public int nVertices;
   public PositionTextureVertex[] vertexPositions;
   private static final String __OBFID = "CL_00000850";
   private boolean invertNormal;

   public TexturedQuad(PositionTextureVertex[] var1, int var2, int var3, int var4, int var5, float var6, float var7) {
      this(var1);
      float var8 = 0.0F / var6;
      float var9 = 0.0F / var7;
      var1[0] = var1[0].setTexturePosition((float)var4 / var6 - var8, (float)var3 / var7 + var9);
      var1[1] = var1[1].setTexturePosition((float)var2 / var6 + var8, (float)var3 / var7 + var9);
      var1[2] = var1[2].setTexturePosition((float)var2 / var6 + var8, (float)var5 / var7 - var9);
      var1[3] = var1[3].setTexturePosition((float)var4 / var6 - var8, (float)var5 / var7 - var9);
   }

   public TexturedQuad(PositionTextureVertex[] var1) {
      this.vertexPositions = var1;
      this.nVertices = var1.length;
   }

   public void flipFace() {
      PositionTextureVertex[] var1 = new PositionTextureVertex[this.vertexPositions.length];

      for(int var2 = 0; var2 < this.vertexPositions.length; ++var2) {
         var1[var2] = this.vertexPositions[this.vertexPositions.length - var2 - 1];
      }

      this.vertexPositions = var1;
   }

   public void draw(WorldRenderer var1, float var2) {
      Vec3 var3 = this.vertexPositions[1].vector3D.subtractReverse(this.vertexPositions[0].vector3D);
      Vec3 var4 = this.vertexPositions[1].vector3D.subtractReverse(this.vertexPositions[2].vector3D);
      Vec3 var5 = var4.crossProduct(var3).normalize();
      float var6 = (float)var5.xCoord;
      float var7 = (float)var5.yCoord;
      float var8 = (float)var5.zCoord;
      if (this.invertNormal) {
         var6 = -var6;
         var7 = -var7;
         var8 = -var8;
      }

      if (Config.isShaders()) {
         var1.begin(7, SVertexFormat.defVertexFormatTextured);
      } else {
         var1.begin(7, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);
      }

      for(int var9 = 0; var9 < 4; ++var9) {
         PositionTextureVertex var10 = this.vertexPositions[var9];
         var1.pos(var10.vector3D.xCoord * (double)var2, var10.vector3D.yCoord * (double)var2, var10.vector3D.zCoord * (double)var2).tex((double)var10.texturePositionX, (double)var10.texturePositionY).normal(var6, var7, var8).endVertex();
      }

      Tessellator.getInstance().draw();
   }
}
