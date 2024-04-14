package de.violence.ui;

import de.violence.ui.Location3D;
import java.util.ArrayList;

public class Line3D {
   Location3D p1;
   Location3D p2;
   Location3D middle;
   double offSetX;
   double offSetY;
   double offSetZ;

   public Line3D(Location3D startLocation, Location3D endLocation) {
      this.p1 = startLocation;
      this.p2 = endLocation;
      this.initLinie();
   }

   public Line3D(Location3D startLocation, double yaw, double pitch, double lengh) {
      this.p1 = startLocation;
      double yoff = Math.sin(Math.toRadians(pitch));
      double y = yoff * lengh + startLocation.getY();
      double xoff = Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
      double zoff = Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
      double x = xoff * lengh + startLocation.getX();
      double z = zoff * lengh + startLocation.getZ();
      this.p2 = new Location3D(x, y, z);
      this.initLinie();
   }

   private void initLinie() {
      this.middle = this.calcCenter(this.p1, this.p2);
      this.offSetX = this.p2.getX() - this.p1.getX();
      this.offSetY = this.p2.getY() - this.p1.getY();
      this.offSetZ = this.p2.getZ() - this.p1.getZ();
   }

   private Location3D calcCenter(Location3D pp1, Location3D pp2) {
      double X = (pp1.getX() + pp2.getX()) / 2.0D;
      double Y = (pp1.getY() + pp2.getY()) / 2.0D;
      double Z = (pp1.getZ() + pp2.getZ()) / 2.0D;
      return new Location3D(X, Y, Z);
   }

   public ArrayList getPointsOn(int amount) {
      return this.calcPointsOn(amount);
   }

   public ArrayList getPointsOn(double interval) {
      int amount = (int)(this.p1.distance(this.p2) / interval) + 1;
      return this.calcPointsOn(amount);
   }

   private ArrayList calcPointsOn(int amount) {
      --amount;
      ArrayList points = new ArrayList();
      double xoff = this.getOffsetX() / (double)amount;
      double yoff = this.getOffsetY() / (double)amount;
      double zoff = this.getOffsetZ() / (double)amount;

      for(int i = 0; i <= amount; ++i) {
         double xoffset = xoff * (double)i;
         double yoffset = yoff * (double)i;
         double zoffset = zoff * (double)i;
         points.add(new Location3D(this.p1.getX() + xoffset, this.p1.getY() + yoffset, this.p1.getZ() + zoffset));
      }

      return points;
   }

   public Location3D getStart() {
      return this.p1;
   }

   public Location3D getEnd() {
      return this.p2;
   }

   public Location3D getCenter() {
      return this.middle;
   }

   public double getLengh() {
      return this.p1.distance(this.p2);
   }

   public double getOffsetX() {
      return this.offSetX;
   }

   public double getOffsetY() {
      return this.offSetY;
   }

   public double getOffsetZ() {
      return this.offSetZ;
   }
}
