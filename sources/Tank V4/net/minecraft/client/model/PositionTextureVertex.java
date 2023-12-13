package net.minecraft.client.model;

import net.minecraft.util.Vec3;

public class PositionTextureVertex {
   public Vec3 vector3D;
   public float texturePositionX;
   public float texturePositionY;

   public PositionTextureVertex setTexturePosition(float var1, float var2) {
      return new PositionTextureVertex(this, var1, var2);
   }

   public PositionTextureVertex(PositionTextureVertex var1, float var2, float var3) {
      this.vector3D = var1.vector3D;
      this.texturePositionX = var2;
      this.texturePositionY = var3;
   }

   public PositionTextureVertex(float var1, float var2, float var3, float var4, float var5) {
      this(new Vec3((double)var1, (double)var2, (double)var3), var4, var5);
   }

   public PositionTextureVertex(Vec3 var1, float var2, float var3) {
      this.vector3D = var1;
      this.texturePositionX = var2;
      this.texturePositionY = var3;
   }
}
