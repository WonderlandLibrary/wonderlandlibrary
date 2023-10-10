package net.minecraft.client.resources.model;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandom;

public class WeightedBakedModel implements IBakedModel {
   private final int totalWeight;
   private final List models;
   private final IBakedModel baseModel;

   public TextureAtlasSprite getParticleTexture() {
      return this.baseModel.getParticleTexture();
   }

   public WeightedBakedModel(List var1) {
      this.models = var1;
      this.totalWeight = WeightedRandom.getTotalWeight(var1);
      this.baseModel = ((WeightedBakedModel.MyWeighedRandomItem)var1.get(0)).model;
   }

   public boolean isGui3d() {
      return this.baseModel.isGui3d();
   }

   public ItemCameraTransforms getItemCameraTransforms() {
      return this.baseModel.getItemCameraTransforms();
   }

   public boolean isBuiltInRenderer() {
      return this.baseModel.isBuiltInRenderer();
   }

   public List getGeneralQuads() {
      return this.baseModel.getGeneralQuads();
   }

   public List getFaceQuads(EnumFacing var1) {
      return this.baseModel.getFaceQuads(var1);
   }

   public boolean isAmbientOcclusion() {
      return this.baseModel.isAmbientOcclusion();
   }

   public IBakedModel getAlternativeModel(long var1) {
      return ((WeightedBakedModel.MyWeighedRandomItem)WeightedRandom.getRandomItem(this.models, Math.abs((int)var1 >> 16) % this.totalWeight)).model;
   }

   static class MyWeighedRandomItem extends WeightedRandom.Item implements Comparable {
      protected final IBakedModel model;

      public int compareTo(WeightedBakedModel.MyWeighedRandomItem var1) {
         return ComparisonChain.start().compare(var1.itemWeight, this.itemWeight).compare(this.getCountQuads(), var1.getCountQuads()).result();
      }

      public int compareTo(Object var1) {
         return this.compareTo((WeightedBakedModel.MyWeighedRandomItem)var1);
      }

      public String toString() {
         return "MyWeighedRandomItem{weight=" + this.itemWeight + ", model=" + this.model + '}';
      }

      public MyWeighedRandomItem(IBakedModel var1, int var2) {
         super(var2);
         this.model = var1;
      }

      protected int getCountQuads() {
         int var1 = this.model.getGeneralQuads().size();
         EnumFacing[] var5;
         int var4 = (var5 = EnumFacing.values()).length;

         for(int var3 = 0; var3 < var4; ++var3) {
            EnumFacing var2 = var5[var3];
            var1 += this.model.getFaceQuads(var2).size();
         }

         return var1;
      }
   }

   public static class Builder {
      private List listItems = Lists.newArrayList();

      public WeightedBakedModel build() {
         Collections.sort(this.listItems);
         return new WeightedBakedModel(this.listItems);
      }

      public WeightedBakedModel.Builder add(IBakedModel var1, int var2) {
         this.listItems.add(new WeightedBakedModel.MyWeighedRandomItem(var1, var2));
         return this;
      }

      public IBakedModel first() {
         return ((WeightedBakedModel.MyWeighedRandomItem)this.listItems.get(0)).model;
      }
   }
}
