package de.violence.ui;

import de.violence.ui.Location2D;
import de.violence.ui.Location3D;

public class BlickWinkel3D {
   public double yaw;
   public double pitch;

   public BlickWinkel3D(Location3D start, Location3D target) {
      double yawX = (new Location2D(start.getX(), start.getZ())).distance(new Location2D(target.getX(), target.getZ()));
      double yawY = target.getY() - start.getY();
      double YawrunterRechnen = (new Location2D(0.0D, 0.0D)).distance(new Location2D(yawX, yawY));
      double var10000 = yawX / YawrunterRechnen;
      yawY /= YawrunterRechnen;
      double yaw = -Math.toDegrees(Math.asin(yawY));
      double pitchX = target.getX() - start.getX();
      double pitchY = target.getZ() - start.getZ();
      double PitchrunterRechnen = (new Location2D(0.0D, 0.0D)).distance(new Location2D(pitchX, pitchY));
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
