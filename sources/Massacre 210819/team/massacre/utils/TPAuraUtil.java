package team.massacre.utils;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3d;

public class TPAuraUtil {
   public static ArrayList<Vec3d> computePath(Vec3d topFrom, Vec3d to) {
      topFrom = topFrom.addVector(0.0D, 1.0D, 0.0D);
      PathFinder pathfinder = new PathFinder(topFrom, to);
      pathfinder.compute();
      int i = 0;
      Vec3d lastLoc = null;
      Vec3d lastDashLoc = null;
      ArrayList<Vec3d> path = new ArrayList();
      ArrayList<Vec3d> pathFinderPath = pathfinder.getPath();

      for(Iterator var8 = pathFinderPath.iterator(); var8.hasNext(); ++i) {
         Vec3d pathElm = (Vec3d)var8.next();
         if (i != 0 && i != pathFinderPath.size() - 1) {
            boolean canContinue = true;
            if (pathElm.squareDistanceTo(lastDashLoc) > 25.0D) {
               canContinue = false;
            } else {
               double smallX = Math.min(lastDashLoc.getX(), pathElm.getX());
               double smallY = Math.min(lastDashLoc.getY(), pathElm.getY());
               double smallZ = Math.min(lastDashLoc.getZ(), pathElm.getZ());
               double bigX = Math.max(lastDashLoc.getX(), pathElm.getX());
               double bigY = Math.max(lastDashLoc.getY(), pathElm.getY());
               double bigZ = Math.max(lastDashLoc.getZ(), pathElm.getZ());

               label50:
               for(int x = (int)smallX; (double)x <= bigX; ++x) {
                  for(int y2 = (int)smallY; (double)y2 <= bigY; ++y2) {
                     for(int z = (int)smallZ; (double)z <= bigZ; ++z) {
                        if (!PathFinder.isValid(x, y2, z, false)) {
                           canContinue = false;
                           break label50;
                        }
                     }
                  }
               }
            }

            if (!canContinue) {
               path.add(lastLoc.addVector(0.5D, 0.0D, 0.5D));
               lastDashLoc = lastLoc;
            }
         } else {
            if (lastLoc != null) {
               path.add(lastLoc.addVector(0.5D, 0.0D, 0.5D));
            }

            path.add(pathElm.addVector(0.5D, 0.0D, 0.5D));
            lastDashLoc = pathElm;
         }

         lastLoc = pathElm;
      }

      return path;
   }

   private static boolean canPassThrough(BlockPos pos) {
      Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).getBlock();
      return block.getMaterial() == Material.air || block.getMaterial() == Material.plants || block.getMaterial() == Material.vine || block == Blocks.ladder || block == Blocks.water || block == Blocks.flowing_water || block == Blocks.wall_sign || block == Blocks.standing_sign;
   }
}
