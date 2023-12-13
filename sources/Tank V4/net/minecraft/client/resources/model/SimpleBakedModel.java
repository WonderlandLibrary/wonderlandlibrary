package net.minecraft.client.resources.model;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BreakingFour;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class SimpleBakedModel implements IBakedModel {
   protected final List faceQuads;
   protected final TextureAtlasSprite texture;
   protected final boolean gui3d;
   protected final ItemCameraTransforms cameraTransforms;
   protected final List generalQuads;
   protected final boolean ambientOcclusion;

   public SimpleBakedModel(List var1, List var2, boolean var3, boolean var4, TextureAtlasSprite var5, ItemCameraTransforms var6) {
      this.generalQuads = var1;
      this.faceQuads = var2;
      this.ambientOcclusion = var3;
      this.gui3d = var4;
      this.texture = var5;
      this.cameraTransforms = var6;
   }

   public boolean isAmbientOcclusion() {
      return this.ambientOcclusion;
   }

   public ItemCameraTransforms getItemCameraTransforms() {
      return this.cameraTransforms;
   }

   public List getFaceQuads(EnumFacing var1) {
      return (List)this.faceQuads.get(var1.ordinal());
   }

   public TextureAtlasSprite getParticleTexture() {
      return this.texture;
   }

   public boolean isBuiltInRenderer() {
      return false;
   }

   public boolean isGui3d() {
      return this.gui3d;
   }

   public List getGeneralQuads() {
      return this.generalQuads;
   }

   public static class Builder {
      private final List builderFaceQuads;
      private final List builderGeneralQuads;
      private final boolean builderAmbientOcclusion;
      private TextureAtlasSprite builderTexture;
      private ItemCameraTransforms builderCameraTransforms;
      private boolean builderGui3d;

      public Builder(ModelBlock var1) {
         this(var1.isAmbientOcclusion(), var1.isGui3d(), var1.func_181682_g());
      }

      public SimpleBakedModel.Builder addGeneralQuad(BakedQuad var1) {
         this.builderGeneralQuads.add(var1);
         return this;
      }

      public Builder(IBakedModel var1, TextureAtlasSprite var2) {
         this(var1.isAmbientOcclusion(), var1.isGui3d(), var1.getItemCameraTransforms());
         this.builderTexture = var1.getParticleTexture();
         EnumFacing[] var6;
         int var5 = (var6 = EnumFacing.values()).length;

         for(int var4 = 0; var4 < var5; ++var4) {
            EnumFacing var3 = var6[var4];
            this.addFaceBreakingFours(var1, var2, var3);
         }

         this.addGeneralBreakingFours(var1, var2);
      }

      public SimpleBakedModel.Builder setTexture(TextureAtlasSprite var1) {
         this.builderTexture = var1;
         return this;
      }

      private void addFaceBreakingFours(IBakedModel var1, TextureAtlasSprite var2, EnumFacing var3) {
         Iterator var5 = var1.getFaceQuads(var3).iterator();

         while(var5.hasNext()) {
            BakedQuad var4 = (BakedQuad)var5.next();
            this.addFaceQuad(var3, new BreakingFour(var4, var2));
         }

      }

      public IBakedModel makeBakedModel() {
         if (this.builderTexture == null) {
            throw new RuntimeException("Missing particle!");
         } else {
            return new SimpleBakedModel(this.builderGeneralQuads, this.builderFaceQuads, this.builderAmbientOcclusion, this.builderGui3d, this.builderTexture, this.builderCameraTransforms);
         }
      }

      public SimpleBakedModel.Builder addFaceQuad(EnumFacing var1, BakedQuad var2) {
         ((List)this.builderFaceQuads.get(var1.ordinal())).add(var2);
         return this;
      }

      private void addGeneralBreakingFours(IBakedModel var1, TextureAtlasSprite var2) {
         Iterator var4 = var1.getGeneralQuads().iterator();

         while(var4.hasNext()) {
            BakedQuad var3 = (BakedQuad)var4.next();
            this.addGeneralQuad(new BreakingFour(var3, var2));
         }

      }

      private Builder(boolean var1, boolean var2, ItemCameraTransforms var3) {
         this.builderGeneralQuads = Lists.newArrayList();
         this.builderFaceQuads = Lists.newArrayListWithCapacity(6);
         EnumFacing[] var7;
         int var6 = (var7 = EnumFacing.values()).length;

         for(int var5 = 0; var5 < var6; ++var5) {
            EnumFacing var10000 = var7[var5];
            this.builderFaceQuads.add(Lists.newArrayList());
         }

         this.builderAmbientOcclusion = var1;
         this.builderGui3d = var2;
         this.builderCameraTransforms = var3;
      }
   }
}
