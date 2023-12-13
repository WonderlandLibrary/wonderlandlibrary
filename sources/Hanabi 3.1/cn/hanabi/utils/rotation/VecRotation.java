package cn.hanabi.utils.rotation;

import net.minecraft.util.Vec3;

public class VecRotation {
   Vec3 vec3;
   Rotation rotation;

   public VecRotation(Vec3 Vec3, Rotation Rotation) {
      this.vec3 = Vec3;
      this.rotation = Rotation;
   }

   public Vec3 getVec3() {
      return this.vec3;
   }

   public Rotation getRotation() {
      return this.rotation;
   }
}
