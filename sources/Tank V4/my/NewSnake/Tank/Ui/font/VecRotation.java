package my.NewSnake.Tank.Ui.font;

import net.minecraft.util.Vec3;

public class VecRotation {
   Vec3 vec3;
   Rotation rotation;

   public Rotation getRotation() {
      return this.rotation;
   }

   public Vec3 getVec3() {
      return this.vec3;
   }

   public VecRotation(Vec3 var1, Rotation var2) {
      this.vec3 = var1;
      this.rotation = var2;
   }
}
