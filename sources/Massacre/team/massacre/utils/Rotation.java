package team.massacre.utils;

public class Rotation {
   private float rotationYaw;
   private float rotationPitch;

   public Rotation(float rotationYaw, float rotationPitch) {
      this.rotationYaw = rotationYaw;
      this.rotationPitch = rotationPitch;
   }

   public float getRotationYaw() {
      return this.rotationYaw;
   }

   public float getRotationPitch() {
      return this.rotationPitch;
   }
}
