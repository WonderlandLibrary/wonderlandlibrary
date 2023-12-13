package com.sun.javafx.sg.prism;

import com.sun.prism.Image;
import com.sun.prism.Material;
import com.sun.prism.PhongMaterial;
import com.sun.prism.ResourceFactory;
import com.sun.prism.TextureMap;
import com.sun.prism.paint.Color;

public class NGPhongMaterial {
   private static final Image WHITE_1X1 = Image.fromIntArgbPreData((int[])(new int[]{-1}), 1, 1);
   private PhongMaterial material;
   private Color diffuseColor;
   private boolean diffuseColorDirty = true;
   private TextureMap diffuseMap;
   private Color specularColor;
   private boolean specularColorDirty;
   private float specularPower;
   private boolean specularPowerDirty;
   private TextureMap specularMap;
   private TextureMap bumpMap;
   private TextureMap selfIllumMap;

   public NGPhongMaterial() {
      this.diffuseMap = new TextureMap(PhongMaterial.MapType.DIFFUSE);
      this.specularColorDirty = true;
      this.specularPowerDirty = true;
      this.specularMap = new TextureMap(PhongMaterial.MapType.SPECULAR);
      this.bumpMap = new TextureMap(PhongMaterial.MapType.BUMP);
      this.selfIllumMap = new TextureMap(PhongMaterial.MapType.SELF_ILLUM);
   }

   Material createMaterial(ResourceFactory var1) {
      if (this.material == null) {
         this.material = var1.createPhongMaterial();
      }

      this.validate(var1);
      return this.material;
   }

   private void validate(ResourceFactory var1) {
      if (this.diffuseColorDirty) {
         if (this.diffuseColor != null) {
            this.material.setDiffuseColor(this.diffuseColor.getRed(), this.diffuseColor.getGreen(), this.diffuseColor.getBlue(), this.diffuseColor.getAlpha());
         } else {
            this.material.setDiffuseColor(0.0F, 0.0F, 0.0F, 0.0F);
         }

         this.diffuseColorDirty = false;
      }

      if (this.diffuseMap.isDirty()) {
         if (this.diffuseMap.getImage() == null) {
            this.diffuseMap.setImage(WHITE_1X1);
         }

         this.material.setTextureMap(this.diffuseMap);
      }

      if (this.bumpMap.isDirty()) {
         this.material.setTextureMap(this.bumpMap);
      }

      if (this.selfIllumMap.isDirty()) {
         this.material.setTextureMap(this.selfIllumMap);
      }

      if (this.specularMap.isDirty()) {
         this.material.setTextureMap(this.specularMap);
      }

      if (this.specularColorDirty || this.specularPowerDirty) {
         if (this.specularColor != null) {
            float var2 = this.specularColor.getRed();
            float var3 = this.specularColor.getGreen();
            float var4 = this.specularColor.getBlue();
            this.material.setSpecularColor(true, var2, var3, var4, this.specularPower);
         } else {
            this.material.setSpecularColor(false, 1.0F, 1.0F, 1.0F, this.specularPower);
         }

         this.specularColorDirty = false;
         this.specularPowerDirty = false;
      }

   }

   public void setDiffuseColor(Object var1) {
      this.diffuseColor = (Color)var1;
      this.diffuseColorDirty = true;
   }

   public void setSpecularColor(Object var1) {
      this.specularColor = (Color)var1;
      this.specularColorDirty = true;
   }

   public void setSpecularPower(float var1) {
      if (var1 < 0.001F) {
         var1 = 0.001F;
      }

      this.specularPower = var1;
      this.specularPowerDirty = true;
   }

   public void setDiffuseMap(Object var1) {
      this.diffuseMap.setImage((Image)var1);
      this.diffuseMap.setDirty(true);
   }

   public void setSpecularMap(Object var1) {
      this.specularMap.setImage((Image)var1);
      this.specularMap.setDirty(true);
   }

   public void setBumpMap(Object var1) {
      this.bumpMap.setImage((Image)var1);
      this.bumpMap.setDirty(true);
   }

   public void setSelfIllumMap(Object var1) {
      this.selfIllumMap.setImage((Image)var1);
      this.selfIllumMap.setDirty(true);
   }

   Color test_getDiffuseColor() {
      return this.diffuseColor;
   }
}
