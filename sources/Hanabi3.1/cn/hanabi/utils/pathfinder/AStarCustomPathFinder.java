package cn.hanabi.utils.pathfinder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockWall;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public class AStarCustomPathFinder {
   private static final Vec3[] flatCardinalDirections = new Vec3[]{new Vec3(1.0, 0.0, 0.0), new Vec3(-1.0, 0.0, 0.0), new Vec3(0.0, 0.0, 1.0), new Vec3(0.0, 0.0, -1.0)};
   private final Vec3 startVec3;
   private final Vec3 endVec3;
   private final ArrayList hubs = new ArrayList();
   private final ArrayList hubsToWork = new ArrayList();
   private ArrayList path = new ArrayList();

   public AStarCustomPathFinder(Vec3 startVec3, Vec3 endVec3) {
      this.startVec3 = startVec3.addVector(0.0, 0.0, 0.0).floor();
      this.endVec3 = endVec3.addVector(0.0, 0.0, 0.0).floor();
   }

   public static boolean checkPositionValidity(Vec3 loc, boolean checkGround) {
      return checkPositionValidity((int)loc.getX(), (int)loc.getY(), (int)loc.getZ(), checkGround);
   }

   public static boolean checkPositionValidity(int x, int y, int z, boolean checkGround) {
      BlockPos block1 = new BlockPos(x, y, z);
      BlockPos block2 = new BlockPos(x, y + 1, z);
      BlockPos block3 = new BlockPos(x, y - 1, z);
      return !isBlockSolid(block1) && !isBlockSolid(block2) && (isBlockSolid(block3) || !checkGround) && isSafeToWalkOn(block3);
   }

   private static boolean isBlockSolid(BlockPos block) {
      return Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock().isFullBlock() || Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockSlab || Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockStairs || Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockCactus || Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockChest || Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockEnderChest || Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockSkull || Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockPane || Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockFence || Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockWall || Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockGlass || Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockPistonBase || Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockPistonExtension || Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockPistonMoving || Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockStainedGlass || Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockTrapDoor;
   }

   private static boolean isSafeToWalkOn(BlockPos block) {
      return !(Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockFence) && !(Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockWall);
   }

   public ArrayList getPath() {
      return this.path;
   }

   public void compute() {
      this.compute(1000, 4);
   }

   public void compute(int loops, int depth) {
      this.path.clear();
      this.hubsToWork.clear();
      ArrayList initPath = new ArrayList();
      initPath.add(this.startVec3);
      this.hubsToWork.add(new Hub(this.startVec3, (Hub)null, initPath, this.startVec3.squareDistanceTo(this.endVec3), 0.0, 0.0));

      label58:
      for(int i = 0; i < loops; ++i) {
         this.hubsToWork.sort(new CompareHub());
         int j = 0;
         if (this.hubsToWork.size() == 0) {
            break;
         }

         Iterator var6 = (new ArrayList(this.hubsToWork)).iterator();

         while(var6.hasNext()) {
            Hub hub = (Hub)var6.next();
            ++j;
            if (j > depth) {
               break;
            }

            this.hubsToWork.remove(hub);
            this.hubs.add(hub);
            Vec3[] var8 = flatCardinalDirections;
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               Vec3 direction = var8[var10];
               Vec3 loc = hub.getLoc().add(direction).floor();
               if (checkPositionValidity(loc, false) && this.addHub(hub, loc, 0.0)) {
                  break label58;
               }
            }

            Vec3 loc1 = hub.getLoc().addVector(0.0, 1.0, 0.0).floor();
            if (checkPositionValidity(loc1, false) && this.addHub(hub, loc1, 0.0)) {
               break label58;
            }

            Vec3 loc2 = hub.getLoc().addVector(0.0, -1.0, 0.0).floor();
            if (checkPositionValidity(loc2, false) && this.addHub(hub, loc2, 0.0)) {
               break label58;
            }
         }
      }

      boolean nearest = true;
      if (nearest) {
         this.hubs.sort(new CompareHub());
         this.path = ((Hub)this.hubs.get(0)).getPath();
      }

   }

   public Hub isHubExisting(Vec3 loc) {
      Iterator var2 = this.hubs.iterator();

      Hub hub;
      do {
         if (!var2.hasNext()) {
            var2 = this.hubsToWork.iterator();

            do {
               if (!var2.hasNext()) {
                  return null;
               }

               hub = (Hub)var2.next();
            } while(hub.getLoc().getX() != loc.getX() || hub.getLoc().getY() != loc.getY() || hub.getLoc().getZ() != loc.getZ());

            return hub;
         }

         hub = (Hub)var2.next();
      } while(hub.getLoc().getX() != loc.getX() || hub.getLoc().getY() != loc.getY() || hub.getLoc().getZ() != loc.getZ());

      return hub;
   }

   public boolean addHub(Hub parent, Vec3 loc, double cost) {
      Hub existingHub = this.isHubExisting(loc);
      double totalCost = cost;
      if (parent != null) {
         totalCost = cost + parent.getTotalCost();
      }

      if (existingHub == null) {
         double minDistanceSquared = 9.0;
         if (loc.getX() == this.endVec3.getX() && loc.getY() == this.endVec3.getY() && loc.getZ() == this.endVec3.getZ() || minDistanceSquared != 0.0 && loc.squareDistanceTo(this.endVec3) <= minDistanceSquared) {
            this.path.clear();
            this.path = parent.getPath();
            this.path.add(loc);
            return true;
         }

         ArrayList path = new ArrayList(parent.getPath());
         path.add(loc);
         this.hubsToWork.add(new Hub(loc, parent, path, loc.squareDistanceTo(this.endVec3), cost, totalCost));
      } else if (existingHub.getCost() > cost) {
         ArrayList path = new ArrayList(parent.getPath());
         path.add(loc);
         existingHub.setLoc(loc);
         existingHub.setParent(parent);
         existingHub.setPath(path);
         existingHub.setSquareDistanceToFromTarget(loc.squareDistanceTo(this.endVec3));
         existingHub.setCost(cost);
         existingHub.setTotalCost(totalCost);
      }

      return false;
   }

   public static class CompareHub implements Comparator {
      public int compare(Hub o1, Hub o2) {
         return (int)(o1.getSquareDistanceToFromTarget() + o1.getTotalCost() - (o2.getSquareDistanceToFromTarget() + o2.getTotalCost()));
      }
   }

   private static class Hub {
      private Vec3 loc = null;
      private Hub parent = null;
      private ArrayList path;
      private double squareDistanceToFromTarget;
      private double cost;
      private double totalCost;

      public Hub(Vec3 loc, Hub parent, ArrayList path, double squareDistanceToFromTarget, double cost, double totalCost) {
         this.loc = loc;
         this.parent = parent;
         this.path = path;
         this.squareDistanceToFromTarget = squareDistanceToFromTarget;
         this.cost = cost;
         this.totalCost = totalCost;
      }

      public Vec3 getLoc() {
         return this.loc;
      }

      public void setLoc(Vec3 loc) {
         this.loc = loc;
      }

      public Hub getParent() {
         return this.parent;
      }

      public void setParent(Hub parent) {
         this.parent = parent;
      }

      public ArrayList getPath() {
         return this.path;
      }

      public void setPath(ArrayList path) {
         this.path = path;
      }

      public double getSquareDistanceToFromTarget() {
         return this.squareDistanceToFromTarget;
      }

      public void setSquareDistanceToFromTarget(double squareDistanceToFromTarget) {
         this.squareDistanceToFromTarget = squareDistanceToFromTarget;
      }

      public double getCost() {
         return this.cost;
      }

      public void setCost(double cost) {
         this.cost = cost;
      }

      public double getTotalCost() {
         return this.totalCost;
      }

      public void setTotalCost(double totalCost) {
         this.totalCost = totalCost;
      }
   }
}
