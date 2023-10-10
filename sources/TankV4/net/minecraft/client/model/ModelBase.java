package net.minecraft.client.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public abstract class ModelBase {
   public boolean isRiding;
   public List boxList = Lists.newArrayList();
   public int textureHeight = 32;
   private Map modelTextureMap = Maps.newHashMap();
   public float swingProgress;
   public boolean isChild = true;
   public int textureWidth = 64;

   public void setModelAttributes(ModelBase var1) {
      this.swingProgress = var1.swingProgress;
      this.isRiding = var1.isRiding;
      this.isChild = var1.isChild;
   }

   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
   }

   public TextureOffset getTextureOffset(String var1) {
      return (TextureOffset)this.modelTextureMap.get(var1);
   }

   public void setLivingAnimations(EntityLivingBase var1, float var2, float var3, float var4) {
   }

   public ModelRenderer getRandomModelBox(Random var1) {
      return (ModelRenderer)this.boxList.get(var1.nextInt(this.boxList.size()));
   }

   protected void setTextureOffset(String var1, int var2, int var3) {
      this.modelTextureMap.put(var1, new TextureOffset(var2, var3));
   }

   public static void copyModelAngles(ModelRenderer var0, ModelRenderer var1) {
      var1.rotateAngleX = var0.rotateAngleX;
      var1.rotateAngleY = var0.rotateAngleY;
      var1.rotateAngleZ = var0.rotateAngleZ;
      var1.rotationPointX = var0.rotationPointX;
      var1.rotationPointY = var0.rotationPointY;
      var1.rotationPointZ = var0.rotationPointZ;
   }

   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
   }
}
