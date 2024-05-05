package team.massacre.utils;

import java.util.ArrayList;
import net.minecraft.util.Vec3d;

public class PathHub {
   private Vec3d loc;
   private PathHub parentPathHub;
   private ArrayList<Vec3d> pathway;
   private double sqDist;
   private double currentCost;
   private double maxCost;

   public PathHub(Vec3d loc, PathHub parentPathHub, ArrayList<Vec3d> pathway, double sqDist, double currentCost, double maxCost) {
      this.loc = loc;
      this.parentPathHub = parentPathHub;
      this.pathway = pathway;
      this.sqDist = sqDist;
      this.currentCost = currentCost;
      this.maxCost = maxCost;
   }

   public Vec3d getLoc() {
      return this.loc;
   }

   public PathHub getLastHub() {
      return this.parentPathHub;
   }

   public ArrayList<Vec3d> getPathway() {
      return this.pathway;
   }

   public double getSqDist() {
      return this.sqDist;
   }

   public double getCurrentCost() {
      return this.currentCost;
   }

   public void setLoc(Vec3d loc) {
      this.loc = loc;
   }

   public void setParentPathHub(PathHub parentPathHub) {
      this.parentPathHub = parentPathHub;
   }

   public void setPathway(ArrayList<Vec3d> pathway) {
      this.pathway = pathway;
   }

   public void setSqDist(double sqDist) {
      this.sqDist = sqDist;
   }

   public void setCurrentCost(double currentCost) {
      this.currentCost = currentCost;
   }

   public double getMaxCost() {
      return this.maxCost;
   }

   public void setMaxCost(double maxCost) {
      this.maxCost = maxCost;
   }
}
