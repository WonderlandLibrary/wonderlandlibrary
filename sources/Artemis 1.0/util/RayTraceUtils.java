package artemis.util;


public class RayTraceUtils {
	
	//by StaticCode
	
	
   public double yaw;
   public double pitch;

   public RayTraceUtils(BlockUtils start, BlockUtils target) {
      double yawX = (new BlockUtils2D(start.getX(), start.getZ())).distance(new BlockUtils2D(target.getX(), target.getZ()));
      double yawY = target.getY() - start.getY();
      double YawrunterRechnen = (new BlockUtils2D(0.0D, 0.0D)).distance(new BlockUtils2D(yawX, yawY));
      double var10000 = yawX / YawrunterRechnen;
      yawY /= YawrunterRechnen;
      double yaw = -Math.toDegrees(Math.asin(yawY));
      double pitchX = target.getX() - start.getX();
      double pitchY = target.getZ() - start.getZ();
      double PitchrunterRechnen = (new BlockUtils2D(0.0D, 0.0D)).distance(new BlockUtils2D(pitchX, pitchY));
      var10000 = pitchX / PitchrunterRechnen;
      pitchY /= PitchrunterRechnen;
      double pitch = Math.toDegrees(Math.asin(pitchY));
      pitch -= 90.0D;
      if(start.getX() > target.getX()) {
         pitch *= -1.0D;
      }

      this.yaw = yaw;
      this.pitch = pitch;
   }

   public double getPitch() {
      return this.yaw;
   }

   public double getYaw() {
      return this.pitch;
   }
}