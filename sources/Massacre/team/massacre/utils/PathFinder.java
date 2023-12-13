package team.massacre.utils;

import java.util.ArrayList;
import java.util.Collections;
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
import net.minecraft.util.Vec3d;

public class PathFinder {
   private final Vec3d startVec3d;
   private final Vec3d endVec3d;
   private ArrayList<Vec3d> path = new ArrayList();
   private final ArrayList<PathHub> pathHubs = new ArrayList();
   private final ArrayList<PathHub> workingPathHubList = new ArrayList();
   private final double minDistanceSquared = 9.0D;
   private final boolean nearest = true;
   private static final Vec3d[] directions = new Vec3d[]{new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(0.0D, 0.0D, -1.0D)};

   public PathFinder(Vec3d startVec3d, Vec3d endVec3d) {
      this.startVec3d = startVec3d.addVector(0.0D, 0.0D, 0.0D).normalize();
      this.endVec3d = endVec3d.addVector(0.0D, 0.0D, 0.0D).normalize();
   }

   public ArrayList<Vec3d> getPath() {
      return this.path;
   }

   public void compute() {
      this.compute(1000, 4);
   }

   public void compute(int loops, int depth) {
      this.path.clear();
      this.workingPathHubList.clear();
      ArrayList<Vec3d> initPath = new ArrayList();
      initPath.add(this.startVec3d);
      this.workingPathHubList.add(new PathHub(this.startVec3d, (PathHub)null, initPath, this.startVec3d.squareDistanceTo(this.endVec3d), 0.0D, 0.0D));

      label53:
      for(int i = 0; i < loops; ++i) {
         Collections.sort(this.workingPathHubList, new PathFinder.CompareHub());
         int j = 0;
         if (this.workingPathHubList.size() == 0) {
            break;
         }

         Iterator var6 = (new ArrayList(this.workingPathHubList)).iterator();

         while(var6.hasNext()) {
            PathHub pathHub = (PathHub)var6.next();
            ++j;
            if (j > depth) {
               break;
            }

            this.workingPathHubList.remove(pathHub);
            this.pathHubs.add(pathHub);
            Vec3d[] var9 = directions;
            int var10 = var9.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               Vec3d direction = var9[var11];
               Vec3d loc = pathHub.getLoc().add(direction).normalize();
               if (isValid(loc, false) && this.putHub(pathHub, loc, 0.0D)) {
                  break label53;
               }
            }

            Vec3d loc1 = pathHub.getLoc().addVector(0.0D, 1.0D, 0.0D).normalize();
            Vec3d loc2;
            if (isValid(loc1, false) && this.putHub(pathHub, loc1, 0.0D) || isValid(loc2 = pathHub.getLoc().addVector(0.0D, -1.0D, 0.0D).normalize(), false) && this.putHub(pathHub, loc2, 0.0D)) {
               break label53;
            }
         }
      }

      Collections.sort(this.pathHubs, new PathFinder.CompareHub());
      this.path = ((PathHub)this.pathHubs.get(0)).getPathway();
   }

   public static boolean isValid(Vec3d loc, boolean checkGround) {
      return isValid((int)loc.getX(), (int)loc.getY(), (int)loc.getZ(), checkGround);
   }

   public static boolean isValid(int x, int y, int z, boolean checkGround) {
      BlockPos block1 = new BlockPos(x, y, z);
      BlockPos block2 = new BlockPos(x, y + 1, z);
      BlockPos block3 = new BlockPos(x, y - 1, z);
      return !isNotPassable(block1) && !isNotPassable(block2) && (isNotPassable(block3) || !checkGround) && canWalkOn(block3);
   }

   private static boolean isNotPassable(BlockPos block) {
      return Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(), block.getZ()).isFullCube() || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(), block.getZ()) instanceof BlockSlab || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(), block.getZ()) instanceof BlockStairs || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(), block.getZ()) instanceof BlockCactus || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(), block.getZ()) instanceof BlockChest || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(), block.getZ()) instanceof BlockEnderChest || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(), block.getZ()) instanceof BlockSkull || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(), block.getZ()) instanceof BlockPane || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(), block.getZ()) instanceof BlockFence || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(), block.getZ()) instanceof BlockWall || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(), block.getZ()) instanceof BlockGlass || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(), block.getZ()) instanceof BlockPistonBase || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(), block.getZ()) instanceof BlockPistonExtension || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(), block.getZ()) instanceof BlockPistonMoving || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(), block.getZ()) instanceof BlockStainedGlass || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(), block.getZ()) instanceof BlockTrapDoor;
   }

   private static boolean canWalkOn(BlockPos block) {
      return !(Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(), block.getZ()) instanceof BlockFence) && !(Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(), block.getZ()) instanceof BlockWall);
   }

   public PathHub doesHubExistAt(Vec3d loc) {
      Iterator var2 = this.pathHubs.iterator();

      PathHub pathHub;
      do {
         if (!var2.hasNext()) {
            var2 = this.workingPathHubList.iterator();

            do {
               if (!var2.hasNext()) {
                  return null;
               }

               pathHub = (PathHub)var2.next();
            } while(pathHub.getLoc().getX() != loc.getX() || pathHub.getLoc().getY() != loc.getY() || pathHub.getLoc().getZ() != loc.getZ());

            return pathHub;
         }

         pathHub = (PathHub)var2.next();
      } while(pathHub.getLoc().getX() != loc.getX() || pathHub.getLoc().getY() != loc.getY() || pathHub.getLoc().getZ() != loc.getZ());

      return pathHub;
   }

   public boolean putHub(PathHub parent, Vec3d loc, double cost) {
      PathHub existingPathHub = this.doesHubExistAt(loc);
      double totalCost = cost;
      if (parent != null) {
         totalCost = cost + parent.getMaxCost();
      }

      ArrayList path;
      if (existingPathHub == null) {
         if (loc.getX() == this.endVec3d.getX() && loc.getY() == this.endVec3d.getY() && loc.getZ() == this.endVec3d.getZ() || loc.squareDistanceTo(this.endVec3d) <= 9.0D) {
            this.path.clear();
            this.path = parent.getPathway();
            this.path.add(loc);
            return true;
         }

         path = new ArrayList(parent.getPathway());
         path.add(loc);
         this.workingPathHubList.add(new PathHub(loc, parent, path, loc.squareDistanceTo(this.endVec3d), cost, totalCost));
      } else if (existingPathHub.getCurrentCost() > cost) {
         path = new ArrayList(parent.getPathway());
         path.add(loc);
         existingPathHub.setLoc(loc);
         existingPathHub.setParentPathHub(parent);
         existingPathHub.setPathway(path);
         existingPathHub.setSqDist(loc.squareDistanceTo(this.endVec3d));
         existingPathHub.setCurrentCost(cost);
         existingPathHub.setMaxCost(totalCost);
      }

      return false;
   }

   public class CompareHub implements Comparator<PathHub> {
      public int compare(PathHub o1, PathHub o2) {
         return (int)(o1.getSqDist() + o1.getMaxCost() - (o2.getSqDist() + o2.getMaxCost()));
      }
   }
}
