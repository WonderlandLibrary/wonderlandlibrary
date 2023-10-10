package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelEnderMite extends ModelBase {
   private static final int[][] field_178714_b = new int[][]{new int[2], {0, 5}, {0, 14}, {0, 18}};
   private final ModelRenderer[] field_178713_d;
   private static final int[][] field_178716_a = new int[][]{{4, 3, 2}, {6, 4, 5}, {3, 3, 1}, {1, 2, 1}};
   private static final int field_178715_c;

   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(var2, var3, var4, var5, var6, var7, var1);

      for(int var8 = 0; var8 < this.field_178713_d.length; ++var8) {
         this.field_178713_d[var8].render(var7);
      }

   }

   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      for(int var8 = 0; var8 < this.field_178713_d.length; ++var8) {
         this.field_178713_d[var8].rotateAngleY = MathHelper.cos(var3 * 0.9F + (float)var8 * 0.15F * 3.1415927F) * 3.1415927F * 0.01F * (float)(1 + Math.abs(var8 - 2));
         this.field_178713_d[var8].rotationPointX = MathHelper.sin(var3 * 0.9F + (float)var8 * 0.15F * 3.1415927F) * 3.1415927F * 0.1F * (float)Math.abs(var8 - 2);
      }

   }

   static {
      field_178715_c = field_178716_a.length;
   }

   public ModelEnderMite() {
      this.field_178713_d = new ModelRenderer[field_178715_c];
      float var1 = -3.5F;

      for(int var2 = 0; var2 < this.field_178713_d.length; ++var2) {
         this.field_178713_d[var2] = new ModelRenderer(this, field_178714_b[var2][0], field_178714_b[var2][1]);
         this.field_178713_d[var2].addBox((float)field_178716_a[var2][0] * -0.5F, 0.0F, (float)field_178716_a[var2][2] * -0.5F, field_178716_a[var2][0], field_178716_a[var2][1], field_178716_a[var2][2]);
         this.field_178713_d[var2].setRotationPoint(0.0F, (float)(24 - field_178716_a[var2][1]), var1);
         if (var2 < this.field_178713_d.length - 1) {
            var1 += (float)(field_178716_a[var2][2] + field_178716_a[var2 + 1][2]) * 0.5F;
         }
      }

   }
}
